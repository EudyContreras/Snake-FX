package com.SnakeGame.PlayerTwo;

import java.util.LinkedList;

import com.SnakeGame.Core.GameImageBank;
import com.SnakeGame.Core.GameLoader;
import com.SnakeGame.Core.GameObject;
import com.SnakeGame.Core.PlayerMovement;
import com.SnakeGame.Core.Settings;
import com.SnakeGame.Core.SnakeGame;
import com.SnakeGame.Debris_Particles.DirtDisplacement;
import com.SnakeGame.GameObjects.Tile;
import com.SnakeGame.HudElements.ScoreKeeper;
import com.SnakeGame.ObjectIDs.GameObjectID;
import com.SnakeGame.ObjectIDs.LevelObjectID;
import com.SnakeGame.Utilities.Animation;
import com.SnakeGame.Utilities.GameObjectManager;
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

public class Player2 extends GameObject {

	double minX;
	double maxX;
	double minY;
	double maxY;
	double speed;
	double clearUp;
	double lightOpacity = 0.8;
	double vibrationUp = 1.0f;
	double vibrationDown = 1.0f;
	double amountOfGlow = 0;
	double offsetX = 0;
	double offsetY = 0;
	boolean isDead = false;
	boolean showTheSkull = false;
	boolean cantShoot = true;
	boolean notShooting = false;
	boolean lampOff = false;
	boolean glowing = false;
	boolean up = true;
	boolean down = false;
	boolean shake = false;
	boolean applyRecoil = false;
	boolean left = true;
	boolean right = false;
	boolean vibrate = false;
	boolean collision = false;
	boolean checkVibrate = true;
	boolean hitBounds = false;
	boolean movingUp = false;
	boolean movingDown = false;
	boolean movingLeft = false;
	boolean movingRight = false;
	boolean standingStill = false;
	boolean standingStillY = false;
	boolean notMovingToTheSides = false;
	boolean loopingAudio = false;
	boolean increaseGlow = true;
	boolean decreaseGlow = false;
	boolean introduceSpaceship = true;
	boolean firstBite = false;
	boolean notEating = true;
	public boolean allowOpen = true;
	boolean eatCoolDown = false;
	boolean setDelay = false;
	boolean allowToTurn = true;
	boolean allowDamage = true;
	boolean noCollisionZone = true;
	boolean allowCollision = true;
	boolean hasBaseBody = false;
	boolean allowTurnLeft = true;
	boolean allowTurnRight = true;
	boolean allowTurnUp = true;
	boolean allowTurnDown = true;
	boolean allowMoving = true;
	int turnDelay = Settings.TURN_DELAY;
	int dirtDelay = 10;
	int tailSize = 30;
	int counter = 0;
	int shootingLag;
	int rightBoundary;
	int recoilDuration = 30;
	int shakeDuration = 30;
	int maxOpenTime = 0;
	int coolDown = 60;
	int immunity = Settings.IMMUNITY_TIME;
	int scroll = 0;
	int moveDelay = 0;
	int safeDistance = 0;
	double fade = 0.0;
	double fadeAway = 1.0f;
	double glowLevel = 0;
	double amountOfBlur = 2.0f;
	double bodyTrigger;
	SnakeGame game;
	LinkedList<PlayerMovement> turns = new LinkedList<>();
	Rectangle fadeRect = new Rectangle(0, 0, Settings.WIDTH, Settings.HEIGHT);
	ImagePattern pattern = new ImagePattern(GameImageBank.snakeEating2);
	ImagePattern pattern2 = new ImagePattern(GameImageBank.snakeBlinking2);
	Circle headBounds;
	Circle skull;
	GameObjectManager gom;
	Animation anim;
	SnakeTwoSectionManager sectManager;
	SnakeHead2 snakeHead;
	SnakeMouth2 mouth;
	SnakeTail2 tail;
	Rectangle bounds;
	EatTrigger2 trigger;
	ScreenOverlay overlay;
	PlayerMovement direction;
	private SnakeSection2 neighbor;
	public static int NUMERIC_ID = 0;
	public static Boolean killTheSnake = false;
	public static Boolean levelComplete = false;
	public static Boolean stopMovement = false;
	public static Boolean MOUTH_OPEN = false;
	public static Boolean MOUTH_CLOSE = true;
	public static Boolean keepMoving = true;

