package com.EudyContreras.Snake.PathFinder;

public class TurnMonitor{

	private boolean allowMoveUp = true;
	private boolean allowMoveLeft = true;
	private boolean allowMoveRight = true;
	private boolean allowMoveDown = true;

	public synchronized void setAllowMoveUp(boolean allowedUpTurns) {
		this.allowMoveUp = allowedUpTurns;
	}

	public synchronized void setAllowMoveLeft(boolean allowedLeftTurns) {
		this.allowMoveLeft = allowedLeftTurns;
	}

	public synchronized void setAllowMoveRight(boolean allowedRightTurns) {
		this.allowMoveRight = allowedRightTurns;
	}

	public synchronized void setAllowMoveDown(boolean allowedDownTurns) {
		this.allowMoveDown = allowedDownTurns;
	}

	public final boolean isAllowMoveUp() {
		return allowMoveUp;
	}

	public final boolean isAllowMoveLeft() {
		return allowMoveLeft;
	}

	public final boolean isAllowMoveRight() {
		return allowMoveRight;
	}

	public final boolean isAllowMoveDown() {
		return allowMoveDown;
	}


}
