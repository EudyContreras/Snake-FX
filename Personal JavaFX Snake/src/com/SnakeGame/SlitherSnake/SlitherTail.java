package com.SnakeGame.SlitherSnake;

import com.SnakeGame.Core.SnakeGame;
import com.SnakeGame.ObjectIDs.GameObjectID;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class SlitherTail extends SlitherMain {
	SlitherSection slitherSect;
	SlitherSection sectionToFollow;

	public SlitherTail(SlitherSection section, SnakeGame game, Pane layer, Circle node, float x, float y,
			GameObjectID id) {
		super(game, layer, node, id);
		this.slitherSect = section;
		this.x = x;
		this.y = y;
	}

	public void move() {
		// this.y = (double) (sectionToFollow.getY()+
		// this.circle.getRadius()*1.5);
		// this.x = sectionToFollow.getX();
		// this.r = 180;
		//
		// snake.getTail().setX((float) (getX() +
		// Math.sin(Math.toRadians(rotation)) * velX * speed));
		// snake.getTail().setY((float) (getY() +
		// Math.cos(Math.toRadians(rotation)) * velY * speed));

		//
		float xChange = sectionToFollow.getX() - getX();
		float yChange = sectionToFollow.getY() - getY();

		float angle = (float) Math.atan2(yChange, xChange);

		x = (sectionToFollow.getX() - (float) Math.cos(angle) * 20);
		y = (sectionToFollow.getY() - (float) Math.sin(angle) * 20);
		r = ((sectionToFollow.getR()));
	}

	public void setWhoToFollow(SlitherSection slitherSect) {
		sectionToFollow = slitherSect;
	}

	public boolean isApproximate(double tail_X, double sect_X, double tail_Y, double sect_Y) {
		double distance = Math.sqrt((tail_X - sect_X) * (tail_X - sect_X) + (tail_Y - sect_Y) * (tail_Y - sect_Y));
		if (distance > 10) {
			return true;
		}
		return false;
	}

	public void checkRemovability() {

	}

	public void checkCollision() {

	}

}