	public Player2(SnakeGame game, Pane layer, Node node, double x, double y, double r, double velX, double velY,
			double velR, double health, double damage, double speed, GameObjectID id, GameObjectManager gom) {
		super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
		this.gom = gom;
		this.speed = speed;
		this.game = game;
		this.bodyTrigger = y + 20;
		this.fadeRect.setOpacity(fade);
		this.fadeRect.setFill(Color.BLACK);
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
		this.anim = new Animation();
		this.circle.setVisible(false);
		this.direction = PlayerMovement.STANDING_STILL;
		this.sectManager = game.getSectionManager2();
		this.snakeHead = new SnakeHead2(this, game, layer, GameImageBank.snakeSphere2, this.x, this.y,
				GameObjectID.SnakeMouth, PlayerMovement.MOVE_DOWN);
		this.mouth = new SnakeMouth2(this, game, game.getSnakeHeadLayer(),
				new Circle(Settings.SECTION_SIZE * 0.30 / GameLoader.ResolutionScaleX, Color.TRANSPARENT), this.x,
				this.y, GameObjectID.SnakeMouth, PlayerMovement.MOVE_LEFT);
		this.trigger = new EatTrigger2(this, game, game.getSnakeHeadLayer(),
				new Circle(Settings.SECTION_SIZE * 0.8 / GameLoader.ResolutionScaleX, Color.TRANSPARENT), this.x,
				this.y, GameObjectID.SnakeMouth, PlayerMovement.MOVE_LEFT);
		this.gom.addObject(trigger);
		this.gom.addObject(mouth);
		this.gom.addObject(snakeHead);
		this.drawBoundingBox();
		this.loadImages();
		this.setDirection(PlayerMovement.MOVE_DOWN);
	}

	public void updateUI() {
		overlay.updateEffect();
		super.updateUI();
		if (!killTheSnake) {
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
			this.snakeHead.setAnim(pattern2);
		}
	}

	public void spawnBody() {
		addbaseSections();
		keepMoving = false;
		this.direction = PlayerMovement.STANDING_STILL;
	}

