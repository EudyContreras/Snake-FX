package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

public class PlayerAction implements Serializable {

	private static final long serialVersionUID = 1L;

	private TurnEvent turnEvent;
	private boolean eatEvent;
	private boolean boostEvent;


	public PlayerAction(TurnEvent turnEvent, boolean eatEvent, boolean boostEvent) {
		super();
		this.turnEvent = turnEvent;
		this.eatEvent = eatEvent;
		this.boostEvent = boostEvent;
	}

	public final TurnEvent getTurnEvent() {
		return turnEvent;
	}

	public final boolean isEatEvent() {
		return eatEvent;
	}

	public final boolean isBoostEvent() {
		return boostEvent;
	}

	public enum TurnEvent implements Serializable {
		LEFT, RIGHT, UP, DOWN,

	}
}