package com.EudyContreras.Snake.PathFindingAI_BK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
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
public class AIPathFinder8 {

	private AIController controller;
	private GameManager game;
	private PlayerTwo snakeAI;
	private Random rand;

	private boolean logDirections = false;
	private boolean searching = false;
	private boolean allowTrace = false;
	private boolean trackingTail = false;

	private double checkTimer = 100;
	private double heuristicScale = 1;

	private int cellCount = 0;
	private int randomBoost = 200;

	private ObjectivePosition location;
	private DistressLevel distressLevel;
	private PathType pathType;
	private HeuristicType heuristicType;
	private SearchType searchType;
	private TieBreaker tieBreaker;
	private ActionType state;
	private Direction lastStep;

	private Objective[] objectives;
	private List<CellNode> pathCoordinates;


	public AIPathFinder8(GameManager game, PlayerTwo snakeAI) {
		this.game = game;
		this.snakeAI = snakeAI;
		this.initialize();
	}

	public AIPathFinder8(GameManager game, AIController controller, PlayerTwo snakeAI, LinkedList<CollideNode> possibleColliders) {
		this.game = game;
		this.controller = controller;
		this.snakeAI = snakeAI;
		this.initialize();
	}

	public void initialize() {
		rand = new Random();
		cellCount = controller.getGrid().getRowCount() * controller.getGrid().getColumnCount();
		heuristicType = HeuristicType.MANHATHAN;
		distressLevel = DistressLevel.NORMAL;
		searchType = SearchType.CLOSEST_OBJECTIVE;
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
			computeClosestPath(5,5);
			snakeAI.setDirectCoordinates(PlayerMovement.MOVE_DOWN);
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
				if(checkTimer<=0){
					computeClosestPath(0,0);
					if(!trackingTail){
						checkTimer = 200;
					}
					else{
						checkTimer = 60;
					}
				}
			}
		}
	}

	/**
	 * Find a path from start to goal using the A* algorithm
	 */

	public synchronized List<CellNode> getPath(GridNode grid, CellNode startingPoint, CellNode objective) {

		HashSet<CellNode> closedSet = new HashSet<>(cellCount);

		PriorityQueue<CellNode>openedSet = new PriorityQueue<CellNode>(cellCount, new CellComparator());

		CellNode current = null;

		double turnPenalty = 0;

		boolean containsNeighbor;

		openedSet.add(startingPoint);

		startingPoint.setOccupied(false);

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

		startingPoint.setMovementCost(0d);

		startingPoint.setTotalCost(startingPoint.getMovementCost() + heuristicCostEstimate(startingPoint, objective,heuristicScale,heuristicType)); //The higher the scale the less the number of turn: scale from 1 to 2

		searching = true;

		while (!openedSet.isEmpty() && searching) {

			current = openedSet.poll();

			if (current.equals(objective)) {
				this.endPathSearch();
				return createCoordinates(objective);
			}
			closedSet.add(current);

			for (CellNode neighbor : grid.getNeighborCells(current,distressLevel)) {

				if (neighbor == null) {
					continue;
				}

				if (closedSet.contains(neighbor)) {
					continue;
				}

				double  tentativeScoreG = current.getMovementCost() + heuristicCostEstimate(current, neighbor,heuristicScale,heuristicType); //The higher the scale the less the number of turn: scale from 1 to 2

				if (!(containsNeighbor = openedSet.contains(neighbor)) || Double.compare(tentativeScoreG, neighbor.getMovementCost()) < 0) {

					neighbor.setParentNode(current);

					neighbor.setMovementCost(tentativeScoreG);

					if (current.getParentNode() != null) {
						if (neighbor.getIndex().getRow() != current.getParentNode().getIndex().getRow()
						 || neighbor.getIndex().getCol() != current.getParentNode().getIndex().getCol()) {
							neighbor.setMovementCost(tentativeScoreG+turnPenalty);
						}
					}
					double heuristic = 0;

					double path = 10 / 1000;
					double dx1 = neighbor.getLocation().getX() - objective.getLocation().getX();
					double dy1 = neighbor.getLocation().getY() - objective.getLocation().getY();
					double dx2 = startingPoint.getLocation().getX() - objective.getLocation().getX();
					double dy2 = startingPoint.getLocation().getY() - objective.getLocation().getY();

					double cross = Math.abs(dx1 * dy2 - dx2 * dy1);

					heuristic =heuristicCostEstimate(neighbor, objective,2.0,heuristicType);

					switch (tieBreaker) {

					case CROSS:
						heuristic += cross * 0.001;
						break;
					case PATH:
						heuristic *= (1.0 + path);
						break;
					case NONE:
						heuristic *= heuristicScale;
						break;
					}

					neighbor.setHeuristic(heuristic); // If used with scaled up heuristic it gives least number of turns!

					neighbor.setTotalCost(neighbor.getMovementCost() + neighbor.getHeuristic());

					if (!containsNeighbor) {

						openedSet.add(neighbor);
					}
				}
			}
		}
		endPathSearch();

		return new ArrayList<>();
	}

	private void endPathSearch(){
		searching = false;
		allowTrace = false;
	}

	/**
	 * Create final path of the A* algorithm. The path is from goal to start.
	 */
	private synchronized List<CellNode> createCoordinates(CellNode current) {

		List<CellNode> totalPath = new LinkedList<CellNode>();

		totalPath.add(current);

		while ((current = current.getParentNode()) != null) {

			totalPath.add(current);

		}

		return totalPath;
	}

	public synchronized void computeClosestPath(int row, int col){
		objectives = new Objective[game.getGameObjectController().getObsFruitList().size()];

		for (int i = 0; i < objectives.length  ; i++) {
			AbstractObject objective = game.getGameObjectController().getObsFruitList().get(i);
			objectives[i] = new Objective(snakeAI,objective);
		}

		controller.getGrid().resetCells();

		CellNode start = null;
		CellNode goal = null;
		CellNode tail = null;

		List<CellNode> path = null;

		if(searchType == SearchType.SHORTEST_PATH){

			start = controller.getHeadCell(snakeAI, 0, 0);

			List<CellNode> path1 = getPath(controller.getGrid(),start,objectives[0].getCell());
			List<CellNode> path2 = getPath(controller.getGrid(),start,objectives[1].getCell());
			List<CellNode> path3 = getPath(controller.getGrid(),start,objectives[2].getCell());
			List<CellNode> path4 = getPath(controller.getGrid(),start,objectives[3].getCell());

			if(start!=null){
				path = getShortestPath(path1,path2,path3,path4);
			}
		}
		else if (searchType == SearchType.CLOSEST_OBJECTIVE) {

			Arrays.sort(objectives);

			if (objectives[0] != null && GameSettings.DEBUG_MODE) {
				objectives[0].getObject().blowUpAlt();
			}

			start = controller.getHeadCell(snakeAI, 0, 0);

			if(!start.isDangerZone()){
				distressLevel = DistressLevel.NORMAL;
			}
			path = checkObjectiveReach(start, goal, path, 0);
		}

		if (!path.isEmpty()) {
			trackingTail = false;
			pathType = PathType.SHORTEST_PATH;
			showPathToObjective(path);

		} else {
			path = checkObjectiveReach(start, goal, path, 1);

			if (!path.isEmpty()) {
				trackingTail = false;
				pathType = PathType.SHORTEST_PATH;
				showPathToObjective(path);

			} else {
				path = checkObjectiveReach(start, goal, path, 2);

				if (!path.isEmpty()) {
					trackingTail = false;
					pathType = PathType.SHORTEST_PATH;
					showPathToObjective(path);

				} else {
					path = checkObjectiveReach(start, goal, path, 3);

					if (!path.isEmpty()) {
						trackingTail = false;
						pathType = PathType.SHORTEST_PATH;
						showPathToObjective(path);

					}
					else {

						start = controller.getHeadCell(snakeAI, 0, 0);
						tail = controller.getGrid().getTailCell();

						if (start != null && tail != null) {
							if(!start.isDangerZone()){
								distressLevel = DistressLevel.NORMAL;
							}
							path = getPath(controller.getGrid(), start, tail);
						}
						if (!path.isEmpty()) {
							trackingTail = true;
							pathType = PathType.LONGEST_PATH;
							showPathToObjective(path);
						} else {

							log("Normal path to tail is empty");

							distressLevel = DistressLevel.EMERGENCY;
							path = getPath(controller.getGrid(), start, tail);

							if (!path.isEmpty()) {
								trackingTail = true;
								pathType = PathType.LONGEST_PATH;
								showPathToObjective(path);
							}
							else {

								log("Emergency path to tail empty!");

								distressLevel = DistressLevel.EMERGENCY;
								path = emergencyTeleport(controller.getGrid(), start);

								if (!path.isEmpty()) {
									trackingTail = true;
									pathType = PathType.LONGEST_PATH;
									log("LEVEL_THREE TELEPORT");
									showPathToObjective(path);
								}
								else {
									// TODO: Stall until path is found or
									// die!!!!
								}
							}
						}
					}
				}
			}
		}
	}
	public List<CellNode> checkObjectiveReach(CellNode start, CellNode goal,List<CellNode> path, int index){
		start = controller.getHeadCell(snakeAI, 0, 0);

		if((objectives[index].getDistance()+100)<objectives[index==3 ? 0 : 3].getInterpolarDistance(snakeAI.getX(), snakeAI.getY())){
			goal = objectives[index].getCell();
		}
		else{
			log("Farthest is closest!!");
			if(start!=null){
				goal = getCrossPolarCell(controller.getGrid(),start,objectives[index].getCell());
			}
		}

		if(start!=null && goal!=null){
			if(!start.isDangerZone()){
				distressLevel = DistressLevel.NORMAL;
			}
			path = getPath(controller.getGrid(),start ,goal);

			if(!path.isEmpty()){
				return path;
			}
			else{
				log("Normal path to "+index+" goal is empty");

				distressLevel = DistressLevel.EMERGENCY;
				path = getPath(controller.getGrid(),start,goal);

				if(!path.isEmpty()){
					return path;
				}
				else{
					log("Emegency path "+index+" to goal is empty!");
				}
			}
		}
		return new ArrayList<>();
	}

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
	public boolean isPathSafe(CellNode start, CellNode goal, CellNode tail){

		return true;
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
	private CellNode getCrossPolarCell(GridNode grid, CellNode start, CellNode objective) {
		if (start.getLocation().getX() < GameSettings.WIDTH * .35) {
			log("WEST");
			LinkedList<CellNode> westBorder =  grid.getTeleportZoneWest();
			for (CellNode cell : westBorder) {
				if (cell.getLocation().getY()>=start.getLocation().getY() && !cell.isOccupied() && !grid.getCell(cell.getIndex().getRow(), grid.getColumnCount() - 1).isOccupied()) {
					List<CellNode> path = getPath(controller.getGrid(), start, cell);
					if (!path.isEmpty()) {
						lastStep = Direction.LEFT;
						return cell;
					} else {
						continue;
					}
				}
			}
		}
		else if (start.getLocation().getX() > GameSettings.WIDTH * .65) {
			log("EAST");
			LinkedList<CellNode> eastBorder =  grid.getTeleportZoneEast();
			for (CellNode cell : eastBorder) {
				if (!cell.isOccupied() && cell.getLocation().getY()>=start.getLocation().getY() && !grid.getCell(cell.getIndex().getRow(), grid.getMinCol()).isOccupied()) {
					List<CellNode> path = getPath(controller.getGrid(), start, cell);
					if (!path.isEmpty()) {
						lastStep = Direction.RIGHT;
						return cell;
					} else {
						continue;
					}
				}
			}
		}
		else if (start.getLocation().getY()<GameSettings.HEIGHT*.35){
			log("NORTH");
			LinkedList<CellNode> northBorder =  grid.getTeleportZoneNorth();
			for (CellNode cell : northBorder) {
				if (!cell.isOccupied() && cell.getLocation().getX()>=start.getLocation().getX() && !grid.getCell(grid.getRowCount() - 1, cell.getIndex().getCol()).isOccupied()) {
					List<CellNode> path = getPath(controller.getGrid(), start, cell);
					if (!path.isEmpty()) {
						lastStep = Direction.UP;
						return cell;
					} else {
						continue;
					}
				}
			}
		}
		else if (start.getLocation().getY() > GameSettings.HEIGHT * .65) {
			log("SOUTH");
			LinkedList<CellNode> southBorder =  grid.getTeleportZoneSouth();
			for (CellNode cell : southBorder) {
				if (!cell.isOccupied() && cell.getLocation().getX()>=start.getLocation().getX()&& !grid.getCell(grid.getMinRow(), cell.getIndex().getCol()).isOccupied()) {
					List<CellNode> path = getPath(controller.getGrid(), start, cell);
					if (!path.isEmpty()) {
						lastStep = Direction.DOWN;
						return cell;
					} else {
						continue;
					}
				}
			}
		}
		return objective;
	}

	public List<CellNode> emergencyTeleport(GridNode grid, CellNode start) {
		List<CellNode> path = new ArrayList<>();

		if (start.getLocation().getX() < GameSettings.WIDTH * .5) {
			log("WEST");
			LinkedList<CellNode> westBorder =  grid.getTeleportZoneWest();
			for (CellNode cell : westBorder) {
				if (!cell.isOccupied() && !grid.getCell(cell.getIndex().getRow(), grid.getColumnCount() - 1).isOccupied()) {
					distressLevel = DistressLevel.EMERGENCY;
					path = getPath(controller.getGrid(), start, cell);
					if (!path.isEmpty()) {
						lastStep = Direction.LEFT;
						return path;

					} else {
						continue;
					}
				}
			}
		}
		else if (start.getLocation().getX() > GameSettings.WIDTH * .50) {
			log("EAST");
			LinkedList<CellNode> eastBorder =  grid.getTeleportZoneEast();
			for (CellNode cell : eastBorder) {
				if (!cell.isOccupied() && cell.getLocation().getY()>=start.getLocation().getY() && !grid.getCell(cell.getIndex().getRow(), grid.getMinCol()).isOccupied()) {
					distressLevel = DistressLevel.EMERGENCY;
					path = getPath(controller.getGrid(), start, cell);
					if (!path.isEmpty()) {
						lastStep = Direction.RIGHT;
						return path;
					} else {
						continue;
					}
				}
			}
		}
		else if (start.getLocation().getY()<GameSettings.HEIGHT*.50){
			log("NORTH");
			LinkedList<CellNode> northBorder =  grid.getTeleportZoneNorth();
			for (CellNode cell : northBorder) {
				if (!cell.isOccupied() && cell.getLocation().getX()>=start.getLocation().getX() && !grid.getCell(grid.getRowCount() - 1, cell.getIndex().getCol()).isOccupied()) {
					distressLevel = DistressLevel.EMERGENCY;
					path = getPath(controller.getGrid(), start, cell);
					if (!path.isEmpty()) {
						lastStep = Direction.UP;
						return path;
					} else {
						continue;
					}
				}
			}
		}
		else if (start.getLocation().getY() > GameSettings.HEIGHT * .50) {
			log("SOUTH");
			LinkedList<CellNode> southBorder =  grid.getTeleportZoneSouth();
			for (CellNode cell : southBorder) {
				if (!cell.isOccupied() && cell.getLocation().getX()>=start.getLocation().getX()&& !grid.getCell(grid.getMinRow(), cell.getIndex().getCol()).isOccupied()) {
					distressLevel = DistressLevel.EMERGENCY;
					path = getPath(controller.getGrid(), start, cell);
					if (!path.isEmpty()) {
						lastStep = Direction.DOWN;
						return path;
					} else {
						continue;
					}
				}
			}
		}
		return path;
	}


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
	private List<CellNode> fetchSafePath(GridNode grid, CellNode start, CellNode goal, CellNode tail,DistressLevel normal) {

		List<CellNode> pathToGoal = getPath(controller.getGrid(),start,goal);

		for(CellNode cell: pathToGoal){
			cell.setCheckBlock(true);
		}

		List<CellNode> pathToTail = getPath(controller.getGrid(),goal,tail);

		for(CellNode cell: pathToGoal){
			cell.setCheckBlock(false);
		}

		if(!pathToGoal.isEmpty() && !pathToTail.isEmpty()){

			return pathToGoal;
		}
		else{

			log("Normal path to goal is empty");

			pathToGoal = getPath(controller.getGrid(),start,goal);

			for(CellNode cell: pathToGoal){
				cell.setCheckBlock(true);
			}

			pathToTail = getPath(controller.getGrid(),goal,tail);

			for(CellNode cell: pathToGoal){
				cell.setCheckBlock(false);
			}
			if(!pathToGoal.isEmpty() && !pathToTail.isEmpty()){

				return pathToGoal;
			}
			else{
				for(CellNode cell: pathToTail){
					cell.setCheckBlock(false);
				}
				return new ArrayList<>();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<CellNode> getShortestPath(List<CellNode>... arrays) {

		List<CellNode> smallest = arrays[0];

		int minSize = Integer.MAX_VALUE;

		for (int i = arrays.length - 1; i >= 0; i--) {

			if (arrays[i].size() < minSize && !arrays[i].isEmpty()) {

				minSize = arrays[i].size();
				smallest = arrays[i];
			}
		}
		return smallest;
	}

	private void showPathToObjective(List<CellNode> cells){
		setPathCoordinates(calculateDirection(cells));
		setAllowTrace(true);
		if (logDirections) {
			for (int i = cells.size() - 1; i >= 0; i--) {
				log("Direction: " + cells.get(i).getDirection().toString());
			}
			log("");
		}
	}

	private void setAllowTrace(boolean state) {
		this.allowTrace = state;
	}

	private List<CellNode> calculateDirection(List<CellNode> cells) {
		for (CellNode node : cells) {
			node.setPathCell(true);
			if (node.getParentNode() != null) {
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
		cells.get(0 ).setDirection(lastStep);
		lastStep = Direction.NONE;
		return cells;
	}
	/**
	 * TODO: Build a list containing coordinates and directions.
	 * make the snake move towards the first direction on the list
	 * if the snake moves reaches the coordinate on the list make the
	 * snake take the next turn and so forth:....
	 */
	public void steerPlayer() {
		CellNode cell = null;
		if (pathCoordinates != null && allowTrace) {
			for (int index = 0; index < pathCoordinates.size(); index++) {
				cell = pathCoordinates.get(index);
				if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
					switch (cell.getDirection()) {
					case DOWN:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_DOWN);
						cell.setPathCell(false);
						break;
					case LEFT:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_LEFT);
						cell.setPathCell(false);
						break;
					case RIGHT:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_RIGHT);
						cell.setPathCell(false);
						break;
					case UP:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_UP);
						cell.setPathCell(false);
						break;
					case NONE:
						break;
					}
					break;
				}
			}
		}
	}

	/**
	 * Method which under certain conditions will activate the speed boost of
	 * the snake
	 *
	 * @param random
	 */
	public void addRandomBoost(boolean random) {
		if (!trackingTail) {
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
	}
	private void log(String str) {
		System.out.println(str);
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
			steerPlayer();
			break;
		case FREE_MODE:
			break;
		default:
			break;
		}
	}

	private void applyThrust() {
		if (game.getEnergyBarTwo().getEnergyLevel() > 50) {
			if (snakeAI.isAllowThrust() && !snakeAI.getSpeedThrust()) {
				snakeAI.setSpeedThrust(true);
			}
		} else {
			snakeAI.setSpeedThrust(false);
		}
	}

	/**
	 * Class which holds the distance and the nearest object and the object!
	 *
	 * @author Eudy Contreras
	 *
	 */
	public class Objective implements Comparable<Objective>{

		private double x;
		private double y;
		private double normalDistance;
//		private double alternateDistance;
		private PlayerTwo snakeAI;
		private AbstractObject object;
		private CellNode cell;

		public Objective(PlayerTwo snake, AbstractObject object) {
			this.x = object.getX();
			this.y = object.getY();
			this.snakeAI = snake;
			this.object = object;
			this.cell = object.getCell();
			this.computeDistances();
		}
		private void computeDistances(){
			this.normalDistance = calculateManhathanDistance(snakeAI.getX(), object.getX(), snakeAI.getY(), object.getY());
		}
		public double getXDistance(double x){
			return Math.abs(x-this.x);
		}
		public double getYDistance(double y){
			return Math.abs(y-this.y);
		}
		public double getDistance(double x, double y){
			return Math.abs(x - this.x)+Math.abs(y - this.y);
		}
		public double getInterpolarXDistance(double x){
			double xDistance;
			if(this.x > x){
				xDistance = x + (GameSettings.WIDTH-this.x);
			}
			else{
				xDistance =this.x + (GameSettings.WIDTH-this.x);
			}
			return xDistance;
		}
		public double getInterpolarYDistance(double y){
			double yDistance;
			if(this.y > y){
				yDistance = y + (GameSettings.HEIGHT-this.y);
			}
			else{
				yDistance = this.y +(GameSettings.HEIGHT-y);
			}
			return yDistance;
		}
		public double getInterpolarDistance(double x, double y){
			double dX = getInterpolarXDistance(x);
			double dY = getInterpolarYDistance(y);

			return Math.abs(dX+dY);

		}
		public double getDistance() {
			return normalDistance;
		}

		public CellNode getCell() {
			return cell;
		}

		public AbstractObject getObject(){
			return object;
		}
		@Override
		public String toString(){
			return normalDistance+"";
		}
	    @Override
	    public boolean equals(Object obj) {
	        if (obj == null) {
	            return false;
	        }
	        if (getClass() != obj.getClass()) {
	            return false;
	        }
	        final Objective other = (Objective) obj;
	        if (this.object.getX() != other.object.getX() && this.object.getY() != other.object.getY()) {
	            return false;
	        }
	        if (this.normalDistance != other.normalDistance) {
	            return false;
	        }
	        return true;
	    }
		@Override
		public int compareTo(Objective distance) {
			return Double.compare(this.getDistance(),distance.getDistance());
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

	private void setPathCoordinates(List<CellNode> coordinates){
		this.pathCoordinates = coordinates;
	}
	public PlayerMovement getDirection() {
		return snakeAI.getCurrentDirection();
	}

	public ObjectivePosition getLocation() {
		return location;
	}

	public Rectangle2D getCollisionBounds() {
		return snakeAI.getAIBounds();
	}

	public void setLocation(ObjectivePosition location) {
		this.location = location;
	}
	private enum SearchType{
		CLOSEST_OBJECTIVE, SHORTEST_PATH;
	}
	private enum PathType{
		LONGEST_PATH,SHORTEST_PATH
	}
	private enum TieBreaker{
		PATH,CROSS, NONE
	}

	private enum ObjectivePosition {
		NORTH, SOUTH, WEST, EAST
	}

	public enum ActionType {
		FREE_MODE, STALL, FIND_PATH, DODGE_OBSTACLES
	}

	private enum HeuristicType{
		MANHATHAN,EUCLIDIAN,CUSTOM_EUCLUDIAN,
	}

	public enum DistressLevel{
		EMERGENCY,DISTRESSED,NORMAL,SAFETY_CHECK,CAUTIOUS_CHECK_EMERGENCY
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

	public class DistanceComparator implements Comparator<Objective> {
		@Override
		public int compare(Objective a, Objective b) {
			if(pathType == PathType.SHORTEST_PATH){
				return Double.compare(a.getDistance(), b.getDistance());
			}
			else{
				return Double.compare(b.getDistance(), a.getDistance());
			}
		}
	}
}
