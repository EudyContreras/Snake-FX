package com.EudyContreras.Snake.PlayRoomHub;

import java.util.Collections;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayRoomHub.ConnectButtons.Alignment;
import com.EudyContreras.Snake.ThreadManagers.ThreadManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

public class ConnectFriends{

	private ObservableList<ConnectedUser> observableData;
	private TableView<ConnectedUser> userTable;
	private StackPane container;
	private Rectangle shape;
	private double width;
	private double height;


	public ConnectFriends(GameManager game){
		super();
		initialize();
		setSize(420,310);
		createColumns();
	}

	private void initialize(){

		container = new StackPane();
		userTable = new TableView<>();
		shape = new Rectangle();
		shape.setArcHeight(25);
		shape.setArcWidth(25);
		container.setClip(shape);
		observableData = FXCollections.observableArrayList();
		container.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
		container.getStylesheets().add(ConnectFriends.class.getResource("connectFriends.css").toExternalForm());
		container.getStylesheets().add(ConnectFriends.class.getResource("connectButtons.css").toExternalForm());
	}

	@SuppressWarnings("unchecked")
	private void createColumns(){

        TableColumn<ConnectedUser, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);
        idCol.setCellFactory(new Callback<TableColumn<ConnectedUser,Integer>,TableCell<ConnectedUser,Integer>>(){
        	@Override
        	public TableCell<ConnectedUser, Integer> call(TableColumn<ConnectedUser, Integer> param) {
        		return new TableCell<ConnectedUser, Integer>(){

        			@Override
        			public void updateItem(Integer item, boolean empty) {
        				if(item!=null){
        					setFont(Font.font(null,FontWeight.EXTRA_BOLD,22));
        					setTextFill(Color.WHITE);
        					setText(item+"");
        				}
        			}
        		};
        	}
        });


        TableColumn<ConnectedUser, String> userInfo = new TableColumn<>("Player");
        userInfo.setCellValueFactory(new PropertyValueFactory<>("name"));
        userInfo.setCellFactory(new Callback<TableColumn<ConnectedUser,String>,TableCell<ConnectedUser,String>>(){
        	@Override
        	public TableCell<ConnectedUser, String> call(TableColumn<ConnectedUser, String> param) {
        		return new TableCell<ConnectedUser, String>(){
        			ImageView imageView = new ImageView();
        			@Override
        			public void updateItem(String item, boolean empty) {
        				if(item!=null){
        					ConnectedUser user = getTableView().getItems().get(getIndex());
        					HBox hBox= new HBox();
        					VBox vBox = new VBox();
        					GameLabel name = new GameLabel(user.getName());
        					GameLabel country = new GameLabel(user.getCountry());
        					GameLabel level = new GameLabel(user.getLevel()+"");

        					name.setFill(user.getName(), Color.WHITE.brighter());
        					name.setFont(user.getName(), Font.font(null,FontWeight.EXTRA_BOLD,16));
        					country.setFill(user.getCountry(), Color.WHITE.brighter());
        					country.setFont(user.getCountry(), Font.font(12));
        					level.setFill(user.getLevel()+"", Color.WHITE.brighter());
        					level.setFont(user.getLevel()+"", Font.font(12));

        					hBox.setSpacing(10) ;
        					vBox.setSpacing(0);
        					vBox.getChildren().add(name.get());
        					vBox.getChildren().add(country.get());
        					vBox.getChildren().add(level.get());

        					imageView.setImage(GameImageBank.profile_default_male);
        					imageView.setPreserveRatio(true);
        					imageView.setFitHeight(60);
        					imageView.setFitWidth(60);

        					hBox.getChildren().addAll(imageView,vBox);
//        					setBackground(FillUtility.PAINT_FILL(FillUtility.LINEAR_GRADIENT(Color.BLACK)));
        					setPadding(new Insets(0,30,0,30));
        					setGraphic(hBox);
        				}
        			}
        		};
        	}
        });

