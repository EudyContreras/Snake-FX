package com.SnakeGame.Core;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class SlitherSection extends SlitherSectionMain{
	SlitherSnake snake;
	GameSectionManager3 sectManager;

	public SlitherSection(SlitherSnake snake, SnakeGame game, Pane layer, Node node, float x, float y, GameObjectID id, PlayerMovement Direction, int numericID) {
		super(game, layer, node, id);
		this.snake = snake;
		this.numericID = numericID;
		this.sectManager = game.getSectionManager3();
		if (this.numericID <= 0) {
			if (Direction == PlayerMovement.MOVE_UP) {
				snake.setCollision(false);
				this.y = (float) (y + this.circle.getRadius());
				this.x = x;
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
			} else if (Direction == PlayerMovement.MOVE_DOWN) {
				snake.setCollision(false);
				this.y = (float) (y - this.circle.getRadius());
				this.x = x;
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
			} else if (Direction == PlayerMovement.MOVE_LEFT) {
				snake.setCollision(false);
				this.x = (float) (x + this.circle.getRadius());
				this.y = y;
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
			} else if (Direction == PlayerMovement.MOVE_RIGHT) {
				snake.setCollision(false);
				this.x = (float) (x - this.circle.getRadius());
				this.y = y;
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
			}
		} else if (this.numericID > 0) {
			for(int i = sectManager.getSectionList().size()-1; i>=0; i--){
				SlitherSectionMain previousSect = sectManager.getSectionList().get(i);
				if(previousSect.getNumericID() == this.numericID-1){
					switch(previousSect.getLastDirection()){
					case MOVE_UP:
						setLastDirection(PlayerMovement.MOVE_UP);
						snake.setCollision(false);
						this.y = (float) (previousSect.getY() + this.circle.getRadius());
						this.x = previousSect.getX();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						break;
					case MOVE_DOWN:
						setLastDirection(PlayerMovement.MOVE_DOWN);
						snake.setCollision(false);
						this.y = (float) (previousSect.getY() - this.circle.getRadius());
						this.x = previousSect.getX();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						break;
					case MOVE_LEFT:
						setLastDirection(PlayerMovement.MOVE_LEFT);
						snake.setCollision(false);
						this.x = (float) (previousSect.getX() + this.circle.getRadius());
						this.y = previousSect.getY();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						break;
					case MOVE_RIGHT:
						setLastDirection(PlayerMovement.MOVE_RIGHT);
						snake.setCollision(false);
						this.x = (float) (previousSect.getX() - this.circle.getRadius());
						this.y = previousSect.getY();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						break;
					case STANDING_STILL:
						break;

					}
				}
			}
		}
//		game.getloader().spawnSnakeFood();
	}

	public void move(){
		super.move();
		if(lastPosition.size()>0){
		if(x == lastPosition.get(0).getX() && y == lastPosition.get(0).getY()){
			removeLatestLocation();
			if(lastDirection.get(0) == PlayerMovement.MOVE_UP){
				setLastDirection(PlayerMovement.MOVE_UP);
	    		removeLatestDirection();
				velX = 0;
	    		velY = -Settings.SNAKE_SPEED2;
	    		sectManager.addNewCoordinates(new Point2D(x,y),PlayerMovement.MOVE_UP, this.numericID+1);
			}
			else if(lastDirection.get(0) == PlayerMovement.MOVE_DOWN){
				setLastDirection(PlayerMovement.MOVE_DOWN);
	    		removeLatestDirection();
				velX = 0;
	    		velY = Settings.SNAKE_SPEED2;
	       		sectManager.addNewCoordinates(new Point2D(x,y),PlayerMovement.MOVE_DOWN, this.numericID+1);
			}
			else if(lastDirection.get(0) == PlayerMovement.MOVE_LEFT){
				setLastDirection(PlayerMovement.MOVE_LEFT);
	    		removeLatestDirection();
				velY = 0;
	    		velX = -Settings.SNAKE_SPEED2;
	       		sectManager.addNewCoordinates(new Point2D(x,y),PlayerMovement.MOVE_LEFT, this.numericID+1);
			}
			else if(lastDirection.get(0) == PlayerMovement.MOVE_RIGHT){
				setLastDirection(PlayerMovement.MOVE_RIGHT);
	    		removeLatestDirection();
				velY = 0;
	    		velX = Settings.SNAKE_SPEED2;
	       		sectManager.addNewCoordinates(new Point2D(x,y),PlayerMovement.MOVE_RIGHT, this.numericID+1);
			}

		}
		}
	}

	public void checkRemovability() {

	}

	public void checkCollision() {

	}

}
