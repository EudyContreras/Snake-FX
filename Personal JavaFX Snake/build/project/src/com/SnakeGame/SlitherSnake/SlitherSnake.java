package com.SnakeGame.SlitherSnake;

import com.SnakeGame.AbstractModels.AbstractObject;
import com.SnakeGame.AbstractModels.AbstractTile;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.HudElements.ScoreKeeper;
import com.SnakeGame.IDenums.GameLevelObjectID;
import com.SnakeGame.IDenums.GameObjectID;
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

public class SlitherSnake extends SlitherMain {

	double minX;
	double maxX;
	double minY;
	double maxY;
	double clearUp;
	double fade = 0.0;
	double fadeAway = 1.0f;
	double bodyTrigger;
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
	public boolean rotateLeft = false;
	public boolean rotateRight = false;
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
	boolean allowOpen = true;
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
	boolean allowMovement = true;
	public boolean thrust = false;
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
	int snakeLength;
	float speed;
	float rotation = 0;
	float glowLevel = 0;
	float angle;
	float rChange;
	float xChange;
	float yChange;
	float amountOfBlur = 2.0f;
	SnakeGame game;
	Rectangle fadeRect = new Rectangle(0, 0, Settings.WIDTH, Settings.HEIGHT);
	ImagePattern pattern = new ImagePattern(GameImageBank.snakeOneEating);
	ImagePattern pattern2 = new ImagePattern(GameImageBank.snakeOneBlinking);
	Circle headBounds;
	Circle skull;
	SlitherManager gom;
	Animation anim;
	SlitherSectionMain thisPart;
	SlitherSectionMain partBefore;
	SlitherSectionMain snakeTail;
	SlitherSectionManager sectManager;
	SlitherTail tail;
	Rectangle bounds;
	ScreenOverlay overlay;
	SlitherSection neighbor;
	PlayerMovement direction;
	public static float NUMERIC_ID = 0;
	public static float ROTATION_OFFSET = 1;;
	public static Boolean killSlither = false;
	public static Boolean levelComplete = false;
	public static Boolean stopMovement = false;
	public static Boolean MOUTH_OPEN = false;
	public static Boolean MOUTH_CLOSE = true;
	public static Boolean keepMoving = true;

	public SlitherSnake(SnakeGame game, Pane layer, Node node, float x, float y, float r, float velX, float velY,
			float velR, double health, double damage, float speed, GameObjectID id, SlitherManager gom) {
		super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
		this.gom = gom;
		this.speed = speed;
		this.game = game;
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
		this.anim = new Animation();
		this.sectManager = game.getSectManagerThree();
		this.velX = Settings.SLITHER_SPEED;
		this.velY = Settings.SLITHER_SPEED;
		this.speed = 0.7f;
		addbaseSections();
		loadImages();
	}

	public void loadImages() {
		anim.addScene(GameImageBank.snakeOneHead, 4000);
		anim.addScene(GameImageBank.snakeOneBlinking, 250);
		setAnimation(anim);
	}

	public void setAnim(ImagePattern scene) {
		this.circle.setFill(scene);
	}

	public void spawnBody() {
		addbaseSections();
		keepMoving = false;
	}

