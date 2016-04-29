package com.SnakeGame.Core;

import java.util.LinkedList;

import com.SnakeGame.Debris_Particles.DirtDisplacement;
import com.SnakeGame.GameObjects.Tile;
import com.SnakeGame.ObjectIDs.GameObjectID;
import com.SnakeGame.ObjectIDs.LevelObjectID;
import com.SnakeGame.SnakeOne.SnakeOneMouth;
import com.SnakeGame.SnakeOne.SnakeOneSectionManager;
import com.SnakeGame.SnakeOne.SnakeOneTail;

import javafx.geometry.Bounds;
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

public class Rodent extends GameObject {

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
	boolean notEating = true;
	boolean allowOpen = true;
	boolean eatCoolDown = false;
	boolean setDelay = false;
	boolean allowToTurn = true;
	boolean allowDamage = true;
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
	double fade = 0.0;
	double fadeAway = 1.0f;
	double glowLevel = 0;
	double amountOfBlur = 2.0f;
	SnakeGame game;
	LinkedList<PlayerMovement> turns = new LinkedList<>();
	Rectangle fadeRect = new Rectangle(0, 0, Settings.WIDTH, Settings.HEIGHT);
	DropShadow borderGlow = new DropShadow();
	DropShadow borderGlow2 = new DropShadow();
	MotionBlur motionBlur = new MotionBlur();
	Light.Point light = new Light.Point();
	Lighting lighting = new Lighting();
	ImagePattern pattern = new ImagePattern(GameImageBank.snakeEating);
	ImagePattern pattern2 = new ImagePattern(GameImageBank.snakeBlinking);
	Circle headBounds;
	Circle skull;
	GameObjectManager gom;
	Animation anim;
	SnakeOneSectionManager sectManager;
	SnakeOneMouth mouth;
	SnakeOneTail tail;
	ScreenOverlay overlay;
	PlayerMovement direction;
	public static int NUMERIC_ID = 0;
	public static Boolean KILL_THE_RODENT = false;
	public static Boolean MOUTH_OPEN = false;
	public static Boolean MOUTH_CLOSE = true;

	public Rodent(SnakeGame game, Pane layer, Node node, double x, double y, double r, double velX, double velY,
			double velR, double health, double damage, double speed, GameObjectID id, GameObjectManager gom) {
		super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
		this.gom = gom;
		this.speed = speed;
		this.game = game;
		this.motionBlur.setAngle(-15.0);
		this.motionBlur.setRadius(amountOfBlur);
		this.imageView.setEffect(motionBlur);
		this.lighting.setSurfaceScale(5.0);
		this.lighting.setLight(light);
		this.lighting.setDiffuseConstant(1.2);
		this.lighting.setSpecularConstant(0.2);
		this.fadeRect.setOpacity(fade);
		this.fadeRect.setFill(Color.BLACK);
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
		this.anim = new Animation();
		this.direction = PlayerMovement.STANDING_STILL;
		this.sectManager = game.getSectionManager();
		this.headBounds = new Circle(x, y, radius * .25);
		this.layer.getChildren().add(headBounds);
		this.headBounds.setFill(Color.TRANSPARENT);
		this.loadImages();
		this.gom.addObject(mouth);
		if (Settings.ADD_LIGHTING)
			this.motionBlur.setInput(lighting);
	}

