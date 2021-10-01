/* ....Show License.... */

package com.EudyContreras.Snake.PlayRoomHub;



import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;



/**

 * This sample demonstrates styling toggle buttons with CSS.

 */

public class ConnectMail {


	private static final String CHAT_TAB = "Chats";
	private static final String REQUESTS_TAB = "Requests";
	private static final String MAIL_TAB = "Mail";

	 private ConnectMailChat chat;
	 private ConnectMailRequest requests;
	 private ToggleGroup group;
	 private ToggleButton[] tabs;
	 private StackPane content;
	 private VBox contentContainer;
	 private HBox tabContainer;

	 public ConnectMail(){
		 group = new ToggleGroup();
		 tabContainer = new HBox();
		 contentContainer = new VBox();
		 content = new StackPane();
		 chat = new ConnectMailChat(null);
		 requests = new ConnectMailRequest(null);
		 createTabs(170,CHAT_TAB,REQUESTS_TAB,MAIL_TAB);
		 createContent();
		 chat.setSize(555,450);
		 requests.setSize(555,450);
	 }

    public Parent createContent() {
    	tabContainer.setAlignment(Pos.CENTER);
    	tabContainer.getChildren().addAll(tabs);
    	contentContainer.setAlignment(Pos.CENTER);
    	contentContainer.getChildren().addAll(tabContainer, content);
    	contentContainer.getStylesheets().add(ConnectMail.class.getResource("connectMailChat.css").toExternalForm());
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

		group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
			if (newValue == null) {
				group.selectToggle(oldValue);
			}else{
				switchContent(((ToggleButton)newValue).getText());
			}
		});

		group.selectToggle(tabs[0]);
	}

    private void switchContent(String name) {
		switch(name){
		case CHAT_TAB:
			content.getChildren().clear();
			content.getChildren().add(chat.get());
			break;
		case REQUESTS_TAB:
			content.getChildren().clear();
			content.getChildren().add(requests.get());
			break;
		case MAIL_TAB:
			break;
		}
	}

    public Parent get(){
    	return contentContainer;
    }


}