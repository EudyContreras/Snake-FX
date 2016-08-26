package com.EudyContreras.Snake.PlayerOne;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Controllers.GameObjectController;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayerOneFangs extends AbstractObject {
	private int index;
	private int counter = 0;
	private boolean stop = false;
	private double offsetX = 0;
	private double offsetY = 0;
	private GameManager game;
	private PlayerOne snake;
	private PlayerOneSectionManager sectManager;
	private PlayerOneHead snakeHead;
	private GameObjectController gom;

	public PlayerOneFangs(PlayerOneHead snakeHead, PlayerOne snake, GameManager game, Pane layer, Circle node, float x,
			float y, GameObjectID id, PlayerMovement Direction) {
		super(game, layer, node, y, y, id);
		this.snakeHead = snakeHead;
		this.snake = snake;
		this.game = game;
		this.gom = game.getGameObjectController();
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
	}

	public void move() {
		if (PlayerOne.DEAD == false) {
			this.index = sectManager.getSectionList().size() - 1;
		}
		checkOffset();
		x = (float) (snakeHead.getX() + offsetX);
		y = (float) (snakeHead.getY() + offsetY);
		circle.setRadius(GameSettings.PLAYER_ONE_SIZE * 0.35);


	}
	public void logicUpdate() {
		killTheSnake();
		showGameOver();
	}
	public void checkOffset() {
		if (snake.getCurrentDirection()== PlayerMovement.MOVE_UP) {
			this.offsetY = -this.snakeHead.getRadius()*.65;
			this.offsetX = 0;
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_DOWN) {
			this.offsetY = this.snakeHead.getRadius()*.65;
			this.offsetX = 0;
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_LEFT) {
			this.offsetX = -this.snakeHead.getRadius()*.65;
			this.offsetY = 0;
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_RIGHT) {
			this.offsetX = this.snakeHead.getRadius()*.65;
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

	}

	public void checkCollision() {
		if (PlayerOne.DEAD == false) {
			for (int i = 0; i < gom.getObsFruitList().size(); i++) {
				AbstractObject tempObject = gom.getObsFruitList().get(i);
				if (tempObject.getId() == GameObjectID.Fruit) {
					if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
						if (PlayerOne.MOUTH_OPEN) {
							snake.addSection();
							snake.closeMouth();
							game.getScoreKeeper().decreaseCount();
							tempObject.blowUp();
							tempObject.getPoint();
							tempObject.remove();
							break;
						}
					}
					if (snake.getHead().getBounds().intersects(tempObject.getBounds())) {
						if(!GameSettings.ALLOW_AI_CONTROLL)
						tempObject.bounce(snake, snake.getX(), snake.getY());
						break;
					}
				}
			}
			if (GameSettings.ALLOW_SELF_COLLISION) {
				for (int i = 0; i < sectManager.getSectionList().size(); i++) {
					AbstractSection tempObject = sectManager.getSectionList().get(i);
					if (tempObject.getId() == GameObjectID.SnakeSection) {
						if (tempObject.getNumericID() > 4) {
							if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
								snake.die();
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
				if (counter >= 4) {
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
			}
		}
	}

	private int showCounter = 0;
	private boolean allowGameOver = true;

	public void showGameOver() {
		if (stop && !snake.getManualGameOver()) {
			showCounter++;
			if (showCounter > 60 && !PlayerOne.ALLOW_FADE) {
				if (allowGameOver) {
					allowGameOver = false;
					PlayerOne.ALLOW_FADE = true;
					game.setStateID(GameStateID.GAME_OVER);
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
