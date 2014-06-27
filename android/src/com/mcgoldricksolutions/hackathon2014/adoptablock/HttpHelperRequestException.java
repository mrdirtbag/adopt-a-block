package com.mcgoldricksolutions.hackathon2014.adoptablock;

public class HttpHelperRequestException extends Exception {

	protected String mCause = null;
	
	public HttpHelperRequestException(String cause) {
		mCause = cause;
	}

}

