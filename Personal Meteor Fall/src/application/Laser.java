package application;


import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Laser extends GameObject{
	Game game;
	Player player;
	GameObjectManager objectManager;
	GameParticleManager particleManager;
	GameTileManager tileManager;
	MotionBlur motionBlur = new MotionBlur();
	float wavingY = 0;
    double imageWidth = 350;
    double imageHeight = 350;
	boolean up = true;
	boolean down = false;
	
	public Laser(Game game, Image image, Pane layer, float x, float y, float r, float velX, float velY, GameObjectID id) {
		super(game,image,layer, x, y, r, velX, velY, id);
		this.game = game;
		this.tileManager = game.getloader().tileManager;
		this.player = game.getloader().getPlayer();
		this.particleManager = game.getParticleManager();
		this.objectManager  = game.getObjectManager();
		this.imageWidth = image.getWidth();
		this.imageHeight = image.getHeight();
	}
	public void move(){
		super.move();
	//	addWooble();
	}
	public void addTrail(){
		particleManager.addObject( new ParticleEffect1(game, Color.rgb(255, 135, 0, 0.5), x+30, y+50, 1.0, new Point2D((Math.random() + 2.0), 0), 10, BlendMode.ADD));
	}
    public void addWiggle(){
        if(down && !up){
        	velY+=0.9;
        	if(velY > 4){
        		up = true;
        		down = false;
        	}
        }
        if(up && !down){
        	velY-=0.9;
        	if(velY < -4){
        		up = false;
        		down = true;
        	}
        }       
    }
    public void addWooble(){
        if(down && !up){
        	velY+=0.5;
        	if(velY > 1){
        		up = true;
        		down = false;
        	}
        }
        if(up && !down){
        	velY-=0.5;
        	if(velY < -1){
        		up = false;
        		down = true;
        	}
        }       
    }
    public boolean isAlive(){
    	return x<Settings.WIDTH+width;
    }
	public void checkRemovability() {
        if( x > Settings.WIDTH+width) {
        	remove();
            setRemovable(true);
        }
	}
    public void draw(GraphicsContext gc){
    	super.draw(gc);
    	drawBoundingBox(gc);
    }
    public void drawBoundingBox(GraphicsContext gc){

    	if(Settings.DEBUG_MODE){
     	gc.setStroke(Color.BLACK);
     	gc.strokeRect (x+100, y+45, imageWidth*.25, imageHeight/5);

    	}
     }
    public void checkCollision() {
		for(int i = 0; i<objectManager.object.size(); i++){
			GameObject tempObject = objectManager.object.get(i);
			if(tempObject.getId() == GameObjectID.asteroid){
				if(getBounds().intersects(tempObject.getBoundsLeft())){		
					game.getScoreBoard().increaseScore();
					if(Settings.ADD_LIGHTING){
						Settings.DIRECT_HIT = true;
						Settings.EXPLOSION_POSITION_X = (float) (x+width);
						Settings.EXPLOSION_POSITION_Y = (float) (y+height);
						player.setLightOpacity(0.8);	
					}
				}
			}	
		}
		for(int i = 0; i<tileManager.barrel.size(); i++){
			Tile tempTile = tileManager.barrel.get(i);
			if(tempTile.getId() == LevelObjectID.ExplosiveBarrel){
				if(getBounds().intersects(tempTile.getBounds())){	
					objectManager.addObject(new Explosion(game,new Image("explosion2.gif"),game.getPlayfieldLayer(),0.8, (float) (tempTile.getX()-tempTile.width/2)-100, (float) (tempTile.getY()-300),  null));
					tempTile.setAlive(false);
					this.setRemovable(true);
				}
			}	
		}
	}
    public Rectangle2D getBounds(){
    	return new Rectangle2D(x+100, y+45, imageWidth*.25, imageHeight/5);
    }
}
