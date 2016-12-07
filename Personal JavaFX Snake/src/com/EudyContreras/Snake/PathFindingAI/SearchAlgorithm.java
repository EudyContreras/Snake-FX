package com.EudyContreras.Snake.PathFindingAI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import com.EudyContreras.Snake.PathFindingAI.AIPathFinder.DistressLevel;
import com.EudyContreras.Snake.PathFindingAI.CellNode.Direction;
import com.EudyContreras.Snake.PathFindingAI.GridNode.Neighbor;
import com.EudyContreras.Snake.PathFindingAI.LinkedPath.ConnectionType;
import com.EudyContreras.Snake.PathFindingAI.PathCell.PathFlag;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

public class SearchAlgorithm {

	private static final int INFINITY = Integer.MAX_VALUE;
	private static final int heuristicScale = 2;

	private HeuristicType heuristicType;
	private TieBreaker tieBreaker;
	private PathType pathType;

	public SearchAlgorithm(){
		this.initialize();
	}

	public void initialize() {
		heuristicType = HeuristicType.MANHATHAN;
		pathType = PathType.SHORTEST_PATH;
		tieBreaker = TieBreaker.NONE;
	}

	public LinkedList<PathCell> GET_SHORTEST_LIST(List<LinkedList<PathCell>> paths) {

		LinkedList<PathCell> shortest = paths.get(0);

		int minSize = Integer.MAX_VALUE;

		for (int i = 0; i<paths.size(); i++) {

			if (paths.get(i).size() < minSize && !paths.get(i).isEmpty()) {

				minSize = paths.get(i).size();
				shortest = paths.get(i);
			}
		}
		return shortest;
	}

	public LinkedList<PathCell> GET_LONGEST_LIST(List<LinkedList<PathCell>> paths) {

		LinkedList<PathCell> longest = paths.get(0);

		int maxSize = Integer.MIN_VALUE;

		for (int i = 0; i<paths.size(); i++) {

			if (paths.get(i).size() > maxSize && !paths.get(i).isEmpty()) {

				maxSize = paths.get(i).size();
				longest = paths.get(i);
			}
		}
		return longest;
	}

	public CellNode GET_FARTHEST_CELL(List<CellNode> nodes){

		CellNode farthest = nodes.get(0);

		double maxDistance = Integer.MIN_VALUE;

		for (int i = 0; i < nodes.size(); i++) {

			CellNode node = nodes.get(i);

			if (node == null) {
				continue;
			}

			if (node.getDistance() > maxDistance) {

				maxDistance = node.getDistance();

				farthest = node;
			}
		}

		return farthest;
	}

	public CellNode GET_FARTHEST_CELL_ALT(PlayerTwo snakeAI, GridNode grid, CellNode from){
		int initialDistance = 10;

		LinkedList<CellNode> cells = new LinkedList<>();

		GET_FARTHEST_CELL_ALT(grid, from, from, cells, initialDistance, DistressLevel.LEVEL_TWO);

		return cells.getLast();
	}

	public void GET_FARTHEST_CELL_ALT(GridNode grid, CellNode start, CellNode current, List<CellNode> nodes, double distance, DistressLevel distressLevel){

		current.setVisited(true);

		if(current.isTeleportZone() && current.getDistance()>11){
			nodes.add(current);
			return;
		}

		for(CellNode neighbor: grid.getNeighborCells(current, distressLevel)){

			if (neighbor.isVisited()){
				continue;
			}

			if(neighbor.getDistance()==distance){
				continue;
			}

			neighbor.setDistance(distance+1);

			GET_FARTHEST_CELL_ALT(grid, start, neighbor, nodes, neighbor.getDistance(), distressLevel);
		}
	}

	public CellNode GET_FARTHEST_CELL(PlayerTwo snakeAI, GridNode grid, CellNode from){

		List<CellNode> nodes = grid.getFreeCells();

		grid.resetDistances(-1);

		grid.prepareDistances(from);

		SORT_NODES(nodes, SortingMethod.DISTANCE_SORT, SortingOrder.DESCENDING);

		CellNode farthest = nodes.get(0);

		farthest.setObjective(true);

		return farthest;
	}


	public void LABEL_DISTANCES(GridNode grid, CellNode from){

		List<CellNode> neighbors = grid.getNeighborCells(from, DistressLevel.LEVEL_THREE);

		for(CellNode neighbor : neighbors){
			if (neighbor == null) {
				continue;
			}

			if (neighbor.isVisited()){
				continue;
			}

			neighbor.setDistance(from.getDistance()+1);

			LABEL_DISTANCES(grid,neighbor);
		}
	}

