package com.github.netopt.rsmsdm.gui;

import org.fxmisc.richtext.InlineCssTextArea;


/**
 * @author plechowicz
 * created on 21.02.19.
 */
public class ColoringTextArea extends InlineCssTextArea {

	public ColoringTextArea() {
		super();
	}

	public void appendLine(String text, String color) {
		var start = this.getText().length();
		this.appendText(text);
		this.appendText("\n");
		this.setStyle(start, start + text.length(), String.format("-fx-fill: %s;", color));
		int end = this.getDocument().getText().length();
		requestFollowCaret();
	}


}
