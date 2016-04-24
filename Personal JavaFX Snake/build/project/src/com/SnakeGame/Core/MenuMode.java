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

public class MenuMode {

	private int currentChoice = 1;
	private ImageView backgroundImage;
    private Label gameTitle = new Label("Snake");
	private Label modeLabel = new Label("Current mode is: FREESTYLE MODE");
	private Label backLabel = new Label("Back");
	private Rectangle r1,r2;
	private VBox modeBox = new VBox();
	private VBox titleBox = new VBox();
	private DropShadow borderGlow = new DropShadow();
    Light.Point light = new Light.Point();
    Lighting lighting = new Lighting();
    private Pane modeRoot = new Pane();
    private SnakeGame game;
    private float x,y,velX,velY,x2,y2,velX2,velY2;
    private MenuOptions optionsMenu;

    public MenuMode(MenuOptions optionsMenu, SnakeGame game) {
    	this.optionsMenu = optionsMenu;
    	this.game = game;
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
     * Sets up the sounds menu
     */
    public void setupModeMenu(){
    	setTitleStyle(gameTitle);
    	setStyle(modeLabel);
        setStyle(backLabel);
        titleBox.getChildren().addAll(gameTitle);
        titleBox.setBackground(Background.EMPTY);
        titleBox.setAlignment(Pos.CENTER);
        modeBox.getChildren().addAll(modeLabel, backLabel);
        modeBox.setBackground(Background.EMPTY);
        r1 = new Rectangle(550,250,200,80);
        r1.setFill(Color.TRANSPARENT);
        r2 = new Rectangle(550,340,220,80);
        r2.setFill(Color.TRANSPARENT);
        titleBox.setTranslateX(480);
        titleBox.setTranslateY(30);
        modeBox.setTranslateX(550);
        modeBox.setTranslateY(240);
        // addBackgroundImages();
        modeRoot.getChildren().addAll( titleBox, modeBox, r1,r2);
        modeLabel.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Ubuntu; -fx-font-size: 36px");
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
    	ds.setColor(Color.BLUE);
    	ds.setOffsetX(3);
    	ds.setOffsetY(3);
    	ds.setRadius(5);
    	ds.setSpread(0.2);

    	blend.setBottomInput(ds);

    	DropShadow ds1 = new DropShadow();
    	ds1.setColor(Color.BLUE);
    	ds1.setRadius(100);
    	ds1.setSpread(0.6);

    	Blend blend2 = new Blend();
    	blend2.setMode(BlendMode.MULTIPLY);

    	InnerShadow is = new InnerShadow();
    	is.setColor(Color.CADETBLUE);
    	is.setRadius(9);
    	is.setChoke(0.8);
    	blend2.setBottomInput(is);

    	InnerShadow is1 = new InnerShadow();
    	is1.setColor(Color.CADETBLUE);
    	is1.setRadius(5);
    	is1.setChoke(0.4);
    	blend2.setTopInput(is1);

    	Blend blend1 = new Blend();
    	blend1.setMode(BlendMode.MULTIPLY);
    	blend1.setBottomInput(ds1);
    	blend1.setTopInput(blend2);

    	blend.setTopInput(blend1);

    	label.setEffect(blend);
        label.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Ubuntu; -fx-font-size: 110px;");
        label.setPadding(new Insets(30, 30, 30, 30));
    }

    /**
     * Sets up the keyinputhandling for the mode menu
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
                		currentChoice =2;
                	}
                    break;
                case DOWN:
                  	currentChoice+=1;
                	if(currentChoice>2){
                		currentChoice =1;
                	}
                    break;
                case W:
                	currentChoice-=1;
                	if(currentChoice<1){
                		currentChoice =2;
                	}
                    break;
                case S:
                  	currentChoice+=1;
                	if(currentChoice>2){
                		currentChoice =1;
                	}
                    break;
                case ENTER:
                    if (currentChoice == 1) { mode();
                    }
                    if (currentChoice == 2) {
                    optionsMenu.getOptionsRoot().getChildren().remove(modeRoot);
                    optionsMenu.setKeyInputHandler();
                    optionsMenu.setMouseInputHandler();
                    }
				    break;
                case ESCAPE:
                	 optionsMenu.getOptionsRoot().getChildren().remove(modeRoot);
                     optionsMenu.setKeyInputHandler();
                     optionsMenu.setMouseInputHandler();
                     break;
                case SPACE:
                	if (currentChoice == 1) { mode();
                		}
                	if (currentChoice == 2) {
                		  optionsMenu.getOptionsRoot().getChildren().remove(modeRoot);
                          optionsMenu.setKeyInputHandler();
                          optionsMenu.setMouseInputHandler();
                       }
                default:
				    break;
            }
    		/*
    		 * Code below determines the styling of the different labels
    		 * if the user has toggled to that choice
    		 */
            if (currentChoice == 1) {
                modeLabel.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Ubuntu; -fx-font-size: 36px");

            }  else if (currentChoice == 2) {
            	 backLabel.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                 modeLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Ubuntu; -fx-font-size: 36px");
            }
        });
    }

    /**
     * Sets the mouseinputhandling for the mode menu
     */
    public void setMouseInputHandler(){
    	/**
    	 * Code below determines what will happen if the mouse
    	 * presses one of the different rectangles
    	 */
		r1.setOnMousePressed(e -> {
			mode();
		});
		r2.setOnMousePressed(e -> {
			optionsMenu.getOptionsRoot().getChildren().remove(modeRoot);
			optionsMenu.setKeyInputHandler();
			optionsMenu.setMouseInputHandler();
		});

		/**
		 * Code below determines the style of the labels if
		 * the mouse enters one of the rectangles
		 */
		r1.setOnMouseEntered(e -> {
			modeLabel.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Ubuntu; -fx-font-size: 36px");
		});
		r2.setOnMouseEntered(e -> {
			backLabel.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			modeLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Ubuntu; -fx-font-size: 36px");
		});
    }

    public Pane getModeRoot(){
    	return modeRoot;
    }

    private void mode(){
    	if(modeLabel.getText()=="Current mode is: FREESTYLE MODE"){
    		modeLabel.setText("Current mode is: MODE 2");
    		//change to Mode 2
    	} else if(modeLabel.getText()=="Current mode is: MODE 2") {
    		modeLabel.setText("Current mode is: MODE 3");
    		//change to Mode 3
    	} else if(modeLabel.getText()=="Current mode is: MODE 3") {
    		modeLabel.setText("Current mode is: FREESTYLE MODE");
    	}
    }


}