package com.EudyContreras.Snake.PathFinder;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PathFindingCell {
	private Index2D index;
	private Point2D location;
	private Rectangle visualRep;
	private Dimension2D dimension;
	private PathFindingCell parent;

	private int id = 0;
	private double heuristic = 0;
	private double movementCost = 40;
	private double totalCost = 0;

	private boolean valid = true;
	private boolean visited = false;
	private boolean showCells = true;
	private boolean targetCell = false;
	private boolean isTraversable = true;
	private boolean spawnAllowed = true;

	private CellType cellType = CellType.FREE;

	public PathFindingCell(double x, double y, double width, double height, int id, Index2D index) {
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

	public final void setValid(boolean valid) {
		this.valid = valid;
		if (showCells) {
			visualRep.setFill(valid ? Color.rgb(0, 0, 0, 0.3) : Color.RED);
		}
	}

	public void setFree(boolean state) {
		if (showCells) {
			visualRep.setFill(Color.BLACK);
		}
	}

	public final PathFindingCell getParent() {
		return parent;
	}

	public final void setParent(PathFindingCell parent) {
		this.parent = parent;
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

	public final void setTraversable(boolean isTraversable) {
		this.isTraversable = isTraversable;
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

	public final boolean isValid() {
		return valid;
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

	public final double getTotalCost() {
		return totalCost;
	}

	public final void setTotalCost(double cost){
		this.totalCost = cost;
	}

	public final void setVisualRep(Rectangle visualRep) {
		this.visualRep = visualRep;
	}

	public Rectangle2D placeChecker() {
		return new Rectangle2D(location.getX(), location.getY(), dimension.getWidth(), dimension.getHeight());
	}

	public final CellType getCellType() {
		return cellType;
	}

	public final void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public enum CellType {
		BLOCKED, TRAVERSABLE, UNAVAILABLE, OCUPIED, FREE
	}
}
