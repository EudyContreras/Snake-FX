package com.EudyContreras.Snake.PathFinder;

public class AllowedTurns{

	private boolean allowedLeftTurns = true;
	private boolean allowedRightTurns = true;
	private boolean allowedUpTurns = true;
	private boolean allowedDownTurns = true;

	public AllowedTurns(){

	}
	public void checkAllowedTurns(){

	}
	public boolean isAllowedLeftTurns() {
		return allowedLeftTurns;
	}

	public void setAllowedLeftTurns(boolean allowedLeftTurns) {
		this.allowedLeftTurns = allowedLeftTurns;
	}

	public boolean isAllowedRightTurns() {
		return allowedRightTurns;
	}

	public void setAllowedRightTurns(boolean allowedRightTurns) {
		this.allowedRightTurns = allowedRightTurns;
	}

	public boolean isAllowedUpTurns() {
		return allowedUpTurns;
	}

	public void setAllowedUpTurns(boolean allowedUpTurns) {
		this.allowedUpTurns = allowedUpTurns;
	}

	public boolean isAllowedDownTurns() {
		return allowedDownTurns;
	}

	public void setAllowedDownTurns(boolean allowedDownTurns) {
		this.allowedDownTurns = allowedDownTurns;
	}

}
