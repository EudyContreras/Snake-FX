package com.EudyContreras.Snake.Controllers;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameStateID;

import javafx.scene.shape.Rectangle;

public class FadeScreenController{
	private GameManager game;
	private Rectangle mainFadeScreen;
	private Rectangle innerFadeScreen;
	private double innerFadePercentage = 0.0;
	private double outerFadePercentage = 0.0;
	private boolean quickFadeOut = false;
	private boolean quickFade = false;
	private boolean mainFade = false;
	private boolean slowFade = false;
	private boolean fadeIn = false;
	private boolean fadeOut = false;

	public FadeScreenController(GameManager game){
		this.game = game;
		this.innerFadeScreen = new Rectangle(0,0, GameSettings.WIDTH, GameSettings.HEIGHT);
		this.mainFadeScreen = new Rectangle(0,0, GameSettings.WIDTH, GameSettings.HEIGHT);
	}
	public void renderFadeScreen() {
		if (!slowFade) {
			innerFadePercentage = 0;
			game.getFadeScreenLayer().getChildren().remove(innerFadeScreen);
			game.getFadeScreenLayer().getChildren().add(innerFadeScreen);
			slowFade = true;
		}
	}
	public void menu_fade_screen() {
		if (!mainFade) {
			game.getMainRoot().getChildren().add(mainFadeScreen);
			mainFade = true;
			slowFade = false;
			fadeIn = false;
		}
	}
	public void restart_fade_screen() {
		if (!fadeIn) {
			game.getFadeScreenLayer().getChildren().remove(innerFadeScreen);
			game.getFadeScreenLayer().getChildren().add(innerFadeScreen);
			fadeIn = true;
			slowFade = false;
		}
	}
	public void quick_restart_fade_screen() {
		if (!fadeIn) {
			game.getFadeScreenLayer().getChildren().remove(innerFadeScreen);
			game.getFadeScreenLayer().getChildren().add(innerFadeScreen);
			innerFadePercentage = 0;
			quickFade = true;
			slowFade = false;
		}
	}
	public void continue_fade_screen() {
		if (!fadeIn) {
			innerFadePercentage = 0;
			game.getFadeScreenLayer().getChildren().add(innerFadeScreen);
			fadeIn = true;
			slowFade = false;
		}
	}
	public void outer_fade_update(){
		if(mainFade){
			outerFadePercentage+=0.03;
			mainFadeScreen.setOpacity(outerFadePercentage);
			if(outerFadePercentage>=1.0){
				if(game.getStateID() == GameStateID.MAIN_MENU){
					game.quitToMain();
					game.getFadeScreenLayer().getChildren().remove(innerFadeScreen);
					game.getMainRoot().getChildren().remove(mainFadeScreen);
					outerFadePercentage = 0;
					innerFadePercentage = 0;
					mainFade = false;
					fadeIn = false;
				}
			}
		}
	}
	public void innerFade_update() {
		innerFadeScreen.setOpacity(innerFadePercentage);
		if (fadeIn) {
			innerFadePercentage += 0.01;
			if (innerFadePercentage >= 1.0f) {
				innerFadePercentage = 1;
				if(game.getStateID() == GameStateID.LEVEL_TRANSITIONING){
					game.goToNext();
					fadeOut = true;
					fadeIn = false;
					game.getCountDownScreen().startCountdown();
				}
				else if(game.getStateID() == GameStateID.MAIN_MENU){
					game.quitToMain();
					game.getMainRoot().getChildren().remove(innerFadeScreen);
					fadeIn = false;
				}
				else if(game.getStateID() == GameStateID.LEVEL_RESTART){
					game.restart();
					fadeOut = true;
					fadeIn = false;
					game.getCountDownScreen().startCountdown();

				}
			}
		}
		if (fadeOut) {
			innerFadePercentage -= 0.01f;
			innerFadeScreen.setOpacity(innerFadePercentage);
			if (innerFadePercentage <= 0) {
				innerFadeScreen.setOpacity(0);
				game.getFadeScreenLayer().getChildren().remove(innerFadeScreen);
				//game.getCountDownScreen().startCountdown();
				fadeOut = false;
			}
		}
		if(slowFade){
			innerFadePercentage += 0.002;
			innerFadeScreen.setOpacity(innerFadePercentage);
			if (innerFadePercentage >= 1.0f) {
				innerFadeScreen.setOpacity(1);
				innerFadePercentage = 1.0f;
			}
		}
		if(quickFade){
			innerFadePercentage += 0.03;
			innerFadeScreen.setOpacity(innerFadePercentage);
			if (innerFadePercentage >= 1.0f) {
				game.restart();
				quickFade = false;
				quickFadeOut = true;
			}
		}
		if(quickFadeOut){
			innerFadePercentage -= 0.05f;
			innerFadeScreen.setOpacity(innerFadePercentage);
			if (innerFadePercentage <= 0) {
				innerFadeScreen.setOpacity(0);
				game.getFadeScreenLayer().getChildren().remove(innerFadeScreen);
				game.getCountDownScreen().startCountdown();
				quickFadeOut = false;
			}
		}
	}
	public void updateFade(){
		innerFade_update();
        outer_fade_update();
	}
}
