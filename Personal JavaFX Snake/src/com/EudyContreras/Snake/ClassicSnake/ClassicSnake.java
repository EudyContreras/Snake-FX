package com.EudyContreras.Snake.ClassicSnake;

import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Controllers.GameObjectController;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.HUDElements.ScoreKeeper;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.Utilities.AnimationUtility;
import com.EudyContreras.Snake.Utilities.ScreenEffectUtility;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ClassicSnake extends AbstractObject {

	private int turnDelay = GameSettings.TURN_DELAY;
	private int immunity = GameSettings.IMMUNITY_TIME;
	private int maxOpenTime = 0;
	private int coolDown = 60;
	private int moveDelay = 0;
	private int appleCount = 0;
	private int counter = 0;
	private double accelaration = 0.5/GameManager.ScaleX_ScaleY;
	private double maxSize = GameSettings.PLAYER_ONE_SIZE+2;
	private double normalSpeed = GameSettings.CLASSIC_SNAKE_SPEED;
	private double maxSpeed = GameSettings.CLASSIC_SNAKE_SPEED*2.5;
	private double minimumSpeed = GameSettings.CLASSIC_SNAKE_SPEED/2;
	private double bodyTrigger;
	private double offsetX = 0;
	private double offsetY = 0;
	private boolean isDead = false;
	private boolean collision = false;
	private boolean allowOpen = true;
	private boolean eatCoolDown = false;
	private boolean setDelay = false;
	private boolean allowDamage = true;
	private boolean allowScreenShake = true;
	private boolean allowCollision = true;
	private boolean hasBaseBody = false;
	private boolean goSlow = false;
	private boolean thrust = false;
	private boolean allowThrust = true;
	private GameManager game;
	private AnimationUtility anim;
	private Rectangle bounds;
	private ScreenEffectUtility overlay;
	private ClassicSnakeSection neighbor;
	private ClassicSnakeSectionManager sectManager;
	private BoxBlur motionBlur;
	private LinkedList<PlayerMovement> turns = new LinkedList<>();
	private PlayerMovement direction;
	public static int NUMERIC_ID = 0;
	public static boolean DEAD = false;
	public static double SPEED = 9;
	public static Boolean LEVEL_COMPLETED = false;
	public static Boolean STOP_MOVING = false;
	public static Boolean MOUTH_OPEN = false;
	public static Boolean MOUTH_CLOSE = true;
	public static Boolean KEEP_MOVING = true;
	public static Boolean ALLOW_FADE = false;

	public ClassicSnake(GameManager game, Pane layer, Node node, double x, double y, double r, double velX, double velY,
			double velR, double health, double damage, double speed, GameObjectID id, GameObjectController gom) {
		super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
		this.game = game;
		this.anim = new AnimationUtility();
		this.overlay = game.getOverlayEffect();
		this.sectManager = game.getSectManagerThree();
		this.loadImages();
		this.loadMouth();
		this.drawBoundingBox();
		this.moveDown();
	}
	public void loadMouth(){
		this.game.getClassicSnakeManager().addObject(new ClassicSnakeFangs(this, game));
	}
	public void loadImages() {
		anim.addScene(GameImageBank.classicSnakeHead, 4000);
		anim.addScene(GameImageBank.classicSnakeHead, 250);

		setAnimation(anim);

	}

	public void spawnBody() {
		addbaseSections();
		KEEP_MOVING = false;
		this.direction = PlayerMovement.STANDING_STILL;
	}

	public void updateUI() {
		super.updateUI();
		updateBounds();
	}

	public void move() {
		if (DEAD == false && LEVEL_COMPLETED == false && KEEP_MOVING)
			super.move();
		this.circle.setRadius(GameSettings.PLAYER_ONE_SIZE);
	}

	public void logicUpdate() {
		controlEating();
		hinderMovement();
		positionBody();
		updateTurns();
		updateImmunity();
		fadeOut();
		speedUp();
		speedDown();

	}

	public void controlEating() {
		if (!DEAD) {
			maxOpenTime--;
			coolDown--;
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
		}
	}

	public void hinderMovement() {
		if (KEEP_MOVING) {
			allowScreenShake = true;
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
		if (neighbor != null) {
			headAdjustment();
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

	public void updateTurns() {
		if (turns.size() > 0) {
			turnDelay--;
			if (turnDelay <= 0) {
				makeTurn();
			}
		}
	}

	public void updateAnimation(long timePassed) {
		anim.update(timePassed);
	}

	public void openMouth() {
		if (direction != PlayerMovement.STANDING_STILL) {
			MOUTH_CLOSE = false;
			MOUTH_OPEN = true;
			setAllowOpen(false);
			setDelay = true;
			maxOpenTime = 30;
		}
	}

	public void closeMouth() {
		MOUTH_OPEN = false;
		MOUTH_CLOSE = true;
		coolDown = GameSettings.BITE_DELAY;
	}

	public void speedUp(){
		if(thrust){
			SPEED+=accelaration;
			if(SPEED>=maxSpeed){
				SPEED = maxSpeed;
			}
		}
	}
	public void speedDown(){
		if(!thrust){
			SPEED-=(accelaration/2);
			if(SPEED<=normalSpeed){
				SPEED = normalSpeed;
			}
		}
	}
	public void slowDown(){
		if(!thrust && goSlow){
			SPEED-=accelaration;
			if(SPEED<= minimumSpeed){
				SPEED = minimumSpeed;
			}
		}
	}
	public void addMotionBlur(){
		this.layer.setEffect(motionBlur);
	}
	public void removeMotionBlur(){
		this.layer.setEffect(null);
	}
	public void setDirection(PlayerMovement direction) {
		if (game.getStateID()== GameStateID.GAMEPLAY) {
			if (!GameSettings.ALLOW_SELF_COLLISION) {
				setDirectCoordinates(direction);
			}
			if (GameSettings.ALLOW_SELF_COLLISION) {
				if (!LEVEL_COMPLETED && !DEAD) {
					if (this.direction == direction) {
						this.direction = direction;
					} else {
						switch (direction) {
						case MOVE_UP:
							if (this.direction != PlayerMovement.MOVE_DOWN) {
								if (!GameSettings.ALLOW_FAST_TURNS) {
										turnDelay(PlayerMovement.MOVE_UP);
								} else {
										moveUp();
								}
							}
							break;
						case MOVE_DOWN:
							if (this.direction != PlayerMovement.MOVE_UP) {
								if (!GameSettings.ALLOW_FAST_TURNS) {
										turnDelay(PlayerMovement.MOVE_DOWN);
								} else {
										moveDown();
								}
							}
							break;
						case MOVE_LEFT:
							if (this.direction != PlayerMovement.MOVE_RIGHT) {
								if (!GameSettings.ALLOW_FAST_TURNS) {
										turnDelay(PlayerMovement.MOVE_LEFT);
								} else {
										moveLeft();
								}
							}
							break;
						case MOVE_RIGHT:
							if (this.direction != PlayerMovement.MOVE_LEFT) {
								if (!GameSettings.ALLOW_FAST_TURNS) {
										turnDelay(PlayerMovement.MOVE_RIGHT);
								} else {
										moveRight();
								}
							}
							break;
						case STANDING_STILL:
							break;
						}
					}
				}
			}
		}
	}

	public void setDirectCoordinates(PlayerMovement direction) {
		if (!GameSettings.ALLOW_SELF_COLLISION) {
			if (!LEVEL_COMPLETED && !DEAD) {
				if (this.direction == direction) {
					this.direction = direction;
				} else {
					switch (direction) {
					case MOVE_UP:
						if (!GameSettings.ALLOW_FAST_TURNS) {

								turnDelay(PlayerMovement.MOVE_UP);
						} else {

								moveUp();
						}
						break;
					case MOVE_DOWN:
						if (!GameSettings.ALLOW_FAST_TURNS) {

								turnDelay(PlayerMovement.MOVE_DOWN);
						} else {

								moveDown();
						}
						break;
					case MOVE_LEFT:
						if (!GameSettings.ALLOW_FAST_TURNS) {

								turnDelay(PlayerMovement.MOVE_LEFT);
						} else {

								moveLeft();
						}
						break;
					case MOVE_RIGHT:
						if (!GameSettings.ALLOW_FAST_TURNS) {

								turnDelay(PlayerMovement.MOVE_RIGHT);
						} else {

								moveRight();
						}
						break;
					case STANDING_STILL:
						break;
					}
				}
			}
		}
	}

	public void turnDelay(PlayerMovement newDirection) {
		turns.add(newDirection);
	}

	public void makeTurn() {
		if (turns.get(0) == PlayerMovement.MOVE_UP) {
			moveUp();
			this.direction = PlayerMovement.MOVE_UP;
		} else if (turns.get(0) == PlayerMovement.MOVE_DOWN) {
			moveDown();
			this.direction = PlayerMovement.MOVE_DOWN;
		} else if (turns.get(0) == PlayerMovement.MOVE_LEFT) {
			moveLeft();
			this.direction = PlayerMovement.MOVE_LEFT;
		} else if (turns.get(0) == PlayerMovement.MOVE_RIGHT) {
			moveRight();
			this.direction = PlayerMovement.MOVE_RIGHT;
		}
	}

	private void moveUp() {
		offsetX = 0;
		offsetY = -20;
		velY = -GameSettings.SNAKE_ONE_SPEED;
		velX = 0;
		r = 180;
		if (!GameSettings.ALLOW_FAST_TURNS){
			if(turns.size()>0)
				turns.remove(0);
		}
		turnDelay = GameSettings.TURN_DELAY;
		if (KEEP_MOVING == false) {
			moveDelay = GameSettings.COLLISION_DELAY;
			KEEP_MOVING = true;
		}
		setCurrentDirection(PlayerMovement.MOVE_UP);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, 0);
		}
	}

	private void moveDown() {
		offsetX = 0;
		offsetY = 20;
		velY = GameSettings.SNAKE_ONE_SPEED;
		velX = 0;
		r = 0;
		if (!GameSettings.ALLOW_FAST_TURNS){
			if(turns.size()>0)
				turns.remove(0);
		}
		turnDelay = GameSettings.TURN_DELAY;
		if (KEEP_MOVING == false) {
			moveDelay = GameSettings.COLLISION_DELAY;
			KEEP_MOVING = true;
		}
		setCurrentDirection(PlayerMovement.MOVE_DOWN);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, 0);
		}
	}

	private void moveRight() {
		offsetX = 20;
		offsetY = 0;
		velX = GameSettings.SNAKE_ONE_SPEED;
		velY = 0;
		r = -90;
		if (!GameSettings.ALLOW_FAST_TURNS){
			if(turns.size()>0)
				turns.remove(0);
		}
		turnDelay = GameSettings.TURN_DELAY;
		if (KEEP_MOVING == false) {
			moveDelay = GameSettings.COLLISION_DELAY;
			KEEP_MOVING = true;
		}
		setCurrentDirection(PlayerMovement.MOVE_RIGHT);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, 0);
		}
	}

	private void moveLeft() {
		offsetX = -20;
		offsetY = 0;
		velX = -GameSettings.SNAKE_ONE_SPEED;
		velY = 0;
		r = 90;
		if (!GameSettings.ALLOW_FAST_TURNS){
			if(turns.size()>0)
				turns.remove(0);
		}
		turnDelay = GameSettings.TURN_DELAY;
		if (KEEP_MOVING == false) {
			moveDelay = GameSettings.COLLISION_DELAY;
			KEEP_MOVING = true;
		}
		setCurrentDirection(PlayerMovement.MOVE_LEFT);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_LEFT, 0);
		}
	}

	public void setCurrentDirection(PlayerMovement direction) {
		this.direction = direction;
	}

	public PlayerMovement getCurrentDirection() {
		return direction;
	}

	public void checkCollision() {

	}

	public void addbaseSections() {
		for (int i = 0; i < 2 + 1; i++) {
			sectManager.addSection(new ClassicSnakeSection(this, game, layer,
					new Circle(GameSettings.PLAYER_ONE_SIZE, new ImagePattern(GameImageBank.classicSnakeBody)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			NUMERIC_ID++;
		}
	}

	public void addSection() {
		if (GameSettings.PLAYER_ONE_SIZE < maxSize) {
			counter++;
			if (counter >= 15) {
				counter = 0;
				if(GameSettings.ALLOW_SNAKE_GROWTH)
					GameSettings.PLAYER_ONE_SIZE += 2;
			}
		}
		for (int i = 0; i < GameSettings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new ClassicSnakeSection(this, game, layer,
					new Circle(GameSettings.PLAYER_ONE_SIZE, new ImagePattern(GameImageBank.classicSnakeBody)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			NUMERIC_ID++;
			appleCount++;
		}
		game.getScoreBoardOne().increaseScore();
		if (ScoreKeeper.APPLE_COUNT > 4)
			game.getGameLoader().spawnClassicSnakeFood();
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


	public void headAdjustment() {
		if (this.direction == PlayerMovement.MOVE_DOWN) {
			if (y - neighbor.getY() >= this.radius) {
				y = (float) (neighbor.getY() + this.radius);
				x = neighbor.getX();
			}
		}
		if (this.direction == PlayerMovement.MOVE_UP) {
			if (neighbor.getY() - y >= this.radius) {
				y = (float) (neighbor.getY() - this.radius);
				x = neighbor.getX();
			}
		}
		if (this.direction == PlayerMovement.MOVE_LEFT) {
			if (neighbor.getX() - x >= this.radius) {
				x = (float) (neighbor.getX() - this.radius);
				y = neighbor.getY();
			}
		}
		if (this.direction == PlayerMovement.MOVE_RIGHT) {
			if (x - neighbor.getX() >= this.radius) {
				x = (float) (neighbor.getX() + this.radius);
				y = neighbor.getY();
			}
		}
	}

	public void setNeighbor(ClassicSnakeSection snakeSection) {
		this.neighbor = snakeSection;
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
	public void die() {
		DEAD = true;
		game.getHealthBarOne().drainAll();
		game.setStateID(GameStateID.GAME_OVER);
		overlay.addToneOverlay(Color.RED, 5, 0.05);
		isDead = true;
	}

	public void fadeOut() {
		if (ALLOW_FADE) {
		}
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

	public void draw(GraphicsContext gc) {

	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public void setSpeedThrust(boolean thrust){
		if (allowThrust) {
			if (thrust == true) {
				this.thrust = thrust;
				this.game.getEnergyBarOne().setSpeedThrust(true);
			}
		}
		if (thrust == false) {
			this.thrust = thrust;
			this.game.getEnergyBarOne().setDelay();
			this.game.getEnergyBarOne().setSpeedThrust(false);
		}

	}
	public void setThrustState(boolean state){
		this.thrust = state;
	}
	public boolean getSpeedThrust(){
		return thrust;
	}
	public void blurOut() {
		this.overlay.addDeathBlur();
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public void checkRemovability() {

	}

	public boolean isNotMovingFast() {
		return false;
	}

	public void setAllowThrust(boolean state) {
		this.allowThrust = state;
	}

	public boolean isAllowThrust(){
		return allowThrust;
	}

	public boolean isAllowCollision() {
		return allowCollision;
	}

	public void setAllowCollision(boolean allowCollision) {
		this.allowCollision = allowCollision;
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

	public boolean isAllowOpen() {
		return allowOpen;
	}

	public void setAllowOpen(boolean allowOpen) {
		this.allowOpen = allowOpen;
	}

	public boolean isAllowScreenShake() {
		return allowScreenShake;
	}

	public void setAllowScreenShake(boolean allowScreenShake) {
		this.allowScreenShake = allowScreenShake;
	}

	public double getAppleCount() {
		return appleCount;
	}

}