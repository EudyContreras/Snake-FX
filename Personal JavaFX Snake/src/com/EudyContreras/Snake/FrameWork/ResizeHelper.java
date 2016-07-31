package com.EudyContreras.Snake.FrameWork;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ResizeHelper {

	public static double baseWidth = GameSettings.SCREEN_WIDTH-100;;
	public static double baseHeight = GameSettings.SCREEN_HEIGHT-58;;

	public static void addSceneManipulation(GameManager game,Stage stage, Scene scene) {
		new GestureListener(game,stage,scene);

	}
	private static class GestureListener {

		private GameManager game;
		private final Scene scene;
		private final Stage stage;

		public GestureListener(GameManager game, Stage stage, Scene scene) {
			this.game = game;
			this.scene = scene;
			this.stage = stage;
			this.handleGestures();
		}
		private void handleGestures() {
			scene.setOnScroll(e -> {

				if (e.isControlDown()) {

					if (!stage.isFullScreen()) {
						zoom(stage, e);
					}
				}
			});
			scene.setOnScrollFinished(e ->{
				if (!stage.isFullScreen()) {
					game.setNewRatio(false);
				}
			});
			scene.setOnScrollStarted(e ->{
				if (!stage.isFullScreen()) {
					game.setNewRatio(false);
				}
			});
		}
		public void zoom(Stage stage, double scaleFactor) {

		    double oldScale = game.getRootLayer().getScaleX();
		    double scale = oldScale * scaleFactor;

		    if (scale < 0.15){ scale = 0.005;}
		    if (scale > 1){scale = 1.015;}

		    baseWidth = baseWidth * scale;
			baseHeight = baseHeight * scale;

		    stage.setWidth(baseWidth);
		    stage.setHeight(baseHeight);

		}

		public void zoom(Stage stage, ScrollEvent event) {
		    zoom(stage, Math.pow(1.001, event.getDeltaY()));
		}

	}
	public static void zoom(Stage stage, Node root, Double scrollFactor) {
	    zoom(stage, root, Math.pow(1.001, scrollFactor));
	}

	private static void zoom(Stage stage, Node root, double scaleFactor) {

		double baseWidth = GameSettings.SCREEN_WIDTH-100;
		double baseHeight = GameSettings.SCREEN_HEIGHT-58;

	    double oldScale = root.getScaleX();
	    double scale = oldScale * scaleFactor;

	    if (scale < 0.15){ scale = 0.15;}
	    if (scale > 1){scale = 1.015;}

	    baseWidth = baseWidth * scale;
		baseHeight = baseHeight * scale;

	    stage.setWidth(baseWidth);
	    stage.setHeight(baseHeight);

	}
	public static boolean isSupportedRatio(){

		double resolutionX = Screen.getPrimary().getBounds().getWidth();
		double resolutionY = Screen.getPrimary().getBounds().getHeight();

		double ratio = resolutionX/resolutionY;

		if(ratio>1.7 && ratio<1.8){
			return true;
		}
		return false;

	}
}