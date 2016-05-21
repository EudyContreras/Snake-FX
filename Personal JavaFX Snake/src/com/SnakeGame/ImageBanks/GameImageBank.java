package com.SnakeGame.ImageBanks;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.Utilities.ImageUtility;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * This is the class where we load all images. we first precreate a shaded,
 * lighted, glowing or regular image here and then we use it in the game. This
 * way images are only loaded once and the game is able to reused the image an
 * unlimited amount of times. We are able to create any combination of post
 * effect image with a simple Image utility I have created which will apply
 * effects to an image and will then take a snapshot of that image and save it
 * to memory.
 *
 * @author Eudy Contreras
 *
 */
public class GameImageBank {


	public static Image glowingImage;
	public static Image glowingCircle;
	public static Image glowingCircle2;
	public static Image shadedImage;
	public static Image preLightedDebris;
	public static Image debris;
	public static Image snakeHead;
	public static Image snakeBlinking;
	public static Image snakeEating;
	public static Circle slither;
	public static Circle snakeSphere;
	public static Circle snakeSphere2;
	public static Circle snakeSphere3;
	public static Image snakeBody;
	public static Image snakeDebris;
	public static Image snakeTail;
	public static Image snakeBones;
	public static Image snakeSkull;
	public static Image dirt;
	public static Image apple;
	public static Image fruit;
	public static Image fruit2;
	public static Image fruit3;
	public static Image snakeHead2;
	public static Image snakeBlinking2;
	public static Image snakeEating2;
	public static Image snakeBody2;
	public static Image snakeDebris2;
	public static Image redHealthBar;
	public static Image greenHealthBar;
	public static Image scoreBoard;
	public static Image countKeeper;
	public static Image levelCompleteSplash;
	public static Image gameOverScreen;
	public static Image continueOpt;
	public static Image quitOpt;
	public static Image restartOpt;
	public static Image continueOpt2;
	public static Image quitOpt2;
	public static Image restartOpt2;
	public static Image hud_bar;
	public static Image hud_bar_cover;
	public static Image health_bar_red_one;
	public static Image health_bar_green_two;
	public static Image health_bar_green_one;
	public static Image health_bar_red_two;
	public static Image hud_timer;
	public static Image player_one_hud;
	public static Image player_two_hud;
	public static Image player_one_wins;
	public static Image player_two_wins;
	public static Image options_board;
	public static Image continue_button;
	public static Image restart_button;
	public static Image quit_button;
	public static Image draw_game;

