package com.SnakeGame.PlayerOne;

import com.SnakeGame.AbstractModels.AbstractObject;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.IDenums.GameObjectID;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class PlayerOneTail extends AbstractObject {
	private PlayerOneSection sectionToFollow;

	public PlayerOneTail(PlayerOneSection snake, SnakeGame game, Pane layer, Node node, double x, double y, GameObjectID id,
			PlayerMovement Direction) {
		super(game, layer, node, y, y, id);
		this.velX = snake.getVelX();
		this.velY = snake.getVelY();
		this.circle.setVisible(false);
		this.r = snake.getR();

	}

	public void move() {
		x = sectionToFollow.getX();
		y = sectionToFollow.getY();
		r = sectionToFollow.getR();
	}

	public void setWhoToFollow(PlayerOneSection snakeSection) {
		sectionToFollow = snakeSection;
	}

}
