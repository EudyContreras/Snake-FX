package com.SnakeGame.FrameWork;

/**
 * Class used to translate a given object across the game world allowing the
 * object to move towards different directions and keeping the object on the
 * center of the screen at all times.
 * 
 * @author Eudy Contreras
 *
 */

public class GameCamera {
	private float x;
	private float y;

	public GameCamera(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void followPlayer(GameObject player) {
		y = (float) (-player.getY() + Settings.HEIGHT - 300);
		x = (float) (-player.getX() + Settings.WIDTH - 600);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

}
