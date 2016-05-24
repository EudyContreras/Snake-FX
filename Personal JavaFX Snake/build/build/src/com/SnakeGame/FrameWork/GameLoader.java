package com.SnakeGame.FrameWork;

import java.awt.Toolkit;
import java.util.Random;

import com.SnakeGame.AbstractModels.AbstractLoaderModel;
import com.SnakeGame.GameObjects.DesertBark;
import com.SnakeGame.GameObjects.DesertBush;
import com.SnakeGame.GameObjects.DesertCactusBig;
import com.SnakeGame.GameObjects.DesertCactusSmall;
import com.SnakeGame.GameObjects.DesertRock;
import com.SnakeGame.GameObjects.GenericObject;
import com.SnakeGame.GameObjects.SnakeFood;
import com.SnakeGame.GameObjects.SpikeFence;
import com.SnakeGame.IDenums.GameLevelObjectID;
import com.SnakeGame.IDenums.GameObjectID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ImageBanks.GameLevelImage;
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
public class GameLoader extends AbstractLoaderModel{

	public GameLoader(SnakeGame game) {
		this.game = game;
		this.rand = new Random();
		this.objectManger = game.getObjectManager();
		this.setTileManager(new GameTileManager(game));
		loadDesertLevels();
		initializeMain();
	}
	public void initializeMain(){
		this.border = GameImageLoader.loadImage("/desert-level-border.png");
		this.fence = GameImageLoader.loadImage("/desert-level-fence.png");
		this.levelMain = GameImageLoader.loadImage("/desert-level.png");
		this.setLevel(levelMain);
		this.levelWidth = getLevel().getWidth();
		this.levelHeight = getLevel().getHeight();
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
		System.out.println("width scale = " + ResolutionScaleX);
		System.out.println("height scale = " + ResolutionScaleY);

	}

