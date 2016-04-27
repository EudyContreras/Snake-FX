package application;


public class Gravity {
	
	private Boolean falling = true;
	private Boolean notDestroyed = true;
	private float gravity = 0.4f;
	private float x,y = 0;
	private float xVel, yVel = 0;
	private float maxSpeed = 4;
	
	public Gravity(float x, float y, float xVel, float yVel ){
		this.x = x;
		this.y = y;
		this.xVel = xVel;
		this.yVel = yVel;
		
	}
	public void GravityG(float x, float y, float xVel, float yVel ){
		this.x = x;
		this.y = y;
		this.xVel = xVel;
		this.yVel = yVel;
		
	}
	public Boolean getFalling() {
		return falling;
	}
	public float getGravity() {
		return gravity;
	}
	public void setGravity(float gravity) {
		this.gravity = gravity;
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
	public float getxVel() {
		return xVel;
	}
	public void setxVel(float xVel) {
		this.xVel = xVel;
	}
	public float getyVel() {
		return yVel;
	}
	public void setyVel(float yVel) {
		this.yVel = yVel;
	}
	public void setFalling(Boolean falling) {
		this.falling = falling;
	}
	public Boolean getNotDestroyed() {
		return notDestroyed;
	}
	public void setNotDestroyed(Boolean notDestroyed) {
		this.notDestroyed = notDestroyed;
	}
	public float getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

}
