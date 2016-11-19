package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.AbstractModels.AbstractHudElement;
import com.EudyContreras.Snake.Application.GameManager;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
/**
 * Class used to display the scores of bouth players in a
 * precompiled format. Each score is displayed through a text
 * which are approximately 185px in dimension through all resolutions.
 * @author Eudy Contreras
 *
 */
public class LocalScoreScreen extends AbstractHudElement{


	private DropShadow dropShadowOne;
	private ScoreBoard boardOne;
	private ScoreBoard boardTwo;
	private Text playerOneScore;
	private Text playerTwoScore;
	private Pane layer;

	/**
	 * Constructor whihc takes the main game class as parameter along with the coordinates for both
	 * scores and the layer to which the scores will be added in order to be displayed
	 * @param game: Main games class that connects this class to all other classses
	 * @param xOne: X coordinate for the player one score
	 * @param yOne: Y coordinate fof the player one score
	 * @param xTwo: X coordinate for the player two score
	 * @param yTwo: Y coordinate fof the player two score
	 * @param layer: Layer to which these UI elements will be added
	 */
	public LocalScoreScreen(GameManager game, double xOne, double yOne, double xTwo, double yTwo, Pane layer){
		this.layer = layer;
		this.xOne = xOne;
		this.xTwo = xTwo;
		this.yOne = yOne;
		this.boardOne = game.getScoreBoardOne();
		this.boardTwo = game.getScoreBoardTwo();
		this.initialize();
	}
	/**
	 * Method whihc initializes all the elements
	 * used by the Score: This method also sets
	 * the colors and positions for the scores.
	 */
	private void initialize(){
        this.dropShadowOne = new DropShadow();
		this.playerOneScore = new Text();
		this.playerTwoScore = new Text();
		this.playerOneScore.setX(xOne);
		this.playerOneScore.setY(yOne);
		this.playerTwoScore.setX(xTwo);
		this.playerTwoScore.setY(yTwo);
		this.playerOneScore.setFont(Font.font(null,FontWeight.BOLD, 185));
		this.playerTwoScore.setFont( Font.font(null,FontWeight.BOLD, 185));
		this.playerOneScore.setText(""+boardOne.getScore());
		this.playerTwoScore.setText(""+boardTwo.getScore());
        this.dropShadowOne.setColor(Color.DODGERBLUE);
        this.dropShadowOne.setRadius(25);
        this.dropShadowOne.setSpread(0.15);
        this.dropShadowOne.setBlurType(BlurType.TWO_PASS_BOX);
        this.playerOneScore.setEffect(dropShadowOne);
        this.playerTwoScore.setEffect(dropShadowOne);
        this.playerOneScore.setId("LocalScores");
        this.playerTwoScore.setId("LocalScores");
	}
	/**
	 * Method which when called will add the
	 * scores to the given layer and display
	 * the scores
	 */
	public void setScores(){
		this.playerOneScore.setOpacity(0);
		this.playerTwoScore.setOpacity(0);
		this.calculateStyle(boardOne.getScore(), playerOneScore);
		this.calculateStyle(boardTwo.getScore(), playerTwoScore);
		this.layer.getChildren().remove(playerOneScore);
		this.layer.getChildren().remove(playerTwoScore);
		this.layer.getChildren().add(playerOneScore);
		this.layer.getChildren().add(playerTwoScore);
	}
	/**
	 * Calculates the style which needs to be applyed to
	 * the score showing text in order to allow the contant
	 * showing of three digits regardless of the score
	 * @param score: Score which is to be review
	 * @param text: Text which will be styled according to the
	 * given score
	 */
	private void calculateStyle(int score, Text text){
		if(score<10){
			text.setText("00"+score);
		}
		else if(score>=10 && score<100){
			text.setText("0"+score);
		}
		else if(score>=100){
			text.setText(""+score);
		}
	}
	/**
	 * Method which makes the scores visible
	 */
	public void showScores(){
		this.playerOneScore.setVisible(true);
		this.playerTwoScore.setVisible(true);
	}
	/**
	 * Method which makes the scores invisible
	 */
	public void hideScores(){
		this.playerOneScore.setVisible(false);
		this.playerTwoScore.setVisible(false);
	}

	public void show(boolean state){
		this.playerOneScore.setVisible(state);
		this.playerTwoScore.setVisible(state);
	}
	/**
	 * Method which changes the opacity of the
	 * scores to the opacity passed through the
	 * parameeter.
	 * @param opacity: Level of opacity for the scores
	 */
	public void setScoreOpacity(double opacity){
		this.playerOneScore.setOpacity(opacity);
		this.playerTwoScore.setOpacity(opacity);
	}
	/**
	 * Method which can relocate the position of
	 * the score number one.
	 * @param x: New x coordinate for the score
	 * @param y: New y coordinate for the score
	 */
	public void relocateScoreOne(double x, double y){
		this.playerOneScore.setX(x);
		this.playerOneScore.setY(y);
	}
	/**
	 * Method which can relocate the position of
	 * the score number two
	 * @param x: New x coordinate for the score
	 * @param y: New y coordinate for the score
	 */
	public void relocateScoreTwo(double x, double y){
		this.playerTwoScore.setX(x);
		this.playerTwoScore.setY(y);
	}
}
