package com.EudyContreras.Snake.Utilities;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
 
public class BlurredReflection extends Application {
  @Override public void start(Stage stage) {
    // construct a reflected label to be displayed in the scene.
    Label label = new Label("The mirror crack'd from side to side");
    label.setStyle("-fx-font-size: 40px; -fx-font-style: italic;");
    label.setEffect(new Reflection());
    
    // show the reflected label on the stage
    StackPane layout = new StackPane();
    layout.getChildren().setAll(label);
    stage.setScene(new Scene(layout, 800, 200));
    stage.show();
    
    // snapshot the reflected part of the label to an image.
    SnapshotParameters params = new SnapshotParameters();
    Bounds effectiveBounds = label.getBoundsInParent();
    params.setViewport(new Rectangle2D(effectiveBounds.getMinX(), effectiveBounds.getMinY() + effectiveBounds.getHeight() * 2 / 3, effectiveBounds.getWidth(), effectiveBounds.getHeight() * 1 /3));
    WritableImage image = label.snapshot(params, null);
 
    // blur the reflected part of the label.
    ImageView reflectedView = new ImageView(image);
    reflectedView.setEffect(new BoxBlur());
    
    // place the original label (now without it's original reflection)
    // in a VBox together with it's blurred reflection.
    VBox blurReflectedLabel = new VBox();
    label.setEffect(null);
    blurReflectedLabel.getChildren().setAll(label, reflectedView);
    blurReflectedLabel.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
    blurReflectedLabel.setPrefSize(effectiveBounds.getWidth(), effectiveBounds.getHeight());
    blurReflectedLabel.setMaxSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
    
    // replace the original reflected label with the blurred reflected version.
    layout.getChildren().setAll(blurReflectedLabel);
  }
 
  public static void main(String[] args) { launch(args); }
}