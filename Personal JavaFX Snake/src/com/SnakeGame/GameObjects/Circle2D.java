package com.SnakeGame.GameObjects;


import javafx.scene.shape.Circle;

public class Circle2D {
	private Circle circle;

	public Circle2D(double x, double y, double radius) {
		this.circle = new Circle(x,y,radius);
	}
	public boolean intersects(Circle shape){
		if(shape.getCenterX()>=circle.getCenterX() && shape.getCenterY() >= shape.getCenterY()){
			if(shape.getCenterX()<=circle.getCenterX()+circle.getRadius() && shape.getCenterY()<=circle.getCenterY()+circle.getRadius()){
				return true;
			}
		}
		return false;
	}

}
