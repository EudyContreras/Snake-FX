package com.SnakeGame.PlayerTwo;

import com.SnakeGame.FrameWork.GameObject;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ObjectIDs.GameObjectID;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class PlayerTwoTail extends GameObject {
	PlayerTwoSection snakeSect;
	PlayerTwoSection sectionToFollow;
	PlayerTwoSectionManager sectManager;

	public PlayerTwoTail(PlayerTwoSection snake, SnakeGame game, Pane layer, Node node, float x, float y, GameObjectID id,
			PlayerMovement Direction) {
		super(game, layer, node, y, y, id);
		this.velX = snake.getVelX();
		this.velY = snake.getVelY();
		this.r = snake.getR();

	}

	public void move() {
		x = sectionToFollow.getX();
		y = sectionToFollow.getY();
		r = sectionToFollow.getR();
	}

	public void setWhoToFollow(PlayerTwoSection snakeSection) {
		sectionToFollow = snakeSection;
	}

}