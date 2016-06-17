package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Utilities.GameAudio;

import javafx.geometry.Pos;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MainMenu {

	private int currentChoice = 1;
	private ImageView backgroundImage;
	private GaussianBlur blur = new GaussianBlur();
	private Pane fadeScreen = new Pane();
	private Pane menuRoot = new Pane();
	private Pane mainMenu = new Pane();
	private MenuBox menuBoxOne;
	private Rectangle clearUp;
	private Rectangle menuLogo;
	private Boolean showMenu = false;
	private Double radius = 63.0;
	private Double opacity = 1.0;
	private GameManager game;
	private MediaPlayer music;

	public MainMenu(GameManager game) {
		this.game = game;
		menuBoxOne = new MenuBox(game,0,200,500,600,20,Color.BLACK,Pos.CENTER);
		menuBoxOne.setMenuBoxCoordinates(500/2,500/2);
		backgroundImage = new ImageView(MenuImageBank.mainMenuBackground);
		clearUp = new Rectangle(0, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		clearUp.setFill(Color.BLACK);
		setupLogo();
	}
	public void setupLogo(){
		menuLogo = new Rectangle();
		menuLogo.setFill(new ImagePattern(MenuImageBank.gameLogo));
		menuLogo.setWidth(MenuImageBank.gameLogo.getWidth()/GameLoader.ResolutionScaleX);
		menuLogo.setHeight(MenuImageBank.gameLogo.getHeight()/GameLoader.ResolutionScaleY);
		menuLogo.setX(GameSettings.WIDTH/2-menuLogo.getWidth()/2);
		menuLogo.setY(30);
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

	/**
	 * Sets up the main menu
	 */
	public void setupMainMenu() {

		showMenu = true;
		menuBoxOne.addButtons(
				 new MenuButton("START GAME", Pos.CENTER, Color.WHITE, Color.LIMEGREEN, 35, 300, 60,true, Color.WHITE),
				 new MenuButton("OPTIONS", Pos.CENTER, Color.WHITE, Color.LIMEGREEN, 35, 300, 60,true, Color.WHITE),
				 new MenuButton("GAME MODES", Pos.CENTER, Color.WHITE, Color.LIMEGREEN, 35, 300, 60,true, Color.WHITE),
				 new MenuButton("HIGH SCORES", Pos.CENTER, Color.WHITE, Color.LIMEGREEN, 35, 300, 60,true, Color.WHITE),
				 new MenuButton("MULTIPLAYER", Pos.CENTER, Color.WHITE, Color.LIMEGREEN, 35, 300, 60,true, Color.WHITE),
				 new MenuButton("EXIT", Pos.CENTER, Color.WHITE, Color.LIMEGREEN, 35, 300, 60,true, Color.WHITE)
				);
		menuBoxOne.getButton(currentChoice-1).setActive(true);
		fadeScreen.getChildren().add(clearUp);
		mainMenu.getChildren().addAll(menuBoxOne.getMenu());
		menuRoot.getChildren().addAll(backgroundImage, menuLogo, mainMenu, fadeScreen);
		game.setRoot(menuRoot);
		setMouseInputHandler();
		setKeyInputHandler();
	}

	public void transition() {
		if (showMenu) {
			opacity -= 0.01;
			clearUp.setOpacity(opacity);
			if (opacity <= 0) {
				opacity = 0.0;
				menuRoot.getChildren().remove(fadeScreen);
				showMenu = false;
			}
		}

		if (radius >= 0) {
			radius -= 1;
			menuRoot.setEffect(blur);
			blur.setRadius(radius);
		}
	}

	/**
	 * Sets the key input handling for the labels
	 */
	private void setKeyInputHandler() {

		/*
		 * Code below determines what will happen if the user presses enter or
		 * space on the different choices
		 */
		game.getScene().setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case UP:
				currentChoice -= 1;
				if (currentChoice < 1) {
					currentChoice = 1;
				}
				break;
			case DOWN:
				currentChoice += 1;
				if (currentChoice > 6) {
					currentChoice = 6;
				}
				break;
			case W:
				currentChoice -= 1;
				if (currentChoice < 1) {
					currentChoice = 1;
				}
				break;
			case S:
				currentChoice += 1;
				if (currentChoice > 6) {
					currentChoice = 6;
				}
				break;
			case ENTER:
				menuBoxOne.getButton(currentChoice-1).activate();
				break;
			case SPACE:
				menuBoxOne.getButton(currentChoice-1).activate();
			default:
				break;
			}
			for(int i = 0; i<menuBoxOne.getButtons().size(); i++){
				if(i!=currentChoice-1){
					menuBoxOne.getButton(i).setActive(false);
				}
				else{
					menuBoxOne.getButton(i).setActive(true);
				}
			}
		});
		game.getScene().setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case ENTER:
					menuBoxOne.getButton(currentChoice-1).activate();
					menuBoxOne.getButton(currentChoice-1).deactivate();
				break;
			case SPACE:
					menuBoxOne.getButton(currentChoice-1).activate();
					menuBoxOne.getButton(currentChoice-1).deactivate();
				break;
			default:
				break;
			}
		});
	}

	public void setMouseInputHandler(){
		menuBoxOne.getButton(0).setAction(() -> startSelected());
		menuBoxOne.getButton(5).setAction(() -> closeGame());
		for(int i = 0; i<menuBoxOne.getButtons().size(); i++){
			final int index = i;
			menuBoxOne.getButtons().get(i).setOnHover(() -> resetSelections(index));
		}
	}

	public void resetSelections(int index) {
		currentChoice = index + 1;
		for (int i = 0; i < menuBoxOne.getButtons().size(); i++) {
			if (i != currentChoice - 1) {
				menuBoxOne.getButton(i).setActive(false);
			} else {
				menuBoxOne.getButton(i).setActive(true);
			}
		}
	}
	/**
	 * Sets up key input handling and mouse input handling for the main menu.
	 * Shows the cursor, sets the root of the game to menu root and pauses the
	 * game.
	 */
	public void setMainMenu() {
		menuBoxOne.getButton(currentChoice-1).setActive(true);
		menuRoot.getChildren().add(fadeScreen);
		showMenu = true;
		opacity = 1.0;
		clearUp.setOpacity(opacity);
		setKeyInputHandler();
		game.showCursor(true, game.getScene());
		game.setRoot(menuRoot);
		game.pauseGame();
	}

	/**
	 * Starts the game if the startbutton is pressed
	 */
	private void startSelected() {
		game.resumeGame();
		menuRoot.getChildren().remove(fadeScreen);
		game.showCursor(false, game.getScene());
		game.setRoot(game.getMainRoot());
		game.processGameInput();
		game.getCountDownScreen().startCountdown();
	}

	/**
	 * Sets up the optionsmenu
	 */
	@SuppressWarnings("unused")
	private void OptionsMenu() {

	}

	@SuppressWarnings("unused")
	private void multiplayerMenu() {

	}

	public void setCurrentChoice(int choice) {
		currentChoice = choice;
	}

	private void closeGame() {
		game.closeGame();
	}

	public Pane getMenuRoot() {
		return menuRoot;
	}

	public void setMenuRoot(Pane menuRoot) {
		this.menuRoot = menuRoot;
	}
}
