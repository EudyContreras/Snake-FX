package com.EudyContreras.Snake.PathFindingAI;

import java.util.LinkedList;
import java.util.List;

import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.PathFindingAI.CollideNode.RiskFactor;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.geometry.Dimension2D;


public class GridNode {

	private int minRow;
	private int minCol;
	private int cellID;
	private int cellSize;
	private int columnCount;
	private int rowCount;
	private int cellPadding;

	private boolean showCells;

	private CellNode tailCell;
	private CellNode cellNode;
	private GameManager game;
	private Dimension2D dimension;
	private PlayerTwo snakeAI;
	private CellNode[][] cellNodes;

	private LinkedList<CollideNode> colliders;
	private LinkedList<CollideNode> penalties;


	public GridNode(GameManager game, AIController aiController, double width, double height, int cellSize,int cellPadding) {
		this.game = game;
		this.cellSize = cellSize;
		this.snakeAI = game.getGameLoader().getPlayerTwo();
		this.showCells = GameSettings.ALLOW_ASTAR_GRAPH;
		this.minCol = ((GameSettings.MIN_Y-cellSize) / cellSize);
		this.minRow = ((GameSettings.MIN_X) / cellSize);
		this.cellPadding = cellPadding;
		this.dimension = new Dimension2D(width, height);
//		this.game.getBaseLayer().setScaleX(.8);
//		this.game.getBaseLayer().setScaleY(.8);
		this.calculateCells();
	}

	private void calculateCells() {
		rowCount = (int) ((dimension.getWidth()+cellSize*2) / cellSize);
		columnCount = (int) ((dimension.getHeight()+cellSize) / cellSize);
		cellNodes = new CellNode[rowCount][columnCount];
		placeCells();
	}