	public void move() {
		checkTurns();
		if (!hasBaseBody) {
			if (y >= bodyTrigger) {
				spawnBody();
				hasBaseBody = true;
			}
		}
		if (Settings.DEBUG_MODE) {
			bounds.setX(x - radius / 2 + offsetX);
			bounds.setY(y - radius / 2 + offsetY);
		}
		if (killTheSnake == false && levelComplete == false && keepMoving)
			super.move();
		dirtDelay--;
		if (dirtDelay <= 0) {
			if (keepMoving) {
				displaceDirt(x + width / 2, y + height / 2, 18, 18);
				dirtDelay = 10;
			}
		}
		if (turns.size() > 0) {
			turnDelay--;
			if (turnDelay <= 0) {
				makeTurn();
			}
		}
		if (keepMoving) {
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

	public void setDirection(PlayerMovement direction) {
		// snakeHead.checkRadiusCollision();
		if (!killTheSnake && !levelComplete) {
			if (this.direction == direction) {
				this.direction = direction;
			} else {
				switch (direction) {
				case MOVE_UP:
					if (this.direction != PlayerMovement.MOVE_DOWN) {
						if (!Settings.FAST_TURNS) {
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
						if (!Settings.FAST_TURNS) {
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
						if (!Settings.FAST_TURNS) {
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
						if (!Settings.FAST_TURNS) {
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
		velY = -Settings.SNAKE_SPEED;
		velX = 0;
		r = 180;
		mouth.setOffsetX(0);
		mouth.setOffsetY(-25);
		offsetX = 0;
		offsetY = -20;
		snakeHead.setRotate(true, PlayerMovement.MOVE_UP, 180);
		if (!Settings.FAST_TURNS)
			turns.remove(0);
		turnDelay = Settings.TURN_DELAY;
		if (keepMoving == false) {
			moveDelay = Settings.COLLISION_DELAY;
			keepMoving = true;
		}

		setCurrentDirection(PlayerMovement.MOVE_UP);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, 0);
		}
	}

	private void moveDown() {
		velY = Settings.SNAKE_SPEED;
		velX = 0;
		r = 0;
		mouth.setOffsetX(0);
		mouth.setOffsetY(25);
		offsetX = 0;
		offsetY = 20;
		snakeHead.setRotate(true, PlayerMovement.MOVE_DOWN, 0);
		if (!Settings.FAST_TURNS)
			turns.remove(0);
		turnDelay = Settings.TURN_DELAY;
		if (keepMoving == false) {
			moveDelay = Settings.COLLISION_DELAY;
			keepMoving = true;
		}
		setCurrentDirection(PlayerMovement.MOVE_DOWN);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, 0);
		}
	}

	private void moveRight() {
		velX = Settings.SNAKE_SPEED;
		velY = 0;
		r = -89;
		mouth.setOffsetX(25);
		mouth.setOffsetY(0);
		offsetX = 20;
		offsetY = 0;
		snakeHead.setRotate(true, PlayerMovement.MOVE_RIGHT, -89);
		if (!Settings.FAST_TURNS)
			turns.remove(0);
		turnDelay = Settings.TURN_DELAY;
		if (keepMoving == false) {
			moveDelay = Settings.COLLISION_DELAY;
			keepMoving = true;
		}
		setCurrentDirection(PlayerMovement.MOVE_RIGHT);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, 0);
		}
	}

	private void moveLeft() {
		velX = -Settings.SNAKE_SPEED;
		velY = 0;
		r = 89;
		mouth.setOffsetX(-25);
		mouth.setOffsetY(0);
		offsetX = -20;
		offsetY = 0;
		snakeHead.setRotate(true, PlayerMovement.MOVE_LEFT, 89);
		if (!Settings.FAST_TURNS)
			turns.remove(0);
		turnDelay = Settings.TURN_DELAY;
		if (keepMoving == false) {
			moveDelay = Settings.COLLISION_DELAY;
			keepMoving = true;
		}
		setCurrentDirection(PlayerMovement.MOVE_LEFT);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_LEFT, 0);
		}
	}

	public void dontMove() {
		velX = 0;
		velY = 0;
		r = 89;
		mouth.setOffsetX(-25);
		mouth.setOffsetY(0);
		offsetX = -20;
		offsetY = 0;
		snakeHead.setRotate(true, PlayerMovement.STANDING_STILL, 89);
		if (!Settings.FAST_TURNS)
			turns.remove(0);
		turnDelay = Settings.TURN_DELAY;
		if (keepMoving == false) {
			moveDelay = Settings.COLLISION_DELAY;
			keepMoving = true;
		}
		setCurrentDirection(PlayerMovement.STANDING_STILL);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.STANDING_STILL, 0);
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

	public void setCurrentDirection(PlayerMovement direction) {
		this.direction = direction;
	}

	public PlayerMovement getCurrentDirection() {
		return direction;
	}

	public void addbaseSections() {
		for (int i = 0; i < Settings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new SnakeSection2(this, game, game.getSnakeBodyLayer(),
					new Circle(Settings.SECTION_SIZE, new ImagePattern(GameImageBank.snakeBody2)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			SnakeGame.writeToLog("New section added " + NUMERIC_ID);
			NUMERIC_ID++;
		}
	}

	public void addSection() {
		for (int i = 0; i < Settings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new SnakeSection2(this, game, game.getSnakeBodyLayer(),
					new Circle(Settings.SECTION_SIZE, new ImagePattern(GameImageBank.snakeBody2)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			SnakeGame.writeToLog("New section added " + NUMERIC_ID);
			NUMERIC_ID++;
		}
		game.getScoreBoard().increaseScore();
		if (ScoreKeeper.APPLE_COUNT > 4)
			game.getloader().spawnSnakeFood();
	}

	public void displaceDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !killTheSnake && !levelComplete) {
			for (int i = 0; i < 15; i++) {
				game.getDebrisManager().addObject(new DirtDisplacement(game, GameImageBank.dirt, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
	}

	public void checkCollision() {
		for (int i = 0; i < game.getloader().tileManager.tile.size(); i++) {
			Tile tempTile = game.getloader().tileManager.tile.get(i);
			if (tempTile.getId() == LevelObjectID.cactus) {
				if (snakeHead.getBounds().intersects(tempTile.getBounds())) {
					if (allowDamage) {
						setCollision(true);
						if (!killTheSnake) {
							this.overlay.addDistortion(15, 0.2);
							// this.overlay.addIntenseBlur(20, 2);
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
							keepMoving = false;
							allowCollision = false;
						}
					}
				}
			}
		}
	}

	public boolean withinBounds() {
		return x > 0 - radius - 1 && x < Settings.WIDTH + radius + 1 && y > 0 - radius - 1
				&& y < Settings.HEIGHT + radius + 1;
	}

	public void checkBounds() {
		if (x < 0 - radius) {
			x = Settings.WIDTH + radius;
		} else if (x > Settings.WIDTH + radius) {
			x = 0 - radius;
		} else if (y < 0 - radius) {
			y = Settings.HEIGHT + radius;
		} else if (y > Settings.HEIGHT + radius) {
			y = 0 - radius;
		}
	}

	public void calculateAction() {

		if (this.direction == PlayerMovement.MOVE_DOWN) {
			if (y > neighbor.getY() + radius) {
				y--;
			}
		}
		if (this.direction == PlayerMovement.MOVE_UP) {
			if (y < neighbor.getY() - radius) {
				y++;
			}
		}
		if (this.direction == PlayerMovement.MOVE_LEFT) {
			if (x < neighbor.getX() - radius) {
				x++;
			}
		}
		if (this.direction == PlayerMovement.MOVE_RIGHT) {
			if (x > neighbor.getX() + radius) {
				x--;
			}
		}
	}

	public void draw(GraphicsContext gc) {
		if (neighbor != null) {
			headAdjustment();
			checkBounds();
		}
		if (allowDamage == false) {
			immunity--;
			if (immunity <= 0) {
				immunity = 0;
				allowDamage = true;
			}
		}

		if (isDead) {
			fade += 0.003;
			fadeRect.setOpacity(fade);
			if (fade >= 1.0f) {
				fadeRect.setOpacity(1);
			}
			if (fade >= 1.1f) {
				game.gameOver();
				//
			}
		}
		if (showTheSkull == true) {
			fadeAway -= 0.005;
			// circle.setOpacity(fadeAway);
			if (fadeAway <= 0) {
				fadeAway = 0;
			}
		}
	}

	public void headAdjustment() {
		if (this.direction == PlayerMovement.MOVE_DOWN) {
			if (y - neighbor.getY() > Settings.SECTION_SIZE) {
				y = neighbor.getY() + Settings.SECTION_SIZE;
				x = neighbor.getX();
			}
		}
		if (this.direction == PlayerMovement.MOVE_UP) {
			if (neighbor.getY() - y > Settings.SECTION_SIZE) {
				y = neighbor.getY() - Settings.SECTION_SIZE;
				x = neighbor.getX();
			}
		}
		if (this.direction == PlayerMovement.MOVE_LEFT) {
			if (neighbor.getX() - x > Settings.SECTION_SIZE) {
				x = neighbor.getX() - Settings.SECTION_SIZE;
				y = neighbor.getY();
			}
		}
		if (this.direction == PlayerMovement.MOVE_RIGHT) {
			if (x - neighbor.getX() > Settings.SECTION_SIZE) {
				x = neighbor.getX() + Settings.SECTION_SIZE;
				y = neighbor.getY();
			}
		}
	}

	public void blurOut() {
		this.overlay.addDeathBlur();
	}

	public void loadImages() {
		anim.addScene(GameImageBank.snakeHead2, 4000);
		anim.addScene(GameImageBank.snakeBlinking2, 250);
		setAnimation(anim);
	}

	public void openMouth() {
		if (direction != PlayerMovement.STANDING_STILL) {
			// this.overlay.addDistortion(30, 0.5);
			// this.overlay.addToneOverlay(Color.GREEN, 8, 0.5);
			// this.overlay.addSoftBlur(20, 0.2);
			// this.overlay.addIntenseBlur(20, 0.5);
			// this.overlay.addBloom(8, 1);
			notEating = false;
			MOUTH_CLOSE = false;
			MOUTH_OPEN = true;
			snakeHead.setAnim(this.pattern);
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

	public boolean isApproximate(double tail_X, double sect_X, double tail_Y, double sect_Y) {
		double distance = Math.sqrt((tail_X - sect_X) * (tail_X - sect_X) + (tail_Y - sect_Y) * (tail_Y - sect_Y));
		if (distance > 10) {
			return true;
		}
		return false;
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

	public void setScroller() {
		this.imageView.translateXProperty().addListener((v, oldValue, newValue) -> {

			int offSet = newValue.intValue();
			if (offSet > 100 && offSet < game.levelLenght - 1900) {
				game.getRoot().setTranslateX(-(offSet - 100));
			}
		});
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

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;

	}

	public void die() {
		killTheSnake = true;
		blurOut();
		game.getHealthBarTwo().drainAll();
	}

	public void addBones() {
		isDead = true;
		skull = new Circle(x, y, this.radius * 0.8, new ImagePattern(GameImageBank.snakeSkull));
		skull.setRotate(r);
		game.getDebrisLayer().getChildren().add(skull);
		fadeAndEnd();

	}

	public void fadeAndEnd() {
		game.getFadeScreen().getChildren().add(fadeRect);
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public SnakeMouth2 getMouth() {
		return mouth;
	}

	public void checkRemovability() {

	}

	public boolean isNotMovingFast() {
		return false;
	}

	public void setSpeedBump(boolean b) {

	}

	public void setTail(SnakeTail2 tail) {
		this.tail = tail;
	}

	public SnakeTail2 getTail() {
		return tail;
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

	public void setNeighbor(SnakeSection2 snakeSection) {
		this.neighbor = snakeSection;

	}
}
