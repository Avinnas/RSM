package com.github.netopt.rsmsdm.gui;

import com.github.netopt.rsmsdm.AppCmdLineInput;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

/**
 * @author plechowicz
 * created on 21.02.19.
 */
public class MainWindow extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("SDM Optimization");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getClassLoader().getResource("gui/MainWindow.fxml"));
		BorderPane borderPane = loader.<BorderPane>load();
		Scene scene = new Scene(borderPane);
		stage.setScene(scene);
		GuiLogger.getInstance().setLoggingTextArea(((MainWindowController) loader.getController()).getConsole());
		stage.show();
		// uncomment in design mode
//		createRefresherScene(stage);

		if (getParameters().getUnnamed().size() > 0) {
			AppCmdLineInput appCmdLine = new AppCmdLineInput();
			CmdLineParser parser = new CmdLineParser(appCmdLine);
			parser.parseArgument(getParameters().getUnnamed());
			if (appCmdLine.getScenarioFile() != null) {
				TextField textField = ((MainWindowController) loader.getController()).getSelectedFileTextField();
				textField.requestFocus();
				textField.setText(appCmdLine.getScenarioFile());
				textField.positionCaret(textField.getText().length());
				textField.setAlignment(Pos.CENTER_RIGHT);
				((MainWindowController) loader.getController()).onRunButtonClicked(null);
			}
		}
	}

	public void createRefresherScene(Stage primaryStage) {
		// allows for refreshing fxmls, used for building helper
		Stage refresherStage = new Stage();
		Button button = new Button("refresh");
		Scene scene = new Scene(button);
		refresherStage.setWidth(100);
		refresherStage.setHeight(100);
		button.setOnAction(actionEvent -> {
			FXMLLoader loader = new FXMLLoader();
			try {
				loader.setLocation(Path.of("./src/main/resources/gui/MainWindow.fxml").toUri().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			BorderPane borderPane = null;
			try {
				borderPane = loader.<BorderPane>load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			primaryStage.setScene(new Scene(borderPane));
		});
		refresherStage.setScene(scene);
		refresherStage.show();
	}
}