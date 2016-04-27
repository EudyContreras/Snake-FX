package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class MainMenu {

    private int currentChoice = 1;
    private ImageView backgroundImage = new ImageView(new Image("background.jpg"));
    private ImageView meteorField = new ImageView(new Image("asteroidField.png"));
    private ImageView meteorField2 = new ImageView(new Image("asteroidField2.png"));
    private Label gameTitle = new Label("Meteor Fall");
	private Label startLabel = new Label("Start Game");
    private Label optionsLabel = new Label("Options");
    private Label exitLabel = new Label("Exit");
    private Rectangle r1,r2,r3;
    private VBox menuBox = new VBox();
    private DropShadow borderGlow= new DropShadow();
    private Rectangle clearUp;
    private Group menuRoot = new Group();
    private Game game;
    private Pane fadeScreen;
    private double opacity = 1.0;
    private double glowPulse = 0;
    private boolean showMenu = false;
    private boolean increaseGlow = true;
    private boolean decreaseGlow = false;
    private float x,y,velX,velY,x2,y2,velX2,velY2;

    public MainMenu(Game game) {

        this.game = game;
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setColor(Color.WHITE);
		borderGlow.setWidth(100);
		borderGlow.setHeight(100);
		borderGlow.setSpread(0.5);
		borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		gameTitle.setLayoutX(500);
		gameTitle.setLayoutY(500);
		y2 = 400;
		x = (float) Settings.WIDTH/3;
		x2 = (float) (Settings.WIDTH);
		velX = -1;
		velX2 = -1;
		fadeScreen = new Pane();     
	    clearUp = new Rectangle(0,0,1920,1080);
	    clearUp.setFill(Color.BLACK);
    }
    public void enterMainMenu() {

        setStyle(startLabel);
        setStyle(optionsLabel);
        setStyle(exitLabel);
        setTitleStyle(gameTitle);
        menuBox.getChildren().addAll(startLabel, optionsLabel, exitLabel);
        menuBox.setBackground(Background.EMPTY);
        menuBox.setAlignment(Pos.CENTER);
        r1 = new Rectangle(160,250,200,80);
        r1.setFill(Color.TRANSPARENT);
        r2 = new Rectangle(160,340,200,80);
        r2.setFill(Color.TRANSPARENT);
        r3 = new Rectangle(160,430,200,80);
        r3.setFill(Color.TRANSPARENT);
        fadeScreen.getChildren().add(clearUp);
        menuBox.setTranslateX(150);
        menuBox.setTranslateY(240);
        menuRoot.getChildren().addAll(backgroundImage,gameTitle,meteorField,meteorField2, menuBox, r1,r2,r3,fadeScreen);
        startLabel.setStyle("-fx-text-fill: #06c5ea; -fx-font-family: Ubuntu; -fx-font-size: 36px");
        game.setRoot(menuRoot);
        setKeyInputHandling();
        setMouseInputHandler();      
        showMenu =true;
    }
    public void setGlowPulse(){
    	if(showMenu){
    		opacity-=0.01;
			clearUp.setOpacity(opacity);
			if(opacity<=0){
				opacity=0;
				menuRoot.getChildren().remove(fadeScreen);
				showMenu = false;
			}
		}
    	if(increaseGlow)
    	glowPulse+=0.005;
    	if(glowPulse >= 0.9){
    		glowPulse = 0.9;
    		increaseGlow = false;
    		decreaseGlow = true;
    	}
    	if(decreaseGlow)
    	glowPulse-=0.005;
    	if(glowPulse <= 0.1){
    		glowPulse = 0.1;
    		decreaseGlow = false;
    		increaseGlow = true;
    	}
    	
    	borderGlow.setSpread(glowPulse);
    	setBackgroundMeteors();
    }
    public void setBackgroundMeteors(){
    	x = x+velX;
    	y = y+velY;
    	x2 = x2+velX2;
    	y2 = y2+velY2;
    	meteorField.setX(x);
    	meteorField.setY(y);
    	if(x<0-meteorField.getImage().getWidth()){
    		x = (float) Settings.WIDTH;
    	}
    	meteorField2.setX(x2);
    	meteorField2.setY(y2);
    	if(x2<0-meteorField2.getImage().getWidth()){
    		x2 = (float) Settings.WIDTH;
    	}
    }
    private void setStyle(Label label) {

        label.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px;");
        label.setPadding(new Insets(20, 20, 20, 20));
        label.setEffect(borderGlow);
    }
    public void setTitleStyle(Label label) {
    	Blend blend = new Blend();
    	blend.setMode(BlendMode.MULTIPLY);

    	DropShadow ds = new DropShadow();
    	ds.setColor(Color.WHITE);
    	ds.setOffsetX(3);
    	ds.setOffsetY(3);
    	ds.setRadius(5);
    	ds.setSpread(0.2);

    	blend.setBottomInput(ds);

    	DropShadow ds1 = new DropShadow();
    	ds1.setColor(Color.WHITE);
    	ds1.setRadius(100);
    	ds1.setSpread(0.6);

    	Blend blend2 = new Blend();
    	blend2.setMode(BlendMode.MULTIPLY);

    	InnerShadow is = new InnerShadow();
    	is.setColor(Color.SILVER);
    	is.setRadius(9);
    	is.setChoke(0.8);
    	blend2.setBottomInput(is);

    	InnerShadow is1 = new InnerShadow();
    	is1.setColor(Color.WHITE);
    	is1.setRadius(5);
    	is1.setChoke(0.4);
    	blend2.setTopInput(is1);

    	Blend blend1 = new Blend();
    	blend1.setMode(BlendMode.MULTIPLY);
    	blend1.setBottomInput(ds1);
    	blend1.setTopInput(blend2);

    	blend.setTopInput(blend1);

    	label.setEffect(blend);
        label.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 110px;");
        label.setPadding(new Insets(20, 20, 20, 20));

    }
    private void setKeyInputHandling() {

        game.getScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP:
                	
                	currentChoice-=1;
                	if(currentChoice<1){
                		currentChoice =3;
                	}
                    break;
                case W:
                	
                  	currentChoice-=1;
                	if(currentChoice<1){
                		currentChoice =3;
                	}
                    break;
                case DOWN:
                  	currentChoice+=1;
                	if(currentChoice>3){
                		currentChoice =1;
                	}
                    break;
                case S:
                   	currentChoice+=1;
                	if(currentChoice>3){
                		currentChoice =1;
                	}
                    break;
                case ENTER:
                    if (currentChoice == 1) { startSelected();
                    }
                    if (currentChoice == 2) { OptionsMenu();
                    }
                    if (currentChoice == 3) { closeGame();
                        
                    }
				    break;
                case SPACE:
                    if (currentChoice == 1) { startSelected();
                    }
                    if (currentChoice == 2) { OptionsMenu();
                    }
                    if (currentChoice == 3) { closeGame();
                        
                    }
                default:
				    break;
            }
            if (currentChoice == 1) {
                startLabel.setStyle("-fx-text-fill: #06c5ea; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                optionsLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                exitLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
               
            } else if (currentChoice == 2) {
                optionsLabel.setStyle("-fx-text-fill: #06c5ea; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                startLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                exitLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
             
            } else if (currentChoice == 3) {
                exitLabel.setStyle("-fx-text-fill: #06c5ea; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                startLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
                optionsLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
             
            } 
        }); 
      
    } 
    public void setMouseInputHandler(){
		r1.setOnMouseEntered(e -> {
			startLabel.setStyle("-fx-text-fill: #3BB9FF; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			optionsLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			exitLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
		});
		r2.setOnMouseEntered(e -> {
			optionsLabel.setStyle("-fx-text-fill: #3BB9FF; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			startLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			exitLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
		});
		r3.setOnMouseEntered(e -> {
			exitLabel.setStyle("-fx-text-fill: #3BB9FF; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			startLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");
			optionsLabel.setStyle("-fx-text-fill: White; -fx-font-family: Ubuntu; -fx-font-size: 36px");

		});
		r1.setOnMousePressed(e -> {
			startSelected();
		});
		r2.setOnMousePressed(e -> {
			OptionsMenu();
		});
		r3.setOnMousePressed(e -> {
			closeGame();
		});
    }
    public void setMainMenu() {
    	menuRoot.getChildren().add(fadeScreen);
    	showMenu = true;
    	opacity = 1;
    	clearUp.setOpacity(opacity);
        setKeyInputHandling();
//        setMouseInputHandler();
        game.showCursor(true, game.getScene());
        game.setRoot(menuRoot);
        game.pauseGame();
    }
    private void startSelected() {
    	menuRoot.getChildren().remove(fadeScreen);
        game.showCursor(false, game.getScene());
        game.setRoot(game.mainRoot);
        game.resumeGame();
        game.processGameInput();

    }
    private void closeGame(){
    	game.closeGame();
    }
    public Group getMenuRoot() {
		return menuRoot;
	}
	public void setMenuRoot(Group menuRoot) {
		this.menuRoot = menuRoot;
	}
    private void OptionsMenu() {
//    	  menuRoot.getChildren().add(backgroundImage);
//        menuRoot.setOnKeyPressed(e -> {
//            if(e.getCode() == KeyCode.ESCAPE) {
//                menuRoot.getChildren().remove(backgroundImage);
//                setKeyInputHandling();
//            }
//        });
    }
    
    
} 


