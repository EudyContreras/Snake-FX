package com.EudyContreras.Snake.FrameWork;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

public class RsizeListener implements ChangeListener<Number> {

	private final Pane pane;
	private final Scene scene;
	private final double ratio;
	private final double initHeight;
	private final double initWidth;


	public RsizeListener(Scene scene, double ratio, double initHeight, double initWidth, Pane pane) {
		this.pane = pane;
		this.scene = scene;
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
