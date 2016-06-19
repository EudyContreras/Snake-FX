package com.EudyContreras.Snake.ClassicSnake;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.Controllers.GameObjectController;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameObjectID;

public class ClassicSnakeFangs extends AbstractObject {
	private int index;
	private int counter = 0;
	private boolean stop = false;
	private GameManager game;
	private ClassicSnake snake;
	private ClassicSnakeSectionManager sectManager;
	private GameObjectController gom;

	public ClassicSnakeFangs( ClassicSnake snake, GameManager game) {
		this.snake = snake;
		this.game = game;
		this.gom = game.getGameObjectController();
		this.sectManager = game.getSectManagerThree();
		this.game.getGameLoader().spawnClassicSnakeFood();
		this.game.getGameLoader().spawnClassicSnakeFood();
	}
	public void updateUI(){

	}
	public void move() {
		if (ClassicSnake.DEAD == false) {
			this.index = sectManager.getSectionList().size() - 1;
		}
	}
	public void logicUpdate() {
		killTheSnake();
		showGameOver();
	}


	public void checkCollision() {
		if (ClassicSnake.DEAD == false) {
			for (int i = 0; i < gom.getFruitList().size(); i++) {
				AbstractObject tempObject = gom.getFruitList().get(i);
				if (tempObject.getId() == GameObjectID.Fruit) {
					if (snake.getRadialBounds().intersects(tempObject.getRadialBounds())) {
						snake.addSection();
						snake.closeMouth();
						game.getScoreKeeper().decreaseCount();
						tempObject.blowUp();
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
							if (snake.getRadialBounds().intersects(tempObject.getRadialBounds())) {
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
				if (counter >= 0) {
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

	private int showCounter = 0;
	private boolean allowGameOver = true;

	public void showGameOver() {
		if (stop) {
			showCounter++;
			if (showCounter > 60) {
				if (allowGameOver) {
					allowGameOver = false;
					ClassicSnake.ALLOW_FADE = true;
				}
			}
		}
	}

}
