package com.SnakeGame.ParticleEffects;
import java.util.Random;

import com.SnakeGame.AbstractModels.AbstractDebrisEffect;
import com.SnakeGame.EnumIDs.GameDebrisID;
import com.SnakeGame.FrameWork.GameDebrisManager;
import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.FrameWork.GameSettings;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class GlowParticle extends AbstractDebrisEffect{

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
	GameDebrisManager particleManager;

	public GlowParticle(GameManager game,Image image, double expireTime, double radius, float x, float y,  Point2D velocity) {
		this.particleManager = game.getDebrisManager();
		this.game = game;
		this.radius = radius/2;
		this.shape = new Circle(-200,-200,0);
		this.imagePattern = new ImagePattern(image);
		this.shape.setRadius(this.radius);
		this.velocity = velocity;
		this.decay = 0.016/expireTime;
		this.x = x;
		this.y = y;
		init();
	}
	public void init(){
        shape.setFill(imagePattern);
		shape.setBlendMode(BlendMode.ADD);
        game.getOuterParticleLayer().getChildren().add(shape);
	}
	public void update(){
		x += velocity.getX();
		y += velocity.getY();
		lifeTime -= decay;
		radius-=1;
		this.shape.setRadius(this.radius);
		//this.shape.setOpacity(lifeTime);
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
		return x<GameSettings.WIDTH && x>0 && y<GameSettings.HEIGHT  && y>0 && lifeTime>0;
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

