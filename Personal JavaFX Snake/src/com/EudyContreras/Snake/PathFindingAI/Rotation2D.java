package com.EudyContreras.Snake.PathFindingAI;

public class Rotation2D {

	private float rotationAngle = 90;
	private float rotationSpeed = 10;
	private float rotationDirection = 0;
	private float rotationLimit = 0;
	private boolean added = false;

	public Rotation2D(float rotationAngle, float rotationSpeed, float rotationDirection, float rotationLimit, boolean added) {
		super();
		this.rotationAngle = rotationAngle;
		this.rotationSpeed = rotationSpeed;
		this.rotationDirection = rotationDirection;
		this.rotationLimit = rotationLimit;
		this.added = added;
	}
	public final float getRotationAngle() {
		return rotationAngle;
	}
	public final float getRotationSpeed() {
		return rotationSpeed;
	}
	public final float getRotationDirection() {
		return rotationDirection;
	}
	public final float getRotationLimit() {
		return rotationLimit;
	}
	public final boolean isAdded() {
		return added;
	}

}
