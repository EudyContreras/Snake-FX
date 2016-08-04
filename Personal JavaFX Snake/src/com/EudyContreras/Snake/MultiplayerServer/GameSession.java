package com.EudyContreras.Snake.MultiplayerServer;

import com.EudyContreras.Snake.Identifiers.GameLevelID;
import com.EudyContreras.Snake.Identifiers.GameThemeID;

public class GameSession {
	private int sessionID;
	private MultiplayerClient playerOne;
	private MultiplayerClient playerTwo;
	private Status sessionStatus;
	private GameThemeID themeID;
	private GameLevelID levelID;

	public GameSession(GameThemeID themeID, GameLevelID levelID, MultiplayerClient playerOne, MultiplayerClient playerTwo){
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;

	}
	public enum Status{
		ACTIVE,
		INNACTIVE,
		TERMINATED,
	}
}
