package com.SnakeGame.Interface;

import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.Utilities.ImageUtility;

import javafx.scene.image.Image;

public class MenuImageBank {
	public static Image gameLogo = new Image(ImageUtility.loadResource("SnakeLogo.png"));
	public static Image mainMenuBackground = new Image(ImageUtility.loadResource("image.png"), GameSettings.WIDTH,
			GameSettings.HEIGHT, false, true);
	public static Image startLogo = new Image(ImageUtility.loadResource("SnakeLogo.png"));
}
