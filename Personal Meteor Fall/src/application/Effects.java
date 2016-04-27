package application;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.FloatMap;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.ImageInput;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.effect.Reflection;
import javafx.scene.effect.SepiaTone;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
 
/**
 *
 * @web http://java-buddy.blogspot.com
 */
public class Effects extends Application {
     
    @Override
    public void start(Stage primaryStage) {
         
        Image image = new Image("http://goo.gl/kYEQl");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
         
        Image secondImage = new Image("http://goo.gl/Z6Qiw0");
         
        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();
         
        //Blend effect
        Blend blend = new Blend();
        blend.setMode(BlendMode.COLOR_BURN);
        ColorInput blendColorInput = new ColorInput();
        blendColorInput.setPaint(Color.STEELBLUE);
        blendColorInput.setX(0);
        blendColorInput.setY(0);
        blendColorInput.setWidth(imageWidth);
        blendColorInput.setHeight(imageHeight);
        blend.setTopInput(blendColorInput);
         
        //Bloom effect
        Bloom bloom = new Bloom(0.1);
         
        //BoxBlur effect
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(3);
        boxBlur.setHeight(3);
        boxBlur.setIterations(3);
         
        //ColorAdjust effect
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(0.1);
        colorAdjust.setHue(-0.05);
        colorAdjust.setBrightness(0.1);
        colorAdjust.setSaturation(0.2);
         
        //ColorInput effect
        ColorInput colorInput;
        colorInput = new ColorInput(0, 0, 
                imageWidth, imageHeight, Color.STEELBLUE);
 
        //DisplacementMap effect
        FloatMap floatMap = new FloatMap();
        floatMap.setWidth(imageWidth);
        floatMap.setHeight(imageHeight);
 
        for (int i = 0; i < imageWidth; i++) {
            double v = (Math.sin(i / 20.0 * Math.PI) - 0.5) / 40.0;
            for (int j = 0; j < imageHeight; j++) {
                floatMap.setSamples(i, j, 0.0f, (float) v);
            }
        }
        DisplacementMap displacementMap = new DisplacementMap();
        displacementMap.setMapData(floatMap);
         
        //DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(10.0);
        dropShadow.setOffsetY(5.0);
        dropShadow.setColor(Color.GREY);
         
        //GaussianBlur effect
        GaussianBlur gaussianBlur = new GaussianBlur();
         
        //Glow effect
        Glow glow = new Glow(1.0);
         
        //ImageInput effect
        ImageInput imageInput = new ImageInput(secondImage);
         
        //InnerShadow effect
        InnerShadow innerShadow = new InnerShadow(5.0, 5.0, 5.0, Color.AZURE);
         
        //Lighting effect
        Light.Distant light = new Light.Distant();
        light.setAzimuth(50.0);
        light.setElevation(30.0);
        light.setColor(Color.YELLOW);
         
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(50.0);
         
        //MotionBlur effect
        MotionBlur motionBlur = new MotionBlur();
        motionBlur.setRadius(30);
        motionBlur.setAngle(-15.0);
         
        //PerspectiveTransform effect
        PerspectiveTransform perspectiveTrasform = new PerspectiveTransform();
        perspectiveTrasform.setUlx(0.0);
        perspectiveTrasform.setUly(0.0);
        perspectiveTrasform.setUrx(imageWidth*1.5);
        perspectiveTrasform.setUry(0.0);
        perspectiveTrasform.setLrx(imageWidth*3);
        perspectiveTrasform.setLry(imageHeight*2);
        perspectiveTrasform.setLlx(0);
        perspectiveTrasform.setLly(imageHeight);
         
        //Reflection effect
        Reflection reflection = new Reflection();
        reflection.setFraction(0.7);
         
        //SepiaTone effect
        SepiaTone sepiaTone = new SepiaTone();
         
        //Shadow effect
        Shadow shadow = new Shadow(BlurType.THREE_PASS_BOX, Color.BLUE, 10.0);
         
        Effect effects[] = {
            null,
            blend,
            bloom,
            boxBlur,
            colorAdjust,
            colorInput,
            displacementMap,
            dropShadow,
            gaussianBlur,
            glow,
            imageInput,
            innerShadow,
            lighting,
            motionBlur,
            perspectiveTrasform,
            reflection,
            sepiaTone,
            shadow
        };
         
        ChoiceBox<String> choiceBox = new ChoiceBox<String>(
            FXCollections.observableArrayList(
                "null", "Blend", "Bloom", "BoxBlur", "ColorAdjust",
                "ColorInput", "DisplacementMap", "DropShadow",
                "GaussianBlur", "Glow", "ImageInput", "InnerShadow",
                "Lighting", "MotionBlur", "PerspectiveTransform",
                "Reflection", "SepiaTone", "Shadow"
            ));
        choiceBox.getSelectionModel().selectFirst();
         
        choiceBox.getSelectionModel().selectedIndexProperty()
            .addListener((ObservableValue<? extends Number> observable, 
                Number oldValue, Number newValue) -> {
            imageView.setEffect(effects[newValue.intValue()]);
        });
         
        ImageView retrievedImage = new ImageView();
        Label labelPath = new Label();
         
        Button btnSnapShot = new Button("Take SnapShot");
        btnSnapShot.setOnAction((ActionEvent event) -> {
            File savedFile = takeSnapShot(imageView);
            retrieveImage(savedFile, retrievedImage, labelPath);
        });
         
        Button btnSaveImage = new Button("Save");
        btnSaveImage.setOnAction((ActionEvent event) -> {
            File savedFile = saveImage(imageView);
            retrieveImage(savedFile, retrievedImage, labelPath);
        });
 
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.getChildren().addAll(choiceBox, imageView, btnSnapShot, 
                btnSaveImage, retrievedImage, labelPath);
         
        StackPane root = new StackPane();
        root.getChildren().add(vBox);
 
        Scene scene = new Scene(root, 400, 350);
         
        primaryStage.setTitle("java-buddy.blogspot.com");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
     
    //Take SnapShot and save
    private File takeSnapShot(Node node){
 
        WritableImage writableImage = node.snapshot(new SnapshotParameters(), null);
         
        File file = new File("snapshot.png");
         
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            System.out.println("snapshot saved: " + file.getAbsolutePath());
            return file;
        } catch (IOException ex) {
            Logger.getLogger(Effects.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
     
    //Save Image of ImageView
    private File saveImage(ImageView iv){
        Image img = iv.getImage();
        File file = new File("savedImage.png");
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(img, null);
        try {
            ImageIO.write(renderedImage, "png", file);
            System.out.println("Image saved: " + file.getAbsolutePath());
            return file;
        } catch (IOException ex) {
            Logger.getLogger(Effects.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
     
    //Retrieve saved image
    private void retrieveImage(File file, ImageView imageView, Label label){
        if(file != null){
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
             
            label.setText(file.getName() + "\n"
                    + image.getWidth() + " x " + image.getHeight());
        }else{
            label.setText("");
            imageView.setImage(null);
        }
    }
}