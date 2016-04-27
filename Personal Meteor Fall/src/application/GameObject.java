package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public abstract class GameObject {

	GameObjectID id;
    protected Image image;
    protected ImageView imageView = new ImageView();
    protected Pane layer;
    protected float x;
    protected float y;
    protected float r;
    protected float velX;
    protected float velY;
    protected float velR;
    protected double width;
    protected double height;
    public double health = 50;
    public double damage;
    public float accelarationX;
    public float accelarationY;
    protected float stopX;
	protected float stopY;
	protected boolean isAlive = false;
    protected boolean removable = false;
    protected boolean canMove = true;
	protected boolean drawBounds = false;
	protected boolean hitBounds= false;
	protected boolean hitBoundsTop= false;
	protected boolean hitBoundsRight= false;
	protected boolean hitBoundsLeft= false;
	
    public GameObject(Game game, Pane layer, Image image, float x, float y, float r, float velX, float velY, float velR, double health, double damage, GameObjectID id) {

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
    public GameObject(Game game, Image image, Pane layer, float x, float y, float r, float velX, float velY, GameObjectID id) {
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
    
    public GameObject(Game game, Pane layer, float x, float y, float r, float velX, float velY, GameObjectID id) {
    	this.layer = layer;
        this.x = x;
        this.y = y;
        this.r = r;
        this.velX = velX;
        this.velY = velY;
        this.id = id;
        addToLayer();

    }
    public GameObject(Game game, Pane layer, Image image, float x, float y, GameObjectID id) {
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
    public GameObject( Image image, float x, float y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.imageView.setImage(image);
        this.imageView.setCache(true);
        this.imageView.setCacheHint(CacheHint.SPEED);
        this.width = image.getWidth(); 
        this.height = image.getHeight(); 
     //   addToLayer();

    }
    public GameObject(Game game, Pane layer, GameObjectID id) {
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
	public void addToCanvas() {
        this.layer.getChildren().add(this.imageView);
    }
    public void removeFromLayer() {
        this.layer.getChildren().remove(this.imageView);
    }
    public Pane getLayer() {
        return layer;
    }
    public void setLayer(Pane layer) {
        this.layer = layer;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public void relocate(float x, float y){
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
    }
    public float getR() {
        return r;
    }
    public void setR(float r) {
        this.r = r;
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
	public float getVelR() {
		return velR;
	}
	public void setVelR(float velR) {
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
    public void move() {
        if( !canMove)
            return;
        x = x + velX*(Settings.FRAMECAP);
        y = y + velY*(Settings.FRAMECAP);
        r = r + velR*(Settings.FRAMECAP);
    }
    public boolean isAlive() {
        return Double.compare(health, 0) > 0;
    }
    public ImageView getView() {
        return imageView;
    }
    public void updateUI() {
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        imageView.setRotate(r);
    }
    public void createLevel() {
    	
    }
    public void updateAnimation(long timePassed){
    	
    }
    public void addPhysics(){
    	
    }
    public void draw(GraphicsContext gc){
    	
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
    public Rectangle2D getBounds(){
  
        return new Rectangle2D(x,y,width,height);
    }
    public Rectangle2D getBoundsTop(){
    	  
        return new Rectangle2D(x,y,width,height);
    }
    public Rectangle2D getBoundsRight(){
    	  
        return new Rectangle2D(x,y,width,height);
    }
    public Rectangle2D getBoundsLeft(){
    	  
        return new Rectangle2D(x,y,width,height);
    }
    public void getDamagedBy( GameObject object) {
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

    public abstract void checkRemovability();
    public abstract void checkCollision();
}
