package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.UserInterface.MenuButtonStyles;
import com.EudyContreras.Snake.Utilities.GradientAnimator;
import com.EudyContreras.Snake.Utilities.ShapeUtility;
import com.EudyContreras.Snake.Utilities.ShapeUtility.Center;
import com.EudyContreras.Snake.Utilities.ShapeUtility.Shape;

import javafx.animation.TranslateTransition;
import javafx.geometry.Dimension2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * This class simulates a simple game heads up display which is shown at the to the level
 * this display shows useful information concerning game statistics
 * @author Eudy Contreras
 *
 */
public class ConnectHub {

	private double moveY = 0;
	private double swipeSpeed = 0;
	private double hideY = 0;
	private double hideVelY = 0;
	private boolean finished = true;
	private boolean showHub = false;
	private boolean showing = false;
	private Pane layer;
	private Pane window;
	private GameManager game;
	private GradientAnimator gradientAnim;
	private TranslateTransition hubTransition;
	private TranslateTransition frameTransition;
	private DropShadow shadow = new DropShadow();
	private Rectangle frame = new Rectangle();
	private Rectangle hubBar = new Rectangle();

	/**
	 * Constructor which takes the main class as a parameter along with the coordintate
	 * and dimensions of Hud element.
	 * @param game: Main game class which connects this class to all others
	 * @param x: X coordinate of this element
	 * @param y: Y coordinate of this element
	 * @param width: Horizontal dimension of this element
	 * @param height: Vertical dimension of this element
	 */

	public ConnectHub(GameManager game, Pane layer) {
		this.moveY = -5;
		this.game = game;
		this.layer = layer;
		this.shadow.setColor(Color.rgb(0, 0, 0, 0.5));
		this.shadow.setRadius(5);
		this.shadow.setOffsetX(0);
		this.shadow.setOffsetY(15);
		this.hubTransition = new TranslateTransition();
		this.frameTransition = new TranslateTransition();
		this.gradientAnim = new GradientAnimator();
		this.window = new Pane();
		this.hubTransition.setNode(hubBar);
		this.frameTransition.setNode(frame);
		this.gradientAnim.setNode(frame);
		this.initHub();
		this.initFrame();
	}

	private void initHub(){
		this.hubBar.setWidth(GameSettings.WIDTH+20);
		this.hubBar.setHeight(125);
		this.hubBar.setTranslateY(-hubBar.getHeight());
		this.hubBar.setFill(new ImagePattern(GameImageBank.play_room_hub_bar));
		ShapeUtility.CENTER_SHAPE(Shape.RECTANGLE, Center.CENTER_X, hubBar, new Dimension2D(GameSettings.WIDTH,GameSettings.HEIGHT));
		layer.getChildren().add(hubBar);
	}

	private void initFrame(){
		this.frame.setWidth(1200);
		this.frame.setHeight(900);
		this.frame.setStyle(MenuButtonStyles.FX_CONNECT_BOX_STYLE);
		this.frame.setStroke(Color.rgb(215, 215, 215));
		this.frame.setStrokeWidth(15);
		this.frame.setFill(Color.BLACK);
		this.frame.setArcHeight(50);
		this.frame.setArcWidth(50);
		this.frame.setTranslateY(-frame.getHeight()-50);
		this.window.getChildren().add(frame);
		ShapeUtility.CENTER_SHAPE(Shape.RECTANGLE, Center.CENTER_X, frame, new Dimension2D(GameSettings.WIDTH,GameSettings.HEIGHT));
	}
	/**
	 * Method which updates the movement of
	 * both the top and the bottom HUD bar
	 *
	 */
	public void updateHudBar() {
		moveY = moveY + swipeSpeed;
		if (showHub) {
			swipeSpeed = 2.8;
			if (moveY >= -5) {
				swipeSpeed = 0;
			}

		}
		if (!showHub) {
			swipeSpeed = -2.8;
			if (moveY < 0 - (hubBar.getHeight())) {
				swipeSpeed = 0;
			}
		}
		hubBar.setTranslateY(moveY);
		updateMovement();
	}

	public void updateMovement(){
		hideY+=hideVelY;
		if(hideY<-80){
			stopMoving();
		}
		if(hideY >0){
			hideY = 0;
		}
	}

	public void swipeUp(Runnable runnable){
		if(!finished)
			return;

		if(runnable!=null){
			runnable.run();
		}
		finished = false;

		hubTransition.setDuration(Duration.millis(250));
		hubTransition.setToY(-hubBar.getHeight());
		hubTransition.play();

		frameTransition.setDuration(Duration.millis(250));
		frameTransition.setToY(-(frame.getHeight()+30));
		frameTransition.play();

		hubTransition.setOnFinished(e-> {
			finished = true;
			showing = false;
			gradientAnim.stop();
			if(layer.getChildren().contains(window)){
				layer.getChildren().remove(window);
			}
		});
	}

	public void swipeDown(Runnable runnable){
		if(!finished)
			return;

		if(runnable!=null){
			runnable.run();
		}

		if(!layer.getChildren().contains(window)){
			layer.getChildren().add(0,window);
		}
		gradientAnim.play();

		finished = false;

		hubTransition.setDuration(Duration.millis(250));
		hubTransition.setToY(-6);
		hubTransition.play();

		frameTransition.setDuration(Duration.millis(250));
		frameTransition.setToY(0);
		frameTransition.play();

		hubTransition.setOnFinished(e->{
			finished = true;
			showing = true;
		});

	}

	public void stopMoving(){
		hideVelY = 0;
	}
	/**
	 * Method which shows the HUD bar
	 * cover
	 */
	public void showHub(boolean state) {
		this.showHub = state;
	}
	/**
	 * Method which sets the visibility
	 * of all the hud elements in this class
	 * to false
	 */
	public void visible(boolean state) {
		hubBar.setVisible(state);
	}

	public void showShadow(boolean state){
		hubBar.setEffect( state? shadow: null);
	}

	public double getTopCoverY(){
		return hubBar.getY();
	}

	public boolean isShowing() {
		return showing;
	}

}
