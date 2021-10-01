package com.EudyContreras.Snake.Utilities;

import com.EudyContreras.Snake.ThreadManagers.ThreadManager;

import javafx.application.Platform;

public class AnimationTimer {

	public static void runLater(Period period, Runnable script){
		ThreadManager.performeScript(()->{
			waitTime(period.getDuration());
			Platform.runLater(script);
		});
	}

	private static void waitTime(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
