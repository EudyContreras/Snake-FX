package com.SnakeGame.Core;

import com.SnakeGame.ObjectIDs.GameObjectID;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class EatTrigger2 extends GameObject{
	int index;
	int counter = 0;
	boolean stop = false;
	SnakeGame game;
	Player2 snake;
	SnakeTwoSectionManager sectManager;
	GameObjectManager gom;


	public EatTrigger2(Player2 snake, SnakeGame game, Pane layer, Circle node, double x, double y, GameObjectID id, PlayerMovement Direction) {
		super(game, layer, node, id);
		this.snake = snake;
		this.game = game;
		this.gom = game.getObjectManager();
		this.sectManager = game.getSectionManager2();
		if (Direction == PlayerMovement.MOVE_UP) {
			this.y = (float) (y - this.circle.getRadius()*3);
			this.x = x;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_DOWN) {
			this.y = (float) (y + this.circle.getRadius()*3);
			this.x = x;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_LEFT) {
			this.x = (float) (x - this.circle.getRadius()*3);
			this.y = y;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_RIGHT) {
			this.x = (float) (x + this.circle.getRadius()*3);
			this.y = y;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.STANDING_STILL) {
			this.y = (float) (y + this.circle.getRadius()*3);
			this.x = x;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		}
		if(Settings.DEBUG_MODE){
			this.circle.setStroke(Color.WHITE);
			this.circle.setStrokeWidth(3);
		}

	}
	public void move(){
		if(Player2.killTheSnake == false){
			this.index = sectManager.getSectionList().size()-1;
		}
		super.move();
			if(snake.getCurrentDirection() == PlayerMovement.MOVE_UP){
				this.y = (float) (snake.getY()- this.circle.getRadius()*3);
				this.x = snake.getX();
			}
			else if(snake.getCurrentDirection() == PlayerMovement.MOVE_DOWN){
				this.y = (float) (snake.getY()+ this.circle.getRadius()*3);
				this.x = snake.getX();
			}
			else if(snake.getCurrentDirection() == PlayerMovement.MOVE_LEFT){
				this.x = (float) (snake.getX()- this.circle.getRadius()*3);
				this.y = snake.getY();
			}
			else if(snake.getCurrentDirection()  == PlayerMovement.MOVE_RIGHT){
				this.x = (float) (snake.getX()+ this.circle.getRadius()*3);
				this.y = snake.getY();
			}
	}

	public void checkCollision() {
		for (int i = 0; i < gom.getObjectList().size(); i++) {
			GameObject tempObject = gom.getObjectList().get(i);
			if (tempObject.getId() == GameObjectID.Fruit) {
				if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
					if (Player2.MOUTH_CLOSE && Settings.AUTOMATIC_EATING) {
						snake.openMouth();
						break;
					}
				}
			}
		}
	}
	public void checkRemovability() {
	}
}
