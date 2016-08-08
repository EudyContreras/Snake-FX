package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * This class aims to represent a count down shown in game which determines
 * whether or not the players can start. If the count says go the players are
 * allow to perform actions.
 *
 * @author Eudy Contreras
 *
 */
public class CountDownScreen {
	private int startUpTime;
	private int index;
	private double x;
	private double y;
	private double width;
	private double height;
	private double baseWidth;
	private double baseHeight;
	private double expireTime;
	private double lifeTime;
	private Boolean allowCount = false;
	private Boolean allowCountdown = false;
	private Pane layer;
	private GameManager game;
	public static Boolean COUNTDOWN_OVER = false;

	/**
	 * Constructor which takes the main game class along with the width and the
	 * height of each of each element to be show. The parameter also takes a
	 * layer to which the elements will be added and displayed upon.
	 *
	 * @param game:Main game class which communicates with almost all the otherclasses
	 * @param width:Horizontal dimension of the UI element
	 * @param height:Vertical dimension of the UI element
	 * @param layer:Layer to which the UI element will be added
	 */
	public CountDownScreen(GameManager game, double width, double height, Pane layer) {
		this.game = game;
		this.width = width;
		this.height = height;
		this.baseWidth = this.width;
		this.baseHeight = this.height;
		this.layer = layer;
	}

	/**
	 * Method which starts the count down. This method can be called at the
	 * beginning of a level in order for allow for a count down before any of
	 * the players can perform any given action
	 */
	public void startCountdown(int startUpTime) {
		this.game.setStateID(GameStateID.COUNT_DOWN);
		this.index = 3;
		this.startUpTime = startUpTime;
		this.lifeTime = 1.0;
		this.expireTime = 0.022 / 1;
		this.x = (GameSettings.WIDTH / 2 - baseWidth / 2);
		this.y = (GameSettings.HEIGHT / 2 - baseHeight / 2)-80;
		this.width = baseWidth;
		this.height = baseHeight;
		this.game.setStateID(GameStateID.COUNT_DOWN);
		this.allowCountdown = false;
		this.allowCount = true;
		COUNTDOWN_OVER = false;
	}

	/**
	 * Resumes the countdown update
	 */
	public void resumeCountdow() {
		if (allowCountdown)
			return;
		allowCountdown = true;

	}

	/**
	 * Stops the countdown update
	 */
	public void stopCountdown() {
		if (!allowCountdown)
			return;
		allowCountdown = false;
	}

	/**
	 * Method which updates all the functionality of this class: the movement,
	 * the fading and the panning are updated through this method.
	 */
	public void update() {
		if (allowCount) {
			startUpTime--;
			if (allowCountdown) {

				lifeTime -= expireTime;

				if (lifeTime < 0) {
					countDown();
				}
			} else {
				if (startUpTime <= 0) {
					startUpTime = 0;
					allowCountdown = true;
				}
			}
		}
	}

	/**
	 * Method which visualizes the count down into images according to the
	 * current index of the count down.
	 */
	private void countDown() {
		if (allowCount) {
			if (index == -1) {
				go();
			}
			if (index == 0) {
				reset(1.0, true);
				new CountDown(layer,GameImageBank.count_go,x,y,width,height,1000,1000,null);
			}
			else if (index == 1) {
				reset(1.0, false);
				new CountDown(layer,GameImageBank.count_one,x,y,width,height,1200,1500,1500,null);
			}
			else if (index == 2) {
				reset(1.0, false);
				new CountDown(layer,GameImageBank.count_two,x,y,width,height,1200,1500,1500,null);
			}
			else if (index == 3) {
				reset(1.0, false);
				new CountDown(layer,GameImageBank.count_three,x,y,width,height,1200,1500,1500,null);
			}
		}
	}

	private class CountDown extends Rectangle {
		public CountDown(Pane layer,ImagePattern image, double x, double y, double width, double height, double fadeDuration, double scaleDuration, Runnable script) {
			super(x, y, width, height);
			FadeTransition fade = new FadeTransition(Duration.millis(fadeDuration), this);
			ScaleTransition scale = new ScaleTransition(Duration.millis(scaleDuration), this);
			setFill(image);
			fade.setFromValue(1);
			fade.setToValue(0);
			scale.setFromY(1);
			scale.setFromX(1);
			scale.setToX(0);
			scale.setToY(0);
			fade.play();
			scale.play();
			layer.getChildren().add(this);
			fade.setOnFinished(e -> {
				if (script != null) {
					script.run();
				}
				layer.getChildren().remove(this);
			});
		}
		public CountDown(Pane layer, ImagePattern image, double x, double y, double width, double height, double fadeDuration, double scaleDuration, double translateDuation,Runnable script) {
			super(x, y, width, height);
			FadeTransition fade = new FadeTransition(Duration.millis(fadeDuration), this);
			ScaleTransition scale = new ScaleTransition(Duration.millis(scaleDuration), this);
			TranslateTransition translate = new TranslateTransition(Duration.millis(translateDuation), this);
			setFill(image);
			fade.setFromValue(1);
			fade.setToValue(0);
			scale.setFromY(1);
			scale.setFromX(1);
			scale.setToX(0);
			scale.setToY(0);
			translate.setByX(-200);
			translate.setByY(-200);
			fade.play();
//			scale.play();
//			translate.play();
			layer.getChildren().add(this);
			fade.setOnFinished(e -> {
				if (script != null) {
					script.run();
				}
				layer.getChildren().remove(this);
			});
		}
	}
	/**
	 * Method which resets the dimensions, opacity and position of the UI
	 * element in order to allow for another number or index to be shown. This
	 * reset method must be called at the end of every lifetime.
	 *
	 * @param life: lifetime assigned to the current index
	 */
	private void reset(double life, Boolean lastCount) {
		index -= 1;
		width = baseWidth;
		height = baseHeight;
		lifeTime = 1;
		x = (double) (GameSettings.WIDTH / 2 - baseWidth / 2);
		y = (double) (GameSettings.HEIGHT / 2 - baseHeight / 2)-80;
		if (lastCount) {
			game.getScoreKeeper().swipeUp();
			game.getGameHud().hideHUDCover();
			game.setStateID(GameStateID.GAMEPLAY);
			game.getScoreKeeper().startTimer();
			height = baseHeight - 60;
			width = baseWidth + 180;
			x = (double) (GameSettings.WIDTH / 2 - width / 2);
			y = (double) (GameSettings.HEIGHT / 2 - height / 2)-20;
			COUNTDOWN_OVER = true;
		}
	}

	/**
	 * Method which when called allows the players to perform their actions.
	 * This method gets called at the end of the count down
	 */
	private void go() {
		allowCount = false;
		allowCountdown = false;
	}

}
