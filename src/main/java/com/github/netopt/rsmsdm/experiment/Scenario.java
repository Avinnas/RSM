package com.github.netopt.rsmsdm.experiment;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.Arrays;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class Scenario {

	@Option(name = "-ep", aliases = {"--experiment-properties", "--exp-prop"}, required = true, usage = "path to experiment properties")
	private String experimentPropertiesPath;

	@Option(name = "-ap", aliases = {"--algorithm-properties", "--alg-prop"}, required = false, usage = "path to algorithm properties")
	private String algorithmPropertiesPath;

	public Scenario() {
	}

	public Scenario(String experimentPropertiesPath, String algorithmPropertiesPath) {
		this.experimentPropertiesPath = experimentPropertiesPath;
		this.algorithmPropertiesPath = algorithmPropertiesPath;
	}

	public String getExperimentPropertiesPath() {
		return experimentPropertiesPath;
	}

	public String getAlgorithmPropertiesPath() {
		return algorithmPropertiesPath;
	}

	public static Scenario parseFromArgs(String[] args) throws PropertiesReadingException {
		Scenario scenario = new Scenario();
		CmdLineParser parser = new CmdLineParser(scenario);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			throw new PropertiesReadingException("Exception in reading scenario " + Arrays.toString(args), e);
		}
		return scenario;
	}
}
