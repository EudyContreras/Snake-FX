package application;


import javafx.scene.effect.DropShadow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Radar extends GameObject{
	Game game;
	GameObjectManager objectManager;
	GameParticleManager particleManager;
	MotionBlur motionBlur = new MotionBlur();
	DropShadow borderGlow= new DropShadow();
	int depth = 75;
	float wavingY = 0;
	boolean up = true;
	boolean down = false;
	Circle circle;
	
	public Radar(Game game, Image image, Pane layer, double opacity, float x, float y, float r, GameObjectID id) {
		super(game,layer,image, x, y, id);
		this.velR = r;
		this.game = game;
		this.particleManager = game.getParticleManager();
		this.objectManager  = game.getObjectManager();
		this.imageView.setOpacity(opacity);
		this.circle = new Circle(200,200,10,Color.WHITE);
	}
	public void init(){

	}
	public void move(){
		super.move();	
		x = (float) (Settings.WIDTH-width);
		y = (float) (Settings.HEIGHT-height);
	}
    public boolean isAlive(){
    	return true;
    }
	public void checkRemovability() {

	}
	public void checkCollision() {
		
	}

}
