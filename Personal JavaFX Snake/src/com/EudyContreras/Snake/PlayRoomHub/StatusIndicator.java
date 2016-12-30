package com.EudyContreras.Snake.PlayRoomHub;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

public class StatusIndicator extends Circle{

	private LinearGradient gradient;
	private DropShadow dropShadow = new DropShadow();
	private InnerShadow innerShadow = new InnerShadow();


	public StatusIndicator(double radius){
		super(radius);
		innerShadow.setBlurType(BlurType.GAUSSIAN);
		innerShadow.setOffsetY(5);
		innerShadow.setRadius(5);
		dropShadow.setRadius(5);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetX(0);
		dropShadow.setBlurType(BlurType.GAUSSIAN);
		dropShadow.setInput(innerShadow);
	}

	public StatusIndicator getIndicator(String status){
		switch(status){
    	case ConnectedUser.AVAILABLE:
    		gradient = new LinearGradient(0, 18, 0, 110,
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.rgb(0,100,250)),
                    new Stop(1.0, Color.rgb(0,70,150)));


    		innerShadow.setColor(Color.color(0, 0, 0, 1));

    		dropShadow.setColor(Color.color(0, 0, 0, 0.65));

			setEffect(dropShadow);
			setFill(gradient);

    		break;
    	case ConnectedUser.UNAVAILABLE:

    		gradient = new LinearGradient(0, 18, 0, 110,
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.rgb(250, 0, 0)),
                    new Stop(1.0, Color.rgb(100, 0, 0)));


    		innerShadow.setColor(Color.color(0, 0, 0, 1));

    		dropShadow.setColor(Color.color(0, 0, 0, 0.65));

			setEffect(dropShadow);
			setFill(gradient);

    		break;
    	case ConnectedUser.PLAYING:
    		gradient = new LinearGradient(0, 18, 0, 110,
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.rgb(0,100,250)),
                    new Stop(1.0, Color.rgb(0,70,150)));


    		innerShadow.setColor(Color.color(0, 0, 0, 1));

    		dropShadow.setColor(Color.color(0, 0, 0, 0.65));

			setEffect(dropShadow);
			setFill(gradient);

    		break;
    	}
		return this;
	}

}
