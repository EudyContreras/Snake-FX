package com.EudyContreras.Snake.PathFindingAI;

public class IndexWrapper{

	private int row;
	private int col;

	public IndexWrapper(int row, int col){
		this.row = row;
		this.col = col;
	}

	public final int getRow() {
		return row;
	}

	public final int getCol() {
		return col;
	}

}