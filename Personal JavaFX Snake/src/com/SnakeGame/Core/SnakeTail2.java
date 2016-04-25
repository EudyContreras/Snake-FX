package com.SnakeGame.Core;

import java.util.LinkedList;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class SnakeTail2 extends GameObject{
	SnakeSection2 snakeSect;
	SnakeSection2 sectionToFollow;
	SnakeTwoSectionManager sectManager;
	LinkedList<Point2D> lastLocation = new LinkedList<Point2D>();
	LinkedList<PlayerMovement> lastDirection = new LinkedList<PlayerMovement>();

	public SnakeTail2(SnakeSection2 snake, SnakeGame game, Pane layer, Node node, double x, double y, GameObjectID id, PlayerMovement Direction) {
		super(game, layer, node, id);
		this.snakeSect = snake;
		this.sectManager = game.getSectionManager2();
		if (Direction == PlayerMovement.MOVE_UP) {
			this.y = (double) (y + this.circle.getRadius()*1.5);
			this.x = x;
			this.r = 180;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_DOWN) {
			this.y = (double) (y - this.circle.getRadius()*1.5);
			this.x = x;
			this.r = 0;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_LEFT) {
			this.x = (double) (x + this.circle.getRadius()*1.5);
			this.y = y;
			this.r = 89;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_RIGHT) {
			this.x = (double) (x - this.circle.getRadius()*1.5);
			this.y = y;
			this.r = -89;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		}

	}

	public void move(){
		if(Player2.killTheSnake == true){
//			this.circle.setVisible(false);
//			this.remove();
		}
		super.move();
//		if(Player.killTheSnake == false)
//			super.move();
//			if (lastLocation.size() > 0) {
//				if (x == lastLocation.get(0).getX() && y == lastLocation.get(0).getY()) {
//					lastLocation.remove(0);
//					if (lastDirection.get(0) == PlayerMovement.MOVE_UP) {
//						lastDirection.remove(0);
//						velX = 0;
//						velY = -Settings.SNAKE_SPEED;
//						r = -89;
//					} else if (lastDirection.get(0) == PlayerMovement.MOVE_DOWN) {
//						lastDirection.remove(0);
//						velX = 0;
//						velY = Settings.SNAKE_SPEED;
//						r = 89;
//					} else if (lastDirection.get(0) == PlayerMovement.MOVE_LEFT) {
//						lastDirection.remove(0);
//						velY = 0;
//						velX = -Settings.SNAKE_SPEED;
//						r = 180;
//					} else if (lastDirection.get(0) == PlayerMovement.MOVE_RIGHT) {
//						lastDirection.remove(0);
//						velY = 0;
//						velX = Settings.SNAKE_SPEED;
//						r = 0;
//					}
//				}
//			}


//		SectionMain joint = sectManager.getSectionList().getLast();


			if(sectionToFollow.getLastDirection() == PlayerMovement.MOVE_UP){
				this.y = (double) (sectionToFollow.getY()+ this.circle.getRadius()*1.5);
				this.x = sectionToFollow.getX();
				this.r = 180;
			}
			else if(sectionToFollow.getLastDirection() == PlayerMovement.MOVE_DOWN){
				this.y = (double) (sectionToFollow.getY()- this.circle.getRadius()*1.5);
				this.x = sectionToFollow.getX();
				this.r = 0;
			}
			else if(sectionToFollow.getLastDirection() == PlayerMovement.MOVE_LEFT){
				this.x = (double) (sectionToFollow.getX()+ this.circle.getRadius()*1.5);
				this.y = sectionToFollow.getY();
				this.r = 89;
			}
			else if(sectionToFollow.getLastDirection()  == PlayerMovement.MOVE_RIGHT){
				this.x = (double) (sectionToFollow.getX()- this.circle.getRadius()*1.5);
				this.y = sectionToFollow.getY();
				this.r = -89;
			}
	}
	public void setWhoToFollow(SnakeSection2 snakeSection) {
		sectionToFollow = snakeSection;
	}
	public void addNewCoordinate(Point2D location, PlayerMovement direction) {
		lastDirection.add(direction);
		lastLocation.add(location);
	}
	public boolean isApproximate(double tail_X, double sect_X, double tail_Y, double sect_Y){
		double distance = Math.sqrt((tail_X-sect_X)*(tail_X-sect_X) + (tail_Y-sect_Y)*(tail_Y-sect_Y));
		if(distance>10){
			return true;
		}
		return false;
	}
	public void checkRemovability() {

	}

	public void checkCollision() {

	}





}
