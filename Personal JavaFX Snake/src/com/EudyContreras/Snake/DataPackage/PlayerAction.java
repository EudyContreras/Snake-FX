package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

import com.EudyContreras.Snake.FrameWork.PlayerMovement;

public class PlayerAction implements Serializable {

	private static final long serialVersionUID = 1L;

	private PlayerMovement turnEvent;
	private boolean eatEvent;
	private boolean boostEvent;


	public PlayerAction(PlayerMovement turnEvent, boolean eatEvent, boolean boostEvent) {
		super();
		this.turnEvent = turnEvent;
		this.eatEvent = eatEvent;
		this.boostEvent = boostEvent;
	}

	public final PlayerMovement getTurnEvent() {
		return turnEvent;
	}

	public final boolean isEatEvent() {
		return eatEvent;
	}

	public final boolean isBoostEvent() {
		return boostEvent;
	}

	public enum ClientCommands{


	}
}