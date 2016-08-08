package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Utilities.ImageLoadingUtility;

import javafx.scene.image.Image;

public class MenuImageBank {
	public static Image gameLogo = new Image(ImageLoadingUtility.loadResource("menu-logo.png"));
	public static Image main_logo = new Image(ImageLoadingUtility.loadResource("main_logo.png"));
	public static Image options_logo = new Image(ImageLoadingUtility.loadResource("options_logo.png"));
	public static Image game_mode_logo = new Image(ImageLoadingUtility.loadResource("game_mode_logo.png"));
	public static Image menuStart = new Image(ImageLoadingUtility.loadResource("menu-start.png"));
	public static Image menuOptions= new Image(ImageLoadingUtility.loadResource("menu-options.png"));
	public static Image menuMultiplayer = new Image(ImageLoadingUtility.loadResource("menu-multiplayer.png"));
	public static Image menuHighscore = new Image(ImageLoadingUtility.loadResource("menu-highscore.png"));
	public static Image menuExit = new Image(ImageLoadingUtility.loadResource("menu-exit.png"));
	public static Image menuBackground = new Image(ImageLoadingUtility.loadResource("menu-background.png"), GameSettings.WIDTH, GameSettings.HEIGHT, false, true);
	public static Image mainMenuBackground = new Image(ImageLoadingUtility.loadResource("black-menu-background.png"));
}