	/**
	 * Method which will attempt to scale the speed and the size of the snake according
	 * to the new scaled resolution, The size and the speed are relative. The size must be
	 * able to be divided by the speed and the result must always be a whole number
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
		if(!Settings.LOAD_SPIKE_FENCE)
		loadDesertBorder();
		if(Settings.LOAD_SPIKE_FENCE){
			loadSpikeFence();
		}
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

	public void loadDesertLevels(Image image) {
		changeBackground(image);
		for (double row = 0; row < levelWidth; row++) {
			for (double col = 0; col < levelHeight; col++) {
				pixel = getLevel().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 0 && green == 0 && blue == 255) {
					DesertBush texture = new DesertBush(game,(float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 55.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.desert_bush,
							GameLevelObjectID.bush);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}else if (red == 255 && green == 0 && blue == 0) {
					DesertRock texture = new DesertRock(game, (float) (row * 55 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_rock,
							GameLevelObjectID.rock);
					getTileManager().addBlock(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 0) {
					DesertCactusBig texture = new DesertCactusBig((float) (row * 53 / GameLoader.ResolutionScaleX),
							(float) (col * 52 / GameLoader.ResolutionScaleY), 4, GameLevelImage.desert_cactus_big,
							GameLevelObjectID.longCactus);
					getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 255 && blue == 0) {
					GenericObject texture = new GenericObject(game, (float) (row * 58 / GameLoader.ResolutionScaleX),
							(float) (col * 57 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_skull,
							GameLevelObjectID.skeleton);
					getTileManager().addTile(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 100 && blue == 0) {
					DesertCactusSmall texture = new DesertCactusSmall(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 48.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_cactus_small,
							GameLevelObjectID.cactus);
					getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 255) {
					DesertBush texture = new DesertBush(game,(float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.desert_bush,
							GameLevelObjectID.bush);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 200 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_flower,
							GameLevelObjectID.flower);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}else if (red == 0 && green == 255 && blue == 255) {
					DesertBark texture = new DesertBark(game, (float) (row * 44 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_bark,
							GameLevelObjectID.treeBark);
					getTileManager().addTile(texture);
					game.getEighthLayer().getChildren().add(texture.getView());
				}
			}
		}
		getLevel().flush();
	}
	/**
	 * This method will load the border of the desert level if there is any.
	 */
	private void loadDesertBorder() {
		for (double row = levelWidth - 1; row >= 0; row--) {
			for (double col = levelHeight - 1; col >= 0; col--) {
				pixel = getBorder().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 255 && green == 255 && blue == 255) {
					SpikeFence texture = new SpikeFence(game, (float) (row * 150 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, 1,GameLevelImage.horizontalFence,
							GameLevelObjectID.fence);
					getTileManager().addTrap(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}
				 if (red == 0 && green == 0 && blue == 255) {
					SpikeFence texture = new SpikeFence(game, (float) (row * 49 / GameLoader.ResolutionScaleX),
							(float) (col * 100 / GameLoader.ResolutionScaleY), 0, 0, 2,GameLevelImage.verticalFence,
							GameLevelObjectID.fence);
					getTileManager().addTrap(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}
			}
		}
		getBorder().flush();
	}
	/**
	 * This method will load a spike fence along the border of the screen
	 * making it so that the snake can no longer teleport
	 */
	private void loadSpikeFence() {
		for (double row = 0; row < levelWidth; row++) {
			for (double col = 0; col < levelHeight; col++) {
				pixel = getSpikeFence().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 255 && green == 255 && blue == 255) {
					SpikeFence texture = new SpikeFence(game, (float) (row * 150 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, 1,GameLevelImage.horizontalFence,
							GameLevelObjectID.fence);
					getTileManager().addTrap(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}
				if (red == 0 && green == 0 && blue == 255) {
					SpikeFence texture = new SpikeFence(game, (float) (row * 50.5-5 / GameLoader.ResolutionScaleX),
							(float) (col * 150 / GameLoader.ResolutionScaleY), 0, 0, 2,GameLevelImage.verticalFence,
							GameLevelObjectID.fence);
					getTileManager().addTrap(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}
				if (red == 255 && green == 0 && blue == 255) {
					SpikeFence textureOne = new SpikeFence(game, (float) (row * 50.5-5 / GameLoader.ResolutionScaleX),
							(float) (col * 150 / GameLoader.ResolutionScaleY), 0, 0, 2,GameLevelImage.verticalFence,
							GameLevelObjectID.fence);
					getTileManager().addTrap(textureOne);
					game.getThirdLayer().getChildren().add(textureOne.getView());
					SpikeFence textureTwo = new SpikeFence(game, (float) (row * 150 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, 1,GameLevelImage.horizontalFence,
							GameLevelObjectID.fence);
					getTileManager().addTrap(textureTwo);
					game.getThirdLayer().getChildren().add(textureTwo.getView());
				}
			}
		}
		getSpikeFence().flush();
	}

	public void loadJungleLevels(Image image) {
		changeBackground(image);
		for (double row = 0; row < levelWidth; row++) {
			for (double col = 0; col < levelHeight; col++) {
				pixel = levelMain.getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 255 && green == 255 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 120 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.horizontalFence,
							GameLevelObjectID.fence);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 0 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 50.5 / GameLoader.ResolutionScaleX),
							(float) (col * 100 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.verticalFence,
							GameLevelObjectID.fence);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 0) {
					GenericObject texture = new GenericObject(game, (float) (row * 55 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_rock,
							GameLevelObjectID.rock);
					getTileManager().addTile(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 0) {
					DesertCactusBig texture = new DesertCactusBig((float) (row * 53 / GameLoader.ResolutionScaleX),
							(float) (col * 52 / GameLoader.ResolutionScaleY), 4, GameLevelImage.desert_cactus_big,
							GameLevelObjectID.rock);
					getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 255 && blue == 0) {
					GenericObject texture = new GenericObject(game, (float) (row * 58 / GameLoader.ResolutionScaleX),
							(float) (col * 57 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_skull,
							GameLevelObjectID.rock);
					getTileManager().addTile(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 100 && blue == 0) {
					DesertCactusSmall texture = new DesertCactusSmall(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 48.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_cactus_small,
							GameLevelObjectID.cactus);
					getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 50 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_bark,
							GameLevelObjectID.LevelTile);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 255) {
					DesertCactusBig texture = new DesertCactusBig((float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.desert_bush,
							GameLevelObjectID.flower);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 200 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_flower,
							GameLevelObjectID.flower);
					getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				}
			}
		}
		getLevel().flush();
	}

	public void loadSeaLevels(Image image) {
		changeBackground(image);
		for (double row = 0; row < levelWidth; row++) {
			for (double col = 0; col < levelHeight; col++) {
				pixel = levelMain.getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 255 && green == 255 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 120 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.horizontalFence,
							GameLevelObjectID.fence);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 0 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 50.5 / GameLoader.ResolutionScaleX),
							(float) (col * 100 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.verticalFence,
							GameLevelObjectID.fence);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 0) {
					GenericObject texture = new GenericObject(game, (float) (row * 55 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_rock,
							GameLevelObjectID.rock);
					getTileManager().addTile(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 0) {
					DesertCactusBig texture = new DesertCactusBig((float) (row * 53 / GameLoader.ResolutionScaleX),
							(float) (col * 52 / GameLoader.ResolutionScaleY), 4, GameLevelImage.desert_cactus_big,
							GameLevelObjectID.rock);
					getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 255 && blue == 0) {
					GenericObject texture = new GenericObject(game, (float) (row * 58 / GameLoader.ResolutionScaleX),
							(float) (col * 57 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_skull,
							GameLevelObjectID.rock);
					getTileManager().addTile(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 100 && blue == 0) {
					DesertCactusSmall texture = new DesertCactusSmall(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 48.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_cactus_small,
							GameLevelObjectID.cactus);
					getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 50 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_bark,
							GameLevelObjectID.LevelTile);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 255) {
					DesertCactusBig texture = new DesertCactusBig((float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.desert_bush,
							GameLevelObjectID.flower);
					getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 200 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_flower,
							GameLevelObjectID.flower);
					getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
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
					GenericObject texture = new GenericObject(game, (float) (row * 40 / 2), (float) (col * 50 / 2), 0, 0,
							GameLevelImage.desert_cactus_small, GameLevelObjectID.rock);
					getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
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
				pixel = desertLevel_1.getRGB(row, col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 0 && green == 0 && blue == 255) {
					this.playerOne = new PlayerOne(game, game.getThirdLayer(), GameImageBank.snakeOneSphere, row * 20,
							col * 20, 0, 0, 0, 0, Settings.PLAYER_HEALTH, 0, Settings.PLAYER_SPEED, GameObjectID.PlayerOne,
							game.getObjectManager());
					game.getObjectManager().addObject(playerOne);
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
		Front_Distance_LOD = 512;
		Rear_Distance_LOD = 511;
		switch (GameLevelImage.LEVEL) {
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

		this.levelWidth = getLevel().getWidth();
		this.levelHeight = getLevel().getHeight();
		loadPixelMap();
		GameLevelImage.LEVEL++;
	}


	/**
	 * Method responsible for the procedural creation of a level it loads and
	 * culls specific sections of the level at a predetermined speed. TODO:
	 * maybe allow the procedural creation speed to be dynamic
	 */
	public void procedurallyCreateLevel() {
		if (Front_Distance_LOD <= 512 && Rear_Distance_LOD <= 511) {
			if (playerOne != null) {
				loadDesertLevels(GameLevelImage.desertBackground);
				cullTheLevel();
			}
		}
	}

	/**
	 * Method responsible for rendering and moving and removing level objects.
	 */
	public void updateLevelObjects() {
		getTileManager().updateTiles();
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
	 * Method used to create the player one and position the player at a specified
	 * position.
	 */
	public void loadPlayerOne() {
		float x = (float) (Settings.WIDTH / 2 - GameImageBank.snakeOneSphere.getRadius());
		float y = (float) (Settings.HEIGHT * 0.50);
		playerOne = new PlayerOne(game, game.getFourthLayer(),
				new Circle(Settings.SECTION_SIZE, new ImagePattern(GameImageBank.snakeOneSkin)), x, y, 0, 0, 0, 0,
				Settings.PLAYER_HEALTH, 0, Settings.PLAYER_SPEED, GameObjectID.PlayerOne, game.getObjectManager());
		game.getObjectManager().addObject(playerOne);
	}
	/**
	 * Method used to create the player two and position the player at a specified
	 * position.
	 */
	public void loadPlayerTwo() {
		float x = (float) (Settings.WIDTH / 2 + GameImageBank.snakeTwoSphere.getRadius());
		float y = (float) (Settings.HEIGHT * 0.50);
		playerTwo = new PlayerTwo(game, game.getFithLayer(),
				new Circle(Settings.SECTION_SIZE, new ImagePattern(GameImageBank.snakeTwoSkin)), x, y, 0, 0, 0, 0,
				Settings.PLAYER_HEALTH, 0, Settings.PLAYER_SPEED, GameObjectID.PlayerTwo, game.getObjectManager());
		game.getObjectManager().addObject(playerTwo);
	}
	/**
	 * Method used to create the Slither snake and position the player at a specified
	 * position.
	 */
	public void createSlither() {
		float x = (float) (Settings.WIDTH / 2 + 25);
		float y = (float) (Settings.HEIGHT * 0.55);
		slither = new SlitherSnake(game, game.getFithLayer(), GameImageBank.slither, x, y, 0, 0, 0, 0,
				Settings.PLAYER_HEALTH, 0, 0, GameObjectID.SlitherSnake, game.getSlitherManager());
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
	/**
	 * Method responsible for spawning the food on the level
	 */
	public void spawnSnakeFood() {
		Circle fruit = new Circle(30 / ResolutionScaleX - (5), new ImagePattern(GameImageBank.fruit));
		float x = (int) (Math.random() * ((Settings.WIDTH - fruit.getRadius() * 3) - fruit.getRadius() * 3 + 1)
				+ fruit.getRadius() * 3);
		float y = (int) (Math.random() * ((Settings.HEIGHT - fruit.getRadius() * 3) - fruit.getRadius() * 5 + 1)
				+ fruit.getRadius() * 5);
		SnakeFood food = new SnakeFood(game, game.getThirdLayer(), fruit, x, y, GameObjectID.Fruit);
		game.getObjectManager().addObject(food);
	}


}
