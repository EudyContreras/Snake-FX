package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.PlayerTwo.Player2;
import com.SnakeGame.SnakeOne.SnakeOne;

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

	boolean depleated = false;
	boolean shotsFired = false;
	boolean setDelay = false;
	boolean killPlayer = false;
	boolean playerIsAlive = true;
	double maxHealth = 100;
	int score = 0;
	double x = 0;
	double y = 0;
	double width = 0;
	double height = 0;
	double delay = 0;
	String message;
	SnakeOne player;
	Text text = new Text();
	Font theFont = Font.font("Helvetica", FontWeight.BOLD, 20 / GameLoader.ResolutionScaleX);
	Rectangle board;

	public ScoreBoard(String text, SnakeGame game, double x1, double y1, double x, double y, double width,
			double height) {
		this.x = x;
		this.y = y;
		this.message = text;
		this.width = width;
		this.height = height;
		this.player = game.getloader().getPlayer();
		this.board = new Rectangle(x, y, width, height);
		this.board.setFill(new ImagePattern(new Image("com/SnakeGame/Images/scoreBoard.png")));
		this.text.setTranslateX(x1);
		this.text.setTranslateY(y1);
		this.text.setFill(Color.RED);
		// this.text.prefWidth(width);
		// this.text.prefHeight(height);
		this.text.setEffect(null);
		this.text.setFont(theFont);
		this.text.setText(message + " :" + "0" + score);
		// game.getOverlay().getChildren().add(board);
		game.getOverlay().getChildren().add(this.text);
	}

	public void showScore() {

	}

	public void hide() {
		if (SnakeOne.levelComplete || Player2.levelComplete) {
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
			text.setText("Hits: " + score);
	}

	public void resetScore() {
		score = 0;
		text.setText(message + " : " + "0" + score);
	}

}
