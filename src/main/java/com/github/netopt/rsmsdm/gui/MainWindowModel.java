package com.github.netopt.rsmsdm.gui;

import com.github.netopt.rsmsdm.experiment.Result;
import javafx.collections.ObservableList;

/**
 * @author plechowicz
 * created on 21.02.19.
 */
public class MainWindowModel {

	private String selectedFilePath;
	private ObservableList<Result> lastResults;

	public String getSelectedFilePath() {
		return selectedFilePath;
	}

	public void setSelectedFilePath(String selectedFilePath) {
		this.selectedFilePath = selectedFilePath;
	}

	public ObservableList<Result> getLastResults() {
		return lastResults;
	}

	public void setLastResults(ObservableList<Result> lastResults) {
		this.lastResults = lastResults;
	}
}
