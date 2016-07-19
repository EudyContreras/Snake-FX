package editor;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScaleAndPan extends Application {


	private Parent zoomPane;
	private ScrollPane scroller;
	private Pane scrollContent;
	private Scene scene;
	private Pane window;
	private VBox layout;

	public void start(final Stage stage) {


		final Label infoLabel = new Label("Drag on scene for Rubberband Selection. Shift+Click to add to selection, CTRL+Click to toggle selection. Drag selected nodes for multi-dragging.");
		final Label infoLabel2 = new Label("Show information about mouse coordinates etc here...");

		zoomPane = createZoomPane();

		layout = new VBox();
		layout.getChildren().setAll(new MenuBox(this, stage, null), infoLabel, zoomPane, infoLabel2);

		VBox.setVgrow(zoomPane, Priority.ALWAYS);
//		scrollContent.setStyle("-fx-background-color:gray");
    	
    	
		scene = new Scene(layout, Utils.WIDTH, Utils.HEIGHT);
		scene.getStylesheets().add( getClass().getResource("application.css").toExternalForm());

		new DragAndSelect(this, scene, scrollContent);
		stage.setTitle("Zoomy");
		stage.getIcons().setAll(new Image(APP_ICON));
		stage.setScene(scene);
		stage.show();

	}

	private Parent createZoomPane() {

		window = new Pane();

		scroller = new ScrollPane();
		scrollContent = new Pane(window);
        scrollContent.setStyle("-fx-background-color:gray; -fx-border-width: 5;-fx-border-color: black;");
		scroller.setContent(scrollContent);

		scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				window.setMinSize(newValue.getWidth(), newValue.getHeight());
			}
		});

		scroller.setPrefViewportWidth(256);
		scroller.setPrefViewportHeight(256);



		return scroller;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static final String APP_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/128/Zoom-icon.png";

}