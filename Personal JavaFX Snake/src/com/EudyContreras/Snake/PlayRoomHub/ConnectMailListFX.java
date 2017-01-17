package com.EudyContreras.Snake.PlayRoomHub;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.EudyContreras.Snake.CustomNodes.FXCallback;
import com.EudyContreras.Snake.CustomNodes.FXListCell;
import com.EudyContreras.Snake.CustomNodes.FXListView;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.Utilities.FillUtility;
import com.EudyContreras.Snake.Utilities.ResizeAnimator;
import com.EudyContreras.Snake.Utilities.TimePeriod;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ConnectMailListFX{

	private FXListView<MailItem> listView;
	private ArrayList<MailItem> toRemove = new ArrayList<>();
	private GameButton buttons = new GameButton();
	private Rectangle clip = new Rectangle();
	private StackPane root = new StackPane();
	private VBox layout = new VBox(4);


	public  ConnectMailListFX(){


		ObservableList<MailItem> data = FXCollections.observableArrayList();

		MailItem[] items = new MailItem[500];

		for (int i = 0; i < items.length; i++) {
			items[i] = new MailItem("Eddie " + i, "Hello Friend!"+i);
			items[i].setDate(LocalDateTime.now());
			if(i%2==0){
				items[i].setMessage("ssdfusdf  udsf iusdif siduf sdf suidf sdfuisdf sidfu");
			}else{
				items[i].setMessage("sdfusdf  udsf iusdif siduf sdf suidf sdfuisdf sidfu"
						+ "Once upon a time there was a game sdfusdf  udsf iusdif siduf sdf suidf sdfuisdf sidfu"
						+ "Once upon a time there was a game sdfusdf  udsf iusdif siduf sdf suidf sdfuisdf sidfu"
						+ "Once upon a time there was a game sdfusdf  udsf iusdif siduf sdf suidf sdfuisdf sidfu");
			}
		}

		data.addAll(items);

		listView = new FXListView<MailItem>(data);
		listView.setFill(Color.ORANGE);
		listView.setHeight(450);
		listView.setWidth(640);
		listView.setSpacing(2);
		listView.setCellFactory(new FXCallback<FXListView<MailItem>, FXListCell<MailItem>>() {

			@Override
			public FXListItem<MailItem> call(FXListView<MailItem> arg0) {

				return new FXListItem<MailItem>() {
					@Override
					public void createCell(MailItem item) {
						super.createCell(item);
						if (item != null) {
							Notifcation notification = new Notifcation();

							notification.setHeader(item.getHeader());
							notification.setDate(item.getDate());
							notification.setContent(item.getMessage());
							notification.setOnSelect(()->{
								animateRemoval(this, item);
							});

							setGraphic(new Pane(notification.get()));
							setBackground(FillUtility.PAINT_FILL(Color.ORANGE));

						}else{
							setGraphic(null);
						}
					}
				};
			}
		});

		buttons.setSpacing(10);
		buttons.addButton("Clear");
		buttons.setIDToAll("button");
		buttons.setFontToAll(Font.font(null, FontWeight.BOLD, 15));


		buttons.addEvent("Clear",()->{
			listView.getItems().clear();
		});

		buttons.setWidthToAll(400);
		buttons.get().getStylesheets().add(ConnectFriends.class.getResource("connectMailList.css").toExternalForm());


		clip.setWidth(listView.get().getPrefWidth()+40);
		clip.setHeight(listView.get().getPrefHeight()+70);
		layout.setSpacing(14);
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(listView.get(),buttons.get());
		root.setClip(clip);
		root.getChildren().add(layout);
		root.getStylesheets().add(ConnectMailChat.class.getResource("connectMailList.css").toExternalForm());
	}

	private void animateRemoval(FXListCell<MailItem> cell, MailItem item){
		ResizeAnimator resize = new ResizeAnimator(ResizeAnimator.RESIZE_HEIGHT);
		resize.setDuration(TimePeriod.millis(300));
		resize.setStartHeight(cell.getGraphic().getHeight());
		resize.setEndHeight(0);
		resize.setRegion(cell.getGraphic());
		resize.setOnFinished(()->{
			listView.getItems().remove(item);
//			cell.getGraphic().requestLayout();
		});
		resize.play();

	}

	public static <T> void addAutoScroll(final ListView<T> view) {
		if (view == null) {
			throw new NullPointerException();
		}

		view.getItems().addListener((ListChangeListener<T>) (c -> {
			c.next();
			final int size = view.getItems().size();
			if (size > 0) {
				view.scrollTo(size - 1);
			}
		}));
	}

	public Region get(){
		return root;
	}

	private class Notifcation {

		private Text date;
		private Label header;
		private Label content;
		private Button delete;
		private VBox container;
		private HBox sections;
		private VBox info;
		private BorderPane divider;
		private BorderPane divider2;
		private StackPane indicator;
		private ImageView imageView;
		private Runnable action;

		public Notifcation() {
			this.info = new VBox(3);
			this.date = new Text();
			this.header = new Label();
			this.content = new Label();
			this.container = new VBox();
			this.sections = new HBox(4);
			this.indicator = new StackPane();
			this.delete = new Button("X");
			this.divider2 = new BorderPane();
			this.divider = new BorderPane();
			this.imageView = new ImageView();
			this.selectListener();
			this.create();
		}

		private void selectListener(){
			this.delete.setOnAction(e->{
				if(action!=null){
					action.run();
				}
			});
			this.delete.setOnMouseEntered(e-> {
				delete.setTextFill(Color.RED);
			});
			this.delete.setOnMouseExited(e-> {
				delete.setTextFill(Color.BLACK);
			});
		}

		public void setOnSelect(Runnable script) {
			this.action = script;
		}

		private void create() {
			divider.setRight(date);
			divider.setLeft(header);
			divider2.setTop(delete);
			BorderPane.setAlignment(delete, Pos.TOP_RIGHT);
			divider.setPadding(new Insets(0, 20, 0, 0));
			header.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 18));
			date.setFont(Font.font(null,FontWeight.BLACK,14));
			content.setFont(Font.font(null,FontWeight.SEMI_BOLD,14));
			header.setTextFill(Color.WHITE);
			content.setTextFill(Color.WHITE);
			date.setFill(Color.WHITE);
			delete.setPadding(new Insets(0,6,0,0));
			delete.setFont(Font.font(null,FontWeight.BOLD,20));
			delete.setTextFill(Color.BLACK);
			delete.setStyle("-fx-background-color: transparent;");
			content.setWrapText(true);
			content.setMinWidth(listView.getWidth() - 200);
			content.setMaxWidth(listView.getWidth() - 200);
			content.setMaxHeight(80);
			content.setTextOverrun(OverrunStyle.ELLIPSIS);
			imageView.setImage(GameImageBank.profile_default_male);
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(60);
			imageView.setFitWidth(60);
			sections.setSpacing(10);
			sections.setAlignment(Pos.CENTER_LEFT);
			container.setAlignment(Pos.CENTER_LEFT);
			indicator.getChildren().add(imageView);
			indicator.setPrefWidth(90);
			indicator.setMinHeight(70);
			indicator.setBackground(FillUtility.PAINT_FILL(FillUtility.LINEAR_GRADIENT(Color.rgb(255, 200, 0))));
			info.setSpacing(2);
			info.getChildren().addAll(divider, content);
			sections.getChildren().addAll(indicator, info, divider2);
			container.getChildren().add(sections);
		}

		public void setHeader(String header) {
			this.header.setText(header);
		}

		public void setDate(String date) {
			this.date.setText(date);
		}

		public void setContent(String content) {
			this.content.setText(content);
		}

		public VBox get() {
			return container;
		}
	}

	public class MailItem {

		private boolean selected;
		private String sender;
		private String header;
		private String message;
		private Object attachment;
		private LocalDateTime date;
		private DateTimeFormatter format;


		public MailItem() {
			super();
			format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		}

		public MailItem(String sender, String header) {
			super();
			this.format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			this.sender = sender;
			this.header = header;
		}

		public String getHeader() {
			return header;
		}

		public void setHeader(String header) {
			this.header = header;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Object getAttachment() {
			return attachment;
		}

		public void setAttachment(Object attachment) {
			this.attachment = attachment;
		}

		public String getDate() {
			return format.format(date);
		}

		public void setDate(LocalDateTime date) {
			this.date = date;
			this.date.format(format);
		}

		public void setSender(String sender) {
			this.sender = sender;
		}

		public String getSender() {
			return sender;
		}

		public void selected(boolean state){
			this.selected = state;
		}

		public boolean isSelected(){
			return selected;
		}

	}

}