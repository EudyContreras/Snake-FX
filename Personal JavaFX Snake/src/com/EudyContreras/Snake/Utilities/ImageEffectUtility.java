 package com.EudyContreras.Snake.Utilities;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This image utility class is used to pre-create images with specific effects
 * added to them. The image will be loaded, the effects will be applied and then
 * the Image will be saved to memory as a snapshot of the post effects image.
 * This class may additionally allow the user to save the post effects image to
 * a desired location on the near future.
 *
 * @author Eudy Contreras
 *
 */
public class ImageEffectUtility {
	public static Image img;
	public static SnapshotParameters parameters = new SnapshotParameters();
	public static ImageView view = new ImageView();
	public static Lighting lighting = new Lighting();
	public static Light.Point light = new Light.Point();
	public static DropShadow shadow = new DropShadow(15, Color.BLACK);
	public static DropShadow borderGlow = new DropShadow();
	public static MotionBlur motionBlur = new MotionBlur();
	public static GaussianBlur gaussianBlur = new GaussianBlur();
	public static Bloom bloom = new Bloom(0.1);
	public static Glow glow = new Glow(1.0);

	public static synchronized Image createImage(Node node) {

		parameters.setFill(Color.TRANSPARENT);
		WritableImage wi = new WritableImage((int)node.getBoundsInLocal().getWidth(), (int) node.getBoundsInLocal().getHeight());
		node.snapshot(parameters, wi);
//		node.setCache(true);
//		node.setCacheHint(CacheHint.SPEED);
		return wi;

	}
	private static void resetInputeffects(){
		lighting.setContentInput(null);
		shadow.setInput(null);
		shadow.setBlurType(BlurType.ONE_PASS_BOX);
	}
	public static synchronized Image precreatedLightedImage(String path, double diffused, double specularMap, double width,
			double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		light.setX(-100);
		light.setY(200);
		light.setZ(100);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(specularMap);
		lighting.setSurfaceScale(10.0);
		lighting.setLight(light);
		if (GameSettings.ADD_LIGHTING)
			view.setEffect(lighting);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}

	public static synchronized Image precreateShadedImages(String path, double diffused, double specularMap, double width,
			double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		shadow.setColor(Color.rgb(0, 0, 0, 0.8));
		shadow.setRadius(15);
		shadow.setOffsetX(20);
		shadow.setOffsetY(-15);
		if (GameSettings.ADD_LIGHTING)
			view.setEffect(shadow);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}

	public static synchronized Image precreatedLightedAndShadedImage(String path, double diffused, double specularMap, double width,
			double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
//		light.setX(-130);
//		light.setY(315);
//		light.setZ(130);

		light.setX(-200);
		light.setY(300);
		light.setZ(150);

//		light.setX(-180);
//		light.setY(200);
//		light.setZ(130);

		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(specularMap);
		lighting.setSurfaceScale(10.0);
		lighting.setLight(light);
		shadow.setColor(Color.rgb(0, 0, 0, 0.6));
		shadow.setRadius(20/(GameManager.ScaleX_ScaleY));
		shadow.setOffsetX(GameManager.ScaleX(25));
		shadow.setOffsetY(GameManager.ScaleY(-20));
		lighting.setContentInput(shadow);
		if (GameSettings.ADD_LIGHTING)
			view.setEffect(lighting);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}
	public static synchronized Image precreatedLightedAndShadedImageTwo(String path, double diffused, double specularMap, double width,
			double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		light.setX(-180);
		light.setY(200);
		light.setZ(130);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(specularMap);
		lighting.setSurfaceScale(10.0);
		lighting.setLight(light);
		shadow.setColor(Color.rgb(0, 0, 0, 0.6));
		shadow.setRadius(20);
		shadow.setOffsetX(20);
		shadow.setOffsetY(-15);
		lighting.setContentInput(shadow);
		if (GameSettings.ADD_LIGHTING)
			view.setEffect(lighting);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}
	public static synchronized Image precreatedLightedAndShadedSnake(String path, double diffused, double specularMap, double width,
			double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		light.setX(-200);
		light.setY(300);
		light.setZ(150);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(specularMap);
		lighting.setSurfaceScale(10.0);
		lighting.setLight(light);
		shadow.setColor(Color.rgb(0, 0, 0, 0.5));
		shadow.setRadius(5);
		shadow.setOffsetX(20);
		shadow.setOffsetY(-15);
		lighting.setContentInput(shadow);
		if (GameSettings.ADD_LIGHTING)
			view.setEffect(lighting);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}
	public static synchronized Image preCreateShadedCircle(Color color, double diffused, double specularMap, double width,
			double height) {
		Lighting lighting = new Lighting();
		Light.Point light = new Light.Point();
		Image img;
		Circle circle = new Circle();
		circle.setFill(color);
		circle.setRadius(width);
		light.setX(340);
		light.setY(600);
		light.setZ(300);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(specularMap);
		lighting.setSurfaceScale(8.0);

		lighting.setLight(light);
		if (GameSettings.ADD_LIGHTING)
			circle.setEffect(lighting);
		img = ImageEffectUtility.createImage(circle);
		return img;
	}

