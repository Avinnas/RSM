package com.github.netopt.rsmsdm.network;

import java.io.FileNotFoundException;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class NetworkFilesReadingException extends Exception {

	public NetworkFilesReadingException(FileNotFoundException cause) {
		super(cause);
	}

	public NetworkFilesReadingException(String message) {
		super(message);
	}

	public NetworkFilesReadingException(String message, Exception cause) {
		super(message, cause);
	}
}
