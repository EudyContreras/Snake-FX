package com.SnakeGame.Utilities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * This is the image loading class. this class takes care of loading images and
 * providing information according to the image dimensions. this class makes it
 * so each image is only loaded once as a check will occur every time an image
 * is loaded in order to make sure there are no duplicates. if an image already
 * located in the HashMap is loaded the image will be discarded and the instance
 * of it located in the HashMap will be returned, this effectively reduces
 * memory consumption and the load time of images when a high amount of images
 * are used in game.
 *
 * @author Eudy Contreras
 */
public class GameImageLoader {

	private static String path;
	private static BufferedImage Image;
	private static int width, height;
	private final static Map<String, BufferedImage> textureMap = new HashMap<String, BufferedImage>();

	public GameImageLoader() {

	}

	/**
	 * constructor which creates a bufferedImage from a given path and will then
	 * store the image inside a hashMap.
	 *
	 * @param path
	 */
	public GameImageLoader(String path) {
		GameImageLoader.path = path;
		BufferedImage oldImage = textureMap.get(path);
		if (oldImage != null) {
			Image = oldImage;
		} else {
			try {
				Image = ImageIO.read(getClass().getResourceAsStream("/com/SnakeGame/Images" + path));
				textureMap.put(path, Image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		width = Image.getWidth();
		height = Image.getHeight();
		Image.flush();
	}

	/**
	 * method which creates a bufferedImage from a given path and returns that
	 * image ready to be used
	 *
	 * @param path
	 */
	public static BufferedImage loadImage(String path) {
		GameImageLoader.path = path;
		BufferedImage oldImage = textureMap.get(path);
		if (oldImage != null) {
			Image = oldImage;
		} else {
			try {
				Image = ImageIO.read(GameImageLoader.class.getResourceAsStream("/com/SnakeGame/Images" + path));
				textureMap.put(path, Image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		width = Image.getWidth();
		height = Image.getHeight();
		return Image;
	}

	public void render(Graphics g, float x, float y) {
		g.drawImage(getImage(), (int) x, (int) y, null);
	}

	public static BufferedImage getImage() {
		return Image;
	}

	public static BufferedImage getLevelImage() {
		return Image;
	}

	public static void clear() {
		textureMap.clear();
	}

	public static void setImage(BufferedImage Image) {
		GameImageLoader.Image = Image;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		GameImageLoader.path = path;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

}
