package com.github.netopt.rsmsdm.gui;

import com.github.netopt.rsmsdm.experiment.Result;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author plechowicz
 * created on 21.02.19.
 */
public class ViewResultWindow {

	private ViewResultController controller;

	public ViewResultWindow(ObservableList<Result> lastResults) throws IOException {
		Stage stage = new Stage();
		stage.setTitle("Last results");
		stage.setWidth(600);
		stage.setHeight(400);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getClassLoader().getResource("gui/ViewResultWindow.fxml"));
		BorderPane borderPane = loader.<BorderPane>load();
		Scene normalScene = new Scene(borderPane);
		stage.setScene(normalScene);
		stage.show();
		controller = (ViewResultController) loader.getController();
		controller.init(lastResults);
	}

	public ViewResultController getController() {
		return controller;
	}
}
