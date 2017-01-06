package com.EudyContreras.Snake.PlayRoomHub;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
public class SwipeMenuDemo extends Application {

    AnchorPane swapPane;
    Button btnMenu;
    boolean isExpanded = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {    
        Label swapPaneLabel = new Label("Expandable Pane");
        swapPaneLabel.setMinWidth(0);

        ImageView swapPaneImage = new ImageView("http://vignette1.wikia.nocookie.net/jfx/images/5/5a/JavaFXIsland600x300.png");
        swapPaneImage.setLayoutY(100);

        Label rootPaneLabel = new Label("Root Pane");
        rootPaneLabel.setStyle("-fx-font-size: 60;");
        rootPaneLabel.setLayoutX(180);
        rootPaneLabel.setLayoutY(180);

        swapPane = new AnchorPane();
        swapPane.setPrefSize(640, 440);
        swapPane.setMinWidth(0);
        swapPane.setLayoutY(40);
        swapPane.setStyle("-fx-background-color: coral; -fx-font-size: 52;");
        swapPane.getChildren().addAll(swapPaneImage, swapPaneLabel);

        btnMenu = new Button("Menu");
        btnMenu.setLayoutX(5);
        btnMenu.setLayoutY(5);
        btnMenu.setOnMouseClicked(e -> {
            if (isExpanded) hideSwapPane().play();
            else showSwapPane().play();
        });

        Button btnClose = new Button("Close");
        btnClose.setLayoutX(590);
        btnClose.setLayoutY(5);
        btnClose.setOnMouseClicked(e -> Platform.exit());

        AnchorPane rootPane = new AnchorPane();
        rootPane.setStyle("-fx-background-color: grey;");
        rootPane.getChildren().addAll(btnMenu, btnClose, rootPaneLabel, swapPane);

        Scene scene = new Scene(rootPane, 640, 480);

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    private Animation hideSwapPane() {            
        btnMenu.setMouseTransparent(true);

        Animation collapsePanel = new Transition() {
            {
                setCycleDuration(Duration.millis(2500));
            }

            @Override
            protected void interpolate(double fraction) {
                swapPane.setPrefWidth(640 * (1.0 - fraction));
            }
        };

        collapsePanel.setOnFinished(e-> {
            isExpanded = false;
            btnMenu.setMouseTransparent(false);
        });

        return collapsePanel;
    }

    private Animation showSwapPane() {            
        btnMenu.setMouseTransparent(true);

        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(2500));
            }

            @Override
            protected void interpolate(double fraction) {
                swapPane.setPrefWidth(640 * fraction);
            }
        };

        expandPanel.setOnFinished(e-> {
            isExpanded = true;
            btnMenu.setMouseTransparent(false);
        });

        return expandPanel;
    }
}