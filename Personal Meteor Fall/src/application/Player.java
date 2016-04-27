package application;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;



public class Player extends GameObject {

    double minX;
    double maxX;
    double minY;
    double maxY;
    double speed;
    double clearUp;
    double lightOpacity = 0.8;
    float vibrationUp = 1.0f;
    float vibrationDown = 1.0f;
	double amountOfGlow = 0;
	boolean isDead = false;
	boolean cantShoot = true;
	boolean notShooting = false;
	boolean lampOff = false;
	boolean glowing = false;
    boolean movingUp = false;
    boolean movingDown = false;
	boolean up = true;
	boolean down = false;
	boolean shake = false;
	boolean applyRecoil = false;
	boolean left = true;
	boolean right = false;
	boolean vibrate = false;
	boolean collision = false;
	boolean checkVibrate = true;
	boolean hitBounds = false;
	boolean movingLeft = false;
	boolean movingRight = false;
	boolean standingStill = false;
	boolean standingStillY = false;
	boolean notMovingToTheSides = false;
	boolean loopingAudio = false;
	boolean increaseGlow = true;
	boolean decreaseGlow = false;
	boolean introduceSpaceship = true;
	boolean headOnCollision = false;
	int counter = 0;
	int shootingLag;
	int rightBoundary ;
	int recoilDuration = 30;
    int shakeDuration = 30;
    int scroll = 0;
    float fade = 0;
    float velY2 =2;
	float glowLevel =0;
	float amountOfBlur = 2.0f;
	float thrustThreshold = 1;
	float thrustThreshold2 = 1;
    Game game;
    ScreenOverlay overlay;
    LightBeam spotLight;
    GameObjectManager gom;
    GameTileManager tileManager;
    Ellipse lamp;
	Rectangle fadeRect = new Rectangle(0,0,1920,1080);
	DropShadow borderGlow= new DropShadow();
	DropShadow borderGlow2= new DropShadow();
	MotionBlur motionBlur = new MotionBlur();
	ImageView afterBurner2 = new ImageView(GameImageBank.thrust1);
	ImageView afterBurner = new ImageView(GameImageBank.thrust2);
    Light.Point light = new Light.Point();
    Lighting lighting = new Lighting();



    public Player(Game game, Pane layer, Image image, float x, float y, float r, float velX, float velY, float velR, double health, double damage, double speed, GameObjectID id, GameObjectManager gom) {
        super(game, layer, image, x, y, r, velX, velY, velR, health, damage, id);
        this.gom = gom;
        this.speed = speed;
        this.game = game;
        this.x = x-1000;
        this.y = 900;
        this.velX = 5;
        this.velY = -2f;
        this.accelarationX = 0.5f;
        this.accelarationY = 0.5f;
        this.rightBoundary = game.levelLenght;
        this.stopX = 1.0f;
        this.stopY = 1.0f;
		this.clearUp = 0.512;
		this.fadeRect.setHeight(1080);
		this.fadeRect.setWidth(1920);
		this.fadeRect.setOpacity(fade);
		this.fadeRect.setFill(Color.BLACK);
		this.motionBlur.setAngle(-15.0);
		this.motionBlur.setRadius(amountOfBlur);
		this.imageView.setEffect(motionBlur);
		this.lighting.setSurfaceScale(5.0);
		this.lighting.setLight(light);
		this.lighting.setDiffuseConstant(1.2);
		this.lighting.setSpecularConstant(0.2);
		this.overlay = new ScreenOverlay(game,game.getGameRoot());
		this.tileManager = game.getloader().tileManager;
		if(Settings.ADD_LIGHTING)
		this.motionBlur.setInput(lighting);
        init();
		if(Settings.ADD_LIGHTING){
		addSpotLight();
        addLamp();
		}
        addThruster();

    }
    private void init() {

        minX = 0;
        maxX = rightBoundary - image.getWidth()+800;
        minY = Settings.START_Y-height+20 ;
        maxY = Settings.HEIGHT - height;
    }
	public void addLamp(){
		if(Settings.ADD_GLOW){
		lamp = new Ellipse(x,y,6,7);
		lamp.setFill(Color.WHITE);
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setColor(Color.rgb(255, 255, 255,1));
		borderGlow.setWidth(60);
		borderGlow.setHeight(50);
		borderGlow.setSpread(0.5);
		borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		lamp.setEffect(borderGlow);
		lamp.setFill(Color.WHITE);
		lamp.setOpacity(1.0);
		game.getPlayfieldLayer().getChildren().add(lamp);
		}
	}
	public void turnOffLamp(){
		if(lampOff == false){
		lamp.setFill(Color.rgb(255, 255, 255,0.2));
		lamp.setStroke(Color.rgb(255, 255, 255,0.3));
		spotLight.setVisible(false);
		lampOff = true;
		}
		else if(lampOff == true){
		lampOff = false;
		lamp.setFill(Color.WHITE);
		lamp.setStroke(Color.WHITE);
		spotLight.setVisible(true);
		}
	}

