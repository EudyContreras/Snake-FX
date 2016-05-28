package com.SnakeGame.PlayerOne;

import java.util.LinkedList;

import com.SnakeGame.AbstractModels.AbstractObject;
import com.SnakeGame.AbstractModels.AbstractTile;
import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.FrameWork.ObjectManager;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.HudElements.ScoreKeeper;
import com.SnakeGame.IDenums.GameLevelObjectID;
import com.SnakeGame.IDenums.GameObjectID;
import com.SnakeGame.IDenums.GameStateID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.Particles.DirtDisplacement;
import com.SnakeGame.Utilities.Animation;
import com.SnakeGame.Utilities.ScreenOverlay;

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

public class PlayerOne extends AbstractObject {

	private int turnDelay = GameSettings.TURN_DELAY;
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
	private GameManager game;
	private Animation anim;
	private Rectangle bounds;
	private ScreenOverlay overlay;
	private PlayerOneHead snakeHead;
	private PlayerOneSection neighbor;
	private PlayerOneSectionManager sectManager;
	private ImagePattern eatingFrame = new ImagePattern(GameImageBank.snakeOneEating);
	private ImagePattern blinkingFrame = new ImagePattern(GameImageBank.snakeOneBlinking);
	private LinkedList<PlayerMovement> turns = new LinkedList<>();
	private PlayerMovement direction;
	public static int NUMERIC_ID = 0;
	public static boolean DEAD = false;
	public static Boolean LEVEL_COMPLETED = false;
	public static Boolean STOP_MOVING = false;
	public static Boolean MOUTH_OPEN = false;
	public static Boolean MOUTH_CLOSE = true;
	public static Boolean KEEP_MOVING = true;
	public static Boolean ALLOW_FADE = false;

	public PlayerOne(GameManager game, Pane layer, Node node, double x, double y, double r, double velX, double velY,
			double velR, double health, double damage, double speed, GameObjectID id, ObjectManager gom) {
		super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
		this.game = game;
		this.anim = new Animation();
		this.circle.setVisible(false);
		this.bodyTrigger = y + 20;
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
		this.snakeHead = new PlayerOneHead(this, game, layer,
				new Circle(GameSettings.PLAYER_ONE_SIZE * 1.4, new ImagePattern(GameImageBank.snakeOneHead)), x, y,
				GameObjectID.SnakeMouth, PlayerMovement.MOVE_DOWN);
		this.game.getPlayerOneManager().addObject(snakeHead);
		this.sectManager = game.getSectManagerOne();
		this.loadImages();
		this.drawBoundingBox();
		this.setDirection(PlayerMovement.MOVE_DOWN);
	}

