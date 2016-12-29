package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Utilities.ImageChooser;
import com.EudyContreras.Snake.Utilities.ShapeUtility;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * This class simulates a simple game heads up display which is shown at the to the level
 * this display shows useful information concerning game statistics
 * @author Eudy Contreras
 *
 */
public class ConnectProfile extends StackPane{

	private Text nameLbl;
	private Text countryLbl;
	private Text ageLbl;
	private Text name;
	private Text country;
	private Text age;
	private HBox frame;
	private VBox details;
	private VBox labels;
	private BorderPane container;
	private ConnectFrame background;
	private ImageChooser chooser;
	private GameManager game;

	public ConnectProfile(GameManager game, String name, String country, String age){
		super();
		this.game = game;
		this.age = new Text(age);
		this.name = new Text(name);
		this.country = new Text(country);
		this.ageLbl = new Text("Age:");
		this.nameLbl = new Text("Name:");
		this.countryLbl = new Text("Country:");
		this.chooser = new ImageChooser();
		this.background = new ConnectFrame(game);
		this.container = new BorderPane();
		this.details = new VBox(10);
		this.labels = new VBox(10);
		this.frame = new HBox(25);
		setPrefSize(300, 500);
		setMaxSize(300, 500);
		initialize();
	}

	private void initialize(){
		setupLabel(nameLbl,20);
		setupLabel(ageLbl,20);
		setupLabel(countryLbl,20);

		setupText(name,20);
		setupText(age,20);
		setupText(country,20);

		details.setAlignment(Pos.CENTER_LEFT);
		details.getChildren().add(name);
		details.getChildren().add(age);
		details.getChildren().add(country);

		labels.setAlignment(Pos.CENTER_LEFT);
		labels.getChildren().add(nameLbl);
		labels.getChildren().add(ageLbl);
		labels.getChildren().add(countryLbl);

		frame.getChildren().addAll(labels,details);

		container.setPadding(new Insets(20));
		container.setTop(chooser);
		container.setCenter(frame);

		background.setFrameSize(300, 500);
		background.setFill(ShapeUtility.LINEAR_GRADIENT(Color.rgb(120, 120, 120),Color.BLACK, Color.rgb(120, 120, 120)));

		getChildren().add(background.get());
		getChildren().add(container);
	}

	private void setupLabel(Text text, int size){
		text.setFont(Font.font(null, FontWeight.EXTRA_BOLD, size));
		text.setFill(Color.DARKGRAY);
	}

	private void setupText(Text text, int size){
		text.setFont(Font.font(null, FontWeight.EXTRA_BOLD, size));
		text.setFill(Color.WHITE);
	}

}
