package com.SnakeGame.VisualInterface;

import com.SnakeGame.Core.Settings;
import com.SnakeGame.Utilities.ImageUtility;

import javafx.scene.image.Image;

public class MenuImageBank {
	public static Image mainMenuBackground = new Image(ImageUtility.loadResource("snake.jpg"), Settings.WIDTH,
			Settings.HEIGHT, false, true);
	public static Image optionsMenuBackground = new Image(ImageUtility.loadResource("rattlesnake.jpg"), Settings.WIDTH,
			Settings.HEIGHT, false, true);
	public static Image soundMenuBackground = new Image(ImageUtility.loadResource("snakeblue.jpg"), Settings.WIDTH,
			Settings.HEIGHT, false, true);
	public static Image modeMenuBackground = new Image(ImageUtility.loadResource("snake1.jpeg"), Settings.WIDTH,
			Settings.HEIGHT, false, true);
	public static Image controllsMenuBackground = new Image(ImageUtility.loadResource("green-snake.jpg"),
			Settings.WIDTH, Settings.HEIGHT, false, true);
	public static Image start_game_button = new Image(ImageUtility.loadResource("startGame.png"));
	public static Image startLogo = new Image(ImageUtility.loadResource("SnakeLogo.png"));
}
