package com.EudyContreras.Snake.PlayRoomHub;

import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ConnectButtons {

	private StackPane container;
	private HashMap<String, Button> buttons;

	public ConnectButtons(Alignment alignment, int buttonWidth, String...names){
		buttons = new HashMap<>();
		container = new StackPane();

		switch(alignment){
		case HORIZONTAL:
			HBox layoutH = new HBox(4);
			layoutH.setPadding(new Insets(2,10,2,10));
			for(int i = 0; i<names.length; i++){
				Button button = new Button(names[i]);
				button.setMinWidth(buttonWidth-layoutH.getSpacing());
				buttons.put(names[i],button);
				layoutH.getChildren().add(button);
			}
			container.getChildren().add(layoutH);
			break;
		case VERTICAL:
			VBox layoutV = new VBox(4);
			layoutV.setPadding(new Insets(2,10,2,10));
			for(int i = 0; i<names.length; i++){
				Button button = new Button(names[i]);
				button.setMinWidth(buttonWidth-layoutV.getSpacing());
				buttons.put(names[i],button);
				layoutV.getChildren().add(button);
			}
			container.getChildren().add(layoutV);
			break;
		}

	}

	public Button get(String name){
		return buttons.get(name);
	}

	public StackPane get(){
		return container;
	}

	public enum Alignment{
		VERTICAL, HORIZONTAL
	}

}
