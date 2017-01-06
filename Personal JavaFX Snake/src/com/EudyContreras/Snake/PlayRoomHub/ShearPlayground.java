package com.EudyContreras.Snake.PlayRoomHub;

import java.text.DecimalFormat;

import com.EudyContreras.Snake.ImageBanks.GameLevelImage;
import com.EudyContreras.Snake.Utilities.FillUtility;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Shear;
import javafx.stage.Stage;

public class ShearPlayground extends Application {
    @Override
    public void start(Stage stage) {


    	ImageView image = new ImageView(GameLevelImage.desert_cactus_big);

    	StackPane layout = new StackPane();
        VBox controlls = new VBox(10);
        VBox sliderPane = new VBox(55);
    	Shear shear = new Shear();

        image.getTransforms().add(shear);

    	Controll shearX = new Controll("Shear X",-1,1);
    	Controll shearY = new Controll("Shear Y",-1,1);
    	Controll shearPivotX = new Controll("Shear pivot X",0,image.getImage().getWidth());
    	Controll shearPivotY = new Controll("Shear pivot Y",0,image.getImage().getHeight());
    	Controll translateX = new Controll("Translate X",-150,150);
    	Controll translateY = new Controll("Translate Y",-150,150);

        shear.xProperty().bind(shearX.getSlider().valueProperty());
        shear.yProperty().bind(shearY.getSlider().valueProperty());
        shear.pivotXProperty().bind(shearPivotX.getSlider().valueProperty());
        shear.pivotYProperty().bind(shearPivotY.getSlider().valueProperty());

        image.translateXProperty().bind(translateX.getSlider().valueProperty());
        image.translateYProperty().bind(translateY.getSlider().valueProperty());

        controlls.getChildren().addAll(shearX,shearY,shearPivotX,shearPivotY,translateX,translateY);
        sliderPane.getChildren().addAll(image,controlls);

        sliderPane.setSpacing(75);

        sliderPane.setAlignment(Pos.CENTER);
        controlls.setAlignment(Pos.CENTER);

        sliderPane.setPrefWidth(200);

        layout.getChildren().add(sliderPane);

        layout.setPadding(new Insets(10));

        layout.setBackground(FillUtility.PAINT_FILL(Color.SILVER));

        stage.setTitle("Shear Transform Playground");
        stage.setScene(new Scene(layout,500,700, Color.GRAY));
        stage.show();
    }


    private class Controll extends StackPane{
    	private HBox container = new HBox();
    	private Text text = new Text();
    	private Text value = new Text("0.00");
    	private Slider slider = new Slider();
    	private DecimalFormat formatter = new DecimalFormat("#0.00");

    	public Controll(String name, double min, double max){
    		slider.setMax(max);
    		slider.setMin(min);
    		slider.setValue(0);
    		slider.setPrefWidth(350);
    		slider.valueProperty().addListener(new ChangeListener<Object>() {
                @Override
                public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
                    value.textProperty().setValue(String.valueOf(formatter.format(slider.getValue())));
                }
            });
    		text.setText(name);
    		text.setFont(Font.font(null,FontWeight.BOLD,14));
    		container.setSpacing(10);
    		container.setAlignment(Pos.CENTER_LEFT);
    		container.getChildren().addAll(text,slider,value);
    		getChildren().add(container);
    	}

    	public Slider getSlider(){
    		return slider;
    	}
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
