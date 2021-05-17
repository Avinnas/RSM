package com.github.netopt.rsmsdm.experiment;

import com.github.netopt.rsmsdm.network.NetworkFilesReadingException;

import java.io.IOException;
import java.util.List;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class ScenarioRunner {

	public void run(String scenarioPath, List<Result> resultList) throws PropertiesReadingException, IOException, NetworkFilesReadingException {
		ResultSaver resultSaver = new ResultSaver(Result.getResultHeaderAsListOfStrings());
		List<Scenario> scenarios = ScenarioParser.readScenariosFromFile(scenarioPath);
		ExperimentRunner experimentRunner = new ExperimentRunner();
		for (var scenario : scenarios) {
			List<Result> results = experimentRunner.run(scenario.getExperimentPropertiesPath(), scenario.getAlgorithmPropertiesPath());
			for (Result r : results) {
				resultSaver.saveLine(r.toListOfString());
			}
			resultList.addAll(results);
		}
	}

//	public static void main(String[] args) throws NetworkFilesReadingException, IOException, PropertiesReadingException {
////		AlgorithmsRepository.init();
////		ScenarioRunner scenarioRunner = new ScenarioRunner();
////		scenarioRunner.run("./src/test/resources/experiments-data/scenario.txt");
//	}
}
