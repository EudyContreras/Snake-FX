package com.EudyContreras.Snake.PlayRoomHub;

import javafx.animation.FadeTransition;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SegmentedButtons {


	private HBox buttonBar = new HBox();
	private VBox container = new VBox();
	private StackPane root = new StackPane();
	private FadeTransition fadeTransition;
	private ConnectMailList connectMailList;
	private ToggleButton[] buttons;


	public SegmentedButtons() {
		connectMailList = new ConnectMailList();
		fadeTransition = new FadeTransition();

		root.setTranslateY(30);
		root.setTranslateX(0);
		root.getStylesheets().add(getClass().getResource("segmented.css").toExternalForm());

		root.setId("background");

		createButtons(160,"News","Notifications","Friend Requests");

		buttonBar.setPadding(new Insets(25,0,15,0));
		buttonBar.setAlignment(Pos.CENTER);
		buttonBar.getStyleClass().setAll("segmented-button-bar");

		buttonBar.getChildren().addAll(buttons);

		container.setAlignment(Pos.CENTER);
		container.getChildren().addAll(buttonBar, connectMailList.get());
		root.getChildren().add(container);

	}

	public void createButtons(double width, String...names){
		ToggleGroup group = new ToggleGroup();
		buttons = new ToggleButton[names.length];

		for(int i = 0; i<buttons.length; i++){
			buttons[i] = new ToggleButton(names[i]);
			buttons[i].setToggleGroup(group);
			buttons[i].setPrefWidth(width);
			buttons[i].setMinWidth(width);
			if(i == 0){
				buttons[i].getStyleClass().addAll("first");
			}
			if(i >= buttons.length-1){
				buttons[i].getStyleClass().addAll("last", "capsule");
			}
		}

		group.selectedToggleProperty()

		.addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
			if (newValue == null) {
				group.selectToggle(oldValue);
			}
		});

		group.selectToggle(buttons[0]);
	}

	public void postNotification(MailItem... items) {
		connectMailList.postNotification(items);
	}


	public Region get() {
		return root;
	}

	public void show(boolean state) {
		if (state) {
			fadeTransition.setNode(container);
			fadeTransition.setDuration(Duration.millis(120));
			fadeTransition.setFromValue(0);
			fadeTransition.setToValue(1);
			fadeTransition.play();
		} else {
			container.setOpacity(0);
		}
	}
}