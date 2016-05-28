package com.SnakeGame.PlayerOne;

import com.SnakeGame.AbstractModels.AbstractObject;
import com.SnakeGame.AbstractModels.AbstractSection;
import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.FrameWork.ObjectManager;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.IDenums.GameObjectID;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayerOneFangs extends AbstractObject {
	private int index;
	private int counter = 0;
	private boolean stop = false;
	private float offsetX = 0;
	private float offsetY = 0;
	private GameManager game;
	private PlayerOne snake;
	private PlayerOneSectionManager sectManager;
	private PlayerOneHead snakeHead;
	private ObjectManager gom;

	public PlayerOneFangs(PlayerOneHead snakeHead, PlayerOne snake, GameManager game, Pane layer, Circle node, double x,
			double y, GameObjectID id, PlayerMovement Direction) {
		super(game, layer, node, y, y, id);
		this.snakeHead = snakeHead;
		this.snake = snake;
		this.game = game;
		this.gom = game.getObjectManager();
		this.sectManager = game.getSectManagerOne();
		if (Direction == PlayerMovement.MOVE_UP) {
			this.y = (float) (y - this.circle.getRadius() * 3);
			this.x = x;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_DOWN) {
			this.y = (float) (y + this.circle.getRadius() * 3);
			this.x = x;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_LEFT) {
			this.x = (float) (x - this.circle.getRadius() * 3);
			this.y = y;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_RIGHT) {
			this.x = (float) (x + this.circle.getRadius() * 3);
			this.y = y;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.STANDING_STILL) {
			this.y = (float) (y + this.circle.getRadius() * 3);
			this.x = x;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		}
		if (GameSettings.DEBUG_MODE) {
			this.circle.setStroke(Color.WHITE);
			this.circle.setStrokeWidth(3);
		}
		this.game.getGameLoader().spawnSnakeFood();
		this.game.getGameLoader().spawnSnakeFood();
	}

	public void move() {
		if (PlayerOne.DEAD == false) {
			this.index = sectManager.getSectionList().size() - 1;
		}
		checkOffset();
		x = (float) (snakeHead.getX() + offsetX);
		y = (float) (snakeHead.getY() + offsetY);

	}

	public void checkOffset() {
		if (snake.getCurrentDirection()== PlayerMovement.MOVE_UP) {
			this.offsetY = -20;
			this.offsetX = 0;
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_DOWN) {
			this.offsetY = 20;
			this.offsetX = 0;
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_LEFT) {
			this.offsetX = -20;
			this.offsetY = 0;
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_RIGHT) {
			this.offsetX = 20;
			this.offsetY = 0;
		}
	}

	public boolean isApproximate(float tail_X, double sect_X, float tail_Y, double sect_Y) {
		double distance = Math.sqrt((tail_X - sect_X) * (tail_X - sect_X) + (tail_Y - sect_Y) * (tail_Y - sect_Y));
		if (distance > 10) {
			return true;
		}
		return false;
	}

	public void checkRemovability() {
		killTheSnake();
	}

	public void checkCollision() {
		if (PlayerOne.DEAD == false) {
			for (int i = 0; i < gom.getObjectList().size(); i++) {
				AbstractObject tempObject = gom.getObjectList().get(i);
				if (tempObject.getId() == GameObjectID.Fruit) {
					if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
						if (PlayerOne.MOUTH_OPEN) {
							snake.addSection();
							snake.closeMouth();
							game.getScoreKeeper().decreaseCount();
							tempObject.blowUp();
							tempObject.remove();
							break;
						}
					}
				}
			}
			if (GameSettings.ALLOW_SELF_COLLISION) {
				for (int i = 0; i < sectManager.getSectionList().size(); i++) {
					AbstractSection tempObject = sectManager.getSectionList().get(i);
					if (tempObject.getId() == GameObjectID.SnakeSection) {
						if (tempObject.getNumericID() > 1) {
							if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
								if (tempObject.getNumericID() != 0 && tempObject.getNumericID() != 1
										&& tempObject.getNumericID() != 2
										&& tempObject.getNumericID() != PlayerOne.NUMERIC_ID
										&& tempObject.getNumericID() != PlayerOne.NUMERIC_ID - 1) {
										snake.die();
								}
							}
						}
					}
				}
			}
		}
	}

	public void killTheSnake() {
		if (PlayerOne.DEAD == true) {
			counter++;
			if (sectManager.getSectionList().size() > 0) {
				if (counter == 5) {
					AbstractSection sectToKill = sectManager.getSectionList().get(index);
					sectToKill.die();
					counter = 0;
					index--;
					if (index <= 0) {
						index = 0;
						if (!stop) {
							snake.getHead().setShowTheSkull(true);
							snake.getHead().addBones();
							stop = true;
						}
					}
				}
			} else {
				if (!stop) {
					index = 0;
					snake.getHead().setShowTheSkull(true);
					snake.getHead().addBones();
					stop = true;
				}
			}
		}
	}

	public Bounds getCollisionBounds() {
		return this.circle.getBoundsInParent();
	}

	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;

	}

	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;

	}
}
