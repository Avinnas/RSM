package com.github.netopt.rsmsdm.experiment;

import com.github.netopt.rsmsdm.spectrum.Spectrum;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class Result {

	private static final AtomicInteger counter = new AtomicInteger(1);
	private final Integer id;
	private final String experimentPropertiesFile;
	private final String algorithmPropertiesFile;
	private final ExperimentProperties experimentProperties;
	private final Properties algorithmProperties;
	private final Spectrum spectrum;
	private final Long timeInNanos;

	public enum ResultField {

		ID("id"),
		SPECTRUM_USAGE("spectrumUsage"),
		TIME_NANO("timeInNanoSeconds"),
		ALGORITHM("algorithm"),
		CORES("cores"),
		CANDIDATE_PATHS("candidatePaths"),
		SUMMARY_VOLUME("summaryVolume"),
		START_VOLUME("startVolume"),
		STOP_VOLUME("stopVolume"),
		STEP_VOLUME("stepVolume"),
		DEMANDS_FILE("demandsFile"),
		DEMANDS_DIRECTORY("demandsDirectory"),
		EXPERIMENT_PROPERTIES_FILE("experimentPropertiesFile"),
		ALGORITHM_PROPERTIES_FILE("algorithmPropertiesFile"),
		CANDIDATE_PATHS_FILE("candidatePathsFile"),
		SLOTS_FILE("slotsFile"),
		;
		final String name;

		public String getName() {
			return name;
		}

		ResultField(String name) {
			this.name = name;
		}
	}

	public List<String> toListOfString() {
		return Arrays.stream(getResultFields()).map(this::getResultValue).collect(Collectors.toList());
	}

	public String getResultValue(ResultField resultField) {
		switch (resultField) {
			case ID:
				return id.toString();
			case ALGORITHM:
				return experimentProperties.getAlgorithm();
			case CORES:
				return experimentProperties.getCores().toString();
			case CANDIDATE_PATHS:
				return experimentProperties.getCandidatePaths().toString();
			case SUMMARY_VOLUME:
				return experimentProperties.getSummaryBitrate().toString();
			case STEP_VOLUME:
				return getStepVolume(experimentProperties.getDemandsDirectory());
			case STOP_VOLUME:
				return getEndingVolume(experimentProperties.getDemandsDirectory());
			case START_VOLUME:
				return getStartingVolume(experimentProperties.getDemandsDirectory());
			case DEMANDS_DIRECTORY:
				return new File(experimentProperties.getDemandsDirectory()).toString();
			case DEMANDS_FILE:
				return new File(experimentProperties.getDemandsFile()).getName();
			case EXPERIMENT_PROPERTIES_FILE:
				return experimentPropertiesFile;
			case ALGORITHM_PROPERTIES_FILE:
				return algorithmPropertiesFile;
			case SLOTS_FILE:
				return experimentProperties.getSlotsForCandidatePathsFile();
			case CANDIDATE_PATHS_FILE:
				return experimentProperties.getCandidatePathsFile();
			case TIME_NANO:
				return timeInNanos.toString();
			case SPECTRUM_USAGE:
				return spectrum.getSpectrumCost().toString();
			default:
				return "not implemented";
		}
	}

	public static List<String> getResultHeaderAsListOfStrings() {
		return Arrays.stream(ResultField.values()).map(ResultField::getName).collect(Collectors.toList());
	}

	public static ResultField[] getResultFields() {
		return ResultField.values();
	}

	public ExperimentProperties getExperimentProperties() {
		return experimentProperties;
	}

	public Properties getAlgorithmProperties() {
		return algorithmProperties;
	}

	public Spectrum getSpectrum() {
		return spectrum;
	}

	public Long getTimeInNanos() {
		return timeInNanos;
	}

	public Integer getId() {
		return id;
	}

	private String getStartingVolume(String demandFilePath) {
		return getVolumeFromDemandFile(demandFilePath, 1);
	}

	private String getEndingVolume(String demandFilePath) {
		return getVolumeFromDemandFile(demandFilePath, 2);
	}

	private String getStepVolume(String demandFilePath) {
		return getVolumeFromDemandFile(demandFilePath, 3);
	}

	private String getVolumeFromDemandFile(String demandFilePath, int order) {
		Pattern pattern = Pattern.compile("[\\d_]+");
		File file = new File(demandFilePath);
		Matcher matcher = pattern.matcher(file.getName());
		try {
			for (int i = -1; i < order; i++) {
				matcher.find();
			}
			return matcher.group();
		} catch (IllegalStateException e) {
			return "-1";
		}
	}

	private Result(Builder builder) {
		id = counter.getAndIncrement();
		experimentPropertiesFile = builder.experimentPropertiesFile;
		algorithmPropertiesFile = builder.algorithmPropertiesFile;
		experimentProperties = builder.experimentProperties;
		algorithmProperties = builder.algorithmProperties;
		spectrum = builder.spectrum;
		timeInNanos = builder.timeInNanos;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public String getExperimentPropertiesFile() {
		return experimentPropertiesFile;
	}

	public String getAlgorithmPropertiesFile() {
		return algorithmPropertiesFile;
	}

	public static Builder copyResult(Result copy) {
		Builder builder = newBuilder(copy);
		counter.getAndDecrement();
		return builder;
	}

	public static Builder newBuilder(Result copy) {
		Builder builder = new Builder();
		builder.experimentPropertiesFile = copy.getExperimentPropertiesFile();
		builder.algorithmPropertiesFile = copy.getAlgorithmPropertiesFile();
		builder.experimentProperties = copy.getExperimentProperties();
		builder.algorithmProperties = copy.getAlgorithmProperties();
		builder.spectrum = copy.getSpectrum();
		builder.timeInNanos = copy.getTimeInNanos();
		return builder;
	}

	public static final class Builder {
		private ExperimentProperties experimentProperties;
		private Properties algorithmProperties;
		private Spectrum spectrum;
		private Long timeInNanos;
		private String experimentPropertiesFile;
		private String algorithmPropertiesFile;

		private Builder() {
		}

		public Builder setExperimentProperties(ExperimentProperties experimentProperties) {
			this.experimentProperties = experimentProperties;
			return this;
		}

		public Builder setAlgorithmProperties(Properties algorithmProperties) {
			this.algorithmProperties = algorithmProperties;
			return this;
		}

		public Builder setSpectrum(Spectrum spectrum) {
			this.spectrum = spectrum;
			return this;
		}

		public Builder setTimeInNanos(Long timeInMilis) {
			this.timeInNanos = timeInMilis;
			return this;
		}

		public Result build() {
			return new Result(this);
		}

		public Builder setExperimentPropertiesFile(String experimentPropertiesFile) {
			this.experimentPropertiesFile = experimentPropertiesFile;
			return this;
		}

		public Builder setAlgorithmPropertiesFile(String algorithmPropertiesFile) {
			this.algorithmPropertiesFile = algorithmPropertiesFile;
			return this;
		}
	}
}
