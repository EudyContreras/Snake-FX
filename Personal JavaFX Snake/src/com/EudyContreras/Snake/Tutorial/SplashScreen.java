package com.EudyContreras.Snake.Tutorial;


import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**

 *
 * @author Eudy Contreras
 *
 */
public class SplashScreen extends Application{

	protected FadeTransition fadeSplash;
	protected Scene scene;
	protected Scene splashScene;
	protected Stage mainWindow;

	protected Pane splashLayout;

	protected ImageView splash;
	protected Rectangle2D bounds;

	protected boolean isRunning = true;
	protected boolean gameRunning = false;

	protected double splashFadeDuration = 0;
	protected double splashFadeDelay = 1;

	protected int splashWidth;
	protected int splashHeight;
	protected int levelLenght;

	private Image splash_image = new Image("image.png");

	public void start(Stage primaryStage) {
		initSplash();
		initializeGame();
		showSplashScreen();
	}
	public void initSplash() {
		splash = new ImageView(splash_image);
		splashWidth = (int) splash.getImage().getWidth();
		splashHeight = (int) splash.getImage().getHeight();
		splashLayout = new StackPane();
		splashLayout.getChildren().add(splash);
		splashLayout.setBackground(Background.EMPTY);
		splashLayout.setEffect(new DropShadow());
	}

	public void showSplashScreen() {
		splashScene = new Scene(splashLayout, Color.TRANSPARENT);
		bounds = Screen.getPrimary().getBounds();

		mainWindow.initStyle(StageStyle.TRANSPARENT);

		mainWindow.setScene(splashScene);

		mainWindow.setX(bounds.getMinX() + bounds.getWidth() / 2 - splashWidth / 2);
		mainWindow.setY(bounds.getMinY() + bounds.getHeight() / 2 - splashHeight / 2);

		mainWindow.show();

		fadeSplash = new FadeTransition(Duration.seconds(splashFadeDuration), splashLayout);

		fadeSplash.setDelay(Duration.seconds(splashFadeDelay));
		fadeSplash.setFromValue(1.0);
		fadeSplash.setToValue(0.0);

		fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				mainWindow.hide();
				mainWindow = null;
				mainWindow = new Stage();
				if (!gameRunning) {
					showGame();
					gameRunning = true;
				}
			}
		});
		fadeSplash.play();
	}
	private void initializeGame() {

	}
	public void showGame() {
		fadeSplash = null;
		splashLayout = null;
		splash = null;
		splashScene = null;

		mainWindow.setScene(scene);
		mainWindow.initStyle(StageStyle.UNDECORATED);
		mainWindow.show();

	}
	public static void main(String[] args) {
		launch(args);
	}

}
