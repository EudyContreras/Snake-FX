package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.UserInterface.MenuButton.ButtonStyle;
import com.EudyContreras.Snake.Utilities.GameAudio;
import com.EudyContreras.Snake.Utilities.PaintUtility;

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

	private final static Pane MAIN_MENU = new Pane();
	private final static Pane GAME_MODE_MENU = new Pane();
	private final static MenuBox MAIN_MENU_BOX = new MenuBox(0,200,500,700,30,Color.BLACK,Pos.CENTER);
	private final static MenuBox MODES_MENU_BOX = new MenuBox(0,200,500,700,30,Color.BLACK,Pos.CENTER);
	private int currentChoice = 1;
	private ImageView backgroundImage;
	private GaussianBlur blur = new GaussianBlur();
	private Pane fadeScreen = new Pane();
	private Pane menuRoot = new Pane();
	private Pane menuContainer = new Pane();
	private Rectangle clearUp;
	private Rectangle menuLogo;
	private boolean showMenu = false;
	private double radius = 63.0;
	private double opacity = 1.0;;
	private GameManager game;
	private MediaPlayer music;

	public MainMenu(GameManager game) {
		this.game = game;
		backgroundImage = new ImageView(MenuImageBank.mainMenuBackground);
		clearUp = new Rectangle(0, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		clearUp.setFill(Color.BLACK);
		setupLogo();
		setUpMenus();
	}
	public void setUpMenus(){
		MAIN_MENU_BOX.setMenuBoxCoordinates(500/2,450/2);
		MAIN_MENU_BOX.addButtons(
				 new MenuButton("START GAME", Pos.CENTER, ButtonStyle.black, 50, true, Color.WHITE),
				 new MenuButton("OPTIONS", Pos.CENTER, ButtonStyle.black, 50, true, Color.WHITE),
				 new MenuButton("GAME MODES", Pos.CENTER, ButtonStyle.black, 50, true, Color.WHITE),
				 new MenuButton("HIGH SCORES", Pos.CENTER, ButtonStyle.black, 50, true, Color.WHITE),
				 new MenuButton("MULTIPLAYER", Pos.CENTER, ButtonStyle.black, 50, true, Color.WHITE),
				 new MenuButton("EXIT",Pos.CENTER, ButtonStyle.black, 50, true, Color.WHITE)
				);
		MODES_MENU_BOX.setMenuBoxCoordinates(500/2,450/2);
		MODES_MENU_BOX.addButtons(
				 new MenuButton("CLASSIC MODE",Pos.CENTER, ButtonStyle.black, 50, true, Color.WHITE),
				 new MenuButton("TIME MODE", Pos.CENTER, ButtonStyle.black, 50, true, Color.WHITE),
				 new MenuButton("MULTIPLAYER", Pos.CENTER, ButtonStyle.black, 50, true, Color.WHITE),
				 new MenuButton("GO BACK", Pos.CENTER, ButtonStyle.black, 50, true, Color.WHITE)
				);
	}
	public void setupMainMenu() {
		showMenu = true;
		fadeScreen.getChildren().add(clearUp);
		setMenu(mainMenuScreen());
		menuRoot.getChildren().addAll(backgroundImage, menuLogo, menuContainer, fadeScreen);
		game.setRoot(menuRoot);
	}
	public void setupLogo(){
		menuLogo = new Rectangle();
		menuLogo.setFill(new ImagePattern(MenuImageBank.gameLogo));
		menuLogo.setWidth((MenuImageBank.gameLogo.getWidth()*1.3)/GameLoader.ResolutionScaleX);
		menuLogo.setHeight((MenuImageBank.gameLogo.getHeight()*1.3)/GameLoader.ResolutionScaleY);
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
	public void setMenu(Pane menu){
		menuContainer.getChildren().clear();
		menuContainer.getChildren().add(menu);
	}
	public void removeMenu(Pane menu){
		menuContainer.getChildren().remove(menu);
	}
	/**
	 * Sets up the main menu
	 */

	public Pane mainMenuScreen(){
		MAIN_MENU_BOX.getButton(currentChoice-1).setActive(true);
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
				MAIN_MENU_BOX.getButton(currentChoice-1).activate();
				break;
			case SPACE:
				MAIN_MENU_BOX.getButton(currentChoice-1).activate();
			default:
				break;
			}
			for(int i = 0; i<MAIN_MENU_BOX.getButtons().size(); i++){
				if(i!=currentChoice-1){
					MAIN_MENU_BOX.getButton(i).setActive(false);
				}
				else{
					MAIN_MENU_BOX.getButton(i).setActive(true);
				}
			}
		});
		game.getScene().setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case ENTER:
					MAIN_MENU_BOX.getButton(currentChoice-1).deactivate();
				break;
			case SPACE:
					MAIN_MENU_BOX.getButton(currentChoice-1).deactivate();
				break;
			default:
				break;
			}
		});


		MAIN_MENU_BOX.getButton(0).setAction(() -> startSelected());
		MAIN_MENU_BOX.getButton(2).setAction(() -> gameModesMenu());
		MAIN_MENU_BOX.getButton(5).setAction(() -> closeGame());
		for(int i = 0; i<MAIN_MENU_BOX.getButtons().size(); i++){
			final int index = i;
			MAIN_MENU_BOX.getButtons().get(i).setOnHover(() -> {
				resetSelections(MAIN_MENU_BOX,index);
		});

		}
		MAIN_MENU.getChildren().remove(MAIN_MENU_BOX.getMenu());
		MAIN_MENU.getChildren().add(MAIN_MENU_BOX.getMenu());
		return MAIN_MENU;
	}
	public Pane modesMenuScreen(){
		MODES_MENU_BOX.getButton(currentChoice-1).setActive(true);
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
				if (currentChoice > 4) {
					currentChoice = 4;
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
				if (currentChoice > 4) {
					currentChoice = 4;
				}
				break;
			case ENTER:
				MODES_MENU_BOX.getButton(currentChoice-1).activate();
				break;
			case SPACE:
				MODES_MENU_BOX.getButton(currentChoice-1).activate();
			default:
				break;
			}
			for(int i = 0; i<MODES_MENU_BOX.getButtons().size(); i++){
				if(i!=currentChoice-1){
					MODES_MENU_BOX.getButton(i).setActive(false);
				}
				else{
					MODES_MENU_BOX.getButton(i).setActive(true);
				}
			}
		});
		game.getScene().setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case ENTER:
					MODES_MENU_BOX.getButton(currentChoice-1).deactivate();
				break;
			case SPACE:
					MODES_MENU_BOX.getButton(currentChoice-1).deactivate();
				break;
			default:
				break;
			}
		});
		MODES_MENU_BOX.getButton(2).setAction(() -> startSelected());
		MODES_MENU_BOX.getButton(3).setAction(() -> goBack());
		for(int i = 0; i<MODES_MENU_BOX.getButtons().size(); i++){
			final int index = i;
			MODES_MENU_BOX.getButtons().get(i).setOnHover(() -> {
				resetSelections(MODES_MENU_BOX,index);
		});

		}
		GAME_MODE_MENU.getChildren().remove(MODES_MENU_BOX.getMenu());
		GAME_MODE_MENU.getChildren().add(MODES_MENU_BOX.getMenu());
		return GAME_MODE_MENU;
	}
	public void resetSelections(MenuBox menu, int index) {
		currentChoice = index + 1;
		for (int i = 0; i < menu.getButtons().size(); i++) {
			if (i != currentChoice - 1) {
				menu.getButton(i).setActive(false);
			} else {
				menu.getButton(i).setActive(true);
			}
		}
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
	 * Sets up key input handling and mouse input handling for the main menu.
	 * Shows the cursor, sets the root of the game to menu root and pauses the
	 * game.
	 */
	public void setMainMenu() {
		MAIN_MENU_BOX.getButton(currentChoice-1).setActive(true);
		menuRoot.getChildren().add(fadeScreen);
		showMenu = true;
		opacity = 1.0;
		setMenu(mainMenuScreen());
		clearUp.setOpacity(opacity);
		game.showCursor(true, game.getScene());
		game.setRoot(menuRoot);
		game.pauseGame();
	}

	/**
	 * Starts the game if the startbutton is pressed
	 */
	private void startSelected() {
		removeMenu(mainMenuScreen());
		menuRoot.getChildren().remove(fadeScreen);
		game.resumeGame();
		game.showCursor(false, game.getScene());
		game.setRoot(game.getMainRoot());
		game.processGameInput();
		game.getCountDownScreen().startCountdown();
	}

	/**
	 * Sets up the optionsmenu
	 */
	@SuppressWarnings("unused")
	private void optionsMenu() {

	}

	@SuppressWarnings("unused")
	private void multiplayerMenu() {

	}
	private void gameModesMenu(){
		setMenu(modesMenuScreen());
	}
	public void setCurrentChoice(int choice) {
		currentChoice = choice;
	}

	private void closeGame() {
		game.closeGame();
	}
	private void goBack(){
		setMenu(mainMenuScreen());
	}
	public Pane getMenuRoot() {
		return menuRoot;
	}

	public void setMenuRoot(Pane menuRoot) {
		this.menuRoot = menuRoot;
	}
}
