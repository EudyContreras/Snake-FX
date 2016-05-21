package com.SnakeGame.PlayerOne;

import java.util.LinkedList;

import com.SnakeGame.FrameWork.AbstractObject;
import com.SnakeGame.FrameWork.AbstractTile;
import com.SnakeGame.FrameWork.GameObjectManager;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.HudElements.ScoreKeeper;
import com.SnakeGame.IDenums.GameObjectID;
import com.SnakeGame.IDenums.GameStateID;
import com.SnakeGame.IDenums.LevelObjectID;
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

	private int turnDelay = Settings.TURN_DELAY;
	private int dirtDelay = 10;
	private int maxOpenTime = 0;
	private int coolDown = 60;
	private int immunity = Settings.IMMUNITY_TIME;
	private int moveDelay = 0;
	private int appleCount = 0;
	private double bodyTrigger;
	private double offsetX = 0;
	private double offsetY = 0;
	private boolean isDead = false;
	private boolean collision = false;
	private boolean showTheSkull = false;
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
	private SnakeGame game;
	private Circle skull;
	private Animation anim;
	private Rectangle bounds;
	private ScreenOverlay overlay;
	private PlayerOneTail tail;
	private PlayerOneHead snakeHead;
	private PlayerOneSection neighbor;
	private PlayerOneSectionManager sectManager;
	private ImagePattern eatingFrame = new ImagePattern(GameImageBank.snakeEating);
	private ImagePattern blinkingFrame = new ImagePattern(GameImageBank.snakeBlinking);
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

	public PlayerOne(SnakeGame game, Pane layer, Node node, double x, double y, double r, double velX, double velY,
			double velR, double health, double damage, double speed, GameObjectID id, GameObjectManager gom) {
		super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
		this.game = game;
		this.anim = new Animation();
		this.circle.setVisible(false);
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
		this.snakeHead = new PlayerOneHead(this, game, layer,
				new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(GameImageBank.snakeHead)), x, y,
				GameObjectID.SnakeMouth, PlayerMovement.MOVE_DOWN);
		this.game.getObjectManager().addObject(snakeHead);
		this.sectManager = game.getSectManagerOne();
		this.loadImages();
		this.drawBoundingBox();
		this.setDirection(PlayerMovement.MOVE_DOWN);
	}

	public void loadImages() {
		anim.addScene(GameImageBank.snakeHead, 4000);
		anim.addScene(GameImageBank.snakeBlinking, 250);
		setAnimation(anim);
	}

	public void spawnBody() {
		addbaseSections();
		KEEP_MOVING = false;
		this.direction = PlayerMovement.STANDING_STILL;
	}

	public void updateUI() {
		super.updateUI();
	}

	public void move() {
		if (DEAD == false && LEVEL_COMPLETED == false && KEEP_MOVING)
			super.move();
	}

	public void logicUpdate() {
		controlEating();
		hinderMovement();
		positionBody();
		updateBounds();
		updateImmunity();
		updateDirt();
		checkTurns();
		fadeOut();
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
		if (turns.size() > 0) {
			turnDelay--;
			if (turnDelay <= 0) {
				makeTurn();
			}
		}
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
		if (Settings.DEBUG_MODE) {
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
		if (Settings.ALLOW_DIRT) {
			dirtDelay--;
			if (dirtDelay <= 0) {
				if (KEEP_MOVING) {
					displaceDirt(x + width / 2, y + height / 2, 18, 18);
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
		if (KEEP_MOVING) {
			moveDelay--;
			if (moveDelay <= 0) {
				moveDelay = 0;
				allowCollision = true;
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
		coolDown = Settings.BITE_DELAY;
	}

	public void setDirection(PlayerMovement direction) {
		KEEP_MOVING = true;
		if (!LEVEL_COMPLETED && !DEAD) {
			if (this.direction == direction) {
				this.direction = direction;
			} else {
				switch (direction) {
				case MOVE_UP:
					if (this.direction != PlayerMovement.MOVE_DOWN) {
						if (allowTurnUp) {
							moveUp();
							snakeHead.setR(180);
						}
					}
					break;
				case MOVE_DOWN:
					if (this.direction != PlayerMovement.MOVE_UP) {
						if (allowTurnDown) {
							moveDown();
							snakeHead.setR(0);
						}
					}
					break;
				case MOVE_LEFT:
					if (this.direction != PlayerMovement.MOVE_RIGHT) {
						if (allowTurnLeft) {
							moveLeft();
							snakeHead.setR(89);
						}
					}
					break;
				case MOVE_RIGHT:
					if (this.direction != PlayerMovement.MOVE_LEFT) {
						if (allowTurnRight) {
							moveRight();
							snakeHead.setR(-89);
						}
					}
					break;
				case STANDING_STILL:
					break;
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
		velY = -Settings.SNAKE_SPEED;
		velX = 0;
		r = 180;
		setCurrentDirection(PlayerMovement.MOVE_UP);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, 0);
		}
	}

	private void moveDown() {
		offsetX = 0;
		offsetY = 20;
		velY = Settings.SNAKE_SPEED;
		velX = 0;
		r = 0;
		setCurrentDirection(PlayerMovement.MOVE_DOWN);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, 0);
		}
	}

	private void moveRight() {
		offsetX = 20;
		offsetY = 0;
		velX = Settings.SNAKE_SPEED;
		velY = 0;
		r = -89;
		setCurrentDirection(PlayerMovement.MOVE_RIGHT);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, 0);
		}
	}

	private void moveLeft() {
		offsetX = -20;
		offsetY = 0;
		velX = -Settings.SNAKE_SPEED;
		velY = 0;
		r = 89;
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
			for (int i = 0; i < game.getGameLoader().tileManager.tile.size(); i++) {
				AbstractTile tempTile = game.getGameLoader().tileManager.tile.get(i);
				if (tempTile.getId() == LevelObjectID.cactus) {
					if (getBounds().intersects(tempTile.getBounds())) {
						if (allowDamage) {
							setCollision(true);
							if (!DEAD) {
								this.overlay.addDistortion(15, 0.2);
								this.overlay.addToneOverlay(Color.rgb(220, 0, 0), 5, 1.0);
							}
							immunity = Settings.IMMUNITY_TIME;
							allowDamage = false;
						}
					}
				}
			}
			for (int i = 0; i < game.getGameLoader().tileManager.block.size(); i++) {
				AbstractTile tempTile = game.getGameLoader().tileManager.block.get(i);
				if (tempTile.getId() == LevelObjectID.rock) {
					if (getBounds().intersects(tempTile.getBounds())) {
						if (Settings.ROCK_COLLISION) {
							if (allowCollision) {
								KEEP_MOVING = false;
								allowCollision = false;
							}
						}
					}
				}
			}
			for (int i = 0; i < game.getGameLoader().tileManager.trap.size(); i++) {
				AbstractTile tempTile = game.getGameLoader().tileManager.trap.get(i);
				if (tempTile.getId() == LevelObjectID.fence) {
					if (getBounds().intersects(tempTile.getBounds())) {
						die();
					}
				}
			}
		}
	}

	public void addbaseSections() {
		for (int i = 0; i < Settings.SECTIONS_TO_ADD + 1; i++) {
			sectManager.addSection(new PlayerOneSection(this, game, layer,
					new Circle(Settings.SECTION_SIZE, new ImagePattern(GameImageBank.snakeBody)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			NUMERIC_ID++;
		}
	}

	public void addSection() {
		for (int i = 0; i < Settings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new PlayerOneSection(this, game, layer,
					new Circle(Settings.SECTION_SIZE, new ImagePattern(GameImageBank.snakeBody)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			SnakeGame.writeToLog("New section added " + NUMERIC_ID);
			NUMERIC_ID++;
			appleCount++;
		}
		game.getScoreBoardOne().increaseScore();
		if (ScoreKeeper.APPLE_COUNT > 4)
			game.getGameLoader().spawnSnakeFood();
	}

	public boolean withinBounds() {
		return x > 0 - radius - 1 && x < Settings.WIDTH + radius + 1 && y > 0 - radius - 1
				&& y < Settings.HEIGHT + radius + 1;
	}

	public void checkBounds() {
		if (x < 0 - radius) {
			x = (float) (Settings.WIDTH + radius);
		} else if (x > Settings.WIDTH + radius) {
			x = (float) (0 - radius);
		} else if (y < 0 - radius) {
			y = (float) (Settings.HEIGHT + radius);
		} else if (y > Settings.HEIGHT + radius) {
			y = (float) (0 - radius);
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
			if (y - neighbor.getY() > Settings.SECTION_SIZE) {
				y = (float) (neighbor.getY() + Settings.SECTION_SIZE);
				x = neighbor.getX();
			}
		}
		if (this.direction == PlayerMovement.MOVE_UP) {
			if (neighbor.getY() - y > Settings.SECTION_SIZE) {
				y = (float) (neighbor.getY() - Settings.SECTION_SIZE);
				x = neighbor.getX();
			}
		}
		if (this.direction == PlayerMovement.MOVE_LEFT) {
			if (neighbor.getX() - x > Settings.SECTION_SIZE) {
				x = (float) (neighbor.getX() - Settings.SECTION_SIZE);
				y = neighbor.getY();
			}
		}
		if (this.direction == PlayerMovement.MOVE_RIGHT) {
			if (x - neighbor.getX() > Settings.SECTION_SIZE) {
				x = (float) (neighbor.getX() + Settings.SECTION_SIZE);
				y = neighbor.getY();
			}
		}
	}

	public void setNeighbor(PlayerOneSection snakeSection) {
		this.neighbor = snakeSection;
	}

	public void drawBoundingBox() {

		if (Settings.DEBUG_MODE) {
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
				game.getDebrisManager().addObject(new DirtDisplacement(game, GameImageBank.dirt,1, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
	}

	public void addBones() {
		isDead = true;
		skull = new Circle(x, y, this.radius * 0.8, new ImagePattern(GameImageBank.snakeSkull));
		skull.setRotate(r);
		game.getFirstLayer().getChildren().add(skull);


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
		checkBounds();
	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public void die() {
		DEAD = true;
		blurOut();
		game.getHealthBarOne().drainAll();
		isDead = true;
	}
	public void fadeOut(){
		if(ALLOW_FADE){
		overlay.addFadeScreen(5, GameStateID.GAME_OVER);
		ALLOW_FADE = false;
		}
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

	}

	public void setTail(PlayerOneTail tail) {
		this.tail = tail;

	}

	public PlayerOneTail getTail() {
		return tail;
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

	public boolean isShowTheSkull() {
		return showTheSkull;
	}

	public void setShowTheSkull(boolean showTheSkull) {
		this.showTheSkull = showTheSkull;
	}

	public boolean isAllowOpen() {
		return allowOpen;
	}

	public void setAllowOpen(boolean allowOpen) {
		this.allowOpen = allowOpen;
	}
	public double getAppleCount(){
		return appleCount;
	}

}