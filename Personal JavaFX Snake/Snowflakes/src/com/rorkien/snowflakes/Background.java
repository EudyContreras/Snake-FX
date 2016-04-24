package com.rorkien.snowflakes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rorkien.snowflakes.Graphics.Image;

public class Background {
	public float offsetx = 0;
	public float offsety = 0;
	public List<Star> stars = new ArrayList<Star>();
	public List<Flakes> flakes = new ArrayList<Flakes>();
	
	public Background() {
		for (int i = 0; i < 200; i ++) stars.add(new Star(RNG.getERNG(Game.WIDTH), RNG.getERNG(Game.HEIGHT), RNG.getRNG(6)));
		for (int i = 0; i < 400; i ++) flakes.add(new Flakes(RNG.getERNG(Game.WIDTH), RNG.getERNG(Game.HEIGHT)));
	}
	
	public class Star {
		public int x, y, type;
		public Star(int x, int y, int type) {
			this.x = x;
			this.y = y;
			this.type = type;
		}
		
		public void render() {
			if (type == 0) {			
				Game.instance.raster.fillSquare(this.x, this.y, 2, 2, 0x4f68cd);//0x8888BB);
				Game.instance.raster.fillSquare(this.x + 2, this.y, 2, 2, 0x263b90);
				Game.instance.raster.fillSquare(this.x - 2, this.y, 2, 2, 0x263b90);
				Game.instance.raster.fillSquare(this.x, this.y + 2, 2, 2, 0x263b90);
				Game.instance.raster.fillSquare(this.x, this.y - 2, 2, 2, 0x263b90);
			}
			else if (type >= 1) {
				Game.instance.raster.fillSquare(this.x, this.y, 1, 1, 0x5485b3);
				Game.instance.raster.fillSquare(this.x + 1, this.y, 1, 1, 0x2b46b3);
				Game.instance.raster.fillSquare(this.x - 1, this.y, 1, 1, 0x2b46b3);
				Game.instance.raster.fillSquare(this.x, this.y + 1, 1, 1, 0x2b46b3);
				Game.instance.raster.fillSquare(this.x, this.y - 1, 1, 1, 0x2b46b3);
			}
		}
	}
	
	public class Flakes {
		public float x, y;
		public float gravity = 0.2F;
		public int randomMotion = RNG.getRNG(1,8);
		public Flakes(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void tick() {
			this.y += gravity;
			if (Game.instance.ticks % randomMotion == 0) this.x += Math.sin((Game.instance.ticks + (randomMotion * 10)) / 20) / 4;
		}
		
		public void render() {
			Game.instance.raster.fillSquare((int)this.x, (int)this.y, 2, 2, 0xDDDDDD);
		}
	}

	public void tick() {
		if (RNG.getRNG(100) > 85) flakes.add(new Flakes(RNG.getERNG(Game.WIDTH), -5));
		
		//long start = System.nanoTime();
		for (Flakes f : flakes) f.tick();
		//System.out.println("Ran through " + flakes.size() + " objects. Time: " + (System.nanoTime() - start) / 1000 / 1000D + "ms");

		for (int i = 0; i < flakes.size(); i++) if (flakes.get(i).y > Game.HEIGHT) flakes.remove(i);
	}
	
	public void render() {
		Arrays.fill(Game.instance.raster.pixels, 0x1b2632);
		for (Star s : stars) s.render();
		for (Flakes f : flakes) f.render();
		
		Image.background.draw((int)offsetx % Game.WIDTH, Game.HEIGHT - Image.background.height + 64);
		Image.background.draw(-Image.background.width + ((int)offsetx % Game.WIDTH), Game.HEIGHT - Image.background.height + 64);
		Image.background.draw(Image.background.width + ((int)offsetx % Game.WIDTH), Game.HEIGHT - Image.background.height + 64);
		
		Image.moon.draw(-Image.moon.width + (560 + (int)(offsetx / 100)) % (Game.WIDTH + Image.moon.width), 30);
	}
	
}
