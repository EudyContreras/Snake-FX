
package application;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class TileMap extends Tile {
	GameTileManager tileManager;
	Game game;
	float speed;
	
    public TileMap(float x, float y, float speed, float velY,Image image, LevelObjectID id ) {
        super(x,y,image, id);
        this.velX = 0;
        this.speed = speed*(Settings.FRAMECAP); 
        this.velY = velY;
        this.view.setTranslateX(x);
        this.view.setTranslateY(y);
    }
    public TileMap(float x, float y, float velX, float velY,Image image) {
        super(x,y,image);
        this.velX = velX;
        this.velY = velY;
        this.view.setTranslateX(x);
        this.view.setTranslateY(y);
    }
    public void move(){
    	velX-=0.02*(Settings.FRAMECAP); 
    	if(velX<speed){
    		velX=speed;
    	}
    	x = x+velX+Settings.GLOBAL_ACCELARATION*(Settings.FRAMECAP);
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
    public Bounds getBounds2D(){
    	return this.view.getBoundsInParent();
    }
    
}
