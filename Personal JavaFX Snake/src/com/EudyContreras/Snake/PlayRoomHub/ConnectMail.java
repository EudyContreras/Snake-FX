/* ....Show License.... */

package com.EudyContreras.Snake.PlayRoomHub;



import javafx.application.Application;

import javafx.beans.value.ChangeListener;

import javafx.beans.value.ObservableValue;

import javafx.geometry.Pos;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.scene.control.Toggle;

import javafx.scene.control.ToggleButton;

import javafx.scene.control.ToggleGroup;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



/**

 * This sample demonstrates styling toggle buttons with CSS.

 */

public class ConnectMail extends Application {


	 private ConnectFriends friends;
	 private ToggleGroup group;
	 private ToggleButton[] tabs;
	 private VBox contentContainer;
	 private HBox tabContainer;

	 public ConnectMail(){
		 group = new ToggleGroup();
		 tabContainer = new HBox();
		 contentContainer = new VBox();
		 friends = new ConnectFriends(null);
		 createTabs(170,"Chats","Requests","Mail");
	 }

    public Parent createContent() {
    	tabContainer.setAlignment(Pos.CENTER);
    	tabContainer.getChildren().addAll(tabs);
    	contentContainer.getChildren().addAll(tabContainer, friends.get());
        return contentContainer;
    }


    private void createTabs(double width, String...names){
		tabs = new ToggleButton[names.length];

		for (int i = 0; i < tabs.length; i++) {
			tabs[i] = new ToggleButton(names[i]);
			tabs[i].setToggleGroup(group);
			tabs[i].setPrefSize(width, 45);
			tabs[i].setMinSize(width, 45);
		}

		group.selectToggle(tabs[0]);

		group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
			if (newValue == null) {
				group.selectToggle(oldValue);
			}
		});
	}

    @Override

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setScene(new Scene(createContent()));

        primaryStage.show();

    }


    public static void main(String[] args) {

        launch(args);

    }

}