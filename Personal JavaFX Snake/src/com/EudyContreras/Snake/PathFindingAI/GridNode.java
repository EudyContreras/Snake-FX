package com.EudyContreras.Snake.PathFindingAI;

import java.util.List;
import java.util.LinkedList;
import javafx.geometry.Dimension2D;
import javafx.collections.ObservableList;
import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.PathFindingAI.CollideNode.RiskFactor;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;


public class GridNode {

	private int cellID;
	private int cellSize;
	private int columnCount;
	private int rowCount;
	private int cellPadding;

	private boolean showCells;

	private CellNode cellNode;
	private GameManager game;
	private Dimension2D dimension;
	private AIController controller;
	private PlayerTwo snakeAI;
	private CellNode[][] cellNodes;

	private LinkedList<CollideNode> colliders;
	private LinkedList<CollideNode> penalties;
	private ObservableList<AbstractObject> objectives;
	private List<CellNode> pathCoordinates;

	public GridNode(GameManager game, AIController aiController, double width, double height, int cellSize,int cellPadding) {
		this.game = game;
		this.cellSize = cellSize;
		this.controller = aiController;
		this.snakeAI = game.getGameLoader().getPlayerTwo();
		this.showCells = GameSettings.SHOW_PATHFINDING_GRAPH;
		this.cellPadding = cellPadding;
		this.dimension = new Dimension2D(width, height);
		this.calculateCells();
	}

	private void calculateCells() {
		rowCount = (int) (dimension.getWidth() / cellSize);
		columnCount = (int) (dimension.getHeight() / cellSize);
		cellNodes = new CellNode[rowCount][columnCount];
		placeCells();
	}

	public void placeCells() {
		for (int col = 0; col < cellNodes.length; col++) {
			for (int row = 0; row < cellNodes[col].length; row++) {
				cellNodes[col][row] = new CellNode(cellPadding * (col + 1) + cellSize * col,cellPadding * (row + 1) + cellSize * row, cellSize, cellSize, cellID, new Index2D(row, col));
				cellID++;
				if (showCells)
					game.getBaseLayer().getChildren().add(cellNodes[col][row].getVisualRep());
			}
		}
	}

	public void placeCellsAlt() {
		for (int row = 0; row < columnCount; row++) {
			for (int col = 0; col < rowCount; col++) {
				cellNode = new CellNode(cellPadding * (col + 1) + cellSize * col,cellPadding * (row + 1) + cellSize * row, cellSize, cellSize, cellID, new Index2D(row, col));
				cellID++;
				if (showCells)
					game.getBaseLayer().getChildren().add(cellNode.getVisualRep());
			}
		}
	}

	public void resetCells() {
		for (int row = 0; row < cellNodes.length; row++) {
			for (int col = 0; col < cellNodes[row].length; col++) {
				cellNodes[row][col].resetValues();
			}
		}
	}

