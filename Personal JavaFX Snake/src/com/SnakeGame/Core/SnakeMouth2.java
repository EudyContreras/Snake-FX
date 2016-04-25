package com.SnakeGame.Core;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SnakeMouth2 extends GameObject{
	int index ;
	int counter = 0;
	boolean stop = false;
	double offsetX;
	double offsetY;
	SnakeGame game;
	Player2 snake;
	SnakeTwoSectionManager sectManager;
	GameObjectManager gom;


	public SnakeMouth2(Player2 snake, SnakeGame game, Pane layer, Circle node, double x, double y, GameObjectID id, PlayerMovement Direction) {
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
			this.circle.setStroke(Color.BLACK);
			this.circle.setFill(Color.BLACK);
		}
		this.game.getloader().spawnSnakeFood();
		this.game.getloader().spawnSnakeFood();
	}
	public void move(){
		if(Player2.killTheSnake == false){
			this.index = sectManager.getSectionList().size()-1;
		}
		x = snake.getCenterX()+offsetX;
        y = snake.getCenterY()+offsetY;
	}
	public boolean isApproximate(float tail_X, double sect_X, float tail_Y, double sect_Y){
		double distance = Math.sqrt((tail_X-sect_X)*(tail_X-sect_X) + (tail_Y-sect_Y)*(tail_Y-sect_Y));
		if(distance>10){
			return true;
		}
		return false;
	}
	public void checkRemovability() {
		killTheSnake();
	}
	public void checkCollision() {
		for (int i = 0; i < sectManager.getSectionList().size(); i++) {
			SectionMain tempObject = sectManager.getSectionList().get(i);
			if (tempObject.getId() == GameObjectID.SnakeSection) {
				if (tempObject.getNumericID() > 1) {
					if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
						if(tempObject.numericID != 0 && tempObject.numericID != 1 && tempObject.numericID != 2){
							snake.die();
						}
					}
				}
			}
		}
		 for(int i = 0; i<gom.getObjectList().size(); i++){
	    		GameObject tempObject = gom.getObjectList().get(i);
	    		if(tempObject.getId() == GameObjectID.Fruit){
	    			if(getRadialBounds().intersects(tempObject.getRadialBounds())){
	    	 			if(Player2.MOUTH_OPEN){
	    	    		snake.addSection();
	    	    		snake.closeMouth();
	    	    		game.getScoreKeeper().decreaseCount();
	    	    		tempObject.blowUp();
	    	    		tempObject.remove();
	    	    		break;
	    	    		}
	    	 			else{
	    	 				tempObject.bounce(snake,x,y);
	    	 			}
	    			}
	    		}
	        }
		  for (int i = 0; i < game.getloader().tileManager.tile.size(); i++) {
	            Tile tempTile = game.getloader().tileManager.tile.get(i);
	            if (tempTile.getId() == LevelObjectID.fence) {
				if (!Settings.ALLOW_TELEPORT) {
					if (getCollisionBounds().intersects(tempTile.getCollisionBounds())) {
						snake.die();
					}
				}
			}
		 }
	}
	public void killTheSnake(){
		if (Player2.killTheSnake == true) {
			counter++;
			if (sectManager.getSectionList().size() > 0) {
				if (counter == 5) {
					SectionMain sectToKill = sectManager.getSectionList().get(index);
					sectToKill.die();
					counter = 0;
					index--;
					if (index <= 0) {
						index = 0;
						if(!stop){
						snake.showTheSkull = true;
						snake.addBones();
						stop = true;
						}
					}
				}
			}
			else{
				if(!stop){
					index = 0;
					snake.showTheSkull = true;
					snake.addBones();
					stop = true;
					}
			}
		}
	}
  public Bounds getCollisionBounds(){
      return this.circle.getBoundsInParent();
  }
	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;

	}
	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;

	}
}
