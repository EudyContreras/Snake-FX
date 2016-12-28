package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.UserInterface.MenuButtonStyles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ConnectWindow extends StackPane{

	private Rectangle background;
	private Pane frame;
	private HBox hBoxRegion;


	public ConnectWindow() {
		super();
		this.hBoxRegion = new HBox(10);
		this.frame = new StackPane();
		this.frame.setPadding(new Insets(150,100,150,100));
		this.frame.getChildren().add(hBoxRegion);
		this.hBoxRegion.setAlignment(Pos.CENTER);
		this.background = new Rectangle();
		this.background.setWidth(GameSettings.WIDTH-300);
		this.background.setHeight(GameSettings.HEIGHT-300);
		this.background.setStyle(MenuButtonStyles.FX_CONNECT_BOX_STYLE);
		this.background.setStroke(Color.rgb(215, 215, 215));
		this.background.setStrokeWidth(7);
		this.background.setFill(Color.BLACK);
		this.background.setArcHeight(50);
		this.background.setArcWidth(50);
		setTranslateX(GameSettings.WIDTH / 2 - background.getWidth()/2);
		setTranslateY(-background.getHeight()-50);
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

	public void addNodeToRegion(Node... node){
		hBoxRegion.getChildren().addAll(node);
	}

	public void setMenuBoxBackground(Paint fill) {
		this.background.setFill(fill);
	}

	public Rectangle getCustomBackground() {
		return background;
	}
}
