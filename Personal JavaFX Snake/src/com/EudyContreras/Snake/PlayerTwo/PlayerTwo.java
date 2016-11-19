package com.EudyContreras.Snake.PlayerTwo;

import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Controllers.GameObjectController;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.HUDElements.ScoreKeeper;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ParticleEffects.DirtDisplacement;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
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

public class PlayerTwo extends AbstractObject {

	public static boolean AI_CONTROLLED = true;
	private int turnDelay = GameSettings.TURN_DELAY;
	private int immunity = GameSettings.IMMUNITY_TIME;
	private int dirtDelay = 10;
	private int maxOpenTime = 0;
	private int coolDown = 60;
	private int moveDelay = 0;
	private int appleCount = 0;
	private int counter = 0;
	private double accelaration = 0.5;
	private double maxSize = GameSettings.ALLOW_AI_CONTROLL ? 26 : 30;
	private double normalSpeed = GameSettings.PLAYER_TWO_SPEED;
	private double maxSpeed = GameSettings.PLAYER_TWO_SPEED*2.5;
	private double minimumSpeed = GameSettings.PLAYER_TWO_SPEED/8;
	private double bodyTrigger;
	private double offset = 30;
	private double offsetX = 0;
	private double offsetY = 0;
	private boolean isDead = false;
	private boolean collision = false;
	private boolean notEating = true;
	private boolean allowOpen = true;
	private boolean eatCoolDown = false;
	private boolean setDelay = false;
	private boolean allowDamage = true;
	private boolean gameOverOverride = false;
	private boolean allowScreenShake = true;
	private boolean allowCollision = true;
	private boolean hasBaseBody = false;
	private boolean allowTurnLeft = true;
	private boolean allowTurnRight = true;
	private boolean allowTurnUp = true;
	private boolean allowTurnDown = true;
	private boolean goSlow = false;
	private boolean thrust = false;
	private boolean allowThrust = true;
	private Rectangle boundBox;
	private GameManager game;
	private AnimationUtility anim;
	private Rectangle bounds;
	private ScreenEffectUtility overlay;
	private PlayerTwoHead snakeHead;
	private PlayerTwoSection neighbor;
	private PlayerTwoSectionManager sectManager;
	private BoxBlur motionBlur;
	private ImagePattern eatingFrame;
	private ImagePattern blinkingFrame;
	private LinkedList<PlayerMovement> turns = new LinkedList<>();
	private PlayerMovement direction;
	public static int NUMERIC_ID = 0;
	public static Double SPEED = 6.0;
	public static Boolean DEAD = false;
	public static Boolean LEVEL_COMPLETED = false;
	public static Boolean STOP_MOVING = false;
	public static Boolean MOUTH_OPEN = false;
	public static Boolean MOUTH_CLOSE = true;
	public static Boolean KEEP_MOVING = true;
	public static Boolean ALLOW_FADE = false;

	public PlayerTwo(GameManager game, Pane layer, Node node, double x, double y, double r, double velX, double velY,
			double velR, double health, double damage, double speed, GameObjectID id, GameObjectController gom) {
		super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
		this.game = game;
		this.anim = new AnimationUtility();
		this.circle.setVisible(false);
		this.overlay = game.getOverlayEffect();
		this.eatingFrame = new ImagePattern(GameImageBank.snakeTwoEating);
		this.blinkingFrame = new ImagePattern(GameImageBank.snakeTwoBlinking);
		this.sectManager = game.getSectManagerTwo();
		this.loadHead();
		this.loadImages();
		this.drawBoundingBox();
		this.moveDown();
	}

	private void loadHead(){
		this.snakeHead = new PlayerTwoHead(this, game, layer,
				new Circle(GameSettings.PLAYER_TWO_SIZE * 1.5, new ImagePattern(GameImageBank.snakeTwoHead)), x, y,
				GameObjectID.SnakeMouth, PlayerMovement.MOVE_DOWN);
		this.game.getPlayerTwoManager().addObject(snakeHead);
	}

	public void loadImages() {
		anim.addScene(GameImageBank.snakeTwoHead, 4000);
		anim.addScene(GameImageBank.snakeTwoBlinking, 250);
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
		this.circle.setRadius(GameSettings.PLAYER_TWO_SIZE);

	}

