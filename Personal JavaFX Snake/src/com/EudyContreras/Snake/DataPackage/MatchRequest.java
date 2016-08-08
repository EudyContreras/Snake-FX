package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

public class MatchRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	private int themeID;
	private byte[] profilePic;

	public MatchRequest(String userName, int themeID, byte[] profilePic){
		this.userName = userName;
		this.themeID = themeID;
		this.profilePic = profilePic;
	}
	public MatchRequest(String userName, int themeID){
		this.userName = userName;
		this.themeID = themeID;
	}
	public final String getUserName() {
		return userName;
	}
	public final int getThemeID() {
		return themeID;
	}
	public byte[] getProfilePic(){
		return profilePic;
	}
	public enum CommandID implements Serializable{

		startMatch,
		updateCount,
		disconnect,

	}
}