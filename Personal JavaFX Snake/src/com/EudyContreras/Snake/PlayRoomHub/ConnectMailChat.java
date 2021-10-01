package com.EudyContreras.Snake.PlayRoomHub;

import java.time.LocalDate;
import java.util.Collections;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ThreadManagers.ThreadManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
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
import javafx.util.StringConverter;

public class ConnectMailChat{

	private ObservableList<PlayerChat> observableData;
	private TableRow<PlayerChat> previous;
	private TableView<PlayerChat> userTable;
	private StackPane container;
	private Rectangle shape;
	private double width;
	private double height;


	public ConnectMailChat(GameManager game){
		super();
		initialize();
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
		container.getStylesheets().add(ConnectMailChat.class.getResource("connectMailChat.css").toExternalForm());
	}

	@SuppressWarnings("unchecked")
	private void createColumns(){

        TableColumn<PlayerChat, String> userInfo = new TableColumn<>("Player");
        userInfo.setMinWidth(200);
        userInfo.setCellValueFactory(new PropertyValueFactory<>("name"));
        userInfo.setCellFactory(new Callback<TableColumn<PlayerChat,String>,TableCell<PlayerChat,String>>(){
        	@Override
        	public TableCell<PlayerChat, String> call(TableColumn<PlayerChat, String> param) {
        		return new TableCell<PlayerChat, String>(){
        			ImageView imageView = new ImageView();
        			@Override
        			public void updateItem(String item, boolean empty) {
        				if(item!=null){
        					PlayerChat user = getTableView().getItems().get(getIndex());
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

        TableColumn<PlayerChat, String> statusCol = new TableColumn<>("Status");
        statusCol.setMinWidth(150);
        statusCol.setCellValueFactory(new PropertyValueFactory<PlayerChat, String>("status"));
        statusCol.setCellFactory(new Callback<TableColumn<PlayerChat, String>, TableCell<PlayerChat, String>>() {
            public TableCell<PlayerChat, String> call(TableColumn<PlayerChat, String> param) {
                return new TableCell<PlayerChat, String>() {

                	final StatusIndicator circle = new StatusIndicator(10);

                    @Override
                    public void updateItem(final String status, boolean empty) {
                        super.updateItem(status, empty);
                        if (!isEmpty()) {
                            if(status!=null){
                            	setGraphic(circle.getIndicator(status));
                            }else{
                            	setGraphic(circle.getIndicator(PlayerChat.UNAVAILABLE));
                            }
                        }else{
                        	setGraphic(null);
                        }
                    }
                };
            }
        });


        TableColumn<PlayerChat, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setPrefWidth(150);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>(){
			@Override
			public String toString(LocalDate object) {
				if (null == object)
					return "";
				return object.toString();
			}

			@Override
			public LocalDate fromString(String value) {
				if (value == null) {
					return null;
				}
				return LocalDate.parse(value);
			}
		}));

    	PlayerChat[] users = new PlayerChat[10];

        ThreadManager.performeScript(()->{

            for(int i = 0; i<users.length; i++){
            	if(i%2==0 && i%5!=0){
            		users[i] = new PlayerChat("User"+i, "Nowhere", i, "dfds kkkkkkkkkkkkkkkkkk  kkkkkkkkkkkkkkkkkkkfd",LocalDate.now(), PlayerChat.AVAILABLE);
            	}else if(i%2!=0 && i%5!=0){
            		users[i] = new PlayerChat("User"+i, "Nowhere", i, "dfdsfd",LocalDate.now(), PlayerChat.PLAYING);
            	}else if(i%5==0){
            		users[i] = new PlayerChat("User"+i, "Nowhere", i, "dfdsfd",LocalDate.now(), PlayerChat.UNAVAILABLE);
            	}
            }

            addUser(users);
       });



        userTable.getColumns().addAll(userInfo, statusCol, dateCol);

        userTable.setItems(observableData);

        userTable.setPickOnBounds(false);

        container.getChildren().add(userTable);

        InnerShadow shadow =  new InnerShadow();
        shadow.setBlurType(BlurType.THREE_PASS_BOX);
        shadow.setChoke(0);
        shadow.setRadius(20);
        shadow.setOffsetX(0);
        shadow.setOffsetY(0);

        userTable.setRowFactory( tv -> {
            final TableRow<PlayerChat> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                    PlayerChat rowData = row.getItem();

                    if(previous==null){
                        row.setEffect(shadow);
                        previous = row;
                    }else if(previous == row){
                    	row.setEffect(shadow);

                    }else{
                    	row.setEffect(shadow);
                    	previous.setEffect(null);
                    	previous = row;
                    }



            });
            return row ;
        });
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

	public void addUser(PlayerChat... user){
		Collections.addAll(observableData, user);
	}

	public TableView<PlayerChat> getTable() {
		return userTable;
	}

}