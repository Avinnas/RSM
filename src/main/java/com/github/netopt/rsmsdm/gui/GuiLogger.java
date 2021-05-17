package com.github.netopt.rsmsdm.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.slf4j.event.Level;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

/**
 * @author plechowicz
 * created on 21.02.19.
 */
public class GuiLogger {

	private static Properties loggingColors = new Properties();

	static {
		try {
			loggingColors.load(ColoringTextArea.class.getClassLoader().getResourceAsStream("gui/logging-colors.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final String blackColor = "black";

	private final String debugColor = loggingColors.getProperty("debugColor", blackColor);
	private final String warnColor = loggingColors.getProperty("warnColor", blackColor);
	private final String errorColor = loggingColors.getProperty("errorColor", blackColor);
	private final String infoColor = loggingColors.getProperty("infoColor", blackColor);

	private ColoringTextArea loggingTextArea = new ColoringTextArea();

	public static final GuiLogger INSTANCE = new GuiLogger();

	public static GuiLogger getInstance() {
		return INSTANCE;
	}

	public void setLoggingTextArea(ColoringTextArea loggingTextArea) {
		this.loggingTextArea = loggingTextArea;
	}

	public void appendLine(String message, Level loggingLevel) {
		loggingTextArea.appendLine(message, getColorByLoggingLevel(loggingLevel));
	}

	public void openMessagePopup(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText("Information");
		alert.setContentText(message);
		alert.setResizable(true);
		alert.getDialogPane().setPrefSize(400, 300);
		alert.showAndWait();
	}

	public void openStackTracePopup(String message, Throwable throwable) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error occurred");
		alert.setContentText(throwable.getMessage());

		Label label = new Label("Stack trace:");

		StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();

		TextArea textArea = new TextArea(exceptionAsString);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);
		alert.getDialogPane().setExpanded(true);
		alert.setResizable(true);
		alert.getDialogPane().setPrefSize(400, 300);
		appendException(message, throwable);
		alert.showAndWait();
	}

	public void appendException(String message, Throwable t) {
		StringBuilder builder = new StringBuilder(message);
		builder.append("\n");
		builder.append(t.getMessage());
		builder.append("\n");
		Throwable nextThrowable = t.getCause();
		while (nextThrowable != null) {
			builder.append(nextThrowable.getMessage());
			builder.append("\n");
			nextThrowable = nextThrowable.getCause();
		}
		loggingTextArea.appendLine(builder.toString(), getColorByLoggingLevel(Level.ERROR));
	}

	private String getColorByLoggingLevel(Level loggingLevel) {
		switch (loggingLevel) {
			case ERROR:
				return errorColor;
			case DEBUG:
				return debugColor;
			case WARN:
				return warnColor;
			case INFO:
				return infoColor;
			default:
				return blackColor;
		}
	}
}
