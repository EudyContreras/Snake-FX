package com.EudyContreras.Snake.PlayRoomHub;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CustomListView extends Application {
    private static class CustomThing {
        private String name;
        private int price;
        public String getName() {
            return name;
        }
        public int getPrice() {
            return price;
        }
        public CustomThing(String name, int price) {
            super();
            this.name = name;
            this.price = price;
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        ObservableList<CustomThing> data = FXCollections.observableArrayList();

        data.addAll(new CustomThing("Cheese", 123), new CustomThing("Horse", 456), new CustomThing("Jam", 789));

        final ListView<CustomThing> listView = new ListView<CustomThing>(data);

        listView.setCellFactory(new Callback<ListView<CustomThing>, ListCell<CustomThing>>() {

            @Override
            public ListCell<CustomThing> call(ListView<CustomThing> arg0) {
                return new ListCell<CustomThing>() {

                    @Override
                    protected void updateItem(CustomThing item, boolean bln) {
                        super.updateItem(item, bln);
                        if (item != null) {
                            VBox vBox = new VBox(new Text(item.getName()), new Text(String.format("%d $", item.getPrice())));
                            HBox hBox = new HBox(new Label("[Graphic]"), vBox);
                            hBox.setSpacing(10);
                            setGraphic(hBox);
                        }
                    }

                };
            }

        });

        StackPane root = new StackPane();
        root.getChildren().add(listView);
        primaryStage.setScene(new Scene(root, 200, 250));
        primaryStage.show();
    }

}
