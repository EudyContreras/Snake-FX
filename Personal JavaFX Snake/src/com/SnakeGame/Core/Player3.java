package com.SnakeGame.Core;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;



public class Player3 extends GameObject2 {

    double minX;
    double maxX;
    double minY;
    double maxY;
    double speed;
    double clearUp;
    double lightOpacity = 0.8;
    float vibrationUp = 1.0f;
    float vibrationDown = 1.0f;
	double amountOfGlow = 0;
	boolean isDead = false;
	boolean cantShoot = true;
	boolean notShooting = false;
	boolean lampOff = false;
	boolean glowing = false;
	boolean up = true;
	boolean down = false;
	boolean shake = false;
	boolean applyRecoil = false;
	boolean left = true;
	boolean right = false;
	boolean vibrate = false;
	boolean collision = false;
	boolean checkVibrate = true;
	boolean hitBounds = false;
    boolean movingUp = false;
    boolean movingDown = false;
	boolean movingLeft = false;
	boolean movingRight = false;
	boolean standingStill = false;
	boolean standingStillY = false;
	boolean notMovingToTheSides = false;
	boolean loopingAudio = false;
	boolean increaseGlow = true;
	boolean decreaseGlow = false;
	boolean introduceSpaceship = true;
	int counter = 0;
	int shootingLag;
	int rightBoundary ;
	int recoilDuration = 30;
    int shakeDuration = 30;
    int NUMERIC_ID = 0;
    int scroll = 0;
	float glowLevel =0;
	float amountOfBlur = 2.0f;
    SnakeGame game;
	DropShadow borderGlow= new DropShadow();
	DropShadow borderGlow2= new DropShadow();
	MotionBlur motionBlur = new MotionBlur();
    Light.Point light = new Light.Point();
    Lighting lighting = new Lighting();
    GameObjectManager2 gom;
    GameSectionManager3 sectManager;
    PlayerMovement direction;

    public Player3(SnakeGame game, Pane layer, Node node, float x, float y, float r, float velX, float velY, float velR, double health, double damage, double speed, GameObjectID id, GameObjectManager2 gom) {
        super(game, layer, node, x, y, r, velX, velY, velR, health, damage, id);
        this.gom = gom;
        this.speed = speed;
        this.game = game;
		this.motionBlur.setAngle(-15.0);
		this.motionBlur.setRadius(amountOfBlur);
		this.imageView.setEffect(motionBlur);
		this.lighting.setSurfaceScale(5.0);
		this.lighting.setLight(light);
		this.lighting.setDiffuseConstant(1.2);
		this.lighting.setSpecularConstant(0.2);
		this.direction = PlayerMovement.STANDING_STILL;
		this.sectManager = game.getSectionManager3();
		if(Settings.ADD_LIGHTING)
		this.motionBlur.setInput(lighting);
    }
    public void updateUI(){
    	super.updateUI();

    }
    public void move(){
    	super.move();

    }
    public void setDirection(PlayerMovement direction) {
		if(this.direction == direction) {
			this.direction = direction;
		} else if(!(
				(this.direction == PlayerMovement.MOVE_LEFT && direction == PlayerMovement.MOVE_RIGHT ) ||
				(this.direction == PlayerMovement.MOVE_RIGHT && direction == PlayerMovement.MOVE_LEFT ) ||
				(this.direction == PlayerMovement.MOVE_UP && direction == PlayerMovement.MOVE_DOWN ) ||
				(this.direction == PlayerMovement.MOVE_DOWN && direction == PlayerMovement.MOVE_UP ))){

			if(direction == PlayerMovement.MOVE_UP) {
				moveUp();
			}
			else if(direction == PlayerMovement.MOVE_DOWN) {
				moveDown();
			}
			else if(direction == PlayerMovement.MOVE_LEFT) {
				moveLeft();
			}
			else if(direction == PlayerMovement.MOVE_RIGHT) {
				moveRight();
			}
		}

		this.direction = direction;
	}

