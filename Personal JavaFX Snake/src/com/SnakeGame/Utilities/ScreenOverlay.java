package com.SnakeGame.Utilities;

import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.IDenums.GameStateID;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ScreenOverlay {

	private MotionBlur motionEffect = new MotionBlur(0, 50);
	private BoxBlur blurEffect = new BoxBlur(25, 25, 2);
	private GaussianBlur deathEffect = new GaussianBlur(0);
	private GaussianBlur gaussianEffect = new GaussianBlur(7);
	private GaussianBlur clearLevelBlur = new GaussianBlur(0);
	private GaussianBlur stormBlur = new GaussianBlur(0);
	private Rectangle toneOverlay = new Rectangle(0, 0, Settings.WIDTH, Settings.HEIGHT);
	private Rectangle fadeScreen = new Rectangle(0, 0, Settings.WIDTH, Settings.HEIGHT);
	private Bloom bloomEffect = new Bloom();
	private Boolean instanceCheck = false;
	private Boolean setDistortion = false;
	private Boolean setBloom = false;
	private Boolean setSoftBlur = false;
	private Boolean setIntenseBlur = false;
	private Boolean setToneOverlay = false;
	private Boolean deathBlur = false;
	private Boolean blurLevel = false;
	private Boolean storm = false;
	private Boolean blurUp = true;
	private Boolean blurDown = false;
	private Boolean clearLevel = false;
	private Boolean setFadeOverlay = false;
	private Double clearLevelBluring = 0.0;
	private Double stormBluring = 0.0;
	private Double softBlurLifetime = 0.0;
	private Double distortionLifetime = 0.0;
	private Double bloomLifetime = 0.0;
	private Double toneLifetime = 0.0;
	private Double intenseBlurLifetime = 0.0;
	private Double deathBlurLifetime = 0.0;
	private Double speedDistortion;
	private Double speedTone;
	private Double speedBlur;
	private Double speedGaussian;
	private Double speedBloom;
	private Double fade;
	private Double fadeSpeed;
	private Pane layer;
	private SnakeGame game;
	private GameStateID stateID;

	public ScreenOverlay(SnakeGame game, Pane layer) {
		this.game = game;
		this.layer = layer;
		this.toneOverlay.setFill(Color.TRANSPARENT);
	}

	/**
	 * Adds a distortion effect to the layer. max lifetime = 63. min lifetime =
	 * 0.
	 *
	 * @param lifetime
	 */
	public void addDistortion(double lifetime, double speed) {
		if (!PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED) {
			this.layer.setEffect(motionEffect);
			this.motionEffect.setAngle(10);
			this.setDistortion = true;
			this.distortionLifetime = lifetime;
			this.speedDistortion = speed;
		}
	}

	/**
	 * Adds a soft blur effect to the layer. max lifetime = 63. min lifetime =
	 * 0.
	 *
	 * @param lifetime
	 */
	public void addSoftBlur(double lifetime, double speed) {
		if (!PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED) {
			this.layer.setEffect(null);
			this.softBlurLifetime = lifetime;
			this.speedGaussian = speed;
			this.layer.setEffect(gaussianEffect);
			this.setSoftBlur = true;
		}
	}

	/**
	 * Adds a intense blur effect to the layer. max lifetime = 255. min lifetime
	 * = 0.
	 *
	 * @param lifetime
	 */
	public void addIntenseBlur(double lifetime, double speed) {
		if (!PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED) {
			this.intenseBlurLifetime = lifetime;
			this.speedBlur = speed;
			this.blurEffect.setIterations(2);
			this.layer.setEffect(blurEffect);
			this.setIntenseBlur = true;
		}
	}

	/**
	 * Adds a bloom effect to the scene max lifetime = 10; min lifeTime = 0;
	 *
	 * @param lifetime
	 */
	public void addBloom(double lifetime, double speed) {
		if (!PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED) {
			this.bloomLifetime = lifetime / 10;
			this.speedBloom = speed / 10;
			this.layer.setEffect(bloomEffect);
			this.setBloom = true;
		}
	}

	/**
	 * Adds a tone overlay to the screen which will disappear after at a given
	 * rate max lifetime = 10; min lifetime = 0;
	 *
	 * @param tone
	 * @param lifetime
	 * @param speed
	 */
	public void addToneOverlay(Color tone, double lifetime, double speed) {
		if (!PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED) {
			this.layer.getChildren().remove(toneOverlay);
			this.toneLifetime = lifetime / 10;
			this.speedTone = speed / 10;
			this.toneOverlay.setFill(tone);
			this.toneOverlay.setOpacity(toneLifetime / 10);
			this.layer.getChildren().add(toneOverlay);
			this.setToneOverlay = true;
		}
	}

	/**
	 * Adds a blur to the screen after the snake dies
	 */
	public void addDeathBlur() {
		if (!PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED) {
			this.deathBlurLifetime = 0.0;
			this.layer.setEffect(null);
			this.layer.setEffect(deathEffect);
			this.deathBlur = true;
		}
	}

	/**
	 * Adds a blur to the screen after the level has been completed
	 */
	public void levelCompleteBlur() {
		this.clearLevelBluring = 0.0;
		this.layer.setEffect(null);
		this.layer.setEffect(clearLevelBlur);
		this.clearLevel = false;
		this.blurLevel = true;
	}

	public void levelCompleteBlurOff() {
		// this.clearLevelBluring = 40.0;
		this.layer.setEffect(clearLevelBlur);
		this.blurLevel = false;
		this.clearLevel = true;
	}

	/**
	 * Adds random blurring during sand storms
	 */
	public void addStormBlur() {
		if (!PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED) {
			this.layer.setEffect(null);
			this.layer.setEffect(stormBlur);
			this.storm = true;
		}
	}

	/**
	 * Adds a fading screen to the game which leads to the main menu. The fade
	 * speed determines the speed of the fade.
	 *
	 * @param fadeSpeed:
	 *            max 10, min 1;
	 */
	public void addFadeScreen(double fadeSpeed, GameStateID stateID) {
		this.stateID = stateID;
		this.game.getFadeScreenLayer().getChildren().remove(fadeScreen);
		this.fade = 0.0;
		this.fadeScreen.setOpacity(fade);
		this.fadeScreen.setFill(Color.BLACK);
		this.fadeSpeed = fadeSpeed / 1000;
		this.game.getFadeScreenLayer().getChildren().add(fadeScreen);
		this.setFadeOverlay = true;
	}

	public void updateEffect() {
		if (setDistortion) {
			setDistortionModifier();
		}
		if (setSoftBlur) {
			setSoftBlurModifier();
		}
		if (setIntenseBlur) {
			setIntenseBlurModifier();
		}
		if (setBloom) {
			setBloomModifier();
		}
		if (setToneOverlay) {
			setToneModifier();
		}
		if (deathBlur) {
			setDeathBlur();
		}
		if (storm) {
			setStormBlur();
		}
		if (blurLevel || clearLevel) {
			setClearLevelBlur();
		}
		if (setFadeOverlay) {
			setFadeModifier();
		}
	}

	private void setDistortionModifier() {
		if (distortionLifetime >= 0) {
			distortionLifetime -= speedDistortion;
			this.motionEffect.setRadius(distortionLifetime);
		}
	}

	private void setSoftBlurModifier() {
		if (softBlurLifetime >= 0) {
			softBlurLifetime -= speedGaussian;
			this.gaussianEffect.setRadius(softBlurLifetime);
		}
	}

	private void setIntenseBlurModifier() {
		if (intenseBlurLifetime >= 0) {
			intenseBlurLifetime -= speedBlur;
			this.blurEffect.setWidth(intenseBlurLifetime);
			this.blurEffect.setHeight(intenseBlurLifetime);
		}
	}

	private void setBloomModifier() {
		bloomLifetime -= speedBloom;
		bloomEffect.setThreshold(bloomLifetime);
		if (bloomLifetime <= 0) {
			this.layer.setEffect(null);
			bloomLifetime = 0.0;
		}
	}

	private void setToneModifier() {
		toneLifetime -= speedTone;
		this.toneOverlay.setOpacity(toneLifetime);
		if (toneLifetime <= 0) {
			toneLifetime = 0.0;
			this.layer.getChildren().remove(toneOverlay);
		}
	}

	private void setFadeModifier() {
		fade += fadeSpeed;
		fadeScreen.setOpacity(fade);
		if (fade >= 1.0f) {
			fadeScreen.setOpacity(1);
		}
		if (fade >= 1.1f) {
			if (stateID == GameStateID.GAME_OVER) {
				setFadeOverlay = false;
			}
			if (stateID == GameStateID.GAME_MENU) {

			}
			if (stateID == GameStateID.LEVEL_COMPLETED) {

			}
		}
	}

	private void setStormBlur() {
		if (!PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED) {
			if (blurUp) {
				stormBluring += 0.1;
				if (stormBluring >= 3) {
					blurDown = true;
					blurUp = false;
				}

			}
			if (blurDown) {
				stormBluring -= 0.1;
				if (stormBluring <= 0) {
					blurUp = true;
					blurDown = false;
					storm = false;
					layer.setEffect(null);
				}
			}
			layer.setEffect(this.stormBlur);
			stormBlur.setRadius(stormBluring);
		}
	}

	private void setDeathBlur() {
		deathBlurLifetime += 0.05;
		this.layer.setEffect(deathEffect);
		this.deathEffect.setRadius(deathBlurLifetime);
		if (deathBlurLifetime >= 63) {
			deathBlurLifetime = 63.0;
		}
		if (deathBlurLifetime >= 10) {
			if (!instanceCheck) {
				game.getGameOverScreen().fadeOut();
				instanceCheck = true;
			}
		}
	}

	private void setClearLevelBlur() {
		if (blurLevel) {
			clearLevelBluring += 0.2;
			if (clearLevelBluring >= 25) {
				clearLevelBluring = 25.0;
			}
		}
		if (clearLevel) {
			clearLevelBluring -= 1.0;
			if (clearLevelBluring <= 0) {
				clearLevelBluring = 0.0;
				clearLevel = false;
			}
		}
		this.layer.setEffect(clearLevelBlur);
		this.clearLevelBlur.setRadius(clearLevelBluring);
	}

	public void removeBlur() {
		this.clearLevelBluring = 0.0;
		setDistortion = false;
		setBloom = false;
		setSoftBlur = false;
		setIntenseBlur = false;
		deathBlur = false;
		blurLevel = false;
		storm = false;
		blurUp = true;
		blurDown = false;
		layer.setEffect(null);
	}

}