	public LinkedList<PathCell> DEEP_NEIGHBOR_CHECK(PlayerTwo snakeAI,GridNode grid, CellNode cell, int depth, Neighbor neighbor){

		LinkedList<PathCell> neighbors = new LinkedList<>();

		CellNode tempCell = null;
		CellNode current = null;

		neighbors.add(0,new PathCell(cell.getIndex()));

		tempCell = grid.getNeighbor(cell, neighbor);

		tempCell.setParentNode(cell);

		if (tempCell.isTraversable() && !tempCell.isOccupied()) {
			neighbors.add(0,new PathCell(tempCell.getIndex()));
		}

		for(int i = 0; i<depth; i++){
			current = tempCell;

			tempCell = grid.getNeighbor(tempCell, neighbor);

			tempCell.setParentNode(current);

			if (tempCell.isTraversable() && !tempCell.isOccupied()) {
				neighbors.add(0,new PathCell(tempCell.getIndex()));
			}
		}

		return neighbors;
	}

	public LinkedList<PathCell> GET_BRUTE_PATH(PlayerTwo snakeAI, GridNode grid, CellNode current, int depth){

		LinkedList<PathCell> brutePath = null;

		List<LinkedList<PathCell>> paths = new LinkedList<>();

		Neighbor directionOne = null;
		Neighbor directionTwo = null;

		switch(snakeAI.getCurrentDirection()){
		case MOVE_UP:
			directionOne = Neighbor.NORTH;
			directionTwo = Neighbor.SOUTH;
			break;
		case MOVE_DOWN:
			directionOne = Neighbor.NORTH;
			directionTwo = Neighbor.SOUTH;
			break;
		case MOVE_LEFT:
			directionOne = Neighbor.EAST;
			directionTwo = Neighbor.WEST;
			break;
		case MOVE_RIGHT:
			directionOne = Neighbor.EAST;
			directionTwo = Neighbor.WEST;
			break;
		case STANDING_STILL:
			directionOne = Neighbor.NORTH;
			directionTwo = Neighbor.SOUTH;
			break;
		}

		paths.add(DEEP_NEIGHBOR_CHECK(snakeAI, grid, current, depth, directionOne));

		paths.add(DEEP_NEIGHBOR_CHECK(snakeAI, grid, current, depth, directionTwo));

		brutePath = GET_LONGEST_LIST(paths);

		brutePath.get(0).setFlag(PathFlag.OBJECTIVE_CELL);

		return brutePath;
	}


	public List<Stack<CellNode>> FIND_ALL_PATHS(GridNode grid, CellNode startingPoint, CellNode objective, DistressLevel distressLevel){

		Stack<CellNode> path = new Stack<>();

		List<Stack<CellNode>> allPaths = new ArrayList<>();

		FIND_ALL_PATHS(grid, startingPoint, objective, path, allPaths, distressLevel);

		return allPaths;
	}


	public void FIND_ALL_PATHS(GridNode grid, CellNode current, CellNode objective, Stack<CellNode> path, List<Stack<CellNode>> allPaths, DistressLevel distressLevel) {

		List<CellNode> neighbors = grid.getNeighborCells(current, distressLevel);

	    for (CellNode neighbor : neighbors) {

	       if (neighbor.equals(objective)) {

	           Stack<CellNode> temp = new Stack<>();

	           for (CellNode node : path){
	               temp.add(node);
	           }

	           allPaths.add(temp);

	       } else if (!path.contains(neighbor)) {

	           path.push(neighbor);

	           FIND_ALL_PATHS(grid, neighbor, objective, path, allPaths, distressLevel);

	           path.pop();
	        }
	    }
	}

	public boolean QUICK_PATH_SEARCH(GridNode grid, CellNode startingPoint, CellNode objective) {

		PriorityQueue<CellNode> openCollection = new PriorityQueue<CellNode>((grid.getRowCount() * grid.getColumnCount()), new CellCostComparator());

		CellNode current = null;

		boolean containsNeighbor;

		grid.resetCells(true);

		startingPoint.setVisited(true);

		startingPoint.setMovementCost(0d);

		startingPoint.setTotalCost(startingPoint.getMovementCost() + heuristicCostEstimate(startingPoint, objective, 1, HeuristicType.MANHATHAN)); //The higher the scale the less the number of turn: scale from 1 to 2

		openCollection.add(startingPoint);

		while (!openCollection.isEmpty()) {

			current = openCollection.poll();

			if (current.equals(objective)) {

				return true;
			}

			current.setVisited(true);

			for (CellNode neighbor : grid.getNeighborCells(current, DistressLevel.LEVEL_THREE)) {

				if (neighbor == null) {
					continue;
				}

				if (neighbor.isVisited()){
					continue;
				}

				double potentialGScore = current.getMovementCost() + heuristicCostEstimate(current, neighbor, 1, HeuristicType.MANHATHAN); //The higher the scale the less the number of turn: scale from 1 to 2

				if (!(containsNeighbor = openCollection.contains(neighbor)) || Double.compare(potentialGScore, neighbor.getMovementCost()) < 0) {

					if (!containsNeighbor) {

					neighbor.setParentNode(current);

					neighbor.setMovementCost(potentialGScore);

					neighbor.setHeuristic(heuristicCostEstimate(neighbor, objective, 1, HeuristicType.MANHATHAN));

					neighbor.setTotalCost(neighbor.getMovementCost() + neighbor.getHeuristic());

					openCollection.add(neighbor);

					}
				}
			}
		}
		return false;
	}

