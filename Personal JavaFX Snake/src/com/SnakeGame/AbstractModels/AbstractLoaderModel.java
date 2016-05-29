package com.SnakeGame.AbstractModels;

import java.awt.image.BufferedImage;
import java.util.Random;

import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.FrameWork.LevelManager;
import com.SnakeGame.FrameWork.ObjectManager;
import com.SnakeGame.GameObjects.GenericObject;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.SlitherSnake.SlitherSnake;
import com.SnakeGame.Utilities.ImageLoadingUtility;
import com.SnakeGame.Utilities.GameTileManager;

public abstract class AbstractLoaderModel {
	public static int LEVEL = 1;
	protected BufferedImage border;
	protected BufferedImage fence;
	protected BufferedImage levelMain;
	protected BufferedImage desertLevel_1;
	protected BufferedImage desertLevel_2;
	protected BufferedImage desertLevel_3;
	protected BufferedImage desertLevel_4;
	protected BufferedImage desertLevel_5;
	protected BufferedImage desertLevel_6;
	protected BufferedImage desertLevel_7;
	protected BufferedImage desertLevel_8;
	protected BufferedImage desertLevel_9;
	protected BufferedImage desertLevel_10;
	protected BufferedImage overlay;
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

	/**
	 * this method will load all the level images in to memory and make them
	 * accessible to the level loader. this class also determines what level
	 * will be loaded first, and the final dimensions of that level
	 */
	public void loadDesertLevels() {
		this.levelMain 	= ImageLoadingUtility.loadImage("/desert-level-0.png");
		this.desertLevel_1 = ImageLoadingUtility.loadImage("/desert-level-1.png");
		this.desertLevel_2 = ImageLoadingUtility.loadImage("/desert-level-2.png");
		this.desertLevel_3 = ImageLoadingUtility.loadImage("/desert-level3.png");
		this.desertLevel_4 = ImageLoadingUtility.loadImage("/desert-level4.png");
		this.desertLevel_5 = ImageLoadingUtility.loadImage("/desert-level5.png");
		this.desertLevel_6 = ImageLoadingUtility.loadImage("/desert-level6.png");
		this.desertLevel_7 = ImageLoadingUtility.loadImage("/image.png");
		this.desertLevel_8 = ImageLoadingUtility.loadImage("/image.png");
		this.desertLevel_9 = ImageLoadingUtility.loadImage("/image.png");
		this.desertLevel_10 = ImageLoadingUtility.loadImage("/image.png");

	}
	public void loadJungleLevels() {


	}
	public void loadSeaLevels() {


	}
	public LevelManager getLevelManager(){
		return levelManager;
	}
	public BufferedImage getLevel() {
		return levelMain;
	}

	public void setLevel(BufferedImage level) {
		this.levelMain = level;
	}

	public GameTileManager getTileManager() {
		return tileManager;
	}

	public void setTileManager(GameTileManager tileManager) {
		this.tileManager = tileManager;
	}

	public BufferedImage getBorder() {
		return border;
	}

	public BufferedImage getSpikeFence() {
		return fence;
	}

	public void setBorder(BufferedImage border) {
		this.border = border;
	}

	public void setSpikeFence(BufferedImage fence) {
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

//	public SnakeOne getSnakeOne(){
//		return snakeOne;
//	}
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
	public BufferedImage getOverlay() {
		return overlay;
	}
	public void setOverlay(BufferedImage overlay) {
		this.overlay = overlay;
	}

}
