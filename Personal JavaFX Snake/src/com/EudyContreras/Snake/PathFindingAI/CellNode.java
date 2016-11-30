package com.EudyContreras.Snake.PathFindingAI;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellNode implements Comparable<CellNode>{

	private Pane layer;
	private Index2D index;
	private Point2D location;
	private GridNode grid;
	private CellNode parentNode;
	private CellNode[] children;
	private Rectangle visualRep;
	private Dimension2D dimension;

	private int id = 0;

	private double cellSize = 0;
	private double distance = 0;
	private double heuristic = 0;
	private double movementCost = 10;
	private double penaltyCost = 0;
	private double totalCost = 0;

	private boolean visited = false;
	private boolean showCells = true;
	private boolean pathCell = false;
	private boolean targetCell = false;
	private boolean occupied = false;
	private boolean teleportZone = false;
	private boolean dangerZone = false;
	private boolean pathToGoal = false;
	private boolean pathToTail = false;
	private boolean objective = false;
	private boolean isTraversable = true;
	private boolean spawnAllowed = true;
	private boolean invalidSpawnZone = false;
	private boolean availableCell = true;
	private boolean playerSpawnZone = false;

	private CellType cellType = CellType.FREE;
	private Direction directionInPath = Direction.NONE;

	public CellNode(){}

	public CellNode(GridNode grid, Pane layer, double x, double y, double size, int id, Index2D index) {
		this.id = id;
		this.grid = grid;
		this.index = index;
		this.layer = layer;
		this.cellSize = size;
		this.location = new Point2D(x, y);
		this.dimension = new Dimension2D(size, size);;
		this.distance = -1;
		if (showCells && layer!=null) {
			this.visualRep = new Rectangle(size, size);
			this.visualRep.setX(location.getX());
			this.visualRep.setY(location.getY());
			this.visualRep.setFill(Color.TRANSPARENT);
			this.visualRep.setStroke(Color.rgb(0,0,0,0.8));
			layer.getChildren().add(this.getVisualRep());
		}
	}

	public void resetConnections(){
		directionInPath = Direction.NONE;
		parentNode = null;
		clear(children);
	}

	public void resetValues(){
		pathCell = false;
		visited = false;
		distance = -1;
		heuristic = 0;
		movementCost = 10;
		penaltyCost = 0;
		totalCost = 0;
	}

	public void clear(CellNode[] children){
		for(int i = 0; i<children.length; children[i] = null, i++);
	}

	public void setLocation(double x, double y) {
		this.location = new Point2D(x, y);
		if (showCells && layer!=null) {
			this.visualRep.setX(x);
			this.visualRep.setY(y);
		}
	}

	public void setDimension(double width, double height) {
		this.dimension = new Dimension2D(width, height);
	}

	public void setContainsTarget(boolean state) {
		setTargetCell(state);
	}

	public void updateVisuals(){
		if (showCells && layer!=null){
			if(spawnAllowed && !pathCell && !occupied && !targetCell && availableCell && !playerSpawnZone)
				visualRep.setFill(Color.TRANSPARENT);
			if(!isSpawnAllowed() && !isTeleportZone() && !isDangerZone() && isTraversable())
				visualRep.setFill(Color.rgb(255, 170, 0));
			if(isDangerZone())
				visualRep.setFill(Color.rgb(250, 110, 0));
			if(!isTraversable())
				visualRep.setFill(Color.RED.darker());
			if(isPathToTail())
				visualRep.setFill(Color.GRAY);
			if(isOccupied())
				visualRep.setFill(Color.WHITE);
			if(isPathCell())
				visualRep.setFill(Color.BLUE);
			if(isTargetCell())
				visualRep.setFill(Color.GREEN);
			if(!isAvailable() && !isTargetCell() && !isPathCell() && !isPathToTail())
				visualRep.setFill(Color.YELLOW);
			if(isTeleportZone())
				visualRep.setFill(Color.BLACK);
			if(isPlayerSpawnZone() && isSpawnAllowed() && !isPathCell())
				visualRep.setFill(Color.WHITE);
		}
	}

	public void killFamily() {
		if(getParentNode()!=null){
			if(getParentNode().getParentNode()!=null){
				getParentNode().setParentNode(null);
			}else{
				setParentNode(null);
			}
		}
		if(getChildren().length>0){
			for(int i = 0; i<getChildren().length; i++){
				if(getChildren()[i]!=null){
					if(getChildren()[i].getChildren()!=null){
						getChildren()[i].setChildren(null);
					}
					else{
						setChildren(null);
					}
				}
			}
		}
	}

	public void setSpawnAllowed(boolean state) {
		this.spawnAllowed = state;
		this.penaltyCost = 200;
	}

	public void setPathCell(boolean state) {
		this.pathCell = state;
	}

	public final void setTraversable(boolean state) {
		this.isTraversable = state;
	}

	public void setOccupied(boolean state) {
		this.occupied = state;
	}

	public boolean invalidSpawnZone(){
		return this.getIndex().getRow()==0 || this.getIndex().getRow()==1 ||
				this.getIndex().getCol()==1 || this.getIndex().getCol()==2 ||
				this.getIndex().getRow()==grid.getRowCount()-1 ||
				this.getIndex().getRow()==grid.getRowCount()-2 ||
				this.getIndex().getCol()==grid.getColumnCount()-1||
				this.getIndex().getCol()==grid.getColumnCount()-2;
	}

	public boolean teleportZone(){
		return this.getIndex().getRow()==0 ||
				this.getIndex().getCol()==1 ||
				this.getIndex().getRow()==grid.getRowCount()-1 ||
				this.getIndex().getCol()==grid.getColumnCount()-1;
	}

	public boolean fruitSpawnAllowed(){
		return isSpawnAllowed() &&
				isAvailable() &&
				!isOccupied() &&
				!isPlayerSpawnZone() &&
				!isTeleportZone() &&
				grid.safeSpawn(this);
	}

	public boolean isInvalidSpawnZone() {
		return invalidSpawnZone;
	}

	public boolean isTeleportZone() {
		return teleportZone;
	}

	public void setTeleportZone(boolean teleportZone) {
		this.teleportZone = teleportZone;
	}

	public boolean isPathCell() {
		return pathCell;
	}

	public final CellNode getParentNode() {
		return parentNode;
	}

	public final CellNode[] getChildren() {
		return children;
	}

	public final void setPlayerSpawnZone(boolean state) {
		this.playerSpawnZone = state;
	}

	public final boolean isPlayerSpawnZone() {
		return playerSpawnZone;
	}

	public final void setParentNode(CellNode parent) {
		this.parentNode = parent;
	}

	public final void setChildren(CellNode[] children) {
		this.children = children;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean isObjective() {
		return objective;
	}

	public void setObjective(boolean objective) {
		this.objective = objective;
	}

	public int getID() {
		return id;
	}

	public Index2D getIndex() {
		return index;
	}

	public void setIndex(Index2D index){
		this.index = index;
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

	public final boolean isTraversable() {
		return isTraversable;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public final boolean isSpawnAllowed() {
		return spawnAllowed;
	}

	public final void setAvailable(boolean state) {
		this.availableCell = state;
	}

	public final boolean isAvailable() {
		return availableCell;
	}

	public final boolean isTargetCell() {
		return targetCell;
	}

	public final boolean isPenalized() {
		return penaltyCost > 0;
	}

	public void pathToGoal(boolean state) {
		this.pathToGoal = state;
	}

	public boolean isPathToGoal() {
		return pathToGoal;
	}

	public void pathToTail(boolean state) {
		this.pathToTail = state;
	}

	public boolean isPathToTail() {
		return pathToTail;
	}

	public final void setDangerZone(boolean state) {
		this.dangerZone = state;
	}

	public final boolean isDangerZone() {
		return dangerZone;
	}

	public void setTargetCell(boolean state) {
		this.targetCell = state;
	}

	public double getSize() {
		return cellSize;
	}

	public final double getHeuristic() {
		return heuristic;
	}

	public final void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

	public final double getMovementCost() {
		return movementCost + getPenaltyCost();
	}

	public final void setMovementCost(double movementCost) {
		this.movementCost = movementCost;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public boolean isVisited() {
		return visited;
	}

	public double getPenaltyCost() {
		return penaltyCost;
	}

	public void setPenaltyCost(double movementPanelty) {
		this.penaltyCost = movementPanelty;
	}

	public final double getTotalCost() {
		return totalCost;
	}

	public final void setTotalCost(double cost) {
		this.totalCost = cost;
	}

	public final void setVisualRep(Rectangle visualRep) {
		this.visualRep = visualRep;
	}

	public Rectangle2D getBoundsCheck() {
		return new Rectangle2D(location.getX(), location.getY(), dimension.getWidth(), dimension.getHeight());
	}

	public Direction getDirection() {
		return directionInPath;
	}

	public void setDirection(Direction direction) {
		this.directionInPath = direction;
	}

	public final CellType getCellType() {
		return cellType;
	}

	public final void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public enum CellType {
		BLOCKED, TRAVERSABLE, UNAVAILABLE, FREE
	}

	public enum Direction {
		UP, DOWN, LEFT, RIGHT, NONE;

		@Override
		public String toString() {
			return this.name();
		}
	}

	public CellNode getCloned() {

		CellNode clonedCell = new CellNode();

		clonedCell.setIndex(getIndex());
		clonedCell.setCellType(getCellType());
		clonedCell.setAvailable(isAvailable());
		clonedCell.setDangerZone(isDangerZone());
		clonedCell.setDimension(getDimension());
		clonedCell.setDirection(getDirection());
		clonedCell.setDistance(getDistance());
		clonedCell.setHeuristic(getHeuristic());
		clonedCell.setLocation(getLocation());
		clonedCell.setMovementCost(getMovementCost());
		clonedCell.setObjective(isObjective());
		clonedCell.setOccupied(isOccupied());
		clonedCell.setPathCell(isPathCell());
		clonedCell.setPenaltyCost(getPenaltyCost());
		clonedCell.setPlayerSpawnZone(isPlayerSpawnZone());
		clonedCell.setSpawnAllowed(isSpawnAllowed());
		clonedCell.setTeleportZone(isTeleportZone());
		clonedCell.setTargetCell(isTargetCell());
		clonedCell.setTraversable(isTraversable());
		clonedCell.setTotalCost(getTotalCost());
		clonedCell.setVisited(isVisited());

		if( getParentNode() != null) {
			clonedCell.setParentNode(parentNode.getCloned());
		}

		if( getChildren().length > 0) {
			clonedCell.setChildren(getChildren());
		}

		return clonedCell;

	}

	public Direction getDirectionTo(CellNode node){

		if (node.getIndex().getRow() > getIndex().getRow()) {
			return Direction.DOWN;
		}
		else if (node.getIndex().getRow() < getIndex().getRow()) {
			return Direction.UP;
		}
		else if (node.getIndex().getCol() > getIndex().getCol()) {
			return Direction.RIGHT;
		}
		else if (node.getIndex().getCol() < getIndex().getCol()) {
			return Direction.LEFT;
		}
		return Direction.NONE;
	}

	public double getDistanceFrom(CellNode from) {
		return SearchAlgorithm.calculateManhathanDistance(from.getLocation().getX(), getLocation().getX(), from.getLocation().getY(), getLocation().getY());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CellNode other = (CellNode) obj;
		if (this.index.getRow() != other.index.getRow()) {
			return false;
		}
		if (this.index.getCol() != other.index.getCol()) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 17 * hash + this.index.getRow();
		hash = 17 * hash + this.index.getCol();
		return hash;
	}

	@Override
	public int compareTo(CellNode node) {
		return Double.compare(this.getTotalCost(),node.getTotalCost());
	}


}