        TableColumn<ConnectedUser, String> actionCol = new TableColumn<>( "Action" );
        actionCol.setCellValueFactory( new PropertyValueFactory<>( "action" ) );
        actionCol.setCellFactory(new Callback<TableColumn<ConnectedUser, String>, TableCell<ConnectedUser, String>>() {
			@Override
			public TableCell<ConnectedUser, String> call(final TableColumn<ConnectedUser, String> param) {
				return new TableCell<ConnectedUser, String>() {

					ConnectButtons buttons = new ConnectButtons(Alignment.HORIZONTAL,170/3, "Unfriend", "Chat", "Play");

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							buttons.get("Unfriend").setId("button");
							buttons.get("Unfriend").setOnAction((ActionEvent event) -> {
								ConnectedUser user = getTableView().getItems().get(getIndex());
								System.out.println(user.getName() + " : Unfriend!");
							});
							buttons.get("Chat").setId("button");
							buttons.get("Chat").setOnAction((ActionEvent event) -> {
								ConnectedUser user = getTableView().getItems().get(getIndex());
								System.out.println(user.getName() + " : Chat!");
							});
							buttons.get("Play").setId("button");
							buttons.get("Play").setOnAction((ActionEvent event) -> {
								ConnectedUser user = getTableView().getItems().get(getIndex());
								System.out.println(user.getName() + " : Play!");
							});
							setGraphic(buttons.get());
							setText(null);
						}
					}
				};
			}
		});


        TableColumn<ConnectedUser, String> statusCol = new TableColumn<>("Status");
        statusCol.setMinWidth(130);
        statusCol.setCellValueFactory(new PropertyValueFactory<ConnectedUser, String>("status"));
        statusCol.setCellFactory(new Callback<TableColumn<ConnectedUser, String>, TableCell<ConnectedUser, String>>() {
            public TableCell<ConnectedUser, String> call(TableColumn<ConnectedUser, String> param) {
                return new TableCell<ConnectedUser, String>() {

                	final StatusIndicator circle = new StatusIndicator(10);

                    @Override
                    public void updateItem(final String status, boolean empty) {
                        super.updateItem(status, empty);
                        if (!isEmpty()) {
                            if(status!=null){
                            	setGraphic(circle.getIndicator(status));
                            }else{
                            	setGraphic(circle.getIndicator(ConnectedUser.UNAVAILABLE));
                            }
                        }else{
                        	setGraphic(null);
                        }
                    }
                };
            }
        });

        TableColumn<ConnectedUser, String> checkCol = new TableColumn<>("Select");
        checkCol.setMinWidth(70);
        checkCol.setCellValueFactory(new PropertyValueFactory<ConnectedUser, String>(""));
        checkCol.setCellFactory(new Callback<TableColumn<ConnectedUser, String>, TableCell<ConnectedUser, String>>() {
            public TableCell<ConnectedUser, String> call(TableColumn<ConnectedUser, String> param) {
                return new TableCell<ConnectedUser, String>() {

                    @Override
                    public void updateItem(final String status, boolean empty) {
                        super.updateItem(status, empty);
                        if (!isEmpty()) {
                        	CheckBox checkBox = new CheckBox();
                        	setGraphic(checkBox);
                        }else{
                        	setGraphic(null);
                        }
                    }
                };
            }
        });

    	ConnectedUser[] users = new ConnectedUser[10];

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

        userTable.getColumns().addAll(idCol, userInfo, statusCol, checkCol);

        userTable.setItems(observableData);

        userTable.setPickOnBounds(false);

        container.getChildren().add(userTable);
	}

	public StackPane get(){
		return container;
	}

	public void setWidth(double width) {
		this.width = width;
		shape.setWidth(width);
		container.setPrefWidth(width);
		container.setMaxWidth(width);
	}

	public void setHeight(double height) {
		this.height = height;
		shape.setHeight(height);
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
		shape.setWidth(width);
		shape.setHeight(height);
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