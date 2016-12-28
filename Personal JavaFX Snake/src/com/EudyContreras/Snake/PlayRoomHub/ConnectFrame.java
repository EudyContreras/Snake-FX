package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.Application.GameSettings;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ConnectFrame extends StackPane{

	private Rectangle background;

	/**
	 *
	 * @param manager
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param spacing
	 * @param fill
	 * @param alignment
	 */

	public ConnectFrame(){
		this(Color.TRANSPARENT,0,0);
	}

	public ConnectFrame(Paint fill, int width, int height) {
		super();
		this.background = new Rectangle();
		this.background.setStroke(Color.rgb(215, 215, 215));
		this.background.setStrokeWidth(7);
		this.background.setFill(Color.BLACK);
		this.background.setArcHeight(50);
		this.background.setArcWidth(50);
		this.background.widthProperty().bind(widthProperty().subtract(20));
		this.background.heightProperty().bind(heightProperty().subtract(20));
		getChildren().addAll(background);
	}

	public void setFill(Paint fill){
		this.background.setFill(fill);
	}

	public void setFrameSize(double width, double height) {
		this.setMaxSize(width, height);
		this.setPrefSize(width, height);
	}

	public void setFrameLocation(double x, double y) {
		setTranslateX(x);
		setTranslateY(y);
	}

	public void setWindowOffset(double offsetX, double offsetY) {
		setTranslateX((GameSettings.WIDTH / 2 - background.getWidth() / 2) + offsetX);
		setTranslateY((GameSettings.HEIGHT / 2 - background.getHeight() / 2) + offsetY);
	}

	public void setMenuBoxBackground(Paint fill) {
		this.background.setFill(fill);
	}

	public Rectangle getCustomBackground() {
		return background;
	}
}
