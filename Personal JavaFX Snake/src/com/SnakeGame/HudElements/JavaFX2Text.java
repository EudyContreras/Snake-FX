package com.SnakeGame.HudElements;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
 
import javafx.stage.Stage;
 
/**
 *
 * @author zoranpavlovic.blogspot.com
 */
public class JavaFX2Text extends Application {
        /**
         * @param args
         *            the command line arguments
         */
        public static void main(String[] args) {
                Application.launch(args);
        }
 
        @Override
        public void start(Stage primaryStage) {
                primaryStage.setTitle("Styling text");
 
                // Layouts stuff
                VBox vb = new VBox();
                vb.setPadding(new Insets(10, 50, 50, 50));
                vb.setSpacing(20);
                HBox hb = new HBox();
 
                // Text stuff
                Text txtJava = new Text("Java");
                txtJava.setFont(Font.font(null, FontWeight.BOLD, 72));
 
                Text txtFX2 = new Text("FX2");
                txtFX2.setFont(Font.font(null, FontWeight.BOLD, 72));
 
                Text text1 = new Text("JavaFX2");
                text1.setFont(Font.font(null, FontWeight.BOLD, 72));
 
                Text text2 = new Text("JavaFX2");
                text2.setFont(Font.font(null, FontWeight.BOLD, 72));
 
                // DropShadow for txtFX2
                DropShadow dropShadow = new DropShadow();
                dropShadow.setColor(Color.DODGERBLUE);
                dropShadow.setRadius(25);
                dropShadow.setSpread(0.25);
                dropShadow.setBlurType(BlurType.GAUSSIAN);
                txtFX2.setEffect(dropShadow);
 
                // DropShadow for text1
                DropShadow dropShadow2 = new DropShadow();
                dropShadow2.setOffsetY(3.0);
                dropShadow2.setOffsetX(3.0);
                dropShadow2.setColor(Color.GREEN);
                dropShadow2.setBlurType(BlurType.GAUSSIAN);
                text1.setEffect(dropShadow2);
 
                // Adding ID's
                text1.setId("JavaFX2");
                txtJava.setId("Java");
                txtFX2.setId("FX2");
                text2.setId("text2");
                vb.setId("root");
 
                // Adding Nodes to Layouts
                hb.getChildren().addAll(txtJava, txtFX2);
                vb.getChildren().addAll(hb, text1, text2);
 
                // Adding VBox to the scene
                Scene scene = new Scene(vb);
                scene.getStylesheets().add(
                                getClass().getClassLoader().getResource("text.css")
                                                .toExternalForm());
                primaryStage.setScene(scene);
                primaryStage.show();
        }
}