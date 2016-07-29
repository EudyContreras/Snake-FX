package com.EudyContreras.Snake.Multiplayer;

public class PlayerID {

	private String location;
	private String userName;
	private int age;
	private int level;


	public enum Status{
		PLAYING,
		IDLE,
		WAITING,
		SEARCHING,
		DISCONNECTED,
	}
}
