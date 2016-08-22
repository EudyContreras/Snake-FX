package com.EudyContreras.Snake.FrameWork;


import java.util.Random;

import com.EudyContreras.Snake.AbstractModels.AbstractLoaderModel;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.ClassicSnake.ClassicSnake;
import com.EudyContreras.Snake.Controllers.GameTileController;
import com.EudyContreras.Snake.GameObjects.BackgroundDirt;
import com.EudyContreras.Snake.GameObjects.ClassicSnakeFood;
import com.EudyContreras.Snake.GameObjects.GameBackground;
import com.EudyContreras.Snake.GameObjects.GenericObject;
import com.EudyContreras.Snake.GameObjects.LevelBounds;
import com.EudyContreras.Snake.GameObjects.NoSpawnZone;
import com.EudyContreras.Snake.GameObjects.SnakeFood;
import com.EudyContreras.Snake.HUDElements.GameTimer.TimerStyle;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameThemeID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ImageBanks.GameLevelImage;
import com.EudyContreras.Snake.PathFinder.CellNode;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.EudyContreras.Snake.Utilities.ImageLoadingUtility;
import com.EudyContreras.Snake.Utilities.RandomGenUtility;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;

/**
 * The GameLoader class is the class responsible for creating and loading the
 * level. this class calculates where every object will be positioned and will
 * also take care of spawning random objects on random locations
 *
 * @author Eudy Contreras
 *
 */
public class GameLoader extends AbstractLoaderModel{

	public static Scale scaleFactor;

	public GameLoader(GameManager game) {
		this.game = game;
		this.rand = new Random();
		this.objectManger = game.getGameObjectController();
		this.levelManager = new LevelManager(game,this);
		this.setTileManager(new GameTileController(game));
		this.setLevelTheme(GameThemeID.DESERT_THEME);
		loadDesertLevels();
		initializeMain();
	}
	public void initializeMain(){
		this.border = ImageLoadingUtility.loadImage("desert-level-border.png");
		this.fence = ImageLoadingUtility.loadImage("desert-level-fence.png");
		this.setLevel(this.levelMain);
		this.setLevelWidth((int) getLevel().getWidth());
		this.setLevelHeight((int) getLevel().getHeight());
		this.game.setLevelLenght(128 * 64);
	}
	/**
	 * Method which creates a resolution scale base on the systems's current resolution
	 */
	public static void scaleResolution() {

		double resolutionX = Screen.getPrimary().getBounds().getWidth();
		double resolutionY = Screen.getPrimary().getBounds().getHeight();

		final double baseResolutionX = 1920;
		final double baseResolutionY = 1080;

		ResolutionScaleX = resolutionX / baseResolutionX;
		ResolutionScaleY = resolutionY / baseResolutionY;

		scaleFactor = new Scale(ResolutionScaleX , ResolutionScaleY);

		System.out.println("width scale = " + ResolutionScaleX);
		System.out.println("height scale = " + ResolutionScaleY);

	}

