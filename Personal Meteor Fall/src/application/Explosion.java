package application;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Explosion extends GameObject{
	Game game;
	int depth = 75;
	double decay = 0;	
	float lifeTime = 1.0f;
	float wavingY = 0;
	boolean up = true;
	boolean down = false;
	
	public Explosion(Game game, Image image, Pane layer, double expireTime, float x, float y, GameObjectID id) {
		super(game,layer,image, x, y, id);
		this.decay = 0.016/expireTime; 
		this.velX = -15;
		this.game = game;
		this.imageView.setFitHeight(400);
		this.imageView.setFitWidth(400);
		this.imageView.setPreserveRatio(true);
	}
	public void move(){
		super.move();	
		this.lifeTime-=this.decay;
	}
    public boolean isAlive(){
    	return this.lifeTime>0;
    }
	public void checkRemovability() {
		if(lifeTime<=0){
			this.getLayer().getChildren().remove(this.imageView);
			remove();
		}
	}
	public void checkCollision() {
		
	}
}
