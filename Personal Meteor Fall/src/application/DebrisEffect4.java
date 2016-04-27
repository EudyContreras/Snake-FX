package application;


import java.util.Random;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

public class DebrisEffect4 extends DebrisEffect{

	GameDebrisID id;
	Random rand = new Random();
	Paint color;
	int depth = 75;
	int amount = 200;
	int lightPos = 150;
	double radius = 25;
	double decay;
	double x;
	double y;
	double r;
	double velX = 0;
	double velY = 0;
	double velR;
	float lifeTime = 5.0f;
	float amountOfBlur = 10.0f;
	float randomRotationLeft = rand.nextFloat()*(-1.8f - -4.5f + 1)+ -4.5f;
	float randomRotationRight = rand.nextFloat()*(4.5f - 1.8f + 1)+ 1.8f;
	double width;
	double height;
	double gravity = 2;
	double energyLoss = .3;
	double frictionX = .7;
	double deltaTime = .2;
	double clearUp;
	boolean isAlive = false;
	boolean removable = false;
    Light.Point light;
    Lighting lighting;
    GameTileManager tileManager;
	Point2D velocity = new Point2D((Math.random()*(-6 - -10 + 1) + -10), Math.random()*(12 - -12 + 1) + -12);


	public DebrisEffect4(Game game, float x, float y, Point2D velocity, GameDebrisID id) {
		this.game = game;
		this.tileManager = game.getloader().tileManager;
		if(Settings.ADD_VARIATION)
		this.radius = rand.nextDouble()*(25 - 5 + 1)+ 5;
		this.shape.setRadius(radius);
		if(Settings.ALLOW_PHYSICS)
		this.velocity = new Point2D((Math.random()*(4 - -15+ 1) + -15), Math.random()*(10 - -17 + 1) + -17);
		if(!Settings.ALLOW_PHYSICS)
		this.velocity = new Point2D((Math.random()*(15 - -30+ 1) + -30), Math.random()*(15 - -15 + 1) + -15);	
		this.decay = 0.016; 
		this.x = x;
		this.y = y;
		this.id = id;
		this.clearUp = 0.512;
		this.game = game;		
		init();
	}
	public void init(){
		if(Settings.ADD_LIGHTING){
		  // light = new Light.Point();
		  // lighting = new Lighting();
		}
        game.getDebrisLayer().getChildren().add(shape);
        shape.setFill(imagePattern);
		velX = (float) velocity.getX()+Settings.GLOBAL_ACCELARATION/4;
		velY = (float) velocity.getY();
		motionBlur.setIterations(1);
		this.shape.setEffect(motionBlur);
		if(velX>0){
			velR = randomRotationRight;
		}
		if(velX<0){
			velR = randomRotationLeft;
		}
	}
	public void addBlur(){	
		 amountOfBlur -= clearUp;
			if(amountOfBlur<=0){
				amountOfBlur = 0;
			}
			motionBlur.setWidth(amountOfBlur);
			motionBlur.setHeight(amountOfBlur);
	}
    public void addLighting(){
		light.setX(0);   	  	
    	light.setY(700);  
    	if(x>500 && x + game.getloader().getPlayer().getX()<1550){
    	if(Math.abs(y - game.getloader().getPlayer().getY()) < 200){
    		lightPos+=5;
    		if(lightPos>=400){
    			lightPos = 400;
    		}
    		
    	}
    	}
    	if(x < 500 ){
    		lightPos-=5;
    		if(lightPos<=50){
    			lightPos = 50;
    		}
    	}
    	if(x>500 && Math.abs(y - game.getloader().getPlayer().getY()) > 200){
    		if(lightPos!=150){
    			lightPos-=5;
    		}
    	}
    	light.setZ(lightPos);
    }
	public void update(){
		if(Settings.ADD_BLUR)
		addBlur();
//		if(Settings.ADD_LIGHTING)
//		addLighting();
	}
	public void move(){
		x += velX+Settings.GLOBAL_ACCELARATION*(Settings.FRAMECAP);
		y += velY*(Settings.FRAMECAP);
		r += velR*(Settings.FRAMECAP);			
		shape.setCenterX(x);
		shape.setCenterY(y);
		shape.setRotate(r);
		if(Settings.ALLOW_PHYSICS)
		addPhysics();

	}
	public void draw(GraphicsContext gc) {
		drawBoundingBox(gc);
	}