	public void addThruster(){
		afterBurner.setOpacity(0.6);
		afterBurner.setBlendMode(BlendMode.ADD);
		afterBurner.setFitWidth(115);
		afterBurner.setPreserveRatio(true);
		afterBurner.toFront();
		afterBurner2.setOpacity(0.8);
		afterBurner2.setBlendMode(BlendMode.ADD);
		afterBurner2.setFitWidth(85);
		afterBurner2.setPreserveRatio(true);
		afterBurner2.toFront();
		game.getBottomLayer().getChildren().add(afterBurner);
		game.getBottomLayer().getChildren().add(afterBurner2);
	}
	public void addSpotLight() {

		spotLight = new LightBeam(game, GameImageBank.spotLight, game.getDebrisLayer(), (float) (x+width+50), y-105,0, GameObjectID.light);

	}
	public void addGlow(){

    	if(increaseGlow){
    		amountOfGlow+=0.05;
    		if(amountOfGlow>=0.8){
    			amountOfGlow = 0.8;
    			increaseGlow = false;
    			decreaseGlow = true;
    	}}
    	else if(decreaseGlow){
    		amountOfGlow-=0.05;
    		if(amountOfGlow<=0.25){
    			amountOfGlow = 0.25;
    			decreaseGlow = false;
    			increaseGlow = true;

    		}
    	}
	}
	public void controlLightOpacity(){
		if(Settings.DIRECT_HIT = true){
		lightOpacity -=0.1;
		if(lightOpacity<=0){
			lightOpacity = 0;
			Settings.DIRECT_HIT = false;
		}
		}
	}
    public double getLightOpacity() {
		return lightOpacity;
	}
	public void setLightOpacity(double lightOpacity) {
		this.lightOpacity = lightOpacity;
	}
	public void move() {
		this.overlay.update();
		if(isDead == false){
    	if(Settings.ADD_LIGHTING){
        	spotLight.updateUI();
        	spotLight.move();
        	lamp.setCenterX(x+width-2);
        	lamp.setCenterY(y+94);

    	    thrustThreshold-=2;
	    	if(thrustThreshold <= -8)
	    		thrustThreshold = 1;

    	}

    	afterBurner2.setTranslateX(x-45+thrustThreshold);
		afterBurner2.setTranslateY(y+52);
		afterBurner.setTranslateX(x-20+thrustThreshold2+thrustThreshold);
		afterBurner.setTranslateY(y+42);
		if(introduceSpaceship) {

		}

		if(introduceSpaceship){
			if(x>10){
				velX -= 0.2;
				velY += 0.01;
//				if(velY >=0){
//					velY = 0;
//				}
				if(velX <=1){
					velX = 1;
				}
				if(x >=(float) (GameImageBank.player.getWidth() / 2.0))
				introduceSpaceship = false;			}
		}
    	super.move();
		if(!introduceSpaceship)
        checkBounds();
		if(Settings.AFTERBURNER){
		    thrustThreshold2-=3;
	    	if(thrustThreshold2 <= -50){
	    		thrustThreshold2 = -50;
	    }
		Settings.GLOBAL_ACCELARATION-=0.05;
		if(Settings.GLOBAL_ACCELARATION <=-25.0f){
			Settings.GLOBAL_ACCELARATION = -25.0f;
		}
	}
		if(!Settings.AFTERBURNER){
		    thrustThreshold2+=2;
	    	if(thrustThreshold2 >= 1){
	    		thrustThreshold2 = 1;
	    }
		Settings.GLOBAL_ACCELARATION+=0.05;
		if(Settings.GLOBAL_ACCELARATION >=0.01f){
			Settings.GLOBAL_ACCELARATION = 0.01f;
		}
	}
		}
    }
    public void ThrustMovementModification(){
    	if(Settings.GLOBAL_ACCELARATION <= 0.01f && vibrate == true ){
    		y = y+velY2*(Settings.FRAMECAP);
    	}
    	if(Settings.GLOBAL_ACCELARATION == 0.01f){

    	}
    }
    public void moveUp(){
    	if(movingUp){
    		if(Settings.GLOBAL_ACCELARATION == 0.01f){
    			velY-=1.15f;
    		}
    		if(Settings.GLOBAL_ACCELARATION != 0.01f){
    			velY2-=1.25f - Settings.GLOBAL_ACCELARATION/40;
    		}
    	}
    }
    public void moveDown(){
    	if(movingDown){
    		if(Settings.GLOBAL_ACCELARATION == 0.01f ){
    			velY+=1.15f;
    		}
    		if(Settings.GLOBAL_ACCELARATION != 0.01f){
    			velY2+=1.25f - Settings.GLOBAL_ACCELARATION/40;
    		}
    	}
    }

