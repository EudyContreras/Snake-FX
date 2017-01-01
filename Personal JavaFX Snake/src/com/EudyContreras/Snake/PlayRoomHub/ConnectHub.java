package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.Utilities.ShapeUtility;
import com.EudyContreras.Snake.Utilities.ShapeUtility.Center;
import com.EudyContreras.Snake.Utilities.ShapeUtility.Shape;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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
	private ScaleTransition[] scaleTransition;
	private TranslateTransition hubTransition;
	private TranslateTransition frameTransition;
	private GameButton buttons;
	private ConnectUsers connectUsers;
	private ConnectFriends connectFriends;
	private ConnectProfile connectProfile;
	private ConnectInbox connectInbox;
	private ConnectLeaderboard connectLeaderboard;
	private ConnectFrame usersFrame;
	private ConnectFrame friendsFrame;
	private ConnectFrame leaderFrame;
	private ConnectWindow connectWindow;
	private ImageView snapShot = new ImageView();
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
		this.scaleTransition = new ScaleTransition[3];
		this.hubTransition = new TranslateTransition();
		this.frameTransition = new TranslateTransition();
		this.connectFriends = new ConnectFriends(game);
		this.connectInbox = new ConnectInbox(this);
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

	public void cullRegion(Region region, boolean state) {
		if (state) {
			WritableImage writableImage = new WritableImage((int) region.getWidth(), (int) region.getHeight());
			SnapshotParameters parameters = new SnapshotParameters();
			parameters.setFill(Color.TRANSPARENT);
			region.snapshot(parameters, writableImage);
			snapShot.setImage(writableImage);
			connectWindow.removeNodeFromRegion(region);
			connectWindow.removeNodeFromRegion(snapShot);
			connectWindow.addNodeToRegion(0,snapShot);
		} else {
			connectWindow.removeNodeFromRegion(snapShot);
			connectWindow.removeNodeFromRegion(region);
			connectWindow.addNodeToRegion(0,region);
		}
	}
	
	public VBox getVRegion(){
		return vSections;
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

		buttons = new GameButton(10, "Remove","Chat", "Play");
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

		for(int i = 0; i<scaleTransition.length; i++){
			scaleTransition[i] = new ScaleTransition();
			if(i == 0){
				scaleTransition[i].setNode(usersFrame.get());
			}else if(i == 1){
				scaleTransition[i].setNode(friendsFrame.get());
			}else if(i == 2){
				scaleTransition[i].setNode(leaderFrame.get());
			}
		}

        usersFrame.get().setScaleX(0);
        usersFrame.get().setScaleY(0);

        friendsFrame.get().setScaleX(0);
        friendsFrame.get().setScaleY(0);

        leaderFrame.get().setScaleX(0);
        leaderFrame.get().setScaleY(0);

		connectWindow.addNodesToRegion(vSections);
		connectWindow.addNodesToRegion(connectInbox.get());
	}

	private void hubSection(){
		this.hubBar.setWidth(GameSettings.WIDTH+20);
		this.hubBar.setHeight(125);
		this.hubBar.setTranslateY(-hubBar.getHeight());
		this.hubBar.setFill(new ImagePattern(GameImageBank.play_room_hub_bar));
		ShapeUtility.CENTER_SHAPE(Shape.RECTANGLE, Center.CENTER_X, hubBar, new Dimension2D(GameSettings.WIDTH,GameSettings.HEIGHT));
		layer.getChildren().add(hubBar);
		layer.getChildren().add(0,window);

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

		cullRegion(vSections,true);

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

	        usersFrame.get().setScaleX(0);
	        usersFrame.get().setScaleY(0);

	        friendsFrame.get().setScaleX(0);
	        friendsFrame.get().setScaleY(0);

	        leaderFrame.get().setScaleX(0);
	        leaderFrame.get().setScaleY(0);

			for (int i = 0; i < scaleTransition.length; i++) {
				scaleTransition[i].stop();
				scaleTransition[i].setDelay(Duration.millis(0));
				scaleTransition[i].setDuration(Duration.millis(200));
				scaleTransition[i].setFromX(1);
				scaleTransition[i].setFromY(1);
				scaleTransition[i].setToX(0);
				scaleTransition[i].setToY(0);
			}

			cullRegion(vSections,false);
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

		int delay = 250;

	    vSections.setScaleX(1);
	    vSections.setScaleY(1);

		for (int i = 0; i < scaleTransition.length; i++) {
			scaleTransition[i].stop();
			scaleTransition[i].setInterpolator(Interpolator.EASE_OUT);
			scaleTransition[i].setDelay(Duration.millis(delay));
			scaleTransition[i].setDuration(Duration.millis(200));
			scaleTransition[i].setFromX(0);
			scaleTransition[i].setFromY(0);
			scaleTransition[i].setToX(1);
			scaleTransition[i].setToY(1);
			scaleTransition[i].play();
			delay+=200;
		}

		hubTransition.setDuration(Duration.millis(250));
		hubTransition.setToY(-6);
		hubTransition.play();

		frameTransition.setInterpolator(Interpolator.EASE_OUT);
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
