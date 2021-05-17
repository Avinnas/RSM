package com.github.netopt.rsmsdm.experiment;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class PropertiesWritingException extends Exception {
	public PropertiesWritingException(Exception cause) {
		super(cause);
	}

	public PropertiesWritingException(String message) {
		super(message);
	}

	public PropertiesWritingException(String message, Exception cause) {
		super(message, cause);
	}
}
