package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
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
	private Text text = new Text();
	private Font theFont = Font.font("Helvetica", FontWeight.BOLD, 20 / GameLoader.ResolutionScaleX);
	private Rectangle board;

	public ScoreBoard(String text, SnakeGame game, double x1, double y1, double x, double y, double width,
			double height) {
		this.message = text;
		this.board = new Rectangle(x, y, width, height);
		this.board.setFill(new ImagePattern(new Image("com/SnakeGame/Images/scoreBoard.png")));
		this.text.setTranslateX(x1);
		this.text.setTranslateY(y1);
		this.text.setFill(Color.RED);
		this.text.setEffect(null);
		this.text.setFont(theFont);
		this.text.setText(message + " :" + "0" + score);
		game.getOverlay().getChildren().add(this.text);
	}

	public void showScore() {

	}

	public void hide() {
		if (PlayerOne.LEVEL_COMPLETED || PlayerTwo.LEVEL_COMPLETED) {
			text.setVisible(false);
			board.setVisible(false);
		}
	}

	public void show() {
		text.setVisible(true);
		board.setVisible(true);
	}

	public void increaseScore() {
		score += 1;
		if (score < 10)
			text.setText(message + " : " + "0" + score);
		else
			text.setText(message + " : " +  score);
	}

	public void resetScore() {
		score = 0;
		text.setText(message + " : " + "0" + score);
	}

}
