package com.EudyContreras.Snake.Utilities;

import com.EudyContreras.Snake.ThreadManagers.ThreadManager;

import javafx.application.Platform;

public class ValueAnimator {

	private Runnable endScript;
	private ValueWrapper update;
	private boolean revert = false;
	private boolean repeat = false;
	private double valueInterpolator;
	private double fromValue;
	private double toValue;
	private double value;
	private double temp;
	private int duration;
	private int current;
	private int counter = 1;


	public ValueAnimator(int duration, double from, double to, ValueWrapper update){
		this.update = update;
		this.duration = duration;
		this.fromValue = from;
		this.toValue = to;
		this.value = from;
		calculateValue();
	}

	public ValueAnimator(int duration, double from, double to){
		this.duration = duration;
		this.fromValue = from;
		this.toValue = to;
		this.value = from;
		calculateValue();
	}

	public ValueAnimator addUpdate(ValueWrapper update){
		this.update = update;
		return this;
	}

	private void calculateValue(){
		double margin = (toValue-fromValue);
		valueInterpolator = margin/duration;
		value = fromValue;
		current = 0;
	}

	private void calculateValue(double from, double to){
		double margin = (to-from);
		valueInterpolator = margin/duration;
		value = fromValue;
		current = 0;
	}

	public void setOnFinished(Runnable script){
		this.endScript = script;
	}

	public ValueAnimator setRepeat(boolean state){
		this.repeat = state;
		return this;
	}

	public ValueAnimator setRevert(boolean state){
		this.revert = state;
		return this;
	}

	public void play(){
		final int toNewValue = 22;
		ThreadManager.performeScript(new Runnable() {
			@Override
			public void run() {
				long lastTime = System.nanoTime();
				double updateCount = 1000.0;
				double nanoSeconds = 1000000000 / updateCount;
				double deltaTime = 0;
				long timer = System.currentTimeMillis();

				while (true) {
					long now = System.nanoTime();
					deltaTime += (now - lastTime) / nanoSeconds;
					lastTime = now;
					if (deltaTime >= 1) {
						Platform.runLater(()->{
								update.onUpdate(value);
								current+=1;
								value += valueInterpolator;
						});
						deltaTime--;
					}if (System.currentTimeMillis() - timer >= duration) {
						break;
					}

				}
				if(revert ^ repeat){
					if(repeat){
						calculateValue(fromValue,toValue);
						play();
					}else{
						calculateValue(value,fromValue);
						revert = false;
						play();
					}
				}else{
					if(endScript!=null){
						Platform.runLater(endScript);
					}
				}
			}
		});
	}

}
