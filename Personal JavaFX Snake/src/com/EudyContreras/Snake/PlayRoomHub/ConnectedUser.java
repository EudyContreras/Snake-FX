package com.EudyContreras.Snake.PlayRoomHub;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class ConnectedUser {

	public static final String AVAILABLE = "Available";
	public static final String PLAYING = "Playing";
	public static final String UNAVAILABLE = "Unavailable";

	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty country;
	private SimpleIntegerProperty level;
	private SimpleIntegerProperty highScore;
	private SimpleIntegerProperty goldenApples;
	private SimpleDoubleProperty winLooseRatio;
	private SimpleStringProperty status;

	public ConnectedUser() {
		this.id = new SimpleIntegerProperty(0);
		this.name = new SimpleStringProperty("Unknown");
		this.country = new SimpleStringProperty("Unknown");
		this.level = new SimpleIntegerProperty(0);
		this.status = new SimpleStringProperty("Unknown");
		this.highScore = new SimpleIntegerProperty(0);
		this.goldenApples = new SimpleIntegerProperty(0);
		this.winLooseRatio = new SimpleDoubleProperty(0);
	}

	public ConnectedUser(int id, String name, String country, int level, String status) {
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.country = new SimpleStringProperty(country);
		this.level = new SimpleIntegerProperty(level);
		this.status = new SimpleStringProperty(status);
		this.highScore = new SimpleIntegerProperty(0);
		this.goldenApples = new SimpleIntegerProperty(0);
		this.winLooseRatio = new SimpleDoubleProperty(0);
	}

	public int getId() {
		return id.get();
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getCountry() {
		return country.get();
	}

	public void setCountry(String country) {
		this.country.set(country);
	}

	public int getLevel(){
		return level.get();
	}

	public void setLevel(int level){
		this.level.set(level);
	}

	public int getGoldenApples(){
		return goldenApples.get();
	}

	public void setGoldenApples(int apples){
		this.goldenApples.set(apples);
	}

	public double getWinLooseRatio(){
		return winLooseRatio.get();
	}

	public void setWinLooseRatio(double ratio){
		this.winLooseRatio.set(ratio);
	}

	public int getHighScore(){
		return highScore.get();
	}

	public void setHighScore(int score){
		this.highScore.set(score);
	}

	public String getStatus(){
		return status.get();
	}

	public void setStatus(String status){
		this.status.set(status);
	}

}