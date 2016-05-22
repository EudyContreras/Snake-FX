package com.SnakeGame.FrameWork;

/**
 * Thread used for updating non graphical elements or any other elements
 * that do not involve x and y translations within nodes.
 * @author Eudy Contreras
 *
 */
public class LogicThread extends Thread {

	private SnakeGame game;
	private Boolean isRunning;
	private Thread mainThread;
	private Thread helperThread;

	public LogicThread(SnakeGame game){
		this.game = game;
	}
	public synchronized void startMainThread() {
		if (isRunning)
			return;
		isRunning = true;
		mainThread = new Thread(this);
		mainThread.start();
	}

	public void stopMainThread() {
		if (!isRunning)
			return;
		isRunning = false;
	}
	public synchronized void startHelperThread() {
		if (isRunning)
			return;
		isRunning = true;
		helperThread = new Thread(new HelperThread());
		helperThread.start();
	}

	public void stopHelperThread() {
		if (!isRunning)
			return;
		isRunning = false;
	}
	public void run() {
		long lastTime = System.nanoTime();
		long startTime = System.currentTimeMillis();
		long cummulativeTime = startTime;
		double amountOfUpadates = 60.0;
		double ns = 1000000000 / amountOfUpadates;
		double delta1 = 0;
		double delta2 = 0;
		double delta3 = 0;
		double delta4 = 0;
		double delta5 = 0;
		double delta6 = 0;
		long timer = System.currentTimeMillis();
		while (isRunning) {
			long now = System.nanoTime();
			if (Settings.RENDER_GAME) {
				long timePassed = System.currentTimeMillis() - cummulativeTime;
				cummulativeTime += timePassed;
				delta1 += (now - lastTime) / ns / 100;
				delta2 += (now - lastTime) / ns / 6;
				delta3 += (now - lastTime) / ns / 2;
				delta4 += (now - lastTime) / ns;
				delta5 += (now - lastTime) / ns * 2;
				delta6 += (now - lastTime) / ns * 4;

				while (delta1 >= 1) {
					updateAt1();
					delta1--;
				}

				while (delta2 >= 1) {
					updateAt10();
					delta2--;
				}

				while (delta3 >= 1) {
					updateAt30();
					delta3--;
				}

				while (delta4 >= 1) {
					updateAt60();
					delta4--;
				}

				while (delta5 >= 1) {
					updateAt120();
					delta5--;
				}

				while (delta6 >= 1) {
					updateAt240();
					delta6--;
				}
			}
			lastTime = now;
			if (System.currentTimeMillis() - timer > 250) {
				timer += 1000;
				lastTime = System.nanoTime();
			}
		}
	}

	public class HelperThread extends Thread {

		public void run() {
			long lastTime = System.nanoTime();
			long startTime = System.currentTimeMillis();
			long cummulativeTime = startTime;
			double amountOfUpadates = 60.0;
			double ns = 1000000000 / amountOfUpadates;
			double delta1 = 0;
			double delta2 = 0;
			double delta3 = 0;
			double delta4 = 0;
			double delta5 = 0;
			double delta6 = 0;
			long timer = System.currentTimeMillis();
			while (isRunning) {
				long now = System.nanoTime();
				if (Settings.RENDER_GAME) {
					long timePassed = System.currentTimeMillis() - cummulativeTime;
					cummulativeTime += timePassed;
					updateAnimation(timePassed);
					delta1 += (now - lastTime) / ns / 100;
					while (delta1 >= 1) {
						updateAt1();
						delta1--;
					}
					delta2 += (now - lastTime) / ns / 6;
					while (delta2 >= 1) {
						updateAt10();
						delta2--;
					}
					delta3 += (now - lastTime) / ns / 2;
					while (delta3 >= 1) {
						updateAt30();
						delta3--;
					}
					delta4 += (now - lastTime) / ns;
					while (delta4 >= 1) {
						updateAt60();
						delta4--;
					}
					delta5 += (now - lastTime) / ns * 2;
					while (delta5 >= 1) {
						updateAt120();
						delta5--;
					}
					delta6 += (now - lastTime) / ns * 4;
					while (delta6 >= 1) {
						updateAt240();
						delta6--;
					}
				}
				lastTime = now;
				if (System.currentTimeMillis() - timer > 250) {
					timer += 1000;
					lastTime = System.nanoTime();
				}
			}
		}
	}
	protected void updateAt1() {
	}

	protected void updateAt10() {
	}

	protected void updateAt30() {
	}

	protected void updateAt60() {
		game.drawOverlay(null);
	}

	protected void updateAt120() {
	}

	protected void updateAt240() {
	}

	protected void updateAnimation(long timePassed) {
	}

}
