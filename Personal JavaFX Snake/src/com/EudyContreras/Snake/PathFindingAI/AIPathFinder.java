package com.EudyContreras.Snake.PathFindingAI;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.PathFindingAI.CellNode.Direction;
import com.EudyContreras.Snake.PathFindingAI.LinkedPath.ConnectionType;
import com.EudyContreras.Snake.PathFindingAI.Objective.SortingType;
import com.EudyContreras.Snake.PathFindingAI.SearchAlgorithm.PathType;
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

	private LinkedPath<CellNode> pathCoordinates;
	private AIController controller;
	private SearchAlgorithm pathFinder;
	private GameManager game;
	private PlayerTwo snakeAI;
	private GridNode grid;
	private Random rand;

	private boolean logDirections = false;
	private boolean allowChecks = false;
	private boolean teleporting = false;
	private boolean safetyCheck = false;
	private boolean onPath = true;

	private double checkTimer = 100;

	private int safetyCheckTimer = 0;
	private int randomBoost = 200;

	private DistressLevel distressLevel;
	private CurrentGoal currentGoal;
	private ActionType state;
	private Direction lastStep;

	public AIPathFinder(GameManager game, AIController controller, PlayerTwo snakeAI, LinkedList<CollideNode> possibleColliders) {
		this.game = game;
		this.controller = controller;
		this.snakeAI = snakeAI;
		this.grid = controller.getGrid();
		this.initialize();
	}

	public void initialize() {
		rand = new Random();
		pathFinder = new SearchAlgorithm(game);
		currentGoal = CurrentGoal.OBJECTIVE;
		distressLevel = DistressLevel.LEVEL_TWO;
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
//			snakeAI.setDirectCoordinates(PlayerMovement.MOVE_DOWN);
			currentGoal = CurrentGoal.OBJECTIVE;
			distressLevel = DistressLevel.LEVEL_TWO;
			state = ActionType.FIND_PATH;
			lastStep = Direction.NONE;
			logDirections = false;
			allowChecks = false;
			teleporting = false;
			safetyCheck = true;
			onPath = true;
			checkTimer = 100;
			safetyCheckTimer = 0;
			randomBoost = 200;
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

				if (allowChecks && currentGoal == CurrentGoal.OBJECTIVE) {

					checkTimer++;

//					if (!onPath) {
//						if (checkTimer >= 50) {
//							computePath();
//							checkTimer = 0;
//						}
//					}
					if (checkTimer >= 120) {
						computePath();
						checkTimer = 0;
					}
				}if (safetyCheck && currentGoal == CurrentGoal.TAIL) {
					safetyCheckTimer++;

					if (safetyCheckTimer >= 3000) {
						safetyCheckTimer = 0;
						currentGoal = CurrentGoal.OBJECTIVE;
						computePath();
					}
				}if (safetyCheck && currentGoal == CurrentGoal.FARTHEST_CELL) {
					safetyCheckTimer++;

					if (safetyCheckTimer >= 2200) {
						safetyCheckTimer = 0;
						currentGoal = CurrentGoal.OBJECTIVE;
						computePath();
					}
				}
			}
		}
	}

	public CellNode GET_FARTHEST_CELL(CellNode from) {
		return pathFinder.GET_FARTHEST_CELL(grid, from);
	}

	public LinkedList<CellNode> GET_BRUTE_PATH(CellNode from) {
		return pathFinder.GET_BRUTE_PATH(snakeAI, grid, from, 5);
	}

	public LinkedList<CellNode> GET_LONGEST_LIST(List<LinkedList<CellNode>> lists) {
		return pathFinder.GET_LONGEST_LIST(lists);
	}

	public LinkedList<CellNode> GET_SHORTEST_LIST(List<LinkedList<CellNode>> lists) {
		return pathFinder.GET_SHORTEST_LIST(lists);
	}

	public LinkedList<CellNode> GET_LONGEST_PATH_POLY(CellNode start, CellNode objective) {
		return pathFinder.GET_LONGEST_PATH_POLY(snakeAI, grid, start, objective, distressLevel);
	}

	public LinkedList<CellNode> GET_BFS_PATH(CellNode start, CellNode objective) {
		return pathFinder.GET_BFS_PATH(snakeAI,grid,start,objective,distressLevel);
	}

	public LinkedList<CellNode> GET_DFS_PATH(CellNode start, CellNode objective) {
		return pathFinder.GET_DFS_PATH(snakeAI,grid,start,objective,distressLevel);
	}

	public boolean QUICK_PATH_CHECK(CellNode start, CellNode objective) {
		return pathFinder.QUICK_PATH_SEARCH(grid, start, objective);
	}

	public LinkedList<CellNode> GET_ASTAR_PATH(CellNode start, CellNode objective) {
		return pathFinder.GET_ASTAR_PATH(snakeAI,grid,start,objective,distressLevel);
	}

	public LinkedList<CellNode> GET_ASTAR_LONGEST_PATH(CellNode start, CellNode objective) {
		return pathFinder.GET_ASTAR_LONGEST_HYBRID_PATH(snakeAI,grid,start,objective);
	}

	public LinkedList<CellNode> GET_SAFE_ASTAR_PATH(CellNode start, CellNode objective, CurrentGoal goal, DistressLevel distressLevel){
		return pathFinder.GET_SAFE_ASTAR_PATH(snakeAI,grid, start,objective,goal,distressLevel);
	}

	public LinkedList<Objective> getObjectives(CellNode start, GoalSearch goalSearch, SortingType sorting){

		LinkedList<Objective> objectives = new LinkedList<>();

		switch(goalSearch){
		case CLOSEST_OBJECTIVE:
			for (AbstractObject object : game.getGameObjectController().getObsFruitList()) {
				Objective objective = new Objective(snakeAI, object, sorting);
				objectives.add(objective);
			}

			Collections.sort(objectives);

			return objectives;
		case SHORTEST_PATH:
			PriorityQueue<LinkedPath<CellNode>> paths = new PriorityQueue<LinkedPath<CellNode>>(getObjectiveCount(), new PathLengthComparator());

			for (AbstractObject object : game.getGameObjectController().getObsFruitList()) {
				Objective objective = new Objective(snakeAI, object);
				paths.add(new LinkedPath<CellNode>(GET_ASTAR_PATH(start, objective.getCell()),new LinkedList<CellNode>(), objective));
			}

			while (paths.peek() != null) {
				objectives.add(paths.poll().getObjective());
			}

			return objectives;
		}
		return objectives;
	}

	public void computePath(){
		teleporting = false;

		CellNode start = null;
		CellNode tail = null;

		LinkedPath<CellNode> path = null;

		switch(currentGoal){
		case OBJECTIVE:

			start = controller.getHeadCell(snakeAI);

			LinkedList<Objective> newObjectives = getObjectives(start,GoalSearch.SHORTEST_PATH,SortingType.CLOSEST);

			pathFinder.setPathType(PathType.SHORTEST_PATH);

			if (newObjectives.size() > 0) {
				if (newObjectives.get(0) != null && GameSettings.SHOW_ASTAR_GRAPH) {
					newObjectives.get(0).getObject().blowUpAlt();
				}

				if (start != null) {
					if (!start.isDangerZone()) {
						distressLevel = DistressLevel.LEVEL_TWO;
					}

					for(int i = 0; i < newObjectives.size(); i++){
//						if(!grid.isNeighborNot(newObjectives.get(i).getCell(), Flag.OCCUPIED))
//							continue;
						path = checkObjectiveReach(start, newObjectives.get(i).getCell(), newObjectives.get(i), i, newObjectives, SearchPhase.TO_GOAL);
						if(path!=null){
							break;
						}
					}
					if(path == null){
						distressLevel = DistressLevel.LEVEL_THREE;

						for(int i = 0; i < newObjectives.size(); i++){
//							if(!grid.isNeighborNot(newObjectives.get(i).getCell(), Flag.OCCUPIED))
//								continue;
							path = checkObjectiveReach(start, newObjectives.get(i).getCell(), newObjectives.get(i), i, newObjectives, SearchPhase.TO_GOAL);
							if(path!=null){
								break;
							}
						}
					}

					if(path!=null){
						showPathToObjective(path);

					}else{
						currentGoal = CurrentGoal.TAIL;
						removeThrust();
						computePath();
					}
				}
			}

			break;
		case TAIL:
			pathFinder.setPathType(PathType.LONGEST_PATH);

			start = controller.getHeadCell(snakeAI);

			tail = grid.getTailCellNeighbor(snakeAI);

			if (tail != null) {
//				if (!start.isDangerZone() && !start.isTeleportZone()) {
//					distressLevel = DistressLevel.LEVEL_ONE;
//				}
//
//				path = new LinkedPath<CellNode>(GET_DFS_PATH(start, tail), new LinkedList<>());
//
//				if (!path.getPathOne().isEmpty()) {
//					showPathToObjective(path);
//				} else {

					if (!start.isDangerZone()) {
						distressLevel = DistressLevel.LEVEL_TWO;
					}
					path = checkObjectiveReach(start, tail, null, -1, null, SearchPhase.TO_TAIL);

					if (path!=null) {

						showPathToObjective(path);
					} else {
						log("Path to tail empty!");
						distressLevel = DistressLevel.LEVEL_THREE;

						path = checkObjectiveReach(start, tail, null, -1, null, SearchPhase.TO_TAIL);

						if (path!=null) {

							showPathToObjective(path);
						} else {

							currentGoal = CurrentGoal.FARTHEST_CELL;
							removeThrust();
							computePath();

							log("Emergency path to tail empty!");
//
//							distressLevel = DistressLevel.LEVEL_THREE;
//
//							LinkedList<Objective> objectives = getObjectives(start,GoalSearch.CLOSEST_OBJECTIVE, SortingType.FARTHEST);
//
//							while(!objectives.isEmpty()){
//
//								path = emergencyTeleport(grid, start, objectives.removeFirst().getCell());
//
//								if(path!=null){
//									break;
//								}
//							}
//
//							if (path!=null) {
//								showPathToObjective(path);
//							} else {
//
//								currentGoal = CurrentGoal.FARTHEST_CELL;
//								computePath();
//								log("Emergency teleport path to tail empty!");
//								//TODO: Stall
//								//TODO: Find farthest point in the grid
//								//Make a path to said point and once the point is reach
//								//Attempt a path search again.
//							}
						}
					}
//				}
			}break;
		case FARTHEST_CELL:
			pathFinder.setPathType(PathType.SHORTEST_PATH);

			start = controller.getHeadCell(snakeAI);

			tail = grid.getTailCellNeighbor(snakeAI);

			if (start != null) {
				LinkedList<Objective> objectives = getObjectives(start, GoalSearch.CLOSEST_OBJECTIVE,SortingType.CLOSEST);

				while (!objectives.isEmpty()) {
					path = checkObjectiveReach(start, GET_FARTHEST_CELL(objectives.poll().getCell()), null, -1, null, SearchPhase.FARTHEST_FROM_OBJECTIVE);

					if (path!=null) {
						break;
					}
				}
				if (path!=null) {
					showPathToObjective(path);

				} else {
					log("Path to farthest cell from objective not found!");
					path = checkObjectiveReach(start, GET_FARTHEST_CELL(tail), null, -1, null, SearchPhase.FARTHEST_FROM_TAIL);

					if (path!=null) {

						showPathToObjective(path);
					} else {
						log("Path to farthest cell from tail not found!");
						path = checkObjectiveReach(start, GET_FARTHEST_CELL(start), null, -1, null, SearchPhase.FARTHEST_FROM_CURRENT);

						if (path!=null) {

							showPathToObjective(path);
						} else {
							log("Path to farthest cell from start not found!");
							LinkedList<CellNode> edges = new LinkedList<>(grid.getEdges());

							Collections.shuffle(edges);

							for(int i = 0; i<10; i++){

								CellNode edge = edges.poll();

								if(edge==null)
									continue;

								path = checkObjectiveReach(start, edge, null, -1, null, SearchPhase.TO_RANDOM_EDGE);

								if(path!=null){
									break;
								}
							}

							if(path!=null){

								showPathToObjective(path);
							}else{
								log("Path to random edge cell from start not found!");

								edges = new LinkedList<>(grid.getEdges());

								Collections.shuffle(edges);

								for(int i = 0; i<10; i++){

									CellNode edge = edges.poll();

									if(edge==null)
										continue;

									path = checkObjectiveReach(start, GET_FARTHEST_CELL(edge), null, -1, null, SearchPhase.FARTHEST_FROM_EDGE);

									if(path!=null){
										break;
									}
								}
								if (path != null) {

									showPathToObjective(path);
								} else {
									log("Path to farthest cell from random edge not found!");
									currentGoal = CurrentGoal.TAIL;
//									computePath();
								}
							}
						}
					}
				}
			}
			break;
		}
		controller.setHasBeenNotified(false);
	}

	/**
	 * TODO: Perform a check to determine if the computed path to the objective is a safe path
	 * by computing a path from the start to the objective and from the objective to the tail of the snake.
	 * The path must be computed as a special path which considers the path to the objective to be an obstacle.
	 * Create a special path made out of special obstacles from the start to goal. The path must be an abstract path. Once
	 * that path is created then compute a path from the goal to the tail of the snake, the path must ignore nodes that
	 * belong to the path from start to goal. If a path can be created from goal to tail then the given a path can
	 * be consider somewhat "Safe"!!. If the path is safe allow the snake to go for the apple. but if the path isnt safe
	 */

	private boolean safePathChecker(){

		LinkedList<Objective> newObjectives = new LinkedList<>();

		PriorityQueue<LinkedPath<CellNode>>  paths = new PriorityQueue<LinkedPath<CellNode>>(getObjectiveCount(), new PathLengthComparator());

		CellNode start = controller.getHeadCell(snakeAI);

		LinkedPath<CellNode> path = new LinkedPath<CellNode>();

		pathFinder.setPathType(PathType.SHORTEST_PATH);

		for (int i = 0; i < getObjectiveCount(); i++) {
			AbstractObject object = game.getGameObjectController().getObsFruitList().get(i);
			Objective objective = new Objective(snakeAI, object);
			paths.add(new LinkedPath<CellNode>(GET_ASTAR_PATH(start, objective.getCell()),new LinkedList<CellNode>(), objective));
		}

		while (paths.peek() != null) {
			newObjectives.add(paths.poll().getObjective());
		}

		if (newObjectives.size() > 0) {

			if (start != null) {
				distressLevel = DistressLevel.LEVEL_THREE;

				for(int i = 0; i < newObjectives.size(); i++){
					path = new LinkedPath<CellNode>(GET_DFS_PATH(start, newObjectives.get(i).getCell()), new LinkedList<CellNode>());
					if(!path.isPathOneEmpty()){
						return true;
					}
				}
			}
		}
		return false;
	}

	public LinkedPath<CellNode> checkObjectiveReach(CellNode start, CellNode goal, Objective objective, int index, LinkedList<Objective> objectives, SearchPhase phase){
		if(start!=null){

			CellNode tail = grid.getTailCellNeighbor(snakeAI);

//			if((objectives.get(index).getDistance()) > objectives.get(objectives.size()-1).getInterpolarDistance(start.getLocation().getX(),start.getLocation().getY())){
//				LinkedPath<CellNode> path = computeInterpolarDirection(grid,start,objective,tail,objectives);
//				if(path!=null){
//					return path;
//				}else{
//					path = GET_SAFE_ASTAR_PATH(grid, start, objective.getCell(),tail);
//
//					if(path.isPathSafe()){
//						return path;
//					}
//				}
//			}
//			else if((objectives.get(index).getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.4) && objectives.get(index).getYDistance(start.getLocation().getY())<GameSettings.HEIGHT*.4){
//				LinkedPath<CellNode> path = computeInterpolarDirection(grid,start,objective,tail,objectives);
//				if(path!=null){
//					return path;
//				}else{
//					path = GET_SAFE_ASTAR_PATH(grid, start, objective.getCell(),tail);
//
//					if(path.isPathSafe()){
//						return path;
//					}
//				}
//			}
//			else if(objectives.get(index).getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.5 && objectives.get(index).getXDistance(start.getLocation().getX())<GameSettings.WIDTH*.5){
//				LinkedPath<CellNode> path = computeInterpolarDirection(grid,start,objective,tail,objectives);
//				if(path!=null){
//					return path;
//				}else{
//					path = GET_SAFE_ASTAR_PATH(grid, start, objective.getCell(),tail);
//
//					if(path.isPathSafe()){
//						return path;
//					}
//				}
//			}
//			else if(objectives.get(index).getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.5 && objectives.get(index).getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.5){
//				LinkedPath<CellNode> path = computeInterpolarDirection(grid,start,objective,tail,objectives);
//				if(path!=null){
//					return path;
//				}else{
//					path = GET_SAFE_ASTAR_PATH(grid, start, objective.getCell(),tail);
//
//					if(path.isPathSafe()){
//						return path;
//					}
//				}
//			}
//			//TODO: Find additional conditions that may qualify for interpolation
//
//			else{

			DistressLevel distress = distressLevel == DistressLevel.LEVEL_TWO ? DistressLevel.SAFETY_CHECK_GOAL_LEVEL_TWO : DistressLevel.SAFETY_CHECK_GOAL_LEVEL_THREE;

			LinkedPath<CellNode> safePath = null;

			LinkedList<CellNode> pathToGoal = null;

			switch(phase){
			case FARTHEST_FROM_CURRENT:
				grid.resetCells(true);

				pathToGoal = GET_SAFE_ASTAR_PATH(start, goal, CurrentGoal.OBJECTIVE,distressLevel);

				if (!pathToGoal.isEmpty()) {

					grid.resetCellValues();

					LinkedList<CellNode> pathToTail = GET_SAFE_ASTAR_PATH(goal, tail, CurrentGoal.TAIL, distress);

					if(!pathToTail.isEmpty()){

						return new LinkedPath<CellNode>(pathToGoal,pathToTail,ConnectionType.SAFE_PATH_CHECK);
					}
				}
				break;
			case FARTHEST_FROM_EDGE:
				grid.resetCells(true);

				pathToGoal = GET_SAFE_ASTAR_PATH(start, goal, CurrentGoal.OBJECTIVE,distressLevel);

				if (!pathToGoal.isEmpty()) {

					grid.resetCellValues();

					LinkedList<CellNode> pathToTail = GET_SAFE_ASTAR_PATH(goal, tail, CurrentGoal.TAIL, distress);

					if(!pathToTail.isEmpty()){

						return new LinkedPath<CellNode>(pathToGoal,pathToTail,ConnectionType.SAFE_PATH_CHECK);
					}
				}
				break;
			case FARTHEST_FROM_OBJECTIVE:

				safePath = new LinkedPath<CellNode>(GET_LONGEST_PATH_POLY(start, goal), new LinkedList<>());

				if (!safePath.getPathOne().isEmpty()) {

					return new LinkedPath<CellNode>(pathToGoal,null);

				}
				break;
			case FARTHEST_FROM_TAIL:
				grid.resetCells(true);

				pathToGoal = GET_SAFE_ASTAR_PATH(start, goal, CurrentGoal.OBJECTIVE,distressLevel);

				if (!pathToGoal.isEmpty()) {

					grid.resetCellValues();

					LinkedList<CellNode> pathToTail = GET_SAFE_ASTAR_PATH(goal, tail, CurrentGoal.TAIL, distress);

					if(!pathToTail.isEmpty()){

						return new LinkedPath<CellNode>(pathToGoal,pathToTail,ConnectionType.SAFE_PATH_CHECK);
					}
				}
				break;
			case TO_GOAL:
				grid.resetCells(true);

				pathToGoal = GET_SAFE_ASTAR_PATH(start, objective.getCell(), CurrentGoal.OBJECTIVE,distressLevel);

				if (!pathToGoal.isEmpty()) {

					grid.resetCellValues();

					LinkedList<CellNode> pathToTail = GET_SAFE_ASTAR_PATH(objective.getCell(), tail, CurrentGoal.TAIL, distress);

					if(!pathToTail.isEmpty()){

						return new LinkedPath<CellNode>(pathToGoal,pathToTail,ConnectionType.SAFE_PATH_CHECK);
					}
				}
				break;
			case TO_RANDOM_EDGE:

				grid.resetCells(true);

				pathToGoal = GET_SAFE_ASTAR_PATH(start, goal, CurrentGoal.OBJECTIVE,distressLevel);

				if (!pathToGoal.isEmpty()) {

					grid.resetCellValues();

					LinkedList<CellNode> pathToTail = GET_SAFE_ASTAR_PATH(goal, tail, CurrentGoal.TAIL, distress);

					if(!pathToTail.isEmpty()){

						return new LinkedPath<CellNode>(pathToGoal,pathToTail,ConnectionType.SAFE_PATH_CHECK);
					}
				}
				break;
			case TO_TAIL:
				safePath = new LinkedPath<CellNode>(GET_LONGEST_PATH_POLY(start, tail), new LinkedList<>());

				if(!safePath.getPathOne().isEmpty()){
					return safePath;
				}
				break;
			}
		}
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
		else if(objective.getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.45 && objective.getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.45){

		}
		return null;
	}

	public LinkedPath<CellNode> emergencyTeleport(GridNode grid, CellNode start, CellNode  end) {
		LinkedPath<CellNode> path;

		if(start.getLocation().getX() > GameSettings.WIDTH*.55 && start.getLocation().getY() > GameSettings.HEIGHT*.35 && start.getLocation().getY() < GameSettings.HEIGHT*.65){
			path = getSafeBorderPath(grid, start, end, null, 500, CardinalPoint.EAST,TeleportationImportance.EMERGENCY);
			if(path!=null){
				return path;
			}
		}
		if(start.getLocation().getX() < GameSettings.WIDTH*.45 && start.getLocation().getY() > GameSettings.HEIGHT*.35 && start.getLocation().getY() < GameSettings.HEIGHT*.65){
			path = getSafeBorderPath(grid, start, end, null, 500, CardinalPoint.WEST,TeleportationImportance.EMERGENCY);
			if(path!=null){
				return path;
			}
		}
		if(start.getLocation().getY() > GameSettings.HEIGHT*.55 && start.getLocation().getX() > GameSettings.WIDTH*.35 && start.getLocation().getX() < GameSettings.WIDTH*.65){
			path = getSafeBorderPath(grid, start, end, null, 500, CardinalPoint.SOUTH,TeleportationImportance.EMERGENCY);
			if(path!=null){
				return path;
			}
		}
		if(start.getLocation().getY() < GameSettings.HEIGHT*.45 && start.getLocation().getX() > GameSettings.WIDTH*.35 && start.getLocation().getX() < GameSettings.WIDTH*.65){
			path = getSafeBorderPath(grid, start, end, null, 500, CardinalPoint.NORTH,TeleportationImportance.EMERGENCY);
			if(path!=null){
				return path;
			}
		}
		return null;
	}


	public LinkedPath<CellNode> getPortal(GridNode grid, CellNode start, CellNode tail, Objective objective, LinkedList<Objective> objectives, CardinalPoint orientation){

		LinkedPath<CellNode> path = null;
		CellNode portalIn;
		CellNode portalOut;

		double normalRange = 150;

		int index = 0;
		int searchCount = 0;
		int cellIndex = 0;

		int [] indexes = {0,-1,1,-2,2,-3,3};

		switch(orientation){
		case EAST:
			while (searchCount < indexes.length) {
				cellIndex = start.getIndex().getCol() + (indexes[index]);
				if (cellIndex > grid.getColumnCount() - 1 || cellIndex < grid.getMinCol()) {
					index++;
					searchCount++;
					continue;
				}
				portalIn = grid.getCell(grid.getRowCount() - 1, cellIndex);
				if (!portalIn.isOccupied()) {
					portalOut = grid.getCell(grid.getMinRow(), cellIndex);
					if (isPortalOutSafe(grid, portalOut, 2, CardinalPoint.WEST)) {
						path = findPortalCell(grid, portalIn, portalOut, start, objectives);
						if (path!=null) {
							lastStep = Direction.RIGHT;
							teleporting = true;
							return path;
						} else {
							path = getSafeBorderPath(grid, start, tail, objectives, normalRange, CardinalPoint.EAST, TeleportationImportance.NORMAL);
							if (path != null) {
								return path;
							}
						}
					}
				}
				index++;
				searchCount++;
			}
			break;
		case WEST:
			while (searchCount < indexes.length) {
				cellIndex = start.getIndex().getCol() + (indexes[index]);
				if (cellIndex > grid.getColumnCount() - 1 || cellIndex < grid.getMinCol()) {
					index++;
					searchCount++;
					continue;
				}
				portalIn = grid.getCell(grid.getMinRow(), cellIndex);
				if (!portalIn.isOccupied()) {
					portalOut = grid.getCell(grid.getRowCount() - 1, cellIndex);
					if (isPortalOutSafe(grid, portalOut, 2, CardinalPoint.EAST)) {
						path = findPortalCell(grid, portalIn, portalOut, start, objectives);
						if (path!=null) {
							lastStep = Direction.LEFT;
							teleporting = true;
							return path;
						} else {
							path = getSafeBorderPath(grid, start, tail, objectives, normalRange, CardinalPoint.WEST, TeleportationImportance.NORMAL);
							if (path != null) {
								return path;
							}
						}
					}
				}
				index++;
				searchCount++;
			}
			break;
		case NORTH:
			while (searchCount < indexes.length) {
				cellIndex = start.getIndex().getRow() + (indexes[index]);
				if (cellIndex > grid.getRowCount() - 1 || cellIndex < grid.getMinRow()) {
					index++;
					searchCount++;
					continue;
				}
				portalIn = grid.getCell(cellIndex,grid.getMinCol());
				if (!portalIn.isOccupied()) {
					portalOut = grid.getCell(cellIndex,grid.getColumnCount()-1);
					if (isPortalOutSafe(grid, portalOut, 2, CardinalPoint.SOUTH)) {
						path = findPortalCell(grid, portalIn, portalOut, start, objectives);
						if (path!=null) {
							lastStep = Direction.UP;
							teleporting = true;
							return path;
						} else {
							path = getSafeBorderPath(grid, start, tail, objectives, normalRange, CardinalPoint.NORTH, TeleportationImportance.NORMAL);
							if (path != null) {
								return path;
							}
						}
					}
				}
				index++;
				searchCount++;
			}
			break;
		case SOUTH:
			while (searchCount < indexes.length) {
				cellIndex = start.getIndex().getRow() + (indexes[index]);
				if (cellIndex > grid.getRowCount() - 1 || cellIndex < grid.getMinRow()) {
					index++;
					searchCount++;
					continue;
				}
				portalIn = grid.getCell(cellIndex,grid.getColumnCount()-1);
				if (!portalIn.isOccupied()) {
					portalOut = grid.getCell(cellIndex,grid.getMinCol());
					if (isPortalOutSafe(grid, portalOut, 2, CardinalPoint.NORTH)) {
						path = findPortalCell(grid, portalIn, portalOut, start, objectives);
						if (path!=null) {
							lastStep = Direction.DOWN;
							teleporting = true;
							return path;
						} else {
							path = getSafeBorderPath(grid, start, tail, objectives, normalRange, CardinalPoint.SOUTH, TeleportationImportance.NORMAL);
							if (path != null) {
								return path;
							}
						}
					}
				}
				index++;
				searchCount++;
			}
			break;
		}
		return null;
	}
	public LinkedList<CellNode> getSafeEnterPath(GridNode grid, PortalWrapper portalWrapper, CellNode start, CardinalPoint orientation){
		int index = 0;
		int searchCount = 0;
		int cellIndex = 0;
		int [] indexes = {0,-1,1,-2,2,-3,3};

		switch(orientation){
		case EAST:
			while(searchCount < indexes.length){
				cellIndex = start.getIndex().getCol() + (indexes[index]);
				if(cellIndex > grid.getColumnCount()-1 || cellIndex < grid.getMinCol()){
					index++; searchCount++;
					continue;
				}
				CellNode portal = grid.getCell(grid.getRowCount()-1,cellIndex);
				if(!portal.isOccupied()){
					LinkedList<CellNode> path = GET_ASTAR_PATH(start, portal);
					if(!path.isEmpty()){
						portalWrapper.setPortalIn(portal);
						return path;
					}
				}
				index++;
				searchCount++;
			}
			break;
		case WEST:
			while(searchCount < indexes.length){
				cellIndex = start.getIndex().getCol() + (indexes[index]);
				if(cellIndex > grid.getColumnCount()-1 || cellIndex < grid.getMinCol()){
					index++; searchCount++;
					continue;
				}
				CellNode portal = grid.getCell(grid.getMinRow(),cellIndex);
				if(!portal.isOccupied()){
					LinkedList<CellNode> path = GET_ASTAR_PATH(start, portal);
					if(!path.isEmpty()){
						portalWrapper.setPortalIn(portal);
						return path;
					}
				}
				index++;
				searchCount++;
			}
			break;
		case NORTH:
			while(searchCount < indexes.length){
				cellIndex = start.getIndex().getRow() + (indexes[index]);
				if(cellIndex > grid.getRowCount()-1 || cellIndex < grid.getMinRow()){
					index++; searchCount++;
					continue;
				}
				CellNode portal = grid.getCell(cellIndex,grid.getMinCol());
				if(!portal.isOccupied()){
					LinkedList<CellNode> path = GET_ASTAR_PATH(start, portal);
					if(!path.isEmpty()){
						portalWrapper.setPortalIn(portal);
						return path;
					}
				}
				index++;
				searchCount++;
			}
			break;
		case SOUTH:
			while(searchCount < indexes.length){
				cellIndex = start.getIndex().getRow() + (indexes[index]);
				if(cellIndex > grid.getRowCount()-1 || cellIndex < grid.getMinRow()){
					index++; searchCount++;
					continue;
				}
				CellNode portal = grid.getCell(cellIndex,grid.getColumnCount()-1);
				if(!portal.isOccupied()){
					LinkedList<CellNode> path = GET_ASTAR_PATH(start, portal);
					if(!path.isEmpty()){
						portalWrapper.setPortalIn(portal);
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

	public boolean isPortalOutSafe(GridNode grid, CellNode portalOut, int depth, CardinalPoint orientation){
		switch (orientation) {
		case EAST:
			for (int i = 0; i <= depth; i++) {
				if (grid.getCell(portalOut.getIndex().getRow() - i, portalOut.getIndex().getCol()).isOccupied()) {
					return false;
				}
			}
			return true;
		case WEST:
			for (int i = 0; i <= depth; i++) {
				if (grid.getCell(portalOut.getIndex().getRow() + i, portalOut.getIndex().getCol()).isOccupied()) {
					return false;
				}
			}
			return true;
		case NORTH:
			for (int i = 0; i <= depth; i++) {
				if (grid.getCell(portalOut.getIndex().getRow(), portalOut.getIndex().getCol() + i).isOccupied()) {
					return false;
				}
			}
			return true;
		case SOUTH:
			for (int i = 0; i <= depth; i++) {
				if (grid.getCell(portalOut.getIndex().getRow(), portalOut.getIndex().getCol() - i).isOccupied()) {
					return false;
				}
			}
			return true;
		}
		return false;
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

	public LinkedPath<CellNode> getSafeBorderPath(GridNode grid, CellNode start, CellNode tail, LinkedList<Objective> objectives, double searchRange, CardinalPoint orientation, TeleportationImportance importance){
		LinkedPath<CellNode> borderPath = null;

		switch(orientation){
		case EAST:
			List<CellNode> eastBorder =  grid.getTeleportZoneEast();
			for (CellNode portalIn : eastBorder) {
				if(portalIn.isOccupied()){
					continue;
				}
				if(portalIn.getDistanceFrom(start)<searchRange){
					CellNode portalOut = grid.getCell(grid.getMinRow(),portalIn.getIndex().getCol());

					if (!isPortalOutSafe(grid, portalOut, 2, CardinalPoint.WEST)) {
						continue;
					}
					if(importance == TeleportationImportance.NORMAL){
						borderPath = findPortalCell(grid, portalIn, portalOut, start, objectives);
					}else if(importance == TeleportationImportance.EMERGENCY){
						borderPath = findPortalCell(grid, portalIn, portalOut, start, tail);
					}
					if (borderPath!=null) {
						lastStep = Direction.RIGHT;
						teleporting = true;
						return borderPath;
					}
				}
			}
			break;
		case WEST:
			List<CellNode> westBoder =  grid.getTeleportZoneWest();
			for (CellNode portalIn : westBoder) {
				if(portalIn.isOccupied()){
					continue;
				}
				if(portalIn.getDistanceFrom(start)<searchRange){
					CellNode portalOut = grid.getCell(grid.getRowCount()-1,portalIn.getIndex().getCol());

					if (!isPortalOutSafe(grid, portalOut, 2, CardinalPoint.EAST)) {
						continue;
					}
					if(importance == TeleportationImportance.NORMAL){
						borderPath = findPortalCell(grid, portalIn, portalOut, start, objectives);
					}else if(importance == TeleportationImportance.EMERGENCY){
						borderPath = findPortalCell(grid, portalIn, portalOut, start, tail);
					}
					if (borderPath!=null) {
						lastStep = Direction.LEFT;
						teleporting = true;
						return borderPath;
					}
				}
			}
			break;
		case NORTH:
			List<CellNode> northBorder =  grid.getTeleportZoneNorth();
			for (CellNode portalIn : northBorder) {
				if(portalIn.isOccupied()){
					continue;
				}
				if(portalIn.getDistanceFrom(start)<searchRange){
					CellNode portalOut = grid.getCell(portalIn.getIndex().getRow(),grid.getColumnCount()-1);

					if (!isPortalOutSafe(grid, portalOut, 2, CardinalPoint.SOUTH)) {
						continue;
					}
					if(importance == TeleportationImportance.NORMAL){
						borderPath = findPortalCell(grid, portalIn, portalOut, start, objectives);
					}else if(importance == TeleportationImportance.EMERGENCY){
						borderPath = findPortalCell(grid, portalIn, portalOut, start, tail);
					}
					if (borderPath!=null) {
						lastStep = Direction.UP;
						teleporting = true;
						return borderPath;
					}
				}
			}
			break;
		case SOUTH:
			List<CellNode> southBorder =  grid.getTeleportZoneSouth();
			for (CellNode portalIn : southBorder) {
				if(portalIn.isOccupied()){
					continue;
				}
				if(portalIn.getDistanceFrom(start)<searchRange){
					CellNode portalOut = grid.getCell(portalIn.getIndex().getRow(),grid.getMinCol());

					if (!isPortalOutSafe(grid, portalOut, 2, CardinalPoint.NORTH)) {
						continue;
					}
					if(importance == TeleportationImportance.NORMAL){
						borderPath = findPortalCell(grid, portalIn, portalOut, start, objectives);
					}else if(importance == TeleportationImportance.EMERGENCY){
						borderPath = findPortalCell(grid, portalIn, portalOut, start, tail);
					}
					if (borderPath!=null) {
						lastStep = Direction.DOWN;
						teleporting = true;
						return borderPath;
					}
				}
			}break;
		}
		return null;
	}

	private LinkedPath<CellNode> findPortalCell(GridNode grid, CellNode portalIn, CellNode portalOut, CellNode start, LinkedList<Objective> objectives){

		LinkedList<CellNode> pathToPortal = null;
		LinkedList<CellNode> pathFromPortal = null;

		PriorityQueue<DistanceWrapper>  distanceWrappers = new PriorityQueue<DistanceWrapper>(objectives.size(), new DistanceComparator());

		for (int i = 0; i < objectives.size(); i++) {
			distanceWrappers.add(new DistanceWrapper(portalOut,game.getGameObjectController().getObsFruitList().get(i)));
		}

		pathToPortal = GET_ASTAR_PATH(start, portalIn);

		if (!pathToPortal.isEmpty()) {

			while (!distanceWrappers.isEmpty()) {

//				LinkedPath<CellNode> path = GET_SAFE_ASTAR_PATH(portalOut,distanceWrappers.poll().getObject().getCell(),grid.getTailCell(snakeAI));
//
//				if(path.isPathSafe()){
//
//					pathFromPortal = path.getPathOne();
//
//					return new LinkedPath<CellNode>(pathToPortal, pathFromPortal);
//				}
			}
		}
		return null;
	}

	private LinkedPath<CellNode> findPortalCell(GridNode grid, CellNode portalIn, CellNode portalOut, CellNode start, CellNode tail){

		LinkedList<CellNode> pathToPortal = null;
		LinkedList<CellNode> pathFromPortal = null;

		pathToPortal = GET_ASTAR_PATH(start, portalIn);

		if (!pathToPortal.isEmpty()) {

			pathFromPortal = GET_DFS_PATH(portalOut, tail);

			if(!pathFromPortal.isEmpty()){

				return new LinkedPath<CellNode>(pathToPortal, pathFromPortal);
			}
		}

		return null;
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
//		if(!paths.getPathOne().isEmpty()){
//			paths.getPathOne().stream().forEach(node -> {
//				if (node.getParentNode() != null) {
//					node.setPathCell(true);
//					if (node.getIndex().getRow() > node.getParentNode().getIndex().getRow()) {
//						node.getParentNode().setDirection(Direction.RIGHT);
//					} else if (node.getIndex().getRow() < node.getParentNode().getIndex().getRow()) {
//						node.getParentNode().setDirection(Direction.LEFT);
//					} else if (node.getIndex().getCol() > node.getParentNode().getIndex().getCol()) {
//						node.getParentNode().setDirection(Direction.DOWN);
//					} else if (node.getIndex().getCol() < node.getParentNode().getIndex().getCol()) {
//						node.getParentNode().setDirection(Direction.UP);
//					}
//				}
//			});
//			paths.getPathOne().get(0).setDirection(lastStep);
//			lastStep = Direction.NONE;
//		}
//		if (!paths.getPathTwo().isEmpty() && paths.getType() == ConnectionType.INTERPOLAR_PATH){
//			paths.getPathOne().stream().forEach(node -> {
//				if (node.getParentNode() != null) {
//					node.setPathCell(true);
//					if (node.getLocation().getX() > node.getParentNode().getLocation().getX()) {
//						node.getParentNode().setDirection(Direction.RIGHT);
//					} else if (node.getLocation().getX() < node.getParentNode().getLocation().getX()) {
//						node.getParentNode().setDirection(Direction.LEFT);
//					} else if (node.getLocation().getY() > node.getParentNode().getLocation().getY()) {
//						node.getParentNode().setDirection(Direction.DOWN);
//					} else if (node.getLocation().getY() < node.getParentNode().getLocation().getY()) {
//						node.getParentNode().setDirection(Direction.UP);
//					}
//				}
//			});
//		}
		return paths;
	}
	/**
	 * TODO: Build a list containing coordinates and directions.
	 * make the snake move towards the first direction on the list
	 * if the snake moves reaches the coordinate on the list make the
	 * snake take the next turn and so forth:....
	 */
	public void steerAI() {
		CellNode head = grid.getRelativeHeadCell(snakeAI);

		if (pathCoordinates != null) {
			pathCoordinates.getPathOne().stream().forEach(cell -> {
				if(!cell.isPathCell())
					return;
				if(cell.isObjective()){
					if(cell.getBoundsCheck().intersects(snakeAI.getAIBounds())){
						controller.nofifyAI();
					}
				}
				if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
					switch (cell.getDirection()) {
					case DOWN:
						snakeAI.setDirectCoordinates(PlayerMovement.MOVE_DOWN);
						cell.setPathCell(false);
						objectiveReached(cell);
						onPath = true;
						break;
					case LEFT:
						snakeAI.setDirectCoordinates(PlayerMovement.MOVE_LEFT);
						cell.setPathCell(false);
						objectiveReached(cell);
						onPath = true;
						break;
					case RIGHT:
						snakeAI.setDirectCoordinates(PlayerMovement.MOVE_RIGHT);
						cell.setPathCell(false);
						objectiveReached(cell);
						onPath = true;
						break;
					case UP:
						snakeAI.setDirectCoordinates(PlayerMovement.MOVE_UP);
						cell.setPathCell(false);
						objectiveReached(cell);
						onPath = true;
						break;
					case NONE:
						onPath = false;
//						cell.setPathCell(false);
						objectiveReached(cell);
						break;
					}
				}
			});

			if(!pathCoordinates.getPathTwo().isEmpty() && pathCoordinates.getType() == ConnectionType.INTERPOLAR_PATH){
				pathCoordinates.getPathTwo().stream().forEach(cell -> {
					if(!cell.isPathCell())
						return;

					if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
						switch (cell.getDirection()) {
						case DOWN:
							snakeAI.setDirectCoordinates(PlayerMovement.MOVE_DOWN);
							cell.setPathCell(false);
							objectiveReached(cell);
							onPath = true;
							break;
						case LEFT:
							snakeAI.setDirectCoordinates(PlayerMovement.MOVE_LEFT);
							cell.setPathCell(false);
							objectiveReached(cell);
							onPath = true;
							break;
						case RIGHT:
							snakeAI.setDirectCoordinates(PlayerMovement.MOVE_RIGHT);
							cell.setPathCell(false);
							objectiveReached(cell);
							onPath = true;
							break;
						case UP:
							snakeAI.setDirectCoordinates(PlayerMovement.MOVE_UP);
							cell.setPathCell(false);
							objectiveReached(cell);
							onPath = true;
							break;
						case NONE:
							onPath = false;
//							cell.setPathCell(false);
							objectiveReached(cell);
							break;
						}
					}
				});
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
//			currentGoal = CurrentGoal.OBJECTIVE;
			controller.nofifyAI();
		}
	}
	/**
	 * Method which under certain conditions will activate the
	 * speed boost of the snake.
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
			removeThrust();
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

	private void removeThrust(){
		snakeAI.setSpeedThrust(false);
		snakeAI.setThrustState(false);
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

	private enum GoalSearch{
		CLOSEST_OBJECTIVE, SHORTEST_PATH
	}

	private enum CardinalPoint{
		WEST, EAST, NORTH, SOUTH
	}

	public enum CurrentGoal{
		OBJECTIVE, TAIL, FARTHEST_CELL
	}

	public enum ActionType {
		FREE_MODE, STALL, FIND_PATH, DODGE_OBSTACLES
	}

	private enum TeleportationImportance{
		NORMAL,EMERGENCY
	}

	private enum SearchPhase{
		TO_GOAL, TO_TAIL, TO_RANDOM_EDGE, FARTHEST_FROM_OBJECTIVE, FARTHEST_FROM_TAIL, FARTHEST_FROM_CURRENT, FARTHEST_FROM_EDGE
	}

	public enum DistressLevel{
		LEVEL_ONE,LEVEL_TWO,LEVEL_THREE,SAFETY_CHECK_GOAL_LEVEL_TWO,CAUTIOUS_CHECK_EMERGENCY, SAFETY_CHECK_TAIL, SAFETY_CHECK_GOAL_LEVEL_THREE
	}

	public int getObjectiveCount(){
		return game.getGameObjectController().getObsFruitList().size();
	}

	private class DistanceComparator implements Comparator<DistanceWrapper>{
		@Override
		public int compare(DistanceWrapper a, DistanceWrapper b){
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

}
