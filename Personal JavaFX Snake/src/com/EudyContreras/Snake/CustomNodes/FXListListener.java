package com.EudyContreras.Snake.CustomNodes;

public interface FXListListener<T> {

	public void onUpdate(FXListState fXListState, FXListCell<T> cell);

	public enum FXListState{
		CELL_ADDED, CELL_REMOVE
	}
}
