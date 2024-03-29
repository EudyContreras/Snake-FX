package com.EudyContreras.Snake.GameObjects;

import java.util.Random;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ParticleEffects.GlowParticle;
import com.EudyContreras.Snake.Utilities.RandomGenUtility;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class is in charge of managing the logic surrounding the spawning of
 * food or buffs within the level.
 *
 * @author Eudy Contreras
 *
 */
public class ClassicSnakeFood extends AbstractObject {

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
	private Rectangle rectBounds;
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
	public ClassicSnakeFood(GameManager game, Pane layer, Node node, double x, double y, GameObjectID id, int numericCode) {
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

	public ClassicSnakeFood(GameManager game, Pane layer, Circle node, double x, double y, GameObjectID id, int numericCode) {
		super(game, layer, node, x, y, id);
		this.game = game;
		this.numericCode = numericCode;
		this.circle.setOpacity(fadeValue);
		this.size = circle.getRadius();
		this.targetSize = size;
		this.staticRadius = (targetSize *2);
		this.bounds = new Circle(x, y, node.getRadius(), Color.TRANSPARENT);
		this.rectBounds = new Rectangle(x,y, staticRadius+5, staticRadius+5);
		this.rectBounds.setFill(Color.TRANSPARENT);
		this.layer.getChildren().add(bounds);
		this.addGLow();
		if (GameSettings.DEBUG_MODE) {
			this.rectBounds.setStroke(Color.WHITE);
			this.layer.getChildren().add(rectBounds);
			this.bounds.setStroke(Color.WHITE);
			this.bounds.setStrokeWidth(3);
		}
	}

	/**
	 * Adds a glowing aura around the node or image
	 */
	public void addGLow() {
		if (GameSettings.ADD_GLOW) {
			borderGlow.setOffsetY(0f);
			borderGlow.setOffsetX(0f);
			borderGlow.setColor(Color.rgb(240, 0, 0, 1));
			borderGlow.setWidth(60);
			borderGlow.setHeight(50);
			borderGlow.setSpread(0.5);
			borderGlow.setBlurType(BlurType.TWO_PASS_BOX);
			//circle.setEffect(borderGlow);
		}
	}

	/**
	 * Updates methods which do not involve movement
	 */
	public void logicUpdate() {
		fadeUpdate();
		fruitFadein();
		lookAtMe();
//		updateGlow();
		updateLife();
		checkBoundaries();
		adjustBounds();
	}

	/**
	 * Method in charge of moving the node.
	 */
	public void move() {
		super.move();
		if(velX!=0)
		velX *= 0.90;
		if(velY!=0)
		velY *= 0.90;
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

	public void adjustBounds(){
		bounds.setCenterX(x);
		bounds.setCenterY(y);
		bounds.setRadius(size * .8);
		rectBounds.setX(x - staticRadius / 2);
		rectBounds.setY(y - staticRadius / 2);

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

			else if (fadeValue < 1.0) {
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
			size += (0.5 * GameSettings.FRAME_SCALE);
			circle.setRadius(size);
			if (size >= targetSize + 10) {
				minSize = false;
				maxSize = true;
			}
		}
		if (maxSize) {
			size -= (0.5 * GameSettings.FRAME_SCALE);
			circle.setRadius(size);
			if (size <= targetSize - 5) {
				maxSize = false;
				minSize = true;
			}
		}
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

	public void resetBounds() {
		if (x < radius * 3 || x > GameSettings.WIDTH - radius * 3 || y < GameSettings.MIN_Y + radius
				|| y > GameSettings.HEIGHT - radius * 3) {
			x = (int) (rand.nextDouble() * ((GameSettings.WIDTH - 30 * 4) - (30 * 4) + 1) + 30 * 4);
			y = (int) (rand.nextDouble() * ((GameSettings.HEIGHT - 30 * 4) - (GameSettings.MIN_Y + radius) + 1)
					+ GameSettings.MIN_Y + radius);
			this.fadeValue = 0;
		}
	}

	public void checkBoundaries() {
		if (x < 0 + radius) {
			x = (double) (GameSettings.WIDTH - radius);
		} else if (x > GameSettings.WIDTH - radius) {
			x = (double) (0 + radius);
		} else if (y < GameSettings.MIN_Y + radius) {
			y = (double) (GameSettings.HEIGHT - radius);
		} else if (y > GameSettings.HEIGHT - radius) {
			y = (double) (GameSettings.MIN_Y + radius);
		}
	}
	/**
	 * Method in charge of checking events within the collision bounds of this
	 * object. this method will also assign a set of actions to collision
	 * events.
	 */
	public void checkCollision() {
		for (AbstractObject tempObject : game.getGameObjectController().getObsFruitList()) {
			if (tempObject.getId() == GameObjectID.Fruit) {
				if (tempObject.getNumericCode() != this.numericCode) {
					if (getBounds().intersects(tempObject.getBounds())) {
						if (!remainStatic) {
							relocate();
							this.fadeValue = 0;
							this.lifeTime = 0;
							break;
						}
						else{

						}
					}
				}
			}
		}
		for (AbstractTile tempTile : game.getGameLoader().getTileManager().getTile()) {

			if (tempTile.getId() == GameLevelObjectID.NO_SPAWN_ZONE) {
				if (getNormalBounds().intersects(tempTile.getBounds())) {
					if (!remainStatic) {
						relocate();
						this.fadeValue = 0;
						this.lifeTime = 0;
						break;
					}
				}
			}
		}

		for (AbstractSection object : game.getSectManagerThree().getSectionList()) {
			if (getRadialBounds().intersects(object.getRadialBounds())) {
				if (!remainStatic) {
					relocate();
					this.fadeValue = 0;
					this.lifeTime = 0;
					break;
				}
			}
		}
	}

	public void checkRemovability(){

	}

	public void relocate(){
		x = RandomGenUtility.getRandom(0, (GameSettings.WIDTH -90));
		y = RandomGenUtility.getRandom(GameSettings.MIN_Y + 60, (GameSettings.HEIGHT - 90));
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
		GlowParticle[] particle = new GlowParticle[GameSettings.MAX_DEBRIS_AMOUNT];
		for (int i = 0; i < GameSettings.MAX_DEBRIS_AMOUNT; i++) {
			if (GameSettings.ALLOW_VARIATIONS) {
				particleSize = Math.random()*(160- 50 +1)+50;
				particleLife = Math.random()*(0.2 - 0.1+1)+0.1;
				particle[i] = new GlowParticle(game,GameImageBank.glowingCircleTwo, particleLife,particleSize,(double) (x+width/2), (double) (y+height/2),  new Point2D((Math.random()*(12 - -12 + 1) + -12), Math.random()*(12 - -12 + 1) + -12));
			}
		}
		game.getDebrisManager().addParticle(particle);
		this.layer.getChildren().remove(rectBounds);
		this.bounds.setVisible(false);
	}

	/**
	 * Method which will make this objects bounce from another object
	 *
	 * @param snake
	 * @param x
	 * @param y
	 */

	public Bounds getRadialBounds() {
		return bounds.getBoundsInParent();
	}
	/**
	 * Method which will make this objects bounce from another object
	 *
	 * @param snake
	 * @param x
	 * @param y
	 */

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
	public Rectangle2D getNormalBounds(){
		return new Rectangle2D(rectBounds.getX(), rectBounds.getY(), rectBounds.getWidth(), rectBounds.getHeight());
	}
}