	public void setScale(Scale scaleFactor){
		game.getScene().getRoot().getTransforms().setAll(scaleFactor);
	}
	public void setScale(Double scale){
		game.getScene().getRoot().setScaleX(scale);
		game.getScene().getRoot().setScaleY(scale);
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
		setLevel(desertLevel_5);
		game.getGameHud().showHUDCover();
		switch(game.getModeID()){
		case CampaingMode:
			break;
		case ClassicMode:
				loadClassicMode();
			break;
		case LocalMultiplayer:
				loadMultiplayerMode();
			break;
		case RemoteMultiplayer:
				loadMultiplayerMode();
			break;
		case SinglePlayer:
			break;
		case TimeMode:
			break;
		default:
			break;

		}
		game.getAIController().updateGrid();
		for(int i = 0; i<4; i++){
			this.spawnSnakeFood();
		}

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

		this.setLevelWidth((int) getLevel().getWidth());
		this.setLevelHeight((int) getLevel().getHeight());
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
				levelManager.loadDesertLevels();
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
	//	getTileManager().updateAll();
	}
	/**
	 * Method responsible for clearing all tiles from the level
	 */
	public void clearTiles() {
		getTileManager().clearAll();
	}
	public void loadClassicMode(){
		GameSettings.SAND_STORM = false;
		game.getScoreKeeper().getTimer().setStyle(TimerStyle.BLUE_STYLE);
		game.getScoreKeeper().getTimer().showTimer(true);
		game.getScoreKeeper().setboardMode(GameModeID.ClassicMode);
		loadClassicSnake();
//		for (int i = 0; i < GameSettings.MAX_AMOUNT_OF_BACKGROUND_OBJECT; i++) {
//			spawnBackgroundStuff(true);
//		}
		GameBackground.SET_BACKGROUND(game, GameLevelImage.classic_background);
		game.getKeyInput().setClassicSnake(game.getGameLoader().getClassicSnake());
		if(levelBounds==null){
		levelBounds = new LevelBounds(game, game.getTenthLayer());
		getTileManager().addTile(levelBounds);
		}
		else{
			levelBounds.showBounds(true);
		}

	}
	public void loadMultiplayerMode(){
		if(levelBounds!=null){
			levelBounds.showBounds(false);
		}
		game.getScoreKeeper().getTimer().setStyle(TimerStyle.BLUE_STYLE);
		game.getScoreKeeper().getTimer().showTimer(false);
		game.getScoreKeeper().setboardMode(GameModeID.LocalMultiplayer);
		if(levelTheme == GameThemeID.DESERT_THEME){
			GameSettings.SAND_STORM = true;
			loadPlayerOne();
			loadPlayerTwo();
			game.assignPlayer();
			for (int i = 0; i < GameSettings.MAX_AMOUNT_OF_BACKGROUND_OBJECT; i++) {
				spawnBackgroundStuff(true);
			}
			if (!GameSettings.LOAD_SPIKE_FENCE && LEVEL<=5)
//				levelManager.loadDesertBorder();
			if (GameSettings.LOAD_SPIKE_FENCE && LEVEL<=5) {
				levelManager.loadSpikeFence();
			}
			levelManager.loadDesertLevels();
			GameBackground.SET_SEQUENTIAL_BACKGROUND(game, GameThemeID.DESERT_THEME);
			game.setLevelLenght(128 * 64);
			loadNoSpawnZone();
		}else if(levelTheme == GameThemeID.JUNGLE_THEME){

		}else if(levelTheme == GameThemeID.WATER_THEME){

		}else if(levelTheme == GameThemeID.SCIFI_THEME){

		}else if(levelTheme == GameThemeID.ALIEN_THEME){

		}else if(levelTheme == GameThemeID.MECH_THEME){

		}
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
		int rows = game.getAIController().getGrid().getRowCount();
		int cols = game.getAIController().getGrid().getColumnCount();
		CellNode cell = game.getAIController().getGrid().getCells()[RandomGenUtility.getRandomInteger(2, rows-2)][RandomGenUtility.getRandomInteger(2, cols-2)];

		if(cell.isValid() && cell.isSpawnAllowed()){
			Circle fruit = new Circle(30, new ImagePattern(GameImageBank.fruit));
			int x =  (int) (cell.getLocation().getX() + cell.getDimension().getWidth()/2);
			int y = (int) (cell.getLocation().getY() + cell.getDimension().getHeight()/2);
			SnakeFood food = new SnakeFood(game, game.getFruitLayer(), fruit, x, y, GameObjectID.Fruit, cell, appleNumber);
			game.getGameObjectController().addFruit(food);
			game.getAIController().updateGrid();
			appleNumber++;
		}
		else{
			spawnSnakeFood();
		}
	}
	/**
	 * Method responsible for spawning the food on the level
	 */
	public void spawnClassicSnakeFood() {
		Circle fruit = new Circle(30, new ImagePattern(GameImageBank.apple_alt));
		double x = RandomGenUtility.getRandomDouble(60, (GameSettings.WIDTH - 90));
		double y = RandomGenUtility.getRandomDouble(GameSettings.MIN_Y + 60, (GameSettings.HEIGHT - 90));
		ClassicSnakeFood food = new ClassicSnakeFood(game, game.getFruitLayer(), fruit, x, y, GameObjectID.Fruit, appleNumber);
		game.getGameObjectController().addFruit(food);
		appleNumber++;
	}
	/**
	 * Method used to create the player one and position the player at a specified
	 * position.
	 */
	public void loadClassicSnake() {
		game.setPlayerInfoVisibility(false);
		classicSnake = null;
		float x = (float) (GameSettings.WIDTH / 2 - GameSettings.CLASSIC_SNAKE_SIZE/2);
		float y = (float) (GameSettings.HEIGHT * 0.50);
		classicSnake = new ClassicSnake(game, game.getSnakeOneLayer(),
				new Circle(GameSettings.CLASSIC_SNAKE_SIZE, new ImagePattern(GameImageBank.classicSnakeHead)), x, y, 0, 0, 0, 0,
				GameSettings.PLAYER_HEALTH, 0, GameSettings.CLASSIC_SNAKE_SPEED, GameObjectID.classicSnake, game.getGameObjectController());
		game.getClassicSnakeManager().addObject(classicSnake);
	}
	/**
	 * Method used to create the player one and position the player at a specified
	 * position.
	 */
	public void loadPlayerOne() {
		game.setPlayerInfoVisibility(true);
		playerOne = null;
		float x = (float) (GameSettings.WIDTH / 2 - GameSettings.PLAYER_SIZE*1.5);
		float y = (float) (GameSettings.HEIGHT * 0.50);
		playerOne = new PlayerOne(game, game.getSnakeOneLayer(),
				new Circle(GameSettings.PLAYER_ONE_SIZE, new ImagePattern(GameImageBank.snakeOneSkin)), x, y, 0, 0, 0, 0,
				GameSettings.PLAYER_HEALTH, 0, GameSettings.PLAYER_ONE_SPEED, GameObjectID.PlayerOne, game.getGameObjectController());
		game.getPlayerOneManager().addObject(playerOne);
	}
	/**
	 * Method used to create the player two and position the player at a specified
	 * position.
	 */
	public void loadPlayerTwo() {
		playerTwo = null;
		float x = (float) (GameSettings.WIDTH / 2 + GameSettings.PLAYER_SIZE*1.5);
		float y = (float) (GameSettings.HEIGHT * 0.50);
		playerTwo = new PlayerTwo(game, game.getSnakeTwoLayer(),
				new Circle(GameSettings.PLAYER_TWO_SIZE, new ImagePattern(GameImageBank.snakeTwoSkin)), x, y, 0, 0, 0, 0,
				GameSettings.PLAYER_HEALTH, 0, GameSettings.PLAYER_TWO_SPEED, GameObjectID.PlayerTwo, game.getGameObjectController());
		game.getPlayerTwoManager().addObject(playerTwo);
	}

	/**
	 * Loads a no spawn zone used to prevent objects such as apples from spawning at a desire location
	 */
	public void loadNoSpawnZone(){
		double width = 160;
		double height = 200;
		float x = (float)(GameSettings.WIDTH/2-width/2);
		float y = (float)((GameSettings.HEIGHT/2-height/2)-15);
		NoSpawnZone noSpawnZone = new NoSpawnZone(game,x,y,width,height, GameLevelObjectID.noSpawnZone);
		getTileManager().addTile(noSpawnZone);
		game.getDirtLayer().getChildren().add(noSpawnZone.getDebugBounds());
	}

	/**
	 * Method used to randomly spawn an esthetic object at a random position and
	 * at random speed.
	 *
	 * @param random
	 */
	public void spawnBackgroundStuff(boolean random) {
		float x = (int) (Math.random() * ((GameSettings.WIDTH - 30) - 30 + 1) + 30);
		float y = (int) (Math.random() * ((GameSettings.HEIGHT - 30) - GameSettings.MIN_Y+10 + 1) + GameSettings.MIN_Y+10);
		new BackgroundDirt(game, game.getDirtLayer(), GameImageBank.sand_grain, x, y);
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
