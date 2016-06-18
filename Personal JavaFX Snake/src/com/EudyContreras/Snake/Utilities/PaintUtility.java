package com.EudyContreras.Snake.Utilities;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class PaintUtility {
	private static RadialGradient RADIAL_GRADIENT;
	private static LinearGradient LINEAR_GRADIENT;
	private static ImagePattern IMAGE_FILL;
	private static Color COLOR_FILL;

	public final static RadialGradient RADIAL_GRADIENT(Color colorOne, Color colorTwo){
		Stop[] stops = new Stop[] { new Stop(0, colorOne), new Stop(1, colorTwo)};
		RADIAL_GRADIENT = new RadialGradient(0, .1, 100,100,20,false,CycleMethod.NO_CYCLE,stops);
		return RADIAL_GRADIENT;

	}
	public final static LinearGradient LINEAR_GRADIENT(Color colorOne, Color colorTwo){
		Stop[] stops = new Stop[] { new Stop(0, colorOne), new Stop(1, colorTwo),new Stop(2, colorOne)};
		LINEAR_GRADIENT = new LinearGradient(1, 0.8, 1, 0.0, true, CycleMethod.NO_CYCLE, stops);
		return LINEAR_GRADIENT;
	}
	public final static ImagePattern IMAGE_FILL(Image image){
		IMAGE_FILL = new ImagePattern(image);
		return IMAGE_FILL;
	}
	public final static Color COLOR_FILL(Color color, double hueShift, double saturation, double brightness, double opacity){
		COLOR_FILL =color.deriveColor(hueShift, saturation, brightness, opacity);
		return COLOR_FILL;
	}
}