	public void updateUI() {
		overlay.updateEffect();
		super.updateUI();
		if (!KILL_THE_RODENT) {
			maxOpenTime--;
			coolDown--;
			if (notEating) {
				this.circle.setFill(anim.getPattern());
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
			this.circle.setFill(pattern2);
		}
	}

	public void move() {
		headBounds.setCenterX(x);
		headBounds.setCenterY(y);
		if (KILL_THE_RODENT == false)
			super.move();
		dirtDelay--;
		if (dirtDelay <= 0) {
			displaceDirt(x + width / 2, y + height / 2, 18, 18);
			dirtDelay = 10;
		}
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

	public void setDirection(PlayerMovement direction) {
		if (!KILL_THE_RODENT) {
			if (this.direction == direction) {
				this.direction = direction;
			} else {
				switch (direction) {
				case MOVE_UP:
					if (this.direction != PlayerMovement.MOVE_DOWN) {
						turnDelay(PlayerMovement.MOVE_UP);
					}
					break;
				case MOVE_DOWN:
					if (this.direction != PlayerMovement.MOVE_UP) {
						turnDelay(PlayerMovement.MOVE_DOWN);
					}
					break;
				case MOVE_LEFT:
					if (this.direction != PlayerMovement.MOVE_RIGHT) {
						turnDelay(PlayerMovement.MOVE_LEFT);
					}
					break;
				case MOVE_RIGHT:
					if (this.direction != PlayerMovement.MOVE_LEFT) {
						turnDelay(PlayerMovement.MOVE_RIGHT);
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
			turns.remove(0);
			turnDelay = Settings.TURN_DELAY;
			this.direction = PlayerMovement.MOVE_UP;
		} else if (turns.get(0) == PlayerMovement.MOVE_DOWN) {
			moveDown();
			turns.remove(0);
			turnDelay = Settings.TURN_DELAY;
			this.direction = PlayerMovement.MOVE_DOWN;
		} else if (turns.get(0) == PlayerMovement.MOVE_LEFT) {
			moveLeft();
			turns.remove(0);
			turnDelay = Settings.TURN_DELAY;
			this.direction = PlayerMovement.MOVE_LEFT;
		} else if (turns.get(0) == PlayerMovement.MOVE_RIGHT) {
			moveRight();
			turns.remove(0);
			turnDelay = Settings.TURN_DELAY;
			this.direction = PlayerMovement.MOVE_RIGHT;
		}
	}

	private void moveUp() {
		velY = -Settings.SNAKE_SPEED;
		velX = 0;
		r = 180;
		setCurrentDirection(PlayerMovement.MOVE_UP);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, 0);
		}
	}

	private void moveDown() {
		velY = Settings.SNAKE_SPEED;
		velX = 0;
		r = 0;
		setCurrentDirection(PlayerMovement.MOVE_DOWN);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, 0);
		}
	}

	private void moveRight() {
		velX = Settings.SNAKE_SPEED;
		velY = 0;
		r = -89;
		setCurrentDirection(PlayerMovement.MOVE_RIGHT);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, 0);
		}
	}

	private void moveLeft() {
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

	public void displaceDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !KILL_THE_RODENT) {
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
				if (getCollisionBounds().intersects(tempTile.getCollisionBounds())) {
					if (allowDamage) {
						setCollision(true);
						if (!KILL_THE_RODENT) {
							this.overlay.addDistortion(30, 2);
							this.overlay.addToneOverlay(Color.RED, 6, 1.5);
						}
						immunity = Settings.IMMUNITY_TIME;
						allowDamage = false;
					}
				}
			} else if (tempTile.getId() == LevelObjectID.fence) {
				if (getCollisionBounds().intersects(tempTile.getCollisionBounds())) {
					// killTheSnake = true;
					// blurOut();
				}
			}
		}
	}

	public void draw(GraphicsContext gc) {
		if (allowDamage == false) {
			immunity--;
			if (immunity <= 0) {
				immunity = 0;
				allowDamage = true;
			}
		}
		drawBoundingBox(gc);
		if (isDead) {
			fade += 0.003;
			fadeRect.setOpacity(fade);
			if (fade >= 1.0f) {
				fadeRect.setOpacity(1);
			}
			if (fade >= 1.1f) {
				game.reset();
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

	public void blurOut() {
		this.overlay.addDeathBlur();
	}

	public void loadImages() {
		anim.addScene(GameImageBank.snakeHead, 4000);
		anim.addScene(GameImageBank.snakeBlinking, 250);
		setAnimation(anim);
	}

	public void openMouth() {
		// this.overlay.addDistortion(30, 0.5);
		// this.overlay.addToneOverlay(Color.GREEN, 8, 0.5);
		// this.overlay.addSoftBlur(20, 0.2);
		// this.overlay.addIntenseBlur(20, 0.5);
		// this.overlay.addBloom(8, 1);
		notEating = false;
		MOUTH_CLOSE = false;
		MOUTH_OPEN = true;
		this.circle.setFill(this.pattern);
		// allowOpen = false;
		// setDelay = true;
		// maxOpenTime = 30;
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

	public void drawBoundingBox(GraphicsContext gc) {

		if (Settings.DEBUG_MODE) {
			this.imageView.setVisible(false);
			gc.setStroke(Color.BLACK);
			// top
			gc.strokeRect(x + 40, y + 55, width - 60, height * 0.24);
			// left
			gc.strokeRect(x + 20, y + 65, 20, height * 0.34);
			// right
			gc.strokeRect(x + width - 20, y + 65, 20, height * 0.34);
			// bottom
			gc.strokeRect(x + 40, y + height / 2 + 15, width - 60, height * 0.20);
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

		return new Rectangle2D(x - radius / 2, y - radius / 2, radius, radius);
	}

	public Bounds getCollisionBounds() {

		return this.headBounds.getBoundsInParent();
	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {

		this.collision = collision;

	}

	public void die() {
		KILL_THE_RODENT = true;
		blurOut();
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

	public SnakeOneMouth getMouth() {
		return mouth;
	}

	public void checkRemovability() {

	}

	public boolean isNotMovingFast() {
		return false;
	}

	public void setSpeedBump(boolean b) {

	}

	public void setTail(SnakeOneTail tail) {
		this.tail = tail;
	}

	public SnakeOneTail getTail() {
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
}
