package com.EudyContreras.Snake.PlayRoomHub;



import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Callback;

public class FileChooserExample extends Application {
  public static void main(String[] args) { launch(args); }
  @Override public void start(final Stage stage) {
    stage.setTitle("People");
//    stage.getIcons().add(new Image("http://icons.iconarchive.com/icons/icons-land/vista-people/72/Historical-Viking-Female-icon.png"));  // icon license: Linkware (Backlink to http://www.icons-land.com required)

    // create a table.
    final TableView<Person> table = new TableView<>(
      FXCollections.observableArrayList(
        new Person("Jacob", "Smith"),
        new Person("Isabella", "Johnson"),
        new Person("Ethan", "Williams"),
        new Person("Emma", "Jones"),
        new Person("Michael", "Brown")
      )
    );

    // define the table columns.

    TableColumn<Person, Boolean> actionCol = new TableColumn<>("Action");
    actionCol.setSortable(false);

     actionCol.setPrefWidth(1000);
    // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
    actionCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, Boolean>, ObservableValue<Boolean>>() {
      @Override public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Person, Boolean> features) {
        return new SimpleBooleanProperty(features.getValue() != null);
      }
    });

    // create a cell value factory with an add button for each row in the table.
    actionCol.setCellFactory(new Callback<TableColumn<Person, Boolean>, TableCell<Person, Boolean>>() {
      @Override public TableCell<Person, Boolean> call(TableColumn<Person, Boolean> personBooleanTableColumn) {
        return new AddPersonCell(stage, table);
      }
    });

    table.getColumns().setAll(actionCol);
    table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

    stage.setScene(new Scene(table));
    stage.show();
  }

  /** A table cell containing a button for adding a new person. */
  private class AddPersonCell extends TableCell<Person, Boolean> {
    // a button for adding a new person.
    final Button addButton       = new Button("Add");
    // pads and centers the add button in the cell.
    final VBox paddedButton = new VBox();
    final HBox mainHolder = new HBox();
    // records the y pos of the last button press so that the add person dialog can be shown next to the cell.
    final DoubleProperty buttonY = new SimpleDoubleProperty();

    /**
     * AddPersonCell constructor
     * @param stage the stage in which the table is placed.
     * @param table the table to which a new person can be added.
     */
    AddPersonCell(final Stage stage, final TableView table) {
      paddedButton.setPadding(new Insets(3));
      paddedButton.getChildren().add(addButton);
      mainHolder.getChildren().add(paddedButton);
      addButton.setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          buttonY.set(mouseEvent.getScreenY());
          if (getTableRow().getPrefHeight() == 100){
              getTableRow().setPrefHeight(35);
              paddedButton.getChildren().remove(1);
              getTableRow().autosize();
          }
          else{
            getTableRow().setPrefHeight(100);
            Label myLabel = new Label();
            myLabel.setText("This is new label text!");
            myLabel.setTextFill(Color.BLACK);
            paddedButton.getChildren().add(myLabel);
            getTableRow().autosize();
          }
        }
      });
      addButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent actionEvent) {
          table.getSelectionModel().select(getTableRow().getIndex());
        }
      });
    }

    /** places an add button in the row only if the row is not empty. */
    @Override protected void updateItem(Boolean item, boolean empty) {
      super.updateItem(item, empty);
      if (!empty) {
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(paddedButton);
      }
    }
  }


}
