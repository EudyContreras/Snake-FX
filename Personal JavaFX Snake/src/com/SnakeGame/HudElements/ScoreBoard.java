package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;

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
	private Font theFont;

	public ScoreBoard(String text, SnakeGame game, double x, double y, Color color) {
		this.message = text;
		this.text = new Text();
		this.theFont = Font.font("Helvetica", FontWeight.BOLD, 20 / GameLoader.ResolutionScaleX);
		this.text.setTranslateX(x);
		this.text.setTranslateY(y);
		this.text.setFill(color);
		this.text.setEffect(null);
		this.text.setFont(theFont);
		this.text.setText(message + " 00" + score);
		game.getNinthLayer().getChildren().add(this.text);
	}

	public void showScore() {

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
