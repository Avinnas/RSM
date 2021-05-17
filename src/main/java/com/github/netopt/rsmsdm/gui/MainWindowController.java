package com.github.netopt.rsmsdm.gui;

import com.github.netopt.rsmsdm.experiment.PropertiesReadingException;
import com.github.netopt.rsmsdm.experiment.Result;
import com.github.netopt.rsmsdm.experiment.ScenarioRunner;
import com.github.netopt.rsmsdm.network.NetworkFilesReadingException;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.File;
import java.io.IOException;

/**
 * @author plechowicz
 * created on 21.02.19.
 */
public class MainWindowController {

	private static final Logger log = LoggerFactory.getLogger(MainWindowController.class);

	private MainWindowModel model = new MainWindowModel();

	@FXML
	private ColoringTextArea console;

	@FXML
	private Button selectFileButton;

	@FXML
	private TextField selectedFileTextField;

	@FXML
	private Button runButton;

	@FXML
	public void selectFileButtonClicked(Event event) {
		FileChooser fileChooser = new FileChooser();
		if (model.getSelectedFilePath() != null) {
			File lastLocation = new File(model.getSelectedFilePath());
			if (lastLocation.getParentFile().exists()) {
				fileChooser.setInitialDirectory(lastLocation.getParentFile());
			}
		}
		File file = fileChooser.showOpenDialog(((Node) event.getTarget()).getScene().getWindow());
		if (file != null) {
			model.setSelectedFilePath(file.getPath());
			selectedFileTextField.setText(file.getPath());
			selectedFileTextField.requestFocus();
			selectedFileTextField.positionCaret(selectedFileTextField.getText().length());
			selectedFileTextField.setAlignment(Pos.CENTER_RIGHT);
			runButton.requestFocus();
		}
	}

	@FXML
	public void onSelectedFileTextFieldEntered(Event event) {
		String text = selectedFileTextField.getText();
		model.setSelectedFilePath(text); // works only when enter is hit
		runButton.requestFocus();
	}

	@FXML
	public void onRunButtonClicked(Event event) {
		String scenarioPath = selectedFileTextField.getText();
		GuiLogger.getInstance().appendLine("Reading scenario file: ", Level.INFO);
		GuiLogger.getInstance().appendLine(scenarioPath, Level.INFO);
		log.info("scenario: " + scenarioPath);
		ScenarioRunner runner = new ScenarioRunner();
		try {
			ObservableList<Result> observableList = FXCollections.observableArrayList();
			ListChangeListener<Result> listChangeListener = change -> {
				if (change.next()) {
					change.getAddedSubList().forEach(
							r -> GuiLogger.getInstance().appendLine(
									String.format("spectrum: %6d; time: %d", r.getSpectrum().getSpectrumCost(), r.getTimeInNanos()), Level.INFO
							)
					);
				}
			};
			observableList.addListener(listChangeListener);
			model.setLastResults(observableList);
			runner.run(scenarioPath, observableList);
			GuiLogger.getInstance().appendLine("Finished.", Level.INFO);
			log.info("Finished.");
		} catch (PropertiesReadingException | IOException | NetworkFilesReadingException e) {
			log.error("Error processing scenario", e);
			GuiLogger.getInstance().openStackTracePopup("Error processing scenario: ", e);
		}
	}

	@FXML
	public void onStopButtonClicked(Event event) {
		GuiLogger.getInstance().openMessagePopup("Not implemented.");
	}

	@FXML
	public void onViewResultButtonClicked(Event event) throws IOException {
		ViewResultWindow viewResultWindow = new ViewResultWindow(model.getLastResults());
	}

	@FXML
	public void onScenarioCreatorButtonClicked(Event event) {
		GuiLogger.getInstance().openMessagePopup("Not implemented.");
	}

	public ColoringTextArea getConsole() {
		return console;
	}

	public TextField getSelectedFileTextField() {
		return selectedFileTextField;
	}
}
