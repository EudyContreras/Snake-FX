package com.SnakeGame.PlayerOne;

import com.SnakeGame.AbstractModels.AbstractSection;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.IDenums.GameObjectID;
import com.SnakeGame.IDenums.GameStateID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.Particles.DirtDisplacement;
import com.SnakeGame.Particles.SectionDisintegration;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class PlayerOneSection extends AbstractSection {
	private PlayerMovement direction;
	private double particleLife;
	private double particleSize;
	private double fadeValue = 1.0;
	private boolean fade = false;
	private boolean blowUp = true;
	private int dirtDelay = 10;
	private GameManager game;
	private Circle bones;
	private PlayerOneTail tail;
	private PlayerOneSectionManager sectManager;

	public PlayerOneSection(PlayerOne snake, GameManager game, Pane layer, Node node, double x, double y, GameObjectID id,
			PlayerMovement Direction, int numericID) {
		super(game, layer, node, id);
		this.game = game;
		this.numericID = numericID;
		this.sectManager = game.getSectManagerOne();
		if (this.numericID <= 0) {
			if (Direction == PlayerMovement.MOVE_UP) {
				this.setLastDirection(Direction);
				this.y = (float) (y + this.circle.getRadius() * GameSettings.SECTION_DISTANCE);
				this.x = x;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				this.tail = new PlayerOneTail(this, game, layer,
						new Circle(GameSettings.SECTION_SIZE - 5, new ImagePattern(GameImageBank.snakeTail)), this.x,
						this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				this.tail.setWhoToFollow(this);
				game.getObjectManager().addObject(tail);
				snake.setTail(tail);
				snake.setNeighbor(this);
			} else if (Direction == PlayerMovement.MOVE_DOWN) {
				this.setLastDirection(Direction);
				this.y = (float) (y - this.circle.getRadius() * GameSettings.SECTION_DISTANCE);
				this.x = x;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				this.tail = new PlayerOneTail(this, game, layer,
						new Circle(GameSettings.SECTION_SIZE - 5, new ImagePattern(GameImageBank.snakeTail)), this.x,
						this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				this.tail.setWhoToFollow(this);
				game.getObjectManager().addObject(tail);
				snake.setTail(tail);
				snake.setNeighbor(this);
			} else if (Direction == PlayerMovement.MOVE_LEFT) {
				this.setLastDirection(Direction);
				this.x = (float) (x + this.circle.getRadius() * GameSettings.SECTION_DISTANCE);
				this.y = y;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				this.tail = new PlayerOneTail(this, game, layer,
						new Circle(GameSettings.SECTION_SIZE - 5, new ImagePattern(GameImageBank.snakeTail)), this.x,
						this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				this.tail.setWhoToFollow(this);
				game.getObjectManager().addObject(tail);
				snake.setTail(tail);
				snake.setNeighbor(this);
			} else if (Direction == PlayerMovement.MOVE_RIGHT) {
				this.setLastDirection(Direction);
				this.x = (float) (x - this.circle.getRadius() * GameSettings.SECTION_DISTANCE);
				this.y = y;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				this.tail = new PlayerOneTail(this, game, layer,
						new Circle(GameSettings.SECTION_SIZE - 5, new ImagePattern(GameImageBank.snakeTail)), this.x,
						this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				this.tail.setWhoToFollow(this);
				game.getObjectManager().addObject(tail);
				snake.setTail(tail);
				snake.setNeighbor(this);
			} else if (Direction == PlayerMovement.STANDING_STILL) {
				this.setLastDirection(Direction);
				this.x = (float) (x - this.circle.getRadius() * GameSettings.SECTION_DISTANCE);
				this.y = y;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				this.tail = new PlayerOneTail(this, game, layer,
						new Circle(GameSettings.SECTION_SIZE - 5, new ImagePattern(GameImageBank.snakeTail)), this.x,
						this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				this.tail.setWhoToFollow(this);
				game.getObjectManager().addObject(tail);
				snake.setTail(tail);
				snake.setNeighbor(this);
			}
		} else if (this.numericID > 0) {
			for (int i = sectManager.getSectionList().size() - 1; i >= 0; i--) {
				AbstractSection previousSect = sectManager.getSectionList().get(i);
				if (previousSect.getNumericID() == this.numericID - 1) {
					switch (previousSect.getLastDirection()) {
					case MOVE_UP:
						setLastDirection(PlayerMovement.MOVE_UP);
						this.y = (float) (previousSect.getY() + this.circle.getRadius() * GameSettings.SECTION_DISTANCE);
						this.x = previousSect.getX();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						snake.getTail().setWhoToFollow(this);
						break;
					case MOVE_DOWN:
						setLastDirection(PlayerMovement.MOVE_DOWN);
						this.y = (float) (previousSect.getY() - this.circle.getRadius() * GameSettings.SECTION_DISTANCE);
						this.x = previousSect.getX();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						snake.getTail().setWhoToFollow(this);
						break;
					case MOVE_LEFT:
						setLastDirection(PlayerMovement.MOVE_LEFT);
						this.x = (float) (previousSect.getX() + this.circle.getRadius() * GameSettings.SECTION_DISTANCE);
						this.y = previousSect.getY();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						snake.getTail().setWhoToFollow(this);
						break;
					case MOVE_RIGHT:
						setLastDirection(PlayerMovement.MOVE_RIGHT);
						this.x = (float) (previousSect.getX() - this.circle.getRadius() * GameSettings.SECTION_DISTANCE);
						this.y = previousSect.getY();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						snake.getTail().setWhoToFollow(this);
						break;
					case STANDING_STILL:
						setLastDirection(PlayerMovement.STANDING_STILL);
						this.x = (float) (previousSect.getX() - this.circle.getRadius() * GameSettings.SECTION_DISTANCE);
						this.y = previousSect.getY();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						snake.getTail().setWhoToFollow(this);
						break;

					}
				}
			}
		}
	}

	public void move() {
		checkBounds();
		disguiseLast();
		if (PlayerOne.DEAD == false && PlayerOne.LEVEL_COMPLETED == false && PlayerOne.KEEP_MOVING && game.getStateID()!=GameStateID.GAME_MENU)
			super.move();
		if (lastPosition.size() > 0) {
			if (x == lastPosition.get(0).getX() && y == lastPosition.get(0).getY()) {
				removeLatestLocation();
				if (lastDirection.get(0) == PlayerMovement.MOVE_UP) {
					setLastDirection(PlayerMovement.MOVE_UP);
					removeLatestDirection();
					velX = 0;
					velY = -GameSettings.SNAKE_SPEED;
					r = 180;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_DOWN) {
					setLastDirection(PlayerMovement.MOVE_DOWN);
					removeLatestDirection();
					velX = 0;
					velY = GameSettings.SNAKE_SPEED;
					r = 0;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_LEFT) {
					setLastDirection(PlayerMovement.MOVE_LEFT);
					removeLatestDirection();
					velY = 0;
					velX = -GameSettings.SNAKE_SPEED;
					r = 89;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_LEFT, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_RIGHT) {
					setLastDirection(PlayerMovement.MOVE_RIGHT);
					removeLatestDirection();
					velY = 0;
					velX = GameSettings.SNAKE_SPEED;
					r = -89;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, this.numericID + 1);
				}
			}
		}
		if(GameSettings.ALLOW_DIRT)
		updateDirt();
	}
	public void updateDirt() {
		dirtDelay--;
		if (dirtDelay <= 0) {
			if (PlayerOne.KEEP_MOVING) {
				displaceDirt(x + width / 2, y + height / 2, 18, 18);
				dirtDelay = 10;
			}
		}
	}
	public void displaceDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !PlayerOne.DEAD && !PlayerOne.LEVEL_COMPLETED) {
			for (int i = 0; i <2; i++) {
				game.getDebrisManager().addObject(new DirtDisplacement(game, GameImageBank.dirt,0.5, x, y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
	}
	public void hideLast() {
		if (this.numericID == PlayerOne.NUMERIC_ID - 1) {
			this.circle.setVisible(false);
		} else if (this.numericID != PlayerOne.NUMERIC_ID - 1) {
			this.circle.setVisible(true);
		}
	}

	public void disguiseLast() {
		if (!PlayerOne.DEAD) {
			if (this.numericID == PlayerOne.NUMERIC_ID - 1) {
				this.circle.setFill(GameImageBank.tailImage);
			} else if (this.numericID != PlayerOne.NUMERIC_ID - 1) {
				this.circle.setFill(GameImageBank.snakeOneBody);
			}
		}
		if (fade == true) {
			fadeValue -= 0.01;
			this.circle.setOpacity(fadeValue);
			if (fadeValue <= 0) {
				fadeValue = 0;
			}
		}
	}

	public void checkBounds() {
		if (x < 0 - radius) {
			x = (float) (GameSettings.WIDTH + radius);
		} else if (x > GameSettings.WIDTH + radius) {
			x = (float) (0 - radius);
		} else if (y < 0 - radius) {
			y = (float) (GameSettings.HEIGHT + radius);
		} else if (y > GameSettings.HEIGHT + radius) {
			y = (float) (0 - radius);
		}
	}
	public void loadBones() {
		bones = new Circle(x, y, this.radius * 0.8, new ImagePattern(GameImageBank.snakeBones));
		game.getFirstLayer().getChildren().add(bones);
		bones.setRotate(r-90);
	}

	public void blowUp() {
		if (blowUp == true) {
			for (int i = 0; i < GameSettings.PARTICLE_LIMIT; i++) {
				if (GameSettings.ADD_VARIATION) {
					particleSize = Math.random() * (12 - 5 + 1) + 5;
					particleLife = Math.random() * (2.0 - 1.0 + 1) + 1.5;
				}
				game.getDebrisManager().addParticle(new SectionDisintegration(game, GameImageBank.snakeOneDebris,
						particleLife, particleSize, (double) (x + this.radius / 2), (double) (y + this.radius / 2)));
			}
			blowUp = false;
		}
	}

	public void die() {
		loadBones();
		fade = true;
		blowUp();
	}
	public Rectangle2D getBounds() {

		return new Rectangle2D(x - radius / 2, y - radius / 2, radius, radius);
	}
}
