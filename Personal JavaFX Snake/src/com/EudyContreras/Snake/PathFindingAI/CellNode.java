package com.EudyContreras.Snake.PathFindingAI;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellNode implements Comparable<CellNode>{

	private Index2D index;
	private Point2D location;
	private GridNode grid;
	private CellNode parentNode;
	private Rectangle visualRep;
	private Dimension2D dimension;

	private int id = 0;

	private double cellSize = 0;
	private double heuristic = 0;
	private double movementCost = 10;
    private double penaltyCost = 0;
	private double totalCost = 0;

	private boolean closed = false;
	private boolean showCells = true;
	private boolean pathCell = false;
	private boolean targetCell = false;
	private boolean occupied = false;
	private boolean teleportZone = false;
	private boolean dangerZone = false;
	private boolean isTraversable = true;
	private boolean spawnAllowed = true;
	private boolean invalidSpawnZone = false;
	private boolean availableCell = true;
	private boolean playerSpawnZone = false;

	private CellType cellType = CellType.FREE;
	private Direction directionInPath = Direction.NONE;

	public CellNode(GridNode grid, Pane layer, double x, double y, double size, int id, Index2D index) {
		this.id = id;
		this.grid = grid;
		this.index = index;
		this.cellSize = size;
		this.location = new Point2D(x, y);
		this.dimension = new Dimension2D(size, size);
		if (showCells) {
			this.visualRep = new Rectangle(size, size);
			this.visualRep.setX(location.getX());
			this.visualRep.setY(location.getY());
			this.visualRep.setFill(Color.TRANSPARENT);
			this.visualRep.setStroke(Color.rgb(255,255,255,0.8));
		    layer.getChildren().add(this.getVisualRep());
		}
	}
	public void resetValues(){
		directionInPath = Direction.NONE;
	    parentNode = null;
		pathCell = false;
		heuristic = 0;
		movementCost = 10;
	    penaltyCost = 0;
	    totalCost = 0;
	}
	public void setLocation(double x, double y) {
		this.location = new Point2D(x, y);
		if (showCells) {
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
		if (showCells){
			if(spawnAllowed && !pathCell && !occupied && !targetCell && availableCell && !playerSpawnZone)
				visualRep.setFill(Color.TRANSPARENT);
			if(!isSpawnAllowed() && !isTeleportZone() && !isDangerZone() && isTraversable())
				visualRep.setFill(Color.rgb(255, 170, 0));
			if(isDangerZone())
				visualRep.setFill(Color.rgb(250, 110, 0));
			if(!isTraversable())
				visualRep.setFill(Color.RED.darker());
			if(isPathCell())
				visualRep.setFill(Color.BLUE);
			if(isOccupied())
				visualRep.setFill(Color.WHITE);
			if(isTargetCell())
				visualRep.setFill(Color.GREEN);
			if(!isAvailable() && !isTargetCell() && !isPathCell())
				visualRep.setFill(Color.YELLOW);
			if(isTeleportZone())
				visualRep.setFill(Color.BLACK);
			if(isPlayerSpawnZone() && isSpawnAllowed() && !isPathCell())
				visualRep.setFill(Color.WHITE);
		}
	}

	public void setSpawnAllowed(boolean state) {
		this.spawnAllowed = state;
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
			   !isPlayerSpawnZone()
			   && grid.safeSpawn(this);
	}
	public boolean isInvalidSpawnZone(){
		return invalidSpawnZone;
	}
	public boolean isTeleportZone(){
		return teleportZone;
	}
	public void setTeleportZone(boolean teleportZone) {
		this.teleportZone = teleportZone;
	}
	public boolean isPathCell(){
		return pathCell;
	}
	public final CellNode getParentNode() {
		return parentNode;
	}
	public final void setPlayerSpawnZone(boolean state){
		this.playerSpawnZone = state;
	}
	public final boolean isPlayerSpawnZone(){
		return playerSpawnZone;
	}
	public final void setParentNode(CellNode parent) {
		this.parentNode = parent;
	}

	public int getID() {
		return id;
	}

	public Index2D getIndex() {
		return index;
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

	public boolean isOccupied(){
		return occupied;
	}

	public final boolean isSpawnAllowed() {
		return spawnAllowed;
	}

	public final void setAvailable(boolean state){
		this.availableCell = state;
	}
	public final boolean isAvailable(){
		return availableCell;
	}
	public final boolean isTargetCell() {
		return targetCell;
	}
	public void setClosed(boolean state) {
		this.closed = state;
	}
	public boolean isClosed(){
		return closed;
	}
	public final boolean isPenalized(){
		return penaltyCost>0;
	}
	public final void setDangerZone(boolean state){
		this.dangerZone = state;
	}
	public final boolean isDangerZone(){
		return dangerZone;
	}

	public void setTargetCell(boolean state) {
		this.targetCell = state;
	}
	public double getSize(){
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

	public double getPenaltyCost() {
		return penaltyCost;
	}
	public void setPenaltyCost(double movementPanelty) {
		this.penaltyCost = movementPanelty;
	}
	public final double getTotalCost() {
		return totalCost;
	}

	public final void setTotalCost(double cost){
		this.totalCost = cost;
	}

	public final void setVisualRep(Rectangle visualRep) {
		this.visualRep = visualRep;
	}

	public Rectangle2D getBoundsCheck() {
		return new Rectangle2D(location.getX(), location.getY(), dimension.getWidth(), dimension.getHeight());
	}
	public Direction getDirection(){
		return directionInPath;
	}
	public void setDirection(Direction direction){
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
	public enum Direction{
		UP,DOWN,LEFT,RIGHT,NONE;
		@Override
		public String toString(){
			return this.name();
		}
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
