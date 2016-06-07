package com.EudyContreras.Snake.ImageBanks;

import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.SnakeGame.Utilities.ImageEffectUtility;

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
	public static Image desert_trap;
	public static Image desert_cactus_small;
	public static Image desert_cactus_big;
	public static Image desert_cactus_big_alt;
	public static Image desert_flower;
	public static Image desert_flower_alt;
	public static Image desert_bush;
	public static Image desert_bones;
	public static Image desert_bones_alt;
	public static Image desert_rock;
	public static Image desert_rock_alt;
	public static Image desert_bark;
	public static Image desert_skull;
	public static Image desert_bones_bridge;
	public static Image desert_sand;
	public static Image desert_flower_two;
	public static Image jungle_rain;


	public GameLevelImage(){
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Desert Level Images.
		 */
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	desertBackground = ImageEffectUtility.preCreateShadedBackground("desert-level-sand-three.png",
			GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
	horizontalFence = ImageEffectUtility.precreatedLightedImage("horizontal_spike_fence.png", GameSettings.GlOBAL_ILLUMINATION,
			GameSettings.SPECULAR_MAP, 180 / GameLoader.ResolutionScaleX, 65 / GameLoader.ResolutionScaleY);
	verticalFence = ImageEffectUtility.precreatedLightedAndShadedImage("vertical_spike_fence.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 55 / GameLoader.ResolutionScaleX,
			170 / GameLoader.ResolutionScaleY);
	desert_rock = ImageEffectUtility.precreatedLightedAndShadedImage("desert_rock.png", GameSettings.GlOBAL_ILLUMINATION,
			GameSettings.SPECULAR_MAP, 150 / GameLoader.ResolutionScaleX, 150 / GameLoader.ResolutionScaleY);
	desert_rock_alt = ImageEffectUtility.precreatedLightedAndShadedImage("desert_rock_alt.png", GameSettings.GlOBAL_ILLUMINATION,
			GameSettings.SPECULAR_MAP, 150 / GameLoader.ResolutionScaleX, 150 / GameLoader.ResolutionScaleY);
	desert_bark = ImageEffectUtility.precreatedLightedAndShadedImageTwo("desert_tree_bark_three.png", GameSettings.GlOBAL_ILLUMINATION,
			GameSettings.SPECULAR_MAP, 480 / GameLoader.ResolutionScaleX, 210 / GameLoader.ResolutionScaleY);
	desert_cactus_big = ImageEffectUtility.precreatedLightedAndShadedImage("big_cactus_thick.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 145 / GameLoader.ResolutionScaleX,
			205 / GameLoader.ResolutionScaleY);
	desert_cactus_big_alt = ImageEffectUtility.precreatedLightedAndShadedImage("big_cactus_thick_alt.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 145 / GameLoader.ResolutionScaleX,
			205 / GameLoader.ResolutionScaleY);
	desert_bush = ImageEffectUtility.precreatedLightedAndShadedImage("desert_bush.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 96 / GameLoader.ResolutionScaleX,
			120 / GameLoader.ResolutionScaleY);
	desert_flower_two = ImageEffectUtility.precreatedLightedAndShadedImage("flower_cactus_two.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 96 / GameLoader.ResolutionScaleX,
			120 / GameLoader.ResolutionScaleY);
	desert_flower = ImageEffectUtility.precreatedLightedAndShadedImage("flower_cactus.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 72 / GameLoader.ResolutionScaleX,
			123 / GameLoader.ResolutionScaleY);
	desert_flower_alt = ImageEffectUtility.precreatedLightedAndShadedImage("flower_cactus_alt.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 72 / GameLoader.ResolutionScaleX,
			123 / GameLoader.ResolutionScaleY);
	desert_skull = ImageEffectUtility.precreatedLightedAndShadedImage("desert_bison_skull.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 140 / GameLoader.ResolutionScaleX,
			105 / GameLoader.ResolutionScaleY);
	desert_bones = ImageEffectUtility.precreatedLightedAndShadedImage("desert_bones.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 140 / GameLoader.ResolutionScaleX,
			105 / GameLoader.ResolutionScaleY);
	desert_bones_alt = ImageEffectUtility.precreatedLightedAndShadedImage("desert_bones_alt.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 200 / GameLoader.ResolutionScaleX,
			95 / GameLoader.ResolutionScaleY);
	desert_bones_bridge = ImageEffectUtility.precreatedLightedAndShadedImage("desert_bones_bridge.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 200 / GameLoader.ResolutionScaleX,
			95 / GameLoader.ResolutionScaleY);
	desert_cactus_small = ImageEffectUtility.precreatedLightedAndShadedImage("little_cactus.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 95 / GameLoader.ResolutionScaleX,
			92 / GameLoader.ResolutionScaleY);
	desert_trap = ImageEffectUtility.precreatedLightedAndShadedImage("desert_trap.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.SPECULAR_MAP, 130 / GameLoader.ResolutionScaleX,
			75 / GameLoader.ResolutionScaleY);
	desert_sand = ImageEffectUtility.precreatedLightedImage("sand_grain.png",
			GameSettings.GlOBAL_ILLUMINATION - 0.5, GameSettings.SPECULAR_MAP - 1.2, 5, 5);


	/**
	 * Jungle level images
	 */

	jungle_rain = new Image(ImageEffectUtility.loadResource("rainDrop.png"),10,10, true, true);
	}


}
