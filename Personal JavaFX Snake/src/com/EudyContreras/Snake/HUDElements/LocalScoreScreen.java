package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.AbstractModels.AbstractHudElement;
import com.EudyContreras.Snake.FrameWork.GameManager;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LocalScoreScreen extends AbstractHudElement{


	private GameManager game;
	private DropShadow dropShadowOne;
	private ScoreBoard boardOne;
	private ScoreBoard boardTwo;
	private Text playerOneScore;
	private Text playerOneTime;
	private Text playerTwoScore;
	private Text playerTwoTime;
	private Pane layer;


	public LocalScoreScreen(GameManager game, double xOne, double yOne, double xTwo, double yTwo, Pane layer){
		this.game = game;
		this.layer = layer;
		this.xOne = xOne/GameManager.ScaleX;
		this.xTwo = xTwo/GameManager.ScaleX;
		this.yOne = yOne/GameManager.ScaleY;
		this.boardOne = game.getScoreBoardOne();
		this.boardTwo = game.getScoreBoardTwo();
		this.initialize();
	}
	private void initialize(){
        this.dropShadowOne = new DropShadow();
		this.playerOneScore = new Text();
		this.playerTwoScore = new Text();
		this.playerOneTime = new Text();
		this.playerTwoTime = new Text();
		this.playerOneScore.setX(xOne);
		this.playerOneScore.setY(yOne);
		this.playerTwoScore.setX(xTwo);
		this.playerTwoScore.setY(yTwo);
		this.playerOneScore.setFont(Font.font(null,FontWeight.BOLD, GameManager.ScaleX(185)));
		this.playerTwoScore.setFont( Font.font(null,FontWeight.BOLD, GameManager.ScaleX(185)));
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
	public void setScores(){
		this.playerOneScore.setOpacity(0);
		this.playerTwoScore.setOpacity(0);
		this.playerOneScore.setText(""+boardOne.getScore());
		this.playerTwoScore.setText(""+boardTwo.getScore());
		this.layer.getChildren().remove(playerOneScore);
		this.layer.getChildren().remove(playerTwoScore);
		this.layer.getChildren().add(playerOneScore);
		this.layer.getChildren().add(playerTwoScore);
	}
	public void showScores(){
		this.playerOneScore.setVisible(true);
		this.playerTwoScore.setVisible(true);
	}
	public void setScoreOpacity(double opacity){
		this.playerOneScore.setOpacity(opacity);
		this.playerTwoScore.setOpacity(opacity);
	}
	public void relocateScoreOne(double x, double y){
		this.playerOneScore.setX(x);
		this.playerOneScore.setY(y);
	}
	public void relocateScoreTwo(double x, double y){
		this.playerTwoScore.setX(x);
		this.playerTwoScore.setY(y);
	}
	public void translateScorePosition(double velocity){
		this.playerOneScore.setX(playerOneScore.getX()+velocity);
		this.playerTwoScore.setX(playerTwoScore.getX()+velocity);
	}
}
