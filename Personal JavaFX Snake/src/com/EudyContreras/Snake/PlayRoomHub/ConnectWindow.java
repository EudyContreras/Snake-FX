package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.UserInterface.MenuButtonStyles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ConnectWindow extends StackPane{

	@SuppressWarnings("unused")
	private GameManager game;
	private Rectangle background;
	private Pane frame;
	private HBox hBoxRegion;


	public ConnectWindow(GameManager game) {
		super();
		this.game = game;
		this.hBoxRegion = new HBox(20);
		this.frame = new StackPane();
		this.frame.setPadding(new Insets(175,20,80,20));
		this.frame.getChildren().add(hBoxRegion);
		this.hBoxRegion.setAlignment(Pos.CENTER);
		this.background = new Rectangle();
		this.background.setWidth(GameSettings.WIDTH-100);
		this.background.setHeight(GameSettings.HEIGHT-20);
		this.background.setStyle(MenuButtonStyles.FX_CONNECT_BOX_STYLE);
		this.background.setStroke(Color.rgb(215, 215, 215));
		this.background.setStrokeWidth(7);
		this.background.setFill(Color.BLACK);
		this.background.setArcHeight(90);
		this.background.setArcWidth(90);
		setTranslateX(GameSettings.WIDTH / 2 - background.getWidth()/2);
		setTranslateY(-background.getHeight()-60);
		getChildren().addAll(background,frame);
	}

	public void setWindowSize(double width, double height) {
		this.background.setWidth(width);
		this.background.setHeight(height);
	}

	public void setWindowLocation(double x, double y) {
		setTranslateX(x);
		setTranslateY(y);
	}

	public void setWindowOffset(double offsetX, double offsetY) {
		setTranslateX((GameSettings.WIDTH / 2 - background.getWidth() / 2) + offsetX);
		setTranslateY((GameSettings.HEIGHT / 2 - background.getHeight() / 2) + offsetY);
	}

	public void addRegion(int index, Node node){
		getChildren().add(index,node);
	}

	public void addRegion(Node node){
		getChildren().add(node);
	}

	public void addNodeToRegion(Node node){
		hBoxRegion.getChildren().add(node);
	}

	public void addNodeToRegion(int index, Node node){
		hBoxRegion.getChildren().add(index,node);
	}

	public void removeNodeFromRegion(Node node){
		hBoxRegion.getChildren().removeAll(node);
	}

	public void addNodesToRegion(Node... node){
		hBoxRegion.getChildren().addAll(node);
	}

	public void removeNodeFromRegion(Node... node){
		hBoxRegion.getChildren().removeAll(node);
	}

	public void setMenuBoxBackground(Paint fill) {
		this.background.setFill(fill);
	}

	public Rectangle getCustomBackground() {
		return background;
	}
}
