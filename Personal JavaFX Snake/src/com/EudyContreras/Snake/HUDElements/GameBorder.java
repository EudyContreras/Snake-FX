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
	private double borderSize = 10;
	private ImageView leftBound;
	private ImageView rightBound;
	private ImageView topBound;
	private ImageView bottomBound;
	private Group layer;

	public GameBorder(GameManager game, Group layer) {
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
		this.setDimensions_h(topBound);
		this.setDimensions_v(leftBound);
		this.setDimensions_v(rightBound);

		this.leftBound.setX(0);
		this.rightBound.setX(GameSettings.WIDTH - borderSize);
		this.topBound.setX(0);
		this.topBound.setY(0);
		this.bottomBound.setX(0);
		this.bottomBound.setY(GameSettings.HEIGHT - borderSize);
		this.displayBorders();
	}
	public void setDimensions_h(ImageView view){
		view.setFitWidth(GameSettings.WIDTH);
		view.setFitHeight(borderSize);
	}
	public void setDimensions_v(ImageView view){
		view.setFitWidth(borderSize);
		view.setFitHeight(GameSettings.HEIGHT);
	}
	public void displayBorders(){
		this.layer.getChildren().add(leftBound);
		this.layer.getChildren().add(rightBound);
		this.layer.getChildren().add(topBound);
		this.layer.getChildren().add(bottomBound);
	}
	public void showBorders(boolean state){
		this.leftBound.setVisible(state);
		this.rightBound.setVisible(state);
		this.topBound.setVisible(state);
		this.bottomBound.setVisible(state);
	}

}