	public GameImageBank() {
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * Game objects
		 */
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		glowingCircle = ImageUtility.preCreateGlowingCircle(Color.RED,1, 500, 0.8, 0, 0);
		fruit = ImageUtility.precreatedLightedAndShadedImage("apple.png", Settings.GlOBAL_ILLUMINATION, 0,
				150 / GameLoader.ResolutionScaleX, 192 / GameLoader.ResolutionScaleY);
		fruit2 = ImageUtility.preCreateShadedCircle(Color.RED, Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP+0.4,
				10 / GameLoader.ResolutionScaleX, 10 / GameLoader.ResolutionScaleY);
		fruit3 = ImageUtility.preCreateShadedGlowingCircle(Color.RED, Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP + 0.4, 10 / GameLoader.ResolutionScaleX, 10 / GameLoader.ResolutionScaleY);
		dirt = ImageUtility.precreatedLightedImage("sand-grain.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 20 / GameLoader.ResolutionScaleX, 20 / GameLoader.ResolutionScaleY);
		snakeBones = ImageUtility.precreatedLightedAndShadedSnake("snake-skeleton.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		snakeSkull = ImageUtility.precreatedLightedAndShadedSnake("snake-skull.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * Player one
		 */
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		snakeHead = ImageUtility.precreatedLightedImage("desert-snake-head3.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeBlinking = ImageUtility.precreatedLightedImage("desert-snake-head-blink3.png",
				Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX,
				97 / GameLoader.ResolutionScaleY);
		snakeEating = ImageUtility.precreatedLightedImage("desert-snake-head-eat3.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeBody = ImageUtility.precreatedLightedImage("snake-skin2.0.png", Settings.GlOBAL_ILLUMINATION - 0.2,
				Settings.SPECULAR_MAP, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		snakeTail = ImageUtility.precreatedLightedAndShadedSnake("desert-snake-tail2.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 114 / GameLoader.ResolutionScaleX, 243 / GameLoader.ResolutionScaleY);
		snakeDebris = ImageUtility.precreatedLightedImage("snake-skin2.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 5 / GameLoader.ResolutionScaleX, 5 / GameLoader.ResolutionScaleY);
		snakeSphere = new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(snakeHead));
		slither = new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(snakeHead));

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Player 2
		 */
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		snakeHead2 = ImageUtility.precreatedLightedImage("desert-snake-head5.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeBlinking2 = ImageUtility.precreatedLightedImage("desert-snake-head-blink5.png",
				Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX,
				97 / GameLoader.ResolutionScaleY);
		snakeEating2 = ImageUtility.precreatedLightedImage("desert-snake-head-eat5.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeBody2 = ImageUtility.precreatedLightedImage("snake-skin3.0.png", Settings.GlOBAL_ILLUMINATION - 0.2,
				Settings.SPECULAR_MAP, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		snakeDebris2 = ImageUtility.precreatedLightedImage("snake-skin3.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 5 / GameLoader.ResolutionScaleX, 5 / GameLoader.ResolutionScaleY);
		snakeSphere2 = new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(snakeHead2));
		/**
		 * Images used for other levels
		 */

		/**
		 * General Images
		 */
		hud_bar = new Image(ImageUtility.loadResource("hud_bar.png"));
		hud_bar_cover = new Image(ImageUtility.loadResource("hud_bar_cover.png"));
		apple = new Image(ImageUtility.loadResource("apple.png"));
		countKeeper = new Image(ImageUtility.loadResource("hud_bar_info_thin.png"));
		hud_timer = new Image(ImageUtility.loadResource("hud_timer.png"));
		levelCompleteSplash = new Image(ImageUtility.loadResource("levelComplete2.png"));
		gameOverScreen = new Image(ImageUtility.loadResource("gameOver_board.png"));
		continueOpt = new Image(ImageUtility.loadResource("continue.png"));
		restartOpt = new Image(ImageUtility.loadResource("restart.png"));
		quitOpt = new Image(ImageUtility.loadResource("quit.png"));
		continueOpt2 = new Image(ImageUtility.loadResource("continue_button_alt.png"));
		restartOpt2 = new Image(ImageUtility.loadResource("restartSelected.png"));
		quitOpt2 = new Image(ImageUtility.loadResource("quitSelected.png"));
		health_bar_green_one = new Image(ImageUtility.loadResource("health_bar_green.png"));
		health_bar_green_two = new Image(ImageUtility.loadResource("health_bar_green_two.png"));
		health_bar_red_one = new Image(ImageUtility.loadResource("health_bar_red_border.png"));
		health_bar_red_two = new Image(ImageUtility.loadResource("health_bar_red_border_two.png"));
		player_one_hud = new Image(ImageUtility.loadResource("player_one_hud.png"));
		player_two_hud = new Image(ImageUtility.loadResource("player_two_hud.png"));
		player_one_wins = new Image(ImageUtility.loadResource("player_one_wins.png"));
		player_two_wins = new Image(ImageUtility.loadResource("player_two_wins.png"));
		options_board = new Image(ImageUtility.loadResource("options_board.png"));
		continue_button = new Image(ImageUtility.loadResource("continue_button.png"));
		restart_button = new Image(ImageUtility.loadResource("restart_button.png"));
		quit_button = new Image(ImageUtility.loadResource("quit_button.png"));
		draw_game = new Image(ImageUtility.loadResource("game_draw_board.png"));


		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	public static String loadResource(String resource) {
		return GameImageBank.class.getResource(resource).toExternalForm();
	}

}
