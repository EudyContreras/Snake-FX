package com.EudyContreras.Snake.PlayRoomHub;

import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameButton {

	private HBox layout;
	private StackPane container;
	private HashMap<String, Button> buttons;

	public GameButton(String... text){
		this.layout = new HBox(5);
		this.buttons = new HashMap<>();
		this.container = new StackPane();

		layout.setAlignment(Pos.CENTER);
		for(int i = 0; i<text.length; i++){
			Button button = new Button(text[i]);
			buttons.put(text[i], button);
			layout.getChildren().add(button);
		}
		container.getChildren().add(layout);
	}

	public GameButton(int spacing, String... text){
		this.layout = new HBox(spacing);
		this.buttons = new HashMap<>();
		this.container = new StackPane();

		layout.setAlignment(Pos.CENTER);
		for(int i = 0; i<text.length; i++){
			Button button = new Button(text[i]);
			buttons.put(text[i], button);
			layout.getChildren().add(button);
		}
		container.getChildren().add(layout);
	}

	public StackPane get(){
		return container;
	}

	public void setFont(String label, Font font){
		buttons.get(label).setFont(font);
	}

	public void setFill(String label, Paint fill){
		buttons.get(label).setTextFill(fill);
	}

	public void setAlignment(String label, TextAlignment alignment){
		buttons.get(label).setTextAlignment(alignment);
	}

	public void setStyle(String label, String style){
		buttons.get(label).setStyle(style);
	}

	public void setSpacing(int spacing){
		this.layout.setSpacing(spacing);
	}

	public Button getLabel(String label){
		return buttons.get(label);
	}

}
