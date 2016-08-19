package com.EudyContreras.Snake.PathFinder;

public class TargetDistance {
	private double x;
	private double y;
	private double width;
	private double height;
	private ObjectEvasionAI evader;

	public TargetDistance(ObjectEvasionAI evader, double x, double y, double width, double height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.evader = evader;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getDistance() {
		return Math.abs(evader.getX() - x)+Math.abs(evader.getY() - y);
	}

}
