package com.EudyContreras.Snake.PlayRoomHub;

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
	private SimpleStringProperty status;

	public ConnectedUser(int id, String name, String country, int level, String status) {
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.country = new SimpleStringProperty(country);
		this.level = new SimpleIntegerProperty(level);
		this.status = new SimpleStringProperty(status);
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

	public String getStatus(){
		return status.get();
	}

	public void setStatus(String status){
		this.status.set(status);
	}

}