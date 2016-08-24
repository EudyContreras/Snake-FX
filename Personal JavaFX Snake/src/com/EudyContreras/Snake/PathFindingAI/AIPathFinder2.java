package com.EudyContreras.Snake.PathFindingAI;

import java.util.ArrayList;
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
 *
 */
public class AIPathFinder2 {

	private AbstractObject objective;
	private AIController controller;
	private GameManager game;
	private PlayerTwo snakeAI;
	private Random rand;

	private boolean searching = false;
	private boolean makingUTurn = false;

	private double range = 20;
	private double closest;
	private double positionX = 0;
	private double positionY = 0;
	private double turnOffset = 100;
	private double heuristicScale = 1.0;

	private int cellCount = 0;
	private int randomBoost = 200;

	private ObjectivePosition location;
	private HeuristicType heuristicType;
	private ActionState state;

	HashSet<CellNode> closedSet;
	LinkedList<CellNode> totalPath;
	PriorityQueue<CellNode> openedSet;

	public AIPathFinder2(GameManager game, PlayerTwo snakeAI) {
		this.game = game;
		this.snakeAI = snakeAI;
		this.initialize();
	}

	public AIPathFinder2(GameManager game, AIController controller, PlayerTwo snakeAI, LinkedList<CollideNode> possibleColliders) {
		this.game = game;
		this.controller = controller;
		this.snakeAI = snakeAI;
		this.initialize();
	}

	public void initialize() {
		rand = new Random();
		cellCount = controller.getGrid().getRowCount() * controller.getGrid().getColumnCount();
		totalPath = new LinkedList<>();
		closedSet = new HashSet<>(cellCount);
		openedSet = new PriorityQueue<CellNode>(cellCount, new CellComparator());
		heuristicType = HeuristicType.MANHATHAN;
		state = ActionState.FREE_MODE;
	}

	public void findObjective() {
		switch (state) {
		case EVADING:
			break;
		case PATH_FINDING:
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
		}
	}

	/*
	 * this method gets called from the game loop and it is called at 60fps. The
	 * method update and keeps track of things
	 */
	public void updateSimulation() {
//		finderCell = controller.getRelativeCell(snakeAI.getBounds(),0,0);
		if (game.getModeID() == GameModeID.LocalMultiplayer) {
			if (game.getStateID() == GameStateID.GAMEPLAY) {
				if (objective != null) {
					checkCurrentLocation();
					addRandomBoost(true);
					reRoute();
				}
//				findClosest();
				checkObjectiveStatus();
//				computeClosestPath(0,0);
			}
		}
	}

	/**
	 * Find a path from start to goal using the A* algorithm
	 */

