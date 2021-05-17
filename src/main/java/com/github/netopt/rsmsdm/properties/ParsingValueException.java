package com.github.netopt.rsmsdm.properties;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class ParsingValueException extends Exception {
	public ParsingValueException(String message, Exception cause) {
		super(message, cause);
	}

	public ParsingValueException(String message) {
		super(message);
	}
}
