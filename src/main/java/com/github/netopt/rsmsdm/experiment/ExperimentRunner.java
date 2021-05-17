package com.github.netopt.rsmsdm.experiment;

import com.github.netopt.rsmsdm.algorithm.Algorithm;
import com.github.netopt.rsmsdm.algorithm.AlgorithmsRepository;
import com.github.netopt.rsmsdm.network.*;
import com.github.netopt.rsmsdm.spectrum.Spectrum;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Runs experiment based on provided {@link ExperimentProperties}
 *
 * @author plechowicz
 * created on 20.02.19.
 */
public class ExperimentRunner {

	public Result run(ExperimentProperties experimentProperties, Properties algorithmProperties) throws NetworkFilesReadingException, IOException {
		// create graph
		Graph graph = GraphFactory.createFromFile(experimentProperties.getNetworkFile());
		List<Demand> demands = DemandsFactory.createFromFile(experimentProperties.getDemandsDirectory() + experimentProperties.getDemandsFile(),
				experimentProperties.getCandidatePathsFile(),
				experimentProperties.getSlotsForCandidatePathsFile(),
				experimentProperties.getSummaryBitrate(),
				experimentProperties.getCandidatePaths(),
				experimentProperties.getMaxCandidatePaths(),
				graph
		);

		// create spectrum resources
		Spectrum spectrum = new Spectrum(graph.getEdges(), experimentProperties.getCores());
		Spectrum spectrumCopy = spectrum.deepCopy();
		// get algorithm from algorithms repository
		Algorithm algorithm = AlgorithmsRepository.getOrElseThrow(experimentProperties.getAlgorithm());
		// invoke it
		algorithm.execute(spectrumCopy, demands, experimentProperties, algorithmProperties);
		// validate solution
		// TODO: finish validating solution
		for (var demand : demands) {
			spectrum.allocate(demand.getChannel());
		}
		System.out.println(spectrum.getSpectrumCost());

		// store result
		Result result = Result.newBuilder().setAlgorithmProperties(algorithmProperties)
				.setExperimentProperties(experimentProperties)
				.setSpectrum(spectrum)
				.setTimeInNanos(algorithm.getTime(TimeUnit.NANOSECONDS)).build();
		return result;
	}

	public List<Result> run(String experimentPropertiesPath, String algorithmPropertiesPath) throws PropertiesReadingException, IOException, NetworkFilesReadingException {
		List<ExperimentProperties> experimentProperties = ExperimentPropertiesParser.readFromFile(experimentPropertiesPath);
		List<Result> results = new ArrayList<>();
		Properties algorithmProperties = new Properties();
		if (algorithmPropertiesPath != null) {
			algorithmProperties.load(new FileInputStream(algorithmPropertiesPath));
		}
		// validate experiment properties

		ExperimentPropertiesValidator validator = new ExperimentPropertiesValidator();
		for (var exProp : experimentProperties) {
			ExperimentPropertiesValidator.ValidationResult validationResult = validator.validate(exProp);
			if (!validationResult.success) {
				throw new PropertiesReadingException(validationResult.message);
			}
		}

		ExperimentRunner runner = new ExperimentRunner();
		for (ExperimentProperties experimentProperty : experimentProperties) {
			Result result = runner.run(experimentProperty, algorithmProperties);
			result = Result.copyResult(result).setExperimentPropertiesFile(experimentPropertiesPath)
					.setAlgorithmPropertiesFile(algorithmPropertiesPath).build();
			results.add(result);
		}
		return results;
	}
}
