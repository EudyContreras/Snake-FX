package com.SnakeGame.AbstractModels;

import java.util.Random;
import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.FrameWork.LevelManager;
import com.SnakeGame.FrameWork.ObjectManager;
import com.SnakeGame.GameObjects.GenericObject;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.SlitherSnake.SlitherSnake;
import com.SnakeGame.Utilities.GameTileManager;
import com.SnakeGame.Utilities.ImageLoadingUtility;
import javafx.scene.image.WritableImage;

public abstract class AbstractLoaderModel {
	public static int LEVEL = 1;
	protected WritableImage border;
	protected WritableImage fence;
	protected WritableImage levelMain;
	protected WritableImage desertLevel_1;
	protected WritableImage desertLevel_2;
	protected WritableImage desertLevel_3;
	protected WritableImage desertLevel_4;
	protected WritableImage desertLevel_5;
	protected WritableImage desertLevel_6;
	protected WritableImage desertLevel_7;
	protected WritableImage desertLevel_8;
	protected WritableImage desertLevel_9;
	protected WritableImage desertLevel_10;
	protected WritableImage overlay;
	protected LevelManager levelManager;
	protected ObjectManager objectManger;
	protected GameTileManager tileManager;
	protected GenericObject gameTile;
	protected PlayerOne playerOne;
	protected PlayerTwo playerTwo;
	protected SlitherSnake slither;
	protected GameManager game;
	protected Random rand;
	protected int appleNumber = 0;
	protected int levelWidth;
	protected int levelHeight;
	protected int pixel;
	protected int red;
	protected int green;
	protected int blue;
	protected double Front_Distance_LOD = 1;
	protected double Rear_Distance_LOD = 0;
	public static double ResolutionScaleX = 1.0;
	public static double ResolutionScaleY = 1.0;
	public static boolean FullScreen = true;

	/**
	 * this method will load all the level images in to memory and make them
	 * accessible to the level loader. this class also determines what level
	 * will be loaded first, and the final dimensions of that level
	 */
	public void loadDesertLevels() {
		this.levelMain 	= ImageLoadingUtility.loadImage("desert-level-0.png");
		this.desertLevel_1 = ImageLoadingUtility.loadImage("desert-level-1.png");
		this.desertLevel_2 = ImageLoadingUtility.loadImage("desert-level-2.png");
		this.desertLevel_3 = ImageLoadingUtility.loadImage("image.png");
		this.desertLevel_4 = ImageLoadingUtility.loadImage("image.png");
		this.desertLevel_5 = ImageLoadingUtility.loadImage("image.png");
		this.desertLevel_6 = ImageLoadingUtility.loadImage("image.png");
		this.desertLevel_7 = ImageLoadingUtility.loadImage("image.png");
		this.desertLevel_8 = ImageLoadingUtility.loadImage("image.png");
		this.desertLevel_9 = ImageLoadingUtility.loadImage("image.png");
		this.desertLevel_10 = ImageLoadingUtility.loadImage("image.png");

	}
	public void loadJungleLevels() {


	}
	public void loadSeaLevels() {


	}
	public LevelManager getLevelManager(){
		return levelManager;
	}
	public WritableImage getLevel() {
		return levelMain;
	}

	public void setLevel(WritableImage level) {
		this.levelMain = level;
	}

	public GameTileManager getTileManager() {
		return tileManager;
	}

	public void setTileManager(GameTileManager tileManager) {
		this.tileManager = tileManager;
	}

	public WritableImage getBorder() {
		return border;
	}

	public WritableImage getSpikeFence() {
		return fence;
	}

	public void setBorder(WritableImage border) {
		this.border = border;
	}

	public void setSpikeFence(WritableImage fence) {
		this.fence = fence;
	}

	public void setPlayerOne(PlayerOne player) {
		this.playerOne = player;
	}

	public void setPlayerTwo(PlayerTwo player) {
		this.playerTwo = player;
	}

	public PlayerOne getPlayerOne() {
		return playerOne;
	}

	public PlayerTwo getPlayerTwo() {
		return playerTwo;
	}

	public SlitherSnake getSlither() {
		return slither;
	}

	public void killPlayerOne() {
		playerOne = null;
	}

	public void killPlayerTwo() {
		playerTwo = null;
	}
	public int getLevelWidth() {
		return levelWidth;
	}
	public void setLevelWidth(int levelWidth) {
		this.levelWidth = levelWidth;
	}
	public int getLevelHeight() {
		return levelHeight;
	}
	public void setLevelHeight(int levelHeight) {
		this.levelHeight = levelHeight;
	}
	public WritableImage getOverlay() {
		return overlay;
	}
	public void setOverlay(WritableImage overlay) {
		this.overlay = overlay;
	}

}
