package application;

import java.util.Random;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class Stars extends DebrisEffect{

	GameDebrisID id;
	BlendMode blendMode = BlendMode.ADD;
	Paint color;
	Random rand = new Random();
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
	GameParticleManager particleManager;
	Lighting lighting = new Lighting();
	Light.Point light = new Light.Point();
	Game game;	
	Circle shape ;
	int depth = 75;
	int amount = 200;
	
	
	public Stars(Game game, float x, float y, float velX,float velR, float radius, double opacity) {
		this.particleManager = game.getParticleManager();
		this.game = game;
		this.velX = velX;
		this.velR = 0;
		this.radius = radius;
		this.shape = new Circle(x,y,radius,Color.WHITE);
		this.shape.setFill(Color.WHITE);
		this.shape.setOpacity(opacity);
		this.x = x;
		this.y = y;
		init();
	}
	public void init(){
        game.getBottomLayer().getChildren().add(shape);
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
		return x> game.getloader().getPlayer().getX()-400;
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

