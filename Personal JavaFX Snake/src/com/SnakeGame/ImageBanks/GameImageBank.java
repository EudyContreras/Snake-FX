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
	public static Image hudBar;
	public static Image healthBarRed1;
	public static Image healthBarGreen2;
	public static Image healthBarGreen1;
	public static Image healthBarRed2;

	public GameImageBank() {
		  glowingCircle = ImageUtility.preCreateGlowingCircle(Color.RED,1, 500, 0.8, 0, 0);
		// redHealthBar =
		// ImageUtility.precreateSnapshot("red-HealthBar.png",400/GameLoader.ResolutionScaleX,
		// 35/GameLoader.ResolutionScaleY);
		// greenHealthBar =
		// ImageUtility.precreateSnapshot("green-HealthBar.png",400/GameLoader.ResolutionScaleX,
		// 35/GameLoader.ResolutionScaleY);
		// scoreBoard =
		// ImageUtility.precreateSnapshot("score-board.png",128/GameLoader.ResolutionScaleX,
		// 41/GameLoader.ResolutionScaleY);
		// "com/SnakeGame/Images/"
		/**
		 * Images used for the desert levels
		 */
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// glowingImage = ImageUtility.preCreateGlowingImages("image.png",
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Color.rgb(255,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 185,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 0,1.0),
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 150,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 0.6,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 0,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 0);
		// glowingCircle = ImageUtility.preCreateGlowingCircle(Color.ORANGE,1,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 500,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 0.8,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 0,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 0);
		// glowingCircle2 =
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ImageUtility.preCreateAlternateGlowingCircle(Color.ORANGE,1,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 500,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 0.8,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 0,
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 0);
		snakeHead = ImageUtility.precreatedLightedImage("desert-snake-head3.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);// new
																											// Image("snake-head.png");
		snakeBlinking = ImageUtility.precreatedLightedImage("desert-snake-head-blink3.png",
				Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX,
				97 / GameLoader.ResolutionScaleY);// new
													// Image("snake-head-blink.png");
		snakeEating = ImageUtility.precreatedLightedImage("desert-snake-head-eat3.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);// new
																											// Image("snake-head-eat.png");
		snakeBones = ImageUtility.precreatedLightedAndShadedSnake("snake-skeleton.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);// new
																												// Image("snakeBody.png");
		snakeSkull = ImageUtility.precreatedLightedAndShadedSnake("snake-skull.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);// new
																											// Image("snakeBody.png");
		snakeBody = ImageUtility.precreatedLightedImage("snake-skin1.png", Settings.GlOBAL_ILLUMINATION - 0.2,
				Settings.SPECULAR_MAP, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);// new
																												// Image("snakeBody.png");
		snakeTail = ImageUtility.precreatedLightedAndShadedSnake("desert-snake-tail2.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 114 / GameLoader.ResolutionScaleX, 243 / GameLoader.ResolutionScaleY); // new
																												// Image("desert-snake-tail.png");
		snakeDebris = ImageUtility.precreatedLightedImage("snake-skin2.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 5 / GameLoader.ResolutionScaleX, 5 / GameLoader.ResolutionScaleY); // new
																											// Image("desert-snake-body5.jpg");
		dirt = ImageUtility.precreatedLightedImage("sand-grain.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 20 / GameLoader.ResolutionScaleX, 20 / GameLoader.ResolutionScaleY); // new
																											// Image("dirt.png");
		fruit = ImageUtility.precreatedLightedAndShadedImage("apple.png", Settings.GlOBAL_ILLUMINATION, 0,
				150 / GameLoader.ResolutionScaleX, 192 / GameLoader.ResolutionScaleY); // new
																						// Image("dirt.png");
		fruit2 = ImageUtility.preCreateShadedCircle(Color.RED, Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP,
				10 / GameLoader.ResolutionScaleX, 10 / GameLoader.ResolutionScaleY);
		fruit3 = ImageUtility.preCreateShadedGlowingCircle(Color.RED, Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 10 / GameLoader.ResolutionScaleX, 10 / GameLoader.ResolutionScaleY);
		snakeSphere = new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(snakeHead));
		slither = new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(snakeHead));
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Player 2
		 */
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		snakeHead2 = ImageUtility.precreatedLightedImage("desert-snake-head4.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);// new
																											// Image("snake-head.png");
		snakeBlinking2 = ImageUtility.precreatedLightedImage("desert-snake-head-blink4.png",
				Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX,
				97 / GameLoader.ResolutionScaleY);// new
													// Image("snake-head-blink.png");
		snakeEating2 = ImageUtility.precreatedLightedImage("desert-snake-head-eat4.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 111 / GameLoader.ResolutionScaleX, 97 / GameLoader.ResolutionScaleY);// new
																											// Image("snake-head-eat.png");
		snakeBody2 = ImageUtility.precreatedLightedImage("snake-skin3.png", Settings.GlOBAL_ILLUMINATION - 0.2,
				Settings.SPECULAR_MAP, 197 / GameLoader.ResolutionScaleX, 176 / GameLoader.ResolutionScaleY);// new
																												// Image("snakeBody.png");
		snakeDebris2 = ImageUtility.precreatedLightedImage("snake-skin3.png", Settings.GlOBAL_ILLUMINATION,
				Settings.SPECULAR_MAP, 5 / GameLoader.ResolutionScaleX, 5 / GameLoader.ResolutionScaleY); // new
																											// Image("desert-snake-body5.jpg");
		snakeSphere2 = new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(snakeHead2));
		snakeSphere3 = new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(snakeHead2));
		/**
		 * Images used for other levels
		 */

		/**
		 * General Images
		 */
		hudBar = new Image(ImageUtility.loadResource("hudBar.png"));
		countKeeper = new Image(ImageUtility.loadResource("countKeeper.png"));
		levelCompleteSplash = new Image(ImageUtility.loadResource("levelComplete2.png"));
		gameOverScreen = new Image(ImageUtility.loadResource("GameOver.png"));
		continueOpt = new Image(ImageUtility.loadResource("continue.png"));
		restartOpt = new Image(ImageUtility.loadResource("restart.png"));
		quitOpt = new Image(ImageUtility.loadResource("quit.png"));
		continueOpt2 = new Image(ImageUtility.loadResource("continueSelected.png"));
		restartOpt2 = new Image(ImageUtility.loadResource("restartSelected.png"));
		quitOpt2 = new Image(ImageUtility.loadResource("quitSelected.png"));
		healthBarGreen1 = new Image(ImageUtility.loadResource("healthBarGreen2.png"));
		healthBarGreen2 = new Image(ImageUtility.loadResource("healthBar2Green2.png"));
		healthBarRed1 = new Image(ImageUtility.loadResource("healthBarRed2.png"));
		healthBarRed2 = new Image(ImageUtility.loadResource("healthBar2Red2.png"));

		// redHealthBar = new Image("com/SnakeGame/Images/red-HealthBar.png");
		// greenHealthBar = new
		// Image("com/SnakeGame/Images/green-HealthBar.png");
		// scoreBoard = new Image("com/SnakeGame/Images/score-board.png");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	public static String loadResource(String resource) {
		return GameImageBank.class.getResource(resource).toExternalForm();
	}

}
