package com.EudyContreras.Snake.PlayRoomHub;

import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameLabel {

	private HBox container;
	private HashMap<String, Label> labels;

	public GameLabel(String... text){
		this.container = new HBox();
		this.labels = new HashMap<>();

		for(int i = 0; i<text.length; i++){
			Label label = new Label(text[i]);
			labels.put(text[i], label);
			container.getChildren().add(label);
		}
	}
	public HBox get(){
		return container;
	}

	public void setFont(String label, Font font){
		labels.get(label).setFont(font);
	}

	public void setFill(String label, Paint fill){
		labels.get(label).setTextFill(fill);
	}

	public void setAlignment(String label, TextAlignment alignment){
		labels.get(label).setTextAlignment(alignment);
	}

	public void setStyle(String label, String style){
		labels.get(label).setStyle(style);
	}

	public void setSpacing(int spacing){
		this.container.setSpacing(spacing);
	}

	public Label getLabel(String label){
		return labels.get(label);
	}

}
