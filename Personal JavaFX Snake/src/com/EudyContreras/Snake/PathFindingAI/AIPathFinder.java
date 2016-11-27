package com.EudyContreras.Snake.PathFindingAI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.PathFindingAI.CellNode.Direction;
import com.EudyContreras.Snake.PathFindingAI.LinkedPath.ConnectionType;
import com.EudyContreras.Snake.PathFindingAI.PathAlgorithms.PathType;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.geometry.Rectangle2D;

/**
 *
 * @author Eudy Contreras
 * TODO: Trigger PATH finding AI only when a collision happens !!!
 *
 * Make it so that if the snake is close to an obstacle it will activate
 * path finding else it will deactivate it!
 *
 * TODO: Make the snake go for the closest only if the path is available
 * if the path is not available use a hierarchy system. If the path is close
 * check if the closest border is opened and if open check if the farthest border
 * is open! if both borders are open teleport!
 *
 * TODO: Do not allow tracking apples which are next to the snakes body. if the closest
 * apple is one cell away from snakes body. Check next closest!
 */
public class AIPathFinder {

	private AIController controller;
	private GameManager game;
	private PlayerTwo snakeAI;
	private Random rand;

	private boolean logDirections = false;
	private boolean teleporting = false;
	private boolean safetyCheck = false;
	private boolean onPath = false;

	private double checkTimer = 100;
	private double heuristicScale = 2;

	private int cellCount = 0;
	private int safetyCheckTimer = 0;
	private int randomBoost = 200;

	private DistressLevel distressLevel;
	private HeuristicType heuristicType;
	private CurrentGoal currentGoal;
	private PathType pathType;
	private TieBreaker tieBreaker;
	private ActionType state;
	private Direction lastStep;

	private LinkedPath<CellNode> pathCoordinates;


	public AIPathFinder(GameManager game, PlayerTwo snakeAI) {
		this.game = game;
		this.snakeAI = snakeAI;
		this.initialize();
	}

	public AIPathFinder(GameManager game, AIController controller, PlayerTwo snakeAI, LinkedList<CollideNode> possibleColliders) {
		this.game = game;
		this.controller = controller;
		this.snakeAI = snakeAI;
		this.initialize();
	}

	public void initialize() {
		rand = new Random();
		cellCount = controller.getGrid().getRowCount() * controller.getGrid().getColumnCount();
		currentGoal = CurrentGoal.OBJECTIVE;
		heuristicType = HeuristicType.MANHATHAN;
		distressLevel = DistressLevel.LEVEL_TWO;
		tieBreaker = TieBreaker.NONE;
		pathType = PathType.SHORTEST_PATH;
		state = ActionType.FIND_PATH;
		lastStep = Direction.NONE;
	}

	public void findObjective() {
		switch (state) {
		case DODGE_OBSTACLES:
			break;
		case FIND_PATH:
			break;
		case FREE_MODE:
			if (game.getModeID() == GameModeID.LocalMultiplayer && GameSettings.ALLOW_AI_CONTROLL)
				createPath();
			break;
		default:
			break;

		}
	}

	/*
	 * Gets called when the game begins in order to initiate the simulation
	 */
	public void startSimulation() {
		if (game.getModeID() == GameModeID.LocalMultiplayer && GameSettings.ALLOW_AI_CONTROLL){
			snakeAI.setDirectCoordinates(PlayerMovement.MOVE_DOWN);
			computePath();
		}
	}

	/*
	 * this method gets called from the game loop and it is called at 60fps. The
	 * method update and keeps track of things
	 */
	public void updateSimulation() {
		if (game.getModeID() == GameModeID.LocalMultiplayer) {
			if (game.getStateID() == GameStateID.GAMEPLAY) {

				performLocationBasedAction();
				addRandomBoost(true);

				checkTimer --;

				if(!onPath){
					if(checkTimer<=0){
						computePath();
						if(currentGoal != CurrentGoal.TAIL){
							checkTimer = 50;
							if(safetyCheck == true){
								safetyCheck = false;
							}
						}else{
							checkTimer = 50;
							if(safetyCheck == false){
								safetyCheck = true;
								safetyCheckTimer = 0;
							}
						}
					}
				}


				if(safetyCheck && currentGoal == CurrentGoal.TAIL){
					safetyCheckTimer++;

					if(safetyCheckTimer>=1000){
						safetyCheckTimer = 0;
						currentGoal = CurrentGoal.OBJECTIVE;
					}
				}
			}
		}
	}

	/**
	 * Find a path from start to goal using the A* algorithm
	 */
	public List<CellNode> GET_ASTAR_PATH(GridNode grid, CellNode startingPoint, CellNode objective) {
		return GET_ASTAR_PATH(grid,startingPoint,objective,distressLevel);
	}

