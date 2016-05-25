
package com.SnakeGame.AbstractModels;

import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.IDenums.GameLevelObjectID;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class is the game tile super class and is the class that every tile
 * object or level object extends. Events within this class will reflect on the
 * children of this class.
 *
 * @author Eudy Contreras
 *
 */
public abstract class AbstractTile {

	protected Image image;
	protected ImageView view = new ImageView();
	protected float x;
	protected float y;
	protected float r;
	protected float velX;
	protected float velY;
	protected float velR;
	protected boolean status = true;
	protected double width;
	protected double height;
	protected GameLevelObjectID id;

	public AbstractTile(float x, float y) {
		this.x = x;
		this.y = y;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	public float getVelR() {
		return velR;
	}

	public void setVelR(float velR) {
		this.velR = velR;
	}

	public void setR(float r) {
		this.r = r;
	}

	public AbstractTile(float x, float y, Image image) {
		this.x = x;
		this.y = y;
		this.image = image;
		this.view.setImage(image);
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	public AbstractTile(float x, float y, Image image, GameLevelObjectID id) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.image = image;
		this.view.setImage(image);
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	public AbstractTile(float x, float y, double width, double height, Image image, GameLevelObjectID id) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.image = image;
		this.view.setImage(image);
		this.width = width;
		this.height = height;
		this.view.setFitWidth(width);
		this.view.setFitHeight(height);
	}
	public ImageView getView() {
		return view;
	}

	public void setView(ImageView view) {
		this.view = view;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getR() {
		return r;
	}
	public void relocate(float x, float y) {
		view.setTranslateX(x);
		view.setTranslateY(y);
		view.setRotate(r);
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public void move() {
		x = x + velX * GameSettings.FRAME_SCALE;
		y = y + velY * GameSettings.FRAME_SCALE;
		r = r + velR * GameSettings.FRAME_SCALE;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}


	public void setId(GameLevelObjectID id) {
		this.id = id;
	}

	public GameLevelObjectID getId() {
		return id;
	}

	public boolean isBehindCharacter() {
		if (view.getFitWidth() > 0)
			return x < 0 - view.getFitWidth() * 98;
		else
			return x < 0 - image.getWidth() * 98;
	}

	public boolean isAlive() {
		return status;
	}

	public void setAlive(boolean status) {
		this.status = status;
	}

	public void updateUI() {
		view.setTranslateX(x);
		view.setTranslateY(y);
		view.setRotate(r);
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D(x, y, width, height);
	}

	public Rectangle2D getBoundsTop() {
		return new Rectangle2D(x + 20, y, width - 40, height);
	}

	public Rectangle2D getBoundsRight() {
		return new Rectangle2D(x + width - 20, y + 10, 20, height - 10);
	}

	public Rectangle2D getBoundsLeft() {
		return new Rectangle2D(x, y + 10, 20, height - 10);
	}

	public Bounds getCollisionBounds() {
		return this.view.getLayoutBounds();
	}

	public Bounds getBounds2D() {
		return null;
	}

	public void checkCollision() {

	}

}
