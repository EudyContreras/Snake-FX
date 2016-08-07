package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

public class PlayerStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	private final boolean damageEvent;
	private final boolean collisionEvent;
	private final boolean selfCollisionEvent;
	private final boolean teleportEvent;
	private final boolean deathEvent;


	/**
	 * @param turnEvent
	 * @param eatEvent
	 * @param boostEvent
	 * @param damageEvent
	 * @param collisionEvent
	 * @param selfCollisionEvent
	 * @param teleportEvent
	 * @param deathEvent
	 */
	public PlayerStatus(boolean damageEvent,boolean collisionEvent, boolean selfCollisionEvent, boolean teleportEvent, boolean deathEvent) {
		super();
		this.damageEvent = damageEvent;
		this.collisionEvent = collisionEvent;
		this.selfCollisionEvent = selfCollisionEvent;
		this.teleportEvent = teleportEvent;
		this.deathEvent = deathEvent;
	}

	public final boolean isDamageEvent() {
		return damageEvent;
	}

	public final boolean isCollisionEvent() {
		return collisionEvent;
	}

	public final boolean isSelfCollisionEvent() {
		return selfCollisionEvent;
	}

	public final boolean isTeleportEvent() {
		return teleportEvent;
	}

	public final boolean isDeathEvent() {
		return deathEvent;
	}


}