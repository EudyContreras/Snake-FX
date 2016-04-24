package com.SnakeGame.Core;

import java.util.Random;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class FruitSplash2 extends DebrisEffect{

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

	public FruitSplash2(SnakeGame game,Paint fill, double expireTime, double radius, float x, float y) {
		this.game = game;
		this.radius = radius/2;
		this.shape = new Circle();
		this.shape.setRadius(this.radius);
		this.decay = 0.016/expireTime;
		this.color = fill;
		this.x = x;
		this.y = y;
		init();
	}
	public void init(){
        shape.setFill(color);
        game.getDebrisLayer().getChildren().add(shape);
	}
	public void update(){
		x = x + velX;
		y = y + velY;
		lifeTime -= decay;
//		radius-=1;
//		this.shape.setRadius(this.radius);
//		if(radius<=0){
//			lifeTime =0;
//		}
	}
	public void move(){

		shape.setCenterX(x);
		shape.setCenterY(y);

	}
	public void collide(){}
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

