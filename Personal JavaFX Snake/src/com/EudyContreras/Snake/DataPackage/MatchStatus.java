package com.EudyContreras.Snake.DataPackage;

import java.io.Serializable;

public class MatchStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean paused;
	private boolean ended;
	private boolean ready;
	private boolean terminated;
	private boolean countDown;
	private boolean deathEvent;
	/**
	 * @param paused
	 * @param ended
	 * @param ready
	 * @param terminated
	 * @param countDown
	 * @param deathEvent
	 */
	public MatchStatus(boolean paused, boolean ended, boolean ready, boolean terminated, boolean countDown,
			boolean deathEvent) {
		super();
		this.paused = paused;
		this.ended = ended;
		this.ready = ready;
		this.terminated = terminated;
		this.countDown = countDown;
		this.deathEvent = deathEvent;
	}
	public final boolean isPaused() {
		return paused;
	}
	public final boolean isEnded() {
		return ended;
	}
	public final boolean isReady() {
		return ready;
	}
	public final boolean isTerminated() {
		return terminated;
	}
	public final boolean isCountDown() {
		return countDown;
	}
	public final boolean isDeathEvent() {
		return deathEvent;
	}




}