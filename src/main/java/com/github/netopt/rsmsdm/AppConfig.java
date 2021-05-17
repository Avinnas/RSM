package com.github.netopt.rsmsdm;

import java.io.IOException;
import java.util.Properties;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public final class AppConfig {

	public static final class Directories {

		private static Properties properties;

		static {
			properties = new Properties();
			try {
				properties.load(AppConfig.class.getClassLoader().getResourceAsStream("directories.properties"));
			} catch (IOException e) {
				System.exit(1);
			}
		}

		public static final String SCENARIOS_OUTPUT_FOLDER = properties.getProperty("scenariosOutputFile");
		public static final String RESULTS_OUTPUT_FOLDER = properties.getProperty("resultsOutputFile");
	}

	public static final class ExperimentPropertiesConstants {
		private static Properties properties;

		static {
			properties = new Properties();
			try {
				properties.load(AppConfig.class.getClassLoader().getResourceAsStream("experiment-properties-constants.properties"));
			} catch (IOException e) {
				System.exit(1);
			}
		}

		public static final String US26NetworkName = properties.getProperty("us26net");
		public static final String Euro28NetworkName = properties.getProperty("euro28net");
		public static final String DT14NetworkName = properties.getProperty("dt14net");
		public static final String Pol12NetworkName = properties.getProperty("pol12net");
		public static final Integer maxCandidatePathsUS26 = Integer.parseInt(properties.getProperty("us26maxCP"));
		public static final Integer maxCandidatePathsEuro28 = Integer.parseInt(properties.getProperty("euro28maxCP"));
		public static final Integer maxCandidatePathsDT14 = Integer.parseInt(properties.getProperty("dt14maxCP"));
		public static final Integer maxCandidatePathsPol12 = Integer.parseInt(properties.getProperty("pol12maxCP"));
		public static final Integer maxCandidatePaths = Integer.parseInt(properties.getProperty("maxCandidatePaths"));
		public static final Integer candidatePathsMin = Integer.parseInt(properties.getProperty("candidatePathsMin"));
		public static final Integer coresMin = Integer.parseInt(properties.getProperty("coresMin"));
		public static final Integer coresMax = Integer.parseInt(properties.getProperty("coresMax"));
		public static final Integer summaryBitrateMin = Integer.parseInt(properties.getProperty("summaryBitrateMin").replace("_", ""));
		public static final Integer summaryBitrateMax = Integer.parseInt(properties.getProperty("summaryBitrateMax").replace("_", ""));
	}
}
