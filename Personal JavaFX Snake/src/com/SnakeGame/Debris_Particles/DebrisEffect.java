package com.SnakeGame.Debris_Particles;

import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ObjectIDs.GameDebrisID;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public abstract class DebrisEffect {

	SnakeGame game;
	protected Circle shape;
	ImageView view;
	ImagePattern imagePattern;
	DropShadow borderGlow;
	Bloom bloom;
	BoxBlur motionBlur;
	double x;
	double velX;
	double y;
	double velY;
	double r;
	double velR;

	public DebrisEffect() {

	}

	public abstract void update();

	public abstract void draw(GraphicsContext gc);

	public void move() {
		x = x + velX * Settings.FRAME_SCALE;
		y = y + velY * Settings.FRAME_SCALE;
		r = r + velR * Settings.FRAME_SCALE;
	}

	public abstract void collide();

	public abstract boolean isAlive();

	public abstract GameDebrisID getID();

	public abstract void setID(GameDebrisID id);

	public abstract Rectangle2D getBoundsTop();

	public abstract Rectangle2D getBounds();

	public abstract Rectangle2D getBoundsRight();

	public abstract Rectangle2D getBoundsLeft();

	public Circle getShape() {
		return shape;
	}
}
