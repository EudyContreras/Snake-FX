package com.EudyContreras.Snake.PathFindingAI;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
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
public class AIPathFinder {

//	private AbstractObject objectives[0].getObject();
	private AIController controller;
	private GameManager game;
	private PlayerTwo snakeAI;
	private Random rand;

	private boolean logDirections = false;
	private boolean searching = false;
	private boolean allowTrace = false;
	private boolean makingUTurn = false;

	private double range = 20;
	private double turnOffset = 100;
	private double checkTimer = 100;
	private double heuristicScale = 1;

	private int cellCount = 0;
	private int randomBoost = 200;

	private ObjectivePosition location;
	private HeuristicType heuristicType;
	private TieBreaker tieBreaker;
	private ActionState state;

	private Objective[] objectives;
	private HashSet<CellNode> closedSet;
	private LinkedPath<CellNode> linkedPath;
	private PriorityQueue<CellNode> openedSet;
	private List<CellNode> pathCoordinates;

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
		linkedPath = new LinkedPath<>();
		closedSet = new HashSet<>(cellCount);
		objectives = new Objective[4];
		openedSet = new PriorityQueue<CellNode>(cellCount, new CellComparator());
		heuristicType = HeuristicType.MANHATHAN;
		tieBreaker = TieBreaker.CROSS;
		state = ActionState.PATH_FINDING;
	}

	public void findObjective() {
		switch (state) {
		case EVADING:
			break;
		case PATH_FINDING:
				computeClosestPath(0,0);
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
			findClosest();
			createPath();
			computeClosestPath(5,5);
			performMove(PlayerMovement.MOVE_DOWN);
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
					checkObjectiveStatus();
					addRandomBoost(true);
					reRoute();

				findClosest();
				checkTimer --;
				if(checkTimer<=0){
//					computeClosestPath(0,0);
					checkTimer = 60;
				}
			}
		}
	}

	/**
	 * Find a path from start to goal using the A* algorithm
	 */

	public synchronized LinkedPath<CellNode> getPath(GridNode grid, CellNode startingPoint, CellNode objective) {

		CellNode current = null;

		double turnPenalty = 0;

		boolean containsNeighbor;

		open(startingPoint);

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
				endPathSearch();
				return createNewPath(objective);
			}
			close(current);


			for (CellNode neighbor : grid.getNeighborCells(current)) {

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

					switch (tieBreaker) {

					case CROSS:
						heuristic += cross * 0.001;
						break;
					case PATH:
						heuristic *= (1.0 + path);
						break;
					case NONE:
						heuristic = heuristic * heuristicScale;
						break;
					}
					heuristic =heuristicCostEstimate(neighbor, objective,2.0,heuristicType);

					neighbor.setHeuristic(heuristic); // If used with scaled up heuristic it gives least number of turns!

					neighbor.setTotalCost(neighbor.getMovementCost() + neighbor.getHeuristic());

					if (!containsNeighbor) {

						open(neighbor);
					}
				}
			}
		}
		endPathSearch();
		return new LinkedPath<CellNode>();
	}
	private void close(CellNode node){
		node.setClosed(true);
		closedSet.add(node);
	}
	private void open(CellNode node){
		node.setClosed(false);
		openedSet.add(node);
	}

	private void endPathSearch(){
		searching = false;
		allowTrace = false;
		openedSet.clear();
		closedSet.clear();
		linkedPath.clear();
	}

	private void calculateDirection(CellNode node) {
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
    /**
     * returns the node with the lowest fCosts.
     *
     * @return
     */
    @SuppressWarnings("unused")
	private CellNode getCheapestCellNode() {

        CellNode cheapest = openedSet.poll();

        for(Iterator<CellNode> cells = openedSet.iterator(); cells.hasNext();){
			CellNode cell = cells.next();
			if(cell.getMovementCost()<cheapest.getMovementCost()){
				cheapest = cell;
			}
			else{
				return cheapest;
			}

		}
        return cheapest;
    }
	private LinkedPath<CellNode> createNewPath(CellNode current) {

		linkedPath.add(current);
		while ((current = current.getParentNode()) != null) {
			linkedPath.add(current);
		}

		return linkedPath;
	}
	/**
	 * Method which under certain conditions will activate the speed boost of
	 * the snake
	 *
	 * @param random
	 */
	public void addRandomBoost(boolean random) {
		if (state == ActionState.FREE_MODE) {
			if (random && rand.nextInt(randomBoost) != 0) {
				return;
			}
			if (snakeAI != null) {
				applyThrust();
			}
		}
		else if (state == ActionState.PATH_FINDING) {
			if (random && rand.nextInt(randomBoost) != 0) {
				return;
			}
			if (snakeAI != null) {
				applyThrust();
			}
		}
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
	 * Method which when called will attempt to find the apple which is closest
	 * to the current position of the snake!
	 *
	 * @return
	 */
	public void findClosest() {
		switch (state) {
		case EVADING:
			computeObjective();
			break;
		case PATH_FINDING:
			computeObjective();
			break;
		case FREE_MODE:
			computeObjective();
			break;
		default:
			break;
		}
	}

	private void computeObjective() {
		for (int i = 0; i < objectives.length  ; i++) {
			objectives[i] = new Objective(calculateManhathanDistance(
		    snakeAI.getX(), game.getGameObjectController().getObsFruitList().get(i).getX(),
			snakeAI.getY(), game.getGameObjectController().getObsFruitList().get(i).getY()),
			game.getGameObjectController().getObsFruitList().get(i));
		}
		Arrays.sort(objectives);

	}


	@SuppressWarnings("unchecked")
	public void computeClosestPath(int row, int col) {

//
//		if (objectives[0].getObject() != null && GameSettings.DEBUG_MODE) {
//			objectives[0].getObject().blowUpAlt();
//		}

		controller.getGrid().resetCells();

		LinkedPath<CellNode> path1 = getPath(controller.getGrid(), controller.getRelativeCell(snakeAI, 0, 0),
				objectives[0].getCell());
		LinkedPath<CellNode> path2 = getPath(controller.getGrid(), controller.getRelativeCell(snakeAI, 0, 0),
				objectives[1].getCell());
		LinkedPath<CellNode> path3 = getPath(controller.getGrid(), controller.getRelativeCell(snakeAI, 0, 0),
				objectives[2].getCell());
		LinkedPath<CellNode> path4 = getPath(controller.getGrid(), controller.getRelativeCell(snakeAI, 0, 0),
				objectives[3].getCell());



			log("Path length " + objectives[0].getDistance());
			log("Path length " + objectives[1].getDistance());
			log("Path length " + objectives[2].getDistance());
			log("Path length " + objectives[3].getDistance());
			log("Path length " + path1.size());
			log("Path length " + path2.size());
			log("Path length " + path3.size());
			log("Path length " + path4.size());
			log("");


//		LinkedPath[] paths = {path1,path2,path3,path4};
//		if(!path1.isEmpty() && path1!=null)
//			paths.addAll(path1,path2,path3,path4);
//		if(!path2.isEmpty() && path2!=null)
//			paths.add(path2);
//		if(!path3.isEmpty() && path3!=null)
//			paths.put(path3.size(), path3);
//		if(!path4.isEmpty() && path3!=null)
//			paths.put(path4.size(), path4);


//		shortestPath = paths[0];
////		farthestPath = Integer.MIN_VALUE;
//
//		    if(paths[1].size()<shortestPath.size())
//		    	shortestPath = paths[1];
//		    if(paths[2].size()<shortestPath.size())
//		    	shortestPath = paths[2];
//		    if(paths[3].size()<shortestPath.size())
//		    	shortestPath = paths[3];
//

		showPathToObjective(shortestPath(path1,path2,path3,path4));
//		if(paths.get(shortestPath)!=null){
//			showPathToObjective(paths.get(shortestPath));
//		}
//		else{
//			if(paths.get(farthestPath)!=null){
//			showPathToObjective(paths.get(farthestPath));
//			}
//		}

//		List<CellNode> path = paths.get(shortestPath);
//
//		if (!path1.isEmpty()) {
//			showPathToObjective(path1);
//			log("1 not empty");
//		} else {
//			if (!path2.isEmpty()) {
//				showPathToObjective(path2);
//				log("2 not empty");
//			} else {
//				if (!path3.isEmpty()) {
//					showPathToObjective(path3);
//					log("3 not empty");
//				} else {
//					if (!path4.isEmpty()) {
//						showPathToObjective(path4);
//						log("4 not empty");
//					}
//				}
//			}
//		}
	}
	@SuppressWarnings("unchecked")
	public LinkedPath<CellNode> shortestPath(LinkedPath<CellNode> ... arrays){
//		Arrays.sort(arrays);
//		  LinkedPath<CellNode> smallest = arrays[0];
//		  int min = Integer.MAX_VALUE;
//		  for(int i = arrays.length-1; i >= 0; i--) {
//		    int length = arrays[i].size();
//		    if (length < min) {
//		      min = length;
//		      smallest = arrays[i];
//		    }
//		  }
//		for(int i = 0; i<arrays.length; i++){
//			log("Path length " + arrays[i].size());
//		}
//		log("");
		  return  arrays[3];
	}
	private void showPathToObjective(List<CellNode> cells){
		for(CellNode cell: cells){
			calculateDirection(cell);
			cell.setPathCell(true);
		}
		setPathCoordinates(cells);
		allowTrace = true;

		if (logDirections) {
			for (int i = cells.size() - 1; i >= 0; i--) {
				log("Direction: " + cells.get(i).getDirection().toString());
			}
		}
	}
	/**
	 * Method which gets called in the update method and will create a new path
	 * after the snake has perform a uTurn!
	 */
	private void reRoute() {
		if (makingUTurn) {
			turnOffset--;
			if (turnOffset <= 0) {
				makingUTurn = false;
				createPath();
			}
		}
	}

	private void log(String str) {
		System.out.println(str);
	}

	/*
	 * Method which attempts to determine the best course of action in order to
	 * move towards the objectives[0].getObject()! The method will first check if the x distance
	 * is less or greater than the y distance and based on that it will decide
	 * to perform a horizontal or vertical move. if the method to be perform is
	 * a vertical move the method will check if the objectives[0].getObject() is above or below
	 * and then perform a move based on the objectives coordinates!
	 */
	private void createPath() {
		switch (state) {
		case EVADING:
			break;
		case PATH_FINDING:
			break;
		case FREE_MODE:
			computeTrackingPath();
			break;
		default:
			break;

		}

	}

	private void computeTrackingPath() {
		if (Math.abs(snakeAI.getX() - objectives[0].getObject().getX()) < Math.abs(snakeAI.getY() - objectives[0].getObject().getY())) {
			if (objectives[0].getObject().getY() > snakeAI.getY()) {
				if (objectives[0].getObject().getYDistance(snakeAI.getY()) > GameSettings.HEIGHT * .45) {
					location = ObjectivePosition.SOUTH;
					performMove(PlayerMovement.MOVE_UP);
				} else {
					location = ObjectivePosition.SOUTH;
					performMove(PlayerMovement.MOVE_DOWN);
				}
			} else {
				if (objectives[0].getObject().getYDistance(snakeAI.getY()) > GameSettings.HEIGHT * .45) {
					location = ObjectivePosition.NORTH;
					performMove(PlayerMovement.MOVE_DOWN);
				} else {
					location = ObjectivePosition.NORTH;
					performMove(PlayerMovement.MOVE_UP);
				}
			}
		} else {
			if (objectives[0].getObject().getX() > snakeAI.getX()) {
				if (objectives[0].getObject().getXDistance(snakeAI.getX()) > GameSettings.WIDTH * .45) {
					location = ObjectivePosition.EAST;
					performMove(PlayerMovement.MOVE_LEFT);
				} else {
					location = ObjectivePosition.EAST;
					performMove(PlayerMovement.MOVE_RIGHT);
				}
			} else {
				if (objectives[0].getObject().getXDistance(snakeAI.getX()) > GameSettings.WIDTH * .45) {
					location = ObjectivePosition.WEST;
					performMove(PlayerMovement.MOVE_RIGHT);
				} else {
					location = ObjectivePosition.WEST;
					performMove(PlayerMovement.MOVE_LEFT);
				}
			}
		}
	}

	/**
	 * Method which performs actions based on the current location of the snake
	 * and the objectives[0].getObject()! if the snake is within a predetermined threshold the
	 * snake will perform the appropriate turn in order to collect the
	 * objectives[0].getObject()!
	 */
	private void performLocationBasedAction() {
		switch (state) {
		case EVADING:
			break;
		case PATH_FINDING:
			steerPlayer();
			break;
		case FREE_MODE:
			computeTrackingManeuver();
			break;
		default:
			break;
		}
	}

	private void computeTrackingManeuver() {
		if (snakeAI.getX() > objectives[0].getObject().getX() - range && snakeAI.getX() < objectives[0].getObject().getX() + range) {
			if (objectives[0].getObject().getY() < snakeAI.getY()) {
				snakeAI.setDirection(PlayerMovement.MOVE_UP);
				applyThrust();
			} else {
				snakeAI.setDirection(PlayerMovement.MOVE_DOWN);
				applyThrust();
			}
		} else if (snakeAI.getY() > objectives[0].getObject().getY() - range && snakeAI.getY() < objectives[0].getObject().getY() + range) {
			if (objectives[0].getObject().getX() < snakeAI.getX()) {
				snakeAI.setDirection(PlayerMovement.MOVE_LEFT);
				applyThrust();
			} else {
				snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);
				applyThrust();
			}
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
	 * Method which checks the status of the current objectives[0].getObject() and base on the
	 * objectives[0].getObject()'s status it will try to re-determine a new objectives[0].getObject() once the
	 * current objectives[0].getObject() has been collected or it has moved!
	 */
	private void checkObjectiveStatus() {
		switch (state) {
		case EVADING:
			break;
		case PATH_FINDING:
			break;
		case FREE_MODE:
			if (objectives[0].getObject().isRemovable()) {
				findClosest();
				createPath();
			}
//			if (objectives[0].getObject().getX() != positionX || objectives[0].getObject().getY() != positionY) {
//				findClosest();
//			}
			break;
		default:
			break;

		}

	}

	/**
	 * Method which when called will determine if the snake has to make an
	 * u-turn or if the snake can perform the desired turn without
	 * complications! NEEDS ANALYSIS!!!!
	 *
	 * @param move:
	 *            Move which the AI desires to perform
	 */
	public void performMove(PlayerMovement move) {
		switch (state) {
		case EVADING:
			break;
		case PATH_FINDING:
			computeTrackingDirection(move);
			break;
		case FREE_MODE:
			computeTrackingDirection(move);
			break;
		default:
			break;

		}
	}

	private void computeTrackingDirection(PlayerMovement move) {
		if (move == PlayerMovement.MOVE_UP && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_DOWN) {
			makeUTurn(snakeAI.getCurrentDirection());
		} else if (move == PlayerMovement.MOVE_DOWN && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_UP) {
			makeUTurn(snakeAI.getCurrentDirection());
		} else if (move == PlayerMovement.MOVE_LEFT && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_RIGHT) {
			makeUTurn(snakeAI.getCurrentDirection());
		} else if (move == PlayerMovement.MOVE_RIGHT && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_LEFT) {
			makeUTurn(snakeAI.getCurrentDirection());
		} else {
			snakeAI.setDirection(move);
		}
	}

	/**
	 * Method which when called will perform a turn based on the location of the
	 * objectives[0].getObject()! once the turn is made the path will be recalculated by the
	 * reRoute method! The method only gets called when the snake attempts to
	 * perform an illegal turn!
	 *
	 * @param currentDirection
	 */

	private void makeUTurn(PlayerMovement currentDirection) {
		switch (state) {
		case EVADING:
			break;
		case PATH_FINDING:
			break;
		case FREE_MODE:
			computeTrackingUTurn(currentDirection);
			break;
		default:
			break;

		}

	}

	private void computeTrackingUTurn(PlayerMovement currentDirection) {
		if (currentDirection == PlayerMovement.MOVE_DOWN || currentDirection == PlayerMovement.MOVE_UP) {
			if (objectives[0].getObject().getX() < snakeAI.getX()) {
				snakeAI.setDirection(PlayerMovement.MOVE_LEFT);
				makingUTurn = true;
				turnOffset = snakeAI.getRadius() * 2;
			} else {
				snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);
				makingUTurn = true;
				turnOffset = snakeAI.getRadius() * 2;
			}
		} else if (currentDirection == PlayerMovement.MOVE_RIGHT || currentDirection == PlayerMovement.MOVE_LEFT) {
			if (objectives[0].getObject().getY() < snakeAI.getY()) {
				snakeAI.setDirection(PlayerMovement.MOVE_UP);
				makingUTurn = true;
				turnOffset = snakeAI.getRadius() * 2;
			} else {
				snakeAI.setDirection(PlayerMovement.MOVE_DOWN);
				makingUTurn = true;
				turnOffset = snakeAI.getRadius() * 2;
			}
		}
	}

	/**
	 * Class which holds the distance and the nearest object and the object!
	 *
	 * @author Eudy Contreras
	 *
	 */
	private class Objective implements Comparable<Objective>{

		private Double distance;
		private AbstractObject object;
		private CellNode cell;

		public Objective(double distance, AbstractObject object) {
			this.distance = distance;
			this.object = object;
			this.cell = object.getCell();
		}

		public double getDistance() {
			return distance;
		}

		public CellNode getCell() {
			return cell;
		}

		public AbstractObject getObject(){
			return object;
		}
		@Override
		public String toString(){
			return distance+"";
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
	        if (this.distance != other.distance) {
	            return false;
	        }
	        return true;
	    }
		@Override
		public int compareTo(Objective distance) {
			return Double.compare(distance.getDistance(),this.getDistance());
		}
	}
	public class LinkedPath<T> extends LinkedList<CellNode> implements Comparable<LinkedPath<CellNode>>{

		private static final long serialVersionUID = 1L;
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			@SuppressWarnings("unchecked")
			final LinkedPath<CellNode> other = (LinkedPath<CellNode>) obj;

			if (this.size() != other.size()) {
				return false;
			}
			return true;
		}

		@Override
		public int compareTo(LinkedPath<CellNode> other) {
			return Integer.compare(other.size(),this.size());
		}
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
	private enum TieBreaker{
		PATH,CROSS, NONE
	}

	private enum ObjectivePosition {
		NORTH, SOUTH, WEST, EAST
	}

	public enum ActionState {
		FREE_MODE, EVADING, PATH_FINDING,
	}

	private enum HeuristicType{
		MANHATHAN,EUCLIDIAN,CUSTOM_EUCLUDIAN,
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
			return Double.compare(a.getTotalCost(), b.getTotalCost());
		}
	}

	public class DistanceComparator implements Comparator<Objective> {
		@Override
		public int compare(Objective a, Objective b) {
			return Double.compare(a.getDistance(), b.getDistance());
		}
	}

	public class LinkedPathComparator implements Comparator<LinkedPath<CellNode>> {
		@Override
		public int compare(LinkedPath<CellNode> a, LinkedPath<CellNode> b) {
			return Integer.compare(a.size(), b.size());
		}
	}
}
