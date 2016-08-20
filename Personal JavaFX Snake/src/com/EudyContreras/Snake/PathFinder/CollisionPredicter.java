package com.EudyContreras.Snake.PathFinder;

import java.util.List;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;

public class CollisionPredicter extends AbstractCollisionMonitor{
	private List<AbstractTile> tileContainer;

	public CollisionPredicter(GameManager game, ObjectEvasionAI evader, double x, double y, RayDirection direction ){
		super(x,y,direction);
		this.width = evader.getWidth();
		this.height = evader.getHeight();
		this.tileContainer = game.getGameLoader().getTileManager().getBlock();
	}
	public void updateLogic(){
		checkCoordinates();
	}
	public void checkCoordinates(){
		lifeTime--;
		if(lifeTime<0){
			setAlive(false);
		}
	}
	public void move(){
		this.move();
		switch(direction){
		case DOWN:
			velX = 0;
			velY = 40;
			break;
		case DOWN_LEFT:
			velX = -40;
			velY = 40;
			break;
		case DOWN_RIGHT:
			velX = 40;
			velY = 40;
			break;
		case LEFT:
			velX = -40;
			velY = 0;
			break;
		case RIGHT:
			velX = 40;
			velY = 0;
			break;
		case UP:
			velX = 0;
			velY = -40;
			break;
		case UP_LEFT:
			velX = -40;
			velY = -40;
			break;
		case UP_RIGHT:
			velX = 40;
			velY = -40;
			break;
		default:
			break;
		}
	}
	public void checkCollision(){
		for(AbstractTile object: tileContainer){
			if(getBounds().intersects(object.getBounds())){
				processCollision(object);
			}
		}
	}
	public void processCollision(AbstractTile object){
		this.setEminentCollider(object);
		this.setHasTarger(true);
	}
}