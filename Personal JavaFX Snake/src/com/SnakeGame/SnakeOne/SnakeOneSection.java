package com.SnakeGame.SnakeOne;

import java.util.Random;

import com.SnakeGame.Core.DirtDisplacement;
import com.SnakeGame.Core.GameImageBank;
import com.SnakeGame.Core.PlayerMovement;
import com.SnakeGame.Core.SectionDisintegration;
import com.SnakeGame.Core.SectionMain;
import com.SnakeGame.Core.Settings;
import com.SnakeGame.Core.SnakeGame;
import com.SnakeGame.ObjectIDs.GameObjectID;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class SnakeOneSection extends SectionMain{
	double particleLife;
	double particleSize;
	double fadeValue = 1.0;
	boolean fade = false;
	boolean blowUp = true;
	int dirtDelay = 10;
	SnakeOne snake;
	SnakeGame game;
	SnakeOneTail tail;
	Random rand;
	Circle bones;
	SnakeOneSectionManager sectManager;
	PlayerMovement direction;
	boolean stopped = false;

	public SnakeOneSection(SnakeOne snake, SnakeGame game, Pane layer, Circle node, double x, double y, GameObjectID id, PlayerMovement Direction, int numericID) {
		super(game, layer, node, id);
		this.game = game;
		this.snake = snake;
		this.numericID = numericID;
		this.sectManager = game.getSectionManager();
		this.rand = new Random();
		if (this.numericID <= 0) {
			if (Direction == PlayerMovement.MOVE_UP) {
				setLastDirection(PlayerMovement.MOVE_UP);

				this.y = (double) (y + this.circle.getRadius());
				this.x = x;
				this.r = -89;
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				setLastPosition(new Point2D(this.x,this.y));
				tail = new SnakeOneTail(this,game, game.getSnakeBodyLayer(), new Circle(Settings.SECTION_SIZE-5,new ImagePattern(GameImageBank.snakeTail)), this.x, this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_UP);
				game.getObjectManager().addObject(tail);
				snake.setTail(tail);
				snake.setNeighbor(this);
				if(this.numericID == SnakeOne.NUMERIC_ID){
					tail.setWhoToFollow(this);
				}
			} else if (Direction == PlayerMovement.MOVE_DOWN) {
				setLastDirection(PlayerMovement.MOVE_DOWN);

				this.y = (double) (y - this.circle.getRadius());
				this.x = x;
				this.r = 89;
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				setLastPosition(new Point2D(this.x,this.y));
				tail = new SnakeOneTail(this,game, game.getSnakeBodyLayer(), new Circle(Settings.SECTION_SIZE-5,new ImagePattern(GameImageBank.snakeTail)), this.x, this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_DOWN);
				game.getObjectManager().addObject(tail);
				snake.setTail(tail);
				snake.setNeighbor(this);
				if(this.numericID == SnakeOne.NUMERIC_ID){
					tail.setWhoToFollow(this);
				}
			} else if (Direction == PlayerMovement.MOVE_LEFT) {
				setLastDirection(PlayerMovement.MOVE_LEFT);

				this.x = (double) (x + this.circle.getRadius());
				this.y = y;
				this.r = 180;
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				setLastPosition(new Point2D(this.x,this.y));
				tail = new SnakeOneTail(this,game, game.getSnakeBodyLayer(), new Circle(Settings.SECTION_SIZE-5,new ImagePattern(GameImageBank.snakeTail)), this.x, this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_LEFT);
				game.getObjectManager().addObject(tail);
				snake.setTail(tail);
				snake.setNeighbor(this);
				if(this.numericID == SnakeOne.NUMERIC_ID){
					tail.setWhoToFollow(this);
				}
			} else if (Direction == PlayerMovement.MOVE_RIGHT) {
				setLastDirection(PlayerMovement.MOVE_RIGHT);

				this.x = (double) (x - this.circle.getRadius());
				this.y = y;
				this.r = 0;
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				setLastPosition(new Point2D(this.x,this.y));
				tail = new SnakeOneTail(this,game, game.getSnakeBodyLayer(), new Circle(Settings.SECTION_SIZE-5,new ImagePattern(GameImageBank.snakeTail)), this.x, this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_RIGHT);
				game.getObjectManager().addObject(tail);
				snake.setTail(tail);
				snake.setNeighbor(this);
				if(this.numericID == SnakeOne.NUMERIC_ID){
					tail.setWhoToFollow(this);
				}
			}
			else if (Direction == PlayerMovement.STANDING_STILL) {
				setLastDirection(PlayerMovement.STANDING_STILL);

				this.y = (double) (y - this.circle.getRadius());
				this.x = x;
				this.r = 89;
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				setLastPosition(new Point2D(this.x,this.y));
				tail = new SnakeOneTail(this,game, game.getSnakeBodyLayer(), new Circle(Settings.SECTION_SIZE-5,new ImagePattern(GameImageBank.snakeTail)), this.x, this.y, GameObjectID.SnakeTail, PlayerMovement.MOVE_DOWN);
				game.getObjectManager().addObject(tail);
				snake.setTail(tail);
				snake.setNeighbor(this);
				if(this.numericID == SnakeOne.NUMERIC_ID){
					tail.setWhoToFollow(this);
				}
			}
		} else if (this.numericID > 0) {
			for(int i = sectManager.getSectionList().size()-1; i>=0; i--){
				SectionMain previousSect = sectManager.getSectionList().get(i);
				if(previousSect.getNumericID() == this.numericID-1){
					switch(previousSect.getLastDirection()){
					case MOVE_UP:
						setLastDirection(PlayerMovement.MOVE_UP);

						this.y = (double) (previousSect.getY() + this.circle.getRadius()* Settings.SECTION_DISTANCE);
						this.x = previousSect.getX();
						this.r = -89;
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						setLastPosition(new Point2D(this.x,this.y));
						if(this.numericID == SnakeOne.NUMERIC_ID){
							snake.getTail().setWhoToFollow(this);
							snake.getTail().addNewCoordinate(new Point2D(x, y), PlayerMovement.MOVE_UP);
						}
						break;
					case MOVE_DOWN:
						setLastDirection(PlayerMovement.MOVE_DOWN);

						this.y = (double) (previousSect.getY() - this.circle.getRadius()* Settings.SECTION_DISTANCE);
						this.x = previousSect.getX();
						this.r = 89;
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						setLastPosition(new Point2D(this.x,this.y));
						if(this.numericID == SnakeOne.NUMERIC_ID){
							snake.getTail().setWhoToFollow(this);
							snake.getTail().addNewCoordinate(new Point2D(x, y), PlayerMovement.MOVE_DOWN);
						}
						break;
					case MOVE_LEFT:
						setLastDirection(PlayerMovement.MOVE_LEFT);

						this.x = (double) (previousSect.getX() + this.circle.getRadius()* Settings.SECTION_DISTANCE);
						this.y = previousSect.getY();
						this.r = 180;
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						setLastPosition(new Point2D(this.x,this.y));
						if(this.numericID == SnakeOne.NUMERIC_ID){
							snake.getTail().setWhoToFollow(this);
							snake.getTail().addNewCoordinate(new Point2D(x, y), PlayerMovement.MOVE_LEFT);
						}
						break;
					case MOVE_RIGHT:
						setLastDirection(PlayerMovement.MOVE_RIGHT);

						this.x = (double) (previousSect.getX() - this.circle.getRadius()* Settings.SECTION_DISTANCE);
						this.y = previousSect.getY();
						this.r = 0;
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						setLastPosition(new Point2D(this.x,this.y));
						if(this.numericID == SnakeOne.NUMERIC_ID){
							snake.getTail().setWhoToFollow(this);
							snake.getTail().addNewCoordinate(new Point2D(x, y), PlayerMovement.MOVE_RIGHT);
						}
						break;
					case STANDING_STILL:
						setLastDirection(PlayerMovement.STANDING_STILL);

						this.y = (double) (previousSect.getY() - this.circle.getRadius()* Settings.SECTION_DISTANCE);
						this.x = previousSect.getX();
						this.r = 89;
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						setLastPosition(new Point2D(this.x,this.y));
						if(this.numericID == SnakeOne.NUMERIC_ID){
							snake.getTail().setWhoToFollow(this);
							snake.getTail().addNewCoordinate(new Point2D(x, y), PlayerMovement.STANDING_STILL);
						}
						break;
					}
				}
			}
		}

	}

	public void move() {
		if(SnakeOne.killTheSnake == false && !SnakeOne.levelComplete && SnakeOne.keepMoving){
		super.move();
		if (lastPosition.size() > 0) {
			if (x == lastPosition.get(0).getX() && y == lastPosition.get(0).getY()) {
				removeLatestLocation();
				if (lastDirection.get(0) == PlayerMovement.MOVE_UP) {
					this.direction = PlayerMovement.MOVE_UP;
					setLastDirection(PlayerMovement.MOVE_UP);
					setLastPosition(new Point2D(x, y));
					removeLatestDirection();
					velX = 0;
					velY = -Settings.SNAKE_SPEED;
					r = -89;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_DOWN) {
					this.direction = PlayerMovement.MOVE_DOWN;
					setLastDirection(PlayerMovement.MOVE_DOWN);
					setLastPosition(new Point2D(x, y));
					removeLatestDirection();
					velX = 0;
					velY = Settings.SNAKE_SPEED;
					r = 89;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_LEFT) {
					this.direction = PlayerMovement.MOVE_LEFT;
					setLastDirection(PlayerMovement.MOVE_LEFT);
					setLastPosition(new Point2D(x, y));
					removeLatestDirection();
					velY = 0;
					velX = -Settings.SNAKE_SPEED;
					r = 180;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_LEFT, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_RIGHT) {
					this.direction = PlayerMovement.MOVE_RIGHT;
					setLastDirection(PlayerMovement.MOVE_RIGHT);
					setLastPosition(new Point2D(x, y));
					removeLatestDirection();
					velY = 0;
					velX = Settings.SNAKE_SPEED;
					r = 0;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, this.numericID + 1);
				}else if (lastDirection.get(0) == PlayerMovement.STANDING_STILL) {
					setLastDirection(PlayerMovement.STANDING_STILL);
					setLastPosition(new Point2D(x, y));
					removeLatestDirection();
					velY = 0;
					velX = 0;
					r = 0;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.STANDING_STILL, this.numericID + 1);
				}
			}
		}}
	}
	public void updateUI(){
		super.updateUI();
		if (Settings.ALLOW_DIRT) {
			dirtDelay--;
			if (dirtDelay <= 0) {
				displaceDirt(x + width / 2, y + height / 2, 18, 18);
				dirtDelay = 18;
			}
		}
    	if(fade == true){
    		fadeValue-=0.01;
    		this.circle.setOpacity(fadeValue);
    		if(fadeValue<=0){
    			fadeValue = 0;
    		}
    	}

	}
	public void loadBones(){
		bones = new Circle(x,y,this.radius*0.8,new ImagePattern(GameImageBank.snakeBones));
		game.getDebrisLayer().getChildren().add(bones);
		bones.setRotate(r);
	}
	public void die(){
		loadBones();
		fade = true;
		blowUp();
	}
    public void displaceDirt(double x, double y, double low, double high){
    	if(!SnakeOne.killTheSnake){
    		for(int i = 0; i<8; i++){
    		game.getDebrisManager().addDebris(new DirtDisplacement(game,GameImageBank.dirt, (double) x, (double) y, new Point2D((Math.random()*(8 - -8 + 1) + -8), Math.random()*(8 - -8 + 1) + -8)));
    		}
    	}
    }
	public void blowUp(){
		if(blowUp == true){
		for (int i = 0; i < Settings.PARTICLE_LIMIT; i++) {
			if (Settings.ADD_VARIATION) {
				particleSize = Math.random() * (12 - 5 + 1) + 5;
				particleLife = Math.random() * (2.0 - 1.0 + 1) + 1.5;
			}
			game.getDebrisManager().addParticle(new SectionDisintegration(game, GameImageBank.snakeDebris, particleLife, particleSize,(double) (x + this.radius/2), (double) (y + this.radius/2)));
		}
		blowUp = false;
		}
	}

	public void checkRemovability() {
		teleport();

	}
	public void teleport(){

		if (x < 0 - radius * 1.8) {
			x = Settings.WIDTH + radius;
		} else if (x > Settings.WIDTH + radius*1.8) {
			x = 0 - radius;
		} else if (y < 0 - radius * 1.8) {
			y = Settings.HEIGHT + radius;
		} else if (y > Settings.HEIGHT + radius* 1.8) {
			y = 0 - radius ;
		}
	}
	public void checkCollision() {

	}
	public PlayerMovement getDirection() {
		return direction;
	}

}
