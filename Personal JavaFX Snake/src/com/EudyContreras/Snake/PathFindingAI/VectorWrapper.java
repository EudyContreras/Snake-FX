package com.EudyContreras.Snake.PathFindingAI;

public class VectorWrapper {

	private double x;
	private double y;

	public VectorWrapper(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}

	public double magnitude() {
		return (double) Math.sqrt(x * x + y * y);
	}

	public void add(VectorWrapper v) {
		x += v.x;
		y += v.y;
	}

	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
	}

	public void multiply(double n) {
		x *= n;
		y *= n;
	}

	public void div(double n) {
		x /= n;
		y /= n;
	}

	public void normalize() {
		double m = magnitude();
		if (m != 0 && m != 1) {
			div(m);
		}
	}

	public void limit(double max) {
		if (magnitude() > max) {
			normalize();
			multiply(max);
		}
	}

	public double angle() {
		double angle = (double) Math.atan2(-y, x);
		return -1 * angle;
	}

	public static VectorWrapper subtract(VectorWrapper v1, VectorWrapper v2) {
		return new VectorWrapper(v1.x - v2.x, v1.y - v2.y);
	}

}
