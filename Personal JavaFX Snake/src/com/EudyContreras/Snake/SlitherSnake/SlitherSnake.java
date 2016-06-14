package com.EudyContreras.Snake.SlitherSnake;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractSlither;
import com.EudyContreras.Snake.AbstractModels.AbstractSlitherSection;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.HUDElements.ScoreKeeper;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ParticleEffects.DirtDisplacement;
import com.EudyContreras.Snake.Utilities.AnimationUtility;
import com.EudyContreras.Snake.Utilities.ScreenEffectUtility;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SlitherSnake extends AbstractSlither {

	private int immunity = GameSettings.IMMUNITY_TIME;
	private int dirtDelay = 10;
	private int maxOpenTime = 0;
	private int coolDown = 60;
	private int moveDelay = 0;
	private int appleCount = 0;
	private int counter = 0;
	private double accelaration = 0.5;
	private double normalSpeed = GameSettings.PLAYER_ONE_SPEED;
	private double maxSpeed = GameSettings.PLAYER_ONE_SPEED*2;
	private double minimumSpeed = GameSettings.PLAYER_ONE_SPEED/2;
	private double bodyTrigger;
	private double offsetX = 0;
	private double offsetY = 0;
	private boolean isDead = false;
	private boolean collision = false;
	private boolean notEating = true;
	private boolean allowOpen = true;
	private boolean eatCoolDown = false;
	private boolean setDelay = false;
	private boolean allowDamage = true;
	private boolean allowCollision = true;
	private boolean hasBaseBody = false;
	private boolean allowTurnLeft = true;
	private boolean allowTurnRight = true;
	private boolean allowTurnUp = true;
	private boolean allowTurnDown = true;
	private boolean goSlow = false;
	private boolean thrust = false;
	private double fadeAway = 1.0f;
	private boolean showTheSkull = false;
	private boolean allowMovement = true;
	private boolean rotateLeft = false;
	private boolean rotateRight = false;
	private int snakeLength;
	private double speed;
	private double rotation = 0;
	private double angle;
	private double xChange;
	private double yChange;
	private Circle skull;
	private GameManager game;
	private AnimationUtility anim;
	private Rectangle bounds;
	private ScreenEffectUtility overlay;
	private ImagePattern eatingFrame = new ImagePattern(GameImageBank.snakeOneEating);
	private ImagePattern blinkingFrame = new ImagePattern(GameImageBank.snakeOneBlinking);
	private AbstractSlitherSection thisPart;
	private AbstractSlitherSection partBefore;
	private SlitherSectionManager sectManager;
	private PlayerMovement direction;
	public static int NUMERIC_ID = 0;
	public static double ROTATION_OFFSET = 1;;
	public static Boolean DEAD = false;
	public static Boolean LEVEL_COMPLETED = false;
	public static Boolean STOP_MOVEMENT = false;
	public static Boolean MOUTH_OPEN = false;
	public static Boolean MOUTH_CLOSE = true;
	public static Boolean KEEP_MOVING = true;

	public SlitherSnake(GameManager game, Pane layer, Node node, double x, double y, double r, double velX, double velY,
			double velR, double health, double damage, double speed, GameObjectID id, SlitherManager gom) {
		super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
		this.speed = speed;
		this.game = game;
		this.overlay = game.getOverlayEffect();
		this.anim = new AnimationUtility();
		this.sectManager = game.getSlitherSectManager();
		this.velX = GameSettings.SLITHER_SPEED;
		this.velY = GameSettings.SLITHER_SPEED;
		this.speed = 0.7f;
		this.bodyTrigger = y + 20;
		this.overlay = game.getOverlayEffect();
		this.loadImages();
		this.drawBoundingBox();
		this.spawnBody();
	}

	public void loadImages() {
		anim.addScene(GameImageBank.snakeOneHead, 4000);
		anim.addScene(GameImageBank.snakeOneBlinking, 250);
		setAnimation(anim);
	}

	public void updateUI() {
		super.updateUI();
		updateBounds();
		logicUpdate();
	}

	public void setAnim(ImagePattern scene) {
		this.circle.setFill(scene);
	}

	public void spawnBody() {
		addbaseSections();
		KEEP_MOVING = false;
	}

	public void logicUpdate() {
		rotateLeft();
		rotateRight();
		accelarate();
		controlEating();
		hinderMovement();
		updateImmunity();
		updateSpeedDirt();
		updateDirt();
		speedUp();
		speedDown();
		slowDown();

	}

	public void move() {
		if (allowMovement) {
			if (DEAD == false && LEVEL_COMPLETED == false && KEEP_MOVING && game.getStateID()==GameStateID.GAMEPLAY){
			this.circle.setRadius(GameSettings.SLITHER_SIZE*1.4);
			x = (double) (getX() + Math.sin(Math.toRadians(rotation)) * velX * speed);
			y = (double) (getY() + Math.cos(Math.toRadians(rotation)) * velY * speed);
			r = -rotation;
			if(sectManager.getSectionList().size()>0){
			AbstractSlitherSection snakeHead = sectManager.getSectionList().get(0);
			snakeLength = sectManager.getSectionList().size() - 1;


			snakeHead.setX((double) (snakeHead.getX() + Math.sin(Math.toRadians(rotation)) * velX * speed));
			snakeHead.setY((double) (snakeHead.getY() + Math.cos(Math.toRadians(rotation)) * velY * speed));
			snakeHead.setR(-rotation);
			}
			for (int i = 1; i <= snakeLength; i++) {

				partBefore = sectManager.getSectionList().get(i - 1);
				thisPart = sectManager.getSectionList().get(i);

				xChange = partBefore.getX() - thisPart.getX();
				yChange = partBefore.getY() - thisPart.getY();
				angle = (double) Math.atan2(yChange, xChange);

				thisPart.setX(partBefore.getX() - (double) Math.cos(angle) * 15);
				thisPart.setY(partBefore.getY() - (double) Math.sin(angle) * 15);
				}
			}
		}
	}

	public void controlEating() {
		if (!DEAD) {
			maxOpenTime--;
			coolDown--;
			if (notEating) {
				this.setAnim(anim.getPattern());
			}
			if (isAllowOpen() == false) {
				if (setDelay == true) {
					if (maxOpenTime <= 0) {
						maxOpenTime = 0;
						closeMouth();
						eatCoolDown = true;
						setDelay = false;
					}
				}
			}
			if (eatCoolDown == true) {
				if (coolDown <= 0) {
					coolDown = 0;
					setAllowOpen(true);
					eatCoolDown = false;
				}
			}
		} else {
			this.setAnim(blinkingFrame);
		}
	}

	public void hinderMovement() {
		if (KEEP_MOVING) {
			moveDelay--;
			if (moveDelay <= 0) {
				moveDelay = 0;
				allowCollision = true;
			}
		}
	}

	public void positionBody() {
		if (!hasBaseBody) {
			if (y >= bodyTrigger) {
				spawnBody();
				hasBaseBody = true;
			}
		}
	}

	public void updateBounds() {
		checkBounds();
		if (GameSettings.DEBUG_MODE) {
			bounds.setX(x - radius / 2 + offsetX);
			bounds.setY(y - radius / 2 + offsetY);
		}
	}

	public void updateImmunity() {
		if (allowDamage == false) {
			immunity--;
			if (immunity <= 0) {
				immunity = 0;
				allowDamage = true;
			}
		}
	}

	public void updateDirt() {
		if (GameSettings.ALLOW_DIRT) {
			dirtDelay--;
			if (dirtDelay <= 0) {
				if (KEEP_MOVING && game.getStateID() != GameStateID.GAME_MENU) {
					displaceDirt(x + width / 2, y + height / 2, 18, 18);
					dirtDelay = 10;
				}
			}
		}
	}
	public void updateSpeedDirt() {
		if (thrust) {
			dirtDelay--;
			if (dirtDelay <= 0) {
				if (KEEP_MOVING && game.getStateID() != GameStateID.GAME_MENU) {
					displaceSpeedDirt(x + width / 2, y + height / 2, 18, 18);
					dirtDelay = 10;
				}
			}
		}
	}

	public void updateAnimation(long timePassed) {
		anim.update(timePassed);
	}

	public void openMouth() {
		if (direction != PlayerMovement.STANDING_STILL) {
			notEating = false;
			MOUTH_CLOSE = false;
			MOUTH_OPEN = true;
			setAnim(eatingFrame);
			setAllowOpen(false);
			setDelay = true;
			maxOpenTime = 30;
		}
	}

	public void closeMouth() {
		MOUTH_OPEN = false;
		MOUTH_CLOSE = true;
		notEating = true;
		coolDown = GameSettings.BITE_DELAY;
	}

	public void speedUp(){
		if(thrust){
			GameSettings.PLAYER_ONE_SPEED+=accelaration;
			if(GameSettings.PLAYER_ONE_SPEED>=maxSpeed){
				GameSettings.PLAYER_ONE_SPEED = maxSpeed;
			}
		}
	}
	public void speedDown(){
		if(!thrust){
			GameSettings.PLAYER_ONE_SPEED-=(accelaration/2);
			if(GameSettings.PLAYER_ONE_SPEED<=normalSpeed){
				GameSettings.PLAYER_ONE_SPEED = normalSpeed;
			}
		}
	}
	public void slowDown(){
		if(!thrust && goSlow){
			GameSettings.PLAYER_ONE_SPEED-=accelaration;
			if(GameSettings.PLAYER_ONE_SPEED<= minimumSpeed){
				GameSettings.PLAYER_ONE_SPEED = minimumSpeed;
			}
		}
	}

	public void rotateLeft() {
		if (isRotateLeft()) {
			KEEP_MOVING = true;
			rotation += 5;
		}
	}

	public void rotateRight() {
		if (isRotateRight()) {
			KEEP_MOVING = true;
			rotation -= 5;
		}
	}

	public void accelarate() {
		if (thrust) {
			velX = 9;
			velY = 9;
		} else {
			velY -= 0.2;
			velX -= 0.2;
			if (velX <= GameSettings.SLITHER_SPEED) {
				velX = GameSettings.SLITHER_SPEED;
				velY = GameSettings.SLITHER_SPEED;
			}
		}
	}

	public void setDirection(double direction) {
		allowMovement = true;
		KEEP_MOVING = true;
		this.rotation = direction;
	}

	public void moveUp() {
		if(allowTurnUp){
			KEEP_MOVING = true;
			rotation = 180;
		}
	}

	public void moveDown() {
		if(allowTurnDown){
			KEEP_MOVING = true;
			rotation = 0;
	}
	}

	public void moveRight() {
		if(allowTurnRight) {
			KEEP_MOVING = true;
			rotation = 90;
		}
	}

	public void moveLeft() {
		if (allowTurnLeft){
			KEEP_MOVING = true;
			rotation = -90;
		}
	}

	public void checkCollision() {
		if (!DEAD && !LEVEL_COMPLETED) {
			for (int i = 0; i < game.getGameObjectController().getObjectList().size(); i++) {
				AbstractObject tempObject = game.getGameObjectController().getObjectList().get(i);
				if (tempObject.getId() == GameObjectID.Fruit) {
					if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
						addSection();
						tempObject.blowUp();
						tempObject.remove();
						break;

					}
				}
			}
			for (int i = 0; i < game.getGameLoader().getTileManager().getTile().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getTile().get(i);
				if (tempTile.getId() == GameLevelObjectID.cactus) {
					if (getBounds().intersects(tempTile.getBounds())) {
						if (allowDamage && game.getStateID() != GameStateID.GAME_MENU) {
							if (!GameSettings.ALLOW_DAMAGE_IMMUNITY) {
								setCollision(true);
								if (!DEAD) {
									this.overlay.addDistortion(15, 0.2);
									this.overlay.addToneOverlay(Color.rgb(220, 0, 0), 5, 1.0);
								}
								immunity = GameSettings.IMMUNITY_TIME;
								allowDamage = false;
							}
						}
					}
				}
			}
			for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
				if (tempTile.getId() == GameLevelObjectID.rock) {
					if (getBounds().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION) {
							if (allowCollision) {
								KEEP_MOVING = false;
								allowCollision = false;
							}
						}
					}
				}
			}
			for (int i = 0; i < game.getGameLoader().getTileManager().getTrap().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getTrap().get(i);
				if (tempTile.getId() == GameLevelObjectID.fence) {
					if (getBounds().intersects(tempTile.getBounds())) {
						if (!DEAD) {
							if (!GameSettings.ALLOW_DAMAGE_IMMUNITY)
								die();
						}
					}
				}
				if (tempTile.getId() == GameLevelObjectID.trap) {
					if (getBounds().intersects(tempTile.getBounds())) {
						if (!DEAD) {
							if (!GameSettings.ALLOW_DAMAGE_IMMUNITY)
								die();
						}
					}
				}
			}
		}
	}
	public void addSection() {
		if (GameSettings.SLITHER_SIZE < 30) {
			counter++;
			if (counter >= 15) {
				counter = 0;
				GameSettings.SLITHER_SIZE += 2;
			}
		}
		for (int i = 0; i < GameSettings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new SlitherSection(this, game, layer,
					new Circle(GameSettings.SLITHER_SIZE, new ImagePattern(GameImageBank.snakeOneSkin)), x,y,
					GameObjectID.SnakeSection, NUMERIC_ID));
			NUMERIC_ID++;
			appleCount++;
		}
		game.getScoreBoardOne().increaseScore();
		if (ScoreKeeper.APPLE_COUNT > 4)
			game.getGameLoader().spawnSnakeFood();
	}

	public void addbaseSections() {
		for (int i = 0; i < GameSettings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new SlitherSection(this, game, layer,
					new Circle(GameSettings.SLITHER_SIZE, new ImagePattern(GameImageBank.snakeOneSkin)), x, y-radius*NUMERIC_ID, GameObjectID.SnakeSection,
					NUMERIC_ID));
			NUMERIC_ID += 1;
		}
	}



	public void setRotation(double rotation) {
		KEEP_MOVING = true;
		this.rotation = rotation;
	}

	public double getRotation() {
		return rotation;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public void checkRemovability() {

	}

	public boolean withinBounds() {
		return x > 0 - radius - 1 && x < GameSettings.WIDTH + radius + 1 && y > 0 - radius - 1
				&& y < GameSettings.HEIGHT + radius + 1;
	}
	public void checkBounds() {
		if (x < 0 - radius) {
			x = (float) (GameSettings.WIDTH + radius);
		} else if (x > GameSettings.WIDTH + radius) {
			x = (float) (0 - radius);
		} else if (y < GameSettings.START_Y - radius) {
			y = (float) (GameSettings.HEIGHT + radius);
		} else if (y > GameSettings.HEIGHT + radius) {
			y = (float) (GameSettings.START_Y - radius);
		}
	}

	public void draw(GraphicsContext gc) {
		if (showTheSkull == true) {
			fadeAway -= 0.005;
			if (fadeAway <= 0) {
				fadeAway = 0;
			}
		}
	}

	public void blurOut() {
		this.overlay.addDeathBlur();
	}

	public boolean isApproximate(double tail_X, double sect_X, double tail_Y, double sect_Y) {
		double distance = Math.sqrt((tail_X - sect_X) * (tail_X - sect_X) + (tail_Y - sect_Y) * (tail_Y - sect_Y));
		if (distance > 10) {
			return true;
		}
		return false;
	}

	public void drawBoundingBox() {

		if (GameSettings.DEBUG_MODE) {
			bounds = new Rectangle(x - radius / 2, y - radius / 2, radius, radius);
			bounds.setStroke(Color.WHITE);
			bounds.setStrokeWidth(3);
			bounds.setFill(Color.TRANSPARENT);
			game.getSeventhLayer().getChildren().add(bounds);
		}
	}

	public void displaceDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !DEAD && !LEVEL_COMPLETED) {
			for (int i = 0; i < 15; i++) {
				game.getDebrisManager().addDebris(new DirtDisplacement(game, GameImageBank.dirt, 1, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
	}

	public void displaceSpeedDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !DEAD && !LEVEL_COMPLETED) {
			for (int i = 0; i < GameSettings.DIRT_AMOUNT; i++) {
				game.getDebrisManager().addDebris(new DirtDisplacement(game, GameImageBank.dirt, 1, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (13 - -13 + 1) + -13)));
			}
		}
	}
	public Rectangle2D getBoundsTop() {

		return new Rectangle2D(x + 40, y + 55, width - 60, height * 0.24);
	}

	public Rectangle2D getBoundsLeft() {

		return new Rectangle2D(x + 20, y + 65, 20, height * 0.34);
	}

	public Rectangle2D getBoundsRight() {

		return new Rectangle2D(x + width - 20, y + 65, 20, height * 0.34);
	}

	public Rectangle2D getBounds() {

		return new Rectangle2D(x - radius / 2 + offsetX, y - radius / 2 + offsetY, radius, radius);
	}
	public void die() {
		DEAD = true;
		game.getHealthBarOne().drainAll();
		game.setStateID(GameStateID.GAME_OVER);
		overlay.addToneOverlay(Color.RED, 5, 0.05);
		isDead = true;
	}

	public Image getAnimationImage() {
		return anim.getImage();
	}

	public AnimationUtility getAnimation() {
		return anim;
	}

	public void setAnimation(AnimationUtility anim) {
		this.anim = anim;
	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public void setSpeedThrust(boolean thrust){
		this.thrust = thrust;
	}
	public boolean getSpeedThrust(){
		return thrust;
	}

	public boolean isAllowOpen() {
		return allowOpen;
	}

	public void setAllowOpen(boolean allowOpen) {
		this.allowOpen = allowOpen;
	}

	public double getAppleCount() {
		return appleCount;
	}

	public void addBones() {
		isDead = true;
		skull = new Circle(x, y, this.radius * 0.8, new ImagePattern(GameImageBank.snakeSkull));
		skull.setRotate(r);
		game.getFirstLayer().getChildren().add(skull);

	}

	public boolean isRotateLeft() {
		return rotateLeft;
	}

	public void setRotateLeft(boolean rotateLeft) {
		this.rotateLeft = rotateLeft;
	}

	public boolean isRotateRight() {
		return rotateRight;
	}

	public void setRotateRight(boolean rotateRight) {
		this.rotateRight = rotateRight;
	}

}
