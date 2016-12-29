package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.PlayRoomHub.ConnectLabel.GlowType;
import com.EudyContreras.Snake.PlayRoomHub.ConnectLabel.Style;
import com.EudyContreras.Snake.Utilities.ShapeUtility;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ConnectFrame{


	private Rectangle background;
	private StackPane mainContainer;
	private StackPane container;
	private ConnectLabel label;
	private VBox layout;

	public ConnectFrame(GameManager game){
		this(game,Color.TRANSPARENT,0,0);
	}

	public ConnectFrame(GameManager game, Paint fill, int width, int height) {
		super();
		this.layout = new VBox(20);
		this.container = new StackPane();
		this.mainContainer = new StackPane();
		this.background = new Rectangle();
		this.label = new ConnectLabel(game,"Online Users",40, 200, 100, Style.BLUE_STYLE);
	    this.label.setFrameGradient(Color.SILVER.brighter(), Color.SILVER.brighter(),Color.GRAY);
	    this.label.setFrameOpacity(50.0);
	    this.label.setFrameSize(width-20, 50);
	    this.label.setTextSize(40);
	    this.label.setBorderColor(Color.TRANSPARENT);
	    this.label.setTextGlow(GlowType.PULSING);
		this.background.setStroke(Color.DARKGRAY);
		this.background.setStrokeWidth(7);
		this.background.setArcHeight(50);
		this.background.setArcWidth(50);
		this.mainContainer.getChildren().addAll(background);
		this.mainContainer.getChildren().add(layout);
		this.layout.setAlignment(Pos.CENTER);
		this.layout.getChildren().add(label.get());
		this.layout.getChildren().add(container);
		this.background.setWidth(mainContainer.getWidth());
		this.background.setHeight(mainContainer.getHeight());
		setFrameGradient(Color.GRAY.brighter(),Color.SILVER.brighter(),Color.GRAY);
	}

	public void setFill(Paint fill){
		this.background.setFill(fill);
	}

	public void setFrameGradient(Color...colors){
		this.background.setStyle(null);
		this.background.setFill(ShapeUtility.LINEAR_GRADIENT(colors));
	}

	public void setBorderColor(Paint fill){
		this.background.setStroke(fill);
	}

	public void setFrameSize(double width, double height) {
		background.setWidth(width);
		background.setHeight(height);
		label.setFrameSize(width-20, 45);
	}

	public void setFrameLocation(double x, double y) {
		container.setTranslateX(x);
		container.setTranslateY(y);
	}

	public void setWindowOffset(double offsetX, double offsetY) {
		container.setTranslateX((GameSettings.WIDTH / 2 - background.getWidth() / 2) + offsetX);
		container.setTranslateY((GameSettings.HEIGHT / 2 - background.getHeight() / 2) + offsetY);
	}

	public StackPane get(){
		return mainContainer;
	}

	public void setMenuBoxBackground(Paint fill) {
		this.background.setFill(fill);
	}

	public Rectangle getCustomBackground() {
		return background;
	}

	public void addForeground(Pane node) {
		container.getChildren().add(node);
		background.setWidth(mainContainer.getWidth()+20);
		background.setHeight(mainContainer.getHeight()+20);
	}

	public void adjustDimension(){
		background.setWidth(mainContainer.getWidth()+20);
		background.setHeight(mainContainer.getHeight()+20);
	}
}
