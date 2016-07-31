package com.EudyContreras.Snake.FrameWork;

import com.EudyContreras.Snake.FrameWork.CursorUtility.CursorID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

public class ResizeHelper {



	public static void addSceneManipulation(GameManager game,Stage stage, Scene scene) {

		GestureListener gestureListener = new GestureListener(game,stage,scene);

		stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, gestureListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, gestureListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, gestureListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, gestureListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, gestureListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED, gestureListener);

	}
	private static class GestureListener implements EventHandler<MouseEvent> {

		private GameManager game;
		private final Scene scene;
		private final Stage stage;
		private double factor = 1.0;
		private double border = 5;
		private double baseWidth = 0;
		private double baseHeight = 0;
		private double yOffset = 0;
		private double xOffset = 0;
		private double stretchContextX = 0;
		private double stretchContextY = 0;

		private Cursor cursorEvent = Cursor.DEFAULT;

		public GestureListener(GameManager game, Stage stage, Scene scene) {
			this.game = game;
			this.scene = scene;
			this.stage = stage;
			this.handleGestures();
		}
		private boolean contextDrag(MouseEvent e){
			boolean state = e.getSceneX()>20 && e.getSceneY()>20 && e.getSceneX()<scene.getWidth()-20 && e.getSceneY()<scene.getHeight()-20;
			return state;
		}
		private void handleGestures() {

			baseWidth = GameSettings.SCREEN_WIDTH-100;
			baseHeight = GameSettings.SCREEN_HEIGHT-58;

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
		@Override
		public void handle(MouseEvent mouseEvent) {
			EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
			Scene scene = stage.getScene();

			double mouseEventX = mouseEvent.getSceneX();
			double mouseEventY = mouseEvent.getSceneY();
			double sceneWidth = scene.getWidth();
			double sceneHeight = scene.getHeight();


			if (!stage.isFullScreen()) {

				if (MouseEvent.MOUSE_MOVED.equals(mouseEventType) == true) {
					if (mouseEventX > sceneWidth - border*6 && mouseEventY > sceneHeight - border*6) {
						cursorEvent = GameImageBank.diagonalDragNW_SE;
					} else if (mouseEventX > sceneWidth - border*6) {
						cursorEvent = GameImageBank.horizontalDrag;
					} else if (mouseEventY > sceneHeight - border*6) {
						cursorEvent = GameImageBank.verticalDrag;
					} else {
						cursorEvent = GameImageBank.normalCursor;
					}
						scene.setCursor(cursorEvent);

				}
				else if (MouseEvent.MOUSE_EXITED.equals(mouseEventType) || MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType)) {
					CursorUtility.setCursor(CursorID.NORMAL, scene);
				}
				else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType) == true) {

					if(!mouseEvent.isControlDown()){
						xOffset = stage.getX() - mouseEvent.getScreenX();
						yOffset = stage.getY() - mouseEvent.getScreenY();

						stretchContextX = mouseEvent.getSceneX();
						stretchContextY = mouseEvent.getSceneY();
					}
				}
				else if (MouseEvent.MOUSE_CLICKED.equals(mouseEventType)){
					stretchContextX = mouseEvent.getSceneX();
					stretchContextY = mouseEvent.getSceneY();
					if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
	                    if (mouseEvent.getClickCount() == 2) {
	                    	if(!stage.isFullScreen()){
	                        stage.setFullScreen(true);
	                    	}
	                    	else if(stage.isFullScreen()){
	                    	stage.setFullScreen(false);
	                    	}
	                    }
	                }
				}
				else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) == true) {


						if (!stage.isFullScreen() && !mouseEvent.isControlDown()) {
							if(contextDrag(mouseEvent)){
								CursorUtility.setCursor(CursorID.DRAG, scene);
								stage.setX(mouseEvent.getScreenX() + xOffset);
								stage.setY(mouseEvent.getScreenY() + yOffset);
							}
							else{

//								if(mouseEvent.getSceneX()>stretchContextX){
//									factor = 1.001;
//								}
//								else if(mouseEvent.getSceneX()<stretchContextX){
//									factor = 0.999;
//								}
//								if(mouseEvent.getSceneY()>stretchContextY){
//									factor = 1.001;
//								}
//								else if(mouseEvent.getSceneY()<stretchContextY){
//									factor = 0.999;
//								}
//
//								double aspectRatioX = factor > 1 ? 1.05 : 0.995;
//								double aspectRatioY = factor > 1 ? 1.05 : 0.995;
//
//								baseWidth = baseWidth * aspectRatioX;
//								baseHeight = baseHeight * aspectRatioY;
//
//								stage.setWidth(baseWidth);
//								stage.setHeight(baseHeight);
//
//								if(baseWidth>GameSettings.SCREEN_WIDTH+50 && baseHeight>GameSettings.SCREEN_HEIGHT){
//									stage.setWidth(GameSettings.SCREEN_WIDTH);
//									stage.setHeight(GameSettings.SCREEN_HEIGHT);
//									baseWidth = GameSettings.SCREEN_WIDTH-100;
//									baseHeight = GameSettings.SCREEN_HEIGHT - 58;
//									stage.setFullScreen(true);
//								}
							}

					}
				}
				else if(MouseEvent.MOUSE_RELEASED.equals(mouseEventType)){

				}
			}
		}
		public void zoom(Stage stage, double scaleFactor, double x, double y) {

		    double oldScale = game.getRootLayer().getScaleX();
		    double scale = oldScale * scaleFactor;

		    if (scale < 0.15){ scale = 0.15;}
		    if (scale > 1){scale = 1.015;}

		    baseWidth = baseWidth * scale;
			baseHeight = baseHeight * scale;

		    stage.setWidth(baseWidth);
		    stage.setHeight(baseHeight);

		}

		public void zoom(Stage stage, ScrollEvent event) {
		    zoom(stage, Math.pow(1.001, event.getDeltaY()), event.getSceneX(), event.getSceneY());
		}
	}

}