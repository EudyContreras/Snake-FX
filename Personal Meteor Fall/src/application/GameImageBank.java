package application;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameImageBank {
	
	 public static Image laser;
	 public static Image muzzle;
	 public static Image glowingCircle;
	 public static Image glowingCircle1;
	 public static Image glowingCircle2;
	 public static Image explosionLight;
	 public static Image preLightedDebris;
	 public static Image debris;
	 public static Image asteroid;
	 public static Image spotLight;
	 public static Image player;
	 public static Image light;
	 public static Image fireBall;
	 public static Image radar;
	 public static Image radarGlass;
	 public static Image thrust1;
	 public static Image thrust2;
	 
	 public GameImageBank(){
		  laser = ImageUtility.preCreateGlowingImages("laser3.png", Color.rgb(255, 185, 0,1.0), 150, 0.6, 0, 0);
		  muzzle = ImageUtility.preCreateGlowingImages("muzzle.png", Color.rgb(255, 185, 0,1.0), 125, 0.5, 0, 0);
		  glowingCircle = ImageUtility.preCreateGlowingCircle(Color.ORANGE,1, 500, 0.8, 0, 0);
		  glowingCircle1 = ImageUtility.preCreateGlowingCircle2(Color.ORANGE,1, 500, 0.8, 0, 0);
		  glowingCircle2 = ImageUtility.preCreateGlowingCircle(Color.rgb(0, 145, 255,1),1, 325, 0.7, 0, 0);
		  explosionLight = ImageUtility.preCreateExplosionLight(Color.rgb(255, 195, 0,1),1, 800, 0.9);
		  preLightedDebris = ImageUtility.preCreateShadedImages("Debris2.png",0.7, 0.2, 0, 0);
		  debris = ImageUtility.preCreateAsteroidDebris("Debris2.png",1.6, 0.5, 0, 0);
		  thrust1 =  new Image("blueThruster5.png");
		  thrust2 =  new Image("blueThruster2.png");
		  asteroid = new Image("asteroid2.png");
		  spotLight = new Image("lightBeam1.png");
		  player = new Image("player.png");
		  light = new Image("lightBeam.png");
		  fireBall = new Image("Fireball.png");
		  radar = new Image("radarSilver.png");
		  radarGlass = new Image("radarGlass1.png");
	 }
}