	public void logicUpdate() {
		controlEating();
		hinderMovement();
		positionBody();
		updateTurns();
		updateImmunity();
		updateSpeedDirt();
		checkTurns();
		speedUp();
		speedDown();
		relax();


	}
	private void relax(){
		if(PlayerOne.DEAD){
			thrust = false;
			goSlow = true;
			slowDown();
		}
	}

	public void controlEating() {
		if (!DEAD) {
			maxOpenTime--;
			coolDown--;
			if (notEating) {
				this.snakeHead.setAnim(anim.getPattern());
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
			this.snakeHead.setAnim(blinkingFrame);
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
			bounds.setWidth(radius);
			bounds.setHeight(radius);
			boundBox.setX(x - (GameSettings.PATH_FINDING_CELL_SIZE-8)/2);
			boundBox.setY(y - (GameSettings.PATH_FINDING_CELL_SIZE-8)/2);
			boundBox.setWidth(GameSettings.PATH_FINDING_CELL_SIZE-8);
			boundBox.setHeight(GameSettings.PATH_FINDING_CELL_SIZE-8);
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

	public void updateSpeedDirt() {
		if (thrust && GameSettings.ALLOW_DIRT) {
			dirtDelay--;
			if (dirtDelay <= 0) {
				if (KEEP_MOVING && game.getStateID()== GameStateID.GAMEPLAY) {
					displaceSpeedDirt(x + width / 2, y + height / 2, 18, 18);
					dirtDelay = 10;
				}
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
			notEating = false;
			MOUTH_CLOSE = false;
			MOUTH_OPEN = true;
			snakeHead.setAnim(eatingFrame);
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
			SPEED+=accelaration;
			if(SPEED>=maxSpeed){
				SPEED = maxSpeed;
			}
		}
	}

	public void speedDown(){
		if(!thrust && !goSlow){
			SPEED-=(accelaration*.4);
			if(SPEED<=normalSpeed){
				SPEED = normalSpeed;
			}
		}
	}

	public void slowDown(){
		if(!thrust && goSlow){
			SPEED-=(accelaration*.1);
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
		if (game.getStateID() == GameStateID.GAMEPLAY) {
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
									if (allowTurnUp)
										turnDelay(PlayerMovement.MOVE_UP);
								} else {
									if (allowTurnUp)
										moveUp();
								}
							}
							break;
						case MOVE_DOWN:
							if (this.direction != PlayerMovement.MOVE_UP) {
								if (!GameSettings.ALLOW_FAST_TURNS) {
									if (allowTurnDown)
										turnDelay(PlayerMovement.MOVE_DOWN);
								} else {
									if (allowTurnDown)
										moveDown();
								}
							}
							break;
						case MOVE_LEFT:
							if (this.direction != PlayerMovement.MOVE_RIGHT) {
								if (!GameSettings.ALLOW_FAST_TURNS) {
									if (allowTurnLeft)
										turnDelay(PlayerMovement.MOVE_LEFT);
								} else {
									if (allowTurnLeft)
										moveLeft();
								}
							}
							break;
						case MOVE_RIGHT:
							if (this.direction != PlayerMovement.MOVE_LEFT) {
								if (!GameSettings.ALLOW_FAST_TURNS) {
									if (allowTurnRight)
										turnDelay(PlayerMovement.MOVE_RIGHT);
								} else {
									if (allowTurnRight)
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
		if (!LEVEL_COMPLETED && !DEAD) {
			switch (direction) {
			case MOVE_UP:
				if (this.direction != PlayerMovement.MOVE_DOWN) {
					moveUp();
				}
				break;
			case MOVE_DOWN:
				if (this.direction != PlayerMovement.MOVE_UP) {
					moveDown();
				}
				break;
			case MOVE_LEFT:
				if (this.direction != PlayerMovement.MOVE_RIGHT) {
					moveLeft();
				}
				break;
			case MOVE_RIGHT:
				if (this.direction != PlayerMovement.MOVE_LEFT) {
					moveRight();
				}
				break;
			case STANDING_STILL:
				break;
			}
		}
	}

	public void setGestureDirection(PlayerMovement direction) {
		if (game.getStateID() == GameStateID.GAMEPLAY) {
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
								if (allowTurnUp)
									moveUp();
							}
							break;
						case MOVE_DOWN:
							if (this.direction != PlayerMovement.MOVE_UP) {
								if (allowTurnDown)
									moveDown();
							}
							break;
						case MOVE_LEFT:
							if (this.direction != PlayerMovement.MOVE_RIGHT) {
								if (allowTurnLeft)
									moveLeft();
							}
							break;
						case MOVE_RIGHT:
							if (this.direction != PlayerMovement.MOVE_LEFT) {
								if (allowTurnRight)
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
	}

	public void turnDelay(PlayerMovement newDirection) {
		turns.add(newDirection);
	}

	public void makeTurn() {
		if (withinBounds()) {
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
	}

	private void moveUp() {
		r = 180;
//		snakeHead.setR(r);
//		if(!GameSettings.ALLOW_AI_CONTROLL)
		snakeHead.performRotation(getCurrentDirection(), PlayerMovement.MOVE_UP);
		setCurrentDirection(PlayerMovement.MOVE_UP);
		sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, 0);
		offsetX = 0;
		offsetY = -offset;
		velY = -GameSettings.SNAKE_TWO_SPEED;
		velX = 0;
		if (!GameSettings.ALLOW_FAST_TURNS){
			if(turns.size()>0)
				turns.remove(0);
		}
		turnDelay = GameSettings.TURN_DELAY+2;
		if (KEEP_MOVING == false) {
			moveDelay = GameSettings.COLLISION_DELAY;
			KEEP_MOVING = true;
		}
	}

	private void moveDown() {
		r = 0;
//		snakeHead.setR(r);
//		if(!GameSettings.ALLOW_AI_CONTROLL)
		snakeHead.performRotation(getCurrentDirection(), PlayerMovement.MOVE_DOWN);
		setCurrentDirection(PlayerMovement.MOVE_DOWN);
		sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, 0);
		offsetX = 0;
		offsetY = offset;
		velY = GameSettings.SNAKE_TWO_SPEED;
		velX = 0;
		if (!GameSettings.ALLOW_FAST_TURNS){
			if(turns.size()>0)
				turns.remove(0);
		}
		turnDelay = GameSettings.TURN_DELAY+2;
		if (KEEP_MOVING == false) {
			moveDelay = GameSettings.COLLISION_DELAY;
			KEEP_MOVING = true;
		}
	}

	private void moveRight() {
		r = -90;
//		snakeHead.setR(r);
//		if(!GameSettings.ALLOW_AI_CONTROLL)
		snakeHead.performRotation(getCurrentDirection(), PlayerMovement.MOVE_RIGHT);
		setCurrentDirection(PlayerMovement.MOVE_RIGHT);
		sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, 0);
		offsetX = offset;
		offsetY = 0;
		velX = GameSettings.SNAKE_TWO_SPEED;
		velY = 0;
		if (!GameSettings.ALLOW_FAST_TURNS){
			if(turns.size()>0)
				turns.remove(0);
		}
		turnDelay = GameSettings.TURN_DELAY+2;
		if (KEEP_MOVING == false) {
			moveDelay = GameSettings.COLLISION_DELAY;
			KEEP_MOVING = true;
		}
	}

	private void moveLeft() {
		r = 90;
//		snakeHead.setR(r);
//		if(!GameSettings.ALLOW_AI_CONTROLL)
		snakeHead.performRotation(getCurrentDirection(), PlayerMovement.MOVE_LEFT);
		setCurrentDirection(PlayerMovement.MOVE_LEFT);
		sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_LEFT, 0);
		offsetX = -offset;
		offsetY = 0;
		velX = -GameSettings.SNAKE_TWO_SPEED;
		velY = 0;
		if (!GameSettings.ALLOW_FAST_TURNS){
			if(turns.size()>0)
				turns.remove(0);
		}
		turnDelay = GameSettings.TURN_DELAY+2;
		if (KEEP_MOVING == false) {
			moveDelay = GameSettings.COLLISION_DELAY;
			KEEP_MOVING = true;
		}
	}

	public void setCurrentDirection(PlayerMovement direction) {
		this.direction = direction;
	}

	public PlayerMovement getCurrentDirection() {
		return direction;
	}

	public void checkCollision() {
		if (!DEAD && !LEVEL_COMPLETED) {
			for (int i = 0; i < game.getGameLoader().getTileManager().getTile().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getTile().get(i);
				if (tempTile.getId() == GameLevelObjectID.CACTUS) {
					if (snakeHead.getBounds().intersects(tempTile.getBounds())) {
						if (allowDamage  && game.getStateID()== GameStateID.GAMEPLAY) {
							if (!GameSettings.ALLOW_DAMAGE_IMMUNITY) {
								setCollision(true);
								if (!DEAD) {
									this.overlay.addDistortion(15, 0.2);
									this.overlay.addToneOverlay(Color.rgb(220, 0, 0), 5, 1.0);
								}
								immunity = GameSettings.IMMUNITY_TIME;
								allowDamage = false;
								break;
							}
						}
					}
				}
			}
			for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
				if (tempTile.getId() == GameLevelObjectID.ROCK) {
					if (getAIBounds().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION && !GameSettings.ALLOW_AI_CONTROLL) {
							if (allowCollision) {
								this.allowThrust = false;
								this.thrust = false;
								this.game.getEnergyBarTwo().setDelay();
								this.game.getEnergyBarTwo().setSpeedThrust(false);
								KEEP_MOVING = false;
								if (allowScreenShake) {
									overlay.addScreenShake(game.getGameRoot(),0.4, true, true);
									overlay.addNodeShake(layer, 0.35);
									allowScreenShake = false;
								}
								allowCollision = false;
								break;
							}
						}
					}
				}
			}
			for (int i = 0; i < game.getGameLoader().getTileManager().getTrap().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getTrap().get(i);
				if (tempTile.getId() == GameLevelObjectID.FENCE) {
					if (snakeHead.getBounds().intersects(tempTile.getBounds())) {
						if (!DEAD) {
							if (!GameSettings.ALLOW_DAMAGE_IMMUNITY){
								if (allowScreenShake) {
									overlay.addScreenShake(game.getGameRoot(),1.2, true, true);
									allowScreenShake = false;
								}
								die();
								break;
							}
						}
					}
				}
				if (tempTile.getId() == GameLevelObjectID.TRAP) {
					if (snakeHead.getBounds().intersects(tempTile.getBounds())) {
						if (!DEAD) {
							if (!GameSettings.ALLOW_DAMAGE_IMMUNITY){
								if (allowScreenShake) {
									overlay.addScreenShake(game.getGameRoot(),1.2, true, true);
									allowScreenShake = false;
								}
								die();
								break;
							}
						}
					}
				}
			}
		}
	}

	public void addbaseSections() {
		for (int i = 0; i < 4 + 1; i++) {
			sectManager.addSection(new PlayerTwoSection(this, game, layer,
					new Circle(GameSettings.PLAYER_TWO_SIZE, new ImagePattern(GameImageBank.snakeTwoSkin)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			NUMERIC_ID++;
		}
	}

	public void addSection() {
		if (GameSettings.PLAYER_TWO_SIZE < maxSize) {
			counter++;
			if (counter >= 10) {
				counter = 0;
				if(GameSettings.ALLOW_SNAKE_GROWTH)
					GameSettings.PLAYER_TWO_SIZE += 2;
			}
		}
		for (int i = 0; i < GameSettings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new PlayerTwoSection(this, game, layer,
					new Circle(GameSettings.PLAYER_TWO_SIZE, new ImagePattern(GameImageBank.snakeTwoSkin)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			NUMERIC_ID++;

			appleCount++;
		}
		game.getScoreBoardTwo().increaseScore();
		if (ScoreKeeper.APPLE_COUNT > 4){
			game.getGameLoader().spawnSnakeFood();
		}
	}

	public boolean withinBounds() {
		return x > 0 - radius - +1 && x < GameSettings.WIDTH + radius -1 && y > 0 - radius +1
				&& y < GameSettings.HEIGHT + radius - 1;
	}

	public void checkBounds() {
		if (x < 0 - radius) {
			x = (float) (GameSettings.WIDTH + radius)+4;
		} else if (x > GameSettings.WIDTH + radius) {
			x = (float) (0 - radius)-4;
		} else if (y < GameSettings.MIN_Y - radius) {
			y = (float) (GameSettings.HEIGHT + radius)+4;
		} else if (y > GameSettings.HEIGHT + radius) {
			y = (float) (GameSettings.MIN_Y - radius)-4;
		}
	}

	public void checkTurns() {
		if (snakeHead.allowLeftTurn()) {
			this.allowTurnLeft = true;
		} else {
			this.allowTurnLeft = false;
		}
		if (snakeHead.allowRightTurn()) {
			this.allowTurnRight = true;
		} else {
			this.allowTurnRight = false;
		}
		if (snakeHead.allowUpTurn()) {
			this.allowTurnUp = true;
		} else {
			this.allowTurnUp = false;
		}
		if (snakeHead.allowDownTurn()) {
			this.allowTurnDown = true;
		} else {
			this.allowTurnDown = false;
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

	public void setNeighbor(PlayerTwoSection snakeSection) {
		this.neighbor = snakeSection;
	}

	public void drawBoundingBox() {
		if (GameSettings.DEBUG_MODE) {
			bounds = new Rectangle(x - radius / 2, y - radius / 2, radius, radius);
			bounds.setStroke(Color.YELLOW);
			bounds.setStrokeWidth(3);
			bounds.setFill(Color.TRANSPARENT);
			boundBox = new Rectangle(x - snakeHead.getRadius()/2,y - snakeHead.getRadius()/2,snakeHead.getRadius(),snakeHead.getRadius());
			boundBox.setStroke(Color.RED);
			boundBox.setStrokeWidth(3);
			boundBox.setFill(Color.TRANSPARENT);
		    layer.getChildren().add(boundBox);
			game.getSeventhLayer().getChildren().add(bounds);
		}
	}

	public void displaceDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !DEAD && !LEVEL_COMPLETED) {
			for (int i = 0; i < GameSettings.DIRT_AMOUNT; i++) {
				game.getDebrisManager().addDebris(new DirtDisplacement(game, GameImageBank.sand_grain, 1, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
	}

	public void displaceSpeedDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !DEAD && !LEVEL_COMPLETED) {
			for (int i = 0; i < GameSettings.DIRT_AMOUNT; i++) {
				game.getDebrisManager().addDebris(new DirtDisplacement(game, GameImageBank.sand_grain, 1, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (13 - -13 + 1) + -13)));
			}
		}
	}

	public void die() {
		DEAD = true;
		game.getHealthBarTwo().drainAll();
		game.setStateID(GameStateID.DEATH_ANIMATION);
		overlay.addToneOverlay(Color.RED, 5, 0.05);
		isDead = true;
		GameSettings.ALLOW_DAMAGE_IMMUNITY = true;
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
		if (allowThrust && direction!=PlayerMovement.STANDING_STILL) {
			if (thrust == true) {
				this.thrust = thrust;
				this.game.getEnergyBarTwo().setSpeedThrust(true);
			}
		}
		if (thrust == false) {
			this.thrust = thrust;
			this.game.getEnergyBarTwo().setDelay();
			this.game.getEnergyBarTwo().setSpeedThrust(false);
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
		return new Rectangle2D(x - (GameSettings.PATH_FINDING_CELL_SIZE-8)/2, y - (GameSettings.PATH_FINDING_CELL_SIZE-8)/2, GameSettings.PATH_FINDING_CELL_SIZE-8, GameSettings.PATH_FINDING_CELL_SIZE-8);
	}

	public Rectangle2D getAIBounds() {
		return new Rectangle2D(x - radius / 2 + offsetX,y - radius / 2 + offsetY,radius,radius);
	}

	public PlayerTwoHead getHead() {
		return snakeHead;
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

	public void setManualGameOver(boolean state){
		if(game.getStateID()==GameStateID.DEATH_ANIMATION)
		this.gameOverOverride = state;
	}

	public boolean getManualGameOver() {
		return gameOverOverride;
	}

}