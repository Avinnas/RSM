package com.github.netopt.rsmsdm.experiment;

import com.github.netopt.rsmsdm.AppConfig;

import java.io.File;

/**
 * @author plechowicz
 * created on 23.02.19.
 */
public class ExperimentPropertiesValidator {
	public ExperimentPropertiesValidator() {

	}

	public ValidationResult validate(ExperimentProperties experimentProperties) {

		int maxCandidatePaths = 30;
		File file = new File(experimentProperties.getNetworkFile());
		String fileName = file.getName();
		if (fileName.equals(AppConfig.ExperimentPropertiesConstants.Euro28NetworkName)) {
			maxCandidatePaths = AppConfig.ExperimentPropertiesConstants.maxCandidatePathsEuro28;
		} else if (fileName.equals(AppConfig.ExperimentPropertiesConstants.US26NetworkName)) {
			maxCandidatePaths = AppConfig.ExperimentPropertiesConstants.maxCandidatePathsUS26;
		} else if (fileName.equals(AppConfig.ExperimentPropertiesConstants.DT14NetworkName)) {
			maxCandidatePaths = AppConfig.ExperimentPropertiesConstants.maxCandidatePathsDT14;
		} else if (fileName.equals(AppConfig.ExperimentPropertiesConstants.Pol12NetworkName)) {
			maxCandidatePaths = AppConfig.ExperimentPropertiesConstants.maxCandidatePathsPol12;
		} else {
			maxCandidatePaths = AppConfig.ExperimentPropertiesConstants.maxCandidatePaths;
		}
		int minCandidatePaths = AppConfig.ExperimentPropertiesConstants.candidatePathsMin;

		if (experimentProperties.getMaxCandidatePaths() != maxCandidatePaths) {
			return new ValidationResult(false, String.format("maxCandidatePaths is={%d} but should be={%d}", experimentProperties.getMaxCandidatePaths(), maxCandidatePaths));
		}
		if (experimentProperties.getCandidatePaths() > maxCandidatePaths || experimentProperties.getCandidatePaths() < minCandidatePaths) {
			return new ValidationResult(false, String.format("candidatePaths is={%d} but should be >= {%d} and <= {%d}", experimentProperties.getCandidatePaths(), minCandidatePaths, maxCandidatePaths));
		}

		if (experimentProperties.getCores() < AppConfig.ExperimentPropertiesConstants.coresMin || experimentProperties.getCores() > AppConfig.ExperimentPropertiesConstants.coresMax) {
			return new ValidationResult(false, String.format("cores is={%d} but should be >= {%d} and <= {%d}", experimentProperties.getCores(), AppConfig.ExperimentPropertiesConstants.coresMin, AppConfig.ExperimentPropertiesConstants.coresMax));
		}

		if (experimentProperties.getSummaryBitrate() < AppConfig.ExperimentPropertiesConstants.summaryBitrateMin || experimentProperties.getSummaryBitrate() > AppConfig.ExperimentPropertiesConstants.summaryBitrateMax) {
			return new ValidationResult(false, String.format("summaryBitrate is={%d} but should be >= {%d} and <= {%d}", experimentProperties.getSummaryBitrate(), AppConfig.ExperimentPropertiesConstants.summaryBitrateMin, AppConfig.ExperimentPropertiesConstants.summaryBitrateMax));
		}

		return new ValidationResult(true, "");
	}

	public class ValidationResult {
		Boolean success = false;
		String message;

		public ValidationResult(Boolean success, String message) {
			this.success = success;
			this.message = message;
		}
	}


}
