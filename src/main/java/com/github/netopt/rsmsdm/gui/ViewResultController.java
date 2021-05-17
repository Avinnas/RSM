package com.github.netopt.rsmsdm.gui;

import com.github.netopt.rsmsdm.experiment.Result;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author plechowicz
 * created on 21.02.19.
 */
public class ViewResultController {

	@FXML
	private TableView<Result> tableView;

	public ViewResultController() {
	}

	public void init(ObservableList<Result> lastResults) {
		List<TableColumn<Result, String>> tableColumns = new ArrayList<>();
		Result.ResultField[] resultFields = Result.getResultFields();
		for (var resultField : resultFields) {
			TableColumn<Result, String> tableColumn = new TableColumn<>(resultField.getName());
			tableColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getResultValue(resultField)));
			tableColumns.add(tableColumn);
		}
		tableView.getColumns().setAll(tableColumns);
		tableView.setItems(lastResults);
		initCopyToClipboard();
	}

	private void initCopyToClipboard() {
		tableView.getSelectionModel().setCellSelectionEnabled(true);
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		MenuItem item = new MenuItem("Copy");
		item.setOnAction(event -> {
			copyTableToClipboard(tableView);
		});
		ContextMenu menu = new ContextMenu();
		menu.getItems().add(item);
		tableView.setContextMenu(menu);
		tableView.setOnKeyPressed(keyEvent -> {
			KeyCodeCombination copyKeyCodeCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
			if (copyKeyCodeCombination.match(keyEvent)) {
				if (keyEvent.getSource() instanceof TableView) {
					// copy to clipboard
					copyTableToClipboard((TableView<?>) keyEvent.getSource());
					// event is handled, consume it
					keyEvent.consume();
				}
			}
		});
	}

	private void copyTableToClipboard(TableView<?> tableView) {
		ObservableList<TablePosition> posList = tableView.getSelectionModel().getSelectedCells();
		int old_r = -1;
		StringBuilder clipboardString = new StringBuilder();
		for (TablePosition p : posList) {
			int r = p.getRow();
			int c = p.getColumn();
			Object cell = this.tableView.getColumns().get(c).getCellData(r);
			if (cell == null)
				cell = "";
			if (old_r == r)
				clipboardString.append('\t');
			else if (old_r != -1)
				clipboardString.append('\n');
			clipboardString.append(cell);
			old_r = r;
		}
		final ClipboardContent content = new ClipboardContent();
		content.putString(clipboardString.toString());
		Clipboard.getSystemClipboard().setContent(content);
	}
}