	public static synchronized Image preCreateShadedGlowingCircle(Color color, double diffused, double specularMap, double width,
			double height) {
		Lighting lighting = new Lighting();
		Light.Point light = new Light.Point();
		Image img;
		Circle circle = new Circle();
		circle.setFill(color);
		circle.setRadius(width);
		light.setX(340);
		light.setY(600);
		light.setZ(300);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(specularMap);
		lighting.setSurfaceScale(8.0);
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setSpread(20);
		borderGlow.setColor(color);
		borderGlow.setWidth(50);
		borderGlow.setHeight(20);
		borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		lighting.setLight(light);
		lighting.setContentInput(borderGlow);
		if (GameSettings.ADD_LIGHTING)
			circle.setEffect(lighting);
		img = ImageEffectUtility.createImage(circle);
		return img;
	}

	public static synchronized Image preCreateShadedBackground(String path, double diffused, double specularMap, double width,
			double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		light.setX(0);
		light.setY(1300);
		light.setZ(850);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(0);
		lighting.setSurfaceScale(10.0);
		lighting.setLight(light);
		if (GameSettings.ADD_LIGHTING)
			view.setEffect(lighting);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}

	public static synchronized Image preCreateShadedDebris(String path, double diffused, double specularMap, double width,
			double height) {
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		light.setX(0);
		light.setY(700);
		light.setZ(250);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(specularMap);
		lighting.setSurfaceScale(5.0);
		lighting.setLight(light);
		if (GameSettings.ADD_LIGHTING)
			view.setEffect(lighting);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}

	public static synchronized Image preCreateGlowingImages(String path, Color color, double depth, double spread, double width,
			double height) {
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(-10f);
		borderGlow.setSpread(spread);
		borderGlow.setColor(color);
		borderGlow.setWidth(depth);
		borderGlow.setHeight(70);
		borderGlow.setBlurType(BlurType.GAUSSIAN);
		view.setEffect(borderGlow);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}

	public static synchronized Image preCreateGlowingCircle(Color color, double opacity,double depth, double spread, double width, double height) {
		Image img;
		Circle circle = new Circle();
		circle.setFill(Color.rgb(255, 200, 0, 1.0));
		circle.setRadius(20);
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setSpread(spread);
        borderGlow.setColor(color);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
        circle.setOpacity(opacity);
        circle.setEffect(borderGlow);
		img = ImageEffectUtility.createImage(circle);
		return img;
	}
	public static synchronized Image preCreateAlternateGlowingCircle(Color color, double opacity, double depth, double spread,
			double width, double height) {
		Image img;
		Circle circle = new Circle();
		circle.setFill(Color.rgb(255, 150, 0, 1.0));
		circle.setRadius(20);
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setSpread(spread);
		borderGlow.setColor(color);
		borderGlow.setWidth(depth);
		borderGlow.setHeight(depth);
		borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		circle.setOpacity(opacity);
		circle.setEffect(borderGlow);
		img = ImageEffectUtility.createImage(circle);
		return img;
	}
	public static synchronized Image preCreateAlternateGlowingCircleTwo(Color color, double opacity, double depth, double spread,
			double width, double height) {
		Image img;
		Circle circle = new Circle();
		circle.setFill(Color.rgb(255, 50, 0, 1.0));
		circle.setRadius(20);
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setSpread(spread);
		borderGlow.setColor(color);
		borderGlow.setWidth(depth);
		borderGlow.setHeight(depth);
		borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		circle.setOpacity(opacity);
		circle.setEffect(borderGlow);
		img = ImageEffectUtility.createImage(circle);
		return img;
	}
	public static synchronized Image preCreateImageWithBloom(String path, double threshold, double width, double height) {
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		bloom.setThreshold(threshold);
		view.setEffect(bloom);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}
	public static synchronized Image preCreateImageWithMotionBlur(String path, double width, double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		light.setX(-100);
		light.setY(200);
		light.setZ(100);
//		blur.setIterations(1);
		lighting.setDiffuseConstant(GameSettings.GlOBAL_ILLUMINATION);
		lighting.setSpecularConstant(GameSettings.GLOBAL_SPECULARITY);
		lighting.setSurfaceScale(10.0);
		lighting.setLight(light);
		shadow.setColor(Color.rgb(0, 0, 0, 0.5));
		shadow.setRadius(5);
		shadow.setOffsetX(20);
		shadow.setOffsetY(-15);
		shadow.setInput(motionBlur);
		lighting.setContentInput(shadow);
		if(GameSettings.ADD_LIGHTING)
		view.setEffect(lighting);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}

	public static synchronized Image preCreateImageWithBlur(String path, double radius, double width, double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		gaussianBlur.setRadius(radius);
		view.setEffect(gaussianBlur);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}

	public static synchronized Image preCreateBrighterImage(String path, double glowLevel, double width, double height) {
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		glow.setLevel(glowLevel);
		view.setEffect(glow);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}
	public static synchronized Image precreateSnapshot(String path, double width, double height) {
		Image img = new Image(loadResource(path), width, height, true, true);
		ImageView view = new ImageView(img);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}

	public static synchronized Image snapShotImage(String path, double width, double height) {
		Image img = new Image(path);
		ImageView view = new ImageView(img);
		img = ImageEffectUtility.createImage(view);
		return img;
	}

	public static String loadResource(String image) {
		String url = GameSettings.IMAGE_SOURCE_DIRECTORY + image;
		return url;
	}
}
