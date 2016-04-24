package com.SnakeGame.Core;

public class SlitherMain3 {
	protected float x;
	protected float y;
	protected float delta;
	protected float direction;

	public SlitherMain3(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	// direction is degrees (0-360, but you can choose -57829547 too ;) )
	public void move() {
		x += (float) (Math.sin(Math.toRadians(direction))) * delta;
		y += (float) (Math.cos(Math.toRadians(direction))) * delta;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
}