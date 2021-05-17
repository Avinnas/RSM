package com.github.netopt.rsmsdm.experiment;

/**
 * @author plechowicz
 * created on 19.02.19.
 */
public class PropertiesReadingException extends Exception {
	public PropertiesReadingException(Exception cause) {
		super(cause);
	}

	public PropertiesReadingException(String message) {
		super(message);
	}

	public PropertiesReadingException(String message, Exception cause) {
		super(message, cause);
	}
}
