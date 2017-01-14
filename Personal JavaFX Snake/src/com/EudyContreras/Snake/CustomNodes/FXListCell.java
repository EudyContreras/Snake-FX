package com.EudyContreras.Snake.CustomNodes;

import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FXListCell<T> extends Region{

	private int index;
	private T item;
	private Region graphic;
	private Timeline timeLine;
	private Transition deleteTransition;
	private Transition addedTransition;


	public FXListCell(){
		this.setPadding(new Insets(3,2,3,2));
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

	public void setTimeLine(Timeline timeline) {
		if (timeLine == null) {
			this.timeLine = timeline;
			this.timeLine.play();
		}
	}

	private void removeTimeLine(){
		if(timeLine!=null){
			timeLine.stop();
			timeLine = null;
		}
	}

	public void setGraphic(Region node) {
		this.graphic = node;
		this.showGraphic(true);
	}

	public void showGraphic(boolean state){
		if(state){
			this.getChildren().clear();
			this.getChildren().add(graphic);
			this.setMinHeight(0);
		}else{
			this.setMinHeight(graphic.getBoundsInParent().getHeight()+8);
			this.getChildren().clear();
			this.removeTimeLine();
		}
	}

	public void render(boolean state) {
		this.setVisible(state);
		this.setOpacity(state ? 1 : 0);
		this.showGraphic(state);
	}

	public void render(VBox list, boolean state) {
		this.setVisible(state);
		this.setOpacity(state ? 1 : 0);
		this.showGraphic(state);
	}

	public boolean isRendered(){
		return this.isVisible();
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
}
