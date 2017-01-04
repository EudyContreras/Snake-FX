package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.AbstractModels.AbstractMenuElement;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.PlayRoomHub.ConnectHub;
import com.EudyContreras.Snake.Utilities.GameAudio;
import com.EudyContreras.Snake.Utilities.TimePeriod;
import com.EudyContreras.Snake.Utilities.ValueAnimator;
import com.EudyContreras.Snake.Utilities.ValueWrapper;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MenuManager extends AbstractMenuElement{

	private Rectangle backgroundImage;
	private Pane hubLayer = new Pane();
	private Pane menuRoot = new Pane();
	private Pane menuContainer = new Pane();
	private Pane mainContainer = new Pane();
	private BoxBlur focus = new BoxBlur();
	private ConnectHub connectHub;
	private MediaPlayer music;
	private MenuMain main_menu;
	private MenuModes modes_menu;

	public MenuManager(GameManager game) {
		this.game = game;
		setUpBackground();
		setupLogo();
		setUpMenus();
		addFXConnect();
	}

	private void addFXConnect() {
		connectHub = new ConnectHub(game,hubLayer);
		focus.setIterations(2);
		mainContainer.setEffect(focus);
	}

	public void showFXConnect(){
		if(connectHub.isShowing()){
			connectHub.swipeUp(()->onFocus());
		}else{
			showMenu = false;
			hideMenu = false;
			connectHub.swipeDown(()->offFocus());
		}
	}

	public void showFXConnect(boolean state){
		if(state){
			showMenu = false;
			hideMenu = false;
			connectHub.swipeDown(()->offFocus());
		}else{
			connectHub.swipeUp(()->onFocus());
		}
	}

	public void onFocus(){
		ValueAnimator animator = new ValueAnimator();

		animator.onUpdate(value->{
			mainContainer.setOpacity(value);
		});
		animator.setDuration(TimePeriod.millis(250));
		animator.setFrom(0.1);
		animator.setTo(1.0);
		animator.setDelay(100);
		animator.play();
	}

	private void offFocus(){
		ValueAnimator animator = new ValueAnimator();

		animator.onUpdate(value->{
			mainContainer.setOpacity(value);
		});
		animator.setDuration(TimePeriod.millis(250));
		animator.setFrom(1.0);
		animator.setTo(0.1);
		animator.play();


	}

	public ConnectHub getConnectHub(){
		return connectHub;
	}

	public void setUpMenus(){
		main_menu = new MenuMain(game,this);
		modes_menu = new MenuModes(game, this);
	}

	public void setUpBackground(){
		backgroundImage = new Rectangle(20, 20, GameSettings.WIDTH-40, GameSettings.HEIGHT-40);
//		mainContainer.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
//		mainContainer.setMinHeight(GameSettings.HEIGHT);
//		mainContainer.setMinWidth(GameSettings.WIDTH);
	}

	public void setupMainMenu() {
		showMenu = true;
		menuRoot.setMaxWidth(GameSettings.WIDTH);
		menuRoot.setMaxHeight(GameSettings.HEIGHT);
		setMenu(main_menu.main_menu_screen());
		mainContainer.getChildren().addAll(backgroundImage, menuLogo, menuContainer);
		menuRoot.getChildren().addAll(mainContainer, hubLayer);
		game.setRoot(menuRoot);
	}

	public void setupLogo(){
		menuLogo = new Rectangle();
		glowLED = new DropShadow();
		glowLED.setColor(Color.LIME);
		glowLED.setBlurType(BlurType.GAUSSIAN);
		glowLED.setRadius(25);
		glowLED.setSpread(0.3);
		menuLogo.setFill(new ImagePattern(MenuImageBank.gameLogo));
		menuLogo.setEffect(glowLED);
		menuLogo.setWidth((MenuImageBank.gameLogo.getWidth()*1.3));
		menuLogo.setHeight((MenuImageBank.gameLogo.getHeight()*1.3));
		menuLogo.setX((GameSettings.WIDTH/2-menuLogo.getWidth()/2));
		menuLogo.setY(0);
	}

	public void addMusic() {
		music = GameAudio.getAudio("AudioResources/Jungle Loop.wav");
		music.play();
		music.setCycleCount(MediaPlayer.INDEFINITE);
	}

	public void playMusic() {
		if(music.getStatus() == Status.STOPPED){
		music = GameAudio.getAudio("AudioResources/Jungle Loop.wav");
		music.play();
		music.setCycleCount(MediaPlayer.INDEFINITE);
		}
	}

	public void stopMusic() {
		if(music.getStatus() == Status.PLAYING){
		music.stop();
		}
	}

	public void setMenu(Pane menu){
		menuContainer.getChildren().clear();
		menuContainer.getChildren().add(menu);
	}

	public void removeMenu(Pane menu){
		if(menuContainer.getChildren().contains(menu))
		menuContainer.getChildren().remove(menu);
	}

	public void transition() {
		if (showMenu) {
			opacity += fadeSpeed;
			mainContainer.setOpacity(opacity);
			if (opacity >= 1) {
				opacity = 1.0;
				showMenu = false;
			}
		}
		if (hideMenu) {
			opacity -= fadeSpeed;
			if(opacity >= 0){
				mainContainer.setOpacity(opacity);
			}
			if (opacity < 0) {
				removeMenu(main_menu.main_menu_screen());
				removeMenu(modes_menu.modes_menu_screen());
				menuLogo.setVisible(false);
			}
			if (opacity <= -1.0) {
				opacity = -1.0;
				introTheGame();
				hideMenu = false;
			}
		}
		if (radius >= 0) {
			radius -= 1;
			mainContainer.setEffect(focus);
			focus.setWidth(radius);
			focus.setHeight(radius);
		}
	}


	/**
	 * Sets up key input handling and mouse input handling for the main menu.
	 * Shows the cursor, sets the root of the game to menu root and pauses the
	 * game.
	 */
	public void setMainMenu() {
		menuRoot.setMaxWidth(GameSettings.WIDTH);
		menuRoot.setMaxHeight(GameSettings.HEIGHT);
		menuLogo.setVisible(true);
		showMenu = true;
		startingGame = false;
		fadeSpeed = 0.01;
		opacity = 0.0;
		mainContainer.setOpacity(opacity);
		setMenu(main_menu.main_menu_screen());
		game.showCursor(true, game.getScene());
		game.setRoot(menuRoot);
		game.pauseGame();
	}

	/**
	 * Starts the game if the startbutton is pressed
	 */
	public void startSelected(GameModeID modeID) {
		if(!startingGame){
			if (opacity <= 0) {
				this.startingGame = true;
				this.modeID = modeID;
				this.fadeSpeed = 0.05;
				this.opacity = 0;

				this.mainContainer.setOpacity(opacity);
				this.hideMenu = true;
			}
			else if(opacity > 0){
				this.fadeSpeed = 0.03;
				this.showMenu = false;
				this.startingGame = true;
				this.modeID = modeID;
				this.hideMenu = true;
			}
		}
	}

	public void introTheGame() {
		if (showMenu == false) {
			// TODO: Show a loading screen
			game.setModeID(modeID);
			game.prepareGame();
			game.resumeGame();
			game.showCursor(false, game.getScene());
			game.setRoot(game.getMainRoot());
			game.getFadeScreenHandler().intro_fade_screen(() -> game.getReadyNotification().showNotification(60));
			game.processGameInput();
			hideMenu = false;
		}

	}
	/**
	 * Sets up the optionsmenu
	 */
	public void optionsMenu() {

	}

	public void multiplayerMenu() {

	}

	public void gameModesMenu(){
		setMenu(modes_menu.modes_menu_screen());
	}

	public void setCurrentChoice(int choice) {
		currentChoice = choice;
	}

	public void closeGame() {
		game.closeGame();
	}

	public void goBack(){
		setMenu(main_menu.main_menu_screen());
	}

	public Pane getMenuRoot() {
		return menuRoot;
	}

	public void setMenuRoot(Pane menuRoot) {
		this.menuRoot = menuRoot;
	}
}