	public List<CellNode> getPath(GridNode grid, CellNode startingPoint, CellNode objective) {

		CellNode current = null;

		double turnPenalty = 0;

		boolean containsNeighbor;

		openedSet.add(startingPoint);

		startingPoint.setOccupied(true);

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
				return createPath(objective);
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

					neighbor.setHeuristic(heuristicCostEstimate(neighbor, objective,2.0,heuristicType)); // If used with scaled up heuristic it gives least number of turns!

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
		openedSet.clear();
		closedSet.clear();
		totalPath.clear();
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
	/**
	 * Create final path of the A* algorithm. The path is from goal to start.
	 */
	private List<CellNode> createPath(CellNode current) {

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
				if (game.getEnergyBarTwo().getEnergyLevel() > 50) {
					if (snakeAI.isAllowThrust()) {
						snakeAI.setSpeedThrust(true);
					}
				} else {
					snakeAI.setSpeedThrust(false);
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
	public AbstractObject findClosest() {
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

		return objective;
	}

	private void computeObjective() {
		Distance[] distance = new Distance[game.getGameObjectController().getFruitList().size()];

		for (int i = 0; i < game.getGameObjectController().getFruitList().size(); i++) {
			distance[i] = new Distance(calculateManhathanDistance(
		    snakeAI.getX(), game.getGameObjectController().getFruitList().get(i).getX(),
			snakeAI.getY(), game.getGameObjectController().getFruitList().get(i).getY()),
			game.getGameObjectController().getFruitList().get(i));
		}

		if (distance.length > 0) {
			closest = distance[0].getDistance();
		}

		for (int i = 0; i < distance.length; i++) {
			if (distance[i].getDistance() < closest) {
				closest = distance[i].getDistance();
			}
		}
		for (int i = 0; i < distance.length; i++) {
			if (distance[i].getDistance() == closest) {
				if (distance[i].getObject().isAlive()) {
					objective = distance[i].getObject();
					positionX = distance[i].getObject().getX();
					positionY = distance[i].getObject().getY();
				}
			}
		}

		if (objective != null && GameSettings.DEBUG_MODE) {
			objective.blowUpAlt();
		}

	}
	public void computeClosestPath(int row, int col){
		controller.getGrid().resetCells();
		if(objective.getCell()!=null){
			//System.out.println();
			showPathToObjective(getPath(controller.getGrid(),controller.getRelativeCell(snakeAI,0,0),objective.getCell())); //controller.getGrid().getCell(45, 20)
		}
	}
	private void showPathToObjective(List<CellNode> cells){
		for(CellNode cell: cells){
			calculateDirection(cell);
			cell.setPathCell(true);
		}
		for(int i = cells.size()-1; i>=0; i--){
			System.out.println("Direction: "+cells.get(i).getDirection().toString());
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

	@SuppressWarnings("unused")
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
		if (Math.abs(snakeAI.getX() - objective.getX()) < Math.abs(snakeAI.getY() - objective.getY())) {
			if (objective.getY() > snakeAI.getY()) {
				if (objective.getYDistance(snakeAI.getY()) > GameSettings.HEIGHT * .45) {
					location = ObjectivePosition.SOUTH;
					performMove(PlayerMovement.MOVE_UP);
				} else {
					location = ObjectivePosition.SOUTH;
					performMove(PlayerMovement.MOVE_DOWN);
				}
			} else {
				if (objective.getYDistance(snakeAI.getY()) > GameSettings.HEIGHT * .45) {
					location = ObjectivePosition.NORTH;
					performMove(PlayerMovement.MOVE_DOWN);
				} else {
					location = ObjectivePosition.NORTH;
					performMove(PlayerMovement.MOVE_UP);
				}
			}
		} else {
			if (objective.getX() > snakeAI.getX()) {
				if (objective.getXDistance(snakeAI.getX()) > GameSettings.WIDTH * .45) {
					location = ObjectivePosition.EAST;
					performMove(PlayerMovement.MOVE_LEFT);
				} else {
					location = ObjectivePosition.EAST;
					performMove(PlayerMovement.MOVE_RIGHT);
				}
			} else {
				if (objective.getXDistance(snakeAI.getX()) > GameSettings.WIDTH * .45) {
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
	 * and the objective! if the snake is within a predetermined threshold the
	 * snake will perform the appropriate turn in order to collect the
	 * objective!
	 */
	private void checkCurrentLocation() {
		switch (state) {
		case EVADING:
			break;
		case PATH_FINDING:
			break;
		case FREE_MODE:
			computeTrackingManeuver();
			break;
		default:
			break;

		}

	}

	private void computeTrackingManeuver() {
		if (snakeAI.getX() > objective.getX() - range && snakeAI.getX() < objective.getX() + range) {
			if (objective.getY() < snakeAI.getY()) {
				snakeAI.setDirection(PlayerMovement.MOVE_UP);
				applyThrust();
			} else {
				snakeAI.setDirection(PlayerMovement.MOVE_DOWN);
				applyThrust();
			}
		} else if (snakeAI.getY() > objective.getY() - range && snakeAI.getY() < objective.getY() + range) {
			if (objective.getX() < snakeAI.getX()) {
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
	 * Method which checks the status of the current objective and base on the
	 * objective's status it will try to re-determine a new objective once the
	 * current objective has been collected or it has moved!
	 */
	private void checkObjectiveStatus() {
		switch (state) {
		case EVADING:
			break;
		case PATH_FINDING:
			break;
		case FREE_MODE:
			if (objective.isRemovable()) {
				findClosest();
				createPath();
			}
			if (objective.getX() != positionX || objective.getY() != positionY) {
				findClosest();
			}
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
	 * objective! once the turn is made the path will be recalculated by the
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
			if (objective.getX() < snakeAI.getX()) {
				snakeAI.setDirection(PlayerMovement.MOVE_LEFT);
				makingUTurn = true;
				turnOffset = snakeAI.getRadius() * 2;
			} else {
				snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);
				makingUTurn = true;
				turnOffset = snakeAI.getRadius() * 2;
			}
		} else if (currentDirection == PlayerMovement.MOVE_RIGHT || currentDirection == PlayerMovement.MOVE_LEFT) {
			if (objective.getY() < snakeAI.getY()) {
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
	private class Distance {

		private Double distance;
		private AbstractObject object;

		public Distance(double distance, AbstractObject object) {
			this.distance = distance;
			this.object = object;
		}

		public double getDistance() {
			return distance;
		}

		public AbstractObject getObject() {
			return object;
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
		double distance = .0;
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
}
