
package com.EudyContreras.Snake.FrameWork;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Identifiers.GameThemeID;
import com.EudyContreras.Snake.ImageBanks.GameLevelImage;
import com.EudyContreras.Snake.Utilities.RandomGenUtility;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents a cactus which creates a moving or wind caused waving
 * illusion.
 *
 * @author Eudy Contreras
 *
 */
 public class GameBackground extends AbstractTile {
	private static int INDEX = 1;
	private static ImageView BACKGROUND_VIEW = new ImageView();

	public static void SET_RANDOM_BACKGROUND(GameManager game, GameThemeID gameTheme) {
		if (gameTheme == GameThemeID.DESERT_THEME) {
			if (RandomGenUtility.getRandom(1, 5) == 1) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundOne);
			} else if (RandomGenUtility.getRandom(1, 5) == 2) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundTwo);
			} else if (RandomGenUtility.getRandom(1, 5) == 3) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundThree);
			} else if (RandomGenUtility.getRandom(1, 5) == 4) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundFour);
			} else if (RandomGenUtility.getRandom(1, 5) == 5) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundFive);
			}
		}
		if (gameTheme == null){
			BACKGROUND_VIEW.setImage(GameLevelImage.classicBackground);
		}
		game.getGameRoot().getChildren().remove(BACKGROUND_VIEW);
		game.getGameRoot().getChildren().add(0, BACKGROUND_VIEW);
	}

	public static void SET_SEQUENTIAL_BACKGROUND(GameManager game, GameThemeID gameTheme) {
		if (gameTheme == GameThemeID.DESERT_THEME) {
			if (INDEX == 1) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundOne);
			}else if (INDEX == 2) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundTwo);
			}else if (INDEX == 3) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundThree);
			}else if (INDEX == 4) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundFour);
			}else if (INDEX == 5) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundFive);
			}else if (INDEX == 6) {
				BACKGROUND_VIEW.setImage(GameLevelImage.desertBackgroundSix);
			}
		}
		else if (gameTheme == null) {
			BACKGROUND_VIEW.setImage(GameLevelImage.classicBackground);
		}
		INDEX += 1;
		if (INDEX > 6) {
			INDEX = 1;
		}
		BACKGROUND_VIEW.setTranslateX(-5);
		game.getGameRoot().getChildren().remove(BACKGROUND_VIEW);
		game.getGameRoot().getChildren().add(0, BACKGROUND_VIEW);
	}

	public static void SET_BACKGROUND(GameManager game, Image image) {
		BACKGROUND_VIEW.setImage(image);
		game.getGameRoot().getChildren().remove(BACKGROUND_VIEW);
		game.getGameRoot().getChildren().add(0, BACKGROUND_VIEW);
	}
}