	public void updateUI() {
		overlay.updateEffect();
		super.updateUI();
		if (!killSlither) {
			maxOpenTime--;
			coolDown--;
			if (notEating) {
				this.setAnim(anim.getPattern());
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
			setAnim(pattern2);
		}
		rotateLeft();
		rotateRight();
		accelarate();
	}

	public void move() {
		if (allowMovement) {
			x = (float) (getX() + Math.sin(Math.toRadians(rotation)) * velX * speed);
			y = (float) (getY() + Math.cos(Math.toRadians(rotation)) * velY * speed);
			r = -rotation;
			SlitherSectionMain snakeHead = sectManager.getSectionList().get(0);
			snakeLength = sectManager.getSectionList().size() - 1;
			snakeTail = sectManager.getSectionList().get(snakeLength);

			snakeHead.setX((float) (snakeHead.getX() + Math.sin(Math.toRadians(rotation)) * velX * speed));
			snakeHead.setY((float) (snakeHead.getY() + Math.cos(Math.toRadians(rotation)) * velY * speed));
			snakeHead.setR(-rotation);

			for (int i = 1; i <= snakeLength; i++) {
				partBefore = sectManager.getSectionList().get(i - 1);
				thisPart = sectManager.getSectionList().get(i);

				xChange = partBefore.getX() - thisPart.getX();
				yChange = partBefore.getY() - thisPart.getY();
				rChange = partBefore.getR() - thisPart.getR();
				angle = (float) Math.atan2(yChange, xChange);

				thisPart.setX(partBefore.getX() - (float) Math.cos(angle) * 15);
				thisPart.setY(partBefore.getY() - (float) Math.sin(angle) * 15);
			}
		}
	}

	public void update() {

		if (Settings.DEBUG_MODE) {
			bounds.setX(x - radius / 2 + offsetX);
			bounds.setY(y - radius / 2 + offsetY);
		}
		dirtDelay--;
		if (dirtDelay <= 0) {
			if (keepMoving) {
				displaceDirt(x + width / 2, y + height / 2, 18, 18);
				dirtDelay = 10;
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

	public void rotateLeft() {
		if (rotateLeft) {
			rotation += 5;
		}
	}

	public void rotateRight() {
		if (rotateRight) {
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
			if (velX <= Settings.SLITHER_SPEED) {
				velX = Settings.SLITHER_SPEED;
				velY = Settings.SLITHER_SPEED;
			}
		}
	}

	public void setDirection(float direction) {
		allowMovement = true;
		this.rotation = direction;
	}

	public void moveUp() {
		velY = -Settings.SLITHER_SPEED;
		;
		velX = 0;
	}

	public void moveDown() {
		velY = Settings.SLITHER_SPEED;
		;
		velX = 0;
	}

	public void moveRight() {
		velX = Settings.SLITHER_SPEED;
		;
		velY = 0;
	}

	public void moveLeft() {
		velX = -Settings.SLITHER_SPEED;
		velY = 0;
	}

	public void checkCollision() {
		if (isDead == false) {
			for (int i = 0; i < game.getObjectManager().getObjectList().size(); i++) {
				AbstractObject tempObject = game.getObjectManager().getObjectList().get(i);
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
						if (allowDamage) {
							setCollision(true);
							if (!killSlither) {
								this.overlay.addDistortion(15, 0.2);
								this.overlay.addToneOverlay(Color.RED, 5, 3.0);
							}
							immunity = Settings.IMMUNITY_TIME;
							allowDamage = false;
						}
					}
				}
			}
			for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
				if (tempTile.getId() == GameLevelObjectID.rock) {
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
	}

	public void addSection() {
		for (int i = 0; i < Settings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new SlitherSection(this, game, game.getFourthLayer(),
					new Circle(25, new ImagePattern(GameImageBank.snakeOneSkin)),
					partBefore.getX() - (float) Math.cos(angle) * 20, partBefore.getY() - (float) Math.cos(angle) * 20,
					GameObjectID.SnakeSection, NUMERIC_ID));
			SnakeGame.writeToLog("New section added " + NUMERIC_ID);
			NUMERIC_ID += 1;
			ROTATION_OFFSET += 0.1;
		}
		game.getScoreBoardOne().increaseScore();
		if (ScoreKeeper.APPLE_COUNT > 4)
			game.getGameLoader().spawnSnakeFood();
	}

	public void setScroller() {
		this.imageView.translateXProperty().addListener((v, oldValue, newValue) -> {

			int offSet = newValue.intValue();
			if (offSet > 100 && offSet < game.getLevelLenght() - 1900) {
				game.getGameRoot().setTranslateX(-(offSet - 100));
			}
		});

	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getRotation() {
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

	public void updateAnimation(long timePassed) {
		anim.update(timePassed);
	}

	//
	// public void checkTurns() {
	// if (snakeHead.allowLeftTurn()) {
	// this.allowTurnLeft = true;
	// } else {
	// this.allowTurnLeft = false;
	// }
	// if (snakeHead.allowRightTurn()) {
	// this.allowTurnRight = true;
	// } else {
	// this.allowTurnRight = false;
	// }
	// if (snakeHead.allowUpTurn()) {
	// this.allowTurnUp = true;
	// } else {
	// this.allowTurnUp = false;
	// }
	// if (snakeHead.allowDownTurn()) {
	// this.allowTurnDown = true;
	// } else {
	// this.allowTurnDown = false;
	// }
	// }
	public void addbaseSections() {
		for (int i = 0; i < Settings.SECTIONS_TO_ADD; i++) {
			sectManager.addSection(new SlitherSection(this, game, game.getFourthLayer(),
					new Circle(25, new ImagePattern(GameImageBank.snakeOneSkin)), x, y, GameObjectID.SnakeSection,
					NUMERIC_ID));
			SnakeGame.writeToLog("New section added " + NUMERIC_ID);
			NUMERIC_ID += 1;
		}
	}

	public void displaceDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !killSlither && !levelComplete) {
			for (int i = 0; i < 15; i++) {
				game.getDebrisManager().addObject(new DirtDisplacement(game, GameImageBank.dirt,1, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
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

	public void draw(GraphicsContext gc) {
		// checkBounds();
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
				//game.gameOver(fadeRect);
			}
		}
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
			setAnim(this.pattern);
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
			game.getSeventhLayer().getChildren().add(bounds);

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

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public void die() {
		killSlither = true;
		blurOut();
		game.getHealthBarOne().drainAll();
	}

	public void addBones() {
		isDead = true;
		skull = new Circle(x, y, this.radius * 0.8, new ImagePattern(GameImageBank.snakeSkull));
		skull.setRotate(r);
		game.getFirstLayer().getChildren().add(skull);
		fadeAndEnd();

	}

	public void fadeAndEnd() {
		game.getFadeScreenLayer().getChildren().add(fadeRect);
	}

	// public SnakeOneMouth getMouth(){
	// return mouth;
	// }
	public boolean isNotMovingFast() {
		return false;
	}

	public void setSpeedBump(boolean b) {

	}

	public void setTail(SlitherTail tail) {
		this.tail = tail;
	}

	public SlitherTail getTail() {
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

	public void setNeighbor(SlitherSection slitherSection) {
		this.neighbor = slitherSection;
	}
}