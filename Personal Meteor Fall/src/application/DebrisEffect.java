package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public abstract class DebrisEffect {

	Game game;
	Circle shape = new Circle(-200,-200,0);
	GameParticleManager particleManager;
	ImagePattern imagePattern = new ImagePattern(GameImageBank.debris);
	DropShadow borderGlow= new DropShadow();
	Bloom bloom = new Bloom();
    BoxBlur motionBlur = new BoxBlur();
    
	public DebrisEffect(){

	}
	public abstract void update();
	public abstract void draw( GraphicsContext gc); 
	public abstract void move(); 
	public abstract void collide(); 
	public abstract boolean isAlive();
	public abstract GameDebrisID getID();
	public abstract void setID(GameDebrisID id);
	public abstract Rectangle2D getBoundsTop();	
	public abstract Rectangle2D getBounds() ;
	public abstract Rectangle2D getBoundsRight();	
	public abstract Rectangle2D getBoundsLeft();
	public Circle getShape() {
		return shape;
	}
	}

