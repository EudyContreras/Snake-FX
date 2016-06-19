package com.EudyContreras.Snake.ClassicSnake;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.Controllers.GameObjectController;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameObjectID;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ClassicSnakeFangs extends AbstractObject {
	private int index;
	private int counter = 0;
	private boolean stop = false;
	private double offsetX = 0;
	private double offsetY = 0;
	private GameManager game;
	private ClassicSnake snake;
	private ClassicSnakeSectionManager sectManager;
	private GameObjectController gom;

	public ClassicSnakeFangs( ClassicSnake snake, GameManager game, Pane layer, Circle node, double x,
			double y, GameObjectID id, PlayerMovement Direction) {
		super(game, layer, node, y, y, id);
		this.snake = snake;
		this.game = game;
		this.gom = game.getGameObjectController();
		this.sectManager = game.getSectManagerThree();
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
		if (ClassicSnake.DEAD == false) {
			this.index = sectManager.getSectionList().size() - 1;
		}
		checkOffset();
		x = (float) (snake.getX() + offsetX);
		y = (float) (snake.getY() + offsetY);

	}
	public void logicUpdate() {
		killTheSnake();
	}
	public void checkOffset() {
		if (snake.getCurrentDirection()== PlayerMovement.MOVE_UP) {
			this.offsetY = GameManager.ScaleY(-25);
			this.offsetX = 0;
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_DOWN) {
			this.offsetY = GameManager.ScaleY(25);
			this.offsetX = 0;
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_LEFT) {
			this.offsetX = GameManager.ScaleX(-25);
			this.offsetY = 0;
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_RIGHT) {
			this.offsetX = GameManager.ScaleX(25);
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
		if (ClassicSnake.DEAD == false) {
			for (int i = 0; i < gom.getFruitList().size(); i++) {
				AbstractObject tempObject = gom.getFruitList().get(i);
				if (tempObject.getId() == GameObjectID.Fruit) {
					if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
						snake.addSection();
						snake.closeMouth();
						game.getScoreKeeper().decreaseCount();
						tempObject.blowUpAlt();
						tempObject.remove();
						break;
					}
					if (snake.getBounds().intersects(tempObject.getBounds())) {
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
		if (ClassicSnake.DEAD == true) {
			counter++;
			if (sectManager.getSectionList().size() > 0) {
				if (counter >= 7) {
					AbstractSection sectToKill = sectManager.getSectionList().get(index);
					sectToKill.die();
					counter = 0;
					index--;
					if (index <= 0) {
						index = 0;
						if (!stop) {
							stop = true;
						}
					}
				}
			} else {
				if (!stop) {
					index = 0;
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
