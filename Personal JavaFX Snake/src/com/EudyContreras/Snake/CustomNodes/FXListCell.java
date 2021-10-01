package com.EudyContreras.Snake.CustomNodes;

import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;

public class FXListCell<T> extends Region{

	private int index;
	private T item;
	private Region graphic;
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

	public void setGraphic(Region node) {
		this.graphic = node;
		this.showGraphic(true);
	}

	public void showGraphic(boolean state){
		if(state){
			this.getChildren().add(graphic);
			this.setMinHeight(0);
		}else{
			this.setMinHeight(graphic.getBoundsInParent().getHeight()+8);
			this.getChildren().clear();
		}
	}

	public void render(boolean state) {
		this.setVisible(state);
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
}
