package application;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class DebrisEffectOriginal extends DebrisEffect{

	GameDebrisID id;
	BlendMode blendMode = BlendMode.ADD;
	Paint color;
	Random rand = new Random();
	double radius;
	double decay;
	float x;
	float y;
	float r;
	float velX = rand.nextInt(15 - -15 + 1) + -15;
	float velY = rand.nextInt(15 - -15 + 1) + -15;
	float velR;
	float lifeTime = 10.0f;
	double width;
	double height;
	boolean isAlive = false;
	boolean removable = false;
	GameParticleManager particleManager;
	Game game;
	Bloom bloom = new Bloom();
	DropShadow borderGlow= new DropShadow();
	Circle shape ;
	int depth = 75;
	int amount = 200;
	double greenRange = Math.random()*(180 - 40 + 1) +40;
	double greenRange2 = Math.random()*(255 - 0 + 1) +0;
	Point2D velocity = new Point2D((Math.random()*(15 - -15 + 1) + -15), Math.random()*(15 - -15 + 1) + -15);
	
	
	public DebrisEffectOriginal(Game game, float x, float y,  Point2D velocity) {
		this.particleManager = game.getParticleManager();
		this.game = game;
		this.shape = new Circle(50,50,10, Color.WHITE);
		this.decay = 0.016; 
		this.x = x;
		this.y = y;
		this.particleManager = game.getParticleManager();
		this.game = game;
		init();
	}
	public void init(){
        game.getDebrisLayer().getChildren().add(shape);
        shape.setFill(Color.WHITE);
        shape.setStroke(Color.GRAY);
	}
	public void update(){
		x += velocity.getX();
		y += velocity.getY();
		lifeTime -= decay;
		if(lifeTime<0.01 || x>Settings.WIDTH || x<0 || y>Settings.HEIGHT  || y<0){
			game.getDebrisLayer().getChildren().remove(shape);
		}
	}
	public void move(){

		shape.setCenterX(x);
		shape.setCenterY(y);

	}
	public void collide(){

	}
	public boolean isAlive() {
		return x<Settings.WIDTH && x>0 && y<Settings.HEIGHT  && y>0 && lifeTime>0;
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

