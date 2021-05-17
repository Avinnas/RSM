package com.github.netopt.rsmsdm.experiment;

import org.junit.Test;

import java.util.List;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class ExperimentPropertiesParserIT {
	private String relativePath = "./experiments-data/experiment.properties";
	private String relativePathOutput = "./experiments-data/test-out-experiment.properties";
	private String path = getClass().getClassLoader().getResource(relativePath).getPath();
	private String pathRoot = "./src/test/resources/";

	@Test
	public void canReadProperties() throws PropertiesReadingException {
		List<ExperimentProperties> experimentProperties = ExperimentPropertiesParser.readFromFile(path);
		System.out.println(experimentProperties);
	}

	@Test
	public void canWriteProperties() throws PropertiesWritingException {
		ExperimentProperties ep = ExperimentProperties.newBuilder().setCandidatePathsFile("can")
				.setCandidatePaths(10)
				.setMaxCandidatePaths(30)
				.setSlotsForCandidatePathsFile("slots")
				.setDemandsFile("dem")
				.setNetworkFile("net").build();
		ExperimentPropertiesParser.writeToFile(ep, pathRoot + relativePathOutput);
	}
}