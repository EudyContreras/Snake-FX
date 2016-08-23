package com.EudyContreras.Snake.GameObjects;

import java.util.Random;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.HUDElements.GamePoint;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ParticleEffects.FruitSplashOne;
import com.EudyContreras.Snake.ParticleEffects.GlowParticle;
import com.EudyContreras.Snake.PathFindingAI.CellNode;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.EudyContreras.Snake.Utilities.RandomGenUtility;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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
	private double lifeTime = 0;
	private boolean maxSize = false;
	private boolean minSize = true;
	private boolean maxGlow = false;
	private boolean noGlow = true;
	private boolean indicator = false;
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

	public SnakeFood(GameManager game, Pane layer, Circle node, float x, float y, GameObjectID id, CellNode cell,int numericCode) {
		super(game, layer, node, x, y, id);
		this.game = game;
		this.cell = cell;
		this.numericCode = numericCode;
		this.circle.setOpacity(fadeValue);
		this.size = circle.getRadius();
		this.targetSize = size;
		this.bounds = new Circle(x, y, node.getRadius(), Color.TRANSPARENT);
		this.rectBounds = new Rectangle(x,y,size*2,size*2);
		this.rectBounds.setFill(Color.TRANSPARENT);
		this.layer.getChildren().add(bounds);
		this.cell.setContainsTarget(true);
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
			circle.setEffect(borderGlow);
		}
	}

	/**
	 * Updates methods which do not involve movement
	 */
	public void logicUpdate() {
		game.getAIController().getGrid().getRelativeCell();
		fadeUpdate();
		fruitFadein();
		lookAtMe();
		updateGlow();
		updateLife();
		checkBoundaries();
		adjustBounds();
	}

	/**
	 * Method in charge of moving the node.
	 */
	public void move() {
		super.move();
		if (velX != 0) {
			velX *= 0.93;
			game.getAIController().getGrid().getRelativeCell();
		}
		if (velY != 0) {
			velY *= 0.93;
			game.getAIController().getGrid().getRelativeCell();
		}
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
		rectBounds.setX(x - size*2 / 2);
		rectBounds.setY(y - size*2 / 2);
		rectBounds.setWidth(size*2);
		rectBounds.setHeight(size*2);

	}
	/**
	 * Method in charge of updating the glow of the object
	 */
	public void updateGlow() {
		if (GameSettings.ADD_GLOW) {
			if (fadeValue == 1.0) {
				if (noGlow) {
					glowValue += 2*GameSettings.FRAME_SCALE;
					borderGlow.setWidth(glowValue);
					borderGlow.setHeight(glowValue);
					if (glowValue >= 150) {
						noGlow = false;
						maxGlow = true;
					}
				}
				if (maxGlow) {
					glowValue -= 2*GameSettings.FRAME_SCALE;
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
		double distance = Math.sqrt((x - object.getX()) * (x - object.getX()) + (y - object.getY()) * (y - object.getY()));
		if (distance < 15) {
			return true;
		}
		return false;
	}

	public void resetBounds() {
		if (x < radius * 3 || x > GameSettings.WIDTH - radius * 3 || y < GameSettings.MIN_Y + radius || y > GameSettings.HEIGHT - radius * 3) {
			x = (int) (rand.nextDouble() * ((GameSettings.WIDTH - 30 * 4) - (30 * 4) + 1) + 30 * 4);
			y = (int) (rand.nextDouble() * ((GameSettings.HEIGHT - 30 * 4) - (GameSettings.MIN_Y + radius) + 1)
					+ GameSettings.MIN_Y + radius);
			this.fadeValue = 0;
		}
	}

	public void checkBoundaries() {
		if (velX != 0 || velY != 0) {
			if (x < 0 - radius / 2) {
				x = (float) (GameSettings.WIDTH + radius / 2);
			} else if (x > GameSettings.WIDTH + radius / 2) {
				x = (float) (0 - radius / 2);
			} else if (y < GameSettings.MIN_Y - radius / 2) {
				y = (float) (GameSettings.HEIGHT + radius / 2);
			} else if (y > GameSettings.HEIGHT + radius / 2) {
				y = (float) (GameSettings.MIN_Y - radius / 2);
			}
		}else {
			if (x < 0) {
				x = (float) (GameSettings.WIDTH);
			} else if (x > GameSettings.WIDTH) {
				x = (float) 0;
			} else if (y < GameSettings.MIN_Y) {
				y = (float) (GameSettings.HEIGHT);
			} else if (y > GameSettings.HEIGHT) {
				y = (float) (GameSettings.MIN_Y);
			}
		}
	}
	/**
	 * Method in charge of checking events within the collision bounds of this
	 * object. this method will also assign a set of actions to collision
	 * events.
	 */
	public void checkCollision() {
		for (AbstractTile tempTile : game.getGameLoader().getTileManager().getBlock()) {
			if (tempTile.getId() == GameLevelObjectID.ROCK) {
				if (getNormalBounds().intersects(tempTile.getBounds())) {
						bounceBack(tempTile);
				}
				if (getNormalBounds().intersects(tempTile.getBounds2D())) {
					this.blowUp();
					this.remove();
					this.game.getGameLoader().spawnSnakeFood();
					break;
				}
			}
		}
		for (AbstractTile tempTile : game.getGameLoader().getTileManager().getTile()) {
			if (tempTile.getId() == GameLevelObjectID.CACTUS) {
				if (getNormalBounds().intersects(tempTile.getBounds())) {
					this.blowUp();
					this.remove();
					this.game.getGameLoader().spawnSnakeFood();
						break;

				}
			}
			if (tempTile.getId() == GameLevelObjectID.TREE) {
				if (getNormalBounds().intersects(tempTile.getBounds())) {
						bounceBack(tempTile);

				}
			}
			if (tempTile.getId() == GameLevelObjectID.FLOWER) {
				if (getNormalBounds().intersects(tempTile.getBounds())) {
						bounceBack(tempTile);

				}
			}
			if (tempTile.getId() == GameLevelObjectID.BUSH) {
				if (getNormalBounds().intersects(tempTile.getBounds())) {
						bounceBack(tempTile);

				}
			}
			if (tempTile.getId() == GameLevelObjectID.NO_SPAWN_ZONE) {
				if (getNormalBounds().intersects(tempTile.getBounds())) {
						bounceBack(tempTile);

				}
			}
		}
		for (AbstractTile tempTile : game.getGameLoader().getTileManager().getTrap()) {
			if (tempTile.getId() == GameLevelObjectID.FENCE || tempTile.getId() == GameLevelObjectID.TRAP) {
				if (getNormalBounds().intersects(tempTile.getBounds())) {
					this.blowUp();
					this.remove();
					this.game.getGameLoader().spawnSnakeFood();
					break;

			}
			}
		}

	}
	public void checkRemovability(){

	}
	public void relocate(){
		x = (int) (rand.nextDouble() * ((GameSettings.WIDTH - 30 * 4) - (30 * 4) + 1) + 30 * 4);
		y = (int) (rand.nextDouble() * ((GameSettings.HEIGHT - 30 * 4) - (GameSettings.MIN_Y + radius) + 1)
		+ GameSettings.MIN_Y + radius);
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

		FruitSplashOne[] fruitPiecesUnder = new FruitSplashOne[GameSettings.MAX_DEBRIS_AMOUNT/2];
		FruitSplashOne[] fruitPiecesAbove = new FruitSplashOne[GameSettings.MAX_DEBRIS_AMOUNT/2];
		for (int i = 0; i < GameSettings.MAX_DEBRIS_AMOUNT/2; i++) {
			if (GameSettings.ADD_VARIATION) {
				particleSize = (Math.random() * (15 - 5 + 1) + 5);
				particleLife = (Math.random() * (3.5 - 1.5 + 1) + 1.5);
			}
			fruitPiecesUnder[i] = new FruitSplashOne(game, new ImagePattern(GameImageBank.fruitDebrisOne),
					particleLife, particleSize, (double) (x + this.radius / 2), (double) (y + this.radius / 2));
			fruitPiecesAbove[i] = new FruitSplashOne(game, game.getOuterParticleLayer(), new ImagePattern(GameImageBank.fruitDebrisOne),
					particleLife/2, particleSize/2, (double) (x + this.radius / 2), (double) (y + this.radius / 2));
		}
		game.getDebrisManager().addDebris(fruitPiecesUnder);
		game.getDebrisManager().addDebris(fruitPiecesAbove);
		rectBounds.setStroke(Color.TRANSPARENT);
		bounds.setStroke(Color.TRANSPARENT);

	}

	public void getPoint(){
		game.getNinthLayer().getChildren().add(new GamePoint(game.getEighthLayer(),x,y));
		this.rectBounds.setStroke(Color.TRANSPARENT);
		this.bounds.setStroke(Color.TRANSPARENT);
	}

	/**
	 * Alternate method which makes this object blow up into smaller pieces
	 */
	public void blowUpAlt() {
		GlowParticle[] particles = new GlowParticle[15];
		if(indicator==false){
		for (int i = 0; i < 15; i++) {
			if (GameSettings.ADD_VARIATION) {
				particleSize = Math.random()*(200 - 40 +1)+40;
				particleLife = Math.random()*(0.5 - 0.1+1)+0.1;

				particles[i] = new GlowParticle(game,GameImageBank.glowingCircleOne, particleLife,particleSize,(float) (x+width/2), (float) (y+height/2),  new Point2D((Math.random()*(12 - -12 + 1) + -12), Math.random()*(12 - -12 + 1) + -12));
			}
		}
		game.getDebrisManager().addParticle(particles);
		indicator = true;
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

		if (snake.getVelX() > 0 || snake.getVelX() < 0) {
			this.velX = (float) (snake.getVelX()) * 9.5;
			this.velY = RandomGenUtility.getRandomInteger(-12, 12);
		}
		else {
			this.velY = (float) (snake.getVelY()) * 9.5;
			this.velX = RandomGenUtility.getRandomInteger(-12, 12);
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

		if (snake.getVelX() > 0 || snake.getVelX() < 0) {
			this.velX = (float) (snake.getVelX()) * 9.5;
			this.velY = RandomGenUtility.getRandomInteger(-12, 12);
		}
		else {
			this.velY = (float) (snake.getVelY()) * 9.5;
			this.velX = RandomGenUtility.getRandomInteger(-12, 12);
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

		if (snake.getVelX() > 0) {
			this.velX = (float) (snake.getVelX()) * 8;
			this.velY = RandomGenUtility.getRandomInteger(-12, 12);
		}
		if (snake.getVelY() > 0) {
			this.velY = (float) (snake.getVelY()) * 8;
			this.velX = RandomGenUtility.getRandomInteger(-12, 12);
		}
	}
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
	public void bounceBack(AbstractTile tile) {

		velX = -Math.abs(velX) + tile.getVelX()*.7;
		velY = -Math.abs(velY) + tile.getVelY()*.7;
		if(velX==0 && velY==0){
			this.blowUp();
			this.remove();
			this.game.getGameLoader().spawnSnakeFood();
		}
	}
	public void bounceBack(AbstractSection section) {
		if (section.getVelX() > 0 || section.getVelX() < 0) {
			velX = -Math.abs(velX) + section.getVelX()*1.2;
			this.velY = RandomGenUtility.getRandomInteger(-12, 12);
		}
		else{
			velY = -Math.abs(velY) + section.getVelY()*1.2;
			this.velX = RandomGenUtility.getRandomInteger(-12, 12);
		}
	}
	public void removeFromLayer() {
		this.layer.getChildren().remove(this.imageView);
		this.layer.getChildren().remove(this.circle);
		if(GameSettings.DEBUG_MODE){
			this.layer.getChildren().remove(rectBounds);
			this.layer.getChildren().remove(bounds);
		}
	}
	/**
	 * Method which returns the collision bounds for this object
	 *
	 * @return
	 */
	public Rectangle2D getBounds() {
		return new Rectangle2D(x - size*2 / 2, y - size*2 / 2, size*2, size*2);
	}

	/**
	 * Method which returns alternate collision bounds for this object
	 *
	 * @return
	 */
	public Rectangle2D getCollisionBounds() {
		return getBounds();
	}
	public Rectangle2D getNormalBounds(){
		return new Rectangle2D(rectBounds.getX(), rectBounds.getY(), rectBounds.getWidth(), rectBounds.getHeight());
	}
}
