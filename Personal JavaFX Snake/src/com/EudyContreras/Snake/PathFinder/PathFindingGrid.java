package com.EudyContreras.Snake.PathFinder;


import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;

import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PathFindingGrid {

	private int cellSize;
	private int columnCount;
	private int rowCount;
	private int cellPadding;
	private boolean showCells;
	private Cell gridCell;
	private Cell[][] gridCells2D;
	private GameManager game;
	private Dimension2D dimension;
	private AI_Controller aiController;
	private LinkedList<CollideObject> blocks;
	private ObservableList<AbstractObject> targetList;


	public PathFindingGrid(GameManager game, AI_Controller aiController, double width, double height, int cellSize, int cellPadding, boolean showCells, LinkedList<CollideObject> blocks,ObservableList<AbstractObject> targetList){
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
		gridCells2D = new Cell[rowCount][columnCount];
	}
	public void placeCells(){
		for(int col = 0; col<gridCells2D.length; col++){
			for(int row = 0; row<gridCells2D[col].length; row++){
				gridCells2D[col][row] = new Cell(cellPadding * (col + 1) + cellSize * col, cellPadding * (row + 1) + cellSize * row,cellSize,cellSize);
				if(showCells)
				game.getBaseLayer().getChildren().add(gridCells2D[col][row].getVisualRep());
			}
		}
	}
	public void placeCellsAlt() {
		for (int row = 0; row < columnCount; row++) {
			for (int col = 0; col < rowCount; col++) {
				gridCell = new Cell(cellPadding * (col + 1) + cellSize * col, cellPadding * (row + 1) + cellSize * row, cellSize, cellSize);
				if(showCells)
				game.getBaseLayer().getChildren().add(gridCell.getVisualRep());
			}
		}
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
						if (showCells) {
							gridCells2D[row][col].visualRep.setFill(Color.RED);
						}
						gridCells2D[row][col].valid = false;
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
	public Cell[][] getCells(){
		return gridCells2D;
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

	public class Cell {
		private Point2D location;
		private Dimension2D dimension;
		private Rectangle visualRep;
		private boolean spawnAllowed = true;
		private boolean targetCell = false;
		private boolean visited = false;
		private boolean valid = true;

		public Cell(double x, double y, double width, double height) {
			this.location = new Point2D(x, y);
			this.dimension = new Dimension2D(width, height);
			if (showCells) {
				this.visualRep = new Rectangle(width, height);
				this.visualRep.setX(location.getX());
				this.visualRep.setY(location.getY());
				this.visualRep.setFill(Color.rgb(0, 0, 0, .3));
			}
//			this.checkValidity();
		}


		public void setLocation(double x, double y) {
			this.location = new Point2D(x, y);
			if (showCells) {
				this.visualRep.setX(x);
				this.visualRep.setY(y);
			}
		}

//		public void checkValidity() {
//			for (int i = 0; i < blocks.size(); i++) {
//				if (placeChecker().intersects(blocks.get(i).getCollideRadius())) {
//					if (showCells) {
//						visualRep.setFill(Color.RED);
//					}
//					valid = false;
//				}
//			}
//		}

		public void setDimension(double width, double height) {
			this.dimension = new Dimension2D(width, height);
		}

		public void setContainsTarget(boolean state) {
			setTargetCell(state);
			if (showCells) {
				visualRep.setFill(state? Color.GREEN : Color.rgb(0, 0, 0, 0.3) );
			}
		}
		public void setSpawnAllowed(boolean state) {
			this.spawnAllowed = state;
			if (showCells) {
				visualRep.setFill(state? Color.rgb(0, 0, 0, 0.3) : Color.ORANGE );
			}
		}
		public final void setValid(boolean valid) {
			this.valid = valid;
			if (showCells) {
				visualRep.setFill(valid? Color.rgb(0, 0, 0, 0.3) : Color.RED );
			}
		}
		public void setFree(boolean state) {
			if (showCells) {
				visualRep.setFill(Color.BLACK);
			}
		}

		public Rectangle getVisualRep() {
			return visualRep;
		}

		public final Point2D getLocation() {
			return location;
		}

		public final void setLocation(Point2D location) {
			this.location = location;
		}

		public final Dimension2D getDimension() {
			return dimension;
		}

		public final void setDimension(Dimension2D dimension) {
			this.dimension = dimension;
		}

		public final boolean isVisited() {
			return visited;
		}

		public final boolean isSpawnAllowed() {
			return spawnAllowed;
		}

		public final boolean isTargetCell() {
			return targetCell;
		}
		public void setTargetCell(boolean state){
			this.targetCell = state;
		}
		public final void setVisited(boolean visited) {
			this.visited = visited;
		}

		public final boolean isValid() {
			return valid;
		}

		public Rectangle2D placeChecker() {
			return new Rectangle2D(location.getX(), location.getY(), dimension.getWidth(), dimension.getHeight());
		}
	}
}
