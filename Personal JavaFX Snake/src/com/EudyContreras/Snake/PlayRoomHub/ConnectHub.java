package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.Utilities.ShapeUtility;
import com.EudyContreras.Snake.Utilities.ShapeUtility.Center;
import com.EudyContreras.Snake.Utilities.ShapeUtility.Shape;
import javafx.animation.TranslateTransition;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
	private Group window;
	private HBox hSections;
	private VBox vSections;
	private GameManager game;
	private TranslateTransition hubTransition;
	private TranslateTransition frameTransition;
	private ConnectUsers connectUsers;
	private ConnectFriends connectFriends;
	private ConnectProfile connectProfile;
	private ConnectLeaderboard connectLeaderboard;
	private ConnectFrame usersFrame;
	private ConnectFrame friendsFrame;
	private ConnectFrame leaderFrame;
	private ConnectWindow connectWindow;
	private DropShadow shadow = new DropShadow();
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
		this.connectFriends = new ConnectFriends(game);
		this.connectLeaderboard = new ConnectLeaderboard(game);
		this.connectWindow = new ConnectWindow(game);
		this.connectUsers = new ConnectUsers(game);
		this.friendsFrame = new ConnectFrame(game);
		this.usersFrame = new ConnectFrame(game);
		this.leaderFrame = new ConnectFrame(game);
		this.window = new Group();
		this.hSections = new HBox(20);
		this.vSections = new VBox(20);
		this.hubTransition.setNode(hubBar);
		this.frameTransition.setNode(connectWindow);
		this.window.getChildren().add(connectWindow);
		this.initSections();
	}

	private void initSections(){
		hubSection();
		profileSection();
	}

	private void profileSection(){
		friendsFrame.setLabel("Friends");
		friendsFrame.addRegion(connectFriends.get());
		leaderFrame.setLabel("Leaderboard");
		leaderFrame.addRegion(connectLeaderboard.get());
        usersFrame.addRegion(connectUsers.get());
        friendsFrame.setFrameSize(connectFriends.getWidth()+20, connectFriends.getHeight()+135);
        usersFrame.setFrameSize(connectUsers.getWidth()+20, connectUsers.getHeight()+100);
        leaderFrame.setFrameSize(connectLeaderboard.getWidth()+20, connectLeaderboard.getHeight()+100);
		connectProfile = new ConnectProfile(game,"Eudy Contreras","28","Sweden");
		GameButton buttons = new GameButton(10, "Remove","Chat", "Play");
		buttons.setIDToAll("button");
		buttons.setID("Remove", "btnUnfriend");
		buttons.setFontToAll(Font.font(null, FontWeight.BOLD, 15));
		buttons.setWidthToAll(120);
		friendsFrame.get().getStylesheets().add(ConnectFriends.class.getResource("connectButtons.css").toExternalForm());
		friendsFrame.setButtons(buttons);

		hSections.getChildren().add(usersFrame.get());
		hSections.getChildren().add(friendsFrame.get());
		vSections.getChildren().add(hSections);
		vSections.getChildren().add(leaderFrame.get());
		connectWindow.addNodeToRegion(vSections);
	}



	private void hubSection(){
		this.hubBar.setWidth(GameSettings.WIDTH+20);
		this.hubBar.setHeight(125);
		this.hubBar.setTranslateY(-hubBar.getHeight());
		this.hubBar.setFill(new ImagePattern(GameImageBank.play_room_hub_bar));
		ShapeUtility.CENTER_SHAPE(Shape.RECTANGLE, Center.CENTER_X, hubBar, new Dimension2D(GameSettings.WIDTH,GameSettings.HEIGHT));
		layer.getChildren().add(hubBar);
	}

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
		frameTransition.setToY(-(connectWindow.getHeight()+30));
		frameTransition.play();

		hubTransition.setOnFinished(e-> {
			finished = true;
			showing = false;
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

		finished = false;

		hubTransition.setDuration(Duration.millis(250));
		hubTransition.setToY(-6);
		hubTransition.play();

		frameTransition.setDuration(Duration.millis(250));
		frameTransition.setToY(-40);
		frameTransition.play();

		hubTransition.setOnFinished(e->{
			finished = true;
			showing = true;
			connectUsers.getTable().requestFocus();
	        connectUsers.getTable().getSelectionModel().clearAndSelect(0);
	        connectUsers.getTable().getFocusModel().focus(0);
	        connectUsers.getTable().getSelectionModel().selectFirst();
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
