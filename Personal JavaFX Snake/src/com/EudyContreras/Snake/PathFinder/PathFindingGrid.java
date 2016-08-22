package com.EudyContreras.Snake.PathFinder;


import java.util.LinkedList;
import java.util.List;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;

import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;

public class PathFindingGrid {

	private int cellID;
	private int cellSize;
	private int columnCount;
	private int rowCount;
	private int cellPadding;
	private boolean showCells;
	private PathFindingCell gridCell;
	private PathFindingCell[][] gridCells2D;
	private GameManager game;
	private Dimension2D dimension;
	private AIController aiController;
	private LinkedList<CollideObject> blocks;
	private ObservableList<AbstractObject> targetList;


	public PathFindingGrid(GameManager game, AIController aiController, double width, double height, int cellSize, int cellPadding, boolean showCells, LinkedList<CollideObject> blocks,ObservableList<AbstractObject> targetList){
		this.game = game;
		this.blocks = blocks;
		this.cellSize = cellSize;
		this.showCells = showCells;
		this.cellPadding = cellPadding;
		this.aiController = aiController;
		this.targetList = targetList;
		this.dimension = new Dimension2D(width,height);
		this.calculateCells();
	}
	private void calculateCells(){
		rowCount = (int) (dimension.getWidth()/cellSize);
		columnCount = (int) (dimension.getHeight()/cellSize);
		gridCells2D = new PathFindingCell[rowCount][columnCount];
	}
	public void placeCells(){
		for(int col = 0; col<gridCells2D.length; col++){
			for(int row = 0; row<gridCells2D[col].length; row++){
				gridCells2D[col][row] = new PathFindingCell(cellPadding * (col + 1) + cellSize * col, cellPadding * (row + 1) + cellSize * row,cellSize,cellSize,cellID,new Index2D(row,col));
				cellID++;
				if(showCells)
				game.getBaseLayer().getChildren().add(gridCells2D[col][row].getVisualRep());
			}
		}
		computeValidCells();
	}
	public void placeCellsAlt() {
		for (int row = 0; row < columnCount; row++) {
			for (int col = 0; col < rowCount; col++) {
				gridCell = new PathFindingCell(cellPadding * (col + 1) + cellSize * col, cellPadding * (row + 1) + cellSize * row, cellSize, cellSize,cellID, new Index2D(row,col));
				cellID++;
				if(showCells)
				game.getBaseLayer().getChildren().add(gridCell.getVisualRep());
			}
		}
		computeValidCells();
	}

	public void computeValidCells() {
		for (int row = 0; row < gridCells2D.length; row++) {
			for (int col = 0; col < gridCells2D[row].length; col++) {
				for (int i = 0; i < blocks.size(); i++) {
					if (gridCells2D[row][col].placeChecker().intersects(blocks.get(i).getCollideRadius())) {
						findNeighbors(row,col);
					}
				}
			}
		}
		for (int row = 0; row < gridCells2D.length; row++) {
			for (int col = 0; col < gridCells2D[row].length; col++) {
				for (int i = 0; i < blocks.size(); i++) {
					if (gridCells2D[row][col].placeChecker().intersects(blocks.get(i).getCollideRadius())) {
						gridCells2D[row][col].setValid(false);
						gridCells2D[row][col].setTraversable(false);
					}
				}
			}
		}
	}
	private void findNeighbors(int row, int col){
		int startPosX = (row - 1 < 0) ? row : row-1;
		int startPosY = (col - 1 < 0) ? col : col-1;
		int endPosX =   (row + 1 > rowCount-1) ? row : row+1;
		int endPosY =   (col + 1 > columnCount-1) ? col : col+1;
		for (int rowNum=startPosX; rowNum<=endPosX; rowNum++) {
		    for (int colNum=startPosY; colNum<=endPosY; colNum++) {
		    	gridCells2D[rowNum][colNum].setSpawnAllowed(false);
		    }
		}
	}
	public List<PathFindingCell> getNeighborCells(PathFindingCell cell) {

		List<PathFindingCell>neighbors = new LinkedList<>();

		int currentColumn = cell.getIndex().getCol();
		int currentRow = cell.getIndex().getRow();

		int neighborColumn;
		int neighborRow;

		// top
		neighborColumn = currentColumn;
		neighborRow = currentRow - 1;

		if (neighborRow >= 0) {
			if( gridCells2D[neighborRow][neighborColumn].isTraversable()) {
				neighbors.add( gridCells2D[neighborRow][neighborColumn]);
			}
		}

		// bottom
		neighborColumn = currentColumn;
		neighborRow = currentRow + 1;

		if (neighborRow < rowCount) {
			if( gridCells2D[neighborRow][neighborColumn].isTraversable()) {
				neighbors.add(gridCells2D[neighborRow][neighborColumn]);
			}
		}

		// left
		neighborColumn = currentColumn - 1;
		neighborRow = currentRow;

		if ( neighborColumn >= 0) {
			if( gridCells2D[neighborRow][neighborColumn].isTraversable()) {
				neighbors.add(gridCells2D[neighborRow][neighborColumn]);
			}
		}

		// right
		neighborColumn = currentColumn + 1;
		neighborRow = currentRow;

		if ( neighborColumn < columnCount) {
			if( gridCells2D[neighborRow][neighborColumn].isTraversable()) {
				neighbors.add(gridCells2D[neighborRow][neighborColumn]);
			}
		}

		return neighbors;
	}

	public PathFindingCell[][] getCells(){
		return gridCells2D;
	}
	public PathFindingCell getCell(int row, int col){
		return gridCells2D[row][col];
	}
	public void setColliderList(LinkedList<CollideObject> list){
		this.blocks = list;
	}
	public final int getCellSize() {
		return cellSize;
	}
	public final void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}
	public final int getColumnCount() {
		return columnCount;
	}
	public final void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
	public final int getRowCount() {
		return rowCount;
	}
	public final void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public final int getCellPadding() {
		return cellPadding;
	}
	public final void setCellPadding(int cellPadding) {
		this.cellPadding = cellPadding;
	}
	public final boolean isShowCells() {
		return showCells;
	}
	public final void setShowCells(boolean showCells) {
		this.showCells = showCells;
	}
	public final Dimension2D getDimension() {
		return dimension;
	}
	public final void setDimension(Dimension2D dimension) {
		this.dimension = dimension;
	}
	public enum CellState{
		CLOSE,OPEN
	}

	public void resetCells() {
		for (int row = 0; row < gridCells2D.length; row++) {
			for (int col = 0; col < gridCells2D[row].length; col++) {
				for (int i = 0; i < blocks.size(); i++) {
					gridCells2D[row][col].resetValues();

				}
			}
		}

	}
}
