package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;

public abstract class ParticleEffect {
	DropShadow borderGlow= new DropShadow();
	public abstract void updateUI();
	public abstract boolean isAlive();
	public abstract void draw(GraphicsContext gc); 
	public void updateNow(long now){
		
	}

}
