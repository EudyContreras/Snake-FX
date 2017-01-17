package com.EudyContreras.Snake.Utilities;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LabeledProgressBarSample extends Application {
  public static void main(String[] args) { launch(args); }

  @Override public void start(final Stage stage) {
    stage.setScene(new Scene(createResettableProgressIndicatorBar()));
    stage.show();
  }

  private VBox createResettableProgressIndicatorBar() {
    final int    TOTAL_WORK = 18;
    final String WORK_DONE_LABEL_FORMAT = "%.0f";

    final ReadOnlyDoubleWrapper workDone  = new ReadOnlyDoubleWrapper();

    final ProgressIndicatorBar bar = new ProgressIndicatorBar(
        workDone.getReadOnlyProperty(),
        TOTAL_WORK,
        WORK_DONE_LABEL_FORMAT
    );

    final Timeline countDown = new Timeline(
        new KeyFrame(Duration.seconds(0), new KeyValue(workDone, TOTAL_WORK)),
        new KeyFrame(Duration.seconds(10), new KeyValue(workDone, 0))
    );
    countDown.play();

    final Button resetButton = new Button("Reset");
    resetButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        countDown.playFromStart();
      }
    });

    final VBox layout = new VBox(20);
    layout.setAlignment(Pos.CENTER);
    layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 20px;");
    layout.getChildren().addAll(bar, resetButton);

    return layout;
  }
}

class ProgressIndicatorBar extends StackPane {
  final private ReadOnlyDoubleProperty workDone;
  final private double totalWork;

  final private ProgressBar bar  = new ProgressBar();
  final private Text        text = new Text();
  final private String      labelFormatSpecifier;

  final private static int DEFAULT_LABEL_PADDING = 5;

  ProgressIndicatorBar(final ReadOnlyDoubleProperty workDone, final double totalWork, final String labelFormatSpecifier) {
    this.workDone  = workDone;
    this.totalWork = totalWork;
    this.labelFormatSpecifier = labelFormatSpecifier;

    syncProgress();
    workDone.addListener(new ChangeListener<Number>() {
      @Override public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
        syncProgress();
      }
    });

    bar.setMaxWidth(Double.MAX_VALUE); // allows the progress bar to expand to fill available horizontal space.

    getChildren().setAll(bar, text);
  }

  // synchronizes the progress indicated with the work done.
  private void syncProgress() {
    if (workDone == null || totalWork == 0) {
      text.setText("");
      bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    } else {
      text.setText(String.format(labelFormatSpecifier, Math.ceil(workDone.get())));
      bar.setProgress(workDone.get() / totalWork);
    }

    bar.setMinHeight(text.getBoundsInLocal().getHeight() + DEFAULT_LABEL_PADDING * 2);
    bar.setMinWidth (text.getBoundsInLocal().getWidth()  + DEFAULT_LABEL_PADDING * 2);
  }
}