package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * This class is used for holding and displaying a score which can the be
 * stored.
 *
 * @author Eudy Contreras
 *
 */
public class ScoreBoard {



	private int score = 0;
	private String message;
	private Text text;
	private GameManager game;
	private DropShadow textGlow;

	public ScoreBoard(String text, GameManager game, double x, double y, Color color) {
		this.message = text;
		this.game = game;
		this.text = new Text();
		this.text.setTranslateX(x);
		this.text.setTranslateY(y);
		this.text.setText(message + " 00" + score);
		this.setupText();
		game.getTwelfthLayer().getChildren().add(this.text);
	}

	public void showScore() {

	}
	public void setupText(){
		this.text.setFont( Font.font(null,FontWeight.BOLD, GameManager.ScaleX(25)));
//		this.text.setStroke(Color.ORANGERED);
        this.textGlow = new DropShadow();
        this.textGlow.setColor(Color.ORANGE);
//        this.textGlow.setRadius(25);
//        this.textGlow.setSpread(0.15);
        this.textGlow.setBlurType(BlurType.GAUSSIAN);
        this.text.setEffect(textGlow);
        this.text.setId("PlayerScore");
        game.getScene().getStylesheets().add(ScoreBoard.class.getResource("text.css").toExternalForm());

	}

	public void hide() {
		if (PlayerOne.LEVEL_COMPLETED || PlayerTwo.LEVEL_COMPLETED) {
			text.setVisible(false);
		}
	}

	public void show() {
		text.setVisible(true);
	}

	public void increaseScore() {
		score += 1;
		if (score < 10){
			text.setText(message + " 00" + score);
		}
		else if (score >= 10 && score<100){
			text.setText(message + " 0" + score);
		}
		else{
			text.setText(message + " " + score);
		}
	}

	public void resetScore() {
		score = 0;
		text.setText(message + " 00" + score);
	}

}
