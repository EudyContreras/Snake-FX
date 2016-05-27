package com.SnakeGame.FrameWork;

import com.SnakeGame.AbstractModels.AbstractLoaderModel;
import com.SnakeGame.GameObjects.DesertBark;
import com.SnakeGame.GameObjects.DesertBones;
import com.SnakeGame.GameObjects.DesertBush;
import com.SnakeGame.GameObjects.DesertCactusBig;
import com.SnakeGame.GameObjects.DesertCactusSmall;
import com.SnakeGame.GameObjects.DesertFlower;
import com.SnakeGame.GameObjects.DesertRock;
import com.SnakeGame.GameObjects.DesertTrap;
import com.SnakeGame.GameObjects.GenericObject;
import com.SnakeGame.GameObjects.SpikeFence;
import com.SnakeGame.IDenums.GameLevelObjectID;
import com.SnakeGame.IDenums.GameObjectID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ImageBanks.GameLevelImage;
import com.SnakeGame.PlayerOne.PlayerOne;

import javafx.scene.image.Image;

/**
 * The GameLoader class is the class responsible for creating and loading the
 * level. this class calculates where every object will be positioned and will
 * also take care of spawning random objects on random locations
 *
 * @author Eudy Contreras
 *
 */
public class LevelManager extends AbstractLoaderModel{
	private GameLoader loader;
	private GameManager game;

