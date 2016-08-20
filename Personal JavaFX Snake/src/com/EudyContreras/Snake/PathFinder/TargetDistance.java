package com.EudyContreras.Snake.PathFinder;

public class TargetDistance {
	private double x;
	private double y;
	private double width;
	private double height;
	private Double distance;
	private Double distanceX;
	private Double distanceY;
	private Double pseudoMagnitude;

	public TargetDistance(ObjectEvasionAI evader, AbstractCollisionMonitor warning) {
		super();
		this.x = warning.getX();
		this.y = warning.getY();
		this.width = warning.getWidth();
		this.height = warning.getHeight();
		this.pseudoMagnitude = width+height;
		this.distanceX = Math.abs(evader.getX() - x);
		this.distanceY = Math.abs(evader.getY() - y);
		this.distance = Math.abs(evader.getX() - x)+Math.abs(evader.getY() - y);
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
	public double getDistanceX() {
		return distanceX;
	}
	public double getDistanceY() {
		return distanceY;
	}
	public double getDistance() {
		return distance;
	}

	public enum Location{
		NORTH,SOUTH,EAST,WEST
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;

		TargetDistance test = (TargetDistance) obj;
		return pseudoMagnitude == test.pseudoMagnitude && (distance == test.distance || (distance != null && distance.equals(test.distance)));
	}
	@Override
	public int hashCode() {
		int hash = 7;
		Double data = distance;
		hash = (int) (31 * hash + distance);
		hash = 31 * hash + (null == data ? 0 : data.hashCode());
		return hash;
	}

}
