package com.rorkien.snowflakes;


public class Screen {
	public static Background background = new Background();
	
	public void tick() {
		background.tick();
	}
	public void render() {
		background.render();
	}
}
