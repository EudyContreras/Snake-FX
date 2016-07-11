package com.EudyContreras.Snake.ImageBanks;

import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Utilities.ImageEffectUtility;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Class used to load all level objects meaning all images that will be used in
 * game. The images may be regular images or images which have effects applied
 * to them
 *
 * @author Eudy Contreras
 *
 */

public class GameLevelImage {
	public static final boolean firstSectionLoaded = false;
	public static final boolean secondSectionLoaded = false;
	public static final boolean thirdSectionLoaded = false;
	public static final boolean fourthSectionLoaded = false;
	public static final boolean fithSectionLoaded = false;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Classic Level Images.
	 */
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 public static final Image classicBackground = ImageEffectUtility.precreateBackground(Color.BLACK, GameSettings.WIDTH, GameSettings.HEIGHT);

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Desert Level Images.
	 */
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////


		public static final Image desertBackgroundOne = ImageEffectUtility.preCreateShadedBackground("desert_level_background_1.png",
			GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		public static final Image desertBackgroundTwo = ImageEffectUtility.preCreateShadedBackground("desert_level_background_2.png",
			GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		public static final Image desertBackgroundThree = ImageEffectUtility.preCreateShadedBackground("desert_level_background_3.png",
			GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		public static final Image desertBackgroundFour = ImageEffectUtility.preCreateShadedBackground("desert_level_background_4.png",
			GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		public static final Image desertBackgroundFive = ImageEffectUtility.preCreateShadedBackground("desert_level_background_5.png",
			GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		public static final Image desertBackgroundSix = ImageEffectUtility.preCreateShadedBackground("desert_level_background_6.png",
				GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		public static final Image desertBackgroundSeven = ImageEffectUtility.preCreateShadedBackground("desert_level_background_7.png",
				GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		public static final Image desertBackgroundEight = ImageEffectUtility.preCreateShadedBackground("desert_level_background_8.png",
				GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		public static final Image desertBackgroundNine = ImageEffectUtility.preCreateShadedBackground("desert_level_background_9.png",
				GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		public static final Image desertBackgroundTen = ImageEffectUtility.preCreateShadedBackground("desert_level_background_10.png",
				GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		public static final Image desertBackgroundEleven = ImageEffectUtility.preCreateShadedBackground("desert_level_background_11.png",
				GameSettings.GlOBAL_ILLUMINATION, 0, GameSettings.WIDTH, GameSettings.HEIGHT);


		public static final Image horizontalFence = ImageEffectUtility.precreatedLightedImage("horizontal_spike_fence_alt.png", GameSettings.GlOBAL_ILLUMINATION,
			GameSettings.GLOBAL_SPECULARITY, 180 / GameLoader.ResolutionScaleX, 65 / GameLoader.ResolutionScaleY);
		public static final Image verticalFence = ImageEffectUtility.precreatedLightedAndShadedImage("vertical_spike_fence_alt.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 55 / GameLoader.ResolutionScaleX,170 / GameLoader.ResolutionScaleY);
		public static final Image desert_rock = ImageEffectUtility.precreatedLightedAndShadedImage("desert_rock_one.png", GameSettings.GlOBAL_ILLUMINATION,
			GameSettings.GLOBAL_SPECULARITY, 150 / GameLoader.ResolutionScaleX, 150 / GameLoader.ResolutionScaleY);
		public static final Image desert_rock_alt = ImageEffectUtility.precreatedLightedAndShadedImage("desert_rock_two.png", GameSettings.GlOBAL_ILLUMINATION,
			GameSettings.GLOBAL_SPECULARITY-0.4, 150 / GameLoader.ResolutionScaleX, 150 / GameLoader.ResolutionScaleY);
		public static final Image desert_bark = ImageEffectUtility.precreatedLightedAndShadedImageTwo("desert_tree_bark_four.png", GameSettings.GlOBAL_ILLUMINATION,
			GameSettings.GLOBAL_SPECULARITY-0.4, 480 / GameLoader.ResolutionScaleX, 210 / GameLoader.ResolutionScaleY);
		public static final Image desert_cactus_big = ImageEffectUtility.precreatedLightedAndShadedImage("big_cactus_thick.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 145 / GameLoader.ResolutionScaleX,
			205 / GameLoader.ResolutionScaleY);
		public static final Image desert_cactus_big_alt = ImageEffectUtility.precreatedLightedAndShadedImage("big_cactus_thick_alt.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 145 / GameLoader.ResolutionScaleX,
			205 / GameLoader.ResolutionScaleY);
		public static final Image desert_bush = ImageEffectUtility.precreatedLightedAndShadedImage("desert_bush.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 96 / GameLoader.ResolutionScaleX,
			120 / GameLoader.ResolutionScaleY);
		public static final Image desert_flower_two = ImageEffectUtility.precreatedLightedAndShadedImage("flower_cactus_two.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 96 / GameLoader.ResolutionScaleX,
			120 / GameLoader.ResolutionScaleY);
		public static final Image desert_flower = ImageEffectUtility.precreatedLightedAndShadedImage("flower_cactus.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 72 / GameLoader.ResolutionScaleX,
			123 / GameLoader.ResolutionScaleY);
		public static final Image desert_flower_alt = ImageEffectUtility.precreatedLightedAndShadedImage("flower_cactus_alt.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 72 / GameLoader.ResolutionScaleX,
			123 / GameLoader.ResolutionScaleY);
		public static final Image desert_flower_alt2 = ImageEffectUtility.precreatedLightedAndShadedImage("flower_cactus_alt2.png",
				GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 72 / GameLoader.ResolutionScaleX,
				123 / GameLoader.ResolutionScaleY);
		public static final Image desert_skull = ImageEffectUtility.precreatedLightedAndShadedImage("desert_bison_skull.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 140 / GameLoader.ResolutionScaleX,
			105 / GameLoader.ResolutionScaleY);
		public static final Image desert_bones = ImageEffectUtility.precreatedLightedAndShadedImage("desert_bones.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 140 / GameLoader.ResolutionScaleX,
			105 / GameLoader.ResolutionScaleY);
		public static final Image desert_bones_alt = ImageEffectUtility.precreatedLightedAndShadedImage("desert_bones_alt.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 200 / GameLoader.ResolutionScaleX,
			95 / GameLoader.ResolutionScaleY);
		public static final Image desert_bones_bridge = ImageEffectUtility.precreatedLightedAndShadedImage("desert_bones_bridge.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 200 / GameLoader.ResolutionScaleX,
			95 / GameLoader.ResolutionScaleY);
		public static final Image desert_cactus_small = ImageEffectUtility.precreatedLightedAndShadedImage("desert_cactus_spiky.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 120/ GameLoader.ResolutionScaleX,
			100 / GameLoader.ResolutionScaleY);
		public static final Image desert_trap = ImageEffectUtility.precreatedLightedAndShadedImage("desert_trap.png",
			GameSettings.GlOBAL_ILLUMINATION, GameSettings.GLOBAL_SPECULARITY, 130 / GameLoader.ResolutionScaleX,
			75 / GameLoader.ResolutionScaleY);
		public static final Image desert_sand = ImageEffectUtility.precreatedLightedImage("sand_grain.png",
			GameSettings.GlOBAL_ILLUMINATION - 0.5, GameSettings.GLOBAL_SPECULARITY - 1.2, 5, 5);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Jungle Level Images.
*/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		public static final Image jungle_rain = new Image(ImageEffectUtility.loadResource("rainDrop.png"),10,10, true, true);


		public static final Image classic_background = new Image(ImageEffectUtility.loadResource("grid.png"),
				GameSettings.WIDTH, GameSettings.HEIGHT, false, true);


}
