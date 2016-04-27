package application;


import javafx.scene.effect.DropShadow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class LightBeam extends GameObject{
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
	
	public LightBeam(Game game, Image image, Pane layer, float x, float y, float r, GameObjectID id) {
		super(game,layer,image, x, y, id);
		this.r = r;
		this.game = game;
		this.particleManager = game.getParticleManager();
		this.objectManager  = game.getObjectManager();
		this.imageView.resize(870, 500);
		this.imageView.minWidth(800);
		this.imageView.prefWidth(800);
		this.imageView.setOpacity(0.15);
	}
	public void move(){
		super.move();	
		x = (float) (game.getloader().getPlayer().getX()+game.getloader().getPlayer().getWidth()+10);
		y = (float) (game.getloader().getPlayer().getY()-55);

	}
	public void setVisible(boolean visible){
		this.imageView.setVisible(visible);
	}
    public boolean isAlive(){
    	return true;
    }
	public void checkRemovability() {

	}
	public void checkCollision() {
		
	}

}
