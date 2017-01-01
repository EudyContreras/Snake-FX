package com.EudyContreras.Snake.PlayRoomHub;

import java.util.Collections;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ThreadManagers.ThreadManager;
import com.EudyContreras.Snake.Utilities.ShapeUtility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ConnectLeaderboard{

	private ObservableList<ConnectedUser> observableData;
	private TableView<ConnectedUser> userTable;
	private StackPane container;
	private Rectangle shape;
	private double width;
	private double height;


	public ConnectLeaderboard(GameManager game){
		super();
		initialize();
		setSize(935,275);
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
		container.getStylesheets().add(ConnectLeaderboard.class.getResource("connectLeaderboards.css").toExternalForm());
	}



	@SuppressWarnings("unchecked")
	private void createColumns(){

        TableColumn<ConnectedUser, Integer> rankCol = new TableColumn<>("Rank");
        rankCol.setMinWidth(80);
        rankCol.setCellValueFactory( new PropertyValueFactory<>("id"));
        rankCol.setCellFactory(new Callback<TableColumn<ConnectedUser,Integer>,TableCell<ConnectedUser,Integer>>(){
        	@Override
        	public TableCell<ConnectedUser, Integer> call(TableColumn<ConnectedUser, Integer> param) {
        		return new TableCell<ConnectedUser, Integer>(){

        			ImageView imageView = new ImageView();

        			@Override
        			public void updateItem(Integer item, boolean empty) {
        				if(item!=null){
        					ConnectedUser user = getTableView().getItems().get(getIndex());
        					StackPane frame = new StackPane();
        					GameLabel label = new GameLabel(user.getId()+"");
        					label.getLabel(user.getId()+"").setTranslateY(-10);
        					label.setFill(user.getId()+"", Color.WHITE);
        					imageView.setImage(GameImageBank.award_icon_gold);
        					imageView.setPreserveRatio(true);
        					imageView.setFitHeight(60);
        					imageView.setFitWidth(60);
        					frame.getChildren().addAll(imageView,label.getLabel(user.getId()+""));
        					setGraphic(frame);
        				}
        			}
        		};
        	}
        });

        TableColumn<ConnectedUser, String> userInfo = new TableColumn<>("Player");
        userInfo.setCellValueFactory(new PropertyValueFactory<>("name"));
        userInfo.setMinWidth(100);
        userInfo.setSortable(false);
        userInfo.setCellFactory(new Callback<TableColumn<ConnectedUser,String>,TableCell<ConnectedUser,String>>(){
        	@Override
        	public TableCell<ConnectedUser, String> call(TableColumn<ConnectedUser, String> param) {
        		return new TableCell<ConnectedUser, String>(){
        			ImageView imageView = new ImageView();
        			@Override
        			public void updateItem(String item, boolean empty) {
        				if(item!=null){
        					imageView.setImage(GameImageBank.profile_default_male);
        					imageView.setPreserveRatio(true);
        					imageView.setFitHeight(60);
        					imageView.setFitWidth(60);
        					setPadding(new Insets(0,20,0,20));
        					setGraphic(imageView);
        				}
        			}
        		};
        	}
        });

        TableColumn<ConnectedUser, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(100);
        nameCol.setSortable(false);
        nameCol.setCellFactory(new Callback<TableColumn<ConnectedUser,String>,TableCell<ConnectedUser,String>>(){
        	@Override
        	public TableCell<ConnectedUser, String> call(TableColumn<ConnectedUser, String> param) {
        		return new TableCell<ConnectedUser, String>(){

        			@Override
        			public void updateItem(String item, boolean empty) {
        				if(item!=null){
        					Text text = new Text();
        					text.setText(""+item);
        					text.setFont(Font.font(null,FontWeight.EXTRA_BOLD,16));
        					text.setFill(ShapeUtility.LINEAR_GRADIENT(Color.WHITE));
        					setGraphic(text);
        				}
        			}
        		};
        	}
        });

        TableColumn<ConnectedUser, String> countryCol = new TableColumn<>("Country");
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryCol.setMinWidth(100);
        countryCol.setSortable(false);
        countryCol.setCellFactory(new Callback<TableColumn<ConnectedUser,String>,TableCell<ConnectedUser,String>>(){
        	@Override
        	public TableCell<ConnectedUser, String> call(TableColumn<ConnectedUser, String> param) {
        		return new TableCell<ConnectedUser, String>(){

        			@Override
        			public void updateItem(String item, boolean empty) {
        				if(item!=null){
        					setFont(Font.font(null,FontWeight.EXTRA_BOLD,16));
        					setTextFill(Color.WHITE);
        					setText(item+"");
        				}
        			}
        		};
        	}
        });


        TableColumn<ConnectedUser, Integer> levelCol = new TableColumn<>("Level");
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        levelCol.setMinWidth(100);
        levelCol.setSortable(false);
        levelCol.setCellFactory(new Callback<TableColumn<ConnectedUser,Integer>,TableCell<ConnectedUser,Integer>>(){
        	@Override
        	public TableCell<ConnectedUser, Integer> call(TableColumn<ConnectedUser, Integer> param) {
        		return new TableCell<ConnectedUser, Integer>(){

        			@Override
        			public void updateItem(Integer item, boolean empty) {
        				if(item!=null){
        					setFont(Font.font(null,FontWeight.EXTRA_BOLD,16));
        					setTextFill(Color.WHITE);
        					setText(item+"");
        				}
        			}
        		};
        	}
        });

        TableColumn<ConnectedUser, Double> winRatioCol = new TableColumn<>("Win Ratio");
        winRatioCol.setMinWidth(120);
        winRatioCol.setCellValueFactory(new PropertyValueFactory<ConnectedUser, Double>("winLooseRatio"));
        winRatioCol.setCellFactory(new Callback<TableColumn<ConnectedUser,Double>,TableCell<ConnectedUser,Double>>(){
        	@Override
        	public TableCell<ConnectedUser, Double> call(TableColumn<ConnectedUser, Double> param) {
        		return new TableCell<ConnectedUser, Double>(){

        			@Override
        			public void updateItem(Double item, boolean empty) {
        				if(item!=null){
        					setFont(Font.font(null,FontWeight.EXTRA_BOLD,16));
        					setTextFill(Color.WHITE);
        					setText(item+"");
        				}
        			}
        		};
        	}
        });

        TableColumn<ConnectedUser, Integer> highScoreCol = new TableColumn<>("High Score");
        highScoreCol.setMinWidth(150);
        highScoreCol.setCellValueFactory(new PropertyValueFactory<ConnectedUser, Integer>("highScore"));
        highScoreCol.setCellFactory(new Callback<TableColumn<ConnectedUser,Integer>,TableCell<ConnectedUser,Integer>>(){
        	@Override
        	public TableCell<ConnectedUser, Integer> call(TableColumn<ConnectedUser, Integer> param) {
        		return new TableCell<ConnectedUser, Integer>(){

        			@Override
        			public void updateItem(Integer item, boolean empty) {
        				if(item!=null){
        					ImageView imageView = new ImageView(GameImageBank.apple);
                			Text text = new Text();
                			HBox container = new HBox(10);
        					text.setText("x "+item);
        					text.setFont(Font.font(null,FontWeight.EXTRA_BOLD,16));
        					text.setFill(ShapeUtility.LINEAR_GRADIENT(Color.WHITE));
        					imageView.setFitWidth(50);
        					imageView.setFitHeight(50);
        					imageView.setPreserveRatio(true);
        					container.getChildren().addAll(imageView,text);
        					container.setAlignment(Pos.CENTER);
        					setPadding(new Insets(0,20,0,20));
        					setGraphic(container);
        				}
        			}
        		};
        	}
        });

        TableColumn<ConnectedUser, Integer> goldAppleCol = new TableColumn<>("Golden Apples");
        goldAppleCol.setMinWidth(150);
        goldAppleCol.setCellValueFactory(new PropertyValueFactory<ConnectedUser, Integer>("goldenApples"));
        goldAppleCol.setCellFactory(new Callback<TableColumn<ConnectedUser,Integer>,TableCell<ConnectedUser,Integer>>(){
        	@Override
        	public TableCell<ConnectedUser, Integer> call(TableColumn<ConnectedUser, Integer> param) {
        		return new TableCell<ConnectedUser, Integer>(){

        			@Override
        			public void updateItem(Integer item, boolean empty) {
        				if(item!=null){
        					ImageView imageView = new ImageView(GameImageBank.apple_gold);
                			Text text = new Text();
                			HBox container = new HBox(10);
        					text.setText("x "+item);
        					text.setFont(Font.font(null,FontWeight.EXTRA_BOLD,16));
        					text.setFill(ShapeUtility.LINEAR_GRADIENT(Color.WHITE));
        					imageView.setFitWidth(50);
        					imageView.setFitHeight(50);
        					imageView.setPreserveRatio(true);
        					container.getChildren().addAll(imageView,text);
        					container.setAlignment(Pos.CENTER);
        					setPadding(new Insets(0,20,0,20));
        					setGraphic(container);
        				}
        			}
        		};
        	}
        });

        userTable.getColumns().addAll(rankCol, userInfo, nameCol, countryCol, levelCol, winRatioCol, highScoreCol, goldAppleCol);

        userTable.setItems(observableData);

      	ConnectedUser[] users = new ConnectedUser[20];

      	ThreadManager.performeScript(() -> {

			for (int i = 0; i < users.length; i++) {
				users[i] = new ConnectedUser(i, "User" + i, "Nowhere", i, ConnectedUser.AVAILABLE);
				ConnectedUser user = users[i];
				user.setGoldenApples(44);
				user.setWinLooseRatio(3.0);
				user.setHighScore(4444);
			}

			addUser(users);
		});

        userTable.setPickOnBounds(false);

        container.getChildren().add(userTable);
	}

	public StackPane get(){
		return container;
	}

	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
		shape.setWidth(width);
		shape.setHeight(height);
		container.setPrefSize(width,height);
		container.setMaxSize(width, height);
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

	public void addUser(ConnectedUser... user){
		Collections.addAll(observableData, user);
	}

	public TableView<ConnectedUser> getTable() {
		return userTable;
	}

}