package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

/**
 * Bounds class which can be used for adding specific events to objects
 * that surround the level
 *
 * @author Eudy Contreras
 *
 */
public class GameBorder extends AbstractTile {
	private GameManager game;
	private double borderSize = 20;
	private ImageView leftBound;
	private ImageView rightBound;
	private ImageView topBound;
	private ImageView bottomBound;
	private Group layer;

	public GameBorder(GameManager game, Group layer) {
		this.game = game;
		this.velX = 0;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		this.layer = layer;
		setupBorders();
		showBorders(false);
	}
	public void setupBorders(){
		this.leftBound = new ImageView(GameImageBank.vertical_border);
		this.rightBound = new ImageView(GameImageBank.vertical_border);
		this.topBound = new ImageView(GameImageBank.horizontal_border);
		this.bottomBound = new ImageView(GameImageBank.horizontal_border);

		this.setDimensions_h(bottomBound);
		this.setDimensions_h(topBound,60);
		this.setDimensions_v(leftBound);
		this.setDimensions_v(rightBound);

		this.leftBound.setX(0);
		this.rightBound.setX(GameSettings.SCREEN_WIDTH - borderSize);
		this.leftBound.setY(60);
		this.rightBound.setY(60);
		this.topBound.setX(-5);
		this.topBound.setY(0);
		this.bottomBound.setX(-5);
		this.bottomBound.setY(GameSettings.SCREEN_HEIGHT - borderSize);
		this.displayBorders();
	}
	public void setDimensions_h(ImageView view){
		view.setFitWidth(GameSettings.SCREEN_WIDTH+5);
		view.setFitHeight(borderSize+5);
	}
	public void setDimensions_h(ImageView view, double height){
		view.setFitWidth(GameSettings.SCREEN_WIDTH+5);
		view.setFitHeight(height);
	}
	public void setDimensions_v(ImageView view){
		view.setFitWidth(borderSize);
		view.setFitHeight(GameSettings.SCREEN_HEIGHT-80);
	}
	public void setDimensions_v(ImageView view, double width){
		view.setFitWidth(width);
		view.setFitHeight(GameSettings.SCREEN_HEIGHT);
	}
	public void displayBorders(){
		this.layer.getChildren().add(topBound);
		this.layer.getChildren().add(bottomBound);
		this.layer.getChildren().add(leftBound);
		this.layer.getChildren().add(rightBound);

	}
	public void showBorders(boolean state){
		this.leftBound.setVisible(state);
		this.rightBound.setVisible(state);
		this.topBound.setVisible(state);
		this.bottomBound.setVisible(state);
	}
	public void removeBorders(){
		this.layer.getChildren().remove(leftBound);
		this.layer.getChildren().remove(rightBound);
		this.layer.getChildren().remove(topBound);
		this.layer.getChildren().remove(bottomBound);
	}
	public void readdBorders(){
		this.layer.getChildren().add(leftBound);
		this.layer.getChildren().add(rightBound);
		this.layer.getChildren().add(topBound);
		this.layer.getChildren().add(bottomBound);
	}
}
