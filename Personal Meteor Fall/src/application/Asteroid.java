package application;


import java.util.Random;



import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class Asteroid extends GameObject {
	Game game;
	Boolean blowUp = false;
	Boolean blowUp2 = false;
	Player player;
	GameObjectManager objectManager;
	GameParticleManager particleManager;
	GameTileManager tileManager;
	Random rand = new Random();
    Light.Point light = new Light.Point();
    Lighting lighting = new Lighting();
    InnerShadow innerShadow = new InnerShadow();
    double imageWidth = 350;
    double imageHeight = 350;
    int depth = 200;
    float lightPos = 150;
	double particleSize = 15;
	double particleLife = 2; 
	double accelaration = 0.01;
	float intensity = 0;
	double size = 168;
	
    public Asteroid(Game game, Pane layer, Image image, double size, float x, float y, float r, float velX, float velY, GameObjectID id) {
        super(game, image, layer, x, y, r, velX, velY, id);
        this.game = game;
        this.size = size;
        this.tileManager = game.getloader().tileManager;
        this.velR = rand.nextFloat()*(1.2f - -1.2f + 1)+ -1.2f;
        this.objectManager = game.getObjectManager();
        this.particleManager = game.getParticleManager();
        this.player = game.getloader().getPlayer();
        this.imageView.setFitWidth(size);
        this.imageView.setFitHeight(size);
        this.imageView.setPreserveRatio(true);
        this.light.setX(0);   	  	
        this.light.setY(700); 
        this.light.setZ(150);
		this.lighting.setSurfaceScale(5.0);
		this.lighting.setLight(light);
		this.lighting.setContentInput(innerShadow);
		this.lighting.setDiffuseConstant(2.0);
		this.lighting.setSpecularConstant(0.5);
	//	this.innerShadow.setInput(lighting);
		this.innerShadow.setBlurType(BlurType.ONE_PASS_BOX);
		this.innerShadow.setRadius(127);
		this.innerShadow.setColor(Color.rgb(255, 170, 0, 0));
		this.imageWidth = size;
		this.imageHeight = size;
	   	if(Settings.ADD_LIGHTING)
		this.imageView.setEffect(lighting);
    }
    public void checkRemovability() {
        if( x < 0-width || y<0-height || y > Settings.HEIGHT+height) {
            setRemovable(true);
        }
    }
    public void move() {
        if( !canMove)
            return;
		x+=velX+Settings.GLOBAL_ACCELARATION*(Settings.FRAMECAP);
		y+=velY*(Settings.FRAMECAP);
        r = r + velR*(Settings.FRAMECAP); 	
    }
    public void addFakeLighting(){ 
    	if(game.getloader().getPlayer().lampOff == false)
    		light.setZ(lightPos);
    	else if(game.getloader().getPlayer().lampOff == true)
        	light.setZ(120);
    	if((x>500 && x + game.getloader().getPlayer().getX()<1650) && (Math.abs(y - game.getloader().getPlayer().getY()) <= 200)){
    		intensity+=0.5*(Settings.FRAMECAP);
    		lightPos+=intensity;
    	}
    	else if(x>500 && Math.abs(y - game.getloader().getPlayer().getY()) > 200){
			lightPos-=15*(Settings.FRAMECAP);  
    	}
    	else if(x <= 500 ){
    		lightPos-=15*(Settings.FRAMECAP);;
    	}
    	if(intensity>=15){
    		intensity = 15*(Settings.FRAMECAP);
    	}
		if(lightPos>=400){
			lightPos = 400;
		}
		if(lightPos<=120){
			lightPos = 120;
		}
	
    }
    public void addLighting(){ 
    	if(x>500 && x + game.getloader().getPlayer().getX()<1550){
    	if(Math.abs(y - game.getloader().getPlayer().getY()) < 200){
    		intensity+=0.5;
    		lightPos+=intensity;
    		if(lightPos>=400){
    			lightPos = 400;
    		}
    		
    	}
    	}
    	if(x < 500 ){
    		lightPos-=15;
    		if(lightPos<=120){
    			lightPos = 120;
    		}
    	}
    	if(x>500 && Math.abs(y - game.getloader().getPlayer().getY()) > 200){
    		if(lightPos>=150){
    			lightPos-=15;
    		}
    	}
    	if(game.getloader().getPlayer().lampOff == false)
    		light.setZ(lightPos);
    	
    	if(game.getloader().getPlayer().lampOff == true)
        	light.setZ(150);
	
    }
    public void updateUI(){
    	super.updateUI();
    }
    public void addPhysics(){
		if(blowUp == true){
			createDebris();
		}
		if(blowUp2 == true){
			createDebris2();
		}
    }
    public void explosionLight(){
		if (Settings.DIRECT_HIT == true) {
			if (x > Settings.EXPLOSION_POSITION_X) {
				if (Math.abs(x - Settings.EXPLOSION_POSITION_X) < 400
						&& Math.abs(y - Settings.EXPLOSION_POSITION_Y) < 400)
					this.innerShadow.setColor(Color.rgb(255, 150, 0, player.getLightOpacity()));
				else
					this.innerShadow.setColor(Color.TRANSPARENT);

			} 
			else if (x < Settings.EXPLOSION_POSITION_X) {
				if (Math.abs(x - Settings.EXPLOSION_POSITION_X) < 400 + width
						&& Math.abs(y - Settings.EXPLOSION_POSITION_Y) < 400)
					this.innerShadow.setColor(Color.rgb(255, 150, 0, player.getLightOpacity()));
				else
					this.innerShadow.setColor(Color.TRANSPARENT);
			}
		}
    }
    public void checkCollision() {
		for(int i = 0; i<game.getObjectManager().object.size(); i++){
			GameObject tempObject = game.getObjectManager().object.get(i);
			if(tempObject.getId() == GameObjectID.laser){
				if(getBoundsLeft().intersects(tempObject.getBounds())){
					if(Math.abs(x - game.getloader().getPlayer().getX())<450){
					game.getloader().getPlayer().shakeDuration = 90;
					game.getloader().getPlayer().shake = true;
					}
					tempObject.setRemovable(true);
					this.setRemovable(true);
					blowUp = true;	
					blowUp2 = false;
				}
			}	
			if (!game.getloader().getPlayer().isDead) {
				if (tempObject.getId() == GameObjectID.player) {
					if (getBoundsTop().intersects(tempObject.getBounds())) {
						if (!game.getloader().getPlayer().shake)
							velY = Math.abs(tempObject.getVelY());
					}
					if (getBounds().intersects(tempObject.getBoundsTop())) {
						if (!game.getloader().getPlayer().shake)
							velY = -Math.abs(tempObject.getVelY());
					}
				}
			}
//			if(tempObject.getId() == GameObjectID.asteroid){
//				if(getBoundsTop().intersects(tempObject.getBounds())){	
//					velY+=0.01f;
//				}
//				if(getBounds().intersects(tempObject.getBoundsTop())){	
//					velY-=0.01f;
//				}
//				if(getBoundsRight().intersects(tempObject.getBoundsLeft())){	
//					velX-=0.05f;
//				}
//			}
		}
		for(int i = 0; i<tileManager.box.size(); i++){
			Tile tempTile = tileManager.box.get(i);
			if(tempTile.getId() == LevelObjectID.CollideBox){
				if(getBounds().intersects(tempTile.getBoundsTop())){	
					this.remove();
				}
				if(getBounds().intersects(tempTile.getBoundsRight())){	
					this.remove();
				}
				if(getBounds().intersects(tempTile.getBoundsLeft())){	
					this.remove();
				}
			}
		}
	}
    public void draw(GraphicsContext gc){
    	if(Settings.ADD_LIGHTING){
    		if(!player.isDead()){
    		explosionLight();
    		addFakeLighting();
 //   		addLighting();
    		}
    	}
    	drawBoundingBox(gc);
    }
    public void drawBoundingBox(GraphicsContext gc){

    	if(Settings.DEBUG_MODE){
    		//this.imageView.setVisible(false);
     	gc.setStroke(Color.BLACK);
     	//top
     	gc.strokeRect (x+30, y, imageWidth-60, imageHeight-(imageHeight/2));
     	//left
     	gc.strokeRect (x, y, 30, imageHeight);
     	//right
     	gc.strokeRect (x+imageWidth-30, y, 30, imageHeight);
     	//bottom
     	gc.strokeRect (x+30, y+imageHeight/2, imageWidth-60, imageHeight-(imageHeight/2));
    	}
     }
	public void createDebris() {
		game.getObjectManager().addObject(new ExplosionLight(game, game.getPlayfieldLayer(), GameImageBank.explosionLight,400, 0.1, x-100, y-100, -15, 0, null));
		for (int i = 0; i < Settings.PARTICLE_LIMIT; i++) {
			if(Settings.ADD_VARIATION){
				particleSize = Math.random()*(200 - 40 +1)+40;
				particleLife = Math.random()*(0.5 - 0.1+1)+0.1;
			}
			game.getDebrisManager().addObject(new DebrisEffect6(game,GameImageBank.glowingCircle, particleLife,particleSize,(float) (x+width/2), (float) (y+height/2),  new Point2D((Math.random()*(12 - -12 + 1) + -12), Math.random()*(12 - -12 + 1) + -12)));
			game.getDebrisManager().addObject(new DebrisEffect6(game,GameImageBank.glowingCircle1, particleLife,particleSize,(float) (x+width/2), (float) (y+height/2),  new Point2D((Math.random()*(12 - -12 + 1) + -12), Math.random()*(12 - -12 + 1) + -12)));
			game.getDebrisManager().addObject(new DebrisEffect4(game, (float) (x+width/2), (float) (y+height/2), new Point2D((Math.random()*(5 - -5 + 1) + -6), Math.random()*(5 - -5 + 1) + -5), GameDebrisID.effect4));
			game.getDebrisManager().addObject(new DebrisEffect2(game,GameImageBank.glowingCircle, (float) (x+width/2), (float) (y+height/2), new Point2D((Math.random()*(18 - -18 + 1) + -18), Math.random()*(18 - -18 + 1) + -18)));
			blowUp = false;
		}
	}
	public void createDebris2() {
		for (int i = 0; i < 130; i++) {
				
		}
		blowUp2 = false;
	}
	public Rectangle2D getBounds() {
		return new Rectangle2D(x+30, y+imageHeight/2, imageWidth-60, imageHeight-(imageHeight/2));
	}
	public Rectangle2D getBoundsTop() {
		return new Rectangle2D(x+30, y, imageWidth-60, imageHeight-(imageHeight/2));
	}
	public Rectangle2D getBoundsRight() {
		return new Rectangle2D(x+imageWidth-30, y, 30, imageHeight);
	}
	public Rectangle2D getBoundsLeft() {
		return new Rectangle2D(x, y, 30, imageHeight);
	}
}