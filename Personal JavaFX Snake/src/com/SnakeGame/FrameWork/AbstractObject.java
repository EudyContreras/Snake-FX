package com.SnakeGame.FrameWork;

import com.SnakeGame.IDenums.GameObjectID;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
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
public abstract class AbstractObject {

	protected GameObjectID id;
	protected Image image;
	protected ImageView imageView = new ImageView();
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
	protected double accelarationX;
	protected double accelarationY;
	protected double stopX;
	protected double stopY;
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
	 *
	 * @param game
	 * @param layer
	 * @param image
	 * @param x
	 * @param y
	 * @param r
	 * @param velX
	 * @param velY
	 * @param velR
	 * @param health
	 * @param damage
	 * @param id
	 */
	public AbstractObject(SnakeGame game, Pane layer, Image image, double x, double y, double r, double velX,
			double velY, double velR, double health, double damage, GameObjectID id) {

		this.layer = layer;
		this.image = image;
		this.x = x;
		this.y = y;
		this.r = r;
		this.velX = velX;
		this.velY = velY;
		this.velR = velR;
		this.id = id;
		this.health = health;
		this.damage = damage;
		this.imageView.setImage(image);
		this.imageView.setCache(true);
		this.imageView.setCacheHint(CacheHint.SPEED);
		this.imageView.setTranslateX(x);
		this.imageView.setTranslateY(y);
		this.imageView.setRotate(r);
		this.width = image.getWidth();
		this.height = image.getHeight();
		addToLayer();

	}

	public AbstractObject(SnakeGame game, Pane layer, Node node, double x, double y, double r, double velX, double velY,
			double velR, double health, double damage, GameObjectID id) {

		this.layer = layer;
		this.x = x;
		this.y = y;
		this.r = r;
		this.velX = velX;
		this.velY = velY;
		this.velR = velR;
		this.id = id;
		this.health = health;
		this.damage = damage;
		if (node instanceof Rectangle) {
			this.rect = (Rectangle) node;
			this.rect.setCache(true);
			this.rect.setCacheHint(CacheHint.SPEED);
			this.rect.setTranslateX(x);
			this.rect.setTranslateY(y);
			this.rect.setRotate(r);
			this.width = rect.getWidth();
			this.height = rect.getHeight();
			addToLayer(rect);
		} else if (node instanceof Circle) {
			this.circle = (Circle) node;
			this.circle.setCache(true);
			this.circle.setCacheHint(CacheHint.SPEED);
			this.circle.setTranslateX(x);
			this.circle.setTranslateY(y);
			this.circle.setRotate(r);
			this.radius = circle.getRadius();
			addToLayer(circle);
		}

	}

	public AbstractObject(SnakeGame game, Image image, Pane layer, double x, double y, double r, double velX,
			double velY, GameObjectID id) {
		this.layer = layer;
		this.image = image;
		this.x = x;
		this.y = y;
		this.r = r;
		this.velX = velX;
		this.velY = velY;
		this.id = id;
		this.imageView.setImage(image);
		this.imageView.setCache(true);
		this.imageView.setCacheHint(CacheHint.SPEED);
		this.imageView.setTranslateX(x);
		this.imageView.setTranslateY(y);
		this.imageView.setRotate(r);
		this.width = image.getWidth();
		this.height = image.getHeight();
		addToLayer();

	}

	public AbstractObject(SnakeGame game, Pane layer, double x, double y, double r, double velX, double velY,
			GameObjectID id) {
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.r = r;
		this.velX = velX;
		this.velY = velY;
		this.id = id;
		addToLayer();

	}

	public AbstractObject(SnakeGame game, Pane layer, Image image, double x, double y, GameObjectID id) {
		this.layer = layer;
		this.image = image;
		this.x = x;
		this.y = y;
		this.id = id;
		this.imageView.setImage(image);
		this.imageView.setCache(true);
		this.imageView.setCacheHint(CacheHint.SPEED);
		this.imageView.setTranslateX(x);
		this.imageView.setTranslateY(y);
		this.width = image.getWidth();
		this.height = image.getHeight();
		addToLayer();

	}

	public AbstractObject(SnakeGame game, Pane layer, Node node, double x, double y, GameObjectID id) {
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.id = id;
		if (node instanceof Rectangle) {
			this.rect = (Rectangle) node;
			this.rect.setCache(true);
			this.rect.setCacheHint(CacheHint.SPEED);
			this.rect.setTranslateX(x);
			this.rect.setTranslateY(y);
			this.rect.setRotate(r);
			this.width = rect.getWidth();
			this.height = rect.getHeight();
			addToLayer(rect);
		} else if (node instanceof Circle) {
			this.circle = (Circle) node;
			this.circle.setCache(true);
			this.circle.setCacheHint(CacheHint.SPEED);
			this.circle.setTranslateX(x);
			this.circle.setTranslateY(y);
			this.circle.setRotate(r);
			this.radius = circle.getRadius();
			addToLayer(circle);
		} else if (node instanceof Rectangle) {

		}

	}

	public AbstractObject(SnakeGame game, Pane layer, Node node, GameObjectID id) {
		this.layer = layer;
		this.id = id;
		if (node instanceof Rectangle) {
			this.rect = (Rectangle) node;
			this.rect.setCache(true);
			this.rect.setCacheHint(CacheHint.SPEED);
			this.rect.setTranslateX(x);
			this.rect.setTranslateY(y);
			this.rect.setRotate(r);
			this.width = rect.getWidth();
			this.height = rect.getHeight();
			addToLayer(rect);
		} else if (node instanceof Circle) {
			this.circle = (Circle) node;
			this.circle.setCache(true);
			this.circle.setCacheHint(CacheHint.SPEED);
			this.circle.setTranslateX(x);
			this.circle.setTranslateY(y);
			this.circle.setRotate(r);
			this.radius = circle.getRadius();
			addToLayer(circle);
		} else if (node instanceof Rectangle) {

		}

	}

	public AbstractObject(Image image, double x, double y) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.imageView.setImage(image);
		this.imageView.setCache(true);
		this.imageView.setCacheHint(CacheHint.SPEED);
		this.width = image.getWidth();
		this.height = image.getHeight();
		// addToLayer();

	}

	public AbstractObject(SnakeGame game, Pane layer, GameObjectID id) {
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
		this.layer.getChildren().add(node);
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
	public void logicUpdate(){

	}
	/**
	 * This method is responsible for moving and rotating the object
	 */
	public void move() {
		if (!canMove)
			return;
		x = x + velX * Settings.FRAME_SCALE;
		y = y + velY * Settings.FRAME_SCALE;
		r = r + velR * Settings.FRAME_SCALE;
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

	public void getDamagedBy(AbstractObject object) {
		health -= object.getDamage();
	}

	public void kill() {
		setHealth(0);
	}

	public void remove() {
		setRemovable(true);
	}

	public void bounce(PlayerOne snake, double x, double y) {

	}

	public void bounce(PlayerTwo snake, double x, double y) {

	}

	public void blowUp() {

	}

	public void stopMovement() {
		this.canMove = false;
	}

	public void checkRemovability() {

	}

	public void checkCollision() {

	}
}
