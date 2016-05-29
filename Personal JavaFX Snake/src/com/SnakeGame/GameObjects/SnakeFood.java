package com.SnakeGame.GameObjects;

import java.util.Random;

import com.SnakeGame.AbstractModels.AbstractObject;
import com.SnakeGame.AbstractModels.AbstractSection;
import com.SnakeGame.AbstractModels.AbstractTile;
import com.SnakeGame.DebrisEffects.FruitSplashOne;
import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.IDenums.GameLevelObjectID;
import com.SnakeGame.IDenums.GameObjectID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ParticleEffects.GlowParticle;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.Utilities.RandomGenerator;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * This class is in charge of managing the logic surrounding the spawning of
 * food or buffs within the level.
 *
 * @author Eudy Contreras
 *
 */
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
	private GameManager game;

	/**
	 * Main constructor which can initialize an edible object
	 *
	 * @param game
	 * @param layer
	 * @param node
	 * @param x
	 * @param y
	 * @param id
	 */
	public SnakeFood(GameManager game, Pane layer, Node node, float x, float y, GameObjectID id, int numericCode) {
		super(game, layer, node, x, y, id);
		this.numericCode = numericCode;
		this.game = game;
	}

	/**
	 * Main constructor which will initialize an edible object and modified some
	 * of the initial valuses.
	 *
	 * @param game
	 * @param layer
	 * @param node
	 * @param x
	 * @param y
	 * @param id
	 */

	public SnakeFood(GameManager game, Pane layer, Circle node, float x, float y, GameObjectID id, int numericCode) {
		super(game, layer, node, x, y, id);
		this.game = game;
		this.numericCode = numericCode;
		this.circle.setOpacity(fadeValue);
		this.size = circle.getRadius();
		this.targetSize = size;
		this.staticRadius = targetSize + 10;
		this.addGLow();
		if (GameSettings.DEBUG_MODE) {
			this.bounds = new Circle(x, y, node.getRadius(), Color.TRANSPARENT);
			this.bounds.setStroke(Color.WHITE);
			this.bounds.setStrokeWidth(3);
			this.layer.getChildren().add(bounds);
		}
	}

	/**
	 * Adds a glowing aura around the node or image
	 */
	public void addGLow() {
		if (GameSettings.ADD_GLOW) {
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

	/**
	 * Updates methods which do not involve movement
	 */
	public void logicUpdate() {
		fadeUpdate();
		fruitFadein();
		lookAtMe();
		updateGlow();
		updateLife();
		checkBoundaries();
	}

	/**
	 * Method in charge of moving the node.
	 */
	public void move() {
		if (GameSettings.DEBUG_MODE) {
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

	/**
	 * Method in charge of updating the lifetime of the object
	 */
	public void updateLife() {
		if (fadeValue >= 1.0) {
			lifeTime++;
			if (lifeTime >= 80) {
				lifeTime = 80;
				remainStatic = true;
			}
		}
	}
	public void fadeUpdate(){
		fadeValue += 0.01;
		if (fadeValue >= 1.0) {
			fadeValue = 1.0;
		}
	}

	/**
	 * Method in charge of updating the glow of the object
	 */
	public void updateGlow() {
		if (GameSettings.ADD_GLOW) {
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

	/**
	 * Method in charge of panning the object in and out
	 */
	public void lookAtMe() {
		if (minSize) {
			size += 0.5 * GameSettings.FRAME_SCALE;
			circle.setRadius(size);
			if (size >= targetSize + 10) {
				minSize = false;
				maxSize = true;
			}
		}
		if (maxSize) {
			size -= 0.5 * GameSettings.FRAME_SCALE;
			circle.setRadius(size);
			if (size <= targetSize - 5) {
				maxSize = false;
				minSize = true;
			}
		}
	}

	/**
	 * Method in charge of the object is approximate to another set of
	 * dimensions
	 *
	 * @param tail_X
	 * @param sect_X
	 * @param tail_Y
	 * @param sect_Y
	 * @return
	 */
	public boolean isApproximate(double tail_X, double sect_X, double tail_Y, double sect_Y) {
		double distance = Math.sqrt((tail_X - sect_X) * (tail_X - sect_X) + (tail_Y - sect_Y) * (tail_Y - sect_Y));
		if (distance > 10) {
			return true;
		}
		return false;
	}

	/**
	 * Method which can determine if the this object is approximate to another
	 * given object
	 *
	 * @param object
	 * @return
	 */
	public boolean isApproximateTo(AbstractObject object) {
		double distance = Math
				.sqrt((x - object.getX()) * (x - object.getX()) + (y - object.getY()) * (y - object.getY()));
		if (distance < 15) {
			return true;
		}
		return false;
	}

	public void checkBoundaries() {
		if (x < radius * 3 || x > GameSettings.WIDTH - radius * 3 || y < GameSettings.START_Y + radius
				|| y > GameSettings.HEIGHT - radius * 3) {
			x = (int) (rand.nextDouble() * ((GameSettings.WIDTH - 30 * 4) - (30 * 4) + 1) + 30 * 4);
			y = (int) (rand.nextDouble() * ((GameSettings.HEIGHT - 30 * 4) - (GameSettings.START_Y + radius) + 1)
					+ GameSettings.START_Y + radius);
			this.fadeValue = 0;
		}
	}

	/**
	 * Method in charge of checking events within the collision bounds of this
	 * object. this method will also assign a set of actions to collision
	 * events.
	 */
	public void checkCollision() {
		float newX = (int) (rand.nextDouble() * ((GameSettings.WIDTH - 30 * 4) - (30 * 4) + 1) + 30 * 4);
		float newY = (int) (rand.nextDouble() * ((GameSettings.HEIGHT - 30 * 4) - (GameSettings.START_Y + radius) + 1)
				+ GameSettings.START_Y + radius);
		for (AbstractObject tempObject : game.getObjectManager().getObjectList()) {
			if (tempObject.getId() == GameObjectID.Fruit) {
				if (tempObject.getNumericCode() != this.numericCode) {
					if (getBounds().intersects(tempObject.getBounds())) {
						this.x = newX;
						this.y = newY;
						this.fadeValue = 0;
					}
				}
			}
		}
		for (AbstractTile tempTile : game.getGameLoader().getTileManager().getBlock()) {
			if (tempTile.getId() == GameLevelObjectID.rock) {
				if (getBounds().intersects(tempTile.getBounds())) {
					this.x = newX;
					this.y = newY;
					this.fadeValue = 0;
				}
			}
		}
		for (AbstractTile tempTile : game.getGameLoader().getTileManager().getTile()) {
			if (tempTile.getId() == GameLevelObjectID.cactus) {
				if (getBounds().intersects(tempTile.getBounds())) {
					this.x = newX;
					this.y = newY;
					this.fadeValue = 0;
				}
			}
			if (tempTile.getId() == GameLevelObjectID.treeBark) {
				if (getCollisionBounds().intersects(tempTile.getBounds())) {
					this.x = newX;
					this.y = newY;
					this.fadeValue = 0;
					break;
				}
			}
			if (tempTile.getId() == GameLevelObjectID.flower) {
				if (getCollisionBounds().intersects(tempTile.getBounds())) {
					this.x = newX;
					this.y = newY;
					this.fadeValue = 0;
					break;
				}
			}
			if (tempTile.getId() == GameLevelObjectID.bush) {
				if (getCollisionBounds().intersects(tempTile.getBounds())) {
					this.x = newX;
					this.y = newY;
					this.fadeValue = 0;
					break;
				}
			}
		}
		for (AbstractTile tempTile : game.getGameLoader().getTileManager().getTrap()) {
			if (tempTile.getId() == GameLevelObjectID.fence) {
				if (getCollisionBounds().intersects(tempTile.getBounds())) {
					this.blowUp();
					this.game.getGameLoader().spawnSnakeFood();
					this.remove();
					break;
				}
			}
		}
		for (AbstractSection object : game.getSectManagerOne().getSectionList()) {
			if (getBounds().intersects(object.getBounds())) {
				if (!remainStatic) {
					this.x = newX;
					this.y = newY;
					this.fadeValue = 0;
					lifeTime = 0;
				}
			}
		}
		for (AbstractSection object : game.getSectManagerTwo().getSectionList()) {
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

	/**
	 * Method which returns true if the location of this object is a permitted
	 * location
	 *
	 * @return
	 */
	public boolean allowedLocation() {
		double newX = rand.nextDouble() * ((GameSettings.WIDTH - 30 * 3) - (30 * 3) + 1) + 30 * 3;
		double newY = rand.nextDouble() * ((GameSettings.HEIGHT - 30 * 3) - (30 * 3) + 1) + 30 * 3;
		for (AbstractTile tempTile : game.getGameLoader().getTileManager().getTile()) {
			if (tempTile.getId() == GameLevelObjectID.rock || tempTile.getId() == GameLevelObjectID.cactus) {
				if (!new Rectangle2D(newX, newY, radius, radius).intersects(tempTile.getBounds())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Method which changes the opacity of this object
	 */
	public void fruitFadein() {
		this.circle.setOpacity(fadeValue);
	}

	/**
	 * Method which makes this object blow up into smaller pieces
	 */
	public void blowUp() {
		for (int i = 0; i < GameSettings.PARTICLE_LIMIT; i++) {
			if (GameSettings.ADD_VARIATION) {
				particleSize = (Math.random() * (20 - 5 + 1) + 5)
						/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
				particleLife = (Math.random() * (3.5 - 1.5 + 1) + 1.5)
						/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
			}
			game.getDebrisManager().addDebris(new FruitSplashOne(game, new ImagePattern(GameImageBank.fruitDebrisOne),
					particleLife, particleSize, (double) (x + this.radius / 2), (double) (y + this.radius / 2)));
		}
	}

	/**
	 * Alternate method which makes this object blow up into smaller pieces
	 */
	public void altblowUp() {
		for (int i = 0; i < GameSettings.PARTICLE_LIMIT; i++) {
			if (GameSettings.ADD_VARIATION) {
//				particleSize = (Math.random() * (40 - 10 + 1) + 10)
//						/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
//				particleLife = (Math.random() * (1.5 - 0.5 + 1) + 0.5)
//						/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
//			}
//			game.getDebrisManager().addObject(new FruitSplashTwo(game, new ImagePattern(GameImageBank.glowingCircleOne),
//					particleLife, particleSize, (float) (x + this.radius / 2), (float) (y + this.radius / 2)));
//
				particleSize = Math.random()*(200 - 40 +1)+40;
				particleLife = Math.random()*(0.5 - 0.1+1)+0.1;

				game.getDebrisManager().addParticle(new GlowParticle(game,GameImageBank.glowingCircleOne, particleLife,particleSize,(float) (x+width/2), (float) (y+height/2),  new Point2D((Math.random()*(12 - -12 + 1) + -12), Math.random()*(12 - -12 + 1) + -12)));
			}
		}
	}

	/**
	 * Method which will make this objects bounce from another object
	 *
	 * @param snake
	 * @param x
	 * @param y
	 */
	public void bounce(PlayerOne snake, double x, double y) {
		if (snake.getVelX() > 0) {
			this.velX = (float) (snake.getVelX()) * 8;
			this.velY = RandomGenerator.getRNG(-12, 12);
		}
		if (snake.getVelY() > 0) {
			this.velY = (float) (snake.getVelY()) * 8;
			this.velX = RandomGenerator.getRNG(-12, 12);
		}
	}

	/**
	 * Method which will make this objects bounce from another object
	 *
	 * @param snake
	 * @param x
	 * @param y
	 */
	public void bounce(PlayerTwo snake, double x, double y) {
		if (snake.getVelX() > 0) {
			this.velX = (float) (snake.getVelX()) * 8;
			this.velY = RandomGenerator.getRNG(-12, 12);
		}
		if (snake.getVelY() > 0) {
			this.velY = (float) (snake.getVelY()) * 8;
			this.velX = RandomGenerator.getRNG(-12, 12);
		}
	}

	/**
	 * Method which will make this objects bounce from another object
	 *
	 * @param snake
	 * @param x
	 * @param y
	 */
	public void bounce(AbstractSection snake, double x, double y) {
		if (snake.getVelX() == 0) {
			this.velX = (float) (snake.getVelX() + (Math.random() * (13 - 8 + 1) - 8));
			this.velY = (float) (snake.getVelY() + (Math.random() * (7 - 4 + 1) - 4));
		} else if (snake.getVelY() == 0) {
			this.velX = (float) (snake.getVelX() + (Math.random() * (7 - 4 + 1) - 4));
			this.velY = (float) (snake.getVelY() + (Math.random() * (13 - 8 + 1) - 8));
		}
	}

	/**
	 * Method which returns the collision bounds for this object
	 *
	 * @return
	 */
	public Rectangle2D getBounds() {
		return new Rectangle2D(x - staticRadius / 2, y - staticRadius / 2, staticRadius, staticRadius);
	}

	/**
	 * Method which returns alternate collision bounds for this object
	 *
	 * @return
	 */
	public Rectangle2D getCollisionBounds() {
		return new Rectangle2D(x - staticRadius / 2, y - staticRadius / 2, staticRadius, staticRadius);
	}
}
