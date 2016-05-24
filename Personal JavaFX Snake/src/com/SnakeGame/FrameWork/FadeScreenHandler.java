package com.SnakeGame.FrameWork;

import com.SnakeGame.IDenums.GameStateID;

import javafx.scene.shape.Rectangle;

public class FadeScreenHandler{
	private SnakeGame game;
	private Rectangle mainFadeScreen;
	private Rectangle innerFadeScreen;
	private double innerFadePercentage = 0.0;
	private double outerFadePercentage = 0.0;
	private boolean mainFade = false;
	private boolean slowFade = false;
	private boolean fadeIn = false;
	private boolean fadeOut = false;

	public FadeScreenHandler(SnakeGame game){
		this.game = game;
		this.innerFadeScreen = new Rectangle(0,0, Settings.WIDTH, Settings.HEIGHT);
		this.mainFadeScreen = new Rectangle(0,0, Settings.WIDTH, Settings.HEIGHT);
	}
	public void renderFadeScreen() {
		if (!slowFade) {
			innerFadePercentage = 0;
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
			fadeIn = true;
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
					game.reset();
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
					game.setStateID(GameStateID.GAMEPLAY);
				}
				else if(game.getStateID() == GameStateID.MAIN_MENU){
					game.reset();
					game.getMainRoot().getChildren().remove(innerFadeScreen);
					fadeIn = false;
				}
				else if(game.getStateID() == GameStateID.LEVEL_RESTART){
					game.restart();
					fadeOut = true;
					fadeIn = false;
					game.setStateID(GameStateID.GAMEPLAY);

				}
			}
		}
		if (fadeOut) {
			innerFadePercentage -= 0.01f;
			innerFadeScreen.setOpacity(innerFadePercentage);
			if (innerFadePercentage <= 0) {
				innerFadeScreen.setOpacity(0);
				game.getFadeScreenLayer().getChildren().remove(innerFadeScreen);
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
	}
}
