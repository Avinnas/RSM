package com.github.netopt.rsmsdm.experiment;

import com.github.netopt.rsmsdm.AppConfig;
import com.github.netopt.rsmsdm.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public final class ScenarioParser {

	public static List<Scenario> readScenariosFromFile(String path) throws PropertiesReadingException {
		List<Scenario> scenarios = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File(path))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (!line.startsWith("!") && !line.startsWith("#")) {
					scenarios.add(Scenario.parseFromArgs(line.split(" ")));
				}
			}
		} catch (IOException e) {
			throw new PropertiesReadingException(String.format("Exception in reading file={%s}", path), e);
		}
		return scenarios;
	}

	public static void writeScenariosToFile(List<Scenario> scenarios, String path) throws PropertiesWritingException {
		try (PrintWriter printWriter = new PrintWriter(new FileWriter(path))) {
			for (var scenario : scenarios) {
				printWriter.print("-ep");
				printWriter.print(" ");
				printWriter.print(scenario.getExperimentPropertiesPath());
				if (scenario.getAlgorithmPropertiesPath() != null && !scenario.getAlgorithmPropertiesPath().isBlank()) {
					printWriter.print(" ");
					printWriter.print("-ap");
					printWriter.print(" ");
					printWriter.print(scenario.getAlgorithmPropertiesPath());
				}
				printWriter.println();
			}
		} catch (IOException e) {
			throw new PropertiesWritingException(String.format("Error in writing properties file={%s}", path), e);
		}
	}

	public static void writeScenariosToFileBasedOnExperimentProperties(List<ExperimentProperties> experimentProperties, String algorithmPropertiesPath) throws PropertiesWritingException, IOException {
		String folder = AppConfig.Directories.SCENARIOS_OUTPUT_FOLDER + FileUtils.getNameBasedOnDateAndTime() + "/";
		File file = new File(folder);
		file.mkdirs();
		List<String> experimentPropertiesPaths = ExperimentPropertiesParser.writeMultipleFilesToFolder(experimentProperties, folder);

		String newAlgorithmPropertiesPath = null;
		if (algorithmPropertiesPath != null && !algorithmPropertiesPath.isBlank()) {
			newAlgorithmPropertiesPath = folder + Path.of(algorithmPropertiesPath).getFileName();
			Files.copy(Path.of(algorithmPropertiesPath), Path.of(newAlgorithmPropertiesPath));
		}

		List<Scenario> scenarios = new ArrayList<>();
		for (String ep : experimentPropertiesPaths) {
			scenarios.add(new Scenario(ep, newAlgorithmPropertiesPath));
		}
		writeScenariosToFile(scenarios, folder + "/" + "000__scenarios.txt");
	}
}
