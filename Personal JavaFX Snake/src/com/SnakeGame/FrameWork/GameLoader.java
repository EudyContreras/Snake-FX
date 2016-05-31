package com.SnakeGame.FrameWork;

import java.awt.Toolkit;
import java.util.Random;

import com.SnakeGame.AbstractModels.AbstractLoaderModel;
import com.SnakeGame.DebrisEffects.BackgroundDirt;
import com.SnakeGame.GameObjects.GenericObject;
import com.SnakeGame.GameObjects.SnakeFood;
import com.SnakeGame.IDEnums.GameObjectID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ImageBanks.GameLevelImage;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.SlitherSnake.SlitherSnake;
import com.SnakeGame.Utilities.GameTileManager;
import com.SnakeGame.Utilities.ImageLoadingUtility;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * The GameLoader class is the class responsible for creating and loading the
 * level. this class calculates where every object will be positioned and will
 * also take care of spawning random objects on random locations
 *
 * @author Eudy Contreras
 *
 */
public class GameLoader extends AbstractLoaderModel{

	public GameLoader(GameManager game) {
		this.game = game;
		this.rand = new Random();
		this.objectManger = game.getObjectManager();
		this.levelManager = new LevelManager(game,this);
		this.setTileManager(new GameTileManager(game));
		loadDesertLevels();
		initializeMain();
	}
	public void initializeMain(){
		this.border = ImageLoadingUtility.loadImage("/desert-level-border.png");
		this.fence = ImageLoadingUtility.loadImage("/desert-level-fence.png");
		this.setLevel(this.levelMain);
		this.setLevelWidth(getLevel().getWidth());
		this.setLevelHeight(getLevel().getHeight());
		this.game.setLevelLenght(128 * 64);
		System.out.print("loaded");
	}
	/**
	 * Method which creates a resolution scale base on the systems's current resolution
	 */
	public static void scaleResolution() {
		double resolutionX = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double resolutionY = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double baseResolutionX = 1920;
		double baseResolutionY = 1080;
		ResolutionScaleX = baseResolutionX / resolutionX;
		ResolutionScaleY = baseResolutionY / resolutionY;
//		ResolutionScaleX = 1.5;
//		ResolutionScaleY = 1.5;
		System.out.println("width scale = " + ResolutionScaleX);
		System.out.println("height scale = " + ResolutionScaleY);

	}

