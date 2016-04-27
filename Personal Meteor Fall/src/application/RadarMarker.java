package application;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class RadarMarker extends DebrisEffect{

	GameDebrisID id;
	BlendMode blendMode = BlendMode.ADD;
	Random rand = new Random();
	Paint color;
	double radius;
	double decay;
	float x;
	float y;
	float r;
	float velX = rand.nextInt(15 - -15 + 1) + -15;
	float velY = rand.nextInt(15 - -15 + 1) + -15;
	float velR;
	float lifeTime = 1.0f;
	double width;
	double height;
	boolean isAlive = false;
	boolean removable = false;
	boolean dead = false;
	int depth = 250;
	int amount = 200;
	double greenRange = Math.random()*(215 - 55 + 1) +55;
	Point2D velocity = new Point2D((Math.random()*(15 - -15 + 1) + -15), Math.random()*(15 - -15 + 1) + -15);
	
	public RadarMarker(Game game, double expireTime, float x, float y) {
		this.particleManager = game.getParticleManager();
		this.game = game;
		this.radius = 5;
		this.shape = new Circle(x,y,radius,Color.RED);
		this.decay = 0.016/expireTime; 
		this.x = x;
		this.y = y;
		init();
	}
	public void init(){
        game.getRadarLayer().getChildren().add(shape);
	}
	public void update(){
		lifeTime -= decay;
		if(lifeTime<0.01){
			game.getRadarLayer().getChildren().remove(shape);
		}
	}
	public void move(){

		shape.setCenterX(x);
		shape.setCenterY(y);

	}
	public void collide(){


	}
	public boolean isAlive() {
		return lifeTime>0;
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

