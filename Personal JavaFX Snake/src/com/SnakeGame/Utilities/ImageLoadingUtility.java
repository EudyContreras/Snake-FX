package com.SnakeGame.Utilities;

import java.util.HashMap;
import java.util.Map;

import com.EudyContreras.Snake.FrameWork.GameSettings;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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
public class ImageLoadingUtility {

	private static Image image;
	private static String name;
	private static WritableImage wImage;
	private static PixelReader reader;
	private static PixelWriter writer;
	private static int width, height;
	private final static Map<String, Image> textureMap = new HashMap<String, Image>();

	public ImageLoadingUtility() {

	}

	/**
	 * constructor which creates a bufferedImage from a given name and will then
	 * store the image inside a hashMap.
	 *
	 * @param name
	 */
	public ImageLoadingUtility(String name) {
		ImageLoadingUtility.name = name;
		Image oldImage = textureMap.get(name);
		if (oldImage != null) {
			image = oldImage;
		} else {

			image = new Image(loadResource(name));
			textureMap.put(name, image);

		}
		width = (int)image.getWidth();
		height = (int)image.getHeight();
	}

	/**
	 * method which creates a bufferedImage from a given name and returns that
	 * image ready to be used
	 *
	 * @param name
	 */
	public static WritableImage loadImage(String name) {
		ImageLoadingUtility.name = name;
		Image oldImage = textureMap.get(name);
		if (oldImage != null) {
			image = oldImage;
		} else {

			image = new Image(loadResource(name));
			textureMap.put(name, image);

		}
		reader = image.getPixelReader();

		width = (int) image.getWidth();
		height = (int) image.getHeight();

		wImage = new WritableImage(width, height);
		writer = wImage.getPixelWriter();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color color = reader.getColor(x, y);
				writer.setColor(x, y, color);
			}
		}
		return wImage;
	}

	public void render(GraphicsContext gc, float positionX, float positionY) {
		gc.drawImage(image, positionX, positionY);
	}

	public static String loadResource(String image) {
		String url = GameSettings.IMAGE_SOURCE_DIRECTORY + image;
		return url;
	}
	public static Image getImage(){
		return image;
	}
	public static WritableImage getWImage() {
		return wImage;
	}

	public static WritableImage getLevelWImage() {
		return wImage;
	}

	public static void clear() {
		textureMap.clear();
	}

	public static void setWImage(WritableImage Image) {
		ImageLoadingUtility.wImage = Image;
	}

	public static String getPath() {
		return name;
	}

	public static void setPath(String name) {
		ImageLoadingUtility.name = name;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

}
