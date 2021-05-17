package com.github.netopt.rsmsdm.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class FileUtils {

	public static String createRelativePathToApplicationRootFolder(String path) {
		String basePath = System.getProperty("user.dir");
		return new File(basePath).toURI().relativize(new File(path).toURI()).getPath();
	}

	public static String getNameBasedOnDateAndTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd_HH-mm-ss"));
	}
}
