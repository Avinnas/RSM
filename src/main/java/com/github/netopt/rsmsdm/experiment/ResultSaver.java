package com.github.netopt.rsmsdm.experiment;

import com.github.netopt.rsmsdm.AppConfig;
import com.github.netopt.rsmsdm.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class ResultSaver {

	/**
	 * File columns delimiter
	 */
	private static final String DELIMITER = ",";

	private final File file;

	public ResultSaver(List<String> resultHeader) throws IOException {
		String folder = AppConfig.Directories.RESULTS_OUTPUT_FOLDER + "/";
		file = new File(folder + FileUtils.getNameBasedOnDateAndTime() + "-results.csv");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
			writer.println(StringUtils.join(resultHeader, DELIMITER));
		}
	}

	public void saveLine(List<String> resultRow) throws IOException {
		try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
			writer.println(StringUtils.join(resultRow, DELIMITER));
		}
	}
}
