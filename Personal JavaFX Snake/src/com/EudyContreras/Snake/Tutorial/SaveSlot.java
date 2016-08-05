package com.EudyContreras.Snake.Tutorial;

import java.io.Serializable;

public class SaveSlot implements Serializable{

	private static final long serialVersionUID = 1L;
	private byte[] profilePic;
	private int score;
	private int level;
	private double gameVolume;
	private double resolutionX;
	private double resolutionY;
	private String profileName;

	public SaveSlot(String profileName, byte[] profilePic, int score, int level, double gameVolume, double resolutionX, double resolutionY) {
		super();
		this.profileName = profileName;
		this.profilePic = profilePic;
		this.score = score;
		this.level = level;
		this.gameVolume = gameVolume;
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
	}
	public SaveSlot(String profileName, int score, int level, double gameVolume, double resolutionX, double resolutionY) {
		new SaveSlot(profileName,null,score,level,gameVolume, resolutionX, resolutionY);
	}
	public int getScore() {return score;}
	public int getLevel() {return level;}
	public String getProfileName() {return profileName;}
	public byte[] getProfilePic() {return profilePic;}
	public double getGameVolume() {return gameVolume;}
	public double getResolutionX() {return resolutionX;}
	public double getResolutionY() {return resolutionY;}
}
