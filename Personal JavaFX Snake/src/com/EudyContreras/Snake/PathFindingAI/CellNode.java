package com.EudyContreras.Snake.PathFindingAI;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellNode {

	private Index2D index;
	private Point2D location;
	private Rectangle visualRep;
	private Dimension2D dimension;
	private CellNode parentNode;

	private int id = 0;
	private double heuristic = 0;
	private double movementCost = 10;
    private double movementPenalty = 0;
	private double totalCost = 0;

	private boolean visited = false;
	private boolean showCells = true;
	private boolean pathCell = false;
	private boolean targetCell = false;
	private boolean isTraversable = true;
	private boolean spawnAllowed = true;

	private CellType cellType = CellType.FREE;

	public CellNode(double x, double y, double width, double height, int id, Index2D index) {
		this.id = id;
		this.index = index;
		this.location = new Point2D(x, y);
		this.dimension = new Dimension2D(width, height);
		if (showCells) {
			this.visualRep = new Rectangle(width, height);
			this.visualRep.setX(location.getX());
			this.visualRep.setY(location.getY());
			this.visualRep.setFill(Color.rgb(0, 0, 0, .3));
		}
	}
	public void resetValues(){
		heuristic = 0;
		movementCost = 10;
	    movementPenalty = 0;
	    totalCost = 0;
	    parentNode = null;
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
		if (showCells) {
			visualRep.setFill(state ? Color.GREEN : Color.rgb(0, 0, 0, 0.3));
		}
	}

	public void setSpawnAllowed(boolean state) {
		this.spawnAllowed = state;
		if (showCells) {
			visualRep.setFill(state ? Color.rgb(0, 0, 0, 0.3) : Color.ORANGE);
		}
	}
	public void setFree(boolean state) {
		if (showCells) {
			visualRep.setFill(Color.rgb(0, 0, 0, 0.3));
		}
	}
	public void setPathCell(boolean state) {
		this.pathCell = state;
		if (showCells && isTraversable()) {
			visualRep.setFill(state ? Color.BLUE : Color.rgb(0, 0, 0, 0.3));
		}

	}
	public boolean getPathCell(){
		return pathCell;
	}
	public final CellNode getParentNode() {
		return parentNode;
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

	public final void setTraversable(boolean state) {
		this.isTraversable = state;
		if (showCells) {
			visualRep.setFill(state ? Color.rgb(0, 0, 0, 0.3) : Color.RED);
		}
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

	public void setTargetCell(boolean state) {
		this.targetCell = state;
	}

	public final void setVisited(boolean visited) {
		this.visited = visited;
	}

	public final double getHeuristic() {
		return heuristic;
	}

	public final void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

	public final double getMovementCost() {
		return movementCost;
	}

	public final void setMovementCost(double movementCost) {
		this.movementCost = movementCost;
	}

	public double getMovementPenalty() {
		return movementPenalty;
	}
	public void setMovementPlty(double movementPanelty) {
		this.movementPenalty = movementPanelty;
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

	public final CellType getCellType() {
		return cellType;
	}

	public final void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public enum CellType {
		BLOCKED, TRAVERSABLE, UNAVAILABLE, FREE
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



}
