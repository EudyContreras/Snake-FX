package application;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

public class CollisionSparks extends DebrisEffect{

	GameDebrisID id;
	Random rand = new Random();
	Paint color;
	double radius= 5;
	double decay;
	float x;
	float y;
	float r;
	float velR;
	float lifeTime = 1.0f;
	double width;
	double height;
	int depth = 75;
	int amount = 200;
	double greenRange = Math.random()*(190 - 40 + 1) +40;
	Point2D velocity = new Point2D((Math.random()*(15 - -15 + 1) + -15), Math.random()*(15 - -15 + 1) + -15);
	
	public CollisionSparks(Game game,Image image, float x, float y,  Point2D velocity) {
		this.particleManager = game.getParticleManager();
		this.game = game;
		this.imagePattern = new ImagePattern(image);
		this.shape.setRadius(radius);
		this.velocity = velocity;
		this.decay = Math.random()*(0.064 - 0.016)+0.016; 
		this.x = x;
		this.y = y;
		init();
	}
	public void init(){
        game.getDebrisLayer().getChildren().add(shape);
        shape.setBlendMode(BlendMode.ADD);
        shape.setFill(imagePattern);
	}
	public void update(){
		shape.setCenterX(x);
		shape.setCenterY(y);
		lifeTime -= decay;
	}
	public void move(){
		x += velocity.getX()+Settings.GLOBAL_ACCELARATION*.8;
		y += velocity.getY();	
		}
	public void collide(){

	}
	public boolean isAlive() {
		return x<Settings.WIDTH && x>0 && y<Settings.HEIGHT  && y>0 && lifeTime>0;
	}

	public void draw(GraphicsContext gc) {
		 shape.setOpacity(lifeTime);
		
	}
	public Rectangle2D getBounds() {
		
		return null;
	}
	
	public Rectangle2D getBoundsTop() {
		
		return null;
	}
	
	public Rectangle2D getBoundsRight() {
		
		return null;
	}
	
	public Rectangle2D getBoundsLeft() {
		
		return null;
	}
	
	public GameDebrisID getID() {
		return id;
	}
	public void setID(GameDebrisID id) {
		this.id = id;	
	}		
}

