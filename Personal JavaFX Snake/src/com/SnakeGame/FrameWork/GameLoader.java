package com.SnakeGame.FrameWork;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.SnakeGame.GameObjects.SnakeFood;
import com.SnakeGame.GameObjects.WavingCactusOne;
import com.SnakeGame.GameObjects.WavingCactusTwo;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ImageBanks.GameLevelImage;
import com.SnakeGame.ObjectIDs.GameObjectID;
import com.SnakeGame.ObjectIDs.LevelObjectID;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.SlitherSnake.SlitherSnake;
import com.SnakeGame.Utilities.GameImageLoader;
import com.SnakeGame.Utilities.GameTileManager;

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
public class GameLoader {
	private BufferedImage border;
	private BufferedImage level;
	private BufferedImage level1;
	private BufferedImage level2;
	private BufferedImage level3;
	private BufferedImage level4;
	private BufferedImage level5;
	private BufferedImage level6;
	private BufferedImage level7;
	private BufferedImage level8;
	private BufferedImage level9;
	private BufferedImage level10;
	private BufferedImage overlay;
	private int levelWidth;
	private int levelHeight;
	private int pixel;
	private int red;
	private int green;
	private int blue;
	private double Front_Distance_LOD = 1;
	private double Rear_Distance_LOD = 0;
	private SnakeGame game;
	public GameTileManager tileManager;
	TileMap texture;
	GameObjectManager objectManger;
	PlayerTwo player2;
	SlitherSnake slither;
	Random rand;
	private PlayerOne player;
	public static double ResolutionScaleX = 1.0;
	public static double ResolutionScaleY = 1.0;

	public GameLoader(SnakeGame game) {
		this.game = game;
		this.rand = new Random();
		this.objectManger = game.getObjectManager();
		this.tileManager = new GameTileManager(game);
		loadLevelManager();
	}

	/**
	 * this method will load all the level images in to memory and make them
	 * accessible to the level loader. this class also determines what level
	 * will be loaded first, and the final dimensions of that level
	 */
	public void loadLevelManager() {
		this.border = GameImageLoader.loadImage("/desert-level-fence.png");
		this.level  = GameImageLoader.loadImage("/desert-level.png");
		this.level1 = GameImageLoader.loadImage("/desert-level.png");
		this.level2 = GameImageLoader.loadImage("/desert-level2.png");
		this.level3 = GameImageLoader.loadImage("/desert-level3.png");
		this.level4 = GameImageLoader.loadImage("/desert-level4.png");
		this.level5 = GameImageLoader.loadImage("/desert-level5.png");
		this.level6 = GameImageLoader.loadImage("/desert-level6.png");
		this.level7 = GameImageLoader.loadImage("/image.png");
		this.level8 = GameImageLoader.loadImage("/image.png");
		this.level9 = GameImageLoader.loadImage("/image.png");
		this.level10 = GameImageLoader.loadImage("/image.png");
		this.setLevel(level);
		this.levelWidth = getLevel().getWidth();
		this.levelHeight = getLevel().getHeight();
		this.game.levelLenght = 128 * 64;
		System.out.print("loaded");
	}

	public static void scaleResolution() {
		double resolutionX = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double resolutionY = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double baseResolutionX = 1920;
		double baseResolutionY = 1080;
		ResolutionScaleX = baseResolutionX / resolutionX;
		ResolutionScaleY = baseResolutionY / resolutionY;
		System.out.println("width scale = " + ResolutionScaleX);
		System.out.println("height scale = " + ResolutionScaleY);

	}

