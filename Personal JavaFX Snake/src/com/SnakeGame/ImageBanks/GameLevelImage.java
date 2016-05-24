package com.SnakeGame.ImageBanks;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.Utilities.ImageUtility;

import javafx.scene.image.Image;

/**
 * Class used to load all level objects meaning all images that will be used in
 * game. The images may be regular images or images which have effects applied
 * to them
 *
 * @author Eudy Contreras
 *
 */

public class GameLevelImage {
	public static boolean firstSectionLoaded = false;
	public static boolean secondSectionLoaded = false;
	public static boolean thirdSectionLoaded = false;
	public static boolean fourthSectionLoaded = false;
	public static boolean fithSectionLoaded = false;


	public static Image desertBackground;
	public static Image horizontalFence;
	public static Image verticalFence;
	public static Image desert_cactus_small;
	public static Image desert_cactus_big;
	public static Image desert_flower;
	public static Image desert_bush;
	public static Image desert_rock;
	public static Image desert_bark;
	public static Image desert_skull;
	public static Image desert_sand;
	public static Image desert_flower_two;

	public static Image jungle_rain;


	public GameLevelImage(){
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Desert Level Images.
		 */
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	desertBackground = ImageUtility.preCreateShadedBackground("desert-level-sand.png",
			Settings.GlOBAL_ILLUMINATION, 1.0, Settings.WIDTH, Settings.HEIGHT);
	horizontalFence = ImageUtility.precreatedLightedImage("horizontal_spike_fence.png", Settings.GlOBAL_ILLUMINATION,
			Settings.SPECULAR_MAP, 180 / GameLoader.ResolutionScaleX, 65 / GameLoader.ResolutionScaleY);
	verticalFence = ImageUtility.precreatedLightedAndShadedImage("vertical_spike_fence.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 55 / GameLoader.ResolutionScaleX,
			170 / GameLoader.ResolutionScaleY);
	desert_rock = ImageUtility.precreatedLightedAndShadedImage("desert_rock.png", Settings.GlOBAL_ILLUMINATION,
			Settings.SPECULAR_MAP, 150 / GameLoader.ResolutionScaleX, 150 / GameLoader.ResolutionScaleY);
	desert_bark = ImageUtility.precreatedLightedAndShadedImageTwo("desert_tree_bark_three.png", Settings.GlOBAL_ILLUMINATION,
			Settings.SPECULAR_MAP, 480 / GameLoader.ResolutionScaleX, 210 / GameLoader.ResolutionScaleY);
	desert_cactus_big = ImageUtility.precreatedLightedAndShadedImage("big_cactus_thick.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 145 / GameLoader.ResolutionScaleX,
			205 / GameLoader.ResolutionScaleY);
	desert_bush = ImageUtility.precreatedLightedAndShadedImage("desert_bush.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 96 / GameLoader.ResolutionScaleX,
			120 / GameLoader.ResolutionScaleY);
	desert_flower_two = ImageUtility.precreatedLightedAndShadedImage("flower_cactus_two.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 96 / GameLoader.ResolutionScaleX,
			120 / GameLoader.ResolutionScaleY);
	desert_flower = ImageUtility.precreatedLightedAndShadedImage("flower_cactus.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 72 / GameLoader.ResolutionScaleX,
			123 / GameLoader.ResolutionScaleY);
	desert_skull = ImageUtility.precreatedLightedAndShadedImage("bison-skull.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 120 / GameLoader.ResolutionScaleX,
			120 / GameLoader.ResolutionScaleY);
	desert_cactus_small = ImageUtility.precreatedLightedAndShadedImage("little_cactus.png",
			Settings.GlOBAL_ILLUMINATION, Settings.SPECULAR_MAP, 88 / GameLoader.ResolutionScaleX,
			92 / GameLoader.ResolutionScaleY);
	desert_sand = ImageUtility.precreatedLightedImage("sandGrain.png",
			Settings.GlOBAL_ILLUMINATION - 0.5, Settings.SPECULAR_MAP - 0.5, 5, 5);


	/**
	 * Jungle level images
	 */

	jungle_rain = new Image(ImageUtility.loadResource("rainDrop.png"),10,10, true, true);
	}


}
