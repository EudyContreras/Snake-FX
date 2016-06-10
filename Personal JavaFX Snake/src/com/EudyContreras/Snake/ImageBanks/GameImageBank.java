package com.EudyContreras.Snake.ImageBanks;

import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Utilities.ImageEffectUtility;

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
	public static Image glowingCircleThree;
	public static Image shadedImage;
	public static Image preLightedDebris;
	public static Image snakeOneHead;
	public static Image snakeOneSkin;
	public static Image snakeOneSkinBlurred;
	public static Image snakeOneDebris;
	public static Image snakeOneBlinking;
	public static Image snakeOneEating;
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
	public static Image snakeTwoSkinBlurred;
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
	public static Image player_score_trans_board;
	public static Image player_score_board;
	public static Image game_over_score_board;
	public static Image game_over_trans_board;
	public static Image hud_timer;
	public static Image splash_screen;
	public static Image player_one_hud;
	public static Image player_two_hud;
	public static Image player_one_wins;
	public static Image player_two_wins;
	public static Image player_two_loses;
	public static Image player_one_loses;
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
	public static Image dirt_grain;
	public static Image sand_grain;
	public static Circle slither;
	public static Circle snakeOneSphere;
	public static Circle snakeTwoSphere;
	public static ImagePattern tailImage;
	public static ImagePattern snakeOneBody;
	public static ImagePattern snakeTwoBody;
	public static ImagePattern speedPatternOne;
	public static ImagePattern normalPatternOne;
	public static ImagePattern speedPatternTwo;
	public static ImagePattern normalPatternTwo;
	public static Image energy_bar_one;
	public static Image energy_bar_one_border;
	public static Image energy_bar_two;
	public static Image energy_bar_two_border;
	public static ImagePattern count_one;
	public static ImagePattern count_two;
	public static ImagePattern count_three;
	public static ImagePattern count_go;

	public GameImageBank() {
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 /*
		 * Game objects
		 *
		 */
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		glowingCircleOne = ImageEffectUtility.preCreateGlowingCircle(Color.RED,1, 500, 0.8, 0, 0);
		glowingCircleTwo= ImageEffectUtility.preCreateAlternateGlowingCircle(Color.RED,1, 500, 0.8, 0, 0);
		glowingCircleThree= ImageEffectUtility.preCreateAlternateGlowingCircleTwo(Color.RED,1, 500, 0.8, 0, 0);
		fruit = ImageEffectUtility.precreatedLightedAndShadedImage("apple.png", GameSettings.GlOBAL_ILLUMINATION, 0,
				150 / GameLoader.ResolutionScaleX, 192 / GameLoader.ResolutionScaleY);
		fruitDebrisOne = ImageEffectUtility.preCreateShadedCircle(Color.RED, GameSettings.GlOBAL_ILLUMINATION-0.8, GameSettings.GLOBAL_SPECULARITY,
				10 / GameLoader.ResolutionScaleX, 10 / GameLoader.ResolutionScaleY);
		fruitDebrisTwo = ImageEffectUtility.preCreateShadedGlowingCircle(Color.RED, GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY + 0.4, 10 / GameLoader.ResolutionScaleX, 10 / GameLoader.ResolutionScaleY);
		sand_grain = ImageEffectUtility.precreatedLightedImage("sand_grain.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 20 / GameLoader.ResolutionScaleX, 20 / GameLoader.ResolutionScaleY);
		snakeBones = ImageEffectUtility.precreatedLightedAndShadedSnake("snake-bones.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY-0.5, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		snakeSkull = ImageEffectUtility.precreatedLightedAndShadedSnake("snake-skull.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY-0.5, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeTail = ImageEffectUtility.precreatedLightedAndShadedSnake("desert-snake-tail2.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 114 / GameLoader.ResolutionScaleX, 243 / GameLoader.ResolutionScaleY);
		dirt_grain = ImageEffectUtility.precreatedLightedImage("dirt_grain.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY-0.8, 20 / GameLoader.ResolutionScaleX, 20 / GameLoader.ResolutionScaleY);
		dirt = ImageEffectUtility.precreatedLightedImage("dirt_grain.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY-0.8, 20 / GameLoader.ResolutionScaleX, 20 / GameLoader.ResolutionScaleY);
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * Player one
		 */
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		snakeOneHead = ImageEffectUtility.precreatedLightedImage("desert-snake-head3.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeOneBlinking = ImageEffectUtility.precreatedLightedImage("desert-snake-head-blink3.png",
				GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX,
				97 / GameLoader.ResolutionScaleY);
		snakeOneEating = ImageEffectUtility.precreatedLightedImage("desert-snake-head-eat3.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeOneSkin = ImageEffectUtility.precreatedLightedAndShadedSnake("snake_skin_one.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		snakeOneSkinBlurred = ImageEffectUtility.preCreateImageWithMotionBlur("snake_skin_one.png", 205 / GameLoader.ResolutionScaleX,
				183 / GameLoader.ResolutionScaleY);
		snakeOneDebris = ImageEffectUtility.precreatedLightedImage("snake_skin_one_debris.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 5 / GameLoader.ResolutionScaleX, 5 / GameLoader.ResolutionScaleY);
		snakeOneSphere = new Circle(GameSettings.PLAYER_ONE_SIZE, new ImagePattern(snakeOneHead));
		slither = new Circle(GameSettings.SLITHER_SIZE * 1.4, new ImagePattern(snakeOneHead));
		normalPatternOne = new ImagePattern(snakeOneSkin);
		speedPatternOne = new ImagePattern(snakeOneSkinBlurred);
		snakeOneBody = new ImagePattern(snakeOneSkin);


		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Player 2
		 */
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		snakeTwoHead = ImageEffectUtility.precreatedLightedImage("desert-snake-head5.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeTwoBlinking = ImageEffectUtility.precreatedLightedImage("desert-snake-head-blink5.png",
				GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX,
				97 / GameLoader.ResolutionScaleY);
		snakeTwoEating = ImageEffectUtility.precreatedLightedImage("desert-snake-head-eat5.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		snakeTwoSkin = ImageEffectUtility.precreatedLightedAndShadedSnake("snake_skin_two.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		snakeTwoSkinBlurred = ImageEffectUtility.preCreateImageWithMotionBlur("snake_skin_two.png", 205 / GameLoader.ResolutionScaleX,
				183 / GameLoader.ResolutionScaleY);
		snakeTwoDebris = ImageEffectUtility.precreatedLightedImage("snake_skin_two_debris.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 5 / GameLoader.ResolutionScaleX, 5 / GameLoader.ResolutionScaleY);
		snakeTwoSphere = new Circle(GameSettings.PLAYER_TWO_SIZE, new ImagePattern(snakeTwoHead));
		tailImage = new ImagePattern(snakeTail);
		snakeTwoBody = new ImagePattern(snakeTwoSkin);
		normalPatternTwo = new ImagePattern(snakeTwoSkin);
		speedPatternTwo = new ImagePattern(snakeTwoSkinBlurred);
		/**
		 * Images used for other levels
		 */

		/**
		 * heads up display elements
		 */
		hud_bar = new Image(ImageEffectUtility.loadResource("hud_bar.png"));
		hud_bar_cover = new Image(ImageEffectUtility.loadResource("hud_bar_cover.png"));
		score_keeper = new Image(ImageEffectUtility.loadResource("hud_bar_info_thin.png"));
		hud_timer = new Image(ImageEffectUtility.loadResource("hud_timer.png"));
		splash_screen = new Image(ImageEffectUtility.loadResource("SplashScreen5.png"));
		level_complete_board = new Image(ImageEffectUtility.loadResource("level_completed_board.png"));
		game_over_board = new Image(ImageEffectUtility.loadResource("gameover_board.png"));
		health_bar_green_one = new Image(ImageEffectUtility.loadResource("health_bar_green.png"));
		health_bar_green_two = new Image(ImageEffectUtility.loadResource("health_bar_green_two.png"));
		health_bar_red_one = new Image(ImageEffectUtility.loadResource("health_bar_red_border.png"));
		health_bar_red_two = new Image(ImageEffectUtility.loadResource("health_bar_red_border_two.png"));
		energy_bar_one = new Image(ImageEffectUtility.loadResource("energy_bar_one.png"));
		energy_bar_two = new Image(ImageEffectUtility.loadResource("energy_bar_two.png"));
		energy_bar_one_border = new Image(ImageEffectUtility.loadResource("energy_bar_one_border.png"));
		energy_bar_two_border = new Image(ImageEffectUtility.loadResource("energy_bar_two_border.png"));
		player_one_hud = new Image(ImageEffectUtility.loadResource("score_board_one.png"));
		player_two_hud = new Image(ImageEffectUtility.loadResource("score_board_two.png"));
		player_one_wins = new Image(ImageEffectUtility.loadResource("player_one_wins.png"));
		player_two_wins = new Image(ImageEffectUtility.loadResource("player_two_wins.png"));
		player_one_loses = new Image(ImageEffectUtility.loadResource("player_one_lost.png"));
		player_two_loses = new Image(ImageEffectUtility.loadResource("player_two_lost.png"));
		player_score_trans_board = new Image(ImageEffectUtility.loadResource("player_score_transition_board.png"));
		player_score_board = new Image(ImageEffectUtility.loadResource("player_score_board.png"));
		game_over_trans_board = new Image(ImageEffectUtility.loadResource("game_over_transition_board.png"));
		game_over_score_board = new Image(ImageEffectUtility.loadResource("game_over_score_board.png"));
		options_board = new Image(ImageEffectUtility.loadResource("options_board.png"));
		continue_button = new Image(ImageEffectUtility.loadResource("continue_button.png"));
		continue_button_alt = new Image(ImageEffectUtility.loadResource("continue_button_alt.png"));
		restart_button = new Image(ImageEffectUtility.loadResource("restart_button.png"));
		quit_button = new Image(ImageEffectUtility.loadResource("quit_button.png"));
		draw_game = new Image(ImageEffectUtility.loadResource("game_draw_board.png"));
		pause_menu = new Image(ImageEffectUtility.loadResource("pause_menu_panel.png"));
		pause_continue = new Image(ImageEffectUtility.loadResource("pause_continue.png"));
		pause_restart = new Image(ImageEffectUtility.loadResource("pause_restart.png"));
		pause_main = new Image(ImageEffectUtility.loadResource("pause_main_menu.png"));
		pause_quit = new Image(ImageEffectUtility.loadResource("pause_quit.png"));
		count_one = new ImagePattern(new Image(ImageEffectUtility.loadResource("counter_one.png")));
		count_two = new ImagePattern(new Image(ImageEffectUtility.loadResource("counter_two.png")));
		count_three = new ImagePattern(new Image(ImageEffectUtility.loadResource("counter_three.png")));
		count_go = new ImagePattern(new Image(ImageEffectUtility.loadResource("counter_go.png")));
		/**
		 * general game object
		 */
		apple = new Image(ImageEffectUtility.loadResource("apple.png"));

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	public static String loadResource(String resource) {
		return GameImageBank.class.getResource(resource).toExternalForm();
	}

}
