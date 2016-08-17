 package com.EudyContreras.Snake.Utilities;

import com.EudyContreras.Snake.Application.GameSettings;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
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
import javafx.scene.shape.Rectangle;

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
	private static Image img;
	private static SnapshotParameters parameters = new SnapshotParameters();
	private static Rectangle rect = new Rectangle();
	private static ImageView view = new ImageView();
	private static Lighting lighting = new Lighting();
	private static Light.Point light = new Light.Point();
	private static DropShadow shadow = new DropShadow(15, Color.BLACK);
	private static DropShadow borderGlow = new DropShadow();
	private static BoxBlur blur = new BoxBlur();
	private static GaussianBlur gaussianBlur = new GaussianBlur();
	private static MotionBlur motionBlur = new MotionBlur();
	private static Bloom bloom = new Bloom(0.1);
	private static Glow glow = new Glow(1.0);
	private static Circle circle = new Circle();

	public static synchronized Image createImage(Node node) {

		parameters.setFill(Color.TRANSPARENT);
		WritableImage wi = new WritableImage((int)node.getBoundsInLocal().getWidth(), (int) node.getBoundsInLocal().getHeight());
		node.snapshot(parameters, wi);
		return wi;

	}
	private static void resetInputeffects(){
		view.setEffect(null);
		lighting.setContentInput(null);
		shadow.setInput(null);
		shadow.setBlurType(BlurType.THREE_PASS_BOX);
	}
	public static synchronized Image precreatedLightedImage(String path, double diffused, double specularMap, double width,
			double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		light.setX(-150);
		light.setY(220);
		light.setZ(115);
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
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view = new ImageView(img);
		light.setX(-200);
		light.setY(250);
		light.setZ(130);

//		light.setX(-150);
//		light.setY(350);
//		light.setZ(140);
//
//
//		light.setX(-150);
//		light.setY(220);
//		light.setZ(115);
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
	public static synchronized Image precreatedLightedAndShadedImageTwo(String path, double diffused, double specularMap, double width,
			double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		light.setX(-150);
		light.setY(300);
		light.setZ(140);
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
		light.setX(-150);
		light.setY(220);
		light.setZ(115);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(2);
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
	public static synchronized Image precreatedLightedAndShadedTail(String path, double diffused, double specularMap, double width,
			double height) {
		resetInputeffects();
		img = new Image(loadResource(path), width, height, true, true);
		view.setImage(img);
		light.setX(-150);
		light.setY(220);
		light.setZ(115);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(2);
		lighting.setSurfaceScale(10.0);
		lighting.setLight(light);
		shadow.setColor(Color.rgb(0, 0, 0, 0.5));
		shadow.setRadius(20);
		shadow.setOffsetX(0);
		shadow.setOffsetY(0);
		lighting.setContentInput(shadow);
		if (GameSettings.ADD_LIGHTING)
			view.setEffect(lighting);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageEffectUtility.createImage(view);
		return img;
	}
	public static synchronized Image preCreateShadedCircle(Color color, double diffused, double specularMap, double radius) {
		resetInputeffects();
		Image img;
		circle.setFill(color);
		circle.setRadius(radius);
		light.setX(0);
		light.setY(200);
		light.setZ(150);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(specularMap);
		lighting.setSurfaceScale(8.0);
		lighting.setLight(light);
		if (GameSettings.ADD_LIGHTING)
			circle.setEffect(lighting);
		img = ImageEffectUtility.createImage(circle);
		return img;
	}
	public static synchronized Image preCreateShadedBlurredCircle(Color color, double diffused, double specularMap, double radius) {
		resetInputeffects();
		Image img;
		circle.setFill(color);
		circle.setRadius(radius);
		light.setX(-150);
		light.setY(220);
		light.setZ(115);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(2);
		lighting.setSurfaceScale(8.0);
		blur.setIterations(1);
		blur.setWidth(10);
		blur.setHeight(10);
		lighting.setContentInput(blur);
		lighting.setLight(light);
		if (GameSettings.ADD_LIGHTING)
			circle.setEffect(lighting);
		img = ImageEffectUtility.createImage(circle);
		return img;
	}
	public static synchronized Image preCreateCircle(Color color, double radius) {
		Image img;
		resetInputeffects();
		shadow.setColor(Color.rgb(0, 0, 0, 0.0));
		shadow.setRadius(0);
		shadow.setBlurType(BlurType.ONE_PASS_BOX);
		shadow.setHeight(radius*5);
		shadow.setWidth(radius*5);
		shadow.setOffsetX(0);
		shadow.setOffsetY(0);
		circle.setEffect(shadow);
		circle.setFill(color);
		circle.setRadius(radius);
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
		light.setY(1920);
		light.setZ(1000);
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
	public static final Image precreateBackground(Color orange, double width, double height) {
		resetInputeffects();
		rect.setFill(orange);
		rect.setWidth(width);
		rect.setHeight(height);
		img = ImageEffectUtility.createImage(rect);
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
			double radius) {
		Image img;
		Circle circle = new Circle();
		circle.setFill(color);
		circle.setRadius(radius);
		circle.setStroke(color);
		circle.setStrokeWidth(3);
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
	public static final synchronized Image GLOWING_RECTANGLE(Color color, double depth, double spread,double width, double height) {
		Image img;
		Rectangle rect = new Rectangle(width, height, color);
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setSpread(spread);
		borderGlow.setColor(color);
		borderGlow.setWidth(depth);
		borderGlow.setHeight(depth);
		borderGlow.setBlurType(BlurType.GAUSSIAN);
		rect.setEffect(borderGlow);
		img = ImageEffectUtility.createImage(rect);
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
		lighting.setDiffuseConstant(GameSettings.GlOBAL_ILLUMINATION);
		lighting.setSpecularConstant(GameSettings.GLOBAL_SPECULARITY);
		lighting.setSurfaceScale(10.0);
		lighting.setLight(light);
		shadow.setColor(Color.rgb(0, 0, 0, 0.5));
		shadow.setRadius(4);
		shadow.setOffsetX(15);
		shadow.setOffsetY(-10);
		gaussianBlur.setRadius(20);
		shadow.setInput(gaussianBlur);
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

	private static String loadResource(String image) {
		String url = GameSettings.IMAGE_SOURCE_DIRECTORY + image;
		return url;
	}

}
