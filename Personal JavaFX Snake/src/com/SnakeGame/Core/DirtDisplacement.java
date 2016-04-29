package com.SnakeGame.Core;

import java.util.Random;

import com.SnakeGame.ObjectIDs.GameDebrisID;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class DirtDisplacement extends DebrisEffect{

	GameDebrisID id;
	Random rand = new Random();
	Paint color;
	double radius = Math.random()*(3.5 - 1+1)+1 /(GameLoader.ResolutionScaleX);
	double decay;
	double lifeTime = 1.0f;
	double width;
	double height;
	double energyLoss = 0.9;
	int depth = 75;
	int amount = 200;
	double greenRange = Math.random()*(190 - 40 + 1) +40;
	Point2D velocity = new Point2D((Math.random()*(10 - -10 + 1) + -10), Math.random()*(10 - -10 + 1) + -10);

	public DirtDisplacement(SnakeGame game,Image image, double x, double y,  Point2D velocity) {
		this.game = game;
		this.imagePattern = new ImagePattern(image);
		this.shape = new Circle(x,y,radius);
		this.shape.setRadius(radius);
		this.velocity = velocity;
		this.decay = 0.026;
		this.velX = (double) velocity.getX()/(GameLoader.ResolutionScaleX);
		this.velY = (double) velocity.getY()/(GameLoader.ResolutionScaleX);
		this.x = x;
		this.y = y;
		init();
	}
	public void init(){
        game.getDebrisLayer().getChildren().add(shape);
        shape.setFill(imagePattern);
	}
	public void update(){
		shape.setCenterX(x);
		shape.setCenterY(y);
		lifeTime -= decay;
		shape.setOpacity(lifeTime);
		velX*=energyLoss;
		velY*=energyLoss;
	}
	public void move(){
		super.move();
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