	public LinkedList<PathCell> GET_ASTAR_LONGEST_HYBRID_PATH(PlayerTwo snakeAI, GridNode grid, CellNode startingPoint, CellNode objective) {

		PriorityQueue<CellNode> openCollection = new PriorityQueue<CellNode>((grid.getRowCount() * grid.getColumnCount()), new HybridCellComparator(PathType.LONGEST_PATH));

		CellNode current = null;

		int searchCount = 0;

		grid.resetCells(true);

		grid.resetDistances(INFINITY);

		grid.prepareDistances(objective);

		switch(snakeAI.getCurrentDirection()){
		case MOVE_DOWN:
			startingPoint.setDirection(Direction.DOWN);
			break;
		case MOVE_LEFT:
			startingPoint.setDirection(Direction.LEFT);
			break;
		case MOVE_RIGHT:
			startingPoint.setDirection(Direction.RIGHT);
			break;
		case MOVE_UP:
			startingPoint.setDirection(Direction.UP);
			break;
		case STANDING_STILL:
			startingPoint.setDirection(Direction.DOWN);
			break;
		}

		objective.setObjective(true);

		startingPoint.setVisited(true);

		startingPoint.setMovementCost(0d);

		startingPoint.setTotalCost(startingPoint.getMovementCost() + heuristicCostEstimate(startingPoint, objective, 1, HeuristicType.EUCLIDIAN)); //The higher the scale the less the number of turn: scale from 1 to 2

		openCollection.add(startingPoint);

		while (!openCollection.isEmpty()) {

			current = openCollection.poll();
			searchCount++;

			if (current.equals(objective)) {

				return createCoordinates(objective,searchCount);
			}

			current.setVisited(true);

			List<CellNode> neighbors = grid.getNeighborCells(current,DistressLevel.LEVEL_TWO);

			SORT_NODES(neighbors, SortingMethod.DISTANCE_SORT, SortingOrder.DESCENDING);

			for (CellNode neighbor : neighbors) {

				if (neighbor == null) {
					continue;
				}

				if (neighbor.isVisited()){
					continue;
				}

				double potentialGScore = current.getMovementCost() + heuristicCostEstimate(current, neighbor, 1, HeuristicType.EUCLIDIAN);

				neighbor.setParentNode(current);

				neighbor.setMovementCost(potentialGScore);

				neighbor.setHeuristic(heuristicCostEstimate(neighbor, objective, 1, HeuristicType.EUCLIDIAN));

				neighbor.setTotalCost(neighbor.getMovementCost() + neighbor.getHeuristic());

				openCollection.add(neighbor);
			}
		}
		return new LinkedList<>();
	}


	public LinkedList<PathCell> GET_LONGEST_PATH_POLY(PlayerTwo snakeAI, GridNode grid, CellNode startingPoint, CellNode objective, DistressLevel distressLevel) {

		if (startingPoint.equals(objective)) {
	        return null;
	    }

		Direction currentDirection = null;

		LinkedList<PathCell> longestPath = new LinkedList<>();

		switch(snakeAI.getCurrentDirection()){
		case MOVE_DOWN:
			currentDirection = Direction.DOWN;
			startingPoint.setDirection(Direction.DOWN);
			break;
		case MOVE_LEFT:
			currentDirection = Direction.LEFT;
			startingPoint.setDirection(Direction.LEFT);
			break;
		case MOVE_RIGHT:
			currentDirection = Direction.RIGHT;
			startingPoint.setDirection(Direction.RIGHT);
			break;
		case MOVE_UP:
			currentDirection = Direction.UP;
			startingPoint.setDirection(Direction.UP);
			break;
		case STANDING_STILL:
			currentDirection = Direction.NONE;
			startingPoint.setDirection(Direction.DOWN);
			break;
		}

		grid.resetCells(true);

		grid.resetDistances(INFINITY);

		objective.setObjective(true);

		GET_LONGEST_PATH_POLY(snakeAI, currentDirection, grid, startingPoint, startingPoint, objective, longestPath, distressLevel);

		if(!longestPath.isEmpty()){
			return longestPath;

		}else{
			return null;
		}
	}

