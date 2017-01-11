package com.EudyContreras.Snake.Utilities;

import javafx.animation.Transition;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class ResizeTransition extends Transition {

	protected Region region;
	protected double fromHeight;
	protected double fromWidth;
	protected double toHeight;
	protected double toWidth;
	protected double heightDiff;
	protected double widthDiff;
	protected Resize resize;

	public ResizeTransition(Resize resize) {
		super();
		this.resize = resize;
	}

	public void setDuration(Duration duration) {
		setCycleDuration(duration);
	}

	private void computeDifference() {
		this.heightDiff = toHeight - fromHeight;
		this.widthDiff = toWidth - fromWidth;
	}

	public void fromWidth(double width) {
		this.fromWidth = width;
	}

	public void fromHeight(double height) {
		this.fromHeight = height;
	}

	public void toWidth(double width) {
		this.toWidth = width;
	}

	public void toHeight(double height) {
		this.toHeight = height;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	private void resizeRegion(double fraction){
		switch(resize){
		case HEIGHT:
			region.setMinHeight(fromHeight + (heightDiff * fraction));
			break;
		case WIDTH:
			region.setMinWidth(fromWidth + (widthDiff * fraction));
			break;
		case WIDTH_AND_HEIGHT:
			region.setMinWidth(fromWidth + (widthDiff * fraction));
			region.setMinHeight(fromHeight + (heightDiff * fraction));
			break;
		}
	}

	@Override
	protected void interpolate(double fraction) {
		resizeRegion(fraction);
	}

	@Override
	public void play(){
		computeDifference();
		super.play();
	}

	public enum Resize{
		WIDTH, HEIGHT, WIDTH_AND_HEIGHT
	}
}