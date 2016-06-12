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

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 /*
		 * Game objects
		 *
		 */
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public static final Image glowingCircleOne = ImageEffectUtility.preCreateGlowingCircle(Color.RED,1, 500, 0.8, 0, 0);
		public static final Image glowingCircleTwo= ImageEffectUtility.preCreateAlternateGlowingCircle(Color.RED,1, 500, 0.8, 0, 0);
		public static final Image glowingCircleThree= ImageEffectUtility.preCreateAlternateGlowingCircleTwo(Color.RED,1, 500, 0.8, 0, 0);
		public static final Image fruit = ImageEffectUtility.precreatedLightedAndShadedImage("apple.png", GameSettings.GlOBAL_ILLUMINATION, 0,
				150 / GameLoader.ResolutionScaleX, 192 / GameLoader.ResolutionScaleY);
		public static final Image fruitDebrisOne = ImageEffectUtility.preCreateShadedCircle(Color.RED, GameSettings.GlOBAL_ILLUMINATION-0.8, GameSettings.GLOBAL_SPECULARITY,
				10 / GameLoader.ResolutionScaleX, 10 / GameLoader.ResolutionScaleY);
		public static final Image fruitDebrisTwo = ImageEffectUtility.preCreateShadedGlowingCircle(Color.RED, GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY + 0.4, 10 / GameLoader.ResolutionScaleX, 10 / GameLoader.ResolutionScaleY);
		public static final Image sand_grain = ImageEffectUtility.precreatedLightedImage("sand_grain.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 20 / GameLoader.ResolutionScaleX, 20 / GameLoader.ResolutionScaleY);
		public static final Image snakeBones = ImageEffectUtility.precreatedLightedAndShadedSnake("snake-bones.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY-0.5, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		public static final Image snakeSkull = ImageEffectUtility.precreatedLightedAndShadedSnake("snake-skull.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY-0.5, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		public static final Image snakeTail = ImageEffectUtility.precreatedLightedAndShadedSnake("desert-snake-tail2.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 114 / GameLoader.ResolutionScaleX, 243 / GameLoader.ResolutionScaleY);
		public static final Image dirt_grain = ImageEffectUtility.precreatedLightedImage("dirt_grain.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY-0.8, 20 / GameLoader.ResolutionScaleX, 20 / GameLoader.ResolutionScaleY);
		public static final Image dirt = ImageEffectUtility.precreatedLightedImage("dirt_grain.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY-0.8, 20 / GameLoader.ResolutionScaleX, 20 / GameLoader.ResolutionScaleY);
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * Player one
		 */
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		public static final Image snakeOneHead = ImageEffectUtility.precreatedLightedImage("desert-snake-head3.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		public static final Image snakeOneBlinking = ImageEffectUtility.precreatedLightedImage("desert-snake-head-blink3.png",
				GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX,
				97 / GameLoader.ResolutionScaleY);
		public static final Image snakeOneEating = ImageEffectUtility.precreatedLightedImage("desert-snake-head-eat3.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		public static final Image snakeOneSkin = ImageEffectUtility.precreatedLightedAndShadedSnake("snake_skin_one.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		public static final Image snakeOneSkinBlurred = ImageEffectUtility.preCreateImageWithMotionBlur("snake_skin_one.png", 205 / GameLoader.ResolutionScaleX,
				183 / GameLoader.ResolutionScaleY);
		public static final Image snakeOneDebris = ImageEffectUtility.precreatedLightedImage("snake_skin_one_debris.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 5 / GameLoader.ResolutionScaleX, 5 / GameLoader.ResolutionScaleY);
		public static final Circle snakeOneSphere = new Circle(GameSettings.PLAYER_ONE_SIZE, new ImagePattern(snakeOneHead));
		public static final Circle slither = new Circle(GameSettings.SLITHER_SIZE * 1.4, new ImagePattern(snakeOneHead));
		public static final ImagePattern normalPatternOne = new ImagePattern(snakeOneSkin);
		public static final ImagePattern speedPatternOne = new ImagePattern(snakeOneSkinBlurred);
		public static final ImagePattern snakeOneBody = new ImagePattern(snakeOneSkin);


		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Player 2
		 */
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public static final Image snakeTwoHead = ImageEffectUtility.precreatedLightedImage("desert-snake-head5.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		public static final Image snakeTwoBlinking = ImageEffectUtility.precreatedLightedImage("desert-snake-head-blink5.png",
				GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX,
				97 / GameLoader.ResolutionScaleY);
		public static final Image snakeTwoEating = ImageEffectUtility.precreatedLightedImage("desert-snake-head-eat5.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);
		public static final Image snakeTwoSkin = ImageEffectUtility.precreatedLightedAndShadedSnake("snake_skin_two.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);
		public static final Image snakeTwoSkinBlurred = ImageEffectUtility.preCreateImageWithMotionBlur("snake_skin_two.png", 205 / GameLoader.ResolutionScaleX,
				183 / GameLoader.ResolutionScaleY);
		public static final Image snakeTwoDebris = ImageEffectUtility.precreatedLightedImage("snake_skin_two_debris.png", GameSettings.GlOBAL_ILLUMINATION,
				GameSettings.GLOBAL_SPECULARITY, 5 / GameLoader.ResolutionScaleX, 5 / GameLoader.ResolutionScaleY);
		public static final Circle snakeTwoSphere = new Circle(GameSettings.PLAYER_TWO_SIZE, new ImagePattern(snakeTwoHead));
		public static final ImagePattern tailImage = new ImagePattern(snakeTail);
		public static final ImagePattern snakeTwoBody = new ImagePattern(snakeTwoSkin);
		public static final ImagePattern normalPatternTwo = new ImagePattern(snakeTwoSkin);
		public static final ImagePattern speedPatternTwo = new ImagePattern(snakeTwoSkinBlurred);
		/**
		 * Images used for other levels
		 */

		/**
		 * heads up display elements
		 */
		public static final Image hud_bar = new Image(ImageEffectUtility.loadResource("hud_bar.png"));
		public static final Image hud_bar_cover = new Image(ImageEffectUtility.loadResource("hud_bar_cover.png"));
		public static final Image score_keeper = new Image(ImageEffectUtility.loadResource("hud_bar_info_thin.png"));
		public static final Image hud_timer = new Image(ImageEffectUtility.loadResource("hud_timer.png"));
		public static final Image splash_screen = new Image(ImageEffectUtility.loadResource("SplashScreen5.png"));
		public static final Image level_complete_board = new Image(ImageEffectUtility.loadResource("level_completed_board.png"));
		public static final Image game_over_board = new Image(ImageEffectUtility.loadResource("gameover_board.png"));
		public static final Image health_bar_green_one = new Image(ImageEffectUtility.loadResource("health_bar_green.png"));
		public static final Image health_bar_green_two = new Image(ImageEffectUtility.loadResource("health_bar_green_two.png"));
		public static final Image health_bar_red_one = new Image(ImageEffectUtility.loadResource("health_bar_red_border.png"));
		public static final Image health_bar_red_two = new Image(ImageEffectUtility.loadResource("health_bar_red_border_two.png"));
		public static final Image energy_bar_one = new Image(ImageEffectUtility.loadResource("energy_bar_one.png"));
		public static final Image energy_bar_two = new Image(ImageEffectUtility.loadResource("energy_bar_two.png"));
		public static final Image energy_bar_one_border = new Image(ImageEffectUtility.loadResource("energy_bar_one_border.png"));
		public static final Image energy_bar_two_border = new Image(ImageEffectUtility.loadResource("energy_bar_two_border.png"));
		public static final Image player_one_hud = new Image(ImageEffectUtility.loadResource("score_board_one.png"));
		public static final Image player_two_hud = new Image(ImageEffectUtility.loadResource("score_board_two.png"));
		public static final Image player_one_wins = new Image(ImageEffectUtility.loadResource("player_one_wins.png"));
		public static final Image player_two_wins = new Image(ImageEffectUtility.loadResource("player_two_wins.png"));
		public static final Image player_one_loses = new Image(ImageEffectUtility.loadResource("player_one_lost.png"));
		public static final Image player_two_loses = new Image(ImageEffectUtility.loadResource("player_two_lost.png"));
		public static final Image player_score_trans_board = new Image(ImageEffectUtility.loadResource("player_score_transition_board.png"));
		public static final Image player_score_board = new Image(ImageEffectUtility.loadResource("player_score_board.png"));
		public static final Image game_draw_trans_board = new Image(ImageEffectUtility.loadResource("game_draw_transition_board.png"));
		public static final Image game_draw_score_board = new Image(ImageEffectUtility.loadResource("game_draw_score_board.png"));
		public static final Image game_over_trans_board = new Image(ImageEffectUtility.loadResource("game_over_transition_board.png"));
		public static final Image game_over_score_board = new Image(ImageEffectUtility.loadResource("game_over_score_board.png"));
		public static final Image options_board = new Image(ImageEffectUtility.loadResource("options_board.png"));
		public static final Image continue_button = new Image(ImageEffectUtility.loadResource("continue_button.png"));
		public static final Image continue_button_alt = new Image(ImageEffectUtility.loadResource("continue_button_alt.png"));
		public static final Image restart_button = new Image(ImageEffectUtility.loadResource("restart_button.png"));
		public static final Image quit_button = new Image(ImageEffectUtility.loadResource("quit_button.png"));
		public static final Image draw_game = new Image(ImageEffectUtility.loadResource("game_draw_board.png"));
		public static final Image pause_menu = new Image(ImageEffectUtility.loadResource("pause_menu_panel.png"));
		public static final Image pause_continue = new Image(ImageEffectUtility.loadResource("pause_continue.png"));
		public static final Image pause_restart = new Image(ImageEffectUtility.loadResource("pause_restart.png"));
		public static final Image pause_main = new Image(ImageEffectUtility.loadResource("pause_main_menu.png"));
		public static final Image pause_quit = new Image(ImageEffectUtility.loadResource("pause_quit.png"));
		public static final ImagePattern count_one = new ImagePattern(new Image(ImageEffectUtility.loadResource("counter_one.png")));
		public static final ImagePattern count_two = new ImagePattern(new Image(ImageEffectUtility.loadResource("counter_two.png")));
		public static final ImagePattern count_three = new ImagePattern(new Image(ImageEffectUtility.loadResource("counter_three.png")));
		public static final ImagePattern count_go = new ImagePattern(new Image(ImageEffectUtility.loadResource("counter_go.png")));
		/**
		 * general game object
		 */
		public static final Image apple = new Image(ImageEffectUtility.loadResource("apple.png"));

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public static final String loadResource(String resource) {
		return GameImageBank.class.getResource(resource).toExternalForm();
	}

}
