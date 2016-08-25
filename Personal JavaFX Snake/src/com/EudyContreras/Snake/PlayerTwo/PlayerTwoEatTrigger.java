package com.EudyContreras.Snake.PlayerTwo;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameObjectID;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayerTwoEatTrigger extends AbstractObject {
	private GameManager game;
	private PlayerTwo snake;

	public PlayerTwoEatTrigger(PlayerTwoHead head, PlayerTwo snake, GameManager game, Pane layer, Circle node, double x, double y,
			GameObjectID id, PlayerMovement Direction) {
		super(game, layer, node, x, y, id);
		this.snake = snake;
		this.game = game;
		this.y = (float) (y + this.circle.getRadius() * 3);
		this.x = x;
		this.velX = snake.getVelX();
		this.velY = snake.getVelY();
		if (GameSettings.DEBUG_MODE) {
			this.circle.setStroke(Color.WHITE);
			this.circle.setStrokeWidth(3);
		}

	}

	public void move() {
		super.move();
		this.circle.setRadius(GameSettings.PLAYER_TWO_SIZE * 0.8);
		if (snake.getCurrentDirection() == PlayerMovement.MOVE_UP) {
			this.y = (float) (snake.getY() - this.circle.getRadius() * 3);
			this.x = snake.getX();
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_DOWN) {
			this.y = (float) (snake.getY() + this.circle.getRadius() * 3);
			this.x = snake.getX();
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_LEFT) {
			this.x = (float) (snake.getX() - this.circle.getRadius() * 3);
			this.y = snake.getY();
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_RIGHT) {
			this.x = (float) (snake.getX() + this.circle.getRadius() * 3);
			this.y = snake.getY();
		}
	}

	public void checkCollision() {
		for (int i = 0; i < game.getGameObjectController().getObsFruitList().size(); i++) {
			AbstractObject tempObject = game.getGameObjectController().getObsFruitList().get(i);
			if (tempObject.getId() == GameObjectID.Fruit) {
				if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
					if (PlayerTwo.MOUTH_CLOSE && GameSettings.ALLOW_AUTOMATIC_EATING || GameSettings.ALLOW_AI_CONTROLL) {
						snake.openMouth();
						break;
					}
				}
			}
		}
	}
}
