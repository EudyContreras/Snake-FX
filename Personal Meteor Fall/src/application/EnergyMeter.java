package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EnergyMeter {

	boolean depleated = false;
	boolean shotsFired = false;
	boolean setDelay = false;
	int energyLevel = 100;
	int x = 0;
	int y = 0;
	int width = 0;
	int height = 0;
	int delay = 0;
	Player player;
	Rectangle energyBar = new Rectangle();
	Rectangle energyBarBorder = new Rectangle();

	public EnergyMeter(Game game, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = game.getloader().getPlayer();
		this.energyBar.setWidth(width);
		this.energyBar.setHeight(height);
		this.energyBar.setTranslateX(x);
		this.energyBar.setTranslateY(y);
		this.energyBar.setFill(Color.ORANGE);
		this.energyBarBorder.setWidth(width);
		this.energyBarBorder.setHeight(height);
		this.energyBarBorder.setTranslateX(x);
		this.energyBarBorder.setTranslateY(y);
		this.energyBarBorder.setStroke(Color.ORANGE);
		this.energyBarBorder.setFill(Color.TRANSPARENT);
		game.getRadarLayer().getChildren().add(energyBar);
		game.getRadarLayer().getChildren().add(energyBarBorder);
		this.energyLevel = width;
	}

	public void deplete() {
		if (shotsFired == true) {
			width -= 70;
			shotsFired = false;
		}

		if (width >= 15) {
			player.setShootingBlock(false);
		} 
		else if (width <= 0) {
			player.setShootingBlock(true);
			setDelay = true;
		}
		this.energyBar.setWidth(width);
	}
	public void setDelay(){
		if(setDelay == true){
			delay = 90;
			width = 1;
			setDelay = false;
		}
	}
	public void shotFired(boolean state) {
		this.shotsFired = state;
	}

	public void regerate() {
		setDelay();
		if(delay>=0){
			delay--;
		}
		if (player.isNotShooting() == true) {
			if (width < energyLevel) {
				if(delay<=0)
				width+=2;
			}
		}
	}


}
