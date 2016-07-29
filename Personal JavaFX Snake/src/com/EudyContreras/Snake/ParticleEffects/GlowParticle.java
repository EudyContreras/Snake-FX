package com.EudyContreras.Snake.ParticleEffects;
import com.EudyContreras.Snake.AbstractModels.AbstractParticlesEffect;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameDebrisID;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class GlowParticle extends AbstractParticlesEffect{

	private GameDebrisID id;
	private double radius;
	private double decay;
	private float lifeTime = 1.0f;


	public GlowParticle(GameManager game,Image image, double expireTime, double radius, double x, double y,  Point2D velocity) {
		this.game = game;
		this.radius = radius/2;
		this.shape = new Circle(-200,-200,0);
		this.imagePattern = new ImagePattern(image);
		this.shape.setRadius(this.radius);
		this.decay = 0.016/expireTime;
		this.velX = velocity.getX();
		this.velY = velocity.getY();
		this.x = x;
		this.y = y;
		init();
	}
	public void init(){
        shape.setFill(imagePattern);
		shape.setBlendMode(BlendMode.ADD);
		layer = game.getOuterParticleLayer();
		addToLayer(shape);
	}
	public void updateUI(){
		shape.setOpacity(lifeTime);
		shape.setCenterX(x);
		shape.setCenterY(y);

	}
	public void move(){
		x += velX;
		y += velY;
		lifeTime -= decay;
		radius-=0.2;
		if(radius<=0){
			lifeTime =0;
		}
	}
	public void collide(){


	}
	public boolean isAlive() {
		return x<GameSettings.WIDTH && x>0 && y<GameSettings.HEIGHT  && y>0 && lifeTime>0;
	}
	public void draw() {

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

