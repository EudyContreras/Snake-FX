package com.SnakeGame.Core;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MenuOptions {
	private int currentChoice = 1;
	private ImageView backgroundImage;
    private Label gameTitle = new Label("Snake");
	private Label soundLabel = new Label("Sound");
	private Label modeLabel = new Label("Mode");
	private Label controllsLabel = new Label("Controlls");
	private Label backLabel = new Label("Back");
	private Rectangle r1,r2,r3,r4;
	private VBox optionsBox = new VBox();
	private VBox titleBox = new VBox();
	private DropShadow borderGlow= new DropShadow();
    Light.Point light = new Light.Point();
    Lighting lighting = new Lighting();
    private Pane optionsRoot = new Pane();
    private SnakeGame game;
    private float x,y,velX,velY,x2,y2,velX2,velY2;
    private MenuMain mainMenu;
    private MenuSound soundMenu;
    private MenuMode modeMenu;
    private MenuControlls controllsMenu;

    public MenuOptions(MenuMain mainMenu, SnakeGame game) {
    	this.mainMenu = mainMenu;
    	this.game = game;
    	soundMenu = new MenuSound(this,game);
    	modeMenu = new MenuMode(this,game);
    	controllsMenu = new MenuControlls(this,game);
		gameTitle.setLayoutX(0);
		y2 = 400;
		x = (float) Settings.WIDTH/3;
		x2 = (float) (Settings.WIDTH);
		velX = -1;
		velX2 = -1;
    }
	public String loadResource(String resource){
		 return getClass().getResource(resource).toExternalForm();
	}
    /**
     * Sets up the optionsmenu
     */
    public void setupOptionsMenu(){
    	setTitleStyle(gameTitle);
    	setStyle(soundLabel);
        setStyle(modeLabel);
        setStyle(controllsLabel);
        setStyle(backLabel);
        titleBox.getChildren().addAll(gameTitle);
        titleBox.setBackground(Background.EMPTY);
        titleBox.setAlignment(Pos.CENTER);
        optionsBox.getChildren().addAll(soundLabel, modeLabel, controllsLabel, backLabel);
        optionsBox.setBackground(Background.EMPTY);
        r1 = new Rectangle(550,250,200,80);
        r1.setFill(Color.TRANSPARENT);
        r2 = new Rectangle(550,340,200,80);
        r2.setFill(Color.TRANSPARENT);
        r3 = new Rectangle(550,430,200,80);
        r3.setFill(Color.TRANSPARENT);
        r4 = new Rectangle(550,520,200,80);
        r4.setFill(Color.TRANSPARENT);
        titleBox.setTranslateX(480);
        titleBox.setTranslateY(30);
        optionsBox.setTranslateX(550);
        optionsBox.setTranslateY(240);
        // addBackgroundImages();
        optionsRoot.getChildren().addAll( titleBox, optionsBox, r1,r2,r3,r4);
        soundLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Ubuntu; -fx-font-size: 36px");
        soundMenu.setupSoundsMenu();
        modeMenu.setupModeMenu();
        controllsMenu.setupControllsMenu();
    }

    /**
     * Sets the standard style of the labels
     * @param label
     */
    private void setStyle(Label label) {
        label.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Ubuntu; -fx-font-size: 36px;");
        label.setPadding(new Insets(20, 20, 20, 20));
        label.setEffect(borderGlow);
    }

    /**
     * Sets the style for the title
     * @param label
     */
    public void setTitleStyle(Label label) {
    	Blend blend = new Blend();
    	blend.setMode(BlendMode.MULTIPLY);

    	DropShadow ds = new DropShadow();
    	ds.setColor(Color.CHOCOLATE);
    	ds.setOffsetX(3);
    	ds.setOffsetY(3);
    	ds.setRadius(5);
    	ds.setSpread(0.2);

    	blend.setBottomInput(ds);

    	DropShadow ds1 = new DropShadow();
    	ds1.setColor(Color.CHOCOLATE);
    	ds1.setRadius(100);
    	ds1.setSpread(0.6);

    	Blend blend2 = new Blend();
    	blend2.setMode(BlendMode.MULTIPLY);

    	InnerShadow is = new InnerShadow();
    	is.setColor(Color.BURLYWOOD);
    	is.setRadius(9);
    	is.setChoke(0.8);
    	blend2.setBottomInput(is);

    	InnerShadow is1 = new InnerShadow();
    	is1.setColor(Color.BURLYWOOD);
    	is1.setRadius(5);
    	is1.setChoke(0.4);
    	blend2.setTopInput(is1);

    	Blend blend1 = new Blend();
    	blend1.setMode(BlendMode.MULTIPLY);
    	blend1.setBottomInput(ds1);
    	blend1.setTopInput(blend2);

    	blend.setTopInput(blend1);

    	label.setEffect(blend);
        label.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Ubuntu; -fx-font-size: 110px;");
        label.setPadding(new Insets(30, 30, 30, 30));
    }

    /**
     * Sets the keyinputhandling for the optionsmenu
     */
    public void setKeyInputHandler() {
    	/*
    	 * Code below determines what will happen if
    	 * the user presses enter or space on the different choices
    	 */
    	game.getScene().setOnKeyPressed(e -> {
    		switch (e.getCode()) {
                case UP:
                	currentChoice-=1;
                	if(currentChoice<1){
                		currentChoice =4;
                	}
                    break;
                case DOWN:
                  	currentChoice+=1;
                	if(currentChoice>4){
                		currentChoice =1;
                	}
                    break;
                case W:
                	currentChoice-=1;
                	if(currentChoice<1){
                		currentChoice =4;
                	}
                    break;
                case S:
                  	currentChoice+=1;
                	if(currentChoice>4){
                		currentChoice =1;
                	}
                    break;
                case ENTER:
                    if (currentChoice == 1) { soundMenu();
                    }
                    if (currentChoice == 2) { modeMenu();
                    }
                    if (currentChoice == 3) { controllsMenu();
                    }
                    if (currentChoice == 4) {
                    mainMenu.getMenuRoot().getChildren().remove(optionsRoot);
                    mainMenu.setMainMenu();
                    }
				    break;
                case ESCAPE:
                	 mainMenu.getMenuRoot().getChildren().remove(optionsRoot);
                     mainMenu.setMainMenu();
 				    break;
                case SPACE:
                	if (currentChoice == 1) { soundMenu();
                		}
                	if (currentChoice == 2) { modeMenu();
                       }
                	if (currentChoice == 3) { controllsMenu();
                       }
                	if (currentChoice == 4) {
                		 mainMenu.getMenuRoot().getChildren().remove(optionsRoot);
                         mainMenu.setMainMenu();
                         mainMenu.setCurrentChoice(1);
                       }
                default:
				    break;
            }
    		/*
    		 * Code below determines the styling of the different labels
    		 * if the user has toggled to that choice
    		 */
            if (currentChoice == 1) {
                soundLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");

            } else if (currentChoice == 2) {
            	 modeLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                 soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                 controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                 backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");

            } else if (currentChoice == 3) {
            	 controllsLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                 modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                 soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                 backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");

            } else if (currentChoice == 4) {
            	 backLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                 modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                 controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                 soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
            }
        });
    }

    /**
     * Sets the mouseinputhandling for the optionslabels
     */
    public void setMouseInputHandler(){
    	/**
    	 * Code below determines what will happen if the mouse
    	 * presses one of the different rectangles
    	 */
    	r1.setOnMousePressed(e -> {
			soundMenu();
		});
		r2.setOnMousePressed(e -> {
			modeMenu();
		});
		r3.setOnMousePressed(e -> {
			controllsMenu();
		});
		r4.setOnMousePressed(e -> {
			mainMenu.getMenuRoot().getChildren().remove(optionsRoot);
			mainMenu.setMainMenu();
			mainMenu.setCurrentChoice(1);
		});

		/**
		 * Code below determines the style of the labels if
		 * the mouse enters one of the rectangles
		 */
		r1.setOnMouseEntered(e -> {
			soundLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
		});
		r2.setOnMouseEntered(e -> {
			modeLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
		});
		r3.setOnMouseEntered(e -> {
			controllsLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
		});
		r4.setOnMouseEntered(e -> {
			backLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Ubuntu; -fx-font-size: 36px");
		});

    }

    public Pane getOptionsRoot(){
    	return optionsRoot;
    }

    public void setCurrentChoice(int choice){
    	currentChoice = choice;
    }

    private void soundMenu(){
    	currentChoice =1;
    	optionsRoot.getChildren().add(soundMenu.getSoundsRoot());
    	soundMenu.setKeyInputHandler();
    	soundMenu.setMouseInputHandler();
    }

    private void modeMenu(){
    	currentChoice = 1;
    	optionsRoot.getChildren().add(modeMenu.getModeRoot());
    	modeMenu.setKeyInputHandler();
    	modeMenu.setMouseInputHandler();
    }

    private void controllsMenu(){
    	currentChoice = 1;
    	optionsRoot.getChildren().add(controllsMenu.getControllsRoot());
    	controllsMenu.setKeyInputHandler();
    	controllsMenu.setMouseInputHandler();
    }
}