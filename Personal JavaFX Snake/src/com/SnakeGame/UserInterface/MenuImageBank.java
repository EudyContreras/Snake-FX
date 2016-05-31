package com.SnakeGame.UserInterface;

import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.Utilities.ImageEffectUtility;

import javafx.scene.image.Image;

public class MenuImageBank {
	public static Image gameLogo = new Image(ImageEffectUtility.loadResource("SnakeLogo.png"));
	public static Image mainMenuBackground = new Image(ImageEffectUtility.loadResource("menu-background.png"), GameSettings.WIDTH,
			GameSettings.HEIGHT, false, true);
	public static Image startLogo = new Image(ImageEffectUtility.loadResource("SnakeLogo.png"), GameSettings.WIDTH,
			GameSettings.HEIGHT, false, true);
}
