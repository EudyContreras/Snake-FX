package com.EudyContreras.Snake.Utilities;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ResizeAnimation implements Animator{

	public static final int RESIZE_WIDTH = 0;
	public static final int RESIZE_HEIGHT = 1;
	public static final int RESIZE_WIDTH_HEIGHT = 2;
	private List<AnimatorListener> listeners;
	private Interpolator interpolator;
	private Runnable endScript;
	private Period playTime;
	private Timeline timeLine;
	private Rectangle node;
	private Region region;
    private long startTime = -1;
	private double startWidth;
	private double startHeight;
	private double endWidth;
	private double endHeight;
  	private double newWidth;
    private double newHeight;
	private int resizeAxis;
	private int delay = 0;


	public ResizeAnimation(int axis){
		resizeAxis = axis;
		listeners = new ArrayList<>();
		timeLine = new Timeline();
        timeLine.setCycleCount( Timeline.INDEFINITE );

	}

    private void applyTransformation(float delta) {
    	double progress;
        switch (resizeAxis){
            case RESIZE_WIDTH:
                progress = interpolator.interpolate(delta);
                newWidth = startWidth + ((int) Math.round((endWidth - startWidth) * progress));
                node.setWidth(newWidth);

                break;
            case RESIZE_HEIGHT:
            	progress = interpolator.interpolate(delta);
                newHeight = startHeight + ((int) Math.round((endHeight - startHeight) * progress));
                node.setHeight(newHeight);

                break;
            case RESIZE_WIDTH_HEIGHT:
                progress = interpolator.interpolate(delta);
                newWidth = startWidth + ((int) Math.round((endWidth - startWidth) * progress));
                newHeight = startHeight + ((int) Math.round((endHeight - startHeight) * progress));
                node.setWidth(newWidth);
                node.setHeight(newHeight);

                break;
        }
    }

	protected void interpolate(float delta) {
		double progress;
        switch (resizeAxis){
            case RESIZE_WIDTH:
                progress = interpolator.interpolate(delta);
                newWidth = startWidth + ((int) Math.round((endWidth - startWidth) * progress));
                region.setPrefWidth(newWidth);
                region.setMinWidth(newWidth);
                region.setMaxWidth(newWidth);

                break;
            case RESIZE_HEIGHT:
            	progress = interpolator.interpolate(delta);
                newHeight = startHeight + ((int) Math.round((endHeight - startHeight) * progress));
                region.setPrefHeight(newHeight);
                region.setMinHeight(newHeight);
                region.setMaxHeight(newHeight);

                break;
            case RESIZE_WIDTH_HEIGHT:
                progress = interpolator.interpolate(delta);
                newWidth = startWidth + ((int) Math.round((endWidth - startWidth) * progress));
                newHeight = startHeight + ((int) Math.round((endHeight - startHeight) * progress));
                region.setPrefHeight(newHeight);
                region.setMinHeight(newHeight);
                region.setMaxHeight(newHeight);
                region.setPrefWidth(newWidth);
                region.setMinWidth(newWidth);
                region.setMaxWidth(newWidth);
                break;
        }
	}


	public void setNode(Rectangle node){
		this.node = node;
	}

	public void setRegion(Region region){
		this.region = region;
	}

	public void setResizeAxis(int axis) {
		this.resizeAxis = axis;
	}

	public double getStartWidth() {
		return startWidth;
	}

	public void setStartWidth(double startWidth) {
		this.startWidth = startWidth;
		this.newWidth = startWidth;
	}

	public double getStartHeight() {
		return startHeight;
	}

	public void setStartHeight(double startHeight) {
		this.startHeight = startHeight;
		this.newHeight = startHeight;
	}

	public double getEndWidth() {
		return endWidth;
	}

	public void setEndWidth(double endWidth) {
		this.endWidth = endWidth;
	}

	public double getEndHeight() {
		return endHeight;
	}

	public void setEndHeight(double endHeight) {
		this.endHeight = endHeight;
	}

	public void setDuration(Period duration){
		this.playTime = duration;
	}

	@Override
	public long getDuration() {
		return playTime!=null ? playTime.getDuration() : 0;
	}

	public void setOnFinished(Runnable script){
		this.endScript = script;
	}

	public void setInterpolator(Interpolator interpolator){
		this.interpolator = interpolator;
	}

	public void setDelay(int delay){
		this.delay = delay;
	}

	public void sleep(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addListener(AnimatorListener listener){
		listeners.add(listener);
	}

	public void removeListener(AnimatorListener listener){
		listeners.remove(listener);
	}

	private void notifyStart(){
		Platform.runLater(() -> {
			if (!listeners.isEmpty()) {
				listeners.stream().filter(listener -> listener != null).forEach(listener -> listener.OnStart(this));
			}
		});
	}

	private void notifyEnd() {
		if(endScript!=null){
			endScript.run();
		}
		Platform.runLater(() -> {
			if (!listeners.isEmpty()) {
				listeners.stream().filter(listener -> listener != null).forEach(listener -> listener.OnEnd(this));
			}
		});
	}

	@Override
	public void play(){
		if(delay>0){
			TimerFX.runLater(TimePeriod.millis(delay), ()->{
				animate();
			});
		}else{
			animate();
		}
	}

	private void animate(){
		notifyStart();

		timeLine.getKeyFrames().clear();

		KeyFrame keyFrame = new KeyFrame(Duration.millis(1), (e) -> {
			if (startTime < 0) {
                startTime = System.currentTimeMillis();
            }
            long now = System.currentTimeMillis();

            long duration = now - startTime;

            float deltaTime = (float) duration / (float) playTime.getDuration();

			if (node != null) {
				applyTransformation(deltaTime);
			} else if (region != null) {
				interpolate(deltaTime);
			}

            if (duration >= playTime.getDuration()) {
                deltaTime = 1;
               	timeLine.stop();
	            notifyEnd();
            }

		});

		timeLine.getKeyFrames().add(keyFrame);
		timeLine.play();

	}

	@Override
	public void stop(){
		timeLine.stop();
	}


}