	/**
	 * will attempt to scale but the speed and the size of the snake according
	 * to the new scaled resolution
	 */
	public static void scaleSpeedAndSize() {
		int newSize = (int) (Settings.SECTION_SIZE / GameLoader.ResolutionScaleX);
		int newSpeed = (int) (Settings.SNAKE_SPEED / GameLoader.ResolutionScaleX);
		boolean divisible = newSize % newSpeed == 0;
		if (divisible) {
			Settings.SECTION_SIZE = newSize;
			Settings.SNAKE_SPEED = newSpeed;
		} else {

			if (reScale(newSize + 1, newSpeed) == true) {
				Settings.SECTION_SIZE = newSize + 1;
				Settings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + Settings.SECTION_SIZE + " Speed " + newSpeed);

			} else if (reScale(newSize + 2, newSpeed) == true) {
				Settings.SECTION_SIZE = newSize + 2;
				Settings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + Settings.SECTION_SIZE + " Speed " + newSpeed);

			} else if (reScale(newSize + 3, newSpeed) == true) {
				Settings.SECTION_SIZE = newSize + 3;
				Settings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + Settings.SECTION_SIZE + " Speed " + newSpeed);

			} else if (reScale(newSize - 1, newSpeed) == true) {
				Settings.SECTION_SIZE = newSize - 1;
				Settings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + Settings.SECTION_SIZE + " Speed " + newSpeed);

			} else if (reScale(newSize - 2, newSpeed) == true) {
				Settings.SECTION_SIZE = newSize - 2;
				Settings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + Settings.SECTION_SIZE + " Speed " + newSpeed);

			} else if (reScale(newSize - 3, newSpeed) == true) {
				Settings.SECTION_SIZE = newSize - 3;
				Settings.SNAKE_SPEED = newSpeed;
				System.out.println("Width " + Settings.SECTION_SIZE + " Speed " + newSpeed);
			}
		}
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
		for (int i = 0; i < Settings.MAX_AMOUNT_OF_OBJECTS; i++) {
			spawnBackgroundStuff(true);
		}
		loadDesertLevels(GameLevelImage.desertBackground);
		loadDesertBorder();
		game.levelLenght = 128 * 64;
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

	public void loadDesertLevels(Image image) {
		changeBackground(image);
		for (double row = 0; row < levelWidth; row++) {
			for (double col = 0; col < levelHeight; col++) {
				pixel = getLevel().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 255 && green == 0 && blue == 0) {
					TileMap texture = new TileMap(game, (float) (row * 55 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.rock,
							LevelObjectID.rock);
					tileManager.addBlock(texture);
					game.getDebrisLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 0) {
					WavingCactusOne texture = new WavingCactusOne((float) (row * 53 / GameLoader.ResolutionScaleX),
							(float) (col * 52 / GameLoader.ResolutionScaleY), 4, GameLevelImage.cactus,
							LevelObjectID.longCactus);
					tileManager.addTile(texture);
					game.getBottomLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 255 && blue == 0) {
					TileMap texture = new TileMap(game, (float) (row * 58 / GameLoader.ResolutionScaleX),
							(float) (col * 57 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.skeleton,
							LevelObjectID.skeleton);
					tileManager.addTile(texture);
					game.getDebrisLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 100 && blue == 0) {
					WavingCactusTwo texture = new WavingCactusTwo(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 48.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.littleCactus,
							LevelObjectID.cactus);
					tileManager.addTile(texture);
					game.getBottomLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 50 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.bark,
							LevelObjectID.treeBark);
					tileManager.addTile(texture);
					game.getSnakeBodyLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 255) {
					WavingCactusOne texture = new WavingCactusOne((float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.flower,
							LevelObjectID.flower);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 200 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.flower2,
							LevelObjectID.flower);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				}
			}
		}
		getLevel().flush();
	}

	private void loadDesertBorder() {
		for (double row = levelWidth - 1; row >= 0; row--) {
			for (double col = levelHeight - 1; col >= 0; col--) {
				pixel = getBorder().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 255 && green == 255 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 115 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.horizontalFence,
							LevelObjectID.fence);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				}
				 if (red == 0 && green == 0 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 50.5 / GameLoader.ResolutionScaleX),
							(float) (col * 100 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.verticalFence,
							LevelObjectID.fence);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				}
			}
		}
		getBorder().flush();
	}

	public void loadJungleLevels(Image image) {
		changeBackground(image);
		for (double row = 0; row < levelWidth; row++) {
			for (double col = 0; col < levelHeight; col++) {
				pixel = level.getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 255 && green == 255 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 120 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.horizontalFence,
							LevelObjectID.fence);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 0 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 50.5 / GameLoader.ResolutionScaleX),
							(float) (col * 100 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.verticalFence,
							LevelObjectID.fence);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 0) {
					TileMap texture = new TileMap(game, (float) (row * 55 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.rock,
							LevelObjectID.rock);
					tileManager.addTile(texture);
					game.getDebrisLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 0) {
					WavingCactusOne texture = new WavingCactusOne((float) (row * 53 / GameLoader.ResolutionScaleX),
							(float) (col * 52 / GameLoader.ResolutionScaleY), 4, GameLevelImage.cactus,
							LevelObjectID.rock);
					tileManager.addTile(texture);
					game.getBottomLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 255 && blue == 0) {
					TileMap texture = new TileMap(game, (float) (row * 58 / GameLoader.ResolutionScaleX),
							(float) (col * 57 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.skeleton,
							LevelObjectID.rock);
					tileManager.addTile(texture);
					game.getDebrisLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 100 && blue == 0) {
					WavingCactusTwo texture = new WavingCactusTwo(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 48.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.littleCactus,
							LevelObjectID.cactus);
					tileManager.addTile(texture);
					game.getBottomLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 50 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.bark,
							LevelObjectID.LevelTile);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 255) {
					WavingCactusOne texture = new WavingCactusOne((float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.flower,
							LevelObjectID.flower);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 200 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.flower2,
							LevelObjectID.flower);
					tileManager.addTile(texture);
					game.getBottomLayer().getChildren().add(texture.getView());
				}
			}
		}
		getLevel().flush();
	}

	public void loadSeaLevels(Image image) {
		changeBackground(image);
		for (double row = 0; row < levelWidth; row++) {
			for (double col = 0; col < levelHeight; col++) {
				pixel = level.getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 255 && green == 255 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 120 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.horizontalFence,
							LevelObjectID.fence);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 0 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 50.5 / GameLoader.ResolutionScaleX),
							(float) (col * 100 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.verticalFence,
							LevelObjectID.fence);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 0) {
					TileMap texture = new TileMap(game, (float) (row * 55 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.rock,
							LevelObjectID.rock);
					tileManager.addTile(texture);
					game.getDebrisLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 0) {
					WavingCactusOne texture = new WavingCactusOne((float) (row * 53 / GameLoader.ResolutionScaleX),
							(float) (col * 52 / GameLoader.ResolutionScaleY), 4, GameLevelImage.cactus,
							LevelObjectID.rock);
					tileManager.addTile(texture);
					game.getBottomLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 255 && blue == 0) {
					TileMap texture = new TileMap(game, (float) (row * 58 / GameLoader.ResolutionScaleX),
							(float) (col * 57 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.skeleton,
							LevelObjectID.rock);
					tileManager.addTile(texture);
					game.getDebrisLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 100 && blue == 0) {
					WavingCactusTwo texture = new WavingCactusTwo(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 48.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.littleCactus,
							LevelObjectID.cactus);
					tileManager.addTile(texture);
					game.getBottomLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 50 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.bark,
							LevelObjectID.LevelTile);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 255) {
					WavingCactusOne texture = new WavingCactusOne((float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.flower,
							LevelObjectID.flower);
					tileManager.addTile(texture);
					game.getPlayfieldLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 200 && blue == 255) {
					TileMap texture = new TileMap(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.flower2,
							LevelObjectID.flower);
					tileManager.addTile(texture);
					game.getBottomLayer().getChildren().add(texture.getView());
				}
			}
		}
		getLevel().flush();
	}

	public void loadLevelOverlay() {
		for (double row = 0; row < levelWidth; row++) {
			for (double col = 0; col < levelHeight; col++) {
				pixel = overlay.getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				if (red == 0 && green == 100 && blue == 0) {
					TileMap texture = new TileMap(game, (float) (row * 40 / 2), (float) (col * 50 / 2), 0, 0,
							GameLevelImage.littleCactus, LevelObjectID.rock);
					tileManager.addTile(texture);
					game.getBottomLayer().getChildren().add(texture.getView());
				}
			}
		}
		overlay.flush();
	}

	/**
	 * Will load the player at a specified positions according to a color code
	 * found within the level image.
	 */
	public void loadPlayer() {
		for (int row = 0; row < levelWidth; row++) {
			for (int col = 0; col < levelHeight; col++) {
				pixel = level1.getRGB(row, col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 0 && green == 0 && blue == 255) {
					this.player = new PlayerOne(game, game.getPlayfieldLayer(), GameImageBank.snakeSphere, row * 20,
							col * 20, 0, 0, 0, 0, Settings.PLAYER_HEALTH, 0, Settings.PLAYER_SPEED, GameObjectID.Player,
							game.getObjectManager());
					game.getObjectManager().addObject(player);
				}
			}
		}
		getLevel().flush();
	}

	public void levelTransition() {

	}

	/**
	 * This function is responsible for switching levels in a specified order
	 * this function will first clear all objects from the game in order to
	 * allow the game to reload this objects for a new and fresh start
	 */
	public void switcLevel() {
		game.getObjectManager().clearAll();
		game.getDebrisManager().clearAll();
		game.getDebrisLayer().getChildren().clear();
		game.getPlayfieldLayer().getChildren().clear();
		game.getBottomLayer().getChildren().clear();
		Front_Distance_LOD = 512;
		Rear_Distance_LOD = 511;
		switch (GameLevelImage.LEVEL) {
		case 1:
			setLevel(level1);
			break;
		case 2:
			setLevel(level2);
			break;
		case 3:
			setLevel(level3);
			break;
		case 4:
			setLevel(level4);
			break;
		case 5:
			setLevel(level5);
			break;
		case 6:
			setLevel(level6);
			break;
		case 7:
			setLevel(level7);
			break;
		case 8:
			setLevel(level8);
			break;
		case 9:
			setLevel(level9);
			break;
		case 10:
			setLevel(level10);
			break;
		}
		this.levelWidth = getLevel().getWidth();
		this.levelHeight = getLevel().getHeight();
		this.game.levelLenght = 128 * 64;
		loadPlayer();
		loadPixelMap();
		game.reset();
		GameLevelImage.LEVEL++;
	}

	public BufferedImage getLevel() {
		return level;
	}

	public void setLevel(BufferedImage level) {
		this.level = level;
	}

	public BufferedImage getBorder() {
		return border;
	}

	public void setBorder(BufferedImage border) {
		this.border = border;
	}
	/**
	 * Method responsible for the procedural creation of a level it loads and
	 * culls specific sections of the level at a predetermined speed. TODO:
	 * maybe allow the procedural creation speed to be dynamic
	 */
	public void procedurallyCreateLevel() {
		if (Front_Distance_LOD <= 512 && Rear_Distance_LOD <= 511) {
			if (player != null) {
				loadDesertLevels(GameLevelImage.desertBackground);
				cullTheLevel();
			}
		}
	}

	/**
	 * Method responsible for rendering and moving and removing level objects.
	 */
	public void updateLevelObjects() {
		tileManager.updateTiles();
		tileManager.checkIfRemovable();
	}

	public void clearTiles() {
		tileManager.clearAll();
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
		TileMap texture = new TileMap(game, x, y, -2, 0, image);
		if (image.isBackgroundLoading())
			game.getGameRoot().getChildren().add(texture.getView());
	}

	/**
	 * Method used to create the player and position the player at a specified
	 * position.
	 */
	public void loadPlayerOne() {
		float x = (float) (Settings.WIDTH / 2 - GameImageBank.snakeSphere.getRadius());
		float y = (float) (Settings.HEIGHT * 0.55);
		player = new PlayerOne(game, game.getSnakeHeadLayer(),
				new Circle(Settings.SECTION_SIZE, new ImagePattern(GameImageBank.snakeBody)), x, y, 0, 0, 0, 0,
				Settings.PLAYER_HEALTH, 0, Settings.PLAYER_SPEED, GameObjectID.OrgPlayer, game.getObjectManager());
		game.getObjectManager().addObject(player);
	}

	public void loadPlayerTwo() {
		float x = (float) (Settings.WIDTH / 2 + GameImageBank.snakeSphere2.getRadius());
		float y = (float) (Settings.HEIGHT * 0.55);
		player2 = new PlayerTwo(game, game.getSnakeHeadLayer(),
				new Circle(Settings.SECTION_SIZE, new ImagePattern(GameImageBank.snakeBody2)), x, y, 0, 0, 0, 0,
				Settings.PLAYER_HEALTH, 0, Settings.PLAYER_SPEED, GameObjectID.Player2, game.getObjectManager());
		game.getObjectManager().addObject(player2);
	}

	public void createSlither() {
		float x = (float) (Settings.WIDTH / 2 + 25);
		float y = (float) (Settings.HEIGHT * 0.55);
		slither = new SlitherSnake(game, game.getSnakeHeadLayer(), GameImageBank.slither, x, y, 0, 0, 0, 0,
				Settings.PLAYER_HEALTH, 0, 0, GameObjectID.Player3, game.getSlitherManager());
		game.getSlitherManager().addObject(slither);
	}

	/**
	 * Method used to randomly spawn an esthetic object at a random position and
	 * at random speed.
	 *
	 * @param random
	 */
	public void spawnBackgroundStuff(boolean random) {
		// randomOpacity = rand.nextFloat() * (0.5 - 0.2 + 1) + 0.2;
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

	public void spawnSnakeFood() {
		Circle fruit = new Circle(30 / ResolutionScaleX - (5), new ImagePattern(GameImageBank.fruit));
		float x = (int) (Math.random() * ((Settings.WIDTH - fruit.getRadius() * 3) - fruit.getRadius() * 3 + 1)
				+ fruit.getRadius() * 3);
		float y = (int) (Math.random() * ((Settings.HEIGHT - fruit.getRadius() * 3) - fruit.getRadius() * 5 + 1)
				+ fruit.getRadius() * 5);
		SnakeFood food = new SnakeFood(game, game.getPlayfieldLayer(), fruit, x, y, GameObjectID.Fruit);
		game.getObjectManager().addObject(food);
	}
	public void setPlayerOne(PlayerOne player) {
		this.player = player;
	}

	public void setPlayerTwo(PlayerTwo player) {
		this.player2 = player;
	}

	public PlayerOne getPlayerOne() {
		return player;
	}

	public PlayerTwo getPlayerTwo() {
		return player2;
	}

	public SlitherSnake getSlither() {
		return slither;
	}

	public void killPlayerOne() {
		player = null;
	}

	public void killPlayerTwo() {
		player2 = null;
	}


}
