package com.rorkien.snowflakes;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.rorkien.snowflakes.Graphics.Renderer;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static final String name = "Slowfakes";
	public static final int WIDTH = 640, HEIGHT = 480, SCALE = 1;
	public boolean running = true;
	
	public int frames = 0, ticks = 0;
	public int lastTickCount;
	
	public BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public Renderer raster = new Renderer(image);
	public Screen screen;
	
	public static Game instance;	
	
	double grav = 1;
	int dif = 0;
	
	public void init() {
		instance = this;
		screen = new Screen();
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public void stop() {
		running = false;
	}
	
	public void run() {	
		init();
		
		final int frameCap = 60;
		final double nsPerFrame = 1000000000 / frameCap;
		double nextTick = System.nanoTime();
		long fpstimer = System.currentTimeMillis();
		
		while(running) {	
			for(long now = System.nanoTime(); now > nextTick; nextTick += nsPerFrame) {
				this.tick();
				this.render();
			}

			if (System.currentTimeMillis() - fpstimer > 1000) {
				fpstimer += 1000;
				System.out.println("Deviance: " + Math.abs((60D / 100D) * (this.ticks - lastTickCount - frameCap)) + "% (" + (this.ticks - lastTickCount) + "/" + this.frames + ")" );
				lastTickCount = this.ticks;
				this.frames = 0;
			}
			
			long sleepTime = 2;
			long now = System.nanoTime(), diff;
			while((diff = System.nanoTime()-now) < sleepTime) {
				if(diff < sleepTime*0.8) try { Thread.sleep(1); } catch(Exception exc) {}
				else Thread.yield();
			}

		}
	}
	
	public void tick() {
		//long start = System.nanoTime();
		this.ticks++;		
		screen.tick();
		//1 million nanos = 1 ms
		//System.out.println((System.nanoTime() - start) / 1000 / 1000D);
	}
	
	public void render() {
		//long start = System.nanoTime();
		this.frames++;	
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}	
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		screen.render();

		g.dispose();
		bs.show();
		//System.out.println((System.nanoTime() - start) / 1000 / 1000D);
	}	
	
	public static void main(String[] args) {
		Game game = new Game();
		JFrame gameFrame = new JFrame(name);
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		Dimension pos = Toolkit.getDefaultToolkit().getScreenSize();
		gameFrame.setLocation((pos.width / 2) - (size.width / 2), (pos.height / 2) - (size.height / 2));
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.getContentPane().setPreferredSize(size);
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);
		gameFrame.add(game);
		gameFrame.pack();
		gameFrame.setVisible(true);	
		
		game.start();
	}
}
