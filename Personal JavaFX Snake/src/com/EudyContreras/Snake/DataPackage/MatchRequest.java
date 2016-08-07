package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

public class MatchRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	private int themeID;


	public final String getUserName() {
		return userName;
	}


	public final int getThemeID() {
		return themeID;
	}


	public enum CommandID{

		startMatch,
		updateCount,
		disconnect,

	}
}