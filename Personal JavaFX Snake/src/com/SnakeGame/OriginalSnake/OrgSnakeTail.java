package com.SnakeGame.OriginalSnake;

import java.util.LinkedList;
import com.SnakeGame.Core.GameObjectID;
import com.SnakeGame.Core.PlayerMovement;
import com.SnakeGame.Core.SnakeGame;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class OrgSnakeTail extends OrgGameObject{
	OrgSnakeSection snakeSect;
	OrgSnakeSection sectionToFollow;
	OrgGameSectionManager sectManager;
	LinkedList<Point2D> lastLocation = new LinkedList<Point2D>();
	LinkedList<PlayerMovement> lastDirection = new LinkedList<PlayerMovement>();

	public OrgSnakeTail(OrgSnakeSection snake, SnakeGame game, Pane layer, Node node, float x, float y, GameObjectID id, PlayerMovement Direction) {
		super(game, layer, node, y, y, id);
		this.velX = snake.getVelX();
		this.velY = snake.getVelY();
		this.r = -snake.getR();
		this.snakeSect = snake;
		this.sectManager = game.getOrgSectManager();
//		if (Direction == PlayerMovement.MOVE_UP) {
//			this.y =(float) (y + this.circle.getRadius()*1.5);
//			this.x = x;
//			this.r = 180;
//			this.velX = snake.getVelX();
//			this.velY = snake.getVelY();
//		} else if (Direction == PlayerMovement.MOVE_DOWN) {
//			this.y = (float) (y - this.circle.getRadius()*1.5);
//			this.x = x;
//			this.r = 0;
//			this.velX = snake.getVelX();
//			this.velY = snake.getVelY();
//		} else if (Direction == PlayerMovement.MOVE_LEFT) {
//			this.x = (float) (x + this.circle.getRadius()*1.5);
//			this.y = y;
//			this.r = 89;
//			this.velX = snake.getVelX();
//			this.velY = snake.getVelY();
//		} else if (Direction == PlayerMovement.MOVE_RIGHT) {
//			this.x = (float) (x - this.circle.getRadius()*1.5);
//			this.y = y;
//			this.r = -89;
//			this.velX = snake.getVelX();
//			this.velY = snake.getVelY();
//		}

	}

	public void move(){
//		super.move();
		x = sectionToFollow.getX();
		y = sectionToFollow.getY();
		r = -sectionToFollow.getR();
//			if(sectionToFollow.getLastDirection() == PlayerMovement.MOVE_UP){
//				this.y = (float) (sectionToFollow.getY()+ this.circle.getRadius()*1.5);
//				this.x = sectionToFollow.getX();
//				this.r = 180;
//			}
//			else if(sectionToFollow.getLastDirection() == PlayerMovement.MOVE_DOWN){
//				this.y = (float) (sectionToFollow.getY()- this.circle.getRadius()*1.5);
//				this.x = sectionToFollow.getX();
//				this.r = 0;
//			}
//			else if(sectionToFollow.getLastDirection() == PlayerMovement.MOVE_LEFT){
//				this.x = (float) (sectionToFollow.getX()+ this.circle.getRadius()*1.5);
//				this.y = sectionToFollow.getY();
//				this.r = 89;
//			}
//			else if(sectionToFollow.getLastDirection()  == PlayerMovement.MOVE_RIGHT){
//				this.x = (float) (sectionToFollow.getX()- this.circle.getRadius()*1.5);
//				this.y = sectionToFollow.getY();
//				this.r = -89;
//			}
	}
	public void setWhoToFollow(OrgSnakeSection snakeSection) {
		sectionToFollow = snakeSection;
	}
	public void addNewCoordinate(Point2D location, PlayerMovement direction) {
		lastDirection.add(direction);
		lastLocation.add(location);
	}
	public void checkRemovability() {

	}
	public void checkCollision() {

	}





}
