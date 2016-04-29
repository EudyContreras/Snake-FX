
package com.SnakeGame.Core;
import com.SnakeGame.ObjectIDs.LevelObjectID;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Every static object or esthetic object in the game such as walls, boxes
 * etc is considered a tile. This class is the main tile class and can be used
 * for creating any level object.
 * @author Eudy Contreras
 *
 */
public class WavingCactus2 extends Tile {

	SnakeGame game;
	Rectangle bounds;
	Rectangle2D collisionBounds;
	float speed;
	float oldX;
    public WavingCactus2(SnakeGame game, float x, float y, float velX, float velR, Image image, LevelObjectID id ) {
        super(x,y,image,id);
        this.oldX = x;
        if(Settings.SAND_STORM)
        this.velX = velX;
        this.velR = velR;
        this.game = game;
        this.view.setTranslateX(x);
        this.view.setTranslateY(y);
        this.drawBoundingBox();
	    this.adjustBounds();
    }
    public void move(){
    	super.move();
    	if(Settings.SAND_STORM)
    	wave();
    }
    public void wave(){
    	if(x>oldX+Settings.WIND_FORCE){
    		velX-=Math.random()*(0.35 - 0.01 +1)+0.01;
    	}
    	if(x<=oldX){
    		if(velX<Settings.WIND_FORCE)
    		velX+=0.4f;
    	}
    }
    public void draw(){

    }
    public void adjustBounds(){
    	if(this.id == LevelObjectID.cactus){
    		collisionBounds = new Rectangle2D(x+15, y+40, width-50, height-60);
    	}
    }
    public void drawBoundingBox(){

    	if(Settings.DEBUG_MODE){
    		bounds = new Rectangle(x+15,y+35,width-50,height-60);
    		bounds.setStroke(Color.WHITE);
    		bounds.setFill(Color.TRANSPARENT);
    		bounds.setStrokeWidth(3);
    		game.getOverlay().getChildren().add(bounds);

    	}
     }
    public Rectangle2D getBounds(){
    	return collisionBounds;
    }
	public Rectangle2D getBoundsTop() {
		return new Rectangle2D(x+20,y,width-40,height);
	}
	public Rectangle2D getBoundsRight() {
		return new Rectangle2D(x+width-20,y+10,20,height-10);
	}
	public Rectangle2D getBoundsLeft() {
		return new Rectangle2D(x,y+10,20,height-10);
	}
	public Bounds getCollisionBounds(){
		return this.view.getBoundsInParent();
	}

}
