package com.EudyContreras.Snake.PlayRoomHub;

import java.util.Collections;
import java.util.LinkedList;

import com.EudyContreras.Snake.Application.GameManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ConnectUsers{

	private LinkedList<ConnectedUser> userData;
	private ObservableList<ConnectedUser> observableData;
	private TableView<ConnectedUser> userTable;
	private GameManager game;
	private StackPane container;
	private double width;
	private double height;


	public ConnectUsers(GameManager game){
		super();
		this.game = game;
		initialize();
		createColumns();
	}

	private void initialize(){
		container = new StackPane();
		userData = new LinkedList<>();
		userTable = new TableView<>();
		setSize(490,500);
		observableData = FXCollections.observableArrayList(userData);
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
        statusCol.setPrefWidth(100);
        statusCol.setCellValueFactory(new PropertyValueFactory<ConnectedUser, String>("status"));
//        statusCol.setCellFactory(new Callback<TableColumn<ConnectedUser, String>, TableCell<ConnectedUser, String>>() {
//            public TableCell<ConnectedUser, String> call(TableColumn<ConnectedUser, String> param) {
//                return new TableCell<ConnectedUser, String>() {
//
//                	final StatusIndicator circle = new StatusIndicator(10);
//
//                    @Override
//                    public void updateItem(final String status, boolean empty) {
//                        super.updateItem(status, empty);
//                        if (!isEmpty()) {
//                            if(status!=null){
//                            	setGraphic(circle.getIndicator(status));
//                            }
//                        }else{
//                        	setGraphic(circle.getIndicator(ConnectedUser.UNAVAILABLE));
//                        }
//                    }
//                };
//            }
//        });


        userTable.getColumns().addAll(idCol, nameCol, countryCol, levelCol, statusCol);

        userTable.setItems(observableData);

        ConnectedUser[] users = new ConnectedUser[10000];

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

        userTable.setPickOnBounds(false);

        container.getChildren().add(userTable);
	}

	public StackPane get(){
		return container;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
		container.setPrefWidth(width);
		container.setMaxWidth(width);
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

	public void setHeight(double height) {
		this.height = height;
		container.setPrefHeight(height);
		container.setMaxHeight(height);
	}

	public void addUser(ConnectedUser... user){
		Collections.addAll(observableData, user);
	}

	public TableView<ConnectedUser> getTable() {
		return userTable;
	}

}