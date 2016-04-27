package application;

import javafx.scene.image.Image;

public class GameLevelImage {
	public static int LEVEL = 1;
	public static boolean firstSectionLoaded = false;
	public static boolean secondSectionLoaded = false;
	public static boolean thirdSectionLoaded = false;
	public static boolean fourthSectionLoaded = false;
	public static boolean fithSectionLoaded = false;
    final static Image tile2 = ImageUtility.preCreateShadedImages("Tile (15).png",1.5,1.8,256,256);  
    final static Image tile3 = ImageUtility.preCreateShadedImages("Box.png",1.5,1.8,200,200);  
    final static Image tile4 = ImageUtility.preCreateShadedImages("Tile (5).png",1.0,1.5,256,256);  
    final static Image tile5 = ImageUtility.preCreateShadedImages("Barrel (1).png",1.5,1.8,100,128);  

}
