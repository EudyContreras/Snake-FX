package com.EudyContreras.Snake.PathFindingAI;

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
public class AIPathFinder {

	private AIController controller;
	private GameManager game;
	private PlayerTwo snakeAI;
	private Random rand;

	private boolean logDirections = false;
	private boolean searching = false;
	private boolean allowTrace = false;

	private double checkTimer = 100;
	private double heuristicScale = 1;

	private int cellCount = 0;
	private int randomBoost = 200;

	private ObjectivePosition location;
	private HeuristicType heuristicType;
	private TieBreaker tieBreaker;
	private ActionState state;

	private Objective[] objectives;
	private HashSet<CellNode> closedPaths;
	private LinkedList<CellNode> totalPath;
	private PriorityQueue<CellNode> openPaths;
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
		heuristicType = HeuristicType.MANHATHAN;
		tieBreaker = TieBreaker.NONE;
		state = ActionState.FIND_PATH;
	}

	public void findObjective() {
		switch (state) {
		case DODGE_OBSTACLES:
			break;
		case FIND_PATH:
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
				findClosest();
				checkTimer --;
				if(checkTimer<=0){
					computeClosestPath(0,0);
					checkTimer = 200;
				}
			}
		}
	}

	/**
	 * Find a path from start to goal using the A* algorithm
	 */

	public List<CellNode> getPath(GridNode grid, CellNode startingPoint, CellNode objective) {

		cellCount = grid.getRowCount() * grid.getColumnCount();

		HashSet<CellNode> closedSet = new HashSet<>(cellCount);

		PriorityQueue<CellNode>openedSet = new PriorityQueue<CellNode>(cellCount, new CellComparator());

		CellNode current = null;

		double turnPenalty = 0;

		boolean containsNeighbor;

		openedSet.add(startingPoint);

//		startingPoint.setOccupied(true);

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
				return createCoordinates(objective);
			}
			closedSet.add(current);


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
//		openedSet.clear();
//		closedSet.clear();
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
	 * Create final path of the A* algorithm. The path is from goal to start.
	 */
	private List<CellNode> createCoordinates(CellNode current) {
		List<CellNode> totalPath = new LinkedList<CellNode>();

		totalPath.add(current);

		while ((current = current.getParentNode()) != null) {

			totalPath.add(current);

		}

		return totalPath;
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
		else if (state == ActionState.FIND_PATH) {
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
		case DODGE_OBSTACLES:
			computeObjective();
			break;
		case FIND_PATH:
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
		objectives = new Objective[game.getGameObjectController().getObsFruitList().size()];

		for (int i = 0; i < objectives.length  ; i++) {
			objectives[i] = new Objective(calculateManhathanDistance(
		    snakeAI.getX(), game.getGameObjectController().getObsFruitList().get(i).getX(),
			snakeAI.getY(), game.getGameObjectController().getObsFruitList().get(i).getY()),
			game.getGameObjectController().getObsFruitList().get(i));
		}

//		Arrays.sort(objectives);
//		if (objectives[0] != null && GameSettings.DEBUG_MODE) {
//			objectives[0].getObject().blowUpAlt();
//		}

	}
	/**
	 * TODO: Perform a check to see if a path is safe by checking if the is a path to the objective and 
	 * a path from the objective to the tail of the snake!
	 * @param row
	 * @param col
	 */
//	@SuppressWarnings("unchecked")
	public void computeClosestPath(int row, int col){
		Arrays.sort(objectives);
		
		controller.getGrid().resetCells();
		
		CellNode start = controller.getRelativeCell(snakeAI, 0, 0);
		CellNode goal = objectives[0].getCell();
		
		List<CellNode> path = new ArrayList<>();
		
		if(start!=null && goal!=null){
			path = getPath(controller.getGrid(),start ,goal);
		}
		if(!path.isEmpty()) {
			showPathToObjective(path); 
		}
		else {
			log("Normal path one empty!");
			
			if (controller.getGrid().getTailCell() != null) {
				
				start = controller.getRelativeCell(snakeAI, 0, 0);
				goal = controller.getGrid().getTailCell();
				
				if(start!=null && goal!=null){
					path = getPath(controller.getGrid(),start ,goal);
				}
				if (!path.isEmpty()) {
					showPathToObjective(path);
				}
				else{
					log("Alternate path one empty!");
				}
			}
		}
	

//		List<CellNode> path1 = getPath(controller.getGrid(), controller.getRelativeCell(snakeAI, 0, 0),
//				objectives[0].getCell());
//		List<CellNode> path2 = getPath(controller.getGrid(), controller.getRelativeCell(snakeAI, 0, 0),
//				objectives[1].getCell());
//		List<CellNode> path3 = getPath(controller.getGrid(), controller.getRelativeCell(snakeAI, 0, 0),
//				objectives[2].getCell());
//		List<CellNode> path4 = getPath(controller.getGrid(), controller.getRelativeCell(snakeAI, 0, 0),
//				objectives[3].getCell());
//
//		List<CellNode> newPath = getShortestPath(path1,path2,path3,path4);
//
//		if(!newPath.isEmpty()){
//			showPathToObjective(newPath);
//		}
	}

	@SuppressWarnings("unchecked")
	public List<CellNode> getShortestPath(List<CellNode>... arrays) {
		List<CellNode> smallest = arrays[0];
		int min = Integer.MAX_VALUE;
		for (int i = arrays.length - 1; i >= 0; i--) {
			int length = arrays[i].size();
			if (length < min && !arrays[i].isEmpty()) {
				min = length;
				smallest = arrays[i];
			}
		}
		return smallest;
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
			return Double.compare(this.getDistance(),distance.getDistance());
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
		FREE_MODE, STALL, FIND_PATH, DODGE_OBSTACLES
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
	/**
	 * Get the cell with the minimum f value.
	 */
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


}
