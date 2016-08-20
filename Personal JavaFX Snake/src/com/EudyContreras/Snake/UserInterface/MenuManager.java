package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.AbstractModels.AbstractMenuElement;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.Utilities.GameAudio;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MenuManager extends AbstractMenuElement{

	private Rectangle backgroundImage;
	private Pane fadeScreen = new Pane();
	private Pane menuRoot = new Pane();
	private Pane menuContainer = new Pane();
	private GaussianBlur blur = new GaussianBlur();
	private MediaPlayer music;
	private MainMenu main_menu;
	private ModesMenu modes_menu;

	public MenuManager(GameManager game) {
		this.game = game;
		setUpBackground();
		setupLogo();
		setUpMenus();
	}
	public void setUpMenus(){
		main_menu = new MainMenu(game,this);
		modes_menu = new ModesMenu(game, this);

	}
	public void setUpBackground(){
		backgroundImage = new Rectangle(20, 20, GameSettings.WIDTH-40, GameSettings.HEIGHT-40);
		clearUp = new Rectangle(0, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		clearUp.setPickOnBounds(false);
		clearUp.setFill(Color.BLACK);
	}
	public void setupMainMenu() {
		showMenu = true;
		fadeScreen.getChildren().add(clearUp);
		fadeScreen.setPickOnBounds(false);
		setMenu(main_menu.main_menu_screen());
		menuRoot.getChildren().addAll(backgroundImage, menuLogo, menuContainer, fadeScreen);
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
		menuLogo.setY(25);
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
			opacity -= fadeSpeed;
			clearUp.setOpacity(opacity);
			if (opacity <= 0) {
				opacity = 0.0;
				menuRoot.getChildren().remove(fadeScreen);
				showMenu = false;
			}
		}
		if (hideMenu) {
			opacity += fadeSpeed;
			if(opacity<=1){
				clearUp.setOpacity(opacity);
			}
			if (opacity >= 1.0) {
				removeMenu(main_menu.main_menu_screen());
				removeMenu(modes_menu.modes_menu_screen());
				menuLogo.setVisible(false);
			}
			if (opacity >=1.5) {
				opacity = 1.0;
				introTheGame();
				hideMenu = false;
			}
		}
		if (radius >= 0) {
			radius -= 1;
			menuRoot.setEffect(blur);
			blur.setRadius(radius);
		}
	}


	/**
	 * Sets up key input handling and mouse input handling for the main menu.
	 * Shows the cursor, sets the root of the game to menu root and pauses the
	 * game.
	 */
	public void setMainMenu() {
		menuLogo.setVisible(true);
		menuRoot.getChildren().add(fadeScreen);
		showMenu = true;
		startingGame = false;
		fadeSpeed = 0.01;
		opacity = 1.0;
		clearUp.setOpacity(opacity);
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
				this.clearUp.setOpacity(opacity);
				this.menuRoot.getChildren().remove(fadeScreen);
				this.menuRoot.getChildren().add(fadeScreen);
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
			menuRoot.getChildren().remove(fadeScreen);
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
