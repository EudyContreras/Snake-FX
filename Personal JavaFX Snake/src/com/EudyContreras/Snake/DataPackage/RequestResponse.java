package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

public class RequestResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Response response;
	private String userName;
	private SessionDetails details;

	public RequestResponse(Response response, String userName, SessionDetails details) {
		this.response = response;
		this.userName = userName;
		this.details = details;
	}

	public final SessionDetails getDetails() {
		return details;
	}

	public final Response getResponse() {
		return response;
	}

	public final String getUserName() {
		return userName;
	}

	public enum Response implements Serializable {
		ACCEPT, DECLINE
	}
}