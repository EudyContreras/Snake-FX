package com.EudyContreras.Snake.PathFinder;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;

import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;

public class GridNode {

	private int cellID;
	private int cellSize;
	private int columnCount;
	private int rowCount;
	private int cellPadding;

	private boolean showCells;

	private CellNode cellNode;
	private CellNode[][] cellNodes;
	private GameManager game;
	private Dimension2D dimension;
	private AIController aiController;

	private LinkedList<CollideNode> blocks;
	private ObservableList<AbstractObject> targetList;


	public GridNode(GameManager game, AIController aiController, double width, double height, int cellSize, int cellPadding, LinkedList<CollideNode> blocks,ObservableList<AbstractObject> targetList){
		this.game = game;
		this.blocks = blocks;
		this.cellSize = cellSize;
		this.showCells = GameSettings.DEBUG_MODE;
		this.cellPadding = cellPadding;
		this.aiController = aiController;
		this.targetList = targetList;
		this.dimension = new Dimension2D(width,height);
		this.calculateCells();
	}
	private void calculateCells(){
		rowCount = (int) (dimension.getWidth()/cellSize);
		columnCount = (int) (dimension.getHeight()/cellSize);
		cellNodes = new CellNode[rowCount][columnCount];
	}
	public void placeCells(){
		for(int col = 0; col<cellNodes.length; col++){
			for(int row = 0; row<cellNodes[col].length; row++){
				cellNodes[col][row] = new CellNode(cellPadding * (col + 1) + cellSize * col, cellPadding * (row + 1) + cellSize * row,cellSize,cellSize,cellID,new Index2D(row,col));
				cellID++;
				if(showCells)
				game.getBaseLayer().getChildren().add(cellNodes[col][row].getVisualRep());
			}
		}
		computeValidCells();
	}
	public void placeCellsAlt() {
		for (int row = 0; row < columnCount; row++) {
			for (int col = 0; col < rowCount; col++) {
				cellNode = new CellNode(cellPadding * (col + 1) + cellSize * col, cellPadding * (row + 1) + cellSize * row, cellSize, cellSize,cellID, new Index2D(row,col));
				cellID++;
				if(showCells)
				game.getBaseLayer().getChildren().add(cellNode.getVisualRep());
			}
		}
		computeValidCells();
	}
	public void resetCells() {
		for (int row = 0; row < cellNodes.length; row++) {
			for (int col = 0; col < cellNodes[row].length; col++) {
				cellNodes[row][col].resetValues();
			}
		}
	}

	public void computeValidCells() {
		for (int row = 0; row < cellNodes.length; row++) {
			for (int col = 0; col < cellNodes[row].length; col++) {
				for (int i = 0; i < blocks.size(); i++) {
					if (cellNodes[row][col].placeChecker().intersects(blocks.get(i).getCollideRadius())) {
						findNeighbors(row,col);
					}
				}
			}
		}
		for (int row = 0; row < cellNodes.length; row++) {
			for (int col = 0; col < cellNodes[row].length; col++) {
				for (int i = 0; i < blocks.size(); i++) {
					if (cellNodes[row][col].placeChecker().intersects(blocks.get(i).getCollideRadius())) {
						cellNodes[row][col].setValid(false);
						cellNodes[row][col].setTraversable(false);
					}
				}
			}
		}
	}

	public CellNode getRelativeCell(Rectangle2D bounds, int r, int c) {
		CellNode cell = getCells()[r][c];
		for (int row = 0; row < getCells().length; row++) {
			for (int col = 0; col < getCells()[row].length; col++) {
				CellNode tempCell = getCells()[row][col];
				if (tempCell.isValid() && tempCell.isSpawnAllowed() && !tempCell.isTargetCell()) {
					tempCell.setFree(true);
				}
				if (tempCell.placeChecker().intersects(bounds.getMinX(), bounds.getMinY(), 10, 10)) {
					tempCell.setPathCell(true);
					cell = tempCell;
				}
			}
		}
		return cell;
	}
	private void findNeighbors(int row, int col){
		int startPosX = (row - 1 < 0) ? row : row-1;
		int startPosY = (col - 1 < 0) ? col : col-1;
		int endPosX =   (row + 1 > rowCount-1) ? row : row+1;
		int endPosY =   (col + 1 > columnCount-1) ? col : col+1;
		for (int rowNum=startPosX; rowNum<=endPosX; rowNum++) {
		    for (int colNum=startPosY; colNum<=endPosY; colNum++) {
		    	cellNodes[rowNum][colNum].setSpawnAllowed(false);
		    }
		}
	}

	public List<CellNode> getNeighborCells(CellNode cell, HashSet<CellNode> closedSet) {

		List<CellNode>neighbors = new LinkedList<>();

	    CellNode temp;

		int col = cell.getIndex().getCol();
		int row = cell.getIndex().getRow();

		int aCol;
		int aRow;

		// top
		aCol = col;
		aRow = row - 1;
		if (aRow >= 0) {
			temp = getCell(aRow,aCol);
			if( temp.isTraversable() && !closedSet.contains(temp)) {
				neighbors.add(temp);
			}
		}

		// bottom
		aCol = col;
		aRow = row + 1;
		if (aRow < rowCount) {
			temp = getCell(aRow,aCol);
			if( temp.isTraversable() && !closedSet.contains(temp)) {
				neighbors.add(temp);
			}
		}

		// left
		aCol = col - 1;
		aRow = row;
		if ( aCol >= 0) {
			temp = getCell(aRow,aCol);
			if(temp.isTraversable() && !closedSet.contains(temp)) {
				neighbors.add(temp);
			}
		}

		// right
		aCol = col + 1;
		aRow = row;
		if ( aCol < columnCount) {
			temp = getCell(aRow,aCol);
			if(temp.isTraversable() && !closedSet.contains(temp)) {
				neighbors.add(temp);
			}
		}

		return neighbors;
	}

	public CellNode[][] getCells(){
		return cellNodes;
	}
	public CellNode getCell(int row, int col){
		return cellNodes[row][col];
	}
	public void setColliderList(LinkedList<CollideNode> list){
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

}
