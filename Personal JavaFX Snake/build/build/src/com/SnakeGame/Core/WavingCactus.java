
package com.SnakeGame.Core;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Every static object or esthetic object in the game such as walls, boxes
 * etc is considered a tile. This class is the main tile class and can be used
 * for creating any level object.
 * @author Eudy Contreras
 *
 */
public class WavingCactus extends Tile {
	GameTileManager tileManager;
	SnakeGame game;
	float speed;

    public WavingCactus(float x, float y, float velR, Image image, LevelObjectID id ) {
        super(x,y,image,id);
        if(Settings.SAND_STORM)
        this.velR = velR;
        this.view.setTranslateX(x);
        this.view.setTranslateY(y);
        this.view.setRotationAxis(Rotate.Y_AXIS);
    }
    public void move(){
    	super.move();
    	if(Settings.SAND_STORM)
    	wave();
    }
    public void wave(){
    	if(r>4){
    		velR-=Math.random()*(0.4 - 0.01 +1)+0.01;
    	}
    	if(r<=0){
    		if(velR<4)
    		velR+=0.5f;
    	}
    }
    public void draw(GraphicsContext gc){
    	drawBoundingBox(gc);
    }
    public void drawBoundingBox(GraphicsContext gc){

    	if(Settings.DEBUG_MODE){
     	gc.setStroke(Color.BLACK);
     	gc.strokeRect (x+100, y+45, width*.25, height/5);

    	}
     }
    public Rectangle2D getBounds(){
    	return new Rectangle2D(x,y,width,height);
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
