package com.EudyContreras.Snake.PlayRoomHub;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlayerChat {


	public static final String AVAILABLE = "Available";
	public static final String PLAYING = "Playing";
	public static final String UNAVAILABLE = "Unavailable";

	private SimpleStringProperty name;
	private SimpleStringProperty country;
	private SimpleIntegerProperty level;
	private SimpleStringProperty status;
	private SimpleStringProperty content;
	private SimpleObjectProperty<LocalDate> date;

	public PlayerChat() {
		super();
		this.name = new SimpleStringProperty("Unknown");
		this.content = new SimpleStringProperty("");
		this.date = new SimpleObjectProperty<>();
		this.country = new SimpleStringProperty("Unknown");
		this.level = new SimpleIntegerProperty(0);
		this.status = new SimpleStringProperty("Unknown");
	}

	public PlayerChat(String name, String country, int level, String content, LocalDate date, String status) {
		super();
		this.name = new SimpleStringProperty(name);
		this.content = new SimpleStringProperty(content);
		this.date = new SimpleObjectProperty<>(date);
		this.country = new SimpleStringProperty(country);
		this.level = new SimpleIntegerProperty(level);
		this.status = new SimpleStringProperty(status);
	}

	public String getName() {
		return name.get();
	}

	public void yer(String name) {
		this.name.set(name);
		;
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

	public String getContent() {
		return content.get();
	}

	public void setContent(String content) {
		this.content.set(content);
	}

	public String getStatus(){
		return status.get();
	}

	public void setStatus(String status){
		this.status.set(status);
	}

	public LocalDate getDate() {
		return date.get();
	}

	public void setDate(LocalDate date) {
		this.date.set(date);
	}

	@Override
	public String toString(){
		return "Name: "	+ name.get()+" Country: "+country.get();
	}
}
