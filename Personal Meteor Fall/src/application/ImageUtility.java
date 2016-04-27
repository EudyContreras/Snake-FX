package application;



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

public class ImageUtility {
	public static WritableImage wi;
	public static Lighting lighting = new Lighting();
	public static Light.Point light = new Light.Point();
	public static DropShadow borderGlow= new DropShadow();
	public static MotionBlur motionBlur = new MotionBlur();
	public static GaussianBlur gaussianBlur = new GaussianBlur();
	public static Bloom bloom = new Bloom(0.1);
	public static Glow glow = new Glow(1.0);
	public static Image createImage(Node node) {

		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		int width = (int) node.getBoundsInLocal().getWidth();
		int height = (int) node.getBoundsInLocal().getHeight();
		wi = new WritableImage(width, height);
		node.snapshot(parameters, wi);
		return wi;

	}

	public static Image preCreateShadedImages(String path, double diffused, double specularMap, double width, double height) {
		Image img = new Image(path, width, height, true, false, false);
		ImageView view = new ImageView(img);
		light.setX(240);
		light.setY(700);
		light.setZ(250);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(specularMap);
		lighting.setSurfaceScale(5.0);
		lighting.setLight(light);
		if(Settings.ADD_LIGHTING)
		view.setEffect(lighting);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageUtility.createImage(view);
		return img;
	}
	public static Image preCreateAsteroidDebris(String path, double diffused, double specularMap, double width, double height) {
		Image img = new Image(path, width, height, true, false, false);
		ImageView view = new ImageView(img);
		light.setX(0);
		light.setY(700);
		light.setZ(250);
		lighting.setDiffuseConstant(diffused);
		lighting.setSpecularConstant(specularMap);
		lighting.setSurfaceScale(5.0);
		lighting.setLight(light);
		if(Settings.ADD_LIGHTING)
		view.setEffect(lighting);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageUtility.createImage(view);
		return img;
	}
	public static Image preCreateGlowingImages(String path, Color color, double depth, double spread, double width, double height) {
		Image img = new Image(path, width, height, true, false, false);
		ImageView view = new ImageView(img);
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
		img = ImageUtility.createImage(view);
		return img;
	}
	public static Image preCreateThrustImage(String path) {
		Image img = new Image(path,150,80,true,false,false);
		ImageView view = new ImageView(img);
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setColor(Color.rgb(0, 50, 255,1));
		borderGlow.setWidth(100);
		borderGlow.setHeight(100);
		borderGlow.setSpread(0.3);
		borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		view.setEffect(borderGlow);
		img = ImageUtility.createImage(view);
		return img;
	}
	public static Image preCreateGlowingCircle(Color color, double opacity,double depth, double spread, double width, double height) {
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
		img = ImageUtility.createImage(circle);
		return img;
	}
	public static Image preCreateGlowingCircle2(Color color, double opacity,double depth, double spread, double width, double height) {
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
		img = ImageUtility.createImage(circle);
		return img;
	}
	public static Image preCreateExplosionLight(Color color, double opacity,double depth, double spread) {
		Image img;
		Circle circle = new Circle();
		circle.setFill(Color.rgb(255, 200, 0, 0.2));
		circle.setRadius(150);
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setSpread(spread);
        borderGlow.setColor(color);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        borderGlow.setBlurType(BlurType.GAUSSIAN);
        circle.setOpacity(opacity);
        circle.setEffect(borderGlow);
		img = ImageUtility.createImage(circle);
		return img;
	}
	public static Image preCreateImageBloom(String path, double threshold, double width, double height) {
		Image img = new Image(path, width, height, true, false, false);
		ImageView view = new ImageView(img);
		bloom.setThreshold(threshold);
		view.setEffect(bloom);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageUtility.createImage(view);
		return img;
	}
	public static Image preCreateBluredImage(String path, double radius, double width, double height) {
		Image img = new Image(path, width, height, true, false, false);
		ImageView view = new ImageView(img);
		gaussianBlur.setRadius(radius);
		view.setEffect(gaussianBlur);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageUtility.createImage(view);
		return img;
	}
	public static Image preCreateBrightImage(String path, double glowLevel, double width, double height) {
		Image img = new Image(path, width, height, true, false, false);
		ImageView view = new ImageView(img);
		glow.setLevel(glowLevel);
		view.setEffect(glow);
		view.setFitWidth(width);
		view.setFitHeight(height);
		img = ImageUtility.createImage(view);
		return img;
	}
	public static Image snapShotImage(String path, double width, double height) {
		Image img = new Image(path);
		ImageView view = new ImageView(img);
		img = ImageUtility.createImage(view);
		return img;
	}
}
