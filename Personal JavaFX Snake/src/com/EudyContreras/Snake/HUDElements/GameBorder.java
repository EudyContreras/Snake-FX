package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.CursorUtility;
import com.EudyContreras.Snake.FrameWork.CursorUtility.CursorID;
import com.EudyContreras.Snake.FrameWork.ResizeHelper;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Bounds class which can be used for adding specific events to objects
 * that surround the level
 *
 * @author Eudy Contreras
 *
 */
public class GameBorder extends AbstractTile {

	private GameManager game;

	private StackPane pane;
	private ImageView leftBound;
	private ImageView rightBound;
	private ImageView topBound;
	private ImageView bottomBound;
	private ImageView exit;

	private DropShadow glow;
	private Group layer;

	private double borderSize = 20;
	protected double yOffset = 0;
	protected double xOffset = 0;

	public GameBorder(GameManager game, Group layer) {
		this.game = game;
		this.velX = 0;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		this.layer = layer;
		setupBorders();
		showBorders(false);
		handleTopBarEvents();
	}
	public void setupBorders(){
		this.glow = new DropShadow(BlurType.THREE_PASS_BOX, Color.RED, 25, 0.5, 0, 0);
		this.pane = new StackPane();
		this.exit = new ImageView(GameImageBank.Exit);
		this.leftBound = new ImageView(GameImageBank.vertical_border);
		this.rightBound = new ImageView(GameImageBank.vertical_border);
		this.topBound = new ImageView(GameImageBank.horizontal_border_alt);
		this.bottomBound = new ImageView(GameImageBank.horizontal_border);

		this.setDimensions_h(bottomBound);
		this.setDimensions_h(topBound,60);
		this.setDimensions_v(leftBound);
		this.setDimensions_v(rightBound);

		this.leftBound.setX(0);
		this.rightBound.setX(GameSettings.SCREEN_WIDTH - borderSize);
		this.leftBound.setY(3);
		this.rightBound.setY(3);
		this.topBound.setTranslateX(3);
		this.topBound.setTranslateY(0);
		this.bottomBound.setX(-5);
		this.bottomBound.setY(GameSettings.SCREEN_HEIGHT - borderSize);
		this.exit.setTranslateX(GameSettings.SCREEN_WIDTH/2 - 45);
		this.exit.setFitWidth(100*.7);
		this.exit.setFitHeight(47*.7);
		this.pane.getChildren().setAll(topBound, new ImageView(GameImageBank.snakeIcon), exit);
		this.displayBorders();
	}
	public void setDimensions_h(ImageView view){
		view.setFitWidth(GameSettings.SCREEN_WIDTH+5);
		view.setFitHeight(borderSize+5);
	}
	public void setDimensions_h(ImageView view, double height){
		view.setFitWidth(GameSettings.SCREEN_WIDTH-6);
		view.setFitHeight(height);
	}
	public void setDimensions_v(ImageView view){
		view.setFitWidth(borderSize);
		view.setFitHeight(GameSettings.SCREEN_HEIGHT-3);
	}
	public void setDimensions_v(ImageView view, double width){
		view.setFitWidth(width);
		view.setFitHeight(GameSettings.SCREEN_HEIGHT);
	}
	private void handleTopBarEvents(){
		exit.setOnMouseEntered(e ->{
			exit.setEffect(glow);
		});
		exit.setOnMouseExited(e ->{
			exit.setEffect(null);
		});
		exit.setOnMouseClicked(e ->{
			game.closeGame();
		});

	 	topBound.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	CursorUtility.setCursor(CursorID.DRAG, game.getScene());
                xOffset = game.getMainWindow().getX() - event.getScreenX();
                yOffset = game.getMainWindow().getY() - event.getScreenY();
            }
        });
	 	topBound.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	CursorUtility.setCursor(CursorID.DRAG, game.getScene());
            	game.getMainWindow().setX(event.getScreenX() + xOffset);
            	game.getMainWindow().setY(event.getScreenY() + yOffset);
            }
        });
	 	topBound.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	CursorUtility.setCursor(CursorID.NORMAL, game.getScene());
            }
        });
	 	topBound.setOnMouseReleased(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				CursorUtility.setCursor(CursorID.NORMAL, game.getScene());
			}

	 	});
	 	topBound.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            	CursorUtility.setCursor(CursorID.NORMAL, game.getScene());
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                    	if(!game.getMainWindow().isFullScreen() && ResizeHelper.isSupportedRatio()){
							game.setNewRatio(true);
							game.getMainWindow().setFullScreen(true);
							game.getGameBorder().showBorders(false);
						}
						else{
							game.setNewRatio(false);
							game.getMainWindow().setFullScreen(false);
							game.getGameBorder().showBorders(true);
						}
                    }
                }
            }
        });
	}
	public void displayBorders(){
		this.layer.getChildren().add(bottomBound);
		this.layer.getChildren().add(leftBound);
		this.layer.getChildren().add(rightBound);
		this.layer.getChildren().add(pane);

	}
	public void showBorders(boolean state){
		this.leftBound.setVisible(state);
		this.rightBound.setVisible(state);
		this.pane.setVisible(state);
		this.bottomBound.setVisible(state);
	}
	public void removeBorders(){
		this.layer.getChildren().remove(leftBound);
		this.layer.getChildren().remove(rightBound);
		this.layer.getChildren().remove(pane);
		this.layer.getChildren().remove(bottomBound);
	}
	public void reAddBorders(){
		this.layer.getChildren().add(bottomBound);
		this.layer.getChildren().add(leftBound);
		this.layer.getChildren().add(rightBound);
		this.layer.getChildren().add(pane);
	}
}
