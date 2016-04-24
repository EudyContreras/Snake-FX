package com.rorkien.snowflakes;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class GameApplet extends Applet {
	private static final long serialVersionUID = 1L;
	private Game game = new Game();

	public void init() {
		setLayout(new BorderLayout());
		Dimension d = new Dimension(Game.WIDTH, Game.HEIGHT);
		setSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setPreferredSize(d);
		add(game, BorderLayout.CENTER);
	}

	public void start() {
		game.start();
	}

	public void stop() {
		game.stop();
	}
}
