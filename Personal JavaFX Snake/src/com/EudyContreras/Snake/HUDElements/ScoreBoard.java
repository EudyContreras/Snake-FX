package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
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
 * stored, manipulated, and processed for the records
 *
 * @author Eudy Contreras
 *
 */
public class ScoreBoard {

	private int score = 0;
	private double x;
	private double y;
	private double moveX;
	private double velX;
	private Text text;
	private String message;
	private GameManager game;
	private DropShadow textGlow;

	/**
	 * Constructor which takes the main game class along with a message for this score board to
	 * display. The constructor also takes its coordinates as parameters
	 * @param text: Message which this score board shows next to the score
	 * @param game: Main game class which connects this class to all others
	 * @param x: Horizontal coordinate for this HUD element
	 * @param y: Vertival coordinate for this HUD element
	 * @param color: Color of the text to be shown
	 */
	public ScoreBoard(String text, GameManager game, double x, double y, Color color, GameObjectID id) {
		this.x = x;
		this.y = y;
		this.game = game;
		this.message = text;
        this.textGlow = new DropShadow();
        this.textGlow.setColor(Color.DODGERBLUE);
        this.textGlow.setBlurType(BlurType.GAUSSIAN);
		this.setupText();
	}
	/**
	 * Method which prepares the text element which
	 * shows the score.
	 */
	private void setupText(){
		this.text = new Text();
		this.text.setTranslateX(x);
		this.text.setTranslateY(y);
		this.text.setText(message + " 00" + score);
		this.text.setFont( Font.font(null,FontWeight.BOLD, 32));
        this.text.setEffect(textGlow);
        this.text.setId("MainTimer");
		this.game.getEleventhLayer().getChildren().add(this.text);

	}
	public void updateUI(){
		hide();
		popIn();
	}
	public void popIn(){
		this.moveX+=velX;
		this.text.setTranslateX(x+moveX);
	}
	public void moveLeft(){
		this.velX = -10;
	}
	public void moveRight(){
		this.velX = 10;
	}
	public void stopMoving(){
		this.velX = 0;
	}
	public void setMoveX(double moveX){
		this.moveX = moveX;
	}
	/**
	 * Method which hides the score by setting its
	 * visibility to false
	 */
	public void hide() {
		if (PlayerOne.LEVEL_COMPLETED || PlayerTwo.LEVEL_COMPLETED) {
			text.setVisible(false);
		}
	}
	/**
	 * Method which shows the score by setting its
	 * visibility to true
	 */
	public void show(boolean state) {
		text.setVisible(state);
	}
	/**
	 * Method which increases the current
	 * score by one with each call.
	 */
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
	/**
	 * Method which decreases the current
	 * score by one with each call
	 */
	public void decreaseScore(){
		score -= 1;
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
	/**
	 * Method whic resets the score back to zero
	 */
	public void resetScore() {
		score = 0;
		text.setText(message + " 00" + score);
	}
	/**
	 * Returns the current socre
	 * @return: current score.
	 */
	public int getScore(){
		return score;
	}

	//TODO: Save the score to a local location so that the player can see
	//the local high scores.
	/**
	 * Compare the last ten local scores and if this score is better than
	 * if a score is beaten put this score on the beaten score index and
	 * shift the scores down.
	 */
	public void saveScoreLocally(){

	}
	//TODO: Save the score to a remote database so that all players can see
	//the high score achieved by this player.
	/**
	 *
	 */
	public void saveScoreToDB(){

	}
	public void reportScore(){

	}

	public void compareScore(){

	}

}
