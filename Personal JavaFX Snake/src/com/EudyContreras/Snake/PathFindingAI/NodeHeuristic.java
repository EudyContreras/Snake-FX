package com.EudyContreras.Snake.PathFindingAI;

import java.util.List;
import java.util.stream.Collectors;

import com.EudyContreras.Snake.PathFindingAI.CellNode.Direction;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.geometry.Point2D;



/**
 * Depending on the type, assigns a heuristic value
 * to a CellNode thus allows the node to be compared to
 * other nodes based on the returned heuristic value.
 * @author Eudy Contreras
 *
 */
public class NodeHeuristic {

	public static final int COST_PER_MOVE = 10;


	public final static double GET_HEURISTIC(Type type, CellNode node, GridNode grid, PlayerTwo snakeAI) {
		switch (type) {
		case DIRECTION:
			return directionRank(node,grid,snakeAI);
		case DISTANCE:
			return distanceToGoal(node,grid,snakeAI);
		case MANHATTAN:
			return manhattanDistance(node,grid,snakeAI);
		case DIRECTIONMANHATTAN:
			return (int) ((directionRank(node,grid,snakeAI) + manhattanDistance(node,grid,snakeAI)) / 2.0);
		case DISTANCEDIRECTION:
			return (int) ((distanceToGoal(node,grid,snakeAI) + directionRank(node,grid,snakeAI)) / 2.0);
		case DISTANCEMANHATTAN:
			return (int) ((distanceToGoal(node,grid,snakeAI) + manhattanDistance(node,grid,snakeAI)) / 2.0);
		}
		return 0;
	}

	private final static boolean inSameDirection(Point2D start, Point2D end, Direction direction) {
		switch (direction) {
		case DOWN:
			return start.getX() == end.getX() && start.getY() <= end.getY();
		case LEFT:
			return start.getY() == end.getY() && start.getX() >= end.getX();
		case RIGHT:
			return start.getY() == end.getY() && start.getX() <= end.getX();
		case UP:
			return start.getX() == end.getX() && start.getY() >= end.getY();
		default:
			break;
		}
		return false;
	}

	private final static boolean inOppositeDirection(Point2D start, Point2D end, Direction direction) {
		switch (direction) {
		case DOWN:
			return start.getX() == end.getX() && start.getY() > end.getY();
		case LEFT:
			return start.getY() == end.getY() && start.getX() < end.getX();
		case RIGHT:
			return start.getY() == end.getY() && start.getX() > end.getX();
		case UP:
			return start.getX() == end.getX() && start.getY() < end.getY();
		default:
			break;
		}
		return false;
	}

	/**
	 * Assumes not same direction
	 * @param start
	 * @param end
	 * @param direction
	 * @return
	 */
	private final static boolean inDiagonalDirection(Point2D start, Point2D end, Direction direction) {
		switch (direction) {
		case DOWN:
			return start.getY() <= end.getY();
		case LEFT:
			return start.getX() >= end.getX();
		case RIGHT:
			return start.getX() <= end.getX();
		case UP:
			return start.getY() >= end.getY();
		default:
			break;
		}
		return false;
	}

	/**
	 * Assumes not same direction, not same diagonal direction or opposite direction
	 * @param start
	 * @param end
	 * @param direction
	 * @return
	 */
	private final static boolean inPerpendicularDirection(Point2D start, Point2D end, Direction direction) {
		switch (direction) {
		case DOWN:
		case UP:
			return start.getY() == end.getY();
		case LEFT:
		case RIGHT:
			return start.getX() == end.getX();
		default:
			break;
		}

		return false;
	}

	public static final double calculateDistance(double fromX, double toX, double fromY, double toY) {
		return Math.hypot(fromX - toX, fromY - toY);
	}

	public static final double calculateManhathanDistance(double fromX, double toX, double fromY, double toY) {
		return Math.abs(fromX - toX) + Math.abs(fromY - toY);
	}

	public static final double calculateEuclidianDistance(double fromX, double toX, double fromY, double toY) {
		return Math.sqrt((fromX - toX) * (fromX - toX) + (fromY - toY) * (fromY - toY));
	}

	public static final double getCrossPolarDistance(CellNode start, CellNode closestRelativeCell, CellNode closestPolarCell, CellNode objective){
		double distanceOne = calculateManhathanDistance(start.getLocation().getX(),start.getLocation().getY(),closestRelativeCell.getLocation().getX(),closestRelativeCell.getLocation().getY());
		double distanceTwo = calculateManhathanDistance(closestPolarCell.getLocation().getX(),closestPolarCell.getLocation().getY(), objective.getLocation().getX(),objective.getLocation().getY());;
		return (distanceOne+distanceTwo);
	}

	@SuppressWarnings("unused")
	private static final double getHeuristicCost(CellNode start, CellNode end, Double scale) {
		double dx = Math.abs(start.getLocation().getX() - end.getLocation().getX());
		double dy = Math.abs(start.getLocation().getY() - end.getLocation().getY());
		return scale * (dx + dy);
	}

