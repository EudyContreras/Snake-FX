package com.EudyContreras.Snake.Utilities;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class GUIElements extends ToolBar {

	public static Slider createSlider(String id) {

		Slider slider = new Slider();
		slider.setId(id);

		return slider;
	}

	public static HBox createButtons(String id, int amount, int width, int height) {
		Rectangle[] button = new Rectangle[3];
		DropShadow glow = new DropShadow();
		HBox buttonBox = new HBox(10);

		glow.setSpread(.6);
		glow.setRadius(10);

		buttonBox.setId(id);
		buttonBox.setAlignment(Pos.TOP_RIGHT);

		button[0] = new Rectangle();
		button[0].setFill(new ImagePattern(GameImageBank.minimize));
		button[0].setWidth(width);
		button[0].setHeight(height);
		button[0].setOnMouseEntered(e ->{
			glow.setColor(Color.DODGERBLUE);
			button[0].setEffect(glow);
		});
		button[0].setOnMouseExited(e ->{
			button[0].setEffect(null);
		});
		button[0].setOnMouseReleased(e ->{
			GameManager.minimize();
		});

		button[1] = new Rectangle();
		button[1].setFill(new ImagePattern(GameImageBank.maximize));
		button[1].setWidth(width);
		button[1].setHeight(height);
		button[1].setOnMouseEntered(e ->{
			glow.setColor(Color.DODGERBLUE);
			button[1].setEffect(glow);
		});
		button[1].setOnMouseExited(e ->{
			button[1].setEffect(null);
		});
		button[1].setOnMouseReleased(e ->{
			GameManager.maximize();
		});

		button[2] = new Rectangle();
		button[2].setFill(new ImagePattern(GameImageBank.exit));
		button[2].setWidth(width*1.5);
		button[2].setHeight(height);
		button[2].setOnMouseEntered(e ->{
			glow.setColor(Color.RED);
			button[2].setEffect(glow);
		});
		button[2].setOnMouseExited(e ->{
			button[2].setEffect(null);
		});
		button[2].setOnMouseReleased(e ->{
			GameManager.exit();
		});

		buttonBox.getChildren().setAll(button);

		return buttonBox;
	}

	public static Label createLabel(String id, String text) {

		Label label = new Label();
		label.setId(id);
		label.setText(text);

		return label;
	}

	public static ComboBox<String> createComboBox(String prompText) {
		ObservableList<String> strings = FXCollections.observableArrayList(

				"layer 1", "layer 2", "layer 3",

				"layer 4", "layer 5", "layer 6",

				"layer 7", "layer 8", "layer 9",

				"layer 10");

		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(15);

		ComboBox<String> comboBox = new ComboBox<String>();

		comboBox.setId("uneditable-combobox");
		comboBox.setEditable(false);
		comboBox.setPromptText(prompText);
		comboBox.setItems(strings);

		return comboBox;
	}

	public static BorderPane createTopBar() {
		BorderPane pane = new BorderPane();
		Label label = createLabel(Styles.TOLLBAR, "");
		HBox hBox = createButtons(Styles.BUTTON, 3, 30,20);
		pane.setPrefHeight(25);
		pane.setId(Styles.LABEL);
		label.setId(Styles.LABEL);
		pane.setLeft(label);
		pane.setRight(hBox);

		return pane;
	}



}
