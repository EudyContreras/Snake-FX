package com.SnakeGame.Core;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class SlitherSection extends SlitherSectionMain{
	double particleLife;
	double particleSize;
	double fadeValue = 1.0;
	boolean fade = false;
	boolean blowUp = true;
	boolean stopped = false;
	float rotation;
	float rotationDelay = 10;
	int dirtDelay = 10;
	SnakeGame game;
	SlitherTail tail;
	Random rand;
	Circle bones;
	SlitherSnake snake;
	SlitherSectionMain previousSect;
	GameSlitherSectionManager sectManager;

	public SlitherSection(SlitherSnake snake, SnakeGame game, Pane layer, Node node, float x, float y, GameObjectID id, float numericID) {
		super(game, layer, node, id);
		this.game = game;
		this.snake = snake;
		this.numericID = numericID;
		this.sectManager = game.getSectionManager3();
		this.x = x;
		this.y = y;
		this.velX = Settings.SLITHER_SPEED;
		this.velY = Settings.SLITHER_SPEED;
		this.speed = 0.7f;
		if(numericID<=0) {
		this.tail = new SlitherTail(this, game, layer, new Circle(Settings.SECTION_SIZE-5,new ImagePattern(GameImageBank.snakeTail)), x, y, id);
		this.game.getSlitherManager().addObject(tail);
		snake.setTail(tail);
		snake.setNeighbor(this);
		if(this.numericID == SlitherSnake.NUMERIC_ID){
			tail.setWhoToFollow(this);
		}
		}
		if(this.numericID == SlitherSnake.NUMERIC_ID){
			snake.getTail().setWhoToFollow(this);
		}
		if(numericID>0) {
			for (int i = sectManager.getSectionList().size() - 1; i >= 0; i--) {
				SlitherSectionMain previousSect = sectManager.getSectionList().get(i);
				if (previousSect.getNumericID() == this.numericID - 1) {
					this.previousSect = previousSect;
					this.r = previousSect.getR();
				}
			}
		}
	}

	public void move() {
//		r = -rotation;
		if(this.numericID>0) {
			this.setRotation(previousSect.getRotation());
		}
	//	if(this.numericID == SlitherSnake.NUMERIC_ID) {
//		snake.getTail().setX((float) (getX() + Math.sin(Math.toRadians(rotation)) * velX * speed));
//		snake.getTail().setY((float) (getY() + Math.cos(Math.toRadians(rotation)) * velY * speed));
//		snake.getTail().setR((float) (this.getRotation()));
	//	}
//		for (int i = 1; i <= snakeLength; i++) {
//
//			SlitherSectionMain partBefore = sectManager.getSectionList().get(i - 1);
//			SlitherSectionMain thisPart = sectManager.getSectionList().get(i);
//
//			float xChange = partBefore.getX() - thisPart.getX();
//			float yChange = partBefore.getY() - thisPart.getY();
//
//			float angle = (float) Math.atan2(yChange, xChange);
//
//			thisPart.setX(partBefore.getX() - (float) Math.cos(angle) * 10);
//			thisPart.setY(partBefore.getY() - (float) Math.sin(angle) * 10);
//
//		}
		if (this.numericID > 0) {
			if(previousSect.getR()>=r) {
				r+=5.0f/SlitherSnake.ROTATION_OFFSET;
				if(r>=previousSect.getR()) {
					r = previousSect.getR();
				}
			}
			if(previousSect.getR()<r) {
				r-=5.0f/SlitherSnake.ROTATION_OFFSET;
				if(r<=previousSect.getR()) {
					r = previousSect.getR();
				}
			}
//			if(x == previousSect.getX() && y == previousSect.getY()) {
//				r = previousSect.getR();
//			}
		}
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
//    	if(this.numericID>0)
//    	this.r = previousSect.getR()-10;
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
    	if(!SlitherSnake.killSlither){
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
	public void teleport(){

		if (x < 0 - radius * 2) {
			x = (float) (Settings.WIDTH + radius);
		} else if (x > Settings.WIDTH + radius*2) {
			x = (float) (0 - radius);
		} else if (y < 0 - radius * 2) {
			y = (float) (Settings.HEIGHT + radius);
		} else if (y > Settings.HEIGHT + radius* 2) {
			y = (float) (0 - radius) ;
		}
	}
	public void checkRemovability() {
		teleport();
	}
	public void checkCollision() {

	}
	public PlayerMovement getDirection() {
		return direction;
	}


}