	public void addPhysics(){
		
		if(y < Settings.START_Y-radius-1){
			y = Settings.START_Y-radius-1;
			velY *=  energyLoss;
			velY = -velY;
			velX-=0.1;
		}
		if(y > Settings.HEIGHT-radius-1){
			y = Settings.HEIGHT-radius-1;
			velY *=  energyLoss;
			velY = -velY;
			velX-=0.1;
		}else{
			
			velY = velY + gravity*deltaTime;
			y += velY*deltaTime + .5*gravity*deltaTime*deltaTime;
		}
		if(y == Settings.HEIGHT-radius || y == Settings.HEIGHT-radius-1){
			if(velX>-10)
				velX-=0.1;
			if(velX<=-10)
			velX=-10;
			velR = -5;
		}
	}
	public void checkBounds(Shape cicle) {

	}
	public void collide() {
		if (Settings.ALLOW_PHYSICS) {
			for (int i = 0; i < game.getObjectManager().object.size(); i++) {
				GameObject object = game.getObjectManager().object.get(i);
				if (object.getId() == GameObjectID.player) {

					if (getBounds().intersects(object.getBoundsTop())) {
						if(velX>=0)
						velX -=2;
						
						velY *=  0.6;
						velY = -Math.abs(velY) + object.getVelY()*.5;
					}
					if (getBounds().intersects(object.getBoundsLeft())) {
						velX = -Math.abs(velX * .70);
					}
					if (!Settings.AFTERBURNER) {
						if (getBounds().intersects(object.getBoundsRight())) {
							velX = Math.abs(velX * .70);
						}

						if (getBounds().intersects(object.getBounds())) {
							if (velX > 0)
								velX = Math.abs(velX);
							if (velX < 0)
								velX = -Math.abs(velX);
							if (velY < 0.1 && x < object.getX() + object.getWidth() / 2)
								velX = -5;
							if (velY < 0.1 && x > object.getX() + object.getWidth() / 2)
								velX = 5;
							velY = 8;
						}
					}
				}
			}
			for (int i = 0; i < game.getObjectManager().object.size(); i++) {
					GameObject object = game.getObjectManager().object.get(i);
				if (object.getId() == GameObjectID.asteroid) {
					if (getBounds().intersects(object.getBounds())) {
						velY = Math.abs(velY * .60);
					}
					if (getBounds().intersects(object.getBoundsTop())) {
						velY = -Math.abs(velY * .60);
					}
					if (getBounds().intersects(object.getBoundsRight())) {
						velX = -Math.abs(velX * .60);
					}
					if (getBounds().intersects(object.getBoundsLeft())) {
						velX = -Math.abs(velX * .60) + object.getVelX()*.5;
					}
				}
			}
		
		for(int i = 0; i<tileManager.barrel.size(); i++){
			Tile tempTile = tileManager.barrel.get(i);
			if(tempTile.getId() == LevelObjectID.ExplosiveBarrel){
				if(getBounds().intersects(tempTile.getBoundsTop())){	
					velY = -Math.abs(velY * .60);
				}
				if(getBounds().intersects(tempTile.getBoundsRight())){	
					velX = -Math.abs(velX * .60);
				}
				if(getBounds().intersects(tempTile.getBoundsLeft())){	
					velX = -Math.abs(velX * .60) + tempTile.getVelX()*.5;
				}
			}
		}
		for(int i = 0; i<tileManager.box.size(); i++){
			Tile tempTile = tileManager.box.get(i);
			if(tempTile.getId() == LevelObjectID.CollideBox){
				if(getBounds().intersects(tempTile.getBoundsTop())){	
					velY = -Math.abs(velY * .60);
				}
				if(getBounds().intersects(tempTile.getBoundsRight())){	
					velX = -Math.abs(velX * .60);
				}
				if(getBounds().intersects(tempTile.getBoundsLeft())){	
					velX = -Math.abs(velX * .60) + tempTile.getVelX()*.5;
				}
			}
		}
		}
	}
	public boolean isAlive() {
		if(Settings.ALLOW_PHYSICS){
			//return velY!=0 && velX!=0;
			return 	x> 0-radius  && x<Settings.WIDTH+radius;
		}
		return x > -20 && x < Settings.WIDTH+20 && y>Settings.START_Y-20 && y<Settings.HEIGHT+20;
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D(x-radius+2, y-radius+2, radius*2-4, radius*2-4);
	}
	
	public Rectangle2D getBoundsTop() {
		return new Rectangle2D(((int)x+(width/2)-((width/2)/2)),  (int)y+50, (width/2), (height/3));
	}
	
	public Rectangle2D getBoundsRight() {
		return new Rectangle2D(((int)x+(width-60)),(int)y+75, 15, Math.abs(height-150));
	}
	
	public Rectangle2D getBoundsLeft() {
		return new Rectangle2D((int)x+40, (int)y+75, 15, height-height/2);
	}
    public void drawBoundingBox(GraphicsContext gc){

    	if(Settings.DEBUG_MODE){
     	gc.setStroke(Color.BLACK);
     	gc.strokeRect(x-radius+2, y-radius+2, radius*2-4, radius*2-4);
    	}
     }
	public GameDebrisID getID() {
		return id;
	}
	public void setID(GameDebrisID id) {
		this.id = id;	
	}
}

