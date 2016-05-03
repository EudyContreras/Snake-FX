package com.SnakeGame.PlayerOne;

import java.util.LinkedList;

import com.SnakeGame.Debris_Particles.DirtDisplacement;
import com.SnakeGame.FrameWork.GameObject;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ObjectIDs.GameObjectID;
import com.SnakeGame.Utilities.Animation;
import com.SnakeGame.Utilities.GameObjectManager;
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

public class OrgPlayer extends OrgGameObject {

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
	public OrgSnakeTail tail;
	public OrgSnakeHead snakeHead;
	public GameObjectManager gom;
	public OrgGameSectionManager sectManager;
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

	public OrgPlayer(SnakeGame game, Pane layer, Node node, float x, float y, float r, float velX, float velY,
			float velR, double health, double damage, double speed, GameObjectID id, GameObjectManager gom) {
		super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
		this.gom = gom;
		this.speed = speed;
		this.game = game;
		this.anim = new Animation();
		this.circle.setVisible(false);
		this.snakeHead = new OrgSnakeHead(this, game, layer,
				new Circle(Settings.SECTION_SIZE * 1.4, new ImagePattern(GameImageBank.snakeHead)), x, y,
				GameObjectID.SnakeMouth, PlayerMovement.MOVE_DOWN);
		this.game.getOrgObjectManager().addObject(snakeHead);
		this.sectManager = game.getOrgSectManager();
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
		if (this.direction == direction) {
			this.direction = direction;
		} else if (!((this.direction == PlayerMovement.MOVE_LEFT && direction == PlayerMovement.MOVE_RIGHT)
				|| (this.direction == PlayerMovement.MOVE_RIGHT && direction == PlayerMovement.MOVE_LEFT)
				|| (this.direction == PlayerMovement.MOVE_UP && direction == PlayerMovement.MOVE_DOWN)
				|| (this.direction == PlayerMovement.MOVE_DOWN && direction == PlayerMovement.MOVE_UP))) {

			if (direction == PlayerMovement.MOVE_UP) {
				moveUp();
				snakeHead.setR(180);
			} else if (direction == PlayerMovement.MOVE_DOWN) {
				moveDown();
				snakeHead.setR(0);
			} else if (direction == PlayerMovement.MOVE_LEFT) {
				moveLeft();
				snakeHead.setR(89);
			} else if (direction == PlayerMovement.MOVE_RIGHT) {
				moveRight();
				snakeHead.setR(-89);
			}
		}

		this.direction = direction;
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
		velY = -Settings.SECTION;
		velX = 0;
		r = 180;
		setCurrentDirection(PlayerMovement.MOVE_UP);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, 0);
		}
	}

	private void moveDown() {
		velY = Settings.SECTION;
		velX = 0;
		r = 0;
		setCurrentDirection(PlayerMovement.MOVE_DOWN);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, 0);
		}
	}

	private void moveRight() {
		velX = Settings.SECTION;
		velY = 0;
		r = -89;
		setCurrentDirection(PlayerMovement.MOVE_RIGHT);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, 0);
		}
	}

	private void moveLeft() {
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
		if (isDead == false) {
			for (int i = 0; i < gom.getObjectList().size(); i++) {
				GameObject tempObject = gom.getObjectList().get(i);
				if (tempObject.getId() == GameObjectID.Fruit) {
					if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
						addSection();
						tempObject.blowUp();
						tempObject.remove();
						break;
					}
				}
			}
		}
	}

	public void addbaseSections() {
		for (int i = 0; i < Settings.SECTIONS_TO_ADD + 1; i++) {
			sectManager.addSection(new OrgSnakeSection(this, game, layer,
					new Circle(Settings.SECTION_SIZE, new ImagePattern(GameImageBank.snakeBody)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			NUMERIC_ID++;
		}
	}

	public void addSection() {
		for (int i = 0; i < Settings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new OrgSnakeSection(this, game, layer,
					new Circle(Settings.SECTION_SIZE, new ImagePattern(GameImageBank.snakeBody)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
			NUMERIC_ID++;
		}
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
		isDead = true;
		game.getPlayfieldLayer().getChildren().remove(this.imageView);
		this.imageView.setVisible(false);
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

	public void setTail(OrgSnakeTail tail) {
		this.tail = tail;

	}

	public OrgSnakeTail getTail() {
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