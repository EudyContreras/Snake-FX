package com.SnakeGame.Core;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class SlitherSection extends SlitherSectionMain{
	SlitherSnake snake;
	GameSlitherSectionManager sectManager;

	public SlitherSection(SlitherSnake snake, SnakeGame game, Pane layer, Node node, float x, float y, GameObjectID id, PlayerMovement Direction, int numericID) {
		super(game, layer, node, id);
		this.snake = snake;
		this.numericID = numericID;
		this.sectManager = game.getSectionManager3();
		this.x = x;
		this.y = y;

	}

	public void move(){
//		x = x + Settings.SNAKE_SPEED2;
//		y = y + Settings.SNAKE_SPEED2;
	}

	public void checkRemovability() {

	}

	public void checkCollision() {

	}

}
