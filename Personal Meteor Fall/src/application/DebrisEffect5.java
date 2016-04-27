package application;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;

public class DebrisEffect5 extends DebrisEffect{

	GameDebrisID id;
	BlendMode blendMode = BlendMode.ADD;
	Random rand = new Random();
	Paint color;
	double radius;
	double decay;
	double accelaration = 0.01;
	float x;
	float y;
	float r;
	double velX = rand.nextInt(15 - -15 + 1) + -15;
	double velY = rand.nextInt(15 - -15 + 1) + -15;
	float velR;
	float lifeTime = 5.0f;
	double width;
	double height;
	boolean isAlive = false;
	boolean removable = false;
	int depth = 75;
	int amount = 200;	
	double greenRange = Math.random()*(180 - 40 + 1) +40;
	Point2D velocity;

	
	
	public DebrisEffect5(Game game, float x, float y,  Point2D velocity) {
		this.game = game;
		this.radius = rand.nextDouble()*(40 - 12 + 1)+ 12;
		this.shape.setRadius(radius);
		this.decay = 0.016; 
		this.velocity = velocity;
		this.x = x;
		this.y = y;
		this.game = game;
		init();
	}
	public void init(){
        game.getDebrisLayer().getChildren().add(shape);
        shape.setFill(imagePattern);
		velX = velocity.getX()+Settings.GLOBAL_ACCELARATION/10;
		velY = velocity.getY();
	}
	public void update(){

		x+=velX+Settings.GLOBAL_ACCELARATION;
		y+=velY;		
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

