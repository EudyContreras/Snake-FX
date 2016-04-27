package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ScoreBoard {

	boolean depleated = false;
	boolean shotsFired = false;
	boolean setDelay = false;
	boolean killPlayer = false;
	boolean playerIsAlive = true;
	int maxHealth = 100;
	int score = 0;
	int x = 0;
	int y = 0;
	int width = 0;
	int height = 0;
	int delay = 0;
	Player player;
	Rectangle energyBar = new Rectangle();
	Rectangle energyBarBorder = new Rectangle();
	Text text  = new Text();
	Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );

	public ScoreBoard(Game game, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = game.getloader().getPlayer();
		this.text.setX(x);
		this.text.setY(y);
		this.text.setFill(Color.WHITE);
		this.text.setFont(theFont);
		this.text.setText("Hits :" + score);
		game.getRadarLayer().getChildren().add(text);
	}

	public void showScore() {

	}
	
	public void increaseScore(){
		score+=1;
		text.setText("HITS: " + score);
	}
	public void resetScore() {

	}


}
