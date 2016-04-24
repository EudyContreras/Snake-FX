package com.rorkien.snowflakes.Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.rorkien.snowflakes.Game;

public class Image {
	public int width, height;
	public int[] pixels;
	
	public static Image background = loadScale("/back.png", 2);	
	public static Image moon = loadScale("/moon.png", 3);
	
	public Image(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}
	
	public void draw(int xpos, int ypos) {
		for (int y = 0; y < height; y++) {
			int yPix = y + ypos;
			if (yPix < 0 || yPix >= Game.instance.raster.height) continue;

			for (int x = 0; x < width; x++) {
				int xPix = x + xpos;
				if (xPix < 0 || xPix >= Game.instance.raster.width) continue;	

				int src = pixels[x + y * width];
				if (src < 0) Game.instance.raster.pixels[xPix + yPix * Game.instance.raster.width] = src;	
			}
		}
	}
	
	public void subDraw(int xpos, int ypos, int[] srccol, int[] subcol) {
		for (int y = 0; y < height; y++) {
			int yPix = y + ypos;
			if (yPix < 0 || yPix >= Game.instance.raster.height) continue;

			for (int x = 0; x < width; x++) {
				int xPix = x + xpos;
				if (xPix < 0 || xPix >= Game.instance.raster.width) continue;	

				int src = pixels[x + y * width];
				if (src < 0) {
					for (int i = 0; i < srccol.length; i++) {
						if (src + 0x1000000 == srccol[i]) src = subcol[i];
					}
					Game.instance.raster.pixels[xPix + yPix * Game.instance.raster.width] = src;												
				}
			}
		}
	}

	public static Image load(String filename) {
		BufferedImage img;
		
		try {
			img = ImageIO.read(Image.class.getResource(filename));
		}
		catch (IOException e) {
			throw new RuntimeException("Loading " + filename + " image failed");
		}

		int sw = img.getWidth();
		int sh = img.getHeight();
		
		Image image = new Image(sw, sh);
		img.getRGB(0, 0, sw, sh, image.pixels, 0, sw);

		return image;
	}
	
	public static Image loadScale(String filename, int scale) {
		Image img = load(filename);		
		return scale(img, scale);
	}	
	
	public static Image[][] load(String filename, int slicewidth, int sliceheight) {
		BufferedImage img;
		try { img = ImageIO.read(Image.class.getResource(filename)); }
		catch (IOException e) { throw new RuntimeException("Loading " + filename + " image failed"); }

		int xSlices = img.getWidth() / slicewidth;
		int ySlices = img.getHeight() / sliceheight;

		Image image[][] = new Image[xSlices][ySlices];
		for (int x = 0; x < xSlices; x++) {
			for (int y = 0; y < ySlices; y++) {
				image[x][y] = new Image(slicewidth, sliceheight);
				img.getRGB(x * slicewidth, y * sliceheight, slicewidth, sliceheight, image[x][y].pixels, 0, slicewidth);
			}
		}
		return image;
	}
	
	public static Image[][] loadScale(String filename, int slicewidth, int sliceheight, int scale) {
		Image[][] img = load(filename, slicewidth, sliceheight);

		for (int y = 0; y < img.length; y++) {
			for (int x = 0; x < img[0].length; x++) {
				img[y][x] = scale(img[y][x], scale);
			}
		}
		
		return img;
	}
	
	public static Image scale(Image i, int scale) {
		Image img = new Image(i.width * scale, i.height * scale);
		
		for (int y = 0; y < img.height; y++) {	
			for (int x = 0; x < img.width; x++) {
				int src = i.pixels[(x / scale) + (y / scale) * i.width];
				if (src < 0) img.pixels[x + y * img.width] = src;
			}
		}
		
		return img;
	}
}
