package com.SnakeGame.PlayerOne;

import java.util.LinkedList;

import com.SnakeGame.FrameWork.GameObject;
import com.SnakeGame.FrameWork.GameObjectManager;
import com.SnakeGame.FrameWork.GameStateID;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.GameObjects.Tile;
import com.SnakeGame.HudElements.ScoreKeeper;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ObjectIDs.GameObjectID;
import com.SnakeGame.ObjectIDs.LevelObjectID;
import com.SnakeGame.Particles.DirtDisplacement;
import com.SnakeGame.Utilities.Animation;
import com.SnakeGame.Utilities.ScreenOverlay;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PlayerOne extends GameObject {

	public int turnDelay = Settings.TURN_DELAY;
	public int dirtDelay = 10;
	public int tailSize = 30;
	public int maxOpenTime = 0;
	public int coolDown = 60;
	public int immunity = Settings.IMMUNITY_TIME;
	public int moveDelay = 0;
	public int safeDistance = 0;
	public int shakeDuration = 30;
	public float amountOfBlur = 2.0f;
	public double fade = 0.0;
	public double fadeAway = 1.0f;
	public double bodyTrigger;
	public double minX;
	public double maxX;
	public double minY;
	public double maxY;
	public double speed;
	public double clearUp;
	public double offsetX = 0;
	public double offsetY = 0;
	public boolean isDead = false;
	public boolean up = true;
	public boolean down = false;
	public boolean shake = false;
	public boolean applyRecoil = false;
	public boolean left = true;
	public boolean right = false;
	public boolean vibrate = false;
	public boolean collision = false;
	public boolean movingUp = false;
	public boolean movingDown = false;
	public boolean movingLeft = false;
	public boolean movingRight = false;
	public boolean standingStill = false;
	public boolean standingStillY = false;
	public boolean showTheSkull = false;
	public boolean firstBite = false;
	public boolean notEating = true;
	public boolean allowOpen = true;
	public boolean eatCoolDown = false;
	public boolean setDelay = false;
	public boolean allowToTurn = true;
	public boolean allowDamage = true;
	public boolean noCollisionZone = true;
	public boolean allowCollision = true;
	public boolean hasBaseBody = false;
	public boolean allowTurnLeft = true;
	public boolean allowTurnRight = true;
	public boolean allowTurnUp = true;
	public boolean allowTurnDown = true;
	public boolean allowMoving = true;
	public boolean notMovingToTheSides = false;
	public boolean loopingAudio = false;
	public boolean increaseGlow = true;
	public boolean decreaseGlow = false;
	public boolean introduceSpaceship = true;
	public SnakeGame game;
	public Circle headBounds;
	public Circle skull;
	public Animation anim;
	public Rectangle bounds;
	public ScreenOverlay overlay;
	public PlayerOneTail tail;
	public PlayerOneHead snakeHead;
	public PlayerOneSection neighbor;
	public GameObjectManager gom;
	public PlayerOneSectionManager sectManager;
	public DropShadow borderGlow = new DropShadow();
	public DropShadow borderGlow2 = new DropShadow();
	public MotionBlur motionBlur = new MotionBlur();
	public Light.Point light = new Light.Point();
	public Lighting lighting = new Lighting();
	public ImagePattern eatingFrame = new ImagePattern(GameImageBank.snakeEating);
	public ImagePattern blinkingFrame = new ImagePattern(GameImageBank.snakeBlinking);
	private LinkedList<PlayerMovement> turns = new LinkedList<>();
	public PlayerMovement direction;
	public static int NUMERIC_ID = 0;
	public static boolean DEAD = false;
	public static Boolean LEVEL_COMPLETED = false;
	public static Boolean STOP_MOVING = false;
	public static Boolean MOUTH_OPEN = false;
	public static Boolean MOUTH_CLOSE = true;
	public static Boolean KEEP_MOVING = true;

	public PlayerOne(SnakeGame game, Pane layer, Node node, float x, float y, float r, float velX, float velY,
			float velR, double health, double damage, double speed, GameObjectID id, GameObjectManager gom) {
		super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
		this.gom = gom;
		this.speed = speed;
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
		overlay.updateEffect();

	}

	public void controlEating() {
		if (!DEAD) {
			maxOpenTime--;
			coolDown--;
			if (notEating) {
				this.snakeHead.setAnim(anim.getPattern());
			}
			if (allowOpen == false) {
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
					allowOpen = true;
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
		dirtDelay--;
		if (dirtDelay <= 0) {
			if (KEEP_MOVING) {
				displaceDirt(x + width / 2, y + height / 2, 18, 18);
				dirtDelay = 10;
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
			allowOpen = false;
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
		velY = -Settings.SECTION;
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
		velY = Settings.SECTION;
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
		velX = Settings.SECTION;
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
		velX = -Settings.SECTION;
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
			for (int i = 0; i < game.getloader().tileManager.tile.size(); i++) {
				Tile tempTile = game.getloader().tileManager.tile.get(i);
				if (tempTile.getId() == LevelObjectID.cactus) {
					if (getBounds().intersects(tempTile.getBounds())) {
						if (allowDamage) {
							setCollision(true);
							if (!DEAD) {
								this.overlay.addDistortion(15, 0.2);
								this.overlay.addToneOverlay(Color.RED, 5, 3.0);
							}
							immunity = Settings.IMMUNITY_TIME;
							allowDamage = false;
						}
					}
				}
			}
			for (int i = 0; i < game.getloader().tileManager.block.size(); i++) {
				Tile tempTile = game.getloader().tileManager.block.get(i);
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
		}
		game.getScoreBoard().increaseScore();
		if (ScoreKeeper.APPLE_COUNT > 4)
			game.getloader().spawnSnakeFood();
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
			game.getOverlay().getChildren().add(bounds);
		}
	}

	public void displaceDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !DEAD && !LEVEL_COMPLETED) {
			for (int i = 0; i < 15; i++) {
				game.getDebrisManager().addObject(new DirtDisplacement(game, GameImageBank.dirt, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
	}

	public void addBones() {
		isDead = true;
		skull = new Circle(x, y, this.radius * 0.8, new ImagePattern(GameImageBank.snakeSkull));
		skull.setRotate(r);
		game.getDebrisLayer().getChildren().add(skull);
		overlay.addFadeScreen(4, GameStateID.GAME_OVER);

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

}