	private void GET_LONGEST_PATH_POLY(PlayerTwo snakeAI, Direction direction, GridNode grid, CellNode current, CellNode startingPoint, CellNode objective, LinkedList<PathCell> path, DistressLevel distressLevel) {

		if(!path.isEmpty()){
			return;
		}

		current.setVisited(true);

		if (current.equals(objective)) {

			buildPath(CurrentGoal.OBJECTIVE, startingPoint, objective, path);
		}
		else {

			List<CellNode> neighbors = grid.getNeighborCells(current, distressLevel);

			if (neighbors.isEmpty()) {
				return;
			}

			for (CellNode neighbor : neighbors) {
				if (neighbor.getDistance() == INFINITY) {
					neighbor.setDistance(neighbor.getDistanceFrom(objective));
				}
			}

			SORT_NODES(neighbors, SortingMethod.DISTANCE_SORT, SortingOrder.DESCENDING);

			Double maxDistance = neighbors.get(0).getDistance();

			for (int i = 0; i < neighbors.size(); i++) {

				CellNode neighbor = neighbors.get(i);

				if (neighbor.getDistance() == maxDistance && direction == current.getDirectionTo(neighbor)) {

					Collections.swap(neighbors, 0, i);

					break;
				}
			}

			for (CellNode neighbor : neighbors) {

				if (!neighbor.isVisited()) {

					neighbor.setParentNode(current);

					GET_LONGEST_PATH_POLY(snakeAI, startingPoint.getDirectionTo(neighbor),grid, neighbor, startingPoint, objective, path, distressLevel);
				}
			}
		}
	}

	public LinkedList<PathCell> GET_ASTAR_PATH(PlayerTwo snakeAI, GridNode grid, CellNode startingPoint, CellNode objective, DistressLevel distressLevel) {

		PriorityQueue<CellNode> openCollection = new PriorityQueue<CellNode>((grid.getRowCount() * grid.getColumnCount()), new CellCostComparator());

		CellNode current = null;

		int searchCount = 0;

		double turnPenalty = 0;

		boolean containsNeighbor;

		grid.resetCells(true);

		switch(snakeAI.getCurrentDirection()){
		case MOVE_DOWN:
			startingPoint.setDirection(Direction.DOWN);
			break;
		case MOVE_LEFT:
			startingPoint.setDirection(Direction.LEFT);
			break;
		case MOVE_RIGHT:
			startingPoint.setDirection(Direction.RIGHT);
			break;
		case MOVE_UP:
			startingPoint.setDirection(Direction.UP);
			break;
		case STANDING_STILL:
			startingPoint.setDirection(Direction.DOWN);
			break;
		}

		objective.setObjective(true);

		startingPoint.setVisited(true);

		startingPoint.setMovementCost(0d);

		startingPoint.setTotalCost(startingPoint.getMovementCost() + heuristicCostEstimate(startingPoint, objective,heuristicScale,heuristicType)); //The higher the scale the less the number of turn: scale from 1 to 2

		openCollection.add(startingPoint);

		while (!openCollection.isEmpty()) {

			current = openCollection.poll();
			searchCount++;

			if (current.equals(objective)) {

				return createCoordinates(objective,searchCount);
			}

			current.setVisited(true);

			for (CellNode neighbor : grid.getNeighborCells(current,distressLevel)) {

				if (neighbor == null) {
					continue;
				}

				if (neighbor.isVisited()){
					continue;
				}

				double potentialGScore = current.getMovementCost() + heuristicCostEstimate(current, neighbor,heuristicScale,heuristicType); //The higher the scale the less the number of turn: scale from 1 to 2

				if (!(containsNeighbor = openCollection.contains(neighbor)) || Double.compare(potentialGScore, neighbor.getMovementCost()) < 0) {

					if (!containsNeighbor) {

					neighbor.setParentNode(current);

					neighbor.setMovementCost(potentialGScore);

					neighbor.setDistance(current.getDistance()+1);

					if (current.getParentNode() != null) {
						if (neighbor.getIndex().getRow() != current.getParentNode().getIndex().getRow()
						 || neighbor.getIndex().getCol() != current.getParentNode().getIndex().getCol()) {
							neighbor.setMovementCost(potentialGScore+turnPenalty);
						}
					}

					double heuristic = 0;

					heuristic = heuristicCostEstimate(neighbor, objective, 2, heuristicType);

					switch (tieBreaker) {
					case CROSS:

						double dx1 = neighbor.getLocation().getX() - objective.getLocation().getX();
						double dy1 = neighbor.getLocation().getY() - objective.getLocation().getY();
						double dx2 = startingPoint.getLocation().getX() - objective.getLocation().getX();
						double dy2 = startingPoint.getLocation().getY() - objective.getLocation().getY();

						double cross = Math.abs((dx1 * dy2) - (dx2 * dy1));

						heuristic += cross * 0.001;
						heuristic *= heuristicScale;
						break;
					case PATH:

						double path = 10 / 1000;

						heuristic *= (1.0 + path);
						heuristic *= heuristicScale;
						break;
					case NONE:
						heuristic *= heuristicScale;
						break;
					}

					neighbor.setHeuristic(heuristic); // If used with scaled up heuristic it gives least number of turns!

					neighbor.setTotalCost(neighbor.getMovementCost() + neighbor.getHeuristic());

					openCollection.add(neighbor);

					}
				}
			}
		}
		return new LinkedList<>();
	}

