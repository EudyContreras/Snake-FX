package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.EnumIDs.GameStateID;
import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class ScoreKeeper {

	public static int APPLE_COUNT = 50;
	private DropShadow dropShadowOne;
	private DropShadow dropShadowTwo;
	private GameManager game;
	private ImageView apple;
	private Rectangle board;
	private Boolean startTimer = false;
	private String secondsS = "00";
	private String minutesS = "00";
	private String hoursS = "00";
	private Text text;
	private Text timer;
	private boolean swipeUp = true;
	private boolean swipeDown = false;
	private double swipeSpeed = 0;
	private double width = 0;
	private double x = 0;
	private double y = 0;
	private double y1 = 0;
	private double oldY = 0;
	private double position = 0;
	private int initialAmount;
	private int counter;
	private int seconds = 00;
	private int minutes = 00;
	private int hours = 00;

	public ScoreKeeper(GameManager game, int count, double x1, double y1, double x, double y, double width,
			double height) {
		APPLE_COUNT = count;
		this.x = x;
		this.y = y;
		this.oldY = y;
		this.y1 = y1;
		this.width = width;
		this.initialAmount = count;
		this.game = game;
		this.text = new Text();
		this.timer = new Text();
		this.apple = new ImageView(GameImageBank.apple);
		this.board = new Rectangle(x, y, width, height);
		this.board.setFill(new ImagePattern(GameImageBank.score_keeper));
		this.apple.setFitWidth(45 / GameLoader.ResolutionScaleX);
		this.apple.setFitHeight(45 / GameLoader.ResolutionScaleY);
		this.apple.setPreserveRatio(true);
		this.apple.setX(GameSettings.WIDTH/2+15);
		this.apple.setY(y1-40);
		setupTextTwo();
		game.getThirTeenthLayer().getChildren().add(board);
		game.getThirTeenthLayer().getChildren().add(apple);
		game.getThirTeenthLayer().getChildren().add(text);
		game.getThirTeenthLayer().getChildren().add(timer);
		updateCount();
	}
	public void setUpText(){
	        this.text.setId("fancytext");
	        this.timer.setId("fancytext");
	        Blend blend = new Blend();
	        blend.setMode(BlendMode.MULTIPLY);

	        DropShadow ds = new DropShadow();
	        ds.setColor(Color.rgb(254, 235, 66, 0.3));
	        ds.setOffsetX(2);
	        ds.setOffsetY(2);
	        ds.setRadius(3);
	        ds.setSpread(0.1);

	        blend.setBottomInput(ds);

	        DropShadow ds1 = new DropShadow();
	        ds1.setColor(Color.web("#f13a00"));
	        ds1.setRadius(5);
	        ds1.setSpread(0.1);

	        Blend blend2 = new Blend();
	        blend2.setMode(BlendMode.MULTIPLY);

	        InnerShadow is = new InnerShadow();
	        is.setColor(Color.web("#feeb42"));
	        is.setRadius(3);
	        is.setChoke(0.2);
	        blend2.setBottomInput(is);

	        InnerShadow is1 = new InnerShadow();
	        is1.setColor(Color.web("#f13a00"));
	        is1.setRadius(2);
	        is1.setChoke(0.2);
	        blend2.setTopInput(is1);

	        Blend blend1 = new Blend();
	        blend1.setMode(BlendMode.MULTIPLY);
	        blend1.setBottomInput(ds1);
	        blend1.setTopInput(blend2);

	        blend.setTopInput(blend1);
	        timer.setEffect(blend);
	        text.setEffect(blend);
	        game.getScene().getStylesheets().add(ScoreKeeper.class.getResource("brickStyle.css").toExternalForm());
	}
	public void setupTextTwo(){
		this.text.setX(apple.getX() + apple.getFitWidth()-5);
		this.text.setY(y1-GameManager.ScaleY(25));
		this.timer.setX(x+width*0.33);
		this.timer.setY(y1-GameManager.ScaleY(25));
		this.timer.setFont(Font.font(null,FontWeight.BOLD, GameManager.ScaleX(27)));
		this.text.setFont( Font.font(null,FontWeight.BOLD, GameManager.ScaleX(27)));
		this.text.setText("x " + APPLE_COUNT);
		this.timer.setText("00:00:00");
        this.dropShadowOne = new DropShadow();
        this.dropShadowTwo = new DropShadow();
        this.dropShadowOne.setColor(Color.DODGERBLUE);
        this.dropShadowOne.setRadius(25);
        this.dropShadowOne.setSpread(0.15);
        this.dropShadowOne.setBlurType(BlurType.TWO_PASS_BOX);
        this.dropShadowTwo.setColor(Color.RED);
        this.dropShadowTwo.setRadius(25);
        this.dropShadowTwo.setSpread(0.15);
        this.dropShadowTwo.setBlurType(BlurType.TWO_PASS_BOX);
        this.text.setEffect(dropShadowTwo);
        this.timer.setEffect(dropShadowOne);
        this.timer.setId("MainTimer");
        this.text.setId("MainScore");
        game.getScene().getStylesheets().add(ScoreKeeper.class.getResource("text.css").toExternalForm());

	}
	public void decreaseCount() {
		APPLE_COUNT -= 1;
		updateCount();
	}
	public void update() {
		y = y + swipeSpeed/GameManager.ScaleY;
		y1 = y1 + swipeSpeed/GameManager.ScaleY;
		if (swipeDown) {
			swipeSpeed = 2.5f;
			if (y >= GameManager.ScaleX(105)) {
				swipeSpeed = 0;
			}
		}
		if (swipeUp) {
			swipeSpeed = -2.5f;
			if (y < oldY) {
				startTimer();
				swipeSpeed = 0;
			}
		}
		board.setY(y);
		apple.setY(y1-GameManager.ScaleY(15));
		text.setTranslateY(y1);
		timer.setTranslateY(y1);
	}

	public void showHide() {
		if (swipeDown) {
			swipeDown = false;
			swipeUp = true;
		} else if (swipeUp) {
			swipeUp = false;
			swipeDown = true;
		}
	}

	public void swipeDown() {
		swipeUp = false;
		swipeDown = true;
	}

	public void swipeUp() {
		swipeDown = false;
		swipeUp = true;
	}
	public void updateTimer() {
		if (!VictoryScreen.LEVEL_COMPLETE && startTimer) {
			counter += 1;
			if (counter > 59) {
				seconds += 1;
				counter = 0;
			}
			if (seconds > 59) {
				minutes += 1;
				seconds = 0;
			}
			if (minutes > 59) {
				hours += 1;
				minutes = 0;
			}
			if (seconds < 10) {
				secondsS = "0" + seconds;
			} else if (seconds >= 10) {
				secondsS = "" + seconds;
			}
			if (minutes < 10) {
				minutesS = "0" + minutes;
			} else if (minutes >= 10) {
				minutesS = "" + minutes;
			}
			if (hours < 10) {
				hoursS = "0" + hours;
			} else if (hours >= 10) {
				hoursS = "" + hours;
			}
			this.timer.setText(hoursS + ":" + minutesS + ":" + secondsS);
		}
		else{
			stopTimer();
		}

	}
	public void startTimer() {
		startTimer = true;
	}
	public void stopTimer() {
		startTimer = false;
	}
	public void resetTimer() {
		startTimer = false;
		seconds = 0;
		minutes = 0;
		hours = 0;
	}

	public void keepCount() {
		updateTimer();
		update();
		if (APPLE_COUNT <= 0) {
			if (VictoryScreen.LEVEL_COMPLETE == false) {
				game.getVictoryScreen().removeBoard();
				game.getVictoryScreen().finishLevel();
				setPosition(1.5f);
				swipeDown();
				game.getGameHud().swipeDown();
				game.setStateID(GameStateID.LEVEL_COMPLETED);
				VictoryScreen.LEVEL_COMPLETE = true;
			}
		}
		else {
			if(!PlayerOne.DEAD && !PlayerTwo.DEAD && game.getStateID()!=GameStateID.GAME_MENU){
			swipeUp();
			game.getGameHud().swipeUp();
			}
		}
	}

	public void updateCount() {
		text.setText(" x " + APPLE_COUNT);
	}

	public void resetCount() {
		APPLE_COUNT = initialAmount;
		text.setText(" x " + APPLE_COUNT);
	}
	public void setPosition(float position) {
		this.position = position;
	}
	public double getPosition() {
		return position;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

}