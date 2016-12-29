package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.PlayRoomHub.ConnectLabel.Style;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author ranga
 */

public class TableViewSample extends Application {

    @SuppressWarnings("unchecked")
	@Override
    public void start(Stage stage) throws Exception {
        //define sample ConnectedUser objects
        final ConnectedUser emp1 = new ConnectedUser(1, "Ram", "Nowhere", 32, ConnectedUser.AVAILABLE);
        final ConnectedUser emp2 = new ConnectedUser(2, "Krishna", "Nowhere", 33, ConnectedUser.PLAYING);

        final ObservableList<ConnectedUser> data  = FXCollections.observableArrayList(emp1, emp2);

        //initialise the TableView
        TableView<ConnectedUser> tableView = new TableView<>();

        //define the columns in the table
        TableColumn<ConnectedUser, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(100);

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
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().addAll(idCol, nameCol, countryCol, levelCol, statusCol);

        tableView.setItems(data);
        tableView.setEditable(true);

        StackPane root = new StackPane();

        ConnectUsers users = new ConnectUsers(null);


        StatusIndicator circle = new StatusIndicator(140);

        ConnectLabel label = new ConnectLabel(null,"Hellow",40, 200, 100, Style.BLUE_STYLE);
        label.setFrameGradient(Color.GRAY,Color.SILVER.brighter(),Color.GRAY);
        label.setTextSize(50);

        root.getChildren().add(label.get());

        Scene scene = new Scene(root);
        //scene.getStylesheets().add(TableViewSample.class.getResource("connectUsers.css").toExternalForm());

        stage.setTitle("JavaFX TableView Sample");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}