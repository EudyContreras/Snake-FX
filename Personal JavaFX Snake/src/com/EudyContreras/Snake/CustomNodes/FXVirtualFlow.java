package com.EudyContreras.Snake.CustomNodes;

import javafx.collections.ObservableList;

/*
 * Create a virtual flow by creating a set a mount of virtual cells. Say 70. The cell count always
 * remains the same. The trick will be to place graphic on base cells and hold all the other cells
 * in a data structure. On scroll down a shift is made by adding a node from the queue to the last
 * cell on the list. The graphic currently on that cell is shift to the one above and so on.
 */
public class FXVirtualFlow<T> {
	
	public ObservableList<T> items;
	public ObservableList<FXVirtualCell> cells;
	
	public FXVirtualFlow(int renderLimit){
		
	}

	public FXVirtualCell getCell(int index){
		return cells.get(0);
	}
	
	public void updateCell(int index){
		
	}
}
