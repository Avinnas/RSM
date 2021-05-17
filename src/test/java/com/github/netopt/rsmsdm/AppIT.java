package com.github.netopt.rsmsdm;

import com.github.netopt.rsmsdm.experiment.PropertiesReadingException;
import com.github.netopt.rsmsdm.network.NetworkFilesReadingException;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;

/**
 * @author plechowicz
 * created on 23.02.19.
 */
public class AppIT {

	@Test
	public void run() throws NetworkFilesReadingException, PropertiesReadingException, IOException, CmdLineException {
		String scenarioFile = getClass().getClassLoader().getResource("experiments-data/scenario.txt").getPath();
		String[] args = {
				"-gui", "-sf", scenarioFile
		};
		App app = new App(args);
	}
}
