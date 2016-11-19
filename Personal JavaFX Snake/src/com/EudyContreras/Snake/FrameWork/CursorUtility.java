package com.EudyContreras.Snake.FrameWork;

import com.EudyContreras.Snake.ImageBanks.GameImageBank;

import javafx.scene.Cursor;
import javafx.scene.Scene;

public class CursorUtility {

	public static void setCursor(CursorID cursorId, Scene scene) {
		switch (cursorId) {
		case DRAG:
			if (scene.getCursor() != (GameImageBank.dragCursor))
				scene.setCursor(GameImageBank.dragCursor);
			break;
		case GAME:
			break;
		case MENU:
			break;
		case NONE:
			if (scene.getCursor() != (Cursor.NONE))
				scene.setCursor(Cursor.NONE);
			break;
		case NORMAL:
			if (scene.getCursor() != GameImageBank.normalCursor)
				scene.setCursor(GameImageBank.normalCursor);
			break;
		case STRETCH:
			if (scene.getCursor() != (GameImageBank.stretchCursor))
				scene.setCursor(GameImageBank.stretchCursor);
			break;
		default:
			break;
		}
	}

	public static void setCursor(Cursor cursor, Scene scene) {
		if (cursor == Cursor.DEFAULT) {
			setCursor(CursorID.NORMAL, scene);
		} else {
			scene.setCursor(cursor);
		}
	}

	public enum CursorID {
		MENU, GAME, STRETCH, DRAG, NONE, NORMAL,

	}

}