	public void placeCells() {
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				cellNodes[row][col] = new CellNode(this,game.getBaseLayer(),((cellPadding * (row + 1)) + (cellSize * row))-cellSize, cellPadding * (col + 1) + cellSize * col, cellSize, cellID, new Index2D(col, row));
				cellID++;
			}
		}
	}

	public void placeCellsAlt() {
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				cellNode = new CellNode(this,game.getBaseLayer(),cellPadding * (col + 1) + cellSize * col,cellPadding * (row + 1) + cellSize * row, cellSize, cellID, new Index2D(row, col));
				cellID++;
				if (showCells)
					game.getBaseLayer().getChildren().add(cellNode.getVisualRep());
			}
		}
	}

	public void resetCells() {
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				cellNodes[row][col].resetValues();
			}
		}
	}

	public void computeValidCells() {
		this.snakeAI = game.getGameLoader().getPlayerTwo();
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				cellNodes[row][col].setContainsTarget(false);
				cellNodes[row][col].setAvailable(true);
				cellNodes[row][col].setTraversable(true);
				cellNodes[row][col].setTargetCell(false);
				cellNodes[row][col].setOccupied(false);
				cellNodes[row][col].setSpawnAllowed(true);
				cellNodes[row][col].setPathCell(false);
				cellNodes[row][col].updateVisuals();
				for (int i = 0; i < colliders.size(); i++) {
					if (cellNodes[row][col].getBoundsCheck().intersects(colliders.get(i).getCollideRadius())) {
						cellNodes[row][col].setTraversable(false);
					}
				}
				for (int i = 0; i < penalties.size(); i++) {
					if (cellNodes[row][col].getBoundsCheck().intersects(penalties.get(i).getCollideRadius())) {
						if(penalties.get(i).getRiskFactor() == RiskFactor.HIGH){
							cellNodes[row][col].setTraversable(false);
						}
					}
				}
			}
		}
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				for (int i = 0; i < colliders.size(); i++) {
					if (cellNodes[row][col].getBoundsCheck().intersects(colliders.get(i).getCollideRadius()))
						findNeighbors(row, col,Flag.NO_SPAWN);
				}
				if(cellNodes[row][col].invalidSpawnZone()){
					cellNodes[row][col].setSpawnAllowed(false);
				}
				if(cellNodes[row][col].teleportZone()){
					cellNodes[row][col].setTeleportZone(true);
				}
				for (int i = 0; i < penalties.size(); i++) {
					if (cellNodes[row][col].getBoundsCheck().intersects(penalties.get(i).getCollideRadius())) {
						cellNodes[row][col].setPenaltyCost(0);
						if(penalties.get(i).getRiskFactor()==RiskFactor.MEDIUM){
							cellNodes[row][col].setSpawnAllowed(false);
						}
						else if(penalties.get(i).getRiskFactor()==RiskFactor.HIGH){
							findNeighbors(row, col,Flag.NO_SPAWN);
						}
						else if(penalties.get(i).getRiskFactor()==RiskFactor.NO_SPAWN_ZONE){
							if(cellNodes[row][col].isSpawnAllowed()){
								cellNodes[row][col].setPlayerSpawnZone(true);
								penalties.get(i).getObject().setCell(cellNodes[row][col]);
							}
						}
					}
				}
			}
		}
	}

	public CellNode getRelativeCell(PlayerTwo snake, int r, int c) {
		CellNode cell = getCells()[r][c];
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				CellNode tempCell = getCells()[row][col];
				if (tempCell.getBoundsCheck().intersects(snake.getAIBounds())) {
					cell = tempCell;
					cell.setPathCell(false);
				}
			}
		}
		return cell;
	}

	public CellNode getRelativeCell() {
		CellNode cell = null;
		AbstractSection section = null;
		if(!game.getSectManagerTwo().getSectionList().isEmpty())
			section = game.getSectManagerTwo().getSectionList().getLast();
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				cell = getCells()[row][col];
				cell.updateVisuals();
				if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
					cell.setOccupied(true);
				}
				if (section != null) {
					if (cell.getBoundsCheck().contains(section.getBounds())) {
						cell.setOccupied(false);
						setTailCell(cell);
					}
				}
			}
		}
		return cell;
	}
	private void setTailCell(CellNode cell) {
		this.tailCell = cell;

	}
	public CellNode getTailCell(){

		AbstractSection section = game.getSectManagerTwo().getSectionList().getLast();
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				if (getCells()[row][col].getBoundsCheck().contains(section.getBounds())) {
					if(getCells()[row][col]!=null)
					tailCell = getCells()[row][col];
				}

			}
		}
		return tailCell;
	}
	public boolean safeSpawn(CellNode cell) {
		boolean safe = true;

		int r = cell.getIndex().getRow();
		int c = cell.getIndex().getCol();

		int startPosX = (r - 1 < minRow) ? r : r - 1;
		int startPosY = (c - 1 < minCol) ? c : c - 1;

		int endPosX = (r + 1 > rowCount - 1) ? r : r + 1;
		int endPosY = (c + 1 > columnCount - 1) ? c : c + 1;

		for (int row = startPosX; row <= endPosX; row++) {
			for (int col = startPosY; col <= endPosY; col++) {
				if(cellNodes[row][col].isOccupied()){
					safe = false;
				}
			}
		}
		return safe;
	}
	public void findNeighbors(int r, int c, Flag flag) {
		int startPosX = (r - 1 < minRow) ? r : r - 1;
		int startPosY = (c - 1 < minCol) ? c : c - 1;

		int endPosX = (r + 1 > rowCount - 1) ? r : r + 1;
		int endPosY = (c + 1 > columnCount - 1) ? c : c + 1;

		for (int row = startPosX; row <= endPosX; row++) {
			for (int col = startPosY; col <= endPosY; col++) {
				if(flag == Flag.AVAILABLE){
					if (cellNodes[row][col].isTraversable()&& cellNodes[row][col].isSpawnAllowed()) {
						cellNodes[row][col].setAvailable(true);
					}
				}
				if(flag == Flag.UNAVAILABLE){
					if (cellNodes[row][col].isTraversable()&& cellNodes[row][col].isSpawnAllowed()) {
						cellNodes[row][col].setAvailable(false);
					}
				}
				else if(flag == Flag.UNSAFE){
					if (cellNodes[row][col].isTraversable()&& cellNodes[row][col].isSpawnAllowed()) {

					}
				}
				else if(flag == Flag.SAFE){
					if (cellNodes[row][col].isTraversable()&& cellNodes[row][col].isSpawnAllowed()) {

					}
				}
				else if(flag == Flag.NO_SPAWN){
					if(cellNodes[row][col].isTraversable()){
						cellNodes[row][col].setSpawnAllowed(false);
					}
				}
			}
		}
	}
	public List<CellNode> getNeighborCells(CellNode cell) {

		List<CellNode> neighbors = new LinkedList<>();

		CellNode tempCell;

		int col = cell.getIndex().getCol();
		int row = cell.getIndex().getRow();

		int aCol;
		int aRow;

		// top
		aCol = col;
		aRow = row - 1;
		if (aRow >= minRow) {
			tempCell = getCell(aRow, aCol);
			if (tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
		}
		// bottom
		aCol = col;
		aRow = row + 1;
		if (aRow < rowCount) {
			tempCell = getCell(aRow, aCol);
			if (tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
		}
		// left
		aCol = col - 1;
		aRow = row;
		if (aCol >= minCol) {
			tempCell = getCell(aRow, aCol);
			if (tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
		}
		// right
		aCol = col + 1;
		aRow = row;
		if (aCol < columnCount) {
			tempCell = getCell(aRow, aCol);
			if (tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
		}
		return neighbors;
	}
	public int getMinRow(){
		return minRow;
	}
	public int getMinCol(){
		return minCol;
	}
	public CellNode[][] getCells() {
		return cellNodes;
	}

	public CellNode getCell(int row, int col) {
		return cellNodes[row][col];
	}

	public void setColliderList(LinkedList<CollideNode> list) {
		this.colliders = list;
	}

	public void setPenaltiesList(LinkedList<CollideNode> penaltyNodes) {
		this.penalties = penaltyNodes;
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
	public enum Flag{
		UNSAFE,UNAVAILABLE, NO_SPAWN, AVAILABLE, SAFE,
	}

}
