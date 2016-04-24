package com.rorkien.snowflakes.Graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Renderer {
	public int width, height;
	public int[] pixels;	
		
	public Renderer(BufferedImage b) {
		this.height = b.getHeight();
		this.width = b.getWidth();
		this.pixels = ((DataBufferInt) b.getRaster().getDataBuffer()).getData();
	}
	
	public static int getHSL(float h, float s, float l) {		
		h = h % 360.0f;
		h /= 360f;
		s /= 100f;
		l /= 100f;

		float q = 0;

		if (l < 0.5) q = l * (1 + s);
		else q = (l + s) - (s * l);

		float p = 2 * l - q;
		float r = Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f)));
		float g = Math.max(0, HueToRGB(p, q, h));
		float b = Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f)));

		r = Math.min(r, 1.0f);
		g = Math.min(g, 1.0f);
		b = Math.min(b, 1.0f);
		
		return ((int)(r * 255) << 16) + ((int)(g * 255) << 8) + (int)(b * 255);
	}

	private static float HueToRGB(float p, float q, float h) {
		if (h < 0) h += 1;
		if (h > 1 ) h -= 1;

		if (6 * h < 1) return p + ((q - p) * 6 * h);
		if (2 * h < 1) return q;
		if (3 * h < 2) return p + ( (q - p) * 6 * ((2.0f / 3.0f) - h) );
   		return p;
	}
	
	public void fillSquare(int xpos, int ypos, int w, int h, int color) {
		for (int x = xpos; x < xpos + w; x++) {
			if (x < 0 || x >= width) continue;	
			for (int y = ypos; y < ypos + h; y++) {
				if (y < 0 || y >= height) continue;	
				pixels[x + y * width] = color;
			}
		}
	}
	
	public void fillRoundedSquare(int xpos, int ypos, int w, int h, int color, double radius) {
		for (int x = xpos; x < xpos + w; x++) {
			if (x < 0 || x >= width) continue;	
			for (int y = ypos; y < ypos + h; y++) {
				if (y < 0 || y >= height) continue;	
				if (y - ypos + 1 > radius / (x - xpos + 1) && h - y - ypos + ypos * 2 > radius / (x - xpos + 1) && y - ypos + 1 > radius / (w - x - xpos + xpos * 2) && h - y - ypos + ypos * 2 > radius / (w - x - xpos + xpos * 2)) {
					pixels[x + y * width] = color;
				}
			}
		}
	}
	
	public void fillTiltedSquare(int xpos, int ypos, int w, int h, int color, int xtilt, int ytilt) {
		int xpix, ypix;		
		for (int x = xpos; x < xpos + w; x++) {
			for (int y = ypos; y < ypos + h; y++) {
				ypix = (x - xpos) * ytilt;
				xpix = (y - ypos) * xtilt;
				if ((x + xpix < 0 || x + xpix >= width) || (y + ypix < 0 || y + ypix >= height)) continue;	
				pixels[(x + xpix) + (y + ypix) * width] = color;
			}
		}
	}
	
	public void fillOutline(int xpos, int ypos, int w, int h, int outline, int color) {
		for (int x = xpos; x < xpos + w; x++) {
			if (x < 0 || x >= width) continue;	
			for (int y = ypos; y < ypos + h; y++) {
				if (y < 0 || y >= height) continue;	
				if ((x - xpos < outline || y - ypos < outline) || (x - xpos >= w - outline || y - ypos >= h - outline))
					pixels[x + y * width] = color;
			}
		}
	}
	
	public void drawBox(int xpos, int ypos, int w, int h, int color, int outline, int outcolor, boolean boxpriority) {
		fillSquare(xpos, ypos, w, h, color);
		if (boxpriority) fillOutline(xpos - outline, ypos - outline, w + outline * 2, h + outline * 2, outline, outcolor);
		else fillOutline(xpos, ypos, w, h, outline, outcolor);
	}
	
	public void drawColumn(int xpos, int ypos, int w, int h, int p, int color1, int color2, int color3) {
		fillSquare(xpos, ypos, w, h, color1);
		fillTiltedSquare(xpos - p, ypos - p, p, h, color2, 0, 1);
		fillTiltedSquare(xpos - p, ypos - p, w, p, color3, 1, 0);	
	}
	
	public void darkenTo(float value) {
		float red, green, blue;

		for (int i = 0; i < this.pixels.length; i++) {
			if (value > 1.0F) this.pixels[i] = 0;

			red = ((this.pixels[i] & 0xFF0000) >> 16) * value;
			if (red < 0) red = 0;
			green = ((this.pixels[i] & 0xFF00) >> 8) * value;
			if (green < 0) green = 0;
			blue = (this.pixels[i] & 0xFF) * value;
			if (blue < 0) blue = 0;
			
			this.pixels[i] = ((int)red << 16) + ((int)green << 8) + (int)blue;
		}
	}
}