	private void moveUp() {
		velY = -Settings.SNAKE_SPEED2;
		velX = 0;
		setCurrentDirection(PlayerMovement.MOVE_UP);
		if(sectManager.getSectionList().size()>0){
			sectManager.addNewCoordinates(new Point2D(x,y),PlayerMovement.MOVE_UP, 0);
		}
	}
	private void moveDown() {
		velY = Settings.SNAKE_SPEED2;
		velX = 0;
		setCurrentDirection(PlayerMovement.MOVE_DOWN);
		if(sectManager.getSectionList().size()>0){
			sectManager.addNewCoordinates(new Point2D(x,y),PlayerMovement.MOVE_DOWN, 0);
		}
	}
	private void moveRight() {
		velX = Settings.SNAKE_SPEED2;
		velY = 0;
		setCurrentDirection(PlayerMovement.MOVE_RIGHT);
		if(sectManager.getSectionList().size()>0){
			sectManager.addNewCoordinates(new Point2D(x,y),PlayerMovement.MOVE_RIGHT, 0);
		}
	}
	private void moveLeft() {
		velX = -Settings.SNAKE_SPEED2;
		velY = 0;
		setCurrentDirection(PlayerMovement.MOVE_LEFT);
		if(sectManager.getSectionList().size()>0){
			sectManager.addNewCoordinates(new Point2D(x,y),PlayerMovement.MOVE_LEFT, 0);
		}
	}
    public void setCurrentDirection(PlayerMovement direction){
    	this.direction = direction;
    }
    public PlayerMovement getCurrentDirection(){
		return direction;
    }
    public void checkCollision() {
    	if(isDead == false){
        for(int i = 0; i<game.getObjectManager().getObjectList().size(); i++){
    		GameObject tempObject = game.getObjectManager().getObjectList().get(i);
    		if(tempObject.getId() == GameObjectID.Fruit){
    			if(getRadialBounds().intersects(tempObject.getRadialBounds())){
    	    		addSection();
    	    		tempObject.blowUp();
    	    		tempObject.remove();
    	    		break;

    			}
    		}
        }}
	}
    public void addSection(){
    	sectManager.addSection(new SnakeSection3(this,game, layer, new Circle(40,Color.WHITE), x, y, GameObjectID.SnakeSection, getCurrentDirection(), NUMERIC_ID));
        SnakeGame.writeToLog("New section added "+ NUMERIC_ID);
        NUMERIC_ID++;
        game.getScoreBoard().increaseScore();
        if(ScoreKeeper.APPLE_COUNT>4)
        game.getloader().spawnSnakeFood();
    }
    public void draw(GraphicsContext gc){
    	drawBoundingBox(gc);
    }
    public void drawBoundingBox(GraphicsContext gc){

    	if(Settings.DEBUG_MODE){
    		this.imageView.setVisible(false);
     	gc.setStroke(Color.BLACK);
     	//top
     	gc.strokeRect(x+40,y+55,width-60,height*0.24);
     	//left
     	gc.strokeRect(x+20,y+65,20,height*0.34);
     	//right
     	gc.strokeRect(x+width-20,y+65,20,height*0.34);
     	//bottom
     	gc.strokeRect(x+40,y+height/2+15,width-60,height*0.20);
    	}
     }
    public void setScroller() {
        this.imageView.translateXProperty().addListener((v, oldValue, newValue) -> {

            int offSet = newValue.intValue();
            if (offSet > 100 && offSet < game.levelLenght - 1900) {
            	game.getRoot().setTranslateX(-(offSet - 100));
            }
        });

    }
//    public Rectangle2D getBoundsTop(){
//
//        return new Rectangle2D(x+40,y+55,width-60,height*0.24);
//    }
//    public Rectangle2D getBoundsLeft(){
//
//        return new Rectangle2D(x+20,y+65,20,height*0.34);
//    }
//    public Rectangle2D getBoundsRight(){
//
//        return new Rectangle2D(x+width-20,y+65,20,height*0.34);
//    }
//    public Rectangle2D getBounds(){
//
//    	return new Rectangle2D(x+40,y+height/2+15,width-60,height*0.20);
//    }

	public boolean isCollision() {
		return collision;
	}
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	public void die() {
		isDead = true;
		game.getPlayfieldLayer().getChildren().remove(this.imageView);
		gom.removeObject(this);
		this.imageView.setVisible(false);

	}
	public boolean isDead() {
		return isDead;
	}
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	public void checkRemovability() {

	}
	public boolean isNotMovingFast() {
		return false;
	}
	public void setSpeedBump(boolean b) {

	}
}