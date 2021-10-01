package com.EudyContreras.Snake.PlayRoomHub;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;

public class StatusIndicator extends Circle{

	private RadialGradient gradient;
	private DropShadow dropShadow = new DropShadow();
	private InnerShadow innerShadow = new InnerShadow();


	public StatusIndicator(double radius){
		super(radius);
		innerShadow.setBlurType(BlurType.ONE_PASS_BOX);
		innerShadow.setOffsetY(0);
		innerShadow.setRadius(5);
		dropShadow.setRadius(5);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetX(0);
		dropShadow.setBlurType(BlurType.ONE_PASS_BOX);
		dropShadow.setInput(innerShadow);
	}

	public StatusIndicator getIndicator(String status){
		String colorValue;
		switch(status){
    	case ConnectedUser.AVAILABLE:
    		colorValue = "radial-gradient(focus-angle 45deg, focus-distance 50%, " +
    				"center 50% 50%, radius 50%, cyan 0%, dodgerblue 100%)";
    		gradient = RadialGradient.valueOf(colorValue);

    		innerShadow.setColor(Color.color(0, 0, 0, 1));

    		dropShadow.setColor(Color.color(0, 0, 0, 0.65));

			setEffect(dropShadow);
			setFill(gradient);

    		break;
    	case ConnectedUser.UNAVAILABLE:
    		colorValue = "radial-gradient(focus-angle 45deg, focus-distance 50%, " +
    				"center 50% 50%, radius 50%, red 0%, rgb(150,0,0) 100%)";
    		gradient = RadialGradient.valueOf(colorValue);

    		innerShadow.setColor(Color.color(0, 0, 0, 1));

    		dropShadow.setColor(Color.color(0, 0, 0, 0.65));

			setEffect(dropShadow);
			setFill(gradient);

    		break;
    	case ConnectedUser.PLAYING:
    		colorValue = "radial-gradient(focus-angle 45deg, focus-distance 50%, " +
    				"center 50% 50%, radius 50%, yellow 0%, orange 100%)";
    		gradient = RadialGradient.valueOf(colorValue);

    		innerShadow.setColor(Color.color(0, 0, 0, 1));

    		dropShadow.setColor(Color.color(0, 0, 0, 0.65));

			setEffect(dropShadow);
			setFill(gradient);

    		break;
    	}
		return this;
	}

}