	/**
	 * Find a path from start to goal using the Breadth first search algorithm
	 */
	public LinkedList<PathCell> GET_BFS_PATH(PlayerTwo snakeAI, GridNode grid, CellNode startingPoint, CellNode objective, DistressLevel distressLevel) {

		Queue<CellNode> openCollection = new LinkedList<>();

		CellNode current = null;

		int searchCount = 0;

		boolean containsNeighbor;

		grid.resetCells(true);

		switch(snakeAI.getCurrentDirection()){

		case MOVE_DOWN:
			startingPoint.setDirection(Direction.DOWN);
			break;
		case MOVE_LEFT:
			startingPoint.setDirection(Direction.LEFT);
			break;
		case MOVE_RIGHT:
			startingPoint.setDirection(Direction.RIGHT);
			break;
		case MOVE_UP:
			startingPoint.setDirection(Direction.UP);
			break;
		case STANDING_STILL:
			startingPoint.setDirection(Direction.DOWN);
			break;
		}

		objective.setObjective(true);

		startingPoint.setVisited(true);

		openCollection.add(startingPoint);


	    while (!openCollection.isEmpty()) {

	    	current = openCollection.poll();

			searchCount++;

			if (current.equals(objective)) {
				return buildPath(CurrentGoal.OBJECTIVE, objective, searchCount);
			}

			current.setVisited(true);

			for (CellNode neighbor : grid.getNeighborCells(current,distressLevel)) {

				if (neighbor == null) {
					continue;
				}

				if (neighbor.isVisited()) {
					continue;
				}

				if (!(containsNeighbor = openCollection.contains(neighbor))) {

					if(!containsNeighbor){

						neighbor.setParentNode(current);

						neighbor.setDistance(current.getDistance()+1);

						openCollection.add(neighbor);
					}
				}
			}
		}
	    return new LinkedList<>();
	}

	/**
	 * Find a path from start to goal using the depth first search algorithm
	 */

	public LinkedList<PathCell> GET_DFS_PATH(PlayerTwo snakeAI, GridNode grid, CellNode startingPoint, CellNode objective, DistressLevel distressLevel) {

		Stack<CellNode> openCollection = new Stack<>();

		CellNode current = null;

		grid.resetCells(true);

		switch(snakeAI.getCurrentDirection()){

		case MOVE_DOWN:
			startingPoint.setDirection(Direction.DOWN);
			break;
		case MOVE_LEFT:
			startingPoint.setDirection(Direction.LEFT);
			break;
		case MOVE_RIGHT:
			startingPoint.setDirection(Direction.RIGHT);
			break;
		case MOVE_UP:
			startingPoint.setDirection(Direction.UP);
			break;
		case STANDING_STILL:
			startingPoint.setDirection(Direction.DOWN);
			break;
		}

//		objective.setObjective(true);

		startingPoint.setVisited(true);

		openCollection.push(startingPoint);

		while (!openCollection.isEmpty()) {

			current = openCollection.pop();

			if (current.equals(objective)) {

				return buildPath(CurrentGoal.OBJECTIVE, objective);
			}

			current.setVisited(true);

			for (CellNode neighbor : grid.getNeighborCells(current,distressLevel)) {

				if (neighbor == null) {
					continue;
				}

				if (neighbor.isVisited()) {
					continue;
				}

				neighbor.setParentNode(current);

				neighbor.setDistance(current.getDistance()+1);

				openCollection.push(neighbor);
			}
		}
		return new LinkedList<>() ;
	}

