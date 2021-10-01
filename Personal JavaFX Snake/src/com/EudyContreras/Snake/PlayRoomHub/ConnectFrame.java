package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.PlayRoomHub.ConnectLabel.GlowType;
import com.EudyContreras.Snake.PlayRoomHub.ConnectLabel.Style;
import com.EudyContreras.Snake.Utilities.FillUtility;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ConnectFrame{


	private FadeTransition fadeTransition;
	private DropShadow dropShadow;
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
		this.layout = new VBox(10);
		this.container = new StackPane();
		this.mainContainer = new StackPane();
		this.background = new Rectangle();
		this.dropShadow = new DropShadow();
		this.fadeTransition = new FadeTransition();
    	this.fadeTransition.setNode(mainContainer);
		this.label = new ConnectLabel(game,"Online Users",40, 200, 100, Style.BLUE_STYLE);
	    this.label.setFrameGradient(Color.SILVER.brighter(), Color.SILVER.brighter(),Color.GRAY);
	    this.label.setFrameOpacity(50.0);
	    this.label.setFrameSize(width-20, 65);
	    this.label.setTextSize(40);
	    this.label.setTextGlow(GlowType.STATIC);
		this.background.setStroke(Color.DARKGRAY);
		this.background.setStrokeWidth(7);
		this.background.setArcHeight(50);
		this.background.setArcWidth(50);
		this.layout.setAlignment(Pos.CENTER);
		this.layout.getChildren().add(label.get());
		this.layout.getChildren().add(container);
		this.mainContainer.getChildren().add(background);
		this.mainContainer.getChildren().add(layout);
		this.background.setWidth(mainContainer.getWidth());
		this.background.setHeight(mainContainer.getHeight());
		setFrameGradient(Color.GRAY.brighter(),Color.SILVER.brighter(),Color.GRAY);
	}

	public void setFill(Paint fill){
		this.background.setFill(fill);
	}

	public void setFrameGradient(Color...colors){
		this.background.setStyle(null);
		this.background.setFill(FillUtility.LINEAR_GRADIENT(colors));
	}

	public void setBorderColor(Paint fill){
		this.background.setStroke(fill);
	}

	public void setButtons(GameButton buttons){
		this.layout.getChildren().add(buttons.get());
	}

	public void setFrameSize(double width, double height) {
		background.setWidth(width);
		background.setHeight(height);
		label.setFrameSize(width-20, 55);
	}

	public void setFrameLocation(double x, double y) {
		container.setTranslateX(x);
		container.setTranslateY(y);
	}

	public void setWindowOffset(double offsetX, double offsetY) {
		container.setTranslateX((GameSettings.WIDTH / 2 - background.getWidth() / 2) + offsetX);
		container.setTranslateY((GameSettings.HEIGHT / 2 - background.getHeight() / 2) + offsetY);
	}

	public void setLabel(String label){
		this.label.setText(label);
	}

	public void activate(boolean state){
		if(state){
			label.setStyle(Style.BLUE_STYLE);
			label.setTextGlow(GlowType.STATIC);
		}else{
			label.setStyle(Style.SILVER_STYLE);
			label.setTextGlow(GlowType.NONE);
		}
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

	public void addRegion(Node node) {
		container.getChildren().add(node);
		background.setWidth(mainContainer.getWidth()+20);
		background.setHeight(mainContainer.getHeight()+20);
	}

	public void addRegionToMan(Pane node) {
		mainContainer.getChildren().remove(layout);
		mainContainer.getChildren().add(node);
		background.setWidth(mainContainer.getWidth()+20);
		background.setHeight(mainContainer.getHeight()+20);
	}

	public void adjustDimension(){
		background.setWidth(mainContainer.getWidth()+20);
		background.setHeight(mainContainer.getHeight()+20);
	}

	public void show(boolean state) {
		if(state){
			fadeTransition.setDuration(Duration.millis(120));
			fadeTransition.setFromValue(0);
			fadeTransition.setToValue(1);
			fadeTransition.play();
		}else{
			mainContainer.setOpacity(0);
		}
	}

	public ConnectLabel getLabel() {
		return label;
	}
}