	public LevelManager(GameManager game, GameLoader loader) {
		this.game = game;
		this.loader = loader;
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
		for (double row = 0; row < loader.getLevelWidth(); row++) {
			for (double col = 0; col < loader.getLevelHeight(); col++) {
				pixel = loader.getLevel().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 0 && green == 0 && blue == 255) {
					DesertBush texture = new DesertBush(game,(float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 55.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.desert_bush,
							GameLevelObjectID.bush);
					loader.getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}else if (red == 255 && green == 0 && blue == 0) {
					DesertRock texture = new DesertRock(game, (float) (row * 55 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_rock,
							GameLevelObjectID.rock);
					loader.getTileManager().addBlock(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 0) {
					DesertCactusBig texture = new DesertCactusBig((float) (row * 53 / GameLoader.ResolutionScaleX),
							(float) (col * 50 / GameLoader.ResolutionScaleY), 4, GameLevelImage.desert_cactus_big,
							GameLevelObjectID.longCactus);
					loader.getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 255 && blue == 0) {
					DesertBones texture = new DesertBones(game, (float) (row * 58 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_skull,
							GameLevelObjectID.skeleton);
					loader.getTileManager().addTile(texture);
					game.getDirtLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 100 && blue == 0) {
					DesertCactusSmall texture = new DesertCactusSmall(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 48.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_cactus_small,
							GameLevelObjectID.cactus);
					loader.getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				}else if (red == 255 && green == 0 && blue == 125) {
					DesertTrap texture = new DesertTrap(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 48.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_trap,
							GameLevelObjectID.fence);
					loader.getTileManager().addTrap(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				}else if (red == 255 && green == 0 && blue == 255) {
					DesertBush texture = new DesertBush(game,(float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.desert_bush,
							GameLevelObjectID.bush);
					loader.getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 200 && blue == 255) {
					DesertFlower texture = new DesertFlower(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_flower,
							GameLevelObjectID.flower);
					loader.getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}else if (red == 0 && green == 255 && blue == 255) {
					DesertBark texture = new DesertBark(game, (float) (row * 44 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_bark,
							GameLevelObjectID.treeBark);
					loader.getTileManager().addTile(texture);
					game.getEighthLayer().getChildren().add(texture.getView());
				}
			}
		}
		loader.getLevel().flush();
	}
	/**
	 * This method will load the border of the desert level if there is any.
	 */
	public void loadDesertBorder() {
		for (double row = loader.getLevelWidth() - 1; row >= 0; row--) {
			for (double col = loader.getLevelHeight() - 1; col >= 0; col--) {
				pixel = loader.getBorder().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 255 && green == 255 && blue == 255) {
					SpikeFence texture = new SpikeFence(game, (float) (row * 150 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, 1,GameLevelImage.horizontalFence,
							GameLevelObjectID.fence);
					loader.getTileManager().addTrap(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}
				 if (red == 0 && green == 0 && blue == 255) {
					SpikeFence texture = new SpikeFence(game, (float) (row * 49 / GameLoader.ResolutionScaleX),
							(float) (col * 100 / GameLoader.ResolutionScaleY), 0, 0, 2,GameLevelImage.verticalFence,
							GameLevelObjectID.fence);
					loader.getTileManager().addTrap(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}
			}
		}
		loader.getBorder().flush();
	}
	/**
	 * This method will load a spike fence along the border of the screen
	 * making it so that the snake can no longer teleport
	 */
	public void loadSpikeFence() {
		for (double row = 0; row < loader.getLevelWidth(); row++) {
			for (double col = 0; col < loader.getLevelHeight(); col++) {
				pixel = loader.getSpikeFence().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 255 && green == 255 && blue == 255) {
					SpikeFence texture = new SpikeFence(game, (float) (row * 150 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, 1,GameLevelImage.horizontalFence,
							GameLevelObjectID.fence);
					loader.getTileManager().addTrap(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				}
				if (red == 0 && green == 0 && blue == 255) {
					SpikeFence texture = new SpikeFence(game, (float) (row * 50.5-5 / GameLoader.ResolutionScaleX),
							(float) (col * 150 / GameLoader.ResolutionScaleY), 0, 0, 2,GameLevelImage.verticalFence,
							GameLevelObjectID.fence);
					loader.getTileManager().addTrap(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				}
				if (red == 255 && green == 0 && blue == 255) {
					SpikeFence textureOne = new SpikeFence(game, (float) (row * 50.5-5 / GameLoader.ResolutionScaleX),
							(float) (col * 150 / GameLoader.ResolutionScaleY), 0, 0, 2,GameLevelImage.verticalFence,
							GameLevelObjectID.fence);
					loader.getTileManager().addTrap(textureOne);
					game.getFirstLayer().getChildren().add(textureOne.getView());
					SpikeFence textureTwo = new SpikeFence(game, (float) (row * 150 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, 1,GameLevelImage.horizontalFence,
							GameLevelObjectID.fence);
					loader.getTileManager().addTrap(textureTwo);
					game.getFirstLayer().getChildren().add(textureTwo.getView());
				}
			}
		}
		loader.getSpikeFence().flush();
	}

	public void loadJungleLevels(Image image) {
		changeBackground(image);
		for (double row = 0; row < loader.getLevelWidth(); row++) {
			for (double col = 0; col < loader.getLevelHeight(); col++) {
				pixel = loader.getLevel().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 0 && green == 0 && blue == 255) {
					DesertBush texture = new DesertBush(game,(float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 55.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.desert_bush,
							GameLevelObjectID.bush);
					loader.getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}else if (red == 255 && green == 0 && blue == 0) {
					DesertRock texture = new DesertRock(game, (float) (row * 55 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_rock,
							GameLevelObjectID.rock);
					loader.getTileManager().addBlock(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 0) {
					DesertCactusBig texture = new DesertCactusBig((float) (row * 53 / GameLoader.ResolutionScaleX),
							(float) (col * 50 / GameLoader.ResolutionScaleY), 4, GameLevelImage.desert_cactus_big,
							GameLevelObjectID.longCactus);
					loader.getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 255 && blue == 0) {
					GenericObject texture = new GenericObject(game, (float) (row * 58 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_skull,
							GameLevelObjectID.skeleton);
					loader.getTileManager().addTile(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 100 && blue == 0) {
					DesertCactusSmall texture = new DesertCactusSmall(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 48.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_cactus_small,
							GameLevelObjectID.cactus);
					loader.getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 255) {
					DesertBush texture = new DesertBush(game,(float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.desert_bush,
							GameLevelObjectID.bush);
					loader.getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 200 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_flower,
							GameLevelObjectID.flower);
					loader.getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}else if (red == 0 && green == 255 && blue == 255) {
					DesertBark texture = new DesertBark(game, (float) (row * 44 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_bark,
							GameLevelObjectID.treeBark);
					loader.getTileManager().addTile(texture);
					game.getEighthLayer().getChildren().add(texture.getView());
				}
			}
		}
		loader.getLevel().flush();
	}

	public void loadSeaLevels(Image image) {
		changeBackground(image);
		for (double row = 0; row < loader.getLevelWidth(); row++) {
			for (double col = 0; col < loader.getLevelHeight(); col++) {
				pixel = loader.getLevel().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 0 && green == 0 && blue == 255) {
					DesertBush texture = new DesertBush(game,(float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 55.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.desert_bush,
							GameLevelObjectID.bush);
					loader.getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}else if (red == 255 && green == 0 && blue == 0) {
					DesertRock texture = new DesertRock(game, (float) (row * 55 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_rock,
							GameLevelObjectID.rock);
					loader.getTileManager().addBlock(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 255 && blue == 0) {
					DesertCactusBig texture = new DesertCactusBig((float) (row * 53 / GameLoader.ResolutionScaleX),
							(float) (col * 50 / GameLoader.ResolutionScaleY), 4, GameLevelImage.desert_cactus_big,
							GameLevelObjectID.longCactus);
					loader.getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 255 && blue == 0) {
					GenericObject texture = new GenericObject(game, (float) (row * 58 / GameLoader.ResolutionScaleX),
							(float) (col * 53 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_skull,
							GameLevelObjectID.skeleton);
					loader.getTileManager().addTile(texture);
					game.getFirstLayer().getChildren().add(texture.getView());
				} else if (red == 0 && green == 100 && blue == 0) {
					DesertCactusSmall texture = new DesertCactusSmall(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 48.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_cactus_small,
							GameLevelObjectID.cactus);
					loader.getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 0 && blue == 255) {
					DesertBush texture = new DesertBush(game,(float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, GameLevelImage.desert_bush,
							GameLevelObjectID.bush);
					loader.getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				} else if (red == 255 && green == 200 && blue == 255) {
					GenericObject texture = new GenericObject(game, (float) (row * 50 / GameLoader.ResolutionScaleX),
							(float) (col * 47.5 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_flower,
							GameLevelObjectID.flower);
					loader.getTileManager().addTile(texture);
					game.getThirdLayer().getChildren().add(texture.getView());
				}else if (red == 0 && green == 255 && blue == 255) {
					DesertBark texture = new DesertBark(game, (float) (row * 44 / GameLoader.ResolutionScaleX),
							(float) (col * 55 / GameLoader.ResolutionScaleY), 0, 0, GameLevelImage.desert_bark,
							GameLevelObjectID.treeBark);
					loader.getTileManager().addTile(texture);
					game.getEighthLayer().getChildren().add(texture.getView());
				}
			}
		}
		loader.getLevel().flush();
	}

	public void loadLevelOverlay() {
		for (double row = 0; row < loader.getLevelWidth(); row++) {
			for (double col = 0; col < loader.getLevelHeight(); col++) {
				pixel = loader.getOverlay().getRGB((int) row, (int) col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				if (red == 0 && green == 100 && blue == 0) {
					GenericObject texture = new GenericObject(game, (float) (row * 40 / 2), (float) (col * 50 / 2), 0, 0,
							GameLevelImage.desert_cactus_small, GameLevelObjectID.rock);
					loader.getTileManager().addTile(texture);
					game.getSecondLayer().getChildren().add(texture.getView());
				}
			}
		}
		getOverlay().flush();
	}

	/**
	 * Will load the player at a specified positions according to a color code
	 * found within the level image.
	 */
	public void loadPlayer() {
		for (int row = 0; row < loader.getLevelWidth(); row++) {
			for (int col = 0; col < loader.getLevelHeight(); col++) {
				pixel = loader.getLevel().getRGB(row, col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (red == 0 && green == 0 && blue == 255) {
					this.playerOne = new PlayerOne(game, game.getThirdLayer(), GameImageBank.snakeOneSphere, row * 20,
							col * 20, 0, 0, 0, 0, GameSettings.PLAYER_HEALTH, 0, GameSettings.PLAYER_SPEED, GameObjectID.PlayerOne,
						game.getObjectManager());
						game.getObjectManager().addObject(playerOne);
				}
			}
		}
		getLevel().flush();
	}

	public void levelTransition() {

	}
}