	/**
	 * Method which will attempt to scale the speed and the size of the snake according
	 * to the new scaled resolution, The size and the speed are relative. The size must be
	 * able to be divided by the speed and the result must always be a whole number
	 */
	public static void scaleSpeedAndSize() {
		int newSize = (int) (GameSettings.SECTION_SIZE / GameLoader.ResolutionScaleX);
		int newSpeed = (int) (GameSettings.SNAKE_SPEED / GameLoader.ResolutionScaleX);
		boolean divisible = newSize % newSpeed == 0;
		if (divisible) {
			GameSettings.SECTION_SIZE = newSize;
			GameSettings.SNAKE_SPEED = newSpeed;
		} else {

			if (reScale(newSize + 1, newSpeed) == true) {
				GameSettings.SECTION_SIZE = newSize + 1;
				GameSettings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + GameSettings.SECTION_SIZE + " Speed " + newSpeed);

			} else if (reScale(newSize + 2, newSpeed) == true) {
				GameSettings.SECTION_SIZE = newSize + 2;
				GameSettings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + GameSettings.SECTION_SIZE + " Speed " + newSpeed);

			} else if (reScale(newSize + 3, newSpeed) == true) {
				GameSettings.SECTION_SIZE = newSize + 3;
				GameSettings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + GameSettings.SECTION_SIZE + " Speed " + newSpeed);

			} else if (reScale(newSize - 1, newSpeed) == true) {
				GameSettings.SECTION_SIZE = newSize - 1;
				GameSettings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + GameSettings.SECTION_SIZE + " Speed " + newSpeed);

			} else if (reScale(newSize - 2, newSpeed) == true) {
				GameSettings.SECTION_SIZE = newSize - 2;
				GameSettings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + GameSettings.SECTION_SIZE + " Speed " + newSpeed);

			} else if (reScale(newSize - 3, newSpeed) == true) {
				GameSettings.SECTION_SIZE = newSize - 3;
				GameSettings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + GameSettings.SECTION_SIZE + " Speed " + newSpeed);
			}
		}
	}

	/**
	 * Method which will attempt to scale the speed and the size of the snake according
	 * to the new scaled resolution, The size and the speed are relative. The size must be
	 * able to be divided by the speed and the result must always be a whole number
	 */

	public static void scalePlayerSize() {
		int newSizeOne = (int) (GameSettings.PLAYER_ONE_SIZE / GameLoader.ResolutionScaleX);
		int newSizeTwo = (int) (GameSettings.PLAYER_TWO_SIZE / GameLoader.ResolutionScaleX);
		GameSettings.PLAYER_ONE_SIZE = newSizeOne;
		GameSettings.PLAYER_TWO_SIZE = newSizeTwo;
		System.out.println("new player radius: " + GameSettings.PLAYER_ONE_SIZE );
	}

	/*
	 * This method will look for a suitable scaling by trying different
	 * combination of viable options
	 *
	 */
	public static boolean reScale(int newSize, int newSpeed) {
		return newSize % newSpeed == 0;
	}

	/**
	 * This method could spawn any given object a specific max amount of times
	 * and at a random position within specified boundaries. here is where we
	 * also call the level loading method.
	 */
	public void loadPixelMap() {
		for (int i = 0; i < GameSettings.MAX_AMOUNT_OF_OBJECTS; i++) {
			spawnBackgroundStuff(true);
		}
		if(!GameSettings.LOAD_SPIKE_FENCE)
			levelManager.loadDesertBorder();
		if(GameSettings.LOAD_SPIKE_FENCE){
			levelManager.loadSpikeFence();
		}
		levelManager.loadDesertLevels(GameLevelImage.desertBackground);
		game.setLevelLenght(128 * 64);
	}

	/**
	 * This method is used in order to cull the level meaning it will load the
	 * level items accordingly to the advancement of the player within the
	 * level. this method also makes it so that the objects no longer seen by
	 * the character are immediately removed from the game. This method
	 * significantly increases the level of performance since only the on screen
	 * section of the level is being rendered by the game.
	 */
	public void cullTheLevel() {
		Front_Distance_LOD += 0.14;
		Rear_Distance_LOD += 0.14;
		if (Front_Distance_LOD >= 512) {
			Front_Distance_LOD = 512;
			Rear_Distance_LOD = 511;
		}
	}

	/**
	 * this method loads level items according to a specific color code. this
	 * method reads the pixels from the level image and according to the color
	 * of the pixel and space calculations it will then load a specified object
	 * at the given pixel's predetermined position.
	 */
	public void changeBackground(Image image) {
		game.setBackgroundImage(image);
	}



	/**
	 * This function is responsible for switching levels in a specified order
	 * this function will first clear all objects from the game in order to
	 * allow the game to reload this objects for a new and fresh start
	 */
	public void switcLevel() {
		Front_Distance_LOD = 512;
		Rear_Distance_LOD = 511;
		switch (LEVEL) {
		case 0:
			setLevel(levelMain);
			break;
		case 1:
			setLevel(desertLevel_1);
			break;
		case 2:
			setLevel(desertLevel_2);
			break;
		case 3:
			setLevel(desertLevel_3);
			break;
		case 4:
			setLevel(desertLevel_4);
			break;
		case 5:
			setLevel(desertLevel_5);
			break;
		case 6:
			setLevel(desertLevel_6);
			break;
		case 7:
			setLevel(desertLevel_7);
			break;
		case 8:
			setLevel(desertLevel_8);
			break;
		case 9:
			setLevel(desertLevel_9);
			break;
		case 10:
			setLevel(desertLevel_10);
			break;
		}

		this.setLevelWidth(getLevel().getWidth());
		this.setLevelHeight(getLevel().getHeight());
		loadPixelMap();
		LEVEL++;
	}


	/**
	 * Method responsible for the procedural creation of a level it loads and
	 * culls specific sections of the level at a predetermined speed. TODO:
	 * maybe allow the procedural creation speed to be dynamic
	 */
	public void procedurallyCreateLevel() {
		if (Front_Distance_LOD <= 512 && Rear_Distance_LOD <= 511) {
			if (playerOne != null) {
				levelManager.loadDesertLevels(GameLevelImage.desertBackground);
				cullTheLevel();
			}
		}
	}

	/**
	 * Method responsible for rendering and moving and removing level objects.
	 */
	public void updateLevelObjects() {
		getTileManager().updateTiles();
		getTileManager().updateBlocks();
		getTileManager().updateTraps();
		getTileManager().updateEdibles();
		getTileManager().checkIfRemovable();
	}
	/**
	 * Method responsible for clearing all tiles from the level
	 */
	public void clearTiles() {
		getTileManager().clearAll();
	}

	/**
	 * Method which could be used to create and place an object at a specific
	 * position within the level
	 *
	 * @param x
	 * @param y
	 * @param image
	 */
	public void createGameObject(float x, float y, Image image) {
		GenericObject texture = new GenericObject(game, x, y, -2, 0, image);
		if (image.isBackgroundLoading())
			game.getGameRoot().getChildren().add(texture.getView());
	}
	/**
	 * Method responsible for spawning the food on the level
	 */
	public void spawnSnakeFood() {
		Circle fruit = new Circle(30 / ResolutionScaleX - (5), new ImagePattern(GameImageBank.fruit));
		float x = (int) (Math.random() * ((GameSettings.WIDTH - fruit.getRadius() * 3) - fruit.getRadius() * 3 + 1)
				+ fruit.getRadius() * 3);
		float y = (int) (Math.random() * ((GameSettings.HEIGHT - fruit.getRadius() * 3) - GameSettings.START_Y+fruit.getRadius() + 1)
				+ GameSettings.START_Y+fruit.getRadius());
		SnakeFood food = new SnakeFood(game, game.getDirtLayer(), fruit, x, y, GameObjectID.Fruit, appleNumber);
		game.getObjectManager().addObject(food);
		appleNumber++;
	}
	/**
	 * Method used to create the player one and position the player at a specified
	 * position.
	 */
	public void loadPlayerOne() {
		float x = (float) (GameSettings.WIDTH / 2 - GameImageBank.snakeOneSphere.getRadius()*1.5);
		float y = (float) (GameSettings.HEIGHT * 0.50);
		playerOne = new PlayerOne(game, game.getSnakeOneLayer(),
				new Circle(GameSettings.PLAYER_ONE_SIZE, new ImagePattern(GameImageBank.snakeOneSkin)), x, y, 0, 0, 0, 0,
				GameSettings.PLAYER_HEALTH, 0, GameSettings.PLAYER_ONE_SPEED, GameObjectID.PlayerOne, game.getObjectManager());
		game.getPlayerOneManager().addObject(playerOne);
	}
	/**
	 * Method used to create the player two and position the player at a specified
	 * position.
	 */
	public void loadPlayerTwo() {
		float x = (float) (GameSettings.WIDTH / 2 + GameImageBank.snakeTwoSphere.getRadius()*1.5);
		float y = (float) (GameSettings.HEIGHT * 0.50);
		playerTwo = new PlayerTwo(game, game.getSnakeTwoLayer(),
				new Circle(GameSettings.PLAYER_TWO_SIZE, new ImagePattern(GameImageBank.snakeTwoSkin)), x, y, 0, 0, 0, 0,
				GameSettings.PLAYER_HEALTH, 0, GameSettings.PLAYER_TWO_SPEED, GameObjectID.PlayerTwo, game.getObjectManager());
		game.getPlayerTwoManager().addObject(playerTwo);
	}
	/**
	 * Method used to create the Slither snake and position the player at a specified
	 * position.
	 */
	public void createSlither() {
		float x = (float) (GameSettings.WIDTH / 2 + 25);
		float y = (float) (GameSettings.HEIGHT * 0.55);
		slither = new SlitherSnake(game, game.getFithLayer(), GameImageBank.slither, x, y, 0, 0, 0, 0,
				GameSettings.PLAYER_HEALTH, 0, 0, GameObjectID.SlitherSnake, game.getSlitherManager());
		game.getSlitherManager().addObject(slither);
	}

	/**
	 * Method used to randomly spawn an esthetic object at a random position and
	 * at random speed.
	 *
	 * @param random
	 */
	public void spawnBackgroundStuff(boolean random) {
		float x = (int) (Math.random() * ((GameSettings.WIDTH - 30) - 30 + 1) + 30);
		float y = (int) (Math.random() * ((GameSettings.HEIGHT - 30) - GameSettings.START_Y+10 + 1) + GameSettings.START_Y+10);
		new BackgroundDirt(game, game.getDirtLayer(), GameImageBank.dirt_grain,0.5, x, y, new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8));
	}

	/**
	 * Method used to spawn a specified object at a random position and at
	 * random speeds.
	 *
	 * @param random
	 */
	public void spawnObjectRandomly(boolean random) {
		// randomRotation = rand.nextFloat() * (1.2f - -1.2f + 1) + -1.2f;
		if (random && rand.nextInt(20) != 0) {
			return;
		}
		// TODO: object to spawn and add
		// game.getDebrisManager().addObject(object);
	}

}