	public void computeValidCells() {
		this.snakeAI = game.getGameLoader().getPlayerTwo();
		for (int row = 0; row < cellNodes.length; row++) {
			for (int col = 0; col < cellNodes[row].length; col++) {
				cellNodes[row][col].setContainsTarget(false);
				cellNodes[row][col].setSpawnAllowed(true);
				cellNodes[row][col].setPathCell(false);
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
		for (int row = 0; row < cellNodes.length; row++) {
			for (int col = 0; col < cellNodes[row].length; col++) {
				for (int i = 0; i < colliders.size(); i++) {
					if (cellNodes[row][col].getBoundsCheck().intersects(colliders.get(i).getCollideRadius()))
						findNeighbors(row, col);
				}
				for (int i = 0; i < penalties.size(); i++) {
					if (cellNodes[row][col].getBoundsCheck().intersects(penalties.get(i).getCollideRadius())) {
//						cellNodes[row][col].setPenaltyCost(10);
						if(penalties.get(i).getRiskFactor()==RiskFactor.MEDIUM){
							cellNodes[row][col].setSpawnAllowed(false);
						}
						else if(penalties.get(i).getRiskFactor()==RiskFactor.HIGH){
							findNeighbors(row, col);
						}
					}
				}
			}
		}
	}

	public CellNode getRelativeCell(PlayerTwo snake, int r, int c) {
		CellNode cell = getCells()[r][c];
		for (int row = 0; row < getCells().length; row++) {
			for (int col = 0; col < getCells()[row].length; col++) {
				CellNode tempCell = getCells()[row][col];
				if (tempCell.getBoundsCheck().intersects(snake.getAIBounds())) {
					cell = tempCell;
				}
				if (tempCell.getBoundsCheck().intersects(snake.getBounds())) {

				}
			}
		}
		return cell;
	}

	public CellNode getRelativeCell() {
		CellNode cell = null;
		AbstractSection section = null;
		AbstractObject object = null;
		for (int row = 0; row < getCells().length; row++) {
			for (int col = 0; col < getCells()[row].length; col++) {
				cell = getCells()[row][col];
				if (cell.isTraversable())
					cell.setContainsTarget(false);
				for (int i = 0; i < objectives.size(); i++) {
					object = objectives.get(i);
					if (cell.getBoundsCheck().intersects(object.getBounds().getMinX() + object.getBounds().getWidth() / 2,object.getBounds().getMinY() + object.getBounds().getHeight() / 2, 5, 5)) {
						object.setCell(cell);
						cell.setContainsTarget(true);
					}
				}
				for (int i = 0; i < game.getSectManagerTwo().getSectionList().size(); i++) {
					section =  game.getSectManagerTwo().getSectionList().get(i);
					if (cell.getBoundsCheck().contains(section.getBounds())) {
						if (section.getNumericID() != PlayerTwo.NUMERIC_ID - 1) {
							cell.setOccupied(true);
						}
						else{
							cell.setOccupied(false);
						}
					}

				}
			}
		}
		return cell;
	}
	/**
	 * TODO: Build a list containing coordinates and directions.
	 * make the snake move towards the first direction on the list
	 * if the snake moves reaches the coordinate on the list make the
	 * snake take the next turn and so forth:....
	 */
	public void steerPlayer() {
		CellNode cell = null;

		if (pathCoordinates != null) {
			for (int index = 0; index < pathCoordinates.size(); index++) {
				cell = pathCoordinates.get(index);
				if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
					switch (cell.getDirection()) {
					case DOWN:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_DOWN);
						break;
					case LEFT:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_LEFT);
						break;
					case RIGHT:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_RIGHT);
						break;
					case UP:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_UP);
						break;
					case NONE:
						break;

					}
					break;
				}
			}
		}
	}
	private void findNeighbors(int row, int col) {
		int startPosX = (row - 1 < 0) ? row : row - 1;
		int startPosY = (col - 1 < 0) ? col : col - 1;
		int endPosX = (row + 1 > rowCount - 1) ? row : row + 1;
		int endPosY = (col + 1 > columnCount - 1) ? col : col + 1;
		for (int rowIndex = startPosX; rowIndex <= endPosX; rowIndex++) {
			for (int colIndex = startPosY; colIndex <= endPosY; colIndex++) {
				if(cellNodes[rowIndex][colIndex].isTraversable()){
					cellNodes[rowIndex][colIndex].setSpawnAllowed(false);
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
		if (aRow >= 0) {
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
		if (aCol >= 0) {
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

	public CellNode[][] getCells() {
		return cellNodes;
	}

	public CellNode getCell(int row, int col) {
		return cellNodes[row][col];
	}

	public void setColliderList(LinkedList<CollideNode> list) {
		this.colliders = list;
	}

	public void setObjectivesList(ObservableList<AbstractObject> fruitList) {
		this.objectives = fruitList;
	}

	public void setPenaltiesList(LinkedList<CollideNode> penaltyNodes) {
		this.penalties = penaltyNodes;
	}

	public void setPathCoordinates(List<CellNode> cells) {
		this.pathCoordinates = cells;

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