	public void fireLaser() {
		if(isDead == false){
		if (cantShoot == false) {
			if (!introduceSpaceship) {
				notShooting = false;
				if (shootingLag >= Settings.SHOOT_DELAY) {
					glowLevel = 3.0f;
					applyRecoil = true;
					//this.overlay.addDistortion(30, 0.5);
					//this.overlay.addToneOverlay(Color.ORANGE, 8, 1.5);
					//this.overlay.addSoftBlur(20, 0.2);
					//this.overlay.addIntenseBlur(20, 0.5);
					//this.overlay.addBloom(8, 1);
					gom.addObject(new MuzzleFlash(game, GameImageBank.muzzle, game.getPlayfieldLayer(), 1,(float) (x + width) - 100, (float) y + 85, null));
					gom.addObject(new Laser(game, GameImageBank.laser, game.getPlayfieldLayer(),(float) (x + width) - 105, (float) (y + height / 2) - 15, 0, 35, 0, GameObjectID.laser));
					game.getEnergyMeter().shotFired(true);
					shootingLag = 0;
					notShooting = true;
				}
			}
		}}
	}
    public void checkBounds() {

        if( Double.compare( y, minY) < 0) {
        	y = (float) minY;
            velY=Math.abs(velY*.9f);
            velY2=Math.abs(velY2);
            sparks(x+width/2,y+20,18,10);
        }
        else if( Double.compare(y, maxY) > 0) {
        	y = (float) maxY;
        	velY=-Math.abs(velY*.9f);
        	velY2=-Math.abs(velY2);
        	sparks(x+width/2,y+height-20,10,18);
        }
        if( Double.compare( x, minX) < 0) {
        	x = (float) minX;
        	velX=Math.abs(velX/2);
        }
    }
    public void updateUI() {
    	if(isDead == false){
        super.updateUI();
        if(!introduceSpaceship){
        controlLightOpacity();
        addStrongerVibration();
        moveUp();
        moveDown();
        stabilize();
        addShake();
        addRecoil();
        ThrustMovementModification();
        stabilizeOnThrust();
        }
    	}
    }
    public void checkRemovability() {
    	if(isDead == false){
    	shootingLag++;
    	addMotionBlur();
    	if(Settings.ADD_LIGHTING)
    	addLighting();
    	}
    	if(isDead){
			fade+=0.005;
			fadeRect.setOpacity(fade);
			if(fade>=1.0f){
				fadeRect.setOpacity(1);
			}
			if(fade>=1.5f){
				game.reset();
			}
		}
    }
    public void addLighting(){
		light.setX(x+300);
		light.setY(y+600);
		light.setZ(500);

    }
    public void addMotionBlur(){
    	if(shake||vibrate){
		if(amountOfBlur<=2){
			amountOfBlur = 2;
		}
		if(amountOfBlur>=8){
			amountOfBlur = 8;
		}
		motionBlur.setRadius(amountOfBlur);
    	}
	    imageView.setEffect(motionBlur);
    }
    public void addRecoil(){
    	if(applyRecoil){
            motionBlur.setRadius(50);
            if(left && !right){
            	velX-=9.5;
            	if(velX < -11.0){
            		left = false;
            		right = true;
            	}
            }
            if(right && !left){
            	velX+=10.5;
        		left = true;
        		right = false;
        		motionBlur.setRadius(2);
        		applyRecoil = false;
            }
    	}
    	if(!applyRecoil)
    		motionBlur.setRadius(2);
    }
    public void addShake(){

    	if(shake){
    		shakeDuration-=4;
    		if(Settings.GLOBAL_ACCELARATION == 0.01f){
        if(down && !up){
        	velY+=5.5;
        	if(velY > 16.0){
        		down = false;
        		up = true;
        	}
        }
        if(up && !down){
        	velY-=5.6;
        	if(velY < -16.0){
        		up = false;
        		down = true;
        	}
        }
    	}
    		if(Settings.GLOBAL_ACCELARATION != 0.01f){
        if(down && !up){
        	velY+=19.0;
        	if(velY > 40.0){
        		down = false;
        		up = true;
        	}
        }
        if(up && !down){
        	velY-=20.0;
        	if(velY < -40.0){
        		up = false;
        		down = true;
        	}
        }
        }
    	if(shakeDuration<0){
    		if(Settings.GLOBAL_ACCELARATION == 0.01f){
    		if(velY>0)
    			velY = 2.0f;
    		if(velY<0)
    			velY = -2.0f;
    		}
    		if(Settings.GLOBAL_ACCELARATION != 0.01f){
    		if(velY>0)
    			velY = 1.0f;
    		if(velY<0)
    			velY = -1.0f;
    		}
    		shakeDuration =0;
    		shake = false;
    	}
    	}
    }
    public void addStrongShake(){

    	if(shake){
    		shakeDuration-=4;
    		if(Settings.GLOBAL_ACCELARATION == 0.01f){
        if(down && !up){
        	velY+=12.9;
        	if(velY > 20.0){
        		down = false;
        		up = true;
        	}
        }
        if(up && !down){
        	velY-=12.6;
        	if(velY < -20.0){
        		up = false;
        		down = true;
        	}
        }
    	}
    		if(Settings.GLOBAL_ACCELARATION != 0.01f){
        if(down && !up){
        	velY+=19.0;
        	if(velY > 40.0){
        		down = false;
        		up = true;
        	}
        }
        if(up && !down){
        	velY-=20.0;
        	if(velY < -40.0){
        		up = false;
        		down = true;
        	}
        }
        }
    	if(shakeDuration<0){
    		if(Settings.GLOBAL_ACCELARATION == 0.01f){
    		if(velY>0)
    			velY = 2.0f;
    		if(velY<0)
    			velY = -2.0f;
    		}
    		if(Settings.GLOBAL_ACCELARATION != 0.01f){
    		if(velY>0)
    			velY = 1.0f;
    		if(velY<0)
    			velY = -1.0f;
    		}
    		shakeDuration =0;
    		shake = false;
    	}
    	}
    }
    public void addVibration(){
    	if(Settings.AFTERBURNER == true){
    		vibrate=true;
    		vibrationDown+=0.005;
    		vibrationUp-=0.0055;
    		amountOfBlur+=0.018;
    		if(vibrationDown > 2.4){
    			vibrationDown = 2.4f;
    		}
    		if(vibrationUp < -2.0){
    			vibrationUp = -2.0f;
    		}
    	}
    	if(Settings.AFTERBURNER == false){
    		vibrationDown-=0.002;
    		vibrationUp+=0.002;
    		amountOfBlur-=0.01;
    		if(vibrationDown < 0.53){
    			vibrationDown = 0.53f;
    		}
    		if(vibrationUp >  0.6 ){
    			vibrationUp = 0.6f;
    			vibrate=false;
    		}
    	}
    	if(vibrate == true && Settings.GLOBAL_ACCELARATION != 0.01f){
        if(down && !up){
        	velY+=1.5;
        	if(velY >= vibrationDown){
        		down = false;
        		up = true;
        	}
        }
        else if(up && !down){
        	velY-=1.5;
        	if(velY <= vibrationUp){
        		up = false;
        		down = true;
        	}
        }
    	}
    }
    public void addStrongerVibration(){
    	if(Settings.AFTERBURNER == true){
    		vibrate=true;
    		vibrationDown+=0.016;
    		vibrationUp  -=0.010;
    		amountOfBlur+=0.018;
    		if(vibrationDown >= 3.0){
    			vibrationDown = 3.0f;
    		}
    		if(vibrationUp <= -2.5){
    			vibrationUp = -2.5f;
    		}
    	}
    	else if(Settings.AFTERBURNER == false){
    		vibrationDown-=0.003;
    		vibrationUp+=0.002;
    		amountOfBlur-=0.01;
    		if(vibrationDown <= 0.6){
    			vibrationDown = 0.6f;
    		}
    		if(vibrationUp >=  0.6 ){
    			vibrationUp = 0.6f;
    			vibrate=false;
    		}
    	}
    	if(vibrate == true && Settings.GLOBAL_ACCELARATION != 0.01f){
        if(down && !up){
        	velY+=2.3;
        	if(velY >= vibrationDown){
        		down = false;
        		up = true;
        	}
        }
        if(up && !down){
        	velY-=2.3;
        	if(velY <= vibrationUp){
        		up = false;
        		down = true;
        	}
        }
    	}
    }
    public void stabilizeOnThrust(){
		if (movingUp == true || movingDown == true) {
            if(velY2 > 15)
            	velY2 -=2.2;
            else if(velY2 < -15)
            	velY2 +=2.2;
        	}
        	if (movingUp == false && movingDown == false) {
            if(velY2 >0)
            	velY2 -=0.25 - Settings.GLOBAL_ACCELARATION/200;
            if(velY2 <0)
            	velY2 +=0.25 - Settings.GLOBAL_ACCELARATION/200;
        	}
    }
    public void stabilize(){
        if(x<10)
      		x = 11;
        if(x>getWidth() / 2.0){
    		x = (float) (getWidth() / 2.0);
    	}
    		if (movingUp == true || movingDown == true) {
            if(velY > 12)
            	velY -=3;
            else if(velY < -12)
            	velY +=3;
        	}
        	if (movingUp == false && movingDown == false) {
            if(velY >0)
            	velY -=0.2;
            if(velY <0)
            	velY +=0.2;
        	}
        	if(x<getWidth() / 2.0){
        		velX+=3.2;
        	}
        	if(x>=getWidth() / 2.0){
        		velX=0;
        	}
        	else if(x>getWidth() / 2.0){
        		velX-=5.5;
        	}
    }
    public void sparks(double x, double y, double low, double high){
    	for(int i = 0; i<15; i++){
    		game.getDebrisManager().addObject(new CollisionSparks(game,GameImageBank.glowingCircle, (float) x, (float) y, new Point2D((Math.random()*(18 - -18 + 1) + -18), Math.random()*(low - -high + 1) + -low)));
    	}
    }
    public void checkCollision() {
    	if(isDead == false){
        for(int i = 0; i<gom.object.size(); i++){
    		GameObject tempObject = gom.object.get(i);
    		if(tempObject.getId() == GameObjectID.asteroid){
    			if(getBounds().intersects(tempObject.getBoundsTop())){
    				if(!shake && !vibrate)
      				velY = -Math.abs(velY);
    				sparks(x+width/2,y+height-20,10,18);
    				collision = true;
    				break;
    			}
    			if(getBoundsTop().intersects(tempObject.getBounds())){
    				if(!shake && !vibrate)
    				velY = Math.abs(velY);
    				sparks(x+width/2,y+20,18,10);
    				collision = true;
    				break;
    			}
//    			if(getBoundsRight().intersects(tempObject.getBoundsLeft())){
//    				headOnCollision = true;
//    			//	die();
//    			}
    		}
        }
        for(int i = 0; i<tileManager.box.size(); i++){
			Tile tempTile = tileManager.box.get(i);
			if(tempTile.getId() == LevelObjectID.CollideBox){
				if(getBounds().intersects(tempTile.getBoundsTop())){
					velY=-Math.abs(velY*.9f);
		        	velY2=-Math.abs(velY2);
		        	sparks(x+width/2,y+height-20,10,18);
				}
				if(getBoundsRight().intersects(tempTile.getBoundsLeft())){
					//die();
				}
				if(getBoundsLeft().intersects(tempTile.getBoundsRight())){
					//die();
				}
			}
		}
        }
	}
    public void draw(GraphicsContext gc){
    	drawBoundingBox(gc);
    }
    public void drawBoundingBox(GraphicsContext gc){

    	if(Settings.DEBUG_MODE){
    		this.imageView.setVisible(false);
     	gc.setStroke(Color.BLACK);
     	//top
     	gc.strokeRect(x+40,y+55,width-60,height*0.24);
     	//left
     	gc.strokeRect(x+20,y+65,20,height*0.34);
     	//right
     	gc.strokeRect(x+width-20,y+65,20,height*0.34);
     	//bottom
     	gc.strokeRect(x+40,y+height/2+15,width-60,height*0.20);
    	}
     }
    public void setScroller() {
        this.imageView.translateXProperty().addListener((v, oldValue, newValue) -> {

            int offSet = newValue.intValue();
            if (offSet > 100 && offSet < game.levelLenght - 1900) {
            	game.getRoot().setTranslateX(-(offSet - 100));
            }
        });

    }
    public Rectangle2D getBoundsTop(){

        return new Rectangle2D(x+40,y+55,width-60,height*0.24);
    }
    public Rectangle2D getBoundsLeft(){

        return new Rectangle2D(x+20,y+65,20,height*0.34);
    }
    public Rectangle2D getBoundsRight(){

        return new Rectangle2D(x+width-20,y+65,20,height*0.34);
    }
    public Rectangle2D getBounds(){

    	return new Rectangle2D(x+40,y+height/2+15,width-60,height*0.20);
    }
    public boolean isMovingUp() {
		return movingUp;
	}
	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}
	public boolean isMovingDown() {
		return movingDown;
	}
	public void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
	}
	public void setShootingBlock(boolean state) {
		this.cantShoot = state;
	}
	public boolean isNotShooting() {

		return notShooting;
	}
	public boolean isCollision() {
		return collision;
	}
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	public void die() {
		isDead = true;
		game.getPlayfieldLayer().getChildren().remove(this.imageView);
//		gom.removeObject(this);
		this.imageView.setVisible(false);
		this.afterBurner.setVisible(false);
		this.afterBurner2.setVisible(false);
		this.lamp.setVisible(false);
		this.spotLight.setVisible(false);
		gom.addObject(new Explosion(game,new Image("explosion2.gif"),game.getPlayfieldLayer(),0.8, (float) (getX()+width/2), (float) (getY()+height/2),  null));
		fadeAndEnd();
	}
	public void fadeAndEnd(){
		game.getFadeScreen().getChildren().add(fadeRect);
	}
	public boolean isDead() {
		return isDead;
	}
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	public boolean headOnCollision() {
		return headOnCollision;
	}
}