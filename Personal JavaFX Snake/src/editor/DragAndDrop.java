package editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public class DragAndDrop {

	private DragAndSelect selecter;
	private Pane contentPane;
	private double x;
	private double y;

	public DragAndDrop(DragAndSelect selecter, Scene scene, Pane window) {
		this.selecter = selecter;
		this.contentPane = window;
		this.contentPane.setOnDragOver(e -> mouseDragOver(e));
		this.contentPane.setOnDragDropped(e -> mouseDragDropped(e));
		this.contentPane.setOnDragExited(e -> contentPane.setStyle("-fx-border-color: #C6C6C6;"));
		scene.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {

				x = event.getSceneX() ;
				y = event.getSceneY();

			}

		});
	}

	private void dropImage(Image image) {

		selecter.AddSelectable(image, image.getWidth(), image.getHeight(), x- image.getWidth() / 2, y - image.getHeight() / 2);
	}

	private void mouseDragDropped(final DragEvent event) {

		final Dragboard dragBoard = event.getDragboard();
		boolean success = false;

		if (dragBoard.hasFiles()) {
			success = true;

			final File file = dragBoard.getFiles().get(0);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {

					System.out.println(file.getAbsolutePath());

					try {

						Image image = new Image(new FileInputStream(file.getAbsolutePath()));
						dropImage(image);

					} catch (FileNotFoundException ex) {
						Logger.getLogger(DragAndDrop.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			});
		}
		event.setDropCompleted(success);
		event.consume();
	}

	private void mouseDragOver(final DragEvent e) {
		final Dragboard dragBoard = e.getDragboard();

		final boolean isAccepted = dragBoard.getFiles().get(0).getName().toLowerCase().endsWith(".png")
				|| dragBoard.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
				|| dragBoard.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");

		if (dragBoard.hasFiles()) {
			if (isAccepted) {
				contentPane.setStyle("-fx-border-color: red;" + "-fx-border-width: 5;"
						+ "-fx-background-color: #C6C6C6;" + "-fx-border-style: solid;");
				e.acceptTransferModes(TransferMode.COPY);
			}
		} else {
			e.consume();
		}
	}

}