	public static final double heuristicCostEstimate(CellNode start, CellNode end, int scale, HeuristicType cost) {
		double distance = 0;
		switch(cost){
		case CUSTOM_EUCLUDIAN:
			distance = scale*calculateDistance(start.getLocation().getX(), end.getLocation().getX(), start.getLocation().getY(),end.getLocation().getY());
			break;
		case EUCLIDIAN:
			distance = scale*calculateEuclidianDistance(start.getLocation().getX(), end.getLocation().getX(), start.getLocation().getY(),end.getLocation().getY());
			break;
		case MANHATHAN:
			distance = scale*calculateManhathanDistance(start.getLocation().getX(), end.getLocation().getX(), start.getLocation().getY(),end.getLocation().getY());
			break;
		}
		return distance;
	}

	/**
	 * TODO: Determine if this is a good heuristic or not
	 *
	 * Returns 10% if any of the foods are in same direction as node
	 * Returns 20% if is diagonal to the direction as node
	 * Returns 30% if perpendicular to direction of node
	 * Returns 40% if diagonally away as direction of node
	 * Returns 50% otherwise
	 * The percentage is of the cost per move (20% of COST_PER_MOVE=10 is 2)
	 *
	 * If cost per move was 10, it will return the following numbers:
	 * Example: Moving down, and numbers represent where the food is and its rank
	 *               4   5   4
	 *               3   D   3
	 *               2   1   2
	 * @param node
	 * @return
	 */
	private final static int directionRank(CellNode node, GridNode grid, PlayerTwo snakeAI) {

		List<CellNode> foodLocations = grid.getFreeCells().stream().filter(nodes->nodes.isTargetCell()).collect(Collectors.toList());
		Point2D snakeHead = grid.getHeadCell(snakeAI).getLocation();
		Direction currentDirection = node.getDirection();

		for (CellNode cell : foodLocations) {
			if (inSameDirection(snakeHead, cell.getLocation(), currentDirection)) {
				return (int) (0.1*COST_PER_MOVE);
			}
		}

		for (CellNode cell : foodLocations) {
			if (inOppositeDirection(snakeHead, cell.getLocation(), currentDirection)) {
				return (int) (0.5*COST_PER_MOVE);
			}
		}

		for (CellNode cell : foodLocations) {
			if (inDiagonalDirection(snakeHead, cell.getLocation(), currentDirection)) {
				return (int) (0.2*COST_PER_MOVE);
			}
		}

		for (CellNode cell : foodLocations) {
			if (inPerpendicularDirection(snakeHead, cell.getLocation(), currentDirection)) {
				return (int) (0.3*COST_PER_MOVE);
			}
		}

		return (int) (0.4*COST_PER_MOVE);	// Must all be in opposite diagonal direction
	}

	/**
	 * Returns the exact diagonal distance between the snake head
	 * and the closest piece of food
	 * @param node
	 * @return
	 */
	private final static int distanceToGoal(CellNode node, GridNode grid, PlayerTwo snakeAI) {
		int minDistance = Integer.MAX_VALUE;
		List<CellNode> goals = grid.getFreeCells().stream().filter(nodes->nodes.isTargetCell()).collect(Collectors.toList());
		Point2D snakeHead = grid.getHeadCell(snakeAI).getLocation();

		for (CellNode goal : goals) {
			int distanceToGoal = (int) snakeHead.distance(goal.getLocation().getX(), goal.getLocation().getY());
			if (distanceToGoal < minDistance) {
				minDistance = distanceToGoal;
			}
		}
		return minDistance * COST_PER_MOVE;
	}

	/**
	 * Calculates distance by moving horizontally and vertically to goal
	 * Returns distance to closest goal
	 * @param node
	 * @return
	 */
	private final static double manhattanDistance(CellNode node, GridNode grid, PlayerTwo snakeAI) {
		double minDistance = Integer.MAX_VALUE;
		List<CellNode> goals = grid.getFreeCells().stream().filter(nodes->nodes.isTargetCell()).collect(Collectors.toList());
		Point2D snakeHead = grid.getHeadCell(snakeAI).getLocation();

		for (CellNode goal : goals) {

			double distanceToGoal = Math.abs(goal.getLocation().getX()-snakeHead.getX()) + Math.abs(goal.getLocation().getY()-snakeHead.getY());
			if (distanceToGoal < minDistance) {
				minDistance = distanceToGoal;
			}
		}
		return minDistance * COST_PER_MOVE;
	}

	public enum HeuristicType{
		MANHATHAN, EUCLIDIAN, CUSTOM_EUCLUDIAN,
	}

	public enum Type {
		DISTANCE, DIRECTION, MANHATTAN, DISTANCEDIRECTION, DISTANCEMANHATTAN, DIRECTIONMANHATTAN
	}
}
