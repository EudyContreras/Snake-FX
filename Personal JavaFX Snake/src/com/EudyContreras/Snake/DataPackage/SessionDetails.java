package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

public class SessionDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	private int ThemeID;
	private int LevelID;
	private int modeID;
	/**
	 * @param themeID
	 * @param levelID
	 * @param modeID
	 */
	public SessionDetails(int themeID, int levelID, int modeID) {
		super();
		this.ThemeID = themeID;
		this.LevelID = levelID;
		this.modeID = modeID;
	}
	public final int getThemeID() {
		return ThemeID;
	}
	public final int getLevelID() {
		return LevelID;
	}
	public final int getModeID() {
		return modeID;
	}


}
