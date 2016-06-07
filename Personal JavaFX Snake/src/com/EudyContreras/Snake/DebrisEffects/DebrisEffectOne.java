package com.EudyContreras.Snake.DebrisEffects;
import com.EudyContreras.Snake.AbstractModels.AbstractDebrisEffect;
import com.EudyContreras.Snake.EnumIDs.GameDebrisID;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class DebrisEffectOne extends AbstractDebrisEffect{

	private GameDebrisID id;
	private double radius;
	private double decay;
	private float x;
	private float y;
	private float lifeTime = 1.0f;
	private Point2D velocity = new Point2D((Math.random()*(15 - -15 + 1) + -15), Math.random()*(15 - -15 + 1) + -15);

	public DebrisEffectOne(GameManager game,Image image, double expireTime, double radius, float x, float y,  Point2D velocity) {
		this.game = game;
		this.radius = radius/2;
		this.imagePattern = new ImagePattern(image);
		this.shape = new Circle();
		this.shape.setRadius(radius);
		this.radius = shape.getRadius();
		this.velocity = velocity;
		this.decay = 0.016/expireTime;
		this.x = x;
		this.y = y;
		init();
	}
	public void init(){
        shape.setFill(imagePattern);
		shape.setBlendMode(BlendMode.ADD);
        game.getFirstLayer().getChildren().add(shape);
	}
	public void update(){
		x += velocity.getX();
		y += velocity.getY();
		lifeTime -= decay;
		radius-=1;
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
		return x<GameSettings.WIDTH && x>0 && y<GameSettings.HEIGHT  && y>0 && lifeTime>0;
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

