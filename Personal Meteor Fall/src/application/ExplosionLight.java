package application;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class ExplosionLight extends GameObject{
	
	double radius;
	double decay;
	float lifeTime = 0.7f;
	
	public ExplosionLight(Game game, Pane layer, Image image, double radius, double expireTime, float x, float y, float velX,float velY, GameObjectID id) {
		super(game, layer, image, x, y, id);
		this.velX = velX;
		this.velY = velY;
		this.radius = radius;
		this.imageView.setFitWidth(radius);
		this.imageView.setFitHeight(radius);
		this.decay = 0.016 /expireTime;
		this.imageView.setOpacity(lifeTime);
	}
	public void move(){
		x += velX+Settings.GLOBAL_ACCELARATION*.8;
		y += velY;
		lifeTime -= decay;

	}
	public void updateUI(){
		super.updateUI();
		this.imageView.setOpacity(lifeTime);
	}
	public void checkRemovability() {
		
		if(lifeTime<=0){
			setRemovable(true);
		}
	}
	public void checkCollision() {

		
	}

	
	
}
