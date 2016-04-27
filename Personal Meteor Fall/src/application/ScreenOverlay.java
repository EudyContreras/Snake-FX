package application;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ScreenOverlay extends GameObject{

    MotionBlur motionEffect = new MotionBlur(0,50);
    BoxBlur blurEffect = new BoxBlur(25,25,2);
    GaussianBlur deathEffect = new GaussianBlur(0);
    GaussianBlur gaussianEffect = new GaussianBlur(7);
    Rectangle toneOverlay = new Rectangle(0,0,Settings.WIDTH, Settings.HEIGHT);
    Bloom bloomEffect = new Bloom();
    Glow glowEffect = new Glow();
    DropShadow shadow = new DropShadow();
    Boolean setDistortion = false;
    Boolean setBloom = false;
    Boolean setSoftBlur = false;
    Boolean setIntenseBlur = false;
    Boolean setToneOverlay = false;
    Boolean deathBlur = false;
    Double softBlurLifetime = 0.0;
    Double distortionLifetime = 0.0;
    Double bloomLifetime = 0.0;
    Double toneLifetime = 0.0;
    Double intenseBlurLifetime = 0.0;
    Double deathBlurLifetime = 0.0;
    Double speedDistortion;
    Double speedTone;
    Double speedBlur;
    Double speedGaussian;
    Double speedDeath;
    Double speedBloom;
    Pane layer;
    Game game;


	public ScreenOverlay(Game game, Pane layer) {
		super(game,layer,null);
		this.game = game;
		this.layer = layer;
		this.toneOverlay.setFill(Color.TRANSPARENT);
	}
	/**
	 * Adds a distortion effect to the layer.
	 * max lifetime = 63.
	 * min lifetime = 0.
	 * @param lifetime
	 */
	public void addDistortion(double lifetime, double speed){
		this.layer.setEffect(motionEffect);
		this.setDistortion = true;
		this.distortionLifetime = lifetime;
		this.speedDistortion = speed;
	}
	/**
	 * Adds a soft blur effect to the layer.
	 * max lifetime = 63.
	 * min lifetime = 0.
	 * @param lifetime
	 */
	public void addSoftBlur(double lifetime, double speed){
		this.softBlurLifetime = lifetime;
		this.speedGaussian = speed;
		this.layer.setEffect(gaussianEffect);
		this.setSoftBlur = true;
	}
	/**
	 * Adds a intense blur effect to the layer.
	 * max lifetime = 255.
	 * min lifetime = 0.
	 * @param lifetime
	 */
	public void addIntenseBlur(double lifetime, double speed){
		this.intenseBlurLifetime = lifetime;
		this.speedBlur = speed;
		this.blurEffect.setIterations(2);
		this.layer.setEffect(blurEffect);
		this.setIntenseBlur = true;
	}
	/**
	 * Adds a bloom effect to the scene
	 * max lifetime = 10;
	 * min lifeTime = 0;
	 * @param lifetime
	 */
	public void addBloom(double lifetime, double speed){
		this.bloomLifetime = lifetime/10;
		this.speedBloom = speed/10;
		this.layer.setEffect(bloomEffect);
		this.setBloom = true;
	}
	/**
	 * Adds a tone overlay to the screen which will disappear after at a given rate
	 * max lifetime = 10;
	 * min lifetime = 0;
	 * @param tone
	 * @param lifetime
	 * @param speed
	 */
	public void addToneOverlay(Color tone, double lifetime, double speed){
		this.layer.getChildren().remove(this.toneOverlay);
		this.toneLifetime = lifetime/10;
		this.speedTone = speed/10;
		this.toneOverlay.setFill(tone);
		this.toneOverlay.setOpacity(toneLifetime/10);
		this.layer.getChildren().add(toneOverlay);
		this.setToneOverlay = true;
	}
	public void addDeathBlur(){
		this.layer.setEffect(deathEffect);
		this.deathBlur = true;
	}
	public void update(){
		if(setDistortion){
			setDistortionModifier();
		}
		if(setSoftBlur){
			setSoftBlurModifier();
		}
		if(setIntenseBlur){
			setIntenseBlurModifier();
		}
		if(setBloom){
			setBloomModifier();
		}
		if(setToneOverlay){
			setToneModifier();
		}
		if(deathBlur){
			setDeathBlur();
		}
	}
	public void updateUI(){

	}
	public void setDistortionModifier(){
		if(distortionLifetime>=0){
			distortionLifetime-=speedDistortion;
			this.motionEffect.setRadius(distortionLifetime);
		}
	}
	public void setSoftBlurModifier(){
		if(softBlurLifetime>=0){
			softBlurLifetime-=speedGaussian;
			this.gaussianEffect.setRadius(softBlurLifetime);
		}
	}
	public void setIntenseBlurModifier(){
		if(intenseBlurLifetime>=0){
			intenseBlurLifetime-=speedBlur;
			this.blurEffect.setWidth(intenseBlurLifetime);
			this.blurEffect.setHeight(intenseBlurLifetime);
		}
	}
	public void setBloomModifier(){
		bloomLifetime-=speedBloom;
		bloomEffect.setThreshold(bloomLifetime);
		if(bloomLifetime<=0){
			this.layer.setEffect(null);
			bloomLifetime = 0.0;
		}
	}
	public void setToneModifier(){
		toneLifetime-=speedTone;
		this.toneOverlay.setOpacity(toneLifetime);
		if(toneLifetime<=0){
			this.layer.getChildren().remove(toneOverlay);
			toneLifetime = 0.0;
		}
	}
	public void setDeathBlur(){
		deathBlurLifetime+=0.1;
		this.deathEffect.setRadius(deathBlurLifetime);
		if(deathBlurLifetime>=63){
			deathBlur = false;
			this.deathEffect.setRadius(0);
			deathBlurLifetime = 0.0;
		}
	}
	public void checkRemovability() {}
	public void checkCollision() {}

}