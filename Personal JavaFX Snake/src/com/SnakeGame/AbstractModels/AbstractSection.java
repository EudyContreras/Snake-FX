package com.SnakeGame.AbstractModels;

import java.util.ArrayList;
import java.util.Arrays;

import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.IDEnums.GameObjectID;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class is the game object super class and is the class that every game
 * object or mob extends. Actions performed within this class will reflect on
 * the children of this class.
 *
 * @author Eudy Contreras
 *
 */
public abstract class AbstractSection {

	protected GameObjectID id;
	protected PlayerMovement direction = PlayerMovement.STANDING_STILL;
	protected Image image;
	protected ImageView imageView;
	protected ArrayList<Point2D> lastPosition = new ArrayList<>();
	protected ArrayList<Enum<PlayerMovement>> lastDirection = new ArrayList<>();
	protected Point2D position;
	protected Pane layer;
	protected Node node;
	protected Rectangle rect;
	protected Circle circle;
	protected double x;
	protected double y;
	protected double r;
	protected double velX;
	protected double velY;
	protected double velR;
	protected double width;
	protected double height;
	protected double radius;
	protected double health = 50;
	protected double damage;
	protected int numericID;
	protected boolean isAlive = false;
	protected boolean removable = false;
	protected boolean canMove = true;
	protected boolean drawBounds = false;
	protected boolean hitBounds = false;
	protected boolean hitBoundsTop = false;
	protected boolean hitBoundsRight = false;
	protected boolean hitBoundsLeft = false;

	/**
	 * The constructors used in this class allows objects to be created in
	 * different ways and with different attributes
	 */

	public AbstractSection(GameManager game, Pane layer, Node node, double x, double y, GameObjectID id) {
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.id = id;
		if (node instanceof Rectangle) {
			this.rect = (Rectangle) node;
			this.rect.setTranslateX(x);
			this.rect.setTranslateY(y);
			this.rect.setRotate(r);
			this.width = rect.getWidth();
			this.height = rect.getHeight();
			addToLayer(rect);
		} else if (node instanceof Circle) {
			this.circle = (Circle) node;
			this.circle.setTranslateX(x);
			this.circle.setTranslateY(y);
			this.circle.setRotate(r);
			this.radius = circle.getRadius();
			addToLayer(circle);
		} else if (node instanceof Rectangle) {

		}

	}

	public AbstractSection(GameManager game, Pane layer, Node node, GameObjectID id) {
		this.layer = layer;
		this.id = id;
		if (node instanceof Rectangle) {
			this.rect = (Rectangle) node;
			this.rect.setTranslateX(x);
			this.rect.setTranslateY(y);
			this.rect.setRotate(r);
			this.width = rect.getWidth();
			this.height = rect.getHeight();
			addToLayer(rect);
		} else if (node instanceof Circle) {
			this.circle = (Circle) node;
			this.circle.setTranslateX(x);
			this.circle.setTranslateY(y);
			this.circle.setRotate(r);
			this.radius = circle.getRadius();
			addToLayer(circle);
		} else if (node instanceof Rectangle) {

		}

	}

	public AbstractSection(Image image, double x, double y) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.imageView.setImage(image);
		this.width = image.getWidth();
		this.height = image.getHeight();
		// addToLayer();

	}

	public AbstractSection(GameManager game, Pane layer, GameObjectID id) {
		this.layer = layer;
		this.id = id;
	}

	public GameObjectID getId() {
		return id;
	}

	public void setId(GameObjectID id) {
		this.id = id;
	}

	public void addToLayer() {
		this.layer.getChildren().add(this.imageView);
	}

	public void addToLayer(Node node) {
		this.layer.getChildren().add(0, node);
	}

	public void addToCanvas() {
		this.layer.getChildren().add(this.imageView);
	}

	public void removeFromLayer() {
		this.layer.getChildren().remove(this.imageView);
		this.layer.getChildren().remove(this.circle);
	}

	public Pane getLayer() {
		return layer;
	}

	public void setLayer(Pane layer) {
		this.layer = layer;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	/**
	 * This method will relocate the object to specific point
	 *
	 * @param x
	 * @param y
	 */
	public void relocate(Point point) {
		imageView.setTranslateX(point.getX());
		imageView.setTranslateY(point.getY());
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

	public double getVelR() {
		return velR;
	}

	public void setVelR(double velR) {
		this.velR = velR;
	}

	public double getHealth() {
		return health;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public boolean isRemovable() {
		return removable;
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	/**
	 * This method is responsible for moving and rotating the object
	 */
	public void move() {
		if (!canMove)
			return;
		x = x + velX * GameSettings.FRAME_SCALE;
		y = y + velY * GameSettings.FRAME_SCALE;
		r = r + velR * GameSettings.FRAME_SCALE;
	}

	public boolean isAlive() {
		return Double.compare(health, 0) > 0;
	}

	public ImageView getView() {
		return imageView;
	}

	/**
	 * This method is responsible for visually updating the object
	 *
	 */
	public void updateUI() {
		circle.setTranslateX(x);
		circle.setTranslateY(y);
		circle.setRotate(r);
	}

	public void logicUpdate(){


	}
	public void createLevel() {

	}

	public void updateAnimation(long timePassed) {

	}

	public void addPhysics() {

	}

	public void draw(GraphicsContext gc) {

	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getCenterX() {
		return x + width * 0.5;
	}

	public double getCenterY() {
		return y + height * 0.5;
	}

	public Bounds getRadialBounds() {
		return circle.getBoundsInParent();
	}

	public Rectangle2D getBounds() {

		return new Rectangle2D(x, y, width, height);
	}

	public Rectangle2D getBoundsTop() {

		return new Rectangle2D(x, y, width, height);
	}

	public Rectangle2D getBoundsRight() {

		return new Rectangle2D(x, y, width, height);
	}

	public Rectangle2D getBoundsLeft() {

		return new Rectangle2D(x, y, width, height);
	}

	public void getDamagedBy(AbstractSection object) {
		health -= object.getDamage();
	}

	public void kill() {
		setHealth(0);
	}

	public void remove() {
		setRemovable(true);
	}

	public void stopMovement() {
		this.canMove = false;
	}

	/**
	 * Abstract methods every object must have in order to determined the
	 * condition in which the object will be removed and the objects collision
	 * boundaries
	 */
	public void checkRemovability(){

	}
	public void checkCollision(){

	}
	protected void removePerformedCoordinateChange() {
		lastPosition.remove(0);
		lastDirection.remove(0);
	}

	protected void removeLatestLocation() {
		lastPosition.remove(0);
	}

	protected void removeLatestDirection() {
		lastDirection.remove(0);
	}

	public void setNewLocation(Point2D... location) {
		if (location.length > 1) {
			lastPosition.addAll(Arrays.asList(location));
		} else {
			lastPosition.add(location[0]);
		}

	}

	public void setNewDirection(PlayerMovement... direction) {
		if (direction.length > 1) {
			lastDirection.addAll(Arrays.asList(direction));
		} else {
			lastDirection.add(direction[0]);
		}
	}

	protected void setLastDirection(PlayerMovement direction) {
		this.direction = direction;
	}

	public PlayerMovement getLastDirection() {
		return direction;
	}

	protected void setLastPosition(Point2D position) {
		this.position = position;
	}

	protected Point2D getLastPosition() {
		return position;
	}

	protected void setNumericID(int SECTION_COUNT) {
		this.numericID = SECTION_COUNT;
	}

	public int getNumericID() {
		return numericID;
	}

	public void die() {

	}
}