	public void loadImages() {
		anim.addScene(GameImageBank.snakeOneHead, 4000);
		anim.addScene(GameImageBank.snakeOneBlinking, 250);
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
		updateSpeedDirt();
		updateDirt();
		checkTurns();
		fadeOut();
		speedUp();
		speedDown();
		slowDown();
		overlay.updateEffect();

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
	public void setDirection(PlayerMovement direction) {
		if (game.getStateID() != GameStateID.GAME_MENU) {
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
								if (!GameSettings.FAST_TURNS) {
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
								if (!GameSettings.FAST_TURNS) {
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
								if (!GameSettings.FAST_TURNS) {
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
								if (!GameSettings.FAST_TURNS) {
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
		if (!GameSettings.ALLOW_SELF_COLLISION) {
			if (!LEVEL_COMPLETED && !DEAD) {
				if (this.direction == direction) {
					this.direction = direction;
				} else {
					switch (direction) {
					case MOVE_UP:
						if (!GameSettings.FAST_TURNS) {
							if (allowTurnUp)
								turnDelay(PlayerMovement.MOVE_UP);
						} else {
							if (allowTurnUp)
								moveUp();
						}
						break;
					case MOVE_DOWN:
						if (!GameSettings.FAST_TURNS) {
							if (allowTurnDown)
								turnDelay(PlayerMovement.MOVE_DOWN);
						} else {
							if (allowTurnDown)
								moveDown();
						}
						break;
					case MOVE_LEFT:
						if (!GameSettings.FAST_TURNS) {
							if (allowTurnLeft)
								turnDelay(PlayerMovement.MOVE_LEFT);
						} else {
							if (allowTurnLeft)
								moveLeft();
						}
						break;
					case MOVE_RIGHT:
						if (!GameSettings.FAST_TURNS) {
							if (allowTurnRight)
								turnDelay(PlayerMovement.MOVE_RIGHT);
						} else {
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
		snakeHead.setR(180);
		if (!GameSettings.FAST_TURNS)
			turns.remove(0);
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
		snakeHead.setR(0);
		if (!GameSettings.FAST_TURNS)
			turns.remove(0);
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
		r = -89;
		snakeHead.setR(-89);
		if (!GameSettings.FAST_TURNS)
			turns.remove(0);
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
		r = 89;
		snakeHead.setR(89);
		if (!GameSettings.FAST_TURNS)
			turns.remove(0);
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
		if (!DEAD && !LEVEL_COMPLETED) {
			for (int i = 0; i < game.getGameLoader().getTileManager().getTile().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getTile().get(i);
				if (tempTile.getId() == GameLevelObjectID.cactus) {
					if (getBounds().intersects(tempTile.getBounds())) {
						if (allowDamage && game.getStateID() != GameStateID.GAME_MENU) {
							if (!GameSettings.DAMAGE_IMMUNITY) {
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
						if (GameSettings.ROCK_COLLISION) {
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
							if (!GameSettings.DAMAGE_IMMUNITY)
								die();
						}
					}
				}
				if (tempTile.getId() == GameLevelObjectID.trap) {
					if (getBounds().intersects(tempTile.getBounds())) {
						if (!DEAD) {
							if (!GameSettings.DAMAGE_IMMUNITY)
								die();
						}
					}
				}
			}
		}
	}

	public void addbaseSections() {
		for (int i = 0; i < GameSettings.SECTIONS_TO_ADD + 1; i++) {
			sectManager.addSection(new PlayerOneSection(this, game, layer,
					new Circle(GameSettings.PLAYER_ONE_SIZE, new ImagePattern(GameImageBank.snakeOneSkin)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			NUMERIC_ID++;
		}
	}

	public void addSection() {
		if (GameSettings.PLAYER_ONE_SIZE < 30) {
			counter++;
			if (counter >= 15) {
				counter = 0;
				GameSettings.PLAYER_ONE_SIZE += 2;
			}
		}
		for (int i = 0; i < GameSettings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new PlayerOneSection(this, game, layer,
					new Circle(GameSettings.PLAYER_ONE_SIZE, new ImagePattern(GameImageBank.snakeOneSkin)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			NUMERIC_ID++;
			appleCount++;
		}
		game.getScoreBoardOne().increaseScore();
		if (ScoreKeeper.APPLE_COUNT > 4)
			game.getGameLoader().spawnSnakeFood();
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

	public void setNeighbor(PlayerOneSection snakeSection) {
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

	public void displaceDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !DEAD && !LEVEL_COMPLETED) {
			for (int i = 0; i < 15; i++) {
				game.getDebrisManager().addObject(new DirtDisplacement(game, GameImageBank.dirt, 1, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
	}

	public void displaceSpeedDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !DEAD && !LEVEL_COMPLETED) {
			for (int i = 0; i < GameSettings.DIRT_AMOUNT; i++) {
				game.getDebrisManager().addObject(new DirtDisplacement(game, GameImageBank.dirt, 1, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (13 - -13 + 1) + -13)));
			}
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
			overlay.addFadeScreen(5, GameStateID.GAME_OVER);
			ALLOW_FADE = false;
		}
	}

	public Image getAnimationImage() {
		return anim.getImage();
	}

	public Animation getAnimation() {
		return anim;
	}

	public void setAnimation(Animation anim) {
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
		this.thrust = thrust;
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

	public void setSpeedBump(boolean b) {
		this.goSlow = true;
		this.thrust = false;
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

	public PlayerOneHead getHead() {
		return snakeHead;
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

}