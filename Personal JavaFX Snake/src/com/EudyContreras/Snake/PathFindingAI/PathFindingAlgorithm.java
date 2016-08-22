package com.EudyContreras.Snake.PathFindingAI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class PathFindingAlgorithm {


	/**
	 * Find a path from start to goal using the A* algorithm
	 */

	public List<CellNode> getPath( GridNode grid, CellNode startingPoint, CellNode objective) {

		CellNode current = null;
		boolean containsNeighbor;

		int cellCount = grid.getRowCount() * grid.getColumnCount();

		HashSet<CellNode> closedSet = new HashSet<>(cellCount);

	    PriorityQueue<CellNode> openSet = new PriorityQueue<CellNode>( cellCount, new CellComparator());

		openSet.add( startingPoint);

		startingPoint.setMovementCost(0d);

		startingPoint.setTotalCost(startingPoint.getMovementCost() + heuristicCostEstimate(startingPoint, objective));


		while( !openSet.isEmpty()) {

			current = openSet.poll();

			if( current == objective) {
				return reconstructPath( objective);
			}

			closedSet.add( current);

			for( CellNode neighbor: grid.getNeighborCells( current, closedSet)) {

				if( neighbor == null) {
					continue;
				}

				if( closedSet.contains( neighbor)) {
					continue;
				}

				double tentativeScoreG = current.getMovementCost() + distanceBetween( current, neighbor);

				if( !(containsNeighbor=openSet.contains( neighbor)) || Double.compare(tentativeScoreG, neighbor.getMovementCost()) < 0) {

					neighbor.setParentNode(current);

					neighbor.setMovementCost(tentativeScoreG);

					neighbor.setTotalCost(heuristicCostEstimate(neighbor, objective));
					neighbor.setTotalCost( neighbor.getMovementCost() + neighbor.getHeuristic());

					if( !containsNeighbor) {
						openSet.add( neighbor);
					}
				}
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Create final path of the A* algorithm.
	 * The path is from goal to start.
	 */
	private List<CellNode> reconstructPath( CellNode current) {

		List<CellNode> totalPath = new ArrayList<>(200); // arbitrary value, we'll most likely have more than 10 which is default for java

		totalPath.add( current);

		while( (current = current.getParentNode()) != null) {

			totalPath.add( current);

		}

		return totalPath;
	}

	/**
	 * Distance between a given cell and its neighbor.
	 * Used in the algorithm as distance calculation between the current cell and a neighbor.
	 * In our case we use the same distance which we use from the current cell to the goal.
	 */
	private double distanceBetween(CellNode current, CellNode neighbor) {
		return heuristicCostEstimate( current, neighbor);
	}

	/**
	 * Distance between two cells. We use the euclidian distance here.
	 * Used in the algorithm as distance calculation between a cell and the goal.
	 */
	private double heuristicCostEstimate(CellNode start, CellNode end) {

		return Math.sqrt((start.getIndex().getCol()-end.getIndex().getCol())*(start.getIndex().getCol()-end.getIndex().getCol()) + (start.getIndex().getRow() - end.getIndex().getRow())*(start.getIndex().getRow()-end.getIndex().getRow()));

	}
	/**
	 * Get the cell with the minimum f value.
	 */
	public class CellComparator implements Comparator<CellNode>
	{
	    @Override
	    public int compare(CellNode a, CellNode b)
	    {
	    	return Double.compare(a.getTotalCost(), b.getTotalCost());
	    }
	}
	public static double calculateDistance(double x1, double x2, double y1, double y2) {
	    return Math.hypot(x1 - x2, y1 - y2);
	}
	public static double calculateDistanceAlt(double x1, double x2, double y1, double y2) {
		return Math.abs(x1 - x2)+Math.abs(y1 - y2);
	}
}
