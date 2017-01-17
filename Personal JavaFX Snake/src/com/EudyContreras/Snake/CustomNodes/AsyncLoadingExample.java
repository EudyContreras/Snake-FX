package com.EudyContreras.Snake.CustomNodes;

import java.util.LinkedList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;

public class AsyncLoadingExample extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        final ListView<String> listView = new ListView<String>();
        final ObservableList<String> listItems = FXCollections.observableArrayList();
        final ProgressIndicator loadingIndicator = new ProgressIndicator();
        final Button button = new Button("Click me to start loading");

        primaryStage.setTitle("Async Loading Example");        

        listView.setPrefSize(200, 250);
        listView.setItems(listItems);

        loadingIndicator.setVisible(false);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final List<String> loadedItems = new LinkedList<String>();

                // clears the list items and start displaying the loading indicator at the Application Thread
                listItems.clear();
                loadingIndicator.setVisible(true); 

                // loads the items at another thread, asynchronously
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000l); // just emulates some loading time

                            // populates the list view with dummy items
                            while (loadedItems.size() < 10) loadedItems.add("Item " + loadedItems.size());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            // just updates the list view items at the
                            // Application Thread
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    listItems.addAll(loadedItems);
                                    loadingIndicator.setVisible(false); // stop displaying the loading indicator
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        VBox root = VBoxBuilder.create()
            .children(
                StackPaneBuilder.create().children(listView, loadingIndicator).build(), 
                button                  
            )
            .build();

        primaryStage.setScene(new Scene(root, 200, 250));
        primaryStage.show();
    }
}