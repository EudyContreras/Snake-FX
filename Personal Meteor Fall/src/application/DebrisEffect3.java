package application;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class DebrisEffect3 extends DebrisEffect{

	GameDebrisID id;
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
	int depth = 400;
	int amount = 200;
	double greenRange = Math.random()*(200 - 65 + 1) +65;
	Point2D velocity = new Point2D((Math.random()*(15 - -15 + 1) + -15), Math.random()*(15 - -15 + 1) + -15);
	
	public DebrisEffect3(Game game, double expireTime, double radius, float x, float y,  Point2D velocity) {
		this.particleManager = game.getParticleManager();
		this.game = game;
		this.radius = radius/2;
		this.shape.setRadius(this.radius);
		this.velocity = velocity;
		this.decay = 0.016/expireTime; 
		this.x = x;
		this.y = y;
		if(Settings.ADD_GLOW){
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setColor(Color.rgb(255, 150, 0,1));
		borderGlow.setWidth(depth);
		borderGlow.setHeight(depth);
		borderGlow.setSpread(0.85);
		borderGlow.setBlurType(BlurType.TWO_PASS_BOX);
		shape.setEffect(borderGlow);
		shape.setBlendMode(BlendMode.ADD);
		}
		init();
	}
	public void init(){
        shape.setFill(Color.rgb(255, (int)greenRange, 0,1));
       // shape.setStroke(Color.rgb(255, (int)greenRange+10, 0,1));
        game.getDebrisLayer().getChildren().add(shape);
	}
	public void update(){
		x += velocity.getX()+Settings.GLOBAL_ACCELARATION*.8;
		y += velocity.getY();
		lifeTime -= decay;
		radius-=0.05;
		this.shape.setRadius(this.radius);
		if(radius<=0){
			lifeTime =0;
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

