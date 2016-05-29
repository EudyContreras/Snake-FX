package com.SnakeGame.AbstractModels;

public class AbstractObjectR {
	   private float x;
	   private float y;

	   public AbstractObjectR(float x, float y) {
	      this.x = x;
	      this.y = y;
	   }

	   public float getX() {
	      return x;
	   }

	   public float getY() {
	      return y;
	   }

	   public void move(float speed, float direction) {
	      x += (float)(Math.sin(Math.toRadians(direction)))*speed;
	      y += (float)(Math.cos(Math.toRadians(direction)))*speed;
	   }

	   public void set(float x, float y) {
	      this.x = x;
	      this.y = y;
	   }
	}
