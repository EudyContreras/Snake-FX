package com.SnakeGame.OriginalSnake;

import com.SnakeGame.Core.GameImageBank;
import com.SnakeGame.Core.GameObject;
import com.SnakeGame.Core.GameObjectID;
import com.SnakeGame.Core.GameObjectManager;
import com.SnakeGame.Core.PlayerMovement;
import com.SnakeGame.Core.Settings;
import com.SnakeGame.Core.SnakeGame;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class OrgPlayer extends OrgGameObject {

	double minX;
	double maxX;
	double minY;
	double maxY;
	double speed;
	double clearUp;
	double lightOpacity = 0.8;
	float vibrationUp = 1.0f;
	float vibrationDown = 1.0f;
	double amountOfGlow = 0;
	boolean isDead = false;
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
	int counter = 0;
	int shootingLag;
	int rightBoundary;
	int recoilDuration = 30;
	int shakeDuration = 30;
	int NUMERIC_ID = 0;
	int scroll = 0;
	float glowLevel = 0;
	float amountOfBlur = 2.0f;
	SnakeGame game;
	DropShadow borderGlow = new DropShadow();
	DropShadow borderGlow2 = new DropShadow();
	MotionBlur motionBlur = new MotionBlur();
	Light.Point light = new Light.Point();
	Lighting lighting = new Lighting();
	GameObjectManager gom;
	OrgGameSectionManager sectManager;
	PlayerMovement direction;

	public OrgPlayer(SnakeGame game, Pane layer, Node node, float x, float y, float r, float velX, float velY, float velR,
			double health, double damage, double speed, GameObjectID id, GameObjectManager gom) {
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
		this.direction = PlayerMovement.STANDING_STILL;
		this.sectManager = game.getOrgSectManager();
		if (Settings.ADD_LIGHTING)
			this.motionBlur.setInput(lighting);
	}

	public void updateUI() {
		super.updateUI();
	}

	public void move() {
		super.move();
	}

	public void setDirection(PlayerMovement direction) {
		if (this.direction == direction) {
			this.direction = direction;
		} else if (!((this.direction == PlayerMovement.MOVE_LEFT && direction == PlayerMovement.MOVE_RIGHT)
				|| (this.direction == PlayerMovement.MOVE_RIGHT && direction == PlayerMovement.MOVE_LEFT)
				|| (this.direction == PlayerMovement.MOVE_UP && direction == PlayerMovement.MOVE_DOWN)
				|| (this.direction == PlayerMovement.MOVE_DOWN && direction == PlayerMovement.MOVE_UP))) {

			if (direction == PlayerMovement.MOVE_UP) {
				moveUp();
			} else if (direction == PlayerMovement.MOVE_DOWN) {
				moveDown();
			} else if (direction == PlayerMovement.MOVE_LEFT) {
				moveLeft();
			} else if (direction == PlayerMovement.MOVE_RIGHT) {
				moveRight();
			}
		}

		this.direction = direction;
	}

	private void moveUp() {
		velY = -Settings.SECTION;
		velX = 0;
		setCurrentDirection(PlayerMovement.MOVE_UP);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, 0);
		}
	}

	private void moveDown() {
		velY = Settings.SECTION;
		velX = 0;
		setCurrentDirection(PlayerMovement.MOVE_DOWN);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, 0);
		}
	}

	private void moveRight() {
		velX = Settings.SECTION;
		velY = 0;
		setCurrentDirection(PlayerMovement.MOVE_RIGHT);
		if (sectManager.getSectionList().size() > 0) {
			sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, 0);
		}
	}

	private void moveLeft() {
		velX = -Settings.SECTION;
		velY = 0;
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
	public void addSection(){
        for(int i = 0; i<Settings.SECTIONS_TO_ADD; i++){
        	sectManager.addSection(new OrgSnakeSection(this, game, layer, new Circle(40, new ImagePattern(GameImageBank.snakeBody)), x, y,
					GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
        NUMERIC_ID++;
        }
//        game.getScoreBoard().increaseScore();
//        if(ScoreKeeper.APPLE_COUNT>4)
        game.getloader().spawnSnakeFood();
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
}