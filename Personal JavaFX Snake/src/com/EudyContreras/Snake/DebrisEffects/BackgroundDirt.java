package com.EudyContreras.Snake.DebrisEffects;

import com.EudyContreras.Snake.AbstractModels.AbstractDebrisEffect;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.EnumIDs.GameDebrisID;
import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class BackgroundDirt extends AbstractDebrisEffect {

	private GameDebrisID id;
	private double radius = Math.random() * (3.5 - 1 + 1) + 1 / (GameLoader.ResolutionScaleX);
	private Pane layer;

	public BackgroundDirt(GameManager game, Pane layer, Image image, double x, double y) {
		this.game = game;
		this.layer = layer;
		this.view = new ImageView(image);
		this.view.setFitWidth(radius*2);
		this.view.setFitHeight(radius*2);
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		init();
	}

	public void init() {
		layer.getChildren().add(view);
		checkSurroundings(() -> collide());
	}
	public void checkSurroundings(Runnable script){
		script.run();
	}
	public void update() {

	}
	public void move() {


	}
	public void collide() {
		for(AbstractTile block: game.getGameLoader().getTileManager().getBlock()){
			if(getBounds().intersects(block.getBounds())){
				this.layer.getChildren().remove(this.view);
			}
		}
	}

	public boolean isAlive() {
		return x < GameSettings.WIDTH && x > 0 && y < GameSettings.HEIGHT && y > 0 ;
	}

	public void draw(GraphicsContext gc) {

	}

	public Rectangle2D getBounds() {

		return new Rectangle2D(x,y,radius,radius);
	}

	public Rectangle2D getBoundsTop() {

		return null;
	}

	public Rectangle2D getBoundsRight() {

		return null;
	}

	public Rectangle2D getBoundsLeft() {

		return null;
	}

	public GameDebrisID getID() {
		return id;
	}

	public void setID(GameDebrisID id) {
		this.id = id;
	}
}
