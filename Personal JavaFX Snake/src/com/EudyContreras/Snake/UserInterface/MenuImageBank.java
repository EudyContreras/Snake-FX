package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Utilities.ImageEffectUtility;

import javafx.scene.image.Image;

public class MenuImageBank {
	public static Image gameLogo = new Image(ImageEffectUtility.loadResource("menu-logo.png"));
	public static Image main_logo = new Image(ImageEffectUtility.loadResource("main_logo.png"));
	public static Image options_logo = new Image(ImageEffectUtility.loadResource("options_logo.png"));
	public static Image game_mode_logo = new Image(ImageEffectUtility.loadResource("game_mode_logo.png"));
	public static Image menuStart = new Image(ImageEffectUtility.loadResource("menu-start.png"));
	public static Image menuOptions= new Image(ImageEffectUtility.loadResource("menu-options.png"));
	public static Image menuMultiplayer = new Image(ImageEffectUtility.loadResource("menu-multiplayer.png"));
	public static Image menuHighscore = new Image(ImageEffectUtility.loadResource("menu-highscore.png"));
	public static Image menuExit = new Image(ImageEffectUtility.loadResource("menu-exit.png"));
	public static Image menuBackground = new Image(ImageEffectUtility.loadResource("menu-background.png"));
	public static Image mainMenuBackground = new Image(ImageEffectUtility.loadResource("black-menu-background.png"));
	public static Image startLogo = new Image(ImageEffectUtility.loadResource("SnakeLogo.png"), GameSettings.WIDTH,
			GameSettings.HEIGHT, false, true);
}
