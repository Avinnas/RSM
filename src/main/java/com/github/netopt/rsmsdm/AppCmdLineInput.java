package com.github.netopt.rsmsdm;

import org.kohsuke.args4j.Option;

/**
 * @author plechowicz
 * created on 23.02.19.
 */
public class AppCmdLineInput {

	@Option(name = "-gui")
	private Boolean gui = false;

	@Option(name = "-sf", aliases = {"--scenario-file"})
	private String scenarioFile;

	public Boolean getGui() {
		return gui;
	}

	public String getScenarioFile() {
		return scenarioFile;
	}
}
