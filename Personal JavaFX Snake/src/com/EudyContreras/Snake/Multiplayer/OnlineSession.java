package com.EudyContreras.Snake.Multiplayer;

import com.EudyContreras.Snake.Identifiers.GameLevelID;
import com.EudyContreras.Snake.Identifiers.GameThemeID;

public class OnlineSession {
	private int sessionID;
	private Status sessionStatus;

	public OnlineSession(GameThemeID themeID, GameLevelID levelID, PlayerID playerOne, PlayerID playerTwo){

	}
	public enum Status{
		ACTIVE,
		INNACTIVE,
		TERMINATED,
	}
}