	public LinkedPath<PathCell> GET_SAFE_ASTAR_PATH(PlayerTwo snakeAI, GridNode grid, CellNode startingPoint, CellNode objective, CellNode tail, DistressLevel distressLevel) {

		LinkedPath<PathCell> safePath = new LinkedPath<>(ConnectionType.SAFE_PATH_CHECK);

		CellNode current = null;

		boolean containsNeighbor;

		double turnPenalty = 0;

		int searchCount = 0;

		int cellCount = grid.getRowCount() * grid.getColumnCount();

		PriorityQueue<CellNode> goalList = new PriorityQueue<CellNode>( cellCount, new CellCostComparator());

		PriorityQueue<CellNode> tailList = new PriorityQueue<CellNode>( cellCount, new CellCostComparator());

		grid.resetCells(true);

		switch(snakeAI.getCurrentDirection()){
		case MOVE_DOWN:
			startingPoint.setDirection(Direction.DOWN);
			break;
		case MOVE_LEFT:
			startingPoint.setDirection(Direction.LEFT);
			break;
		case MOVE_RIGHT:
			startingPoint.setDirection(Direction.RIGHT);
			break;
		case MOVE_UP:
			startingPoint.setDirection(Direction.UP);
			break;
		case STANDING_STILL:
			startingPoint.setDirection(Direction.DOWN);
			break;
		}

		containsNeighbor = false;

		current = null;

		searchCount = 0;

		objective.setObjective(true);

		startingPoint.setVisited(true);

		startingPoint.setMovementCost(0d);

		startingPoint.setTotalCost(startingPoint.getMovementCost() + heuristicCostEstimate(startingPoint, objective, heuristicScale, heuristicType));

		goalList.add(startingPoint);

		while( !goalList.isEmpty()) {

			current = goalList.poll();

			searchCount++;

			if( current == objective) {
				safePath.setPathOne(buildPath(CurrentGoal.OBJECTIVE, objective, searchCount));

				break;
			}

			current.setVisited(true);

			for( CellNode neighbor: grid.getNeighborCells(current, distressLevel)) {

				if( neighbor == null) {
					continue;
				}

				if( neighbor.isVisited()) {
					continue;
				}

				double potentialGScore = current.getMovementCost() + heuristicCostEstimate( current, neighbor, heuristicScale, heuristicType);

				if( !(containsNeighbor=goalList.contains( neighbor)) || Double.compare(potentialGScore, neighbor.getMovementCost()) < 0) {

					if( !containsNeighbor) {

					neighbor.setParentNode(current);

					neighbor.setMovementCost(potentialGScore);

					neighbor.setDistance(current.getDistance()+1);

					if (current.getParentNode() != null) {
						if (neighbor.getIndex().getRow() != current.getParentNode().getIndex().getRow()
						 || neighbor.getIndex().getCol() != current.getParentNode().getIndex().getCol()) {
							neighbor.setMovementCost(potentialGScore+turnPenalty);
						}
					}

					double heuristic = 0;

					heuristic = heuristicCostEstimate(neighbor, objective, 2, heuristicType);

					switch (tieBreaker) {
					case CROSS:

						double dx1 = neighbor.getLocation().getX() - objective.getLocation().getX();
						double dy1 = neighbor.getLocation().getY() - objective.getLocation().getY();
						double dx2 = startingPoint.getLocation().getX() - objective.getLocation().getX();
						double dy2 = startingPoint.getLocation().getY() - objective.getLocation().getY();

						double cross = Math.abs((dx1 * dy2) - (dx2 * dy1));

						heuristic += cross * 0.001;
						heuristic *= heuristicScale;
						break;
					case PATH:

						double path = 10 / 1000;

						heuristic *= (1.0 + path);
						heuristic *= heuristicScale;
						break;
					case NONE:
						heuristic *= heuristicScale;
						break;
					}

					neighbor.setHeuristic(heuristic); // If used with scaled up heuristic it gives least number of turns!

					neighbor.setTotalCost(neighbor.getMovementCost() + neighbor.getHeuristic());

					goalList.add(neighbor);

					}
				}
			}
		}

		containsNeighbor = false;

		current = null;

		turnPenalty = 0;

		searchCount = 0;

		grid.resetCellValues();

		objective.setMovementCost(0d);

		objective.setTotalCost(objective.getMovementCost() + heuristicCostEstimate(objective, tail, heuristicScale, heuristicType));

		tailList.add(objective);

		while(!tailList.isEmpty()) {

			current = tailList.poll();

			searchCount++;

			if( current == tail) {
				safePath.setPathTwo(buildPath(CurrentGoal.TAIL, tail, searchCount));
				break;
			}

			current.setVisited(true);

			for( CellNode neighbor: grid.getNeighborCells(current, DistressLevel.SAFETY_CHECK_GOAL)) {

				if( neighbor == null) {
					continue;
				}

				if( neighbor.isVisited()) {
					continue;
				}

				double potentialGScore = current.getMovementCost() + heuristicCostEstimate( current, neighbor, heuristicScale, heuristicType);

				if( !(containsNeighbor=tailList.contains(neighbor)) || Double.compare(potentialGScore, neighbor.getMovementCost()) < 0) {

					if( !containsNeighbor) {

					neighbor.setParentNode(current);

					neighbor.setMovementCost(potentialGScore);

					if (current.getParentNode() != null) {
						if (neighbor.getIndex().getRow() != current.getParentNode().getIndex().getRow()
						 || neighbor.getIndex().getCol() != current.getParentNode().getIndex().getCol()) {
							neighbor.setMovementCost(potentialGScore+turnPenalty);
						}
					}

					double heuristic = 0;

					heuristic = heuristicCostEstimate(neighbor, tail, 2, heuristicType);

					switch (tieBreaker) {
					case CROSS:

						double dx1 = neighbor.getLocation().getX() - tail.getLocation().getX();
						double dy1 = neighbor.getLocation().getY() - tail.getLocation().getY();
						double dx2 = objective.getLocation().getX() - tail.getLocation().getX();
						double dy2 = objective.getLocation().getY() - tail.getLocation().getY();

						double cross = Math.abs((dx1 * dy2) - (dx2 * dy1));

						heuristic += cross * 0.001;
						heuristic *= heuristicScale;
						break;
					case PATH:

						double path = 10 / 1000;

						heuristic *= (1.0 + path);
						heuristic *= heuristicScale;
						break;
					case NONE:
						heuristic *= heuristicScale;
						break;
					}

					neighbor.setHeuristic(heuristic); // If used with scaled up heuristic it gives least number of turns!

					neighbor.setTotalCost(neighbor.getMovementCost() + neighbor.getHeuristic());

					tailList.add( neighbor);
					}
				}
			}
		}
		return safePath;
	}
	private void buildPath(CurrentGoal goal, CellNode from, CellNode to, LinkedList<PathCell> path){
//		 CellNode tmp = to, parent;
////
////		 	path.add(0,tmp);
////		 	tmp.pathToGoal(true);
//
//		    while (tmp != from) {
//		        parent = tmp.getParentNode();
//		        path.add(0,parent);
//		        parent.pathToGoal(true);
//		        tmp = parent;
//		    }


		path.add(0,new PathCell(to.getIndex()));
		to.pathToGoal(true);

		while((to = to.getParentNode()) != null) {

			path.add(0,new PathCell(to.getIndex()));
			to.pathToGoal(true);
		}
//		    path.add(0,from);
	}

