package com.SnakeGame.UserInterface;

import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.Utilities.ImageEffectUtility;

import javafx.scene.image.Image;

public class MenuImageBank {
	public static Image gameLogo = new Image(ImageEffectUtility.loadResource("menu-logo.png"));
	public static Image menuStart = new Image(ImageEffectUtility.loadResource("menu-start.png"));
	public static Image menuOptions= new Image(ImageEffectUtility.loadResource("menu-options.png"));
	public static Image menuMultiplayer = new Image(ImageEffectUtility.loadResource("menu-multiplayer.png"));
	public static Image menuHighscore = new Image(ImageEffectUtility.loadResource("menu-highscore.png"));
	public static Image menuExit = new Image(ImageEffectUtility.loadResource("menu-exit.png"));
	public static Image mainMenuBackground = new Image(ImageEffectUtility.loadResource("black-menu-background.png"));
	public static Image startLogo = new Image(ImageEffectUtility.loadResource("SnakeLogo.png"), GameSettings.WIDTH,
			GameSettings.HEIGHT, false, true);
}
