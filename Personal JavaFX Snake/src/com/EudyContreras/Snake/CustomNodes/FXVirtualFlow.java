package com.EudyContreras.Snake.CustomNodes;

import javafx.collections.ObservableList;

public class FXVirtualFlow<T> {
	
	public ObservableList<T> items;
	public ObservableList<FXVirtualCell> cells;
	
	public FXVirtualFlow(int renderLimit){
		
	}

	public FXVirtualCell getCell(int index){
		return cells.get(0);
	}
}
