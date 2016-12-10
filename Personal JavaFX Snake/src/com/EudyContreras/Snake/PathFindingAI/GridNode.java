package com.EudyContreras.Snake.PathFindingAI;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

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
	private CellNode tailCell;
	private GameManager game;
	private Dimension2D dimension;
	private PlayerTwo snakeAI;
	private CellNode[][] cellNodes;

	private List<CellNode> mapEdges;
	private List<CellNode> freeCells;

	private LinkedList<CollideNode> colliders;
	private LinkedList<CollideNode> penalties;

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
		this.createFreeCells();
		this.createEdges();
	}

	private void calculateCells() {
		rowCount = (int) ((dimension.getWidth()+cellSize*2) / cellSize);
		columnCount = (int) ((dimension.getHeight()+cellSize) / cellSize);
		cellNodes = new CellNode[rowCount][columnCount];
		placeCells();
	}

	public void placeCells() {
		freeCells = new LinkedList<>();
		for (int row = 0; row < cellNodes.length; row++) {
			for (int col = 0; col < cellNodes[row].length; col++) {
				cellNodes[row][col] = new CellNode(this,game.getBaseLayer(),((cellPadding * (row + 1)) + (cellSize * row))-cellSize, cellPadding * (col + 1) + cellSize * col, cellSize, cellID, new IndexWrapper(row, col));
				cellID++;
			}
		}
	}

	public void createTeleportZones() {
		teleportZoneEast = new LinkedList<>();
		teleportZoneWest = new LinkedList<>();
		teleportZoneNorth = new LinkedList<>();
		teleportZoneSouth = new LinkedList<>();

		IntStream.range(cellNodes.length-1, cellNodes.length)
		.forEach(row -> IntStream.range(minCol, cellNodes[row].length).forEach(col -> {
			final CellNode cell = cellNodes[row][col];
			cell.setTeleportZone(true);
			cell.setDangerZone(false);
			cell.setTraversable(true);
			teleportZoneEast.add(cell);
		}));

		IntStream.range(minRow, minRow+1)
		.forEach(row -> IntStream.range(minCol, cellNodes[row].length).forEach(col -> {
			final CellNode cell = cellNodes[row][col];
			cell.setTeleportZone(true);
			cell.setDangerZone(false);
			cell.setTraversable(true);
			teleportZoneWest.add(cell);
		}));

		IntStream.range(minRow, cellNodes.length)
		.forEach(row -> IntStream.range(cellNodes[row].length-1, cellNodes[row].length).forEach(col -> {
			final CellNode cell = cellNodes[row][col];
			cell.setTeleportZone(true);
			cell.setDangerZone(false);
			cell.setTraversable(true);
			teleportZoneSouth.add(cell);
		}));

		IntStream.range(minRow, cellNodes.length)
		.forEach(row -> IntStream.range(minCol, minCol+1).forEach(col -> {
			final CellNode cell = cellNodes[row][col];
			cell.setTeleportZone(true);
			cell.setDangerZone(false);
			cell.setTraversable(true);
			teleportZoneNorth.add(cell);
		}));
	}

	public void createEdges(){
		mapEdges = new LinkedList<>();
		mapEdges.addAll(teleportZoneEast);
		mapEdges.addAll(teleportZoneWest);
		mapEdges.addAll(teleportZoneNorth);
		mapEdges.addAll(teleportZoneSouth);
	}

	public void createFreeCells(){
		IntStream.range(minRow, cellNodes.length)
		.forEach(row -> IntStream.range(minCol, cellNodes[row].length).forEach(col -> {
			final CellNode cell = cellNodes[row][col];
			if(cell.isTraversable()){
				freeCells.add(cell);
			}
		}));
	}

	public void resetCells(boolean check) {
		freeCells.parallelStream().forEach(cell -> cell.
				resetConnections().
				resetValues().
				setPathToGoal(check ? false : cell.isPathToGoal()).
				setPathToTail(check ? false : cell.isPathToTail()));
	}

	public void resetPathToTail() {
		freeCells.parallelStream().forEach(cell -> cell.setPathToTail(false));
	}

	public void resetCellValues() {
		freeCells.parallelStream().forEach(cell -> cell.resetValues());
	}

	public void resetDistances(int distance) {
		freeCells.parallelStream().forEach(cell -> cell.setDistance(distance));
	}

	public void prepareDistances(CellNode from) {
		freeCells.parallelStream().filter(cell -> cell.isTraversable()).forEach(cell -> cell.setDistance(cell.getDistanceFrom(from)));
	}

	public boolean areAllVisited(){
		return freeCells.stream().allMatch(cell -> cell.isVisited());
	}

	public void computeValidCells() {
		this.snakeAI = game.getGameLoader().getPlayerTwo();

		IntStream.range(minRow, cellNodes.length)
				.forEach(row -> IntStream.range(minCol, cellNodes[row].length).forEach(col -> {
					final CellNode cell = cellNodes[row][col];
					cell.setContainsTarget(false);
					cell.setAvailable(true);
					cell.setTraversable(true);
					cell.setTargetCell(false);
					cell.setOccupied(false);
					cell.setSpawnAllowed(true);
					cell.setPathCell(false);
					cell.setDangerZone(false);
					cell.setPathToGoal(false);
					cell.setPathToTail(false);
					cell.updateVisuals();

					colliders.parallelStream()
							.forEach(collider -> new CollisionCheck<CollideNode>(cell, collider.getCollideRadius())
									.intersect(e -> cell.setTraversable(false)));

					penalties.parallelStream()
							.forEach(penalty -> new CollisionCheck<CollideNode>(cell, penalty.getCollideRadius())
									.intersect(e -> cell.setDangerZone(true), penalty,
											p -> penalty.getRiskFactor() == RiskFactor.HIGH));
				}));

		IntStream.range(minRow, cellNodes.length)
				.forEach(row -> IntStream.range(minCol, cellNodes[row].length).forEach(col -> {
					final CellNode cell = cellNodes[row][col];

					colliders.parallelStream()
							.forEach(collider -> new CollisionCheck<CollideNode>(cell, collider.getCollideRadius())
									.intersect(e -> findNeighbors(cell, Flag.NO_SPAWN)));

					penalties.parallelStream()
							.forEach(penalty -> new CollisionCheck<CollideNode>(cell, penalty.getCollideRadius())
									.intersect(e -> cell.setSpawnAllowed(false), penalty,
											p -> penalty.getRiskFactor() == RiskFactor.MEDIUM));

					penalties.parallelStream()
							.forEach(penalty -> new CollisionCheck<CollideNode>(cell, penalty.getCollideRadius())
									.intersect(e -> findNeighbors(cell, Flag.NO_SPAWN), penalty,
											p -> penalty.getRiskFactor() == RiskFactor.HIGH));

					penalties.parallelStream()
							.forEach(penalty -> new CollisionCheck<CollideNode>(cell, penalty.getCollideRadius())
									.intersect(e -> setPlayerSpawnZone(cell, penalty), penalty,
											p -> penalty.getRiskFactor() == RiskFactor.NO_SPAWN_ZONE));
					if (cell.invalidSpawnZone()) {
						cell.setSpawnAllowed(false);
					}

				}));
	}

	public void setPlayerSpawnZone(CellNode cell, CollideNode penalty){
		if(cell.isSpawnAllowed()){
			cell.setPlayerSpawnZone(true);
			penalty.getObject().setCell(cell);
		}
	}

	public CellNode getRelativeHeadCell(PlayerTwo snake, int r, int c) {

		CellNode headCell = freeCells.parallelStream().filter(cell -> cell.getBoundsCheck().intersects(snake.getAIBounds())).findFirst().orElse(null);

		return headCell;
	}

	public CellNode getRelativeHeadCell(PlayerTwo snake) {

		CellNode headCell = freeCells.parallelStream().filter(cell -> (cell.getBoundsCheck().intersects(snake.getAIBounds()))).findFirst().orElse(null);

		return headCell;
	}

	public CellNode getRelativeTailCell(PlayerTwo snake) {

		AbstractSection tail = game.getSectManagerTwo().getSectionList().getLast();

		CellNode tailCell = freeCells.parallelStream().filter(cell -> (cell.getBoundsCheck().intersects(tail.getBounds()))).findFirst().orElse(null);

		return tailCell;
	}

	public CellNode getTailCellNeighbor(PlayerTwo snake) {

		CellNode tail = getRelativeTailCell(snake);

		for(CellNode cell: getChildren(tail)){
			if(cell==null)
				continue;

			if(!cell.isOccupied()){
				return cell;
			}
		}

		return tail;
	}

	public void markKeyCells() {
		AbstractSection tail = game.getSectManagerTwo().getSectionList().get(game.getSectManagerTwo().getSectionList().size()-2);

		IntStream.range(minRow, cellNodes.length)
				.forEach(row -> IntStream.range(minCol, cellNodes[row].length).forEach(col -> {
					final CellNode cell = cellNodes[row][col];

					if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
						cell.setOccupied(true);
						cell.setPathToGoal(false);
					}
					if (cell.getBoundsCheck().contains(tail.getBounds())) {
						cell.setOccupied(false);
					}

					cell.updateVisuals();

				}));
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

	public boolean isNeighborNot(CellNode cell, Flag flag) {
		boolean safe = true;

		int r = cell.getIndex().getRow();
		int c = cell.getIndex().getCol();

		int startPosX = (r - 1 < minRow) ? r : r - 1;
		int startPosY = (c - 1 < minCol) ? c : c - 1;

		int endPosX = (r + 1 > rowCount - 1) ? r : r + 1;
		int endPosY = (c + 1 > columnCount - 1) ? c : c + 1;

		for (int row = startPosX; row <= endPosX; row++) {
			for (int col = startPosY; col <= endPosY; col++) {
				CellNode neighbor = cellNodes[row][col];
				if(neighbor.equals(cell))
				continue;

				switch(flag){
				case AVAILABLE:
					break;
				case NO_SPAWN:
					if(!neighbor.isSpawnAllowed()){
						safe = false;
					}
					break;
				case OCCUPIED:
					if(neighbor.isOccupied()){
						safe = false;
					}
					break;
				case SAFE:
					if(!neighbor.isOccupied() && neighbor.isTraversable() && !neighbor.isDangerZone()){
						safe = false;
					}
					break;
				case UNAVAILABLE:
					if(!neighbor.isAvailable()){
						safe = false;
					}
					break;
				case UNSAFE:
					if(neighbor.isOccupied() || !neighbor.isTraversable() || neighbor.isDangerZone()){
						safe = false;
					}
					break;
				}
			}
		}
		return safe;
	}

	public void findNeighbors(CellNode cell, Flag flag){
		findNeighbors(cell.getIndex().getRow(),cell.getIndex().getCol(),flag);
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
			tempCell = getNeighbor(cell, Neighbor.NORTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isTeleportZone()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.SOUTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isTeleportZone()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.WEST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isTeleportZone()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.EAST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isTeleportZone()) {
				neighbors.add(tempCell);
			}
			break;
		case LEVEL_TWO:
			tempCell = getNeighbor(cell, Neighbor.NORTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.SOUTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.WEST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.EAST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone()) {
				neighbors.add(tempCell);
			}
			break;
		case LEVEL_THREE:
			tempCell = getNeighbor(cell, Neighbor.NORTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.SOUTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.WEST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.EAST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(tempCell);
			}
			break;
		case SAFETY_CHECK_GOAL_LEVEL_TWO:
			tempCell = getNeighbor(cell, Neighbor.NORTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.SOUTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.WEST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.EAST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			break;
		case SAFETY_CHECK_GOAL_LEVEL_THREE:
			tempCell = getNeighbor(cell, Neighbor.NORTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.SOUTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.WEST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.EAST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isPathToGoal()) {
				neighbors.add(tempCell);
			}
			break;
		case SAFETY_CHECK_TAIL:
			tempCell = getNeighbor(cell, Neighbor.NORTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isPathToTail()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.SOUTH);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isPathToTail()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.WEST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isPathToTail()) {
				neighbors.add(tempCell);
			}
			tempCell = getNeighbor(cell, Neighbor.EAST);
			if (!tempCell.equals(cell) && tempCell.isTraversable() && !tempCell.isOccupied() && !tempCell.isDangerZone() && !tempCell.isPathToTail()) {
				neighbors.add(tempCell);
			}
			break;
		case CAUTIOUS_CHECK_EMERGENCY:
			break;
		}

		return neighbors;
	}

	public CellNode getPrevious(PlayerTwo snakeAI, CellNode cell){
		CellNode previous = null;

		Neighbor neighbor = null;

		switch(snakeAI.getCurrentDirection()){
		case MOVE_DOWN:
			neighbor = Neighbor.NORTH;
			break;
		case MOVE_LEFT:
			neighbor = Neighbor.EAST;
			break;
		case MOVE_RIGHT:
			neighbor = Neighbor.WEST;
			break;
		case MOVE_UP:
			neighbor = Neighbor.SOUTH;
			break;
		case STANDING_STILL:
			neighbor = Neighbor.NORTH;
			break;

		}
		previous = getNeighbor(cell, neighbor);

		return previous;
	}

	public boolean isNeighborOccupied(PlayerTwo snakeAI, CellNode cell){

		List<CellNode> children = new LinkedList<>();

		CellNode north = getNeighbor(cell, Neighbor.NORTH);
		CellNode south = getNeighbor(cell, Neighbor.SOUTH);
		CellNode east = getNeighbor(cell, Neighbor.EAST);
		CellNode west = getNeighbor(cell, Neighbor.WEST);

		switch(snakeAI.getCurrentDirection()){
		case MOVE_DOWN:
			if(south!=null && !south.equals(cell) && south.isTraversable()){
				children.add(south);
			}
			if(east!=null && !east.equals(cell) && east.isTraversable()){
				children.add(east);
			}
			if(west!=null && !west.equals(cell) && west.isTraversable()){
				children.add(west);
			}
			break;
		case MOVE_LEFT:
			if(north!=null && !north.equals(cell) && north.isTraversable()){
				children.add(north);
			}
			if(south!=null && !south.equals(cell) && south.isTraversable()){
				children.add(south);
			}
			if(west!=null && !west.equals(cell) && west.isTraversable()){
				children.add(west);
			}
			break;
		case MOVE_RIGHT:
			if(north!=null && !north.equals(cell) && north.isTraversable()){
				children.add(north);
			}
			if(south!=null && !south.equals(cell) && south.isTraversable()){
				children.add(south);
			}
			if(east!=null && !east.equals(cell) && east.isTraversable()){
				children.add(east);
			}
			break;
		case MOVE_UP:
			if(north!=null && !north.equals(cell) && north.isTraversable()){
				children.add(north);
			}
			if(east!=null && !east.equals(cell) && east.isTraversable()){
				children.add(east);
			}
			if(west!=null && !west.equals(cell) && west.isTraversable()){
				children.add(west);
			}
			break;
		case STANDING_STILL:
			break;
		}

		for(CellNode node: children){
			if(node.isOccupied()){
				return true;
			}
		}
		return false;
	}


	public CellNode[] getChildren(CellNode cell){

		List<CellNode> children = new LinkedList<>();

		CellNode north = getNeighbor(cell, Neighbor.NORTH);
		CellNode south = getNeighbor(cell, Neighbor.SOUTH);
		CellNode east = getNeighbor(cell, Neighbor.EAST);
		CellNode west = getNeighbor(cell, Neighbor.WEST);

		if(north!=null && !north.equals(cell) && north.isTraversable()){
			children.add(north);
		}
		if(south!=null && !south.equals(cell) && south.isTraversable()){
			children.add(south);
		}
		if(east!=null && !east.equals(cell) && east.isTraversable()){
			children.add(east);
		}
		if(west!=null && !west.equals(cell) && west.isTraversable()){
			children.add(west);
		}

		CellNode[] childArray = new CellNode[children.size()];
		return children.toArray(childArray);
	}

	public CellNode getHeadCell() {
		return headCell;
	}

	public void setHeadCell(CellNode headCell) {
		this.headCell = headCell;
	}

	public CellNode getTailCell() {
		return tailCell;
	}

	public void setTailCell(CellNode tailCell) {
		this.tailCell = tailCell;
	}

	public CellNode getTailCell(PlayerTwo snake){
		return getRelativeTailCell(snake);
	}

	public CellNode getHeadCell(PlayerTwo snake){
		return getRelativeHeadCell(snake);
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

	public CellNode getCell(IndexWrapper index) {
		return cellNodes[index.getRow()][index.getCol()];
	}

	public List<CellNode> getEdges() {
		return mapEdges;
	}

	public List<CellNode> getFreeCells(){
		return freeCells;
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
		UNSAFE, OCCUPIED, UNAVAILABLE, NO_SPAWN, AVAILABLE, SAFE,
	}

	public enum TeleportZone {
		WEST, EAST, SOUTH, NORTH
	}

	public enum Neighbor{
		WEST, EAST, SOUTH, NORTH
	}

}
