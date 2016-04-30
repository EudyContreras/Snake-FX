package com.SnakeGame.PlayerOne;

import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ObjectIDs.GameObjectID;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class OrgSnakeTail extends OrgGameObject {
	OrgSnakeSection snakeSect;
	OrgSnakeSection sectionToFollow;
	OrgGameSectionManager sectManager;

	public OrgSnakeTail(OrgSnakeSection snake, SnakeGame game, Pane layer, Node node, float x, float y, GameObjectID id,
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

	public void setWhoToFollow(OrgSnakeSection snakeSection) {
		sectionToFollow = snakeSection;
	}

}
