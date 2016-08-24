
package com.EudyContreras.Snake.PathFinder;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameSettings;

import javafx.geometry.Rectangle2D;

/**
 * This class is the game tile super class and is the class that every tile
 * object or level object extends. Events within this class will reflect on the
 * children of this class.
 *
 * @author Eudy Contreras
 *
 */
public abstract class AbstractCollisionMonitor {


	protected double x;
	protected double y;
	protected double velX;
	protected double velY;
	protected double width;
	protected double height;
	protected double lifeTime = 60;
	protected boolean alive = true;
	protected boolean hasTarger = false;
	protected boolean proneToCollision = false;
	protected AbstractTile collideTarget;
	protected RayDirection direction;
	protected Status status;

	public AbstractCollisionMonitor() {

	}

	public AbstractCollisionMonitor(double x, double y, RayDirection direction) {
		super();
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public AbstractCollisionMonitor(double x, double y, double width, double height, RayDirection direction) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.direction = direction;
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

	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
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

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public double getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(double lifeTime) {
		this.lifeTime = lifeTime;
	}

	public boolean hasTarget() {
		return hasTarger;
	}

	public void setHasTarger(boolean hasTarger) {
		this.hasTarger = hasTarger;
	}

	public boolean isProneToCollision() {
		return proneToCollision;
	}

	public void setProneToCollision(boolean proneToCollision) {
		this.proneToCollision = proneToCollision;
	}
	public Status getStatus(){
		return status;
	}
	public void setStatus(Status status){
		this.status = status;
	}
	public RayDirection getDirection() {
		return direction;
	}

	public void setDirection(RayDirection direction) {
		this.direction = direction;
	}
	public void updateLogic(){

	}
	public void checkCollision(){

	}
	public void checkRemovability() {
		if(x>GameSettings.WIDTH || x<0 || y<0 || y>GameSettings.HEIGHT){
			this.setAlive(false);
			this.setStatus(Status.OUT_OF_RANGE);
		}
	}
	public void move() {
		x = x + velX;
		y = y + velY;
	}

	public AbstractTile getEminentCollider() {
		return collideTarget;
	}

	public void setEminentCollider(AbstractTile collideTarget) {
		this.collideTarget = collideTarget;
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D(x, y, width, height);
	}

	public enum RayDirection{
		UP,DOWN,LEFT,RIGHT,UP_LEFT,UP_RIGHT,DOWN_LEFT,DOWN_RIGHT
	}
	public enum Status{
		TRAVELING,COLLIDED,OUT_OF_RANGE
	}


}
