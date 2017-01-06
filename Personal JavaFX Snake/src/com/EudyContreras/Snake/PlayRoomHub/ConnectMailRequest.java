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

public class ConnectMailRequest{

	private ObservableList<PlayRequest> observableData;
	private TableRow<PlayRequest> previous;
	private TableView<PlayRequest> userTable;
	private StackPane container;
	private Rectangle shape;
	private double width;
	private double height;


	public ConnectMailRequest(GameManager game){
		super();
		initialize();
		setSize(535,555);
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
		container.getStylesheets().add(ConnectMailRequest.class.getResource("connectMailChat.css").toExternalForm());
	}

	@SuppressWarnings("unchecked")
	private void createColumns(){

        TableColumn<PlayRequest, String> userInfo = new TableColumn<>("Player");
        userInfo.setMinWidth(200);
        userInfo.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PlayRequest, String> statusCol = new TableColumn<>("Status");
        statusCol.setMinWidth(150);
        statusCol.setCellValueFactory(new PropertyValueFactory<PlayRequest, String>("status"));
        statusCol.setCellFactory(new Callback<TableColumn<PlayRequest, String>, TableCell<PlayRequest, String>>() {
            public TableCell<PlayRequest, String> call(TableColumn<PlayRequest, String> param) {
                return new TableCell<PlayRequest, String>() {

                	final StatusIndicator circle = new StatusIndicator(10);

                    @Override
                    public void updateItem(final String status, boolean empty) {
                        super.updateItem(status, empty);
                        if (!isEmpty()) {
                            if(status!=null){
                            	setGraphic(circle.getIndicator(status));
                            }else{
                            	setGraphic(circle.getIndicator(PlayRequest.UNAVAILABLE));
                            }
                        }else{
                        	setGraphic(null);
                        }
                    }
                };
            }
        });


        TableColumn<PlayRequest, LocalDate> dateCol = new TableColumn<>("Date");
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

    	PlayRequest[] users = new PlayRequest[10];

        ThreadManager.performeScript(()->{

            for(int i = 0; i<users.length; i++){
            	if(i%2==0 && i%5!=0){
            		users[i] = new PlayRequest("User"+i, "Nowhere", i, "dfdsdd",LocalDate.now(), PlayRequest.AVAILABLE);
            	}else if(i%2!=0 && i%5!=0){
            		users[i] = new PlayRequest("User"+i, "Nowhere", i, "dfdsfd",LocalDate.now(), PlayRequest.PLAYING);
            	}else if(i%5==0){
            		users[i] = new PlayRequest("User"+i, "Nowhere", i, "dfdsfd",LocalDate.now(), PlayRequest.UNAVAILABLE);
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
            final TableRow<PlayRequest> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                    PlayRequest rowData = row.getItem();

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

	public void addUser(PlayRequest... user){
		Collections.addAll(observableData, user);
	}

	public TableView<PlayRequest> getTable() {
		return userTable;
	}

}