package com.EudyContreras.Snake.FrameWork;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class ResizeListener implements ChangeListener<Number> {

	private Pane pane;
	private Scene scene;
	private double ratio;
	private double initHeight;
	private double initWidth;

	public ResizeListener(Stage stage, Scene scene, double ratio, double initHeight, double initWidth, Pane pane) {
		this.pane = pane;
		this.scene = scene;
		this.ratio = ratio;
		this.initHeight = initHeight;
		this.initWidth = initWidth;
	}
	public void setNewRatio(double ratio, double initHeight, double initWidth){
		this.ratio = ratio;
		this.initHeight = initHeight;
		this.initWidth = initWidth;
	}
	@Override
	public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {

		final double newWidth = scene.getWidth();
		final double newHeight = scene.getHeight();

		double scaleFactor = newWidth / newHeight > ratio ? newHeight / initHeight : newWidth / initWidth;

		Scale scale = new Scale(scaleFactor, scaleFactor);

		scale.setPivotX(0);
		scale.setPivotY(0);

		scene.getRoot().getTransforms().setAll(scale);

		pane.setPrefWidth(newWidth / scaleFactor);
		pane.setPrefHeight(newHeight / scaleFactor);

	}


}