	private LinkedList<PathCell> buildPath(CurrentGoal goal, CellNode current) {
		LinkedList<PathCell> path = new LinkedList<>();

		switch(goal){
		case OBJECTIVE:
			path.add(new PathCell(current.getIndex()));
			current.pathToGoal(true);
			current.setPathCell(true);
			while(true) {
				if(current.getParentNode()!=null){
					current = current.getParentNode();
					path.add(new PathCell(current.getIndex()));
					current.pathToGoal(true);
					current.setPathCell(true);
				}else{
					break;
				}
			}
			break;
		case TAIL:
			path.add(new PathCell(current.getIndex()));
			current.pathToTail(true);

			while(true) {
				if(current.getParentNode()!=null){
					current = current.getParentNode();
					path.add(new PathCell(current.getIndex()));
					current.pathToTail(true);
				}else{
					break;
				}
			}
			break;
		}
		for (int i = path.size()-2; i>=0; i--) {
			PathCell parentNode = path.get(i+1);
			PathCell currentNode = path.get(i);
			if (currentNode.getIndex().getRow() > parentNode.getIndex().getRow()) {
				parentNode.setDirection(Direction.RIGHT);
			} else if (currentNode.getIndex().getRow() < parentNode.getIndex().getRow()) {
				parentNode.setDirection(Direction.LEFT);
			} else if (currentNode.getIndex().getCol() > parentNode.getIndex().getCol()) {
				parentNode.setDirection(Direction.DOWN);
			} else if (currentNode.getIndex().getCol() < parentNode.getIndex().getCol()) {
				parentNode.setDirection(Direction.UP);
			}
		}
		return path;
	}
	private LinkedList<PathCell> buildPath(CurrentGoal goal, CellNode current, int searchCount) {
		LinkedList<PathCell> path = new LinkedList<>();

		switch(goal){
		case OBJECTIVE:
			path.add(new PathCell(current.getIndex()));
			current.pathToGoal(true);
			current.setPathCell(true);
			while(true) {
				if(current.getParentNode()!=null){
					current = current.getParentNode();
					path.add(new PathCell(current.getIndex()));
					current.pathToGoal(true);
					current.setPathCell(true);
				}else{
					break;
				}
			}
			break;
		case TAIL:
			path.add(new PathCell(current.getIndex()));
			current.pathToTail(true);

			while(true) {
				if(current.getParentNode()!=null){
					current = current.getParentNode();
					path.add(new PathCell(current.getIndex()));
					current.pathToTail(true);
				}else{
					break;
				}
			}
			break;
		}
		for (int i = path.size()-2; i>=0; i--) {
			PathCell parentNode = path.get(i+1);
			PathCell currentNode = path.get(i);
			if (currentNode.getIndex().getRow() > parentNode.getIndex().getRow()) {
				parentNode.setDirection(Direction.RIGHT);
			} else if (currentNode.getIndex().getRow() < parentNode.getIndex().getRow()) {
				parentNode.setDirection(Direction.LEFT);
			} else if (currentNode.getIndex().getCol() > parentNode.getIndex().getCol()) {
				parentNode.setDirection(Direction.DOWN);
			} else if (currentNode.getIndex().getCol() < parentNode.getIndex().getCol()) {
				parentNode.setDirection(Direction.UP);
			}
		}
		return path;
	}

