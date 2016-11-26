package com.EudyContreras.Snake.PathFindingAI;

import java.util.LinkedList;
import java.util.List;

import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.PathFindingAI.AIPathFinder.DistressLevel;
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

	private CellNode headCell;
	private GameManager game;
	private Dimension2D dimension;
	private PlayerTwo snakeAI;
	private CellNode[][] cellNodes;

	private LinkedList<CollideNode> colliders;
	private LinkedList<CollideNode> penalties;

	private LinkedList<CellNode> mapEdges;
	private LinkedList<CellNode> lightWeightMap;
	private LinkedList<CellNode> teleportZoneWest;
	private LinkedList<CellNode> teleportZoneEast;
	private LinkedList<CellNode> teleportZoneNorth;
	private LinkedList<CellNode> teleportZoneSouth;


	public GridNode(GameManager game, AIController aiController, double width, double height, int cellSize,int cellPadding) {
		this.game = game;
		this.cellSize = cellSize;
		this.snakeAI = game.getGameLoader().getPlayerTwo();
		this.showCells = GameSettings.ALLOW_ASTAR_GRAPH;
		this.minCol = ((GameSettings.MIN_Y-cellSize) / cellSize);
		this.minRow = ((GameSettings.MIN_X) / cellSize);
		this.cellPadding = cellPadding;
		this.dimension = new Dimension2D(width, height);
//		this.game.getBaseLayer().setScaleX(.4);
//		this.game.getBaseLayer().setScaleY(.4);

		this.calculateCells();
		this.createTeleportZones();
		this.createEdges();
	}

	private void calculateCells() {
		rowCount = (int) ((dimension.getWidth()+cellSize*2) / cellSize);
		columnCount = (int) ((dimension.getHeight()+cellSize) / cellSize);
		cellNodes = new CellNode[rowCount][columnCount];
		placeCells();
	}

	public void placeCells() {
		lightWeightMap = new LinkedList<>();
		for (int row = 0; row < cellNodes.length; row++) {
			for (int col = 0; col < cellNodes[row].length; col++) {
				cellNodes[row][col] = new CellNode(this,game.getBaseLayer(),((cellPadding * (row + 1)) + (cellSize * row))-cellSize, cellPadding * (col + 1) + cellSize * col, cellSize, cellID, new Index2D(row, col));
				cellID++;
			}
		}
	}

	public void createTeleportZones() {
		CellNode cell = null;
		teleportZoneEast = new LinkedList<>();
		teleportZoneWest = new LinkedList<>();
		teleportZoneNorth = new LinkedList<>();
		teleportZoneSouth = new LinkedList<>();

		for (int row = cellNodes.length - 1; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				cell = cellNodes[row][col];
				cell.setTeleportZone(true);
				cell.setDangerZone(false);
				cell.setTraversable(true);
				teleportZoneEast.add(cell);
			}
		}
		for (int row = minRow; row < minRow + 1; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				cell = cellNodes[row][col];
				cell.setTeleportZone(true);
				cell.setDangerZone(false);
				cell.setTraversable(true);
				teleportZoneWest.add(cell);
			}
		}
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = cellNodes[row].length - 1; col < cellNodes[row].length; col++) {
				cell = cellNodes[row][col];
				cell.setTeleportZone(true);
				cell.setDangerZone(false);
				cell.setTraversable(true);
				teleportZoneSouth.add(cell);
			}
		}
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < minCol + 1; col++) {
				cell = cellNodes[row][col];
				cell.setTeleportZone(true);
				cell.setDangerZone(false);
				cell.setTraversable(true);
				teleportZoneNorth.add(cell);
			}
		}
	}

	public void createEdges(){
		mapEdges = new LinkedList<>();

		mapEdges.addAll(teleportZoneEast);
		mapEdges.addAll(teleportZoneWest);
		mapEdges.addAll(teleportZoneNorth);
		mapEdges.addAll(teleportZoneSouth);
	}

	public void resetCells(boolean resetSafetyCheck) {
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				cellNodes[row][col].resetConnections();
				cellNodes[row][col].resetValues();
				if(resetSafetyCheck){
					cellNodes[row][col].pathToGoal(false);
					cellNodes[row][col].pathToTail(false);
				}
			}
		}
	}

	public void resetCellValues() {
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
				cellNodes[row][col].setDangerZone(false);
				cellNodes[row][col].pathToGoal(false);
				cellNodes[row][col].pathToTail(false);
				cellNodes[row][col].updateVisuals();
				for (int i = 0; i < colliders.size(); i++) {
					if (cellNodes[row][col].getBoundsCheck().intersects(colliders.get(i).getCollideRadius())) {
						cellNodes[row][col].setTraversable(false);
					}
				}
				for (int i = 0; i < penalties.size(); i++) {
					if (cellNodes[row][col].getBoundsCheck().intersects(penalties.get(i).getCollideRadius())) {
						if(penalties.get(i).getRiskFactor() == RiskFactor.HIGH){
							cellNodes[row][col].setDangerZone(true);
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

	public void computeLightWeightMap(){
		lightWeightMap.clear();
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				if(cellNodes[row][col].isTraversable()){
					lightWeightMap.add(cellNodes[row][col]);
				}
			}
		}
	}

	public CellNode getRelativeHeadCell(PlayerTwo snake) {
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				CellNode cell = cellNodes[row][col];
				if (cell.getBoundsCheck().intersects(snake.getAIBounds())) {
					return cell;
				}
			}
		}
		return null;
	}

	public CellNode getRelativeTailCell(AbstractSection tail) {
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				CellNode cell = cellNodes[row][col];
				if (cell.getBoundsCheck().intersects(tail.getBounds())) {
					return cell;
				}
			}
		}
		return null;
	}

	public CellNode markKeyCells() {
		CellNode cell = null;
		AbstractSection tail = null;

		if(!game.getSectManagerTwo().getSectionList().isEmpty()) {
			tail = game.getSectManagerTwo().getSectionList().get(game.getSectManagerTwo().getSectionList().size()-2);
		}
		for (int row = minRow; row < cellNodes.length; row++) {
			for (int col = minCol; col < cellNodes[row].length; col++) {
				cell = getCells()[row][col];
				cell.updateVisuals();

				if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
					if(!cell.isOccupied()){
						cell.setOccupied(true);
					}
				}
				if (tail != null) {
					if (cell.getBoundsCheck().contains(tail.getBounds())) {
						cell.setOccupied(false);
					}
				}
			}
		}
		return cell;
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
				else if (flag == Flag.NO_SPAWN) {
					cellNodes[row][col].setSpawnAllowed(false);
				}
			}
		}
	}

	public CellNode getNeighbor(CellNode cell, Neighbor neighbor) {
		switch(neighbor){
		case NORTH:
			return getCell(
					cell.getIndex().getRow()-(cell.getIndex().getRow()-1 < (minRow) ? 0 : 1),
					cell.getIndex().getCol());
		case SOUTH:
			return getCell(
					cell.getIndex().getRow()+(cell.getIndex().getRow()+1 > (rowCount-1) ? 0 : 1),
					cell.getIndex().getCol());
		case WEST:
			return getCell(
					cell.getIndex().getRow(),
					cell.getIndex().getCol()-(cell.getIndex().getCol()-1 < (minCol) ? 0 : 1));
		case EAST:
			return getCell(
					cell.getIndex().getRow(),
					cell.getIndex().getCol()+(cell.getIndex().getCol()+1 > (columnCount-1) ? 0 : 1));
		}
		return null;
	}

	public List<CellNode> getNeighborCells(CellNode cell ,DistressLevel scenario) {

		List<CellNode> neighbors = new LinkedList<>();

		CellNode tempCell = null;

		switch(scenario){
		case LEVEL_ONE:
			tempCell = getNeighbor(cell, Neighbor.WEST);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && tempCell.isSpawnAllowed()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.EAST);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && tempCell.isSpawnAllowed()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.NORTH);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && tempCell.isSpawnAllowed()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.SOUTH);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && tempCell.isSpawnAllowed()) {
				neighbors.add(tempCell);
			}
			break;
		case LEVEL_TWO:
			tempCell = getNeighbor(cell, Neighbor.WEST);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.EAST);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.NORTH);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.SOUTH);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone()) {
				neighbors.add(tempCell);
			}
			break;
		case LEVEL_THREE:
			tempCell = getNeighbor(cell, Neighbor.WEST);
			if (tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.EAST);
			if (tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.NORTH);
			if (tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.SOUTH);
			if (tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
			break;
		case SAFETY_CHECK:
			tempCell = getNeighbor(cell, Neighbor.WEST);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.EAST);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.NORTH);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.SOUTH);
			if (tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			break;
		case CAUTIOUS_CHECK_EMERGENCY:
			break;
		}

		return neighbors;
	}

	public CellNode getHeadCell() {
		return headCell;
	}

	public CellNode getTailCell(AbstractSection tail){
		return getRelativeTailCell(tail);
	}

	public int getMinRow() {
		return minRow;
	}

	public int getMinCol() {
		return minCol;
	}

	public CellNode[][] getCells() {
		return cellNodes;
	}

	public CellNode getCell(int row, int col) {
		return cellNodes[row][col];
	}

	public final LinkedList<CellNode> getTeleportZoneWest() {
		return teleportZoneWest;
	}

	public final LinkedList<CellNode> getTeleportZoneEast() {
		return teleportZoneEast;
	}

	public final LinkedList<CellNode> getTeleportZoneNorth() {
		return teleportZoneNorth;
	}

	public final LinkedList<CellNode> getTeleportZoneSouth() {
		return teleportZoneSouth;
	}

	public final LinkedList<CellNode> getEdges(){
		return mapEdges;
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

	public enum Flag {
		UNSAFE, UNAVAILABLE, NO_SPAWN, AVAILABLE, SAFE,
	}

	public enum TeleportZone {
		WEST, EAST, SOUTH, NORTH
	}

	public enum Neighbor{
		WEST, EAST, SOUTH, NORTH
	}
}
