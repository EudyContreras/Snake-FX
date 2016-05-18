package com.SnakeGame.ImageBanks;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.Utilities.ImageUtility;

import javafx.scene.image.Image;

/**
 * Class used to load all level objects meaning all images that will be used in
 * game. The images may be regular images or images which have effects applyed
 * to them
 *
 * @author Eudy Contreras
 *
 */

public class GameLevelImage {
	public static int LEVEL = 1;
	public static boolean firstSectionLoaded = false;
	public static boolean secondSectionLoaded = false;
	public static boolean thirdSectionLoaded = false;
	public static boolean fourthSectionLoaded = false;
	public static boolean fithSectionLoaded = false;

	/**
	 * d no shading
	 */
	// final static Image horizontalFence = new
	// Image("fence.png",120,50,false,true);
	// final static Image verticalFence = new
	// Image("fence2.png",50,120,false,true);
	// final static Image rock = new Image("rock.png",150,150,true,true);
	// final static Image sandyRock = new
	// Image("Sandstone_rock.png",100,150,true,true);
	// final static Image cactus = new Image("cactus-object2.png");
	// final static Image littleCactus = new
	// Image("little-cactus3.png",50,50,false,true);
	// final static Image skeleton = new
	// Image("Skeleton2.png",100,100,true,true);

	/**
	 * shaded images with added depth
	 */

	// final static Image horizontalFence =
	// ImageUtility.preCreateShadedImages("fence.png",1.9,1.3,120/GameLoader.ResolutionScaleX,50/GameLoader.ResolutionScaleY);
	// final static Image verticalFence =
	// ImageUtility.preCreateShadedImages("fence2.png",1.9,1.3,50/GameLoader.ResolutionScaleX,120/GameLoader.ResolutionScaleY);
	// final static Image smallRock =
	// ImageUtility.preCreateShadedImages("small-rock.png",1.9,1.3,30/GameLoader.ResolutionScaleX,30/GameLoader.ResolutionScaleY);
	// final static Image rock =
	// ImageUtility.preCreateShadedImages("rock.png",1.9,1.3,150/GameLoader.ResolutionScaleX,150/GameLoader.ResolutionScaleY);
	// final static Image bark =
	// ImageUtility.preCreateShadedImages("bark.png",1.9,1.3,350/GameLoader.ResolutionScaleX,200/GameLoader.ResolutionScaleY);
	// final static Image cactus =
	// ImageUtility.preCreateShadedImages("cactus-object3.png",1.9,1.3,150/GameLoader.ResolutionScaleX,150/GameLoader.ResolutionScaleY);
	// final static Image flower =
	// ImageUtility.preCreateShadedImages("little-cactus-flower.png",1.9,1.3,70/GameLoader.ResolutionScaleX,70/GameLoader.ResolutionScaleY);
	// final static Image skeleton =
	// ImageUtility.preCreateShadedImages("Skeleton2.png",1.9,1.3,120/GameLoader.ResolutionScaleX,120/GameLoader.ResolutionScaleY);
	// final static Image littleCactus =
	// ImageUtility.preCreateShadedImages("little-cactus3.png",1.9,1.3,63/GameLoader.ResolutionScaleX,49/GameLoader.ResolutionScaleY);
	//
	public final static Image desertBackground = ImageUtility.preCreateShadedBackground("desert-sand10.png",
			Settings.GlOBAL_ILLUMINATION - 0.1, 0.0, Settings.WIDTH, Settings.HEIGHT);// new
																						// Image("desert-sand.jpg",
																						// Settings.WIDTH,
																						// Settings.HEIGHT,
																						// false,
																						// true);
	public final static Image horizontalFence = ImageUtility.precreatedLightedImage("fence.png", Settings.GlOBAL_ILLUMINATION,
			Settings.SPECULAR_MAP, 120 / GameLoader.ResolutionScaleX, 50 / GameLoader.ResolutionScaleY);
	public final static Image verticalFence = ImageUtility.precreatedLightedAndShadedImage("fence2.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 50 / GameLoader.ResolutionScaleX,
			120 / GameLoader.ResolutionScaleY);
	// final static Image smallRock =
	// ImageUtility.preCreateShadedImages("small-rock.png",Settings.GlOBAL_ILLUMINATION,Settings.SPECULAR_MAP,30/GameLoader.ResolutionScaleX,30/GameLoader.ResolutionScaleY);
	public final static Image rock = ImageUtility.precreatedLightedAndShadedImage("rock2.png", Settings.GlOBAL_ILLUMINATION,
			Settings.SPECULAR_MAP, 150 / GameLoader.ResolutionScaleX, 150 / GameLoader.ResolutionScaleY);
	public final static Image bark = ImageUtility.precreatedLightedAndShadedImage("bark.png", Settings.GlOBAL_ILLUMINATION,
			Settings.SPECULAR_MAP - 0.4, 400 / GameLoader.ResolutionScaleX, 220 / GameLoader.ResolutionScaleY);
	public final static Image cactus = ImageUtility.precreatedLightedAndShadedImage("big_cactus_thick2.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 130 / GameLoader.ResolutionScaleX,
			190 / GameLoader.ResolutionScaleY);
	public final static Image flower = ImageUtility.precreatedLightedAndShadedImage("desert_bush.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 96 / GameLoader.ResolutionScaleX,
			120 / GameLoader.ResolutionScaleY);
	public final static Image flower2 = ImageUtility.precreatedLightedAndShadedImage("flower_cactus.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 72 / GameLoader.ResolutionScaleX,
			123 / GameLoader.ResolutionScaleY);
	public final static Image skeleton = ImageUtility.precreatedLightedAndShadedImage("bison-skull.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 120 / GameLoader.ResolutionScaleX,
			120 / GameLoader.ResolutionScaleY);
	public final static Image littleCactus = ImageUtility.precreatedLightedAndShadedImage("little_cactus.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 88 / GameLoader.ResolutionScaleX,
			92 / GameLoader.ResolutionScaleY);
	public final static Image sandGrain = ImageUtility.precreatedLightedImage("sandGrain.png",
			Settings.GlOBAL_ILLUMINATION - 0.5, Settings.SPECULAR_MAP - 0.5, 5, 5);
	// final static Image spider = ImageUtility.preCreateShadedDebris(path,
	// diffused, specularMap, width, height)
}