	private LinkedList<PathCell> createCoordinates(CellNode current, int searchCount) {

		LinkedList<PathCell> path = new LinkedList<>();

		int pathLength = 0;

		path.add(new PathCell(current.getIndex()));
		current.pathToGoal(true);
		current.setPathCell(true);

		while (true) {

			pathLength++;

			if(current.getParentNode() != null){
				current = current.getParentNode();
				path.add(new PathCell(current.getIndex()));
				current.pathToGoal(true);
				current.setPathCell(true);

				if(pathLength>=searchCount){
					break;
				}
			}
			else{
				break;
			}
		}

		for (int i = path.size()-2; i>=0; i--) {
			PathCell parentNode = path.get(i+1);
			PathCell currentNode = path.get(i);
			if (currentNode.getIndex().getRow() > parentNode.getIndex().getRow()) {
				parentNode.setDirection(Direction.RIGHT);
			} else if (currentNode.getIndex().getRow() < parentNode.getIndex().getRow()) {
				parentNode.setDirection(Direction.LEFT);
			} else if (currentNode.getIndex().getCol() > parentNode.getIndex().getCol()) {
				parentNode.setDirection(Direction.DOWN);
			} else if (currentNode.getIndex().getCol() < parentNode.getIndex().getCol()) {
				parentNode.setDirection(Direction.UP);
			}
		}
		return path;
	}

	public double calculateDistance(double fromX, double toX, double fromY, double toY) {
		return Math.hypot(fromX - toX, fromY - toY);
	}

	public static double calculateManhathanDistance(double fromX, double toX, double fromY, double toY) {
		return Math.abs(fromX - toX) + Math.abs(fromY - toY);
	}

	public double calculateEuclidianDistance(double fromX, double toX, double fromY, double toY) {
		return Math.sqrt((fromX - toX) * (fromX - toX) + (fromY - toY) * (fromY - toY));
	}

	public double getCrossPolarDistance(CellNode start, CellNode closestRelativeCell, CellNode closestPolarCell, CellNode objective){
		double distanceOne = calculateManhathanDistance(start.getLocation().getX(),start.getLocation().getY(),closestRelativeCell.getLocation().getX(),closestRelativeCell.getLocation().getY());
		double distanceTwo = calculateManhathanDistance(closestPolarCell.getLocation().getX(),closestPolarCell.getLocation().getY(), objective.getLocation().getX(),objective.getLocation().getY());;
		return (distanceOne+distanceTwo);
	}

	@SuppressWarnings("unused")
	private double getHeuristicCost(CellNode start, CellNode end, Double scale) {
		double dx = Math.abs(start.getLocation().getX() - end.getLocation().getX());
		double dy = Math.abs(start.getLocation().getY() - end.getLocation().getY());
		return scale * (dx + dy);
	}

	private double heuristicCostEstimate(CellNode start, CellNode end, int scale, HeuristicType cost) {
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

	public void setPathType(PathType pathType) {
		this.pathType = pathType;
	}

	public enum CurrentGoal{
		OBJECTIVE, TAIL,
	}

	public enum SearchType{
		CLOSEST_OBJECTIVE, SHORTEST_PATH;
	}

	public enum PathType{
		LONGEST_PATH, SHORTEST_PATH
	}

	public enum TieBreaker{
		PATH,CROSS, NONE
	}

	public enum HeuristicType{
		MANHATHAN, EUCLIDIAN, CUSTOM_EUCLUDIAN,
	}

	public enum BorderPole{
		NORTH, SOUTH, WEST, EAST
	}

	public enum SortingMethod{
		DISTANCE_SORT, COST_SORT
	}

	public enum SortingOrder{
		DESCENDING, ASCENDING
	}

	public void SORT_NODES(List<CellNode> nodes, SortingMethod method, SortingOrder order){
		switch(method){
		case COST_SORT:
			Collections.sort(nodes, new Comparator<CellNode>(){
				@Override
				public int compare(CellNode a, CellNode b) {
					switch(order){
					case DESCENDING:
						return Double.compare(b.getTotalCost(),a.getTotalCost());
					default:
						return Double.compare(a.getTotalCost(),b.getTotalCost());
					}
				}

			});
			break;
		case DISTANCE_SORT:
			Collections.sort(nodes, new Comparator<CellNode>(){
				@Override
				public int compare(CellNode a, CellNode b) {
					switch(order){
					case DESCENDING:
						return Double.compare(b.getDistance(),a.getDistance());
					default:
						return Double.compare(a.getDistance(),b.getDistance());
					}
				}

			});
			break;
		}
	}

	public class HybridCellComparator implements Comparator<CellNode> {
		private PathType type;

		public HybridCellComparator(PathType pathType){
			this.type = pathType;
		}

		@Override
		public int compare(CellNode a, CellNode b) {
			if(type == PathType.SHORTEST_PATH){
				return Double.compare(a.getTotalCost(), b.getTotalCost());
			}
			else{
				return Double.compare(b.getTotalCost(), a.getTotalCost());
			}
		}
	}

	public class CellCostComparator implements Comparator<CellNode> {
		@Override
		public int compare(CellNode a, CellNode b) {
			if(pathType == PathType.SHORTEST_PATH){
				return Double.compare(a.getTotalCost(), b.getTotalCost());
			}
			else{
				return Double.compare(b.getTotalCost(), a.getTotalCost());
			}
		}
	}

	public class CellDistanceComparator implements Comparator<CellNode> {
		@Override
		public int compare(CellNode a, CellNode b) {
			return Double.compare(b.getDistance(), a.getDistance());
		}
	}
}
