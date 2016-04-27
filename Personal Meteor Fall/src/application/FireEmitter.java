package application;


import java.util.LinkedList;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

public class FireEmitter extends ParticleEmitter{
	Game game;
	GameLoader loader;
	Color flameColor = Color.rgb(0, 50, 255);
	double flameLenght = 6.5;
	double flameLife = 0.12;
	
	public FireEmitter(Game game){
		this.game = game;
		this.loader = game.getloader();
	}
	public LinkedList<ParticleEffect> emit(float x, float y) {
		LinkedList<ParticleEffect> particles = new LinkedList<>();
		
		int numberOfParticles = 5;
		for(int i = 0; i<numberOfParticles; i++){
			if(Settings.AFTERBURNER == true){
				flameLenght+=0.0004;
				flameLife+=0.0004;
				flameColor = Color.rgb(0, 15, 255);
				if(flameLenght >= 7.5)
					flameLenght = 7.5;
				if(flameLife >= 0.18)
					flameLife = 0.18;
			}
			if(Settings.AFTERBURNER == false){
				flameLenght-=0.0004;
				flameLife-=0.0004;
				flameColor = Color.rgb(0, 40, 255);
				if(flameLenght <= 6.5)
					flameLenght = 6.5;
				if(flameLife <= 0.12)
					flameLife = 0.12;
			}
			ParticleEffect1 p1 = new ParticleEffect1(game,flameColor , loader.getPlayer().getX()+20, loader.getPlayer().getY()+75, flameLife, new Point2D((Math.random() - flameLenght), Math.random() * -0.5), 25, BlendMode.ADD);
			particles.add(p1);
		}	
		return particles;
	}

}
