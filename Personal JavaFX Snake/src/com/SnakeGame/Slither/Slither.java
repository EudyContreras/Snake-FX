package com.SnakeGame.Slither;

import java.util.LinkedList;

public class Slither extends SlitherMain3{
	LinkedList<SnakePart> parts = new LinkedList<>();
	public Slither(float x, float y) {
		super(x, y);
		for(int i = 0; i<6; i++	) {
		 SnakePart part = new SnakePart();
		 parts.add(part);
		}
	}
	public void move() {

	      SnakePart snakeHead = parts.get(0);
	      int snakeLength = parts.size() - 1;

	      snakeHead.setPosX((float)(snakeHead.getPosX() + Math.sin(Math.toRadians(snakeHead.getRotDegree())) * snakeHead.getSpeed() * delta));
	      snakeHead.setPosY((float)(snakeHead.getPosY() + Math.cos(Math.toRadians(snakeHead.getRotDegree())) * snakeHead.getSpeed() * delta));

	      for (int i = 1; i <= snakeLength; i++) {

	         SnakePart partBefore = parts.get(i-1);
	         SnakePart thisPart = parts.get(i);

	         float xChange = partBefore.getPosX() - thisPart.getPosX();
	         float yChange = partBefore.getPosY() - thisPart.getPosY();

	         float angle = (float)Math.atan2(yChange, xChange);

	         thisPart.setPosX(partBefore.getPosX() - (float)Math.cos(angle) * 10);
	         thisPart.setPosY(partBefore.getPosY() - (float)Math.sin(angle) * 10);

	      }

	   }
	public class SnakePart{
		float x;
		float y;
		float speed;
		float degree;
		public float getPosX() {
			// TODO Auto-generated method stub
			return x;
		}

		public float getPosY() {
			// TODO Auto-generated method stub
			return y;
		}
		public float getSpeed() {
			// TODO Auto-generated method stub
			return speed;
		}

		public float getRotDegree() {
			// TODO Auto-generated method stub
			return degree;
		}

		public void setPosX(float x) {
			this.x = x;

		}
		public void setPosY(float y) {
			this.y = y;

		}
		public void move() {

		}

	}
}
