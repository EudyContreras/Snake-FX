package com.EudyContreras.Snake.Utilities;


import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ShapeUtility {


	public final static RadialGradient RADIAL_GRADIENT(Color colorOne, Color colorTwo){

		Stop[] stops = new Stop[] { new Stop(0, colorOne), new Stop(1, colorTwo)};

		RadialGradient RADIAL_GRADIENT = new RadialGradient(0, .1, 100,100,20,false,CycleMethod.NO_CYCLE,stops);

		return RADIAL_GRADIENT;
	}

//	public final static LinearGradient LINEAR_GRADIENT(Color colorOne, Color colorTwo){
//
//		Stop[] stops = new Stop[] { new Stop(0.25, colorOne), new Stop(0.50, colorTwo),new Stop(0.75, colorOne)};
//
//		LinearGradient LINEAR_GRADIENT = new LinearGradient(1, 0.5, 1, 0.0, true, CycleMethod.NO_CYCLE, stops);
//
//		return LINEAR_GRADIENT;
//	}

	public final static LinearGradient LINEAR_GRADIENT(Color... colors){

		double offset = 1.0/colors.length;

		Stop[] stops = new Stop[colors.length];

		for(int i = 0; i<colors.length; i++){
			stops[i] = new Stop(offset*i, colors[i]);
		}

		LinearGradient LINEAR_GRADIENT = new LinearGradient(0, 0.2, 0, 1, true, CycleMethod.NO_CYCLE, stops);

		return LINEAR_GRADIENT;
	}

	public final static ImagePattern IMAGE_FILL(Image image){

		ImagePattern IMAGE_FILL = new ImagePattern(image);

		return IMAGE_FILL;
	}

	public final static Color COLOR_FILL(Color color, double hueShift, double saturation, double brightness, double opacity){

		Color COLOR_FILL =color.deriveColor(hueShift, saturation, brightness, opacity);

		return COLOR_FILL;
	}

	public final static void CENTER_SHAPE(Shape shape, Node node, Dimension2D dimension){
		CENTER_SHAPE(shape,Center.CENTER_X_Y, node, dimension);
	}

	public final static void CENTER_SHAPE(Shape shape, Center center, Node node, Dimension2D dimension){
		double width = dimension.getWidth();
		double height = dimension.getHeight();
		double shapeWidth = 0;
		double shapeHeight = 0;

		switch(shape){
		case CIRCLE:
			shapeWidth = ((Circle)node).getRadius();
			shapeHeight = ((Circle)node).getRadius();
			break;
		case RECTANGLE:
			shapeWidth = ((Rectangle)node).getWidth();
			shapeHeight = ((Rectangle)node).getHeight();
			break;
		}

		switch(center){
		case CENTER_X:
			node.setTranslateX((width/2)-(shapeWidth/2));
			break;
		case CENTER_Y:
			node.setTranslateY((height/2)-(shapeHeight/2));
			break;
		case CENTER_X_Y:
			node.setTranslateX((width/2)-(shapeWidth/2));
			node.setTranslateY((height/2)-(shapeHeight/2));
			break;
		}
	}

	public static enum Center{
		CENTER_X, CENTER_Y, CENTER_X_Y
	}
	public static enum Shape{
		RECTANGLE, CIRCLE
	}
}
