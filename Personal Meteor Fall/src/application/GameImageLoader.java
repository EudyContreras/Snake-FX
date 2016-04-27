package application;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


public class GameImageLoader {

	private static String path;
	private static BufferedImage Image;
	private static int width, height;
	private final static Map<String, BufferedImage> textureMap = new HashMap<String, BufferedImage>();
	
	public GameImageLoader(){
		
	}
	public GameImageLoader(String path){
		GameImageLoader.path = path;
		BufferedImage oldTexture = textureMap.get(path);
		if(oldTexture!= null){
			Image = oldTexture;
		}else{
		try {
			Image = ImageIO.read(GameImageLoader.class.getResourceAsStream(path));
			textureMap.put(path, Image);				
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		width = Image.getWidth();
		height = Image.getHeight();
		Image.flush();
	}
	public static BufferedImage loadImage(String path){
		GameImageLoader.path = path;
		BufferedImage oldTexture = textureMap.get(path);
		if(oldTexture!= null){
			Image = oldTexture;
		}else{
		try {
			Image = ImageIO.read(GameImageLoader.class.getResourceAsStream(path));
			textureMap.put(path, Image);				
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		width = Image.getWidth();
		height = Image.getHeight();
		return Image;
	}
	public static BufferedImage loadImage(BufferedImage oldTexture){
		oldTexture = textureMap.get(path);
		if(oldTexture!= null){
			Image = oldTexture;
		}else{
		try {
			Image = ImageIO.read(GameImageLoader.class.getResourceAsStream(path));
			textureMap.put(path, Image);				
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		width = Image.getWidth();
		height = Image.getHeight();
		return Image;
	}
	public void render(Graphics g, float x, float y){
		g.drawImage(getImage(),(int)x, (int)y,null);
	}
	public static BufferedImage getImage(){
		return Image;
	}
	public static BufferedImage getLevelImage() {
		return Image;
	}
	public static void clear(){
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
