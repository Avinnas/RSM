package com.github.netopt.rsmsdm.experiment;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class ScenarioIT {

	private String relativePath = "./experiments-data/scenario.txt";
	private String path = getClass().getClassLoader().getResource(relativePath).getPath();
	private String relativePathOutput = "./experiments-data/test-out-scenario.txt";
	private String pathRoot = "./src/test/resources/";


	@Test
	public void canReadScenarios() throws PropertiesReadingException {
		List<Scenario> scenarios = ScenarioParser.readScenariosFromFile(path);
		scenarios.forEach(s -> System.out.println(s.getExperimentPropertiesPath() + " " + s.getAlgorithmPropertiesPath()));
	}

	@Test
	public void canWriteScenarios() throws PropertiesWritingException {
		List<Scenario> scenarios = new ArrayList<>();
		scenarios.add(new Scenario("path1", "path2"));
		scenarios.add(new Scenario("path3", "path4"));
		scenarios.add(new Scenario("path5", null));
		scenarios.add(new Scenario("path6", " "));
		ScenarioParser.writeScenariosToFile(scenarios, pathRoot + relativePathOutput);
	}

	@Test
	public void canWriteScenariosBasedOnExperimentFiles() throws PropertiesWritingException, IOException {
		List<ExperimentProperties> experimentProperties = ExperimentProperties.newCartesianProductBuilder()
				.setAlgorithm("FirstFit")
				.setCores(Arrays.asList(1, 2, 3, 4, 5))
				.setCandidatePaths(Arrays.asList(10, 20))
				.setDemandsFile(Arrays.asList("10", "20"))
				.setSummaryBitrate(Arrays.asList(100L, 200L, 300L))
				.build();
		ScenarioParser.writeScenariosToFileBasedOnExperimentProperties(experimentProperties, "./src/test/resources/experiments-data/algorithm.properties");
	}
}