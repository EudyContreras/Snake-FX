package com.EudyContreras.Snake.PathFindingAI;

public class Cell {
	public int row;
	public int col;

	public Cell(int row, int column) {
		this.row = row;
		this.col = column;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		Cell cell = (Cell) obj;
		if (row == cell.row && col == cell.col)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "(" + row + "," + col + ")";
	}
}
