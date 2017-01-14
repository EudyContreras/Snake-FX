/**
 *
 */
package com.EudyContreras.Snake.CustomNodes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class FXListWrapper{

  private VBox list;
  private ResizableCanvas canvas;

  public FXListWrapper() {

  }

	/*
	 * Canvas is normally not resizable but by overriding isResizable() and
	 * binding its width and height to the width and height of the cell it will
	 * automatically resize.
	 */
	class ResizableCanvas extends Canvas {

		private Image imageData;

		public ResizableCanvas() {

			widthProperty().addListener(it -> draw());
			heightProperty().addListener(it -> draw());
		}

		@Override
		public boolean isResizable() {
			return true;
		}

		@Override
		public double prefWidth(double height) {
			return getWidth();
		}

		@Override
		public double prefHeight(double width) {
			return getHeight();
		}

		public void setData(Image data) {
			this.imageData = data;
		}

		private void draw() {
			GraphicsContext gc = getGraphicsContext2D();

			gc.clearRect(0, 0, getWidth(), getHeight());

			gc.drawImage(imageData, 0, 0);
		}
	}
}