	public List<CellNode> GET_ASTAR_PATH(GridNode grid, CellNode startingPoint, CellNode objective, DistressLevel distressLevel) {

		PriorityQueue<CellNode> openCollection = new PriorityQueue<CellNode>(cellCount, new CellComparator());

		CellNode current = null;

		int searchCount = 0;

		double turnPenalty = 0;

		boolean containsNeighbor;

		grid.resetCells(true);

		openCollection.add(startingPoint);

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

		startingPoint.setMovementCost(0d);

		startingPoint.setTotalCost(startingPoint.getMovementCost() + heuristicCostEstimate(startingPoint, objective,heuristicScale,heuristicType)); //The higher the scale the less the number of turn: scale from 1 to 2

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

					current.setChildNode(neighbor);

					neighbor.setMovementCost(potentialGScore);

					neighbor.setDistance(current.getDistance()+1);

					if (current.getParentNode() != null) {
						if (neighbor.getIndex().getRow() != current.getParentNode().getIndex().getRow()
						 || neighbor.getIndex().getCol() != current.getParentNode().getIndex().getCol()) {
							neighbor.setMovementCost(potentialGScore+turnPenalty);
						}
					}

					double heuristic = 0;

					double path = 10 / 1000;
					double dx1 = neighbor.getLocation().getX() - objective.getLocation().getX();
					double dy1 = neighbor.getLocation().getY() - objective.getLocation().getY();
					double dx2 = startingPoint.getLocation().getX() - objective.getLocation().getX();
					double dy2 = startingPoint.getLocation().getY() - objective.getLocation().getY();

					double cross = Math.abs((dx1 * dy2) - (dx2 * dy1));

					heuristic = heuristicCostEstimate(neighbor, objective,2.0,heuristicType);

					switch (tieBreaker) {
					case CROSS:
						heuristic += cross * 0.001;
						heuristic *= heuristicScale;
						break;
					case PATH:
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

	private List<CellNode> GET_BFS_PATH(GridNode grid, CellNode startingPoint, CellNode objective) {

		Queue<CellNode> openCollection = new LinkedList<>();

		CellNode current = null;

		int searchCount = 0;

		boolean containsNeighbor;

		grid.resetCells(true);

		openCollection.add(startingPoint);

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
					neighbor.setParentNode(current);
					neighbor.setDistance(current.getDistance()+1);
					openCollection.add(neighbor);
				}
			}
		}
	    return new ArrayList<>();
	}

	/**
	 * Find a path from start to goal using the depth first search algorithm
	 */

	public List<CellNode> GET_DFS_PATH(GridNode grid, CellNode startingPoint, CellNode objective) {

		Stack<CellNode> openCollection = new Stack<CellNode>();

		CellNode current = null;

		int searchCount = 0;

		boolean containsNeighbor;

		grid.resetCells(true);

		openCollection.push(startingPoint);

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

		while (!openCollection.isEmpty()) {

			current = openCollection.pop();

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

					neighbor.setParentNode(current);

					neighbor.setDistance(current.getDistance()+1);

					if (!containsNeighbor) {

						openCollection.add(neighbor);
						neighbor.setVisited(true);
					}
				}
			}
		}

		return new ArrayList<>();
	}

	public LinkedPath<CellNode> GET_SAFE_ASTAR_PATH(GridNode grid, CellNode startingPoint, CellNode objective, CellNode tail) {

		LinkedPath<CellNode> safePath = new LinkedPath<CellNode>(ConnectionType.SAFE_PATH_CHECK);

		CellNode current = null;

		boolean containsNeighbor;

		double turnPenalty = 0;

		int searchCount = 0;

		int cellCount = grid.getRowCount() * grid.getColumnCount();

		PriorityQueue<CellNode> goalList = new PriorityQueue<CellNode>( cellCount, new CellComparator());

		PriorityQueue<CellNode> tailList = new PriorityQueue<CellNode>( cellCount, new CellComparator());

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

		goalList.add(startingPoint);

		containsNeighbor = false;

		current = null;

		searchCount = 0;

		objective.setObjective(true);

		startingPoint.setMovementCost(0d);

		startingPoint.setTotalCost(startingPoint.getMovementCost() + heuristicCostEstimate(startingPoint, objective, heuristicScale, heuristicType));

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

					current.setChildNode(neighbor);

					neighbor.setMovementCost(potentialGScore);

					neighbor.setDistance(current.getDistance()+1);

					if (current.getParentNode() != null) {
						if (neighbor.getIndex().getRow() != current.getParentNode().getIndex().getRow()
						 || neighbor.getIndex().getCol() != current.getParentNode().getIndex().getCol()) {
							neighbor.setMovementCost(potentialGScore+turnPenalty);
						}
					}

					double heuristic = 0;

					double path = 10 / 1000;
					double dx1 = neighbor.getLocation().getX() - objective.getLocation().getX();
					double dy1 = neighbor.getLocation().getY() - objective.getLocation().getY();
					double dx2 = startingPoint.getLocation().getX() - objective.getLocation().getX();
					double dy2 = startingPoint.getLocation().getY() - objective.getLocation().getY();

					double cross = Math.abs((dx1 * dy2) - (dx2 * dy1));

					heuristic = heuristicCostEstimate(neighbor, objective,2.0,heuristicType);

					switch (tieBreaker) {
					case CROSS:
						heuristic += cross * 0.001;
						heuristic *= heuristicScale;
						break;
					case PATH:
						heuristic *= (1.0 + path);
						heuristic *= heuristicScale;
						break;
					case NONE:
						heuristic *= heuristicScale;
						break;
					}

					neighbor.setHeuristic(heuristic); // If used with scaled up heuristic it gives least number of turns!

					neighbor.setTotalCost(neighbor.getMovementCost() + neighbor.getHeuristic());


					goalList.add( neighbor);
					}
				}
			}
		}

		tailList.add(objective);

		containsNeighbor = false;

		current = null;

		turnPenalty = 0;

		searchCount = 0;

		grid.resetCellValues();

		objective.setMovementCost(0d);

		objective.setTotalCost(objective.getMovementCost() + heuristicCostEstimate(objective, tail, heuristicScale, heuristicType));

		while(!tailList.isEmpty()) {

			current = tailList.poll();

			searchCount++;

			if( current == tail) {
				safePath.setPathTwo(buildPath(CurrentGoal.TAIL, tail, searchCount));
				break;
			}

			current.setVisited(true);

			for( CellNode neighbor: grid.getNeighborCells(current, DistressLevel.SAFETY_CHECK)) {

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

					current.setChildNode(neighbor);

					neighbor.setMovementCost(potentialGScore);

					if (current.getParentNode() != null) {
						if (neighbor.getIndex().getRow() != current.getParentNode().getIndex().getRow()
						 || neighbor.getIndex().getCol() != current.getParentNode().getIndex().getCol()) {
							neighbor.setMovementCost(potentialGScore+turnPenalty);
						}
					}

					double heuristic = 0;

					double path = 10 / 1000;
					double dx1 = neighbor.getLocation().getX() - tail.getLocation().getX();
					double dy1 = neighbor.getLocation().getY() - tail.getLocation().getY();
					double dx2 = objective.getLocation().getX() - tail.getLocation().getX();
					double dy2 = objective.getLocation().getY() - tail.getLocation().getY();

					double cross = Math.abs((dx1 * dy2) - (dx2 * dy1));

					heuristic = heuristicCostEstimate(neighbor, tail,2.0,heuristicType);

					switch (tieBreaker) {
					case CROSS:
						heuristic += cross * 0.001;
						heuristic *= heuristicScale;
						break;
					case PATH:
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

	private List<CellNode> buildPath(CurrentGoal goal, CellNode current, int searchCount) {

		List<CellNode> totalPath = new LinkedList<>();

		switch(goal){
		case OBJECTIVE:
			totalPath.add(current);
			current.pathToGoal(true);

			while((current = current.getParentNode()) != null) {

				totalPath.add(current);
				current.pathToGoal(true);
			}

			break;
		case TAIL:
			totalPath.add(current);
			current.pathToTail(true);

			while((current = current.getParentNode()) != null) {
				totalPath.add(current);
				current.pathToTail(true);
			}

			break;

		}

		return totalPath;
	}

	/**
	 * Create final path of the A* algorithm. The path is from goal to start.
	 * TODO: Find out the reason why the loop will never break sometimes.
	 */
	private List<CellNode> createCoordinates(CellNode current, int searchCount) {

		List<CellNode> totalPath = new LinkedList<CellNode>();

		boolean createPath = true;

		int pathLength = 0;

		totalPath.add(current);
		current.pathToGoal(true);

		while (createPath) {

			pathLength++;

			if(current.getParentNode() != null){
				current = current.getParentNode();

				totalPath.add(current);
				current.pathToGoal(true);

				if(pathLength>=searchCount){
					createPath = false;
					log("path lenght exceded");
				}
			}
			else{
				createPath = false;
			}
		}

//		for (CellNode node : totalPath) {
//			if (node.getParentNode() != null) {
//				if (node.getIndex().getRow() > node.getParentNode().getIndex().getRow()) {
//					node.getParentNode().setDirection(Direction.RIGHT);
//				} else if (node.getIndex().getRow() < node.getParentNode().getIndex().getRow()) {
//					node.getParentNode().setDirection(Direction.LEFT);
//				} else if (node.getIndex().getCol() > node.getParentNode().getIndex().getCol()) {
//					node.getParentNode().setDirection(Direction.DOWN);
//				} else if (node.getIndex().getCol() < node.getParentNode().getIndex().getCol()) {
//					node.getParentNode().setDirection(Direction.UP);
//				}
//			}
//		}
		return totalPath;
	}

	@SuppressWarnings("unused")
	private void findClosestObjective(Objective[] objectives){
		PriorityQueue<Distance>  distances = new PriorityQueue<Distance>(4, new DistanceComparator());

		for (int i = 0; i < getObjectiveCount(); i++) {
			distances.add(new Distance(calculateManhathanDistance(
					snakeAI.getX(), game.getGameObjectController().getObsFruitList().get(i).getX(),
					snakeAI.getY(), game.getGameObjectController().getObsFruitList().get(i).getY()),
					game.getGameObjectController().getObsFruitList().get(i)));
		}

		if(distances.poll().getObject().getNumericCode()!=objectives[0].getObject().getNumericCode()){
			computePath();
		}
	}

	public synchronized void computePath(){
		controller.setHasBeenNotified(true);
		teleporting = false;

		LinkedList<Objective> newObjectives = new LinkedList<>();
		PriorityQueue<LinkedPath<CellNode>>  paths = new PriorityQueue<LinkedPath<CellNode>>(getObjectiveCount(), new PathLengthComparator());

		CellNode start = controller.getHeadCell(snakeAI, 0, 0);
		CellNode tail = null;
		CellNode goal = null;

		LinkedPath<CellNode> path = null;

		switch(currentGoal){
		case OBJECTIVE:
//			for (int i = 0; i<getObjectiveCount(); i++){
//				AbstractObject object = game.getGameObjectController().getObsFruitList().get(i);
//				Objective objective = new Objective(snakeAI, object);
//				newObjectives.add(objective);
//			}
//
//			Collections.sort(newObjectives);

			pathType = PathType.SHORTEST_PATH;

			for (int i = 0; i < getObjectiveCount(); i++) {
				AbstractObject object = game.getGameObjectController().getObsFruitList().get(i);
				Objective objective = new Objective(snakeAI, object);
				paths.add(new LinkedPath<CellNode>(GET_ASTAR_PATH(controller.getGrid(), start, objective.getCell()),new ArrayList<>(), objective));
			}

			while (paths.peek() != null) {
				newObjectives.add(paths.poll().getObjective());
			}

			if (newObjectives.size() > 0) {
				if (newObjectives.get(0) != null && GameSettings.SHOW_ASTAR_GRAPH) {
					newObjectives.get(0).getObject().blowUpAlt();
				}

				if (start != null) {
					if (!start.isDangerZone()) {
						distressLevel = DistressLevel.LEVEL_TWO;
					}

					for(int i = 0; i < newObjectives.size(); i++){
						path = checkObjectiveReach(start, newObjectives.get(i), i, newObjectives);

						if(path!=null){
							break;
						}
					}

					if(path == null){
						distressLevel = DistressLevel.LEVEL_THREE;

						for(int i = 0; i < newObjectives.size(); i++){
							path = checkObjectiveReach(start, newObjectives.get(i), i, newObjectives);

							if(path!=null){
								break;
							}
						}
					}

					if(path!=null){
						log("path is safe!!");
						showPathToObjective(path);
					}else{
						currentGoal = CurrentGoal.TAIL;
						log("path is not safe!!");
						computePath();
					}
				}
			}

			break;
		case TAIL:
			pathType = PathType.LONGEST_PATH;
			tail = controller.getGrid().getTailCell(snakeAI);

			if (tail != null) {
				if (!start.isDangerZone()) {
					distressLevel = DistressLevel.LEVEL_TWO;
					path = new LinkedPath<CellNode>(GET_DFS_PATH(controller.getGrid(), start, tail), new ArrayList<>());
				}
				if (!path.getPathOne().isEmpty()) {
					showPathToObjective(path);
				} else {

					distressLevel = DistressLevel.LEVEL_THREE;
					path = new LinkedPath<CellNode>(GET_DFS_PATH(controller.getGrid(), start, tail), new ArrayList<>());

					if (!path.getPathOne().isEmpty()) {

						showPathToObjective(path);
					} else {
						log("Emergency path to tail empty!");

						distressLevel = DistressLevel.LEVEL_THREE;
						path = emergencyTeleport(controller.getGrid(), start, tail);

						if (!path.getPathOne().isEmpty()) {

							showPathToObjective(path);

						} else {
							// TODO: Stall until path is found or die!
						}
					}
				}
			}break;
		}
	}

	private boolean pathChecker(){

		LinkedList<Objective> newObjectives = new LinkedList<>();
		PriorityQueue<LinkedPath<CellNode>>  paths = new PriorityQueue<LinkedPath<CellNode>>(getObjectiveCount(), new PathLengthComparator());

		CellNode start = controller.getHeadCell(snakeAI, 0, 0);

		LinkedPath<CellNode> path = new LinkedPath<CellNode>();

		pathType = PathType.SHORTEST_PATH;

		for (int i = 0; i < getObjectiveCount(); i++) {
			AbstractObject object = game.getGameObjectController().getObsFruitList().get(i);
			Objective objective = new Objective(snakeAI, object);
			paths.add(new LinkedPath<CellNode>(GET_ASTAR_PATH(controller.getGrid(), start, objective.getCell()),new ArrayList<>(), objective));
		}

		while (paths.peek() != null) {
			newObjectives.add(paths.poll().getObjective());
		}

		if (newObjectives.size() > 0) {

			if (start != null) {
				distressLevel = DistressLevel.LEVEL_THREE;

				for(int i = 0; i < newObjectives.size(); i++){
					path = new LinkedPath<CellNode>(GET_DFS_PATH(controller.getGrid(), start, newObjectives.get(i).getCell()), new ArrayList<>());
					if(!path.isPathOneEmpty()){
						return true;
					}
				}
			}
		}
		return false;
	}

	public LinkedPath<CellNode> checkObjectiveReach(CellNode start, Objective objective, int index, LinkedList<Objective> objectives){
		if(start!=null){

			CellNode tail = controller.getGrid().getTailCell(snakeAI);
//
//			if((objectives.get(index).getDistance()) > objectives.get(objectives.size()-1).getInterpolarDistance(start.getLocation().getX(),start.getLocation().getY())){
//				LinkedPath<CellNode> path = computeInterpolarDirection(controller.getGrid(),start,objective,tail,objectives);
//				if(path!=null){
//					return path;
//				}
//			}
//			else if((objectives.get(index).getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.4) && objectives.get(index).getYDistance(start.getLocation().getY())<GameSettings.HEIGHT*.4){
//				LinkedPath<CellNode> path = computeInterpolarDirection(controller.getGrid(),start,objective,tail,objectives);
//				if(path!=null){
//					return path;
//				}
//			}
//			else if(objectives.get(index).getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.5 && objectives.get(index).getXDistance(start.getLocation().getX())<GameSettings.WIDTH*.5){
//				LinkedPath<CellNode> path = computeInterpolarDirection(controller.getGrid(),start,objective,tail,objectives);
//				if(path!=null){
//					return path;
//				}
//			}
//			else if(objectives.get(index).getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.5 && objectives.get(index).getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.5){
//				LinkedPath<CellNode> path = computeInterpolarDirection(controller.getGrid(),start,objective,tail,objectives);
//				if(path!=null){
//					return path;
//				}
//			}
//			//TODO: Find additional conditions that may qualify for interpolation
//
//			else{
				if(start!=null && objective!=null){

					LinkedPath<CellNode> path = GET_SAFE_ASTAR_PATH(controller.getGrid(), start, objective.getCell(),tail);

					if(path.isPathSafe()){
						return path;
					}
				}
			}
//		}
		return null;
	}
	/*
	 * Unsafe amd unchecked teleport method which triggers a teleportation when a the distance
	 * between the snake and the objective is above a certain threshold. The calculations are made
	 * based on relational distance planes.
	 */

	private LinkedPath<CellNode> computeInterpolarDirection(GridNode grid, CellNode start, Objective objective, CellNode tail, LinkedList<Objective> objectives) {

		if((objective.getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.45) && objective.getYDistance(start.getLocation().getY())<GameSettings.HEIGHT*.45){

			if(start.getLocation().getX() > GameSettings.WIDTH*.65){
				return getPortal(grid, start, tail, objective, objectives, CardinalPoint.EAST);
			}
			else if(start.getLocation().getX() < GameSettings.WIDTH*.35){
				return getPortal(grid, start, tail, objective, objectives, CardinalPoint.WEST);
			}
		}
		else if(objective.getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.45  && objective.getXDistance(start.getLocation().getX())<GameSettings.WIDTH*.45){

			if(start.getLocation().getY() < GameSettings.HEIGHT*.35){
				return getPortal(grid, start, tail, objective, objectives, CardinalPoint.NORTH);
			}
			else if(start.getLocation().getY() > GameSettings.HEIGHT*.65){
				return getPortal(grid, start, tail, objective, objectives, CardinalPoint.SOUTH);
			}
		}
//		else if(objective.getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.45 && objective.getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.45){
//
//		}
		return null;
	}


	public LinkedPath<CellNode> getPortal(GridNode grid, CellNode start, CellNode tail, Objective objective, LinkedList<Objective> objectives, CardinalPoint orientation){

		LinkedPath<CellNode> interpolarPath = null;

		Portal2D portal = new Portal2D();

		List<CellNode> enterPath;

		switch(orientation){
		case EAST:
			enterPath = getSafeEnterPath(grid, portal, start,orientation);
			if(enterPath!=null){
				CellNode portalOut = grid.getCell(portal.getPortalInIndex().getRow(),grid.getMinCol());
				if(isPortalSafe(grid, portalOut, 2, CardinalPoint.WEST)){
					interpolarPath = createCrosspolarPath(grid, enterPath, portalOut, start, objectives);
					if(interpolarPath != null){
						lastStep = Direction.RIGHT;
						teleporting = true;
						return interpolarPath;
					}else{
						interpolarPath = getSafeBorderPath(grid, start, objectives, orientation);
						if(interpolarPath!=null){
							return interpolarPath;
						}
					}
				}
			}
			else{
				interpolarPath = getSafeBorderPath(grid, start, objectives, orientation);
				if(interpolarPath!=null){
					return interpolarPath;
				}
			}
			break;
		case WEST:
			enterPath = getSafeEnterPath(grid, portal, start,orientation);
			if(enterPath!=null){
				CellNode portalOut = grid.getCell(portal.getPortalInIndex().getRow(),grid.getColumnCount()-1);
				if(isPortalSafe(grid, portalOut, 2, CardinalPoint.EAST)){
					interpolarPath = createCrosspolarPath(grid, enterPath, portalOut, start, objectives);
					if(interpolarPath != null){
						lastStep = Direction.LEFT;
						teleporting = true;
						return interpolarPath;
					}else{
						interpolarPath = getSafeBorderPath(grid, start, objectives, orientation);
						if(interpolarPath!=null){
							return interpolarPath;
						}
					}
				}
			}
			else{
				interpolarPath = getSafeBorderPath(grid, start, objectives, orientation);
				if(interpolarPath!=null){
					return interpolarPath;
				}
			}
			break;
		case NORTH:
			enterPath = getSafeEnterPath(grid, portal, start,orientation);
			if(enterPath!=null){
				CellNode portalOut = grid.getCell(grid.getRowCount()-1,portal.getPortalInIndex().getCol());
				if(isPortalSafe(grid, portalOut, 2, CardinalPoint.SOUTH)){
					interpolarPath = createCrosspolarPath(grid, enterPath, portalOut, start, objectives);
					if(interpolarPath != null){
						lastStep = Direction.UP;
						teleporting = true;
						return interpolarPath;
					}else{
						interpolarPath = getSafeBorderPath(grid, start, objectives, orientation);
						if(interpolarPath!=null){
							return interpolarPath;
						}
					}
				}
			}
			else{
				interpolarPath = getSafeBorderPath(grid, start, objectives, orientation);
				if(interpolarPath!=null){
					return interpolarPath;
				}
			}
			break;
		case SOUTH:
			enterPath = getSafeEnterPath(grid, portal, start,orientation);
			if(enterPath!=null){
				CellNode portalOut = grid.getCell(grid.getMinRow(),portal.getPortalInIndex().getCol());
				if(isPortalSafe(grid, portalOut, 2, CardinalPoint.NORTH)){
					interpolarPath = createCrosspolarPath(grid, enterPath, portalOut, start, objectives);
					if(interpolarPath != null){
						lastStep = Direction.DOWN;
						teleporting = true;
						return interpolarPath;
					}else{
						interpolarPath = getSafeBorderPath(grid, start, objectives, orientation);
						if(interpolarPath!=null){
							return interpolarPath;
						}
					}
				}
			}
			else{
				interpolarPath = getSafeBorderPath(grid, start, objectives, orientation);
				if(interpolarPath!=null){
					return interpolarPath;
				}
			}
			break;

		}
		return null;
	}

	public List<CellNode> getSafeEnterPath(GridNode grid, Portal2D portal2D, CellNode start, CardinalPoint orientation){
		int index = 0;
		int searchCount = 0;
		int cellIndex = 0;
		int [] indexes = {0,-1,1,-2,2,-3,3};

		switch(orientation){
		case EAST:
			while(searchCount < indexes.length){
				cellIndex = start.getIndex().getRow() + (indexes[index]);
				if(cellIndex > grid.getRowCount()-1 || cellIndex < grid.getMinRow()){
					index++; searchCount++;
					continue;
				}
				CellNode portal = grid.getCell(cellIndex, grid.getColumnCount()-1);
				if(!portal.isOccupied()){
					List<CellNode> path = GET_ASTAR_PATH(grid, start, portal);
					if(!path.isEmpty()){
						portal2D.setPortalIn(portal);
						return path;
					}
				}
				index++;
				searchCount++;
			}
			break;
		case WEST:
			while(searchCount < indexes.length){
				cellIndex = start.getIndex().getRow() + (indexes[index]);
				if(cellIndex > grid.getRowCount()-1 || cellIndex < grid.getMinRow()){
					index++; searchCount++;
					continue;
				}
				CellNode portal = grid.getCell(cellIndex, grid.getMinCol());
				if(!portal.isOccupied()){
					List<CellNode> path = GET_ASTAR_PATH(grid, start, portal);
					if(!path.isEmpty()){
						portal2D.setPortalIn(portal);
						return path;
					}
				}
				index++;
				searchCount++;
			}
			break;
		case NORTH:
			while(searchCount < indexes.length){
				cellIndex = start.getIndex().getCol() + (indexes[index]);
				if(cellIndex > grid.getColumnCount()-1 || cellIndex < grid.getMinCol()){
					index++; searchCount++;
					continue;
				}
				CellNode portal = grid.getCell(grid.getMinRow(), cellIndex);
				if(!portal.isOccupied()){
					List<CellNode> path = GET_ASTAR_PATH(grid, start, portal);
					if(!path.isEmpty()){
						portal2D.setPortalIn(portal);
						return path;
					}
				}
				index++;
				searchCount++;
			}
			break;
		case SOUTH:
			while(searchCount < indexes.length){
				cellIndex = start.getIndex().getCol() + (indexes[index]);
				if(cellIndex > grid.getColumnCount()-1 || cellIndex < grid.getMinCol()){
					index++; searchCount++;
					continue;
				}
				CellNode portal = grid.getCell(grid.getRowCount()-1, cellIndex);
				if(!portal.isOccupied()){
					List<CellNode> path = GET_ASTAR_PATH(grid, start, portal);
					if(!path.isEmpty()){
						portal2D.setPortalIn(portal);
						return path;
					}
				}
				index++;
				searchCount++;
			}
			break;
		}
		return null;
	}

	public boolean isPortalSafe(GridNode grid, CellNode portalOut, int depth, CardinalPoint orientation){
		switch (orientation) {
		case EAST:
			for (int i = 0; i <= depth; i++) {
				if (grid.getCell(portalOut.getIndex().getRow(), portalOut.getIndex().getCol() - i).isOccupied()) {
					return false;
				}
			}
			return true;
		case WEST:
			for (int i = 0; i <= depth; i++) {
				if (grid.getCell(portalOut.getIndex().getRow(), portalOut.getIndex().getCol() + i).isOccupied()) {
					return false;
				}
			}
			return true;
		case NORTH:
			for (int i = 0; i <= depth; i++) {
				if (grid.getCell(portalOut.getIndex().getRow() + i, portalOut.getIndex().getCol()).isOccupied()) {
					return false;
				}
			}
			return true;
		case SOUTH:
			for (int i = 0; i <= depth; i++) {
				if (grid.getCell(portalOut.getIndex().getRow() - i, portalOut.getIndex().getCol()).isOccupied()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public LinkedPath<CellNode> getSafeBorderPath(GridNode grid, CellNode start, LinkedList<Objective> objectives, CardinalPoint orientation){
		LinkedPath<CellNode> borderPath;
		switch(orientation){
		case EAST:
			List<CellNode> eastBorder =  grid.getTeleportZoneEast();
			for (CellNode cell : eastBorder) {
				if(cell.isOccupied()){
					continue;
				}
				if (!cell.isOccupied() && !grid.getCell(cell.getIndex().getRow(), grid.getMinCol()).isOccupied()) {
					if(cell.getLocation().getY()>=start.getLocation().getY()-100){
						borderPath = findPortalCell(grid, cell, grid.getCell(cell.getIndex().getRow(), grid.getMinCol()), start, objectives);
						if (borderPath!=null) {
							lastStep = Direction.RIGHT;
							teleporting = true;
							return borderPath;
						}
					}
				}
			}
			break;
		case WEST:
			List<CellNode> westBoder =  grid.getTeleportZoneWest();
			for (CellNode cell : westBoder) {
				if(cell.isOccupied()){
					continue;
				}
				if (!cell.isOccupied() && !grid.getCell(cell.getIndex().getRow(), grid.getColumnCount()-1).isOccupied()) {
					if(cell.getLocation().getY()>=start.getLocation().getY()-100){
						borderPath = findPortalCell(grid, cell, grid.getCell(cell.getIndex().getRow(), grid.getColumnCount()-1), start, objectives);
						if (borderPath!=null) {
							lastStep = Direction.LEFT;
							teleporting = true;
							return borderPath;
						}
					}
				}
			}
			break;
		case NORTH:
			List<CellNode> northBorder =  grid.getTeleportZoneNorth();
			for (CellNode cell : northBorder) {
				if(cell.isOccupied()){
					continue;
				}
				if (!cell.isOccupied() && !grid.getCell(grid.getRowCount()-1,cell.getIndex().getCol()).isOccupied()) {
					if(cell.getLocation().getX()>=start.getLocation().getX()-100){
						borderPath = findPortalCell(grid, cell, grid.getCell(grid.getRowCount()-1,cell.getIndex().getCol()), start, objectives);
						if (borderPath!=null) {
							lastStep = Direction.UP;
							teleporting = true;
							return borderPath;
						}
					}
				}
			}
			break;
		case SOUTH:
			List<CellNode> southBorder =  grid.getTeleportZoneSouth();
			for (CellNode cell : southBorder) {
				if(cell.isOccupied()){
					continue;
				}
				if (!cell.isOccupied() && !grid.getCell(grid.getMinRow(),cell.getIndex().getCol()).isOccupied()) {
					if(cell.getLocation().getX()>=start.getLocation().getX()-100){
						borderPath = findPortalCell(grid, cell, grid.getCell(grid.getMinCol(),cell.getIndex().getCol()), start, objectives);
						if (borderPath!=null) {
							lastStep = Direction.DOWN;
							teleporting = true;
							return borderPath;
						}
					}
				}
			}break;
		}
		return null;
	}

	public LinkedPath<CellNode> emergencyTeleport(GridNode grid, CellNode start, CellNode  end) {
		LinkedPath<CellNode> path;

		if(start.getLocation().getX() > GameSettings.WIDTH*.65 && start.getLocation().getY() > GameSettings.HEIGHT*.35 && start.getLocation().getY() < GameSettings.HEIGHT*.65){

		}
		if(start.getLocation().getX() < GameSettings.WIDTH*.35 && start.getLocation().getY() > GameSettings.HEIGHT*.35 && start.getLocation().getY() < GameSettings.HEIGHT*.65){

		}
		if(start.getLocation().getY() > GameSettings.HEIGHT*.65 && start.getLocation().getX() > GameSettings.WIDTH && start.getLocation().getX() < GameSettings.WIDTH*.65){

		}
		if(start.getLocation().getY() < GameSettings.HEIGHT*.35 && start.getLocation().getX() > GameSettings.WIDTH && start.getLocation().getX() < GameSettings.WIDTH*.65){

		}

		return null;
	}

	private LinkedPath<CellNode> createCrosspolarPath(GridNode grid, List<CellNode> enterPath, CellNode portalOut, CellNode start, LinkedList<Objective> objectives){

		List<CellNode> pathFromPortal = null;

		PriorityQueue<Distance>  distances = new PriorityQueue<Distance>(objectives.size(), new DistanceComparator());

		for (int i = 0; i < objectives.size(); i++) {
			distances.add(new Distance(portalOut,game.getGameObjectController().getObsFruitList().get(i)));
		}

		while (!distances.isEmpty()) {

			LinkedPath<CellNode> path = GET_SAFE_ASTAR_PATH(grid,portalOut,distances.poll().getObject().getCell(),controller.getGrid().getTailCell(snakeAI));

			if(path.isPathSafe()){

				pathFromPortal = path.getPathOne();

				return new LinkedPath<CellNode>(enterPath, pathFromPortal);
			}
		}
		return null;
	}

	private LinkedPath<CellNode> findPortalCell(GridNode grid, CellNode portalIn, CellNode portalOut, CellNode start, LinkedList<Objective> objectives){

		List<CellNode> pathToPortal = null;
		List<CellNode> pathFromPortal = null;

		PriorityQueue<Distance>  distances = new PriorityQueue<Distance>(objectives.size(), new DistanceComparator());

		for (int i = 0; i < objectives.size(); i++) {
			distances.add(new Distance(portalOut,game.getGameObjectController().getObsFruitList().get(i)));
		}

		pathToPortal = GET_ASTAR_PATH(grid, start, portalIn);

		if (!pathToPortal.isEmpty()) {

			while (!distances.isEmpty()) {

				LinkedPath<CellNode> path = GET_SAFE_ASTAR_PATH(grid,portalOut,distances.poll().getObject().getCell(),controller.getGrid().getTailCell(snakeAI));

				if(path.isPathSafe()){

					pathFromPortal = path.getPathOne();

					return new LinkedPath<CellNode>(pathToPortal, pathFromPortal);
				}
			}
		}

//			pathToPortal = GET_ASTAR_PATH(controller.getGrid(), start, portalIn);
//
//			if (!pathToPortal.isEmpty()) {
//				pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, distances.poll().getObject().getCell());
//
//				if(!pathFromPortal.isEmpty()){
//					return new LinkedPath<CellNode>(pathToPortal,pathFromPortal);
//				}
//				else{
//					pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, distances.poll().getObject().getCell());
//
//					if(!pathFromPortal.isEmpty()){
//						return new LinkedPath<CellNode>(pathToPortal,pathFromPortal);
//					}
//					else{
//						pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, distances.poll().getObject().getCell());
//
//						if(!pathFromPortal.isEmpty()){
//							return new LinkedPath<CellNode>(pathToPortal,pathFromPortal);
//						}
//						else{
//							pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, distances.poll().getObject().getCell());
//
//							if(!pathFromPortal.isEmpty()){
//								return new LinkedPath<CellNode>(pathToPortal,pathFromPortal);
//							}
//						}
//					}
//				}
//			}

		return null;
	}

	/**
	 * Condition which checks the relative location of the player and the calculates which edge
	 * the player is closest too. Once the closest edge is determined we check which cells on that edge
	 * can be reached! if a cell is found the function then checks if the opposite cell at the same row/column
	 * is also accessible if yes a path is created and the player teleports to the opposite side of the screen, at
	 * the specified cell. If not the search will continue until a cell has been found. If no cell the is found
	 * the player will stall until a path is open or until it dies!
	 *
	 * TODO: Make sure a path can be found from the cross polar cell to the cross polar objective!
	 * @param start
	 * @return
	 */


	/**
	 * TODO: Perform a check to determine if the computed path to the objective is a safe path
	 * by computing a path from the start to the objective and from the objective to the tail of the snake.
	 * The path must be computed as a special path which considers the path to the objective to be an obstacle.
	 * Create a special path made out of special obstacles from the start to goal. The path must be an abstract path. Once
	 * that path is created then compute a path from the goal to the tail of the snake, the path must ignore nodes that
	 * belong to the path from start to goal. If a path can be created from goal to tail then the given a path can
	 * be consider somewhat "Safe"!!. If the path is safe allow the snake to go for the apple. but if the path isnt safe
	 *
	 * @param start
	 * @param goal
	 * @param tail
	 * @return
	 */
	private boolean isPathSafe(GridNode grid, CellNode start, CellNode goal, CellNode tail) {

		LinkedPath<CellNode> safeCheck = GET_SAFE_ASTAR_PATH(grid, start, goal, tail);

		return safeCheck.isPathSafe();
	}

	public List<CellNode> getShortestPath(List<List<CellNode>> paths) {

		List<CellNode> smallest = paths.get(0);

		int minSize = Integer.MAX_VALUE;

		for (int i = paths.size() - 1; i >= 0; i--) {

			if (paths.get(i).size() < minSize && !paths.get(i).isEmpty()) {

				minSize = paths.get(i).size();
				smallest = paths.get(i);
			}
		}
		return smallest;
	}

	private void showPathToObjective(LinkedPath<CellNode> cells){
		setPathCoordinates(calculateDirection(cells));
		if (logDirections) {
			for (int i = cells.getPathOne().size() - 1; i >= 0; i--) {
				log("Direction: "+i+ "  = " + cells.getPathOne().get(i).getDirection().toString());
			}
			log("");
			for (int i = cells.getPathTwo().size() - 1; i >= 0; i--) {
				log("Direction: "+i+ "  = " + cells.getPathTwo().get(i).getDirection().toString());
			}
			log("");
		}
	}

	private LinkedPath<CellNode> calculateDirection(LinkedPath<CellNode> paths) {
		if(!paths.getPathOne().isEmpty()){
			for (CellNode node : paths.getPathOne()) {
				if (node.getParentNode() != null) {
					node.setPathCell(true);
					if (node.getIndex().getRow() > node.getParentNode().getIndex().getRow()) {
						node.getParentNode().setDirection(Direction.RIGHT);
					} else if (node.getIndex().getRow() < node.getParentNode().getIndex().getRow()) {
						node.getParentNode().setDirection(Direction.LEFT);
					} else if (node.getIndex().getCol() > node.getParentNode().getIndex().getCol()) {
						node.getParentNode().setDirection(Direction.DOWN);
					} else if (node.getIndex().getCol() < node.getParentNode().getIndex().getCol()) {
						node.getParentNode().setDirection(Direction.UP);
					}
				}
			}
			paths.getPathOne().get(0).setDirection(lastStep);
			lastStep = Direction.NONE;
		}
		if (!paths.getPathTwo().isEmpty() && paths.getType() == ConnectionType.INTERPOLAR_PATH){
			for (CellNode node : paths.getPathTwo()) {
				if (node.getParentNode() != null) {
					node.setPathCell(true);
					if (node.getLocation().getX() > node.getParentNode().getLocation().getX()) {
						node.getParentNode().setDirection(Direction.RIGHT);
					} else if (node.getLocation().getX() < node.getParentNode().getLocation().getX()) {
						node.getParentNode().setDirection(Direction.LEFT);
					} else if (node.getLocation().getY() > node.getParentNode().getLocation().getY()) {
						node.getParentNode().setDirection(Direction.DOWN);
					} else if (node.getLocation().getY() < node.getParentNode().getLocation().getY()) {
						node.getParentNode().setDirection(Direction.UP);
					}
				}
			}
		}
		return paths;
	}
	/**
	 * TODO: Build a list containing coordinates and directions.
	 * make the snake move towards the first direction on the list
	 * if the snake moves reaches the coordinate on the list make the
	 * snake take the next turn and so forth:....
	 */
	public void steerAI() {
		CellNode cell = null;
		CellNode head = controller.getGrid().getRelativeHeadCell(snakeAI);

		if (pathCoordinates != null) {
			for (int index = 0; index < pathCoordinates.getPathOne().size(); index++) {
				cell = pathCoordinates.getPathOne().get(index);
				if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
					switch (cell.getDirection()) {
					case DOWN:
						pathCoordinates.getPathOne().remove(cell);
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_DOWN);
						cell.setPathCell(false);
						objectiveReached(cell);
						onPath = true;
						break;
					case LEFT:
						pathCoordinates.getPathOne().remove(cell);
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_LEFT);
						cell.setPathCell(false);
						objectiveReached(cell);
						onPath = true;
						break;
					case RIGHT:
						pathCoordinates.getPathOne().remove(cell);
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_RIGHT);
						cell.setPathCell(false);
						objectiveReached(cell);
						onPath = true;
						break;
					case UP:
						pathCoordinates.getPathOne().remove(cell);
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_UP);
						cell.setPathCell(false);
						objectiveReached(cell);
						onPath = true;
						break;
					case NONE:
						onPath = false;
						break;
					}
				}
			}
			if(!pathCoordinates.getPathTwo().isEmpty() && pathCoordinates.getType() == ConnectionType.INTERPOLAR_PATH){
				for (int index = 0; index < pathCoordinates.getPathTwo().size(); index++) {
					cell = pathCoordinates.getPathTwo().get(index);
					if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
						switch (cell.getDirection()) {
						case DOWN:
							pathCoordinates.getPathTwo().remove(cell);
							game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_DOWN);
							cell.setPathCell(false);
							objectiveReached(cell);
							onPath = true;
							break;
						case LEFT:
							pathCoordinates.getPathTwo().remove(cell);
							game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_LEFT);
							cell.setPathCell(false);
							objectiveReached(cell);
							onPath = true;
							break;
						case RIGHT:
							pathCoordinates.getPathTwo().remove(cell);
							game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_RIGHT);
							cell.setPathCell(false);
							objectiveReached(cell);
							onPath = true;
							break;
						case UP:
							pathCoordinates.getPathTwo().remove(cell);
							game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_UP);
							cell.setPathCell(false);
							objectiveReached(cell);
							onPath = true;
							break;
						case NONE:
							onPath = false;
							break;
						}
					}
				}
			}
		}
		if(head != null){
			if(head.isPathToGoal()){
				onPath = true;
			}else{
				onPath = false;
			}
		}
	}

	private void objectiveReached(CellNode cell){
		if(cell.isObjective()){
			cell.setObjective(false);
			log("Objective reached");
			controller.nofifyAI();
		}
	}
	/**
	 * Method which under certain conditions will activate the speed boost of
	 * the snake
	 *
	 * @param random
	 */
	public void addRandomBoost(boolean random) {
		if (currentGoal == CurrentGoal.OBJECTIVE) {
			if (state == ActionType.FREE_MODE) {
				if (random && rand.nextInt(randomBoost) != 0) {
					return;
				}
				if (snakeAI != null) {
					applyThrust();
				}
			} else if (state == ActionType.FIND_PATH) {
				if (random && rand.nextInt(randomBoost) != 0) {
					return;
				}
				if (snakeAI != null) {
					applyThrust();
				}
			}
		}
		else{
			snakeAI.setSpeedThrust(false);
		}
	}

	private void log(String str) {
		System.out.println(str+"\n");
	}

	/*
	 * Method which attempts to determine the best course of action in order to
	 * move towards the objective! The method will first check if the x distance
	 * is less or greater than the y distance and based on that it will decide
	 * to perform a horizontal or vertical move. if the method to be perform is
	 * a vertical move the method will check if the objective is above or below
	 * and then perform a move based on the objectives coordinates!
	 */
	private void createPath() {
		switch (state) {
		case DODGE_OBSTACLES:
			break;
		case FIND_PATH:
			break;
		case FREE_MODE:
			break;
		default:
			break;
		}
	}

	/**
	 * Method which performs actions based on the current location of the snake
	 * and the objective! if the snake is within a predetermined threshold the
	 * snake will perform the appropriate turn in order to collect the
	 * objective!
	 */
	private void performLocationBasedAction() {
		switch (state) {
		case DODGE_OBSTACLES:
			break;
		case FIND_PATH:
			steerAI();
			break;
		case FREE_MODE:
			break;
		default:
			break;
		}
	}

	private void applyThrust() {
		if (game.getEnergyBarTwo().getEnergyLevel() >150) {
			if (snakeAI.isAllowThrust() && !snakeAI.getSpeedThrust()) {
				snakeAI.setSpeedThrust(true);
			}
		}
		if (game.getEnergyBarTwo().getEnergyLevel() < 50) {
			snakeAI.setSpeedThrust(false);
		}
	}


	/**
	 * A program which takes a starting cell and the cell to flee from!
	 * The program computes the which cell is farthest away from the cell it wishes to flee from
	 * and computes a path to that cell!
	 * @author Eudy
	 *
	 */
	public class AFleePathFinding{

	}

	public double getX() {
		return snakeAI.getX();
	}

	public double getY() {
		return snakeAI.getY();
	}

	public double getWidth() {
		return snakeAI.getAIBounds().getWidth();
	}

	public double getHeight() {
		return snakeAI.getAIBounds().getHeight();
	}

	public void setPlayer() {
		this.snakeAI = null;
		this.snakeAI = game.getGameLoader().getPlayerTwo();
	}

	private void setPathCoordinates(LinkedPath<CellNode> coordinates){
		this.pathCoordinates = coordinates;
	}

	public PlayerMovement getDirection() {
		return snakeAI.getCurrentDirection();
	}

	public Rectangle2D getCollisionBounds() {
		return snakeAI.getAIBounds();
	}

	private enum CardinalPoint{
		WEST, EAST, NORTH, SOUTH
	}

	private enum CurrentGoal{
		OBJECTIVE, TAIL,
	}

	private enum SearchType{
		CLOSEST_OBJECTIVE, SHORTEST_PATH;
	}

	private enum TieBreaker{
		PATH,CROSS, NONE
	}

	public enum ActionType {
		FREE_MODE, STALL, FIND_PATH, DODGE_OBSTACLES
	}

	private enum HeuristicType{
		MANHATHAN,EUCLIDIAN,CUSTOM_EUCLUDIAN,
	}

	public enum DistressLevel{
		LEVEL_ONE,LEVEL_TWO,LEVEL_THREE,SAFETY_CHECK,CAUTIOUS_CHECK_EMERGENCY
	}

	public int getObjectiveCount(){
		return game.getGameObjectController().getObsFruitList().size();
	}

	public double calculateDistance(double fromX, double toX, double fromY, double toY) {
		return Math.hypot(fromX - toX, fromY - toY);
	}

	public double calculateManhathanDistance(double fromX, double toX, double fromY, double toY) {
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

	private double heuristicCostEstimate(CellNode start, CellNode end, Double scale, HeuristicType cost) {
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

	private class DistanceComparator implements Comparator<Distance>{
		@Override
		public int compare(Distance a, Distance b){
			return Double.compare(a.getDistance(), b.getDistance());
		}
	}

	private class PathLengthComparator implements Comparator<LinkedPath<CellNode>>{
		@Override
		public int compare(LinkedPath<CellNode> a, LinkedPath<CellNode> b){
			return Double.compare(a.getPathOneLength(), b.getPathOneLength());
		}
	}

	public class ObjectiveComparator implements Comparator<Objective> {
		@Override
		public int compare(Objective a, Objective b) {
			return Double.compare(a.getDistance(), b.getDistance());
		}
	}

	public class CellComparator implements Comparator<CellNode> {
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
}
