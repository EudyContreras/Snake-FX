package com.SnakeGame.GameObjects;

import java.util.Random;

import com.SnakeGame.FrameWork.AbstractObject;
import com.SnakeGame.FrameWork.AbstractSection;
import com.SnakeGame.FrameWork.AbstractTile;
import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ObjectIDs.GameObjectID;
import com.SnakeGame.ObjectIDs.LevelObjectID;
import com.SnakeGame.Particles.FruitSplashOne;
import com.SnakeGame.Particles.FruitSplashTwo;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerOne.PlayerOneFangs;
import com.SnakeGame.PlayerTwo.PlayerTwo;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class SnakeFood extends AbstractObject {

	private double size;
	private double targetSize;
	private double particleLife;
	private double particleSize;
	private double fadeValue = 0.0;
	private double glowValue = 0.0;
	private double staticRadius = 0;
	private double lifeTime = 0;
	private boolean remainStatic = false;
	private boolean maxSize = false;
	private boolean minSize = true;
	private boolean maxGlow = false;
	private boolean noGlow = true;
	private Circle bounds;
	private DropShadow borderGlow = new DropShadow();
	private Random rand = new Random();
	private SnakeGame game;

	public SnakeFood(SnakeGame game, Pane layer, Node node, float x, float y, GameObjectID id) {
		super(game, layer, node, x, y, id);
		this.game = game;
	}

	public SnakeFood(SnakeGame game, Pane layer, Circle node, float x, float y, GameObjectID id) {
		super(game, layer, node, x, y, id);
		this.game = game;
		this.circle.setOpacity(fadeValue);
		this.size = circle.getRadius();
		this.targetSize = size;
		this.staticRadius = targetSize + 10;
		this.addGLow();
		if (Settings.DEBUG_MODE) {
			this.bounds = new Circle(x, y, node.getRadius(), Color.TRANSPARENT);
			this.bounds.setStroke(Color.WHITE);
			this.bounds.setStrokeWidth(3);
			this.layer.getChildren().add(bounds);
		}
	}

	public void addGLow() {
		if (Settings.ADD_GLOW) {
			borderGlow.setOffsetY(0f);
			borderGlow.setOffsetX(0f);
			borderGlow.setColor(Color.rgb(255, 255, 255, 1));
			borderGlow.setWidth(60);
			borderGlow.setHeight(50);
			borderGlow.setSpread(0.3);
			borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
			circle.setEffect(borderGlow);
		}
	}

	public void logicUpdate() {
		fadeValue += 0.01;
		if (fadeValue >= 1.0) {
			fadeValue = 1.0;
		}
		fruitFadein();
		lookAtMe();
		updateGlow();
		updateLife();
	}

	public void move() {
		if (Settings.DEBUG_MODE) {
			bounds.setCenterX(x);
			bounds.setCenterY(y);
			bounds.setRadius(size);
		}
		super.move();
		if (velX > 0) {
			velX -= 0.2;
		}
		if (velY > 0) {
			velY -= 0.2;
		}
		if (velX < 0) {
			velX += 0.2;
		}
		if (velY < 0) {
			velY += 0.2;
		}
		velX *= 0.97;
		velY *= 0.97;
	}

	public void updateLife() {
		if (fadeValue >= 1.0) {
			lifeTime++;
			if (lifeTime >= 80) {
				lifeTime = 80;
				remainStatic = true;
			}
		}
	}

	public void updateGlow() {
		if (Settings.ADD_GLOW) {
			if (fadeValue == 1.0) {
				if (noGlow) {
					glowValue += 2;
					borderGlow.setWidth(glowValue);
					borderGlow.setHeight(glowValue);
					if (glowValue >= 150) {
						noGlow = false;
						maxGlow = true;
					}
				}
				if (maxGlow) {
					glowValue -= 2;
					borderGlow.setWidth(glowValue);
					borderGlow.setHeight(glowValue);
					if (glowValue <= 0) {
						maxGlow = false;
						noGlow = true;
					}
				}
			}

			if (fadeValue < 1.0) {
				glowValue = 0;
				borderGlow.setWidth(glowValue);
				borderGlow.setHeight(glowValue);
			}
		}
	}

	public void lookAtMe() {
		if (minSize) {
			size += 0.5 * Settings.FRAME_SCALE;
			circle.setRadius(size);
			if (size >= targetSize + 10) {
				minSize = false;
				maxSize = true;
			}
		}
		if (maxSize) {
			size -= 0.5 * Settings.FRAME_SCALE;
			circle.setRadius(size);
			if (size <= targetSize - 5) {
				maxSize = false;
				minSize = true;
			}
		}
	}

	public boolean isApproximate(double tail_X, double sect_X, double tail_Y, double sect_Y) {
		double distance = Math.sqrt((tail_X - sect_X) * (tail_X - sect_X) + (tail_Y - sect_Y) * (tail_Y - sect_Y));
		if (distance > 10) {
			return true;
		}
		return false;
	}

	public boolean isApproximateTo(PlayerOneFangs object) {
		double distance = Math
				.sqrt((x - object.getX()) * (x - object.getX()) + (y - object.getY()) * (y - object.getY()));
		if (distance < 15) {
			return true;
		}
		return false;
	}

	public void checkCollision() {
		float newX = (int) (rand.nextDouble() * ((Settings.WIDTH - 30 * 3) - (30 * 3) + 1) + 30 * 3);
		float newY = (int) (rand.nextDouble() * ((Settings.HEIGHT - 30 * 3) - (30 * 3) + 1) + 30 * 3);
		for (AbstractTile tempTile : game.getGameLoader().tileManager.block) {
			if (tempTile.getId() == LevelObjectID.rock || tempTile.getId() == LevelObjectID.cactus) {
				if (getBounds().intersects(tempTile.getBounds())) {
					this.x = newX;
					this.y = newY;
					this.fadeValue = 0;
				}
			}
		}
		for (AbstractTile tempTile : game.getGameLoader().tileManager.tile) {
			if (tempTile.getId() == LevelObjectID.cactus) {
				if (getBounds().intersects(tempTile.getBounds())) {
					this.x = newX;
					this.y = newY;
					this.fadeValue = 0;
				}
			}
		}
		for (AbstractTile tempTile : game.getGameLoader().tileManager.tile) {
			if (tempTile.getId() == LevelObjectID.fence) {
				if (getCollisionBounds().intersects(tempTile.getBounds())) {
					this.blowUp();
					this.game.getGameLoader().spawnSnakeFood();
					this.remove();
					break;
				}
			}
		}
		for (AbstractSection object : game.getSectManagerOne().getSectionList()) {
			if (object.getNumericID() > 1) {
				if (getBounds().intersects(object.getBounds())) {
					if (!remainStatic) {
						this.x = newX;
						this.y = newY;
						this.fadeValue = 0;
						lifeTime = 0;
					}
				}
			}
		}
		for (AbstractSection object : game.getSectManagerTwo().getSectionList()) {
			if (object.getNumericID() > 1) {
				if (getBounds().intersects(object.getBounds())) {
					if (!remainStatic) {
						this.x = newX;
						this.y = newY;
						this.fadeValue = 0;
						lifeTime = 0;
					}
				}
			}
		}
	}

	public boolean allowedLocation() {
		double newX = rand.nextDouble() * ((Settings.WIDTH - 30 * 3) - (30 * 3) + 1) + 30 * 3;
		double newY = rand.nextDouble() * ((Settings.HEIGHT - 30 * 3) - (30 * 3) + 1) + 30 * 3;
		for (AbstractTile tempTile : game.getGameLoader().tileManager.tile) {
			if (tempTile.getId() == LevelObjectID.rock || tempTile.getId() == LevelObjectID.cactus) {
				if (!new Rectangle2D(newX, newY, radius, radius).intersects(tempTile.getBounds())) {
					return true;
				}
			}
		}
		return false;
	}

	public void fruitFadein() {
		this.circle.setOpacity(fadeValue);
	}

	public void blowUp() {
		for (int i = 0; i < Settings.PARTICLE_LIMIT; i++) {
			if (Settings.ADD_VARIATION) {
				particleSize = (Math.random() * (20 - 5 + 1) + 5)
						/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
				particleLife = (Math.random() * (1.5 - 0.5 + 1) + 0.5)
						/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
			}
			game.getDebrisManager().addObject(new FruitSplashOne(game, new ImagePattern(GameImageBank.fruit2),
					particleLife, particleSize, (double) (x + this.radius / 2), (double) (y + this.radius / 2)));
		}
	}

	public void blowUp2() {
		for (int i = 0; i < Settings.PARTICLE_LIMIT; i++) {
			if (Settings.ADD_VARIATION) {
				particleSize = (Math.random() * (40 - 10 + 1) + 10)
						/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
				particleLife = (Math.random() * (1.5 - 0.5 + 1) + 0.5)
						/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
			}
			game.getDebrisManager().addObject(new FruitSplashTwo(game, new ImagePattern(GameImageBank.glowingCircle),
					particleLife, particleSize, (float) (x + this.radius / 2), (float) (y + this.radius / 2)));
		}
	}

	public void bounce(PlayerOne snake, float x, float y) {
		if (snake.getVelX() == 0) {
			this.velX = (float) (snake.getVelX() + (Math.random() * (13 - 8 + 1) - 8));
			this.velY = (float) (snake.getVelY() + (Math.random() * (7 - 4 + 1) - 4));
		} else if (snake.getVelY() == 0) {
			this.velX = (float) (snake.getVelX() + (Math.random() * (7 - 4 + 1) - 4));
			this.velY = (float) (snake.getVelY() + (Math.random() * (13 - 8 + 1) - 8));
		}
	}

	public void bounce(PlayerTwo snake, double x, double y) {
		if (snake.getVelX() == 0) {
			this.velX = (float) (snake.getVelX() + (Math.random() * (13 - 8 + 1) - 8));
			this.velY = (float) (snake.getVelY() + (Math.random() * (7 - 4 + 1) - 4));
		} else if (snake.getVelY() == 0) {
			this.velX = (float) (snake.getVelX() + (Math.random() * (7 - 4 + 1) - 4));
			this.velY = (float) (snake.getVelY() + (Math.random() * (13 - 8 + 1) - 8));
		}
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D(x - staticRadius, y - staticRadius, staticRadius * 2, staticRadius * 2);
	}

	public Rectangle2D getCollisionBounds() {
		return new Rectangle2D(x - staticRadius / 2, y - staticRadius / 2, staticRadius, staticRadius);
	}
}
