package com.github.netopt.rsmsdm.experiment;

import java.util.ArrayList;
import java.util.List;

/**
 * Unmodifiable properties of a single experiment.
 *
 * @author plechowicz
 * created on 19.02.19.
 */
public class ExperimentProperties {

	private final String networkFile;
	private final String candidatePathsFile;
	private final String demandsDirectory;
	private final String demandsFile;
	private final String slotsForCandidatePathsFile;
	private final String algorithm;
	private final Integer cores;
	private final Integer candidatePaths;
	private final Integer maxCandidatePaths;
	private final Long summaryBitrate;

	private ExperimentProperties(Builder builder) {
		networkFile = builder.networkFile;
		candidatePathsFile = builder.candidatePathsFile;
		demandsDirectory = builder.demandsDirectory;
		demandsFile = builder.demandsFile;
		slotsForCandidatePathsFile = builder.slotsForCandidatePathsFile;
		algorithm = builder.algorithm;
		cores = builder.cores;
		candidatePaths = builder.candidatePaths;
		maxCandidatePaths = builder.maxCandidatePaths;
		summaryBitrate = builder.summaryBitrate;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static CartesianProductBuilder newCartesianProductBuilder() {
		return new CartesianProductBuilder();
	}

	public static Builder newBuilder(ExperimentProperties copy) {
		Builder builder = new Builder();
		builder.networkFile = copy.getNetworkFile();
		builder.candidatePathsFile = copy.getCandidatePathsFile();
		builder.demandsDirectory = copy.getDemandsDirectory();
		builder.demandsFile = copy.getDemandsFile();
		builder.slotsForCandidatePathsFile = copy.getSlotsForCandidatePathsFile();
		builder.algorithm = copy.getAlgorithm();
		builder.cores = copy.getCores();
		builder.candidatePaths = copy.getCandidatePaths();
		builder.maxCandidatePaths = copy.getMaxCandidatePaths();
		builder.summaryBitrate = copy.getSummaryBitrate();
		return builder;
	}

	public String getNetworkFile() {
		return networkFile;
	}

	public String getCandidatePathsFile() {
		return candidatePathsFile;
	}

	public String getDemandsFile() {
		return demandsFile;
	}

	public String getSlotsForCandidatePathsFile() {
		return slotsForCandidatePathsFile;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public Integer getCores() {
		return cores;
	}

	public Integer getCandidatePaths() {
		return candidatePaths;
	}

	public Integer getMaxCandidatePaths() {
		return maxCandidatePaths;
	}

	public Long getSummaryBitrate() {
		return summaryBitrate;
	}

	public String getDemandsDirectory() {
		return demandsDirectory;
	}

	public static final class Builder {
		private String networkFile;
		private String candidatePathsFile;
		private String demandsDirectory;
		private String demandsFile;
		private String slotsForCandidatePathsFile;
		private String algorithm;
		private Integer cores;
		private Integer candidatePaths;
		private Integer maxCandidatePaths;
		private Long summaryBitrate;

		private Builder() {
		}

		public Builder setNetworkFile(String networkFile) {
			this.networkFile = networkFile;
			return this;
		}

		public Builder setCandidatePathsFile(String candidatePathsFile) {
			this.candidatePathsFile = candidatePathsFile;
			return this;
		}

		public Builder setDemandsDirectory(String demandsDirectory) {
			this.demandsDirectory = demandsDirectory;
			return this;
		}

		public Builder setDemandsFile(String demandsFile) {
			this.demandsFile = demandsFile;
			return this;
		}

		public Builder setSlotsForCandidatePathsFile(String slotsForCandidatePathsFile) {
			this.slotsForCandidatePathsFile = slotsForCandidatePathsFile;
			return this;
		}

		public Builder setAlgorithm(String algorithm) {
			this.algorithm = algorithm;
			return this;
		}

		public Builder setCores(Integer cores) {
			this.cores = cores;
			return this;
		}

		public Builder setCandidatePaths(Integer candidatePaths) {
			this.candidatePaths = candidatePaths;
			return this;
		}

		public Builder setMaxCandidatePaths(Integer maxCandidatePaths) {
			this.maxCandidatePaths = maxCandidatePaths;
			return this;
		}

		public Builder setSummaryBitrate(Long summaryBitrate) {
			this.summaryBitrate = summaryBitrate;
			return this;
		}

		public ExperimentProperties build() {
			return new ExperimentProperties(this);
		}
	}

	public static final class CartesianProductBuilder {
		private String networkFile;
		private String candidatePathsFile;
		private String demandsDirectory;
		private List<String> demandsFile;
		private String slotsForCandidatePathsFile;
		private String algorithm;
		private List<Integer> cores;
		private List<Integer> candidatePaths;
		private Integer maxCandidatePaths;
		private List<Long> summaryBitrate;

		private CartesianProductBuilder() {
		}

		public CartesianProductBuilder setNetworkFile(String networkFile) {
			this.networkFile = networkFile;
			return this;
		}

		public CartesianProductBuilder setCandidatePathsFile(String candidatePathsFile) {
			this.candidatePathsFile = candidatePathsFile;
			return this;
		}

		public CartesianProductBuilder setDemandsDirectory(String demandsDirectory) {
			this.demandsDirectory = demandsDirectory;
			return this;
		}

		public CartesianProductBuilder setDemandsFile(List<String> demandsFile) {
			this.demandsFile = demandsFile;
			return this;
		}

		public CartesianProductBuilder setSlotsForCandidatePathsFile(String slotsForCandidatePathsFile) {
			this.slotsForCandidatePathsFile = slotsForCandidatePathsFile;
			return this;
		}

		public CartesianProductBuilder setAlgorithm(String algorithm) {
			this.algorithm = algorithm;
			return this;
		}

		public CartesianProductBuilder setCores(List<Integer> cores) {
			this.cores = cores;
			return this;
		}

		public CartesianProductBuilder setCandidatePaths(List<Integer> candidatePaths) {
			this.candidatePaths = candidatePaths;
			return this;
		}

		public CartesianProductBuilder setMaxCandidatePaths(Integer maxCandidatePaths) {
			this.maxCandidatePaths = maxCandidatePaths;
			return this;
		}

		public CartesianProductBuilder setSummaryBitrate(List<Long> summaryBitrates) {
			this.summaryBitrate = summaryBitrates;
			return this;
		}

		public List<ExperimentProperties> build() {
			List<ExperimentProperties> experimentProperties = new ArrayList<>();
			for (var core : cores) {
				for (var candidatePath : candidatePaths) {
					for (var summaryBitrate : summaryBitrate) {
						for (var demandsFile : demandsFile) {
							experimentProperties.add(ExperimentProperties.newBuilder()
									.setNetworkFile(networkFile)
									.setDemandsDirectory(demandsDirectory)
									.setDemandsFile(demandsFile)
									.setSlotsForCandidatePathsFile(slotsForCandidatePathsFile)
									.setMaxCandidatePaths(maxCandidatePaths)
									.setCandidatePaths(candidatePath)
									.setCandidatePathsFile(candidatePathsFile)
									.setAlgorithm(algorithm)
									.setCores(core)
									.setSummaryBitrate(summaryBitrate).build());
						}
					}
				}
			}
			return experimentProperties;
		}
	}
}
