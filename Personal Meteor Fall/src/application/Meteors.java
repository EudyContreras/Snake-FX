package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Meteors extends DebrisEffect{

	GameDebrisID id;
	Paint color;
	double radius;
	double decay;
	float x;
	float y;
	float r;
	float velX ;
	float velY ;
	float velR;
	float lifeTime = 10.0f;
	boolean isAlive = false;
	boolean removable = false;
	Game game;	
	Circle shape ;
	int depth = 75;
	int amount = 200;
	
	
	public Meteors(Game game, float x, float y, float velX,float velR, float radius, float height) {
		this.particleManager = game.getParticleManager();
		this.shape = new Circle(x,y,radius, Color.WHITE);
		this.imagePattern = new ImagePattern(GameImageBank.preLightedDebris);
		this.game = game;
		this.velX = velX;
		this.velR = velR;
		this.radius = radius;
		this.x = x;
		this.y = y;
		init();
	}
	public void init(){
        game.getBottomLayer().getChildren().add(shape);
        shape.setFill(imagePattern);
	}
	public void update(){
		x += velX+Settings.GLOBAL_ACCELARATION*(Settings.FRAMECAP);
		r += velR*(Settings.FRAMECAP);
		if(x<0+radius){
			game.getBottomLayer().getChildren().remove(shape);
		}
	}
	public void move(){		
		shape.setCenterX(x);
		shape.setCenterY(y);
		shape.setRotate(r);
	}
	public void collide(){
		
	}
	public boolean isAlive() {
		return x> 0-radius;
	}

	public void draw(GraphicsContext gc) {
		
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

