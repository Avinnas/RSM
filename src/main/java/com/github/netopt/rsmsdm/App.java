package com.github.netopt.rsmsdm;

import com.github.netopt.rsmsdm.experiment.PropertiesReadingException;
import com.github.netopt.rsmsdm.experiment.ScenarioRunner;
import com.github.netopt.rsmsdm.gui.MainWindow;
import com.github.netopt.rsmsdm.network.NetworkFilesReadingException;
import javafx.application.Application;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author plechowicz
 * created on 21.02.19.
 */
public class App {

	public static void main(String[] args) throws NetworkFilesReadingException, PropertiesReadingException, IOException, CmdLineException {
		new App(args);
	}

	public App(String[] args) throws NetworkFilesReadingException, IOException, PropertiesReadingException, CmdLineException {
		AlgorithmsConfig.registerAlgorithms(); // register algorithms
		// if application called without any arguments - start gui
		if (args.length == 0) {
			Application.launch(MainWindow.class);
		}

		AppCmdLineInput cmdline = new AppCmdLineInput();
		CmdLineParser parser = new CmdLineParser(cmdline);
		parser.parseArgument(args);
		if (cmdline.getGui()) {
			Application.launch(MainWindow.class, args);
		} else {
			if (cmdline.getScenarioFile() != null) {
				ScenarioRunner runner = new ScenarioRunner();
				runner.run(cmdline.getScenarioFile(), new ArrayList<>());
			}
		}
	}
}
