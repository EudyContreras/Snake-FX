	package com.EudyContreras.Snake.ImageBanks;

	import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Utilities.ImageEffectUtility;
import com.EudyContreras.Snake.Utilities.ImageLoadingUtility;

import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

	/**
	 * This is the class where we load all images. we first precreate a shaded,
	 * lighted, glowing or regular image here and then we use it in the game. This
	 * way images are only loaded once and the game is able to reused the image an
	 * unlimited amount of times without reinitializing it. We are able to create
	 * any combination of post effect image with a simple Image utility I have created
	 * which will apply effects to an image and will then take a snapshot of that
	 * image and save it o memory.
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
			public static final Image glowingCircleTwo= ImageEffectUtility.preCreateAlternateGlowingCircle(Color.RED,1, 300, 0.6, 20);
			public static final Image glowingCircleThree= ImageEffectUtility.preCreateAlternateGlowingCircleTwo(Color.RED,1, 500, 0.8, 0, 0);
			public static final Image fruit = ImageEffectUtility.precreatedLightedAndShadedImage("apple.png", GameSettings.GlOBAL_ILLUMINATION, 0,
					150, 192 );
			public static final Image classicSnakeFruit = ImageEffectUtility.preCreateShadedCircle(Color.RED, GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, GameSettings.PLAYER_ONE_SIZE);
			public static final Image fruitDebrisOne = ImageEffectUtility.preCreateShadedCircle(Color.RED, GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY,
					10);
			public static final Image fruitDebrisTwo = ImageEffectUtility.preCreateShadedGlowingCircle(Color.RED, GameSettings.GlOBAL_ILLUMINATION * 1.1,
					GameSettings.GLOBAL_SPECULARITY*0.8, 10, 10 );
			public static final Image sand_grain = ImageEffectUtility.precreatedLightedImage("sand_grain.png", GameSettings.GlOBAL_ILLUMINATION * 1.3,
					0, 20, 20 );
			public static final Image snakeBones = ImageEffectUtility.precreatedLightedImage("snake-bones.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY-0.5, 197, 176 );
			public static final Image snakeSkull = ImageEffectUtility.precreatedLightedImage("snake-skull.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY-0.5, 111, 97 );
			public static final Image snakeTail = ImageEffectUtility.precreatedLightedAndShadedTail("desert-snake-tail2.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY, 114, 243 );
			public static final Image dirt_grain = ImageEffectUtility.precreatedLightedImage("dirt_grain.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY-1.2, 20, 20 );
			public static final Image dirt = ImageEffectUtility.precreatedLightedImage("dirt_grain.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY-0.8, 20, 20 );
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			/*
			 * Player one
			 */
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			public static final Image snakeOneHead = ImageEffectUtility.precreatedLightedImage("desert-snake-head3.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY, 111, 97 );
			public static final Image snakeOneBlinking = ImageEffectUtility.precreatedLightedImage("desert-snake-head-blink3.png",
					GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 111,
					97 );
			public static final Image snakeOneEating = ImageEffectUtility.precreatedLightedImage("desert-snake-head-eat3.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY, 111, 97 );
			public static final Image snakeOneHeadBlurred = ImageEffectUtility.preCreateImageWithMotionBlur("desert-snake-head-eat3.png", 145,
					145 );
			public static final Image snakeOneSkin = ImageEffectUtility.precreatedLightedAndShadedSnake("snake_skin_one.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY, 197, 176 );
			public static final Image snakeOneSkinBlurred = ImageEffectUtility.preCreateImageWithMotionBlur("snake_skin_one.png", 197,
					176 );
			public static final Image snakeOneDebris = ImageEffectUtility.precreatedLightedImage("snake_skin_one_debris.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY-1.5, 5, 5 );
			public static final Circle snakeOneSphere = new Circle(GameSettings.PLAYER_ONE_SIZE, new ImagePattern(snakeOneHead));
			public static final Circle slither = new Circle(GameSettings.SLITHER_SIZE * 1.4, new ImagePattern(snakeOneHead));
			public static final ImagePattern normalPatternOneHead = new ImagePattern(snakeOneHead);
			public static final ImagePattern speedPatternOneHead = new ImagePattern(snakeOneHeadBlurred);
			public static final ImagePattern normalPatternOne = new ImagePattern(snakeOneSkin);
			public static final ImagePattern speedPatternOne = new ImagePattern(snakeOneSkinBlurred);
			public static final ImagePattern snakeOneBody = new ImagePattern(snakeOneSkin);


			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			/**
			 * Player 2
			 */
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			public static final Image snakeTwoHead = ImageEffectUtility.precreatedLightedImage("desert-snake-head5.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY, 111, 97 );
			public static final Image snakeTwoBlinking = ImageEffectUtility.precreatedLightedImage("desert-snake-head-blink5.png",
					GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 111,
					97 );
			public static final Image snakeTwoEating = ImageEffectUtility.precreatedLightedImage("desert-snake-head-eat5.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY, 111, 97 );
			public static final Image snakeTwoSkin = ImageEffectUtility.precreatedLightedAndShadedSnake("snake_skin_two.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY, 197, 176 );
			public static final Image snakeTwoSkinBlurred = ImageEffectUtility.preCreateImageWithMotionBlur("snake_skin_two.png", 197,
					176 );
			public static final Image snakeTwoDebris = ImageEffectUtility.precreatedLightedImage("snake_skin_two_debris.png", GameSettings.GlOBAL_ILLUMINATION,
					GameSettings.GLOBAL_SPECULARITY-1.5, 5, 5 );
			public static final Circle snakeTwoSphere = new Circle(GameSettings.PLAYER_TWO_SIZE, new ImagePattern(snakeTwoHead));
			public static final ImagePattern tailImage = new ImagePattern(snakeTail);
			public static final ImagePattern snakeTwoBody = new ImagePattern(snakeTwoSkin);
			public static final ImagePattern normalPatternTwo = new ImagePattern(snakeTwoSkin);
			public static final ImagePattern speedPatternTwo = new ImagePattern(snakeTwoSkinBlurred);


			/**
			 * heads up display elements
			 */


			public static final Image hud_bar = new Image(ImageLoadingUtility.loadResource("hud_bar_plain.png"));
			public static final Image hud_bar_orange = new Image(ImageLoadingUtility.loadResource("hud_bar_orange.png"),GameSettings.WIDTH*2,100,false, true);
			public static final Image hud_bar_black = new Image(ImageLoadingUtility.loadResource("hud_bar_black.png"),GameSettings.WIDTH+5,70,false, true);
			public static final Image clipping_bar_h = ImageEffectUtility.GLOWING_RECTANGLE(Color.ORANGE, 0, 0.35, GameSettings.WIDTH*2,100);
			public static final Image clipping_bar_v = ImageEffectUtility.GLOWING_RECTANGLE(Color.ORANGE, 0, 0.35, 100,GameSettings.HEIGHT*2);
			public static final Image horizontal_border_alt = new Image(ImageLoadingUtility.loadResource("border_h.png"));
			public static final Image horizontal_border = new Image(ImageLoadingUtility.loadResource("border_h1.png"));
			public static final Image vertical_border = new Image(ImageLoadingUtility.loadResource("border_v1.png"));
			public static final Image hud_bar_cover = new Image(ImageLoadingUtility.loadResource("hud_bar_cover.png"));
			public static final Image score_keeper_multiPlayer = new Image(ImageLoadingUtility.loadResource("hud_bar_info_thin.png"));
			public static final Image score_keeper_singlePlayer = new Image(ImageLoadingUtility.loadResource("hud_bar_info.png"));
			public static final Image hud_timer = new Image(ImageLoadingUtility.loadResource("hud_timer.png"));
			public static final Image main_multiplayer_hud = new Image(ImageLoadingUtility.loadResource("main_multiplayer_hud.png"));
			public static final Image splash_screen = new Image(ImageLoadingUtility.loadResource("SplashScreen5.png"));
			public static final Image level_complete_board = new Image(ImageLoadingUtility.loadResource("level_completed_board.png"));
			public static final Image game_over_board = new Image(ImageLoadingUtility.loadResource("gameover_board.png"));
			public static final Image red_health = new Image(ImageLoadingUtility.loadResource("red_health.png"));
			public static final Image green_health = new Image(ImageLoadingUtility.loadResource("green_health.png"));
			public static final Image health_bar_one = new Image(ImageLoadingUtility.loadResource("hud_healthbar_one.png"));
			public static final Image health_bar_two = new Image(ImageLoadingUtility.loadResource("hud_healthbar_two.png"));
			public static final Image energy = new Image(ImageLoadingUtility.loadResource("energy.png"));
			public static final Image player_one_hud = new Image(ImageLoadingUtility.loadResource("player_one_hud.png"));
			public static final Image player_two_hud = new Image(ImageLoadingUtility.loadResource("player_two_hud.png"));
			public static final Image player_one_wins = new Image(ImageLoadingUtility.loadResource("player_one_wins.png"));
			public static final Image player_two_wins = new Image(ImageLoadingUtility.loadResource("player_two_wins.png"));
			public static final Image player_one_loses = new Image(ImageLoadingUtility.loadResource("player_one_lost.png"));
			public static final Image player_two_loses = new Image(ImageLoadingUtility.loadResource("player_two_lost.png"));
			public static final Image player_score_trans_board = new Image(ImageLoadingUtility.loadResource("player_score_transition_board.png"));
			public static final Image player_score_board = new Image(ImageLoadingUtility.loadResource("player_score_board.png"));
			public static final Image game_draw_trans_board = new Image(ImageLoadingUtility.loadResource("game_draw_transition_board.png"));
			public static final Image game_draw_score_board = new Image(ImageLoadingUtility.loadResource("game_draw_score_board.png"));
			public static final Image game_over_trans_board = new Image(ImageLoadingUtility.loadResource("game_over_transition_board.png"));
			public static final Image game_over_score_board = new Image(ImageLoadingUtility.loadResource("game_over_score_board.png"));
			public static final Image options_board = new Image(ImageLoadingUtility.loadResource("options_board.png"));
			public static final Image continue_button = new Image(ImageLoadingUtility.loadResource("continue_button.png"));
			public static final Image continue_button_alt = new Image(ImageLoadingUtility.loadResource("continue_button_alt.png"));
			public static final Image restart_button = new Image(ImageLoadingUtility.loadResource("restart_button.png"));
			public static final Image quit_button = new Image(ImageLoadingUtility.loadResource("quit_button.png"));
			public static final Image draw_game = new Image(ImageLoadingUtility.loadResource("game_draw_board.png"));
			public static final Image pause_menu = new Image(ImageLoadingUtility.loadResource("pause_menu_panel.png"));
			public static final Image pause_menu_classic = new Image(ImageLoadingUtility.loadResource("pause_menu_panel_black.png"));
			public static final Image pause_continue = new Image(ImageLoadingUtility.loadResource("pause_continue.png"));
			public static final Image pause_restart = new Image(ImageLoadingUtility.loadResource("pause_restart.png"));
			public static final Image pause_main = new Image(ImageLoadingUtility.loadResource("pause_main_menu.png"));
			public static final Image pause_quit = new Image(ImageLoadingUtility.loadResource("pause_quit.png"));
			public static final Image ready_notification = new Image(ImageLoadingUtility.loadResource("ready.png"));
			public static final ImagePattern count_one = new ImagePattern(new Image(ImageLoadingUtility.loadResource("counter_one.png")));
			public static final ImagePattern count_two = new ImagePattern(new Image(ImageLoadingUtility.loadResource("counter_two.png")));
			public static final ImagePattern count_three = new ImagePattern(new Image(ImageLoadingUtility.loadResource("counter_three.png")));
			public static final ImagePattern count_go = new ImagePattern(new Image(ImageLoadingUtility.loadResource("counter_go_alt.png")));


			public static final Image Exit = new Image(ImageLoadingUtility.loadResource("exit.png"));
			public static final Image appIcon = new Image(ImageLoadingUtility.loadResource("snake.png"));
			public static final Image snakeIcon = new Image(ImageLoadingUtility.loadResource("snakeIcon.png"));
			public static ImageCursor normalCursor = new ImageCursor(new Image(ImageLoadingUtility.loadResource("normalCursor.png")));
			public static ImageCursor dragCursor = new ImageCursor(new Image(ImageLoadingUtility.loadResource("dragMarker.png")));
			public static ImageCursor stretchCursor = new ImageCursor(new Image(ImageLoadingUtility.loadResource("stretchMarker.png")));
			public static ImageCursor horizontalDrag = new ImageCursor(new Image(ImageLoadingUtility.loadResource("horizontalDrag.png")));
			public static ImageCursor verticalDrag = new ImageCursor(new Image(ImageLoadingUtility.loadResource("verticalDrag.png")));
			public static ImageCursor diagonalDragNE_SW = new ImageCursor(new Image(ImageLoadingUtility.loadResource("diagonalDragNE_SW.png")));
			public static ImageCursor diagonalDragNW_SE = new ImageCursor(new Image(ImageLoadingUtility.loadResource("diagonalDragNW_SE.png")));
		/**
		 * general game object
		 */
			public static final Image apple = new Image(ImageLoadingUtility.loadResource("apple.png"));
			public static final Image apple_gold = new Image(ImageLoadingUtility.loadResource("apple_golden.png"));
			public static final Image apple_alt = new Image(ImageLoadingUtility.loadResource("apple_alt.png"));
			public static final Image award_icon_gold = new Image(ImageLoadingUtility.loadResource("award_icon_gold.png"));
			public static final Image classicSnakeHead = ImageEffectUtility.preCreateShadedCircle(Color.LIME, GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 30);
			public static final Image classicSnakeBody = ImageEffectUtility.preCreateShadedCircle(Color.GREEN, GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 30);
			public static final Image classicSnakeBodyDebris = ImageEffectUtility.preCreateAlternateGlowingCircle(Color.GREEN,1, 300, 0.2, GameSettings.PLAYER_ONE_SIZE*5);
			public static final Image classicSnakeBodyBlurred = ImageEffectUtility.preCreateShadedBlurredCircle(Color.GREEN, GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, GameSettings.PLAYER_ONE_SIZE);
			public static final Image transparentFill = ImageEffectUtility.preCreateCircle(Color.GREEN, GameSettings.PLAYER_ONE_SIZE*.75);


			public static final Image play_room_hub_bar = new Image(ImageLoadingUtility.loadResource("connect_hub.png"));
			public static final Image default_user_pic = new Image(ImageLoadingUtility.loadResource("default-user.png"));
			public static final Image profile_default_male = new Image(ImageLoadingUtility.loadResource("user_placeholder_male.png"));
			public static final Image profile_default_female = new Image(ImageLoadingUtility.loadResource("user_placeholder_female.png"));
			public static final Image play_engine_logo = new Image(ImageLoadingUtility.loadResource("fx_play_engine_logo.png"));
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public static final String loadResource(String resource) {
		return GameImageBank.class.getResource(resource).toExternalForm();
	}

}
