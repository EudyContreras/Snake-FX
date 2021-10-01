package com.EudyContreras.Snake.Utilities;

import java.util.ArrayList;
import java.util.List;

import com.EudyContreras.Snake.ThreadManagers.ThreadManager;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public class ResizeAnimator implements Animator{

	public static final int RESIZE_WIDTH = 0;
	public static final int RESIZE_HEIGHT = 1;
	public static final int RESIZE_WIDTH_HEIGHT = 2;
	private List<AnimatorListener> listeners;
	private Interpolator interpolator;
	private Runnable endScript;
	private Period playTime;
	private Rectangle node;
	private Region region;
    private long startTime = -1;
	private boolean running = true;
	private boolean revert = false;
	private boolean repeat = false;
	private double startWidth;
	private double startHeight;
	private double endWidth;
	private double endHeight;
  	private double newWidth;
    private double newHeight;
	private int resizeAxis;
	private int delay = 0;


	public ResizeAnimator(int axis){
		resizeAxis = axis;
		listeners = new ArrayList<>();
	}

    private void applyTransformation(float delta) {
    	double progress;
        switch (resizeAxis){
            case RESIZE_WIDTH:
                progress = interpolator.interpolate(delta);
                newWidth = startWidth + ((int) Math.round((endWidth - startWidth) * progress));
                Platform.runLater(()->{
                    node.setWidth(newWidth);
				});
                break;
            case RESIZE_HEIGHT:
            	progress = interpolator.interpolate(delta);
                newHeight = startHeight + ((int) Math.round((endHeight - startHeight) * progress));
                Platform.runLater(()->{
                    node.setHeight(newHeight);
				});
                break;
            case RESIZE_WIDTH_HEIGHT:
                progress = interpolator.interpolate(delta);
                newWidth = startWidth + ((int) Math.round((endWidth - startWidth) * progress));
                newHeight = startHeight + ((int) Math.round((endHeight - startHeight) * progress));
                Platform.runLater(()->{
                    node.setWidth(newWidth);
                    node.setHeight(newHeight);
				});
                break;
        }
    }

	protected void interpolate(float delta) {
		double progress;
        switch (resizeAxis){
            case RESIZE_WIDTH:
                progress = interpolator.interpolate(delta);
                newWidth = startWidth + ((int) Math.round((endWidth - startWidth) * progress));
                Platform.runLater(()->{
                    region.setPrefWidth(newWidth);
				});
                break;
            case RESIZE_HEIGHT:
            	progress = interpolator.interpolate(delta);
                newHeight = startHeight + ((int) Math.round((endHeight - startHeight) * progress));
                Platform.runLater(()->{
                    region.setPrefHeight(newHeight);
				});
                break;
            case RESIZE_WIDTH_HEIGHT:
                progress = interpolator.interpolate(delta);
                newWidth = startWidth + ((int) Math.round((endWidth - startWidth) * progress));
                newHeight = startHeight + ((int) Math.round((endHeight - startHeight) * progress));
                Platform.runLater(()->{
                    region.setPrefWidth(newWidth);
                    region.setPrefHeight(newHeight);
				});
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

	public ResizeAnimator setRepeat(boolean state){
		this.repeat = state;
		return this;
	}

	public ResizeAnimator setRevert(boolean state){
		this.revert = state;
		return this;
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
		Platform.runLater(() -> {
			if (!listeners.isEmpty()) {
				listeners.stream().filter(listener -> listener != null).forEach(listener -> listener.OnEnd(this));
			}
		});
	}

	@SuppressWarnings("unused")
	private void notifyRepeat() {
		Platform.runLater(() -> {
			if (!listeners.isEmpty()) {
				listeners.stream().filter(listener -> listener != null).forEach(listener -> listener.OnRepeat(this));
			}
		});
	}
	@Override
	public void play(){
		if(interpolator==null){
			interpolator = Interpolators.getLinearInstance();
		}
		ThreadManager.performeScript(() ->{

			sleep(delay);
			notifyStart();

			while(running){
				  if (startTime < 0) {
	                  startTime = System.currentTimeMillis();
	              }
	              long now = System.currentTimeMillis();
	              long duration = now - startTime;
	              float deltaTime = (float) duration / (float) playTime.getDuration();

	              if(node!=null){
	            	  applyTransformation(deltaTime);
	              }else if(region!=null){
	            	  interpolate(deltaTime);
	              }

	              if (duration >= playTime.getDuration()) {
	                  deltaTime = 1;
	                  running = false;
		              notifyEnd();
	              }

	      		sleep(1);
			}

			if(endScript!=null){
				Platform.runLater(endScript);

			}
		});
	}

	@Override
	public void stop(){
		running = false;
	}


}
