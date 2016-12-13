package com.EudyContreras.Snake.PathFindingAI;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellNode implements Comparable<CellNode>{

	private Pane layer;
	private IndexWrapper index;
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

	private boolean headCell = false;
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
	private boolean traversable = true;
	private boolean goalRadius = false;
	private boolean spawnAllowed = true;
	private boolean invalidSpawnZone = false;
	private boolean available = true;
	private boolean playerSpawnZone = false;

	private CellType cellType = CellType.FREE;
	private Direction direction = Direction.NONE;

	public CellNode(){}

	public CellNode(GridNode grid, Pane layer, double x, double y, double size, int id, IndexWrapper index) {
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
			layer.getChildren().add(visualRep);
		}
	}

	public CellNode resetConnections(){
		direction = Direction.NONE;
		children = null;
		parentNode = null;
		pathCell = false;
		objective = false;
		return this;
	}

	public CellNode resetValues(){
		visited = false;
		distance = -1;
		heuristic = 0;
		movementCost = 10;
		penaltyCost = 0;
		totalCost = 0;
		return this;
	}

	public final void updateVisuals(){
		if (showCells && layer!=null){
			if(spawnAllowed && !goalRadius && !pathToGoal && !pathToTail && !occupied && !targetCell && available && !playerSpawnZone)
				visualRep.setFill(Color.TRANSPARENT);
			if(!isSpawnAllowed() && !isTeleportZone() && !isDangerZone() && isTraversable())
				visualRep.setFill(Color.rgb(255, 170, 0));
			if(isDangerZone())
				visualRep.setFill(Color.rgb(250, 110, 0));
			if(!isTraversable())
				visualRep.setFill(Color.RED.darker());
			if(isPathToTail() && !isOccupied())
				visualRep.setFill(Color.GRAY);
			if(isOccupied())
				visualRep.setFill(Color.WHITE);
			if(isPathToGoal())
				visualRep.setFill(Color.BLUE);
			if(isTargetCell())
				visualRep.setFill(Color.GREEN);
			if(isGoalRadius() && !isTargetCell() && !isPathToTail())
				visualRep.setFill(Color.YELLOW);
			if(isTeleportZone())
				visualRep.setFill(Color.BLACK);
			if(isPlayerSpawnZone() && isSpawnAllowed() && !isPathCell())
				visualRep.setFill(Color.WHITE);
		}
	}

	public CellNode killFamily() {
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
		return this;
	}

	public final boolean invalidSpawnZone(){
		return this.getIndex().getRow()==0 || this.getIndex().getRow()==1 ||
				this.getIndex().getCol()==1 || this.getIndex().getCol()==2 ||
				this.getIndex().getRow()==grid.getRowCount()-1 ||
				this.getIndex().getRow()==grid.getRowCount()-2 ||
				this.getIndex().getCol()==grid.getColumnCount()-1||
				this.getIndex().getCol()==grid.getColumnCount()-2;
	}

	public final boolean teleportZone(){
		return this.getIndex().getRow()==0 ||
				this.getIndex().getCol()==1 ||
				this.getIndex().getRow()==grid.getRowCount()-1 ||
				this.getIndex().getCol()==grid.getColumnCount()-1;
	}

	public final boolean fruitSpawnAllowed(){
		return isSpawnAllowed() &&
				isAvailable() &&
				!isGoalRadius() &&
				!isOccupied() &&
				!isPlayerSpawnZone() &&
				!isTeleportZone() &&
				grid.safeSpawn(this);
	}

	public CellNode setLocation(double x, double y) {
		this.location = new Point2D(x, y);
		if (showCells && layer!=null) {
			this.visualRep.setX(x);
			this.visualRep.setY(y);
		}
		return this;
	}


	public final boolean isHeadCell() {
		return headCell;
	}

	public CellNode setHeadCell(boolean headCell) {
		this.headCell = headCell;
		return this;
	}

	public CellNode setDimension(double width, double height) {
		this.dimension = new Dimension2D(width, height);
		return this;
	}

	public void setContainsTarget(boolean state) {
		setTargetCell(state);
	}

	public final boolean isPenalized() {
		return penaltyCost > 0;
	}

	public final double getMovementCost() {
		return movementCost + getPenaltyCost();
	}

	public CellNode setMovementCost(double movementCost) {
		this.movementCost = movementCost;
		return this;
	}

	public final IndexWrapper getIndex() {
		return index;
	}

	public CellNode setIndex(IndexWrapper index) {
		this.index = index;
		return this;
	}

	public final boolean isGoalRadius() {
		return goalRadius;
	}

	public CellNode setGoalRadius(boolean goalRadius) {
		this.goalRadius = goalRadius;
		return this;
	}

	public final Point2D getLocation() {
		return location;
	}

	public CellNode setLocation(Point2D location) {
		this.location = location;
		return this;
	}

	public final CellNode getParentNode() {
		return parentNode;
	}

	public CellNode setParentNode(CellNode parentNode) {
		this.parentNode = parentNode;
		return this;
	}

	public final CellNode[] getChildren() {
		return children;
	}

	public final CellNode setChildren(CellNode[] children) {
		this.children = children;
		return this;
	}

	public final Dimension2D getDimension() {
		return dimension;
	}

	public CellNode setDimension(Dimension2D dimension) {
		this.dimension = dimension;
		return this;
	}

	public final int getId() {
		return id;
	}

	public CellNode setId(int id) {
		this.id = id;
		return this;
	}

	public final double getCellSize() {
		return cellSize;
	}

	public CellNode setCellSize(double cellSize) {
		this.cellSize = cellSize;
		return this;
	}

	public final double getDistance() {
		return distance;
	}

	public CellNode setDistance(double distance) {
		this.distance = distance;
		return this;
	}

	public final double getHeuristic() {
		return heuristic;
	}

	public CellNode setHeuristic(double heuristic) {
		this.heuristic = heuristic;
		return this;
	}

	public final double getPenaltyCost() {
		return penaltyCost;
	}

	public CellNode setPenaltyCost(double penaltyCost) {
		this.penaltyCost = penaltyCost;
		return this;
	}

	public final double getTotalCost() {
		return totalCost;
	}

	public CellNode setTotalCost(double totalCost) {
		this.totalCost = totalCost;
		return this;
	}

	public final boolean isVisited() {
		return visited;
	}

	public CellNode setVisited(boolean visited) {
		this.visited = visited;
		return this;
	}

	public final boolean isPathCell() {
		return pathCell;
	}

	public CellNode setPathCell(boolean pathCell) {
		this.pathCell = pathCell;
		return this;
	}

	public final boolean isTargetCell() {
		return targetCell;
	}

	public CellNode setTargetCell(boolean targetCell) {
		this.targetCell = targetCell;
		return this;
	}

	public final boolean isOccupied() {
		return occupied;
	}

	public CellNode setOccupied(boolean occupied) {
		this.occupied = occupied;
		return this;
	}

	public final boolean isTeleportZone() {
		return teleportZone;
	}

	public CellNode setTeleportZone(boolean teleportZone) {
		this.teleportZone = teleportZone;
		return this;
	}

	public final boolean isDangerZone() {
		return dangerZone;
	}

	public CellNode setDangerZone(boolean dangerZone) {
		this.dangerZone = dangerZone;
		return this;
	}

	public final boolean isPathToGoal() {
		return pathToGoal;
	}

	public CellNode setPathToGoal(boolean pathToGoal) {
		this.pathToGoal = pathToGoal;
		return this;
	}

	public final boolean isPathToTail() {
		return pathToTail;
	}

	public CellNode setPathToTail(boolean pathToTail) {
		this.pathToTail = pathToTail;
		return this;
	}

	public final boolean isObjective() {
		return objective;
	}

	public CellNode setObjective(boolean objective) {
		if (!isTeleportZone()) {
			this.objective = objective;
		}
		return this;
	}

	public final boolean isTraversable() {
		return traversable;
	}

	public CellNode setTraversable(boolean traversable) {
		this.traversable = traversable;
		return this;
	}

	public final boolean isSpawnAllowed() {
		return spawnAllowed;
	}

	public CellNode setSpawnAllowed(boolean spawnAllowed) {
		this.spawnAllowed = spawnAllowed;
		return this;
	}

	public final boolean isInvalidSpawnZone() {
		return invalidSpawnZone;
	}

	public CellNode setInvalidSpawnZone(boolean invalidSpawnZone) {
		this.invalidSpawnZone = invalidSpawnZone;
		return this;
	}

	public final boolean isAvailable() {
		return available;
	}

	public CellNode setAvailable(boolean availableCell) {
		this.available = availableCell;
		return this;
	}

	public final boolean isPlayerSpawnZone() {
		return playerSpawnZone;
	}

	public CellNode setPlayerSpawnZone(boolean playerSpawnZone) {
		this.playerSpawnZone = playerSpawnZone;
		return this;
	}

	public final CellType getCellType() {
		return cellType;
	}

	public CellNode setCellType(CellType cellType) {
		this.cellType = cellType;
		return this;
	}

	public final Direction getDirection() {
		return direction;
	}

	public CellNode setDirection(Direction direction) {
		this.direction = direction;
		return this;
	}

	public Rectangle2D getBoundsCheck() {
		return new Rectangle2D(location.getX(), location.getY(), dimension.getWidth(), dimension.getHeight());
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
	public int compareTo(CellNode node) {
		return Double.compare(this.getTotalCost(),node.getTotalCost());
	}

	@Override
	public String toString(){
		return "ID: "+this.getId()+" Index: "+index.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CellNode other = (CellNode) obj;
		if (this.index.getRow() != other.index.getRow())
			return false;
		if (this.index.getCol() != other.index.getCol())
			return false;
		if (id != other.id)
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
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

}
