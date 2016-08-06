package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

public class PlayerDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name = "";
	private String lastName = "";
	private String userName = "";
	private String passWord = "";
	private String location = "";
	private Boolean returning = false;
	private byte[] profilePic = null;
	private int level = 0;
	private int age = 0;

	public PlayerDetails(String name, String lastName, String userName, String passWord, String location, int level, int age, byte[] profilePic) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.userName = userName;
		this.location = location;
		this.passWord = passWord;
		this.profilePic = profilePic;
		this.level = level;
		this.age = age;
	}

	public PlayerDetails(String name, String lastName, String userName, String passWord, int level, byte[] profilePic) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.userName = userName;
		this.passWord = passWord;
		this.profilePic = profilePic;
		this.level = level;
	}

	public PlayerDetails(String userName, int level) {
		super();
		this.userName = userName;
		this.level = level;
	}

	public void setFromLocal(boolean state){
		this.returning = state;
	}

	public Boolean getTypeOfClient(){
		return returning;
	}

	public byte[] getProfilePicture(){
		return profilePic;
	}

	public final String getName() {
		return name;
	}

	public final String getLastName() {
		return lastName;
	}

	public final String getUserName() {
		return userName;
	}

	public final String getPassWord(){
		return passWord;
	}

	public final String getLocation() {
		return location;
	}

	public final int getLevel() {
		return level;
	}

	public final int getAge() {
		return age;
	}


}