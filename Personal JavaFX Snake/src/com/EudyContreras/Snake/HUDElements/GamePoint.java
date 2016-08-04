package com.EudyContreras.Snake.HUDElements;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GamePoint extends Text{

	public GamePoint(Pane layer, double x, double y){
		setTranslateX(x-10);
		setTranslateY(y);
		setText("+1");
		setFont(Font.font(null,FontWeight.EXTRA_BOLD, 55));
        setId("point");
        FadeTransition fade = new FadeTransition(Duration.millis(1000),this);
        TranslateTransition translate = new TranslateTransition(Duration.millis(1000), this);
        fade.setFromValue(1);
        fade.setToValue(0);
        translate.setByX(15);
        translate.setByY(-40);
        fade.play();
        translate.play();
        fade.setOnFinished(e ->{
        	layer.getChildren().remove(this);
        });

	}


}