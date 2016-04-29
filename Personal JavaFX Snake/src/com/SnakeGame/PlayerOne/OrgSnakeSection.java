package com.SnakeGame.PlayerOne;

import com.SnakeGame.Core.GameImageBank;
import com.SnakeGame.Core.PlayerMovement;
import com.SnakeGame.Core.Settings;
import com.SnakeGame.Core.SnakeGame;
import com.SnakeGame.ObjectIDs.GameObjectID;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class OrgSnakeSection extends OrgSectionMain {
	OrgPlayer snake;
	OrgSnakeTail tail;
	OrgGameSectionManager sectManager;

	public OrgSnakeSection(OrgPlayer snake, SnakeGame game, Pane layer, Node node, float x, float y, GameObjectID id,
			PlayerMovement Direction, int numericID) {
		super(game, layer, node, id);
		this.snake = snake;
		this.numericID = numericID;
		this.sectManager = game.getOrgSectManager();
		if (this.numericID <= 0) {
			if (Direction == PlayerMovement.MOVE_UP) {
				this.setLastDirection(Direction);
				this.y = (float) (y + this.circle.getRadius() * Settings.SECTION_DISTANCE);
				this.x = x;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				this.tail = new OrgSnakeTail(this, game, game.getSnakeHeadLayer(),
						new Circle(Settings.SECTION_SIZE - 5, new ImagePattern(GameImageBank.snakeTail)), this.x,
						this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				this.tail.setWhoToFollow(this);
				game.getOrgObjectManager().addObject(tail);
				snake.setTail(tail);
			} else if (Direction == PlayerMovement.MOVE_DOWN) {
				this.setLastDirection(Direction);
				this.y = (float) (y - this.circle.getRadius() * Settings.SECTION_DISTANCE);
				this.x = x;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				this.tail = new OrgSnakeTail(this, game, game.getSnakeHeadLayer(),
						new Circle(Settings.SECTION_SIZE - 5, new ImagePattern(GameImageBank.snakeTail)), this.x,
						this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				this.tail.setWhoToFollow(this);
				game.getOrgObjectManager().addObject(tail);
				snake.setTail(tail);
			} else if (Direction == PlayerMovement.MOVE_LEFT) {
				this.setLastDirection(Direction);
				this.x = (float) (x + this.circle.getRadius() * Settings.SECTION_DISTANCE);
				this.y = y;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				this.tail = new OrgSnakeTail(this, game, game.getSnakeHeadLayer(),
						new Circle(Settings.SECTION_SIZE - 5, new ImagePattern(GameImageBank.snakeTail)), this.x,
						this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				this.tail.setWhoToFollow(this);
				game.getOrgObjectManager().addObject(tail);
				snake.setTail(tail);
			} else if (Direction == PlayerMovement.MOVE_RIGHT) {
				this.setLastDirection(Direction);
				this.x = (float) (x - this.circle.getRadius() * Settings.SECTION_DISTANCE);
				this.y = y;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				this.tail = new OrgSnakeTail(this, game, game.getSnakeHeadLayer(),
						new Circle(Settings.SECTION_SIZE - 5, new ImagePattern(GameImageBank.snakeTail)), this.x,
						this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				this.tail.setWhoToFollow(this);
				game.getOrgObjectManager().addObject(tail);
				snake.setTail(tail);
			} else if (Direction == PlayerMovement.STANDING_STILL) {
				this.setLastDirection(Direction);
				this.x = (float) (x - this.circle.getRadius() * Settings.SECTION_DISTANCE);
				this.y = y;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				this.tail = new OrgSnakeTail(this, game, game.getSnakeHeadLayer(),
						new Circle(Settings.SECTION_SIZE - 5, new ImagePattern(GameImageBank.snakeTail)), this.x,
						this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				this.tail.setWhoToFollow(this);
				game.getOrgObjectManager().addObject(tail);
				snake.setTail(tail);
			}
		} else if (this.numericID > 0) {
			for (int i = sectManager.getSectionList().size() - 1; i >= 0; i--) {
				OrgSectionMain previousSect = sectManager.getSectionList().get(i);
				if (previousSect.getNumericID() == this.numericID - 1) {
					switch (previousSect.getLastDirection()) {
					case MOVE_UP:
						setLastDirection(PlayerMovement.MOVE_UP);
						this.y = (float) (previousSect.getY() + this.circle.getRadius() * Settings.SECTION_DISTANCE);
						this.x = previousSect.getX();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						snake.getTail().setWhoToFollow(this);
						break;
					case MOVE_DOWN:
						setLastDirection(PlayerMovement.MOVE_DOWN);
						this.y = (float) (previousSect.getY() - this.circle.getRadius() * Settings.SECTION_DISTANCE);
						this.x = previousSect.getX();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						snake.getTail().setWhoToFollow(this);
						break;
					case MOVE_LEFT:
						setLastDirection(PlayerMovement.MOVE_LEFT);
						this.x = (float) (previousSect.getX() + this.circle.getRadius() * Settings.SECTION_DISTANCE);
						this.y = previousSect.getY();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						snake.getTail().setWhoToFollow(this);
						break;
					case MOVE_RIGHT:
						setLastDirection(PlayerMovement.MOVE_RIGHT);
						this.x = (float) (previousSect.getX() - this.circle.getRadius() * Settings.SECTION_DISTANCE);
						this.y = previousSect.getY();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						snake.getTail().setWhoToFollow(this);
						break;
					case STANDING_STILL:
						setLastDirection(PlayerMovement.STANDING_STILL);
						this.x = (float) (previousSect.getX() - this.circle.getRadius() * Settings.SECTION_DISTANCE);
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
		hideLast();
		if (OrgPlayer.DEAD == false && OrgPlayer.LEVEL_COMPLETED == false && OrgPlayer.KEEP_MOVING)
			super.move();
		if (lastPosition.size() > 0) {
			if (x == lastPosition.get(0).getX() && y == lastPosition.get(0).getY()) {
				removeLatestLocation();
				if (lastDirection.get(0) == PlayerMovement.MOVE_UP) {
					setLastDirection(PlayerMovement.MOVE_UP);
					removeLatestDirection();
					velX = 0;
					velY = -Settings.SECTION;
					r = 180;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_DOWN) {
					setLastDirection(PlayerMovement.MOVE_DOWN);
					removeLatestDirection();
					velX = 0;
					velY = Settings.SECTION;
					r = 0;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_LEFT) {
					setLastDirection(PlayerMovement.MOVE_LEFT);
					removeLatestDirection();
					velY = 0;
					velX = -Settings.SECTION;
					r = 89;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_LEFT, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_RIGHT) {
					setLastDirection(PlayerMovement.MOVE_RIGHT);
					removeLatestDirection();
					velY = 0;
					velX = Settings.SECTION;
					r = -89;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, this.numericID + 1);
				}

			}
		}
	}

	public void hideLast() {
		if (this.numericID == OrgPlayer.NUMERIC_ID - 1) {
			this.circle.setVisible(false);
		} else if (this.numericID != OrgPlayer.NUMERIC_ID - 1) {
			this.circle.setVisible(true);
		}
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
}
