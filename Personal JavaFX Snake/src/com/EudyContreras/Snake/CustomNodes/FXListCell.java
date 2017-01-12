package com.EudyContreras.Snake.CustomNodes;

import javafx.animation.Transition;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class FXListCell<T> extends Region {

	private int index;
	private T item;
	private Region graphic;
	private ImageView placeHolder;
	private ScrollPane container;
	private Transition deleteTransition;
	private Transition addedTransition;
	private WritableImage writableImage;
	private SnapshotParameters parameters;
	private boolean showingPlaceholder;

	public FXListCell() {
		this.setCache(true);
		this.setCacheShape(true);
		this.setCacheHint(CacheHint.SPEED);
		this.setPadding(new Insets(3, 2, 3, 2));
		this.placeHolder = new ImageView();
		this.parameters = new SnapshotParameters();
		this.parameters.setFill(Color.TRANSPARENT);
	}

	private void addCullListener() {
		container.vvalueProperty().addListener((obs, oldValue, newValue) -> {
			if(!showingPlaceholder)
			showPlaceHolder(true);
		});
	}

	private void cullCells(ScrollPane container) {
		Bounds paneBounds = container.localToScene(container.getBoundsInParent());
		Bounds nodeBounds = this.localToScene(this.getBoundsInLocal());
		if (withinBounds(50, paneBounds, nodeBounds)) {
			if (!this.isRendered()) {
				this.render(true);
			}
		} else {
			if (this.isRendered()) {
				this.render(false);
			}
		}
	}

	private boolean withinBounds(double offset, Bounds parent, Bounds child) {
		return new Rectangle2D(parent.getMinX(), parent.getMinY() - offset, parent.getWidth(),
				parent.getHeight() + offset * 2).intersects(child.getMinX(), child.getMinY(), child.getWidth(),
						child.getHeight());
	}

	public void setParentList(ScrollPane region) {
//		this.container = region;
//		if (region != null) {
//			addCullListener();
//		}
	}

	public void createCell(T item) {
		this.setItem(item);
	}

	public Region getGraphic() {
		return graphic;
	}

	public Transition getDeleteTransition() {
		return deleteTransition;
	}

	public void setDeleteTransition(Transition deleteTransition) {
		this.deleteTransition = deleteTransition;
		if (deleteTransition != null) {
			FXTransition.addTransition(this.deleteTransition, this);
		} else {
			if (this.deleteTransition != null) {
				FXTransition.removeTransition(this.deleteTransition, this);
			}
		}
	}

	public Transition getAddedTransition() {
		return addedTransition;
	}

	public void setAddedTransition(Transition addedTransition) {
		this.addedTransition = addedTransition;
		if (addedTransition != null) {
			FXTransition.addTransition(this.addedTransition, this);
		} else {
			if (this.addedTransition != null) {
				FXTransition.removeTransition(this.addedTransition, this);
			}
		}
	}

	public void setGraphic(Region node) {
		this.graphic = node;
		this.showGraphic(true);
	}

	private void showGraphic(boolean state) {
		if (state) {
			this.getChildren().remove(graphic);
			this.getChildren().add(graphic);
			this.setMinHeight(0);
		} else {
			this.setMinHeight(graphic.getBoundsInParent().getHeight() + 8);
			this.getChildren().remove(graphic);
		}
	}

	public boolean containsItem(T item) {
		return this.item.equals(item);
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		FXListCell<T> other = (FXListCell<T>) obj;
		if (index != other.index)
			return false;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}

	public void showPlaceHolder(boolean state) {
		if (state) {
			if (writableImage == null) {
				if (graphic.getHeight() > 8) {
					writableImage = new WritableImage((int) graphic.getWidth(), (int) graphic.getHeight());
					graphic.snapshot(parameters, writableImage);
					placeHolder.setImage(writableImage);
					this.getChildren().remove(graphic);
					this.getChildren().remove(placeHolder);
					this.getChildren().add(placeHolder);
					this.showingPlaceholder = true;
				}
			}
		} else {
			this.getChildren().remove(placeHolder);
			this.getChildren().remove(graphic);
			this.getChildren().add(graphic);
			this.showingPlaceholder = false;
		}
	}

	public void render(boolean state) {
		 this.setVisible(state);
		 this.setDisable(!state);
		 this.showGraphic(state);
	}

	public boolean isRendered() {
		return this.isVisible();
	}
}
