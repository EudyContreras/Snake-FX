package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar {

	boolean depleated = false;
	boolean shotsFired = false;
	boolean setDelay = false;
	boolean killPlayer = false;
	boolean playerIsAlive = true;
	double maxHealth = 100;
	int x = 0;
	int y = 0;
	double width = 0;
	double height = 0;
	int delay = 0;
	Player player;
	Rectangle energyBar = new Rectangle();
	Rectangle energyBarBorder = new Rectangle();

	public HealthBar(Game game, int x, int y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = game.getloader().getPlayer();
		this.energyBar.setWidth(width);
		this.energyBar.setHeight(height);
		this.energyBar.setTranslateX(x);
		this.energyBar.setTranslateY(y);
		this.energyBar.setFill(Color.rgb(0, 255, 80));
		this.energyBarBorder.setWidth(width);
		this.energyBarBorder.setHeight(height);
		this.energyBarBorder.setTranslateX(x);
		this.energyBarBorder.setTranslateY(y);
		this.energyBarBorder.setStroke(Color.GREEN);
		this.energyBarBorder.setFill(Color.TRANSPARENT);
		game.getRadarLayer().getChildren().add(energyBar);
		game.getRadarLayer().getChildren().add(energyBarBorder);
		this.maxHealth = width;
	}

	public void depleteHealth() {
		
		if (player.isCollision() == true) {
			width -= 50;
			setDelay = true;
			player.setCollision(false);
		}
		if (width <= 0 && playerIsAlive) {
			killPlayer = true;
			playerIsAlive = false;
		}
		if(player.headOnCollision()== true){
			width = 0;
		}
		this.energyBar.setWidth(width);
	}	
	public void setDelay(){
		
		if(setDelay == true){
			delay = 100;
			setDelay = false;
		}
	}
	public void regerateHealth() {
		if(player.isDead() == false){
		setDelay();
		if(delay>=0){
			delay--;
		}
		
		if (player.isCollision() == false) {
			if (width < maxHealth) {
				if(delay<=0)
				width+=0.5;
			}
		}
		}
		if(killPlayer == true){
			player.die();
			killPlayer = false; 
		}
	}
}
