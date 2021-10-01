package com.EudyContreras.Snake.PlayRoomHub;

import java.util.Collections;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.ThreadManagers.ThreadManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ConnectUsers{

	private ObservableList<ConnectedUser> observableData;
	private TableView<ConnectedUser> userTable;
	private StackPane container;
	private double width;
	private double height;


	public ConnectUsers(GameManager game){
		super();
		initialize();
		setSize(475,350);
		createColumns();
	}

	private void initialize(){
		container = new StackPane();
		userTable = new TableView<>();
		observableData = FXCollections.observableArrayList();
		container.setId("container");
		container.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
		container.getStylesheets().add(ConnectUsers.class.getResource("connectUsers.css").toExternalForm());
	}

	@SuppressWarnings("unchecked")
	private void createColumns(){
        TableColumn<ConnectedUser, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        TableColumn<ConnectedUser, String> nameCol = new TableColumn<>("Name");
        nameCol.setPrefWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ConnectedUser, String> countryCol = new TableColumn<>("Country");
        countryCol.setPrefWidth(100);
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));

        TableColumn<ConnectedUser, Integer> levelCol = new TableColumn<>("Level");
        levelCol.setPrefWidth(100);
        levelCol.setCellValueFactory( new PropertyValueFactory<>("level"));

        TableColumn<ConnectedUser, String> statusCol = new TableColumn<>("Status");
        statusCol.setPrefWidth(85);
        statusCol.setCellValueFactory(new PropertyValueFactory<ConnectedUser, String>("status"));
        statusCol.setCellFactory(new Callback<TableColumn<ConnectedUser, String>, TableCell<ConnectedUser, String>>() {
            public TableCell<ConnectedUser, String> call(TableColumn<ConnectedUser, String> param) {
                return new TableCell<ConnectedUser, String>() {
                    @Override
                    public void updateItem(final String status, boolean empty) {
                        super.updateItem(status, empty);
                        if (!isEmpty()) {
							if (status != null) {
//								HBox container = new HBox(10);
//								Text text = new Text(status);
								StatusIndicator circle = new StatusIndicator(10);
								circle.getIndicator(status);
//								text.setFill(Color.WHITE);
//								container.setAlignment(Pos.CENTER_LEFT);
//								container.getChildren().addAll(circle, text);
								setGraphic(circle);
//								setTextAlignment(TextAlignment.LEFT);
//								setAlignment(Pos.CENTER_LEFT);
                            }
                        }
                    }
                };
            }
        });


        userTable.getColumns().addAll(idCol, nameCol, countryCol, levelCol, statusCol);

        userTable.setItems(observableData);

      	ConnectedUser[] users = new ConnectedUser[10000];

      	 ThreadManager.performeScript(()->{

            for(int i = 0; i<users.length; i++){
            	if(i%2==0 && i%5!=0){
            		users[i] = new ConnectedUser(i, "User"+i, "Nowhere", i, ConnectedUser.AVAILABLE);
            	}else if(i%2!=0 && i%5!=0){
            		users[i] = new ConnectedUser(i, "User"+i, "Nowhere", i, ConnectedUser.PLAYING);
            	}else if(i%5==0){
            		users[i] = new ConnectedUser(i, "User"+i, "Nowhere", i, ConnectedUser.UNAVAILABLE);
            	}
            }

            addUser(users);
       });

        userTable.setPickOnBounds(false);

        container.getChildren().add(userTable);
	}

	public StackPane get(){
		return container;
	}

	public void setWidth(double width) {
		this.width = width;
		container.setPrefWidth(width);
		container.setMaxWidth(width);
	}

	public void setHeight(double height) {
		this.height = height;
		container.setPrefHeight(height);
		container.setMaxHeight(height);
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
		container.setPrefSize(width,height);
		container.setMaxSize(width, height);
	}

	public void addUser(ConnectedUser... user){
		Collections.addAll(observableData, user);
	}

	public TableView<ConnectedUser> getTable() {
		return userTable;
	}

}