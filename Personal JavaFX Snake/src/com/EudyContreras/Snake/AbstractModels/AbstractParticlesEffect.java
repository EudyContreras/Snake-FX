package com.EudyContreras.Snake.AbstractModels;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameDebrisID;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public abstract class AbstractParticlesEffect {

	protected GameManager game;
	protected Circle shape;
	protected ImageView view;
	protected ImagePattern imagePattern;
	protected DropShadow borderGlow;
	protected Bloom bloom;
	protected BoxBlur motionBlur;
	protected Pane layer;
	protected double x;
	protected double velX;
	protected double y;
	protected double velY;
	protected double r;
	protected double velR;

	protected void addToLayer(Node node) {
		layer.getChildren().add(node);
	}

	public void remove() {
		layer.getChildren().remove(shape);
		layer.getChildren().remove(view);
	}

	public abstract void updateUI();

	public abstract void draw();

	public void move() {
		x = x + velX * GameSettings.FRAME_SCALE;
		y = y + velY * GameSettings.FRAME_SCALE;
		r = r + velR * GameSettings.FRAME_SCALE;
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

	public ImageView getView() {
		return view;
	}

	public void setView(ImageView view) {
		this.view = view;
	}
}
