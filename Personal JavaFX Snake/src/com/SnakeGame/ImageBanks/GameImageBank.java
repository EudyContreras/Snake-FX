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
	public static Image glowingCircleOne;
	public static Image glowingCircleTwo;
	public static Image shadedImage;
	public static Image preLightedDebris;
	public static Image snakeOneHead;
	public static Image snakeOneSkin;
	public static Image snakeOneDebris;
	public static Image snakeOneBlinking;
	public static Image snakeOneEating;
	public static Circle slither;
	public static Circle snakeOneSphere;
	public static Circle snakeTwoSphere;
	public static Image snakeTail;
	public static Image snakeBones;
	public static Image snakeSkull;
	public static Image dirt;
	public static Image apple;
	public static Image fruit;
	public static Image fruitDebrisOne;
	public static Image fruitDebrisTwo;
	public static Image snakeTwoHead;
	public static Image snakeTwoBlinking;
	public static Image snakeTwoEating;
	public static Image snakeTwoSkin;
	public static Image snakeTwoDebris;
	public static Image score_keeper;
	public static Image level_complete_board;
	public static Image game_over_board;
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
	public static Image continue_button_alt;
	public static Image continue_button;
	public static Image restart_button;
	public static Image pause_menu;
	public static Image pause_main;
	public static Image pause_quit;
	public static Image pause_continue;
	public static Image pause_restart;
	public static Image quit_button;
	public static Image draw_game;
	public static ImagePattern tailImage;
	public static ImagePattern snakeOneBody;
	public static ImagePattern snakeTwoBody;

	public GameImageBank() {
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 /*
		 * Game objects
		 *
		 */
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		glowingCircleOne = ImageUtility.preCreateGlowingCircle(Color.RED,1, 500, 0.8, 0, 0);
		fruit = ImageUtility.precreatedLightedAndShadedImage("apple.png", Settings.GlOBAL_ILLUMINATION, 0,
				150 / GameLoader.ResolutionScaleX, 192 / GameLoader.ResolutionScaleY);
		fruitDebrisOne = ImageUtility.preCreateShadedCircle(Color.RED, Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP+0.4,
				10 / GameLoader.ResolutionScaleX, 10 / GameLoader.ResolutionScaleY);
		fruitDebrisTwo = ImageUtility.preCreateShadedGlowingCircle(Color.RED, Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP + 0.4, 10 / GameLoader.ResolutionScaleX, 10 / GameLoader.ResolutionScaleY);
		dirt = ImageUtility.precreatedLightedImage("sand-grain.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 20 / GameLoader.ResolutionScaleX, 20 / GameLoader.ResolutionScaleY);
		snakeBones = ImageUtility.precreatedLightedAndShadedSnake("snake-bones.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		snakeSkull = ImageUtility.precreatedLightedImage("snake-skull.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * Player one
		 */
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		snakeOneHead = ImageUtility.precreatedLightedImage("desert-snake-head3.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeOneBlinking = ImageUtility.precreatedLightedImage("desert-snake-head-blink3.png",
				Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX,
				97 / GameLoader.ResolutionScaleY);
		snakeOneEating = ImageUtility.precreatedLightedImage("desert-snake-head-eat3.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeOneSkin = ImageUtility.precreatedLightedAndShadedSnake("snake_skin_one.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		snakeTail = ImageUtility.precreatedLightedAndShadedSnake("desert-snake-tail2.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 114 / GameLoader.ResolutionScaleX, 243 / GameLoader.ResolutionScaleY);
		snakeOneDebris = ImageUtility.precreatedLightedImage("snake_skin_one_debris.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 5 / GameLoader.ResolutionScaleX, 5 / GameLoader.ResolutionScaleY);
		snakeOneSphere = new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(snakeOneHead));
		slither = new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(snakeOneHead));
		snakeOneBody = new ImagePattern(snakeOneSkin);

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Player 2
		 */
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		snakeTwoHead = ImageUtility.precreatedLightedImage("desert-snake-head5.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeTwoBlinking = ImageUtility.precreatedLightedImage("desert-snake-head-blink5.png",
				Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX,
				97 / GameLoader.ResolutionScaleY);
		snakeTwoEating = ImageUtility.precreatedLightedImage("desert-snake-head-eat5.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeTwoSkin = ImageUtility.precreatedLightedAndShadedSnake("snake_skin_two.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		snakeTwoDebris = ImageUtility.precreatedLightedImage("snake_skin_two_debris.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 5 / GameLoader.ResolutionScaleX, 5 / GameLoader.ResolutionScaleY);
		snakeTwoSphere = new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(snakeTwoHead));
		tailImage = new ImagePattern(snakeTail);
		snakeTwoBody = new ImagePattern(snakeTwoSkin);
		/**
		 * Images used for other levels
		 */

		/**
		 * heads up display elements
		 */
		hud_bar = new Image(ImageUtility.loadResource("hud_bar.png"));
		hud_bar_cover = new Image(ImageUtility.loadResource("hud_bar_cover.png"));
		score_keeper = new Image(ImageUtility.loadResource("hud_bar_info_thin.png"));
		hud_timer = new Image(ImageUtility.loadResource("hud_timer.png"));
		level_complete_board = new Image(ImageUtility.loadResource("levelComplete2.png"));
		game_over_board = new Image(ImageUtility.loadResource("gameover_board.png"));
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
		continue_button_alt = new Image(ImageUtility.loadResource("continue_button_alt.png"));
		restart_button = new Image(ImageUtility.loadResource("restart_button.png"));
		quit_button = new Image(ImageUtility.loadResource("quit_button.png"));
		draw_game = new Image(ImageUtility.loadResource("game_draw_board.png"));
		pause_menu = new Image(ImageUtility.loadResource("pause_menu_panel.png"));
		pause_continue = new Image(ImageUtility.loadResource("pause_continue.png"));
		pause_restart = new Image(ImageUtility.loadResource("pause_restart.png"));
		pause_main = new Image(ImageUtility.loadResource("pause_main_menu.png"));
		pause_quit = new Image(ImageUtility.loadResource("pause_quit.png"));


		/**
		 * general game object
		 */
		apple = new Image(ImageUtility.loadResource("apple.png"));

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	public static String loadResource(String resource) {
		return GameImageBank.class.getResource(resource).toExternalForm();
	}

}
