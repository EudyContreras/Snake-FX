package application;


import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class MuzzleFlash extends GameObject{
	Game game;
	int depth = 75;
	double decay;	
	float lifeTime = 2.0f;
	float wavingY = 0;
	boolean up = true;
	boolean down = false;
	
	public MuzzleFlash(Game game, Image image, Pane layer, double expireTime, float x, float y, GameObjectID id) {
		super(game,layer,image, x, y, id);
		this.decay = 0.512/expireTime; 
		this.game = game;
		this.imageView.setFitHeight(100);
		this.imageView.setFitWidth(100);
		this.imageView.setPreserveRatio(true);
	}
	public void move(){
		super.move();	
		lifeTime-=decay;
		x = (float) (game.getloader().getPlayer().getX()+game.getloader().getPlayer().getWidth()-100);
		y = (float) (game.getloader().getPlayer().getY()+82);
	}
    public boolean isAlive(){
    	return lifeTime>0;
    }
	public void checkRemovability() {
		if(lifeTime<=0){
			remove();
		}
	}
	public void checkCollision() {
		
	}

}
