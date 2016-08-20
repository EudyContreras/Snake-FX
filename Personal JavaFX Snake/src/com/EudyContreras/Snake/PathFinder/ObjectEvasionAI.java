package com.EudyContreras.Snake.PathFinder;

import java.util.Random;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

/**

 * @author Eudy Contreras
 *
 */
public class ObjectEvasionAI {

	private AbstractObject closestObjective;
	private AbstractTile obstacle;
	private GameManager game;
	private PlayerTwo snakeAI;
	private Random rand;

	private boolean makingUTurn = false;

	private double closest;
	private double positionX = 0;
	private double positionY = 0;
	private double turnOffset = 100;

	private int randomBoost = 200;

	private ObjectivePosition location;
	private ActionState state;

	public ObjectEvasionAI(GameManager game, PlayerTwo snakeAI) {
		this.game = game;
		this.snakeAI = snakeAI;
		this.initialize();
	}

	public void initialize() {
		rand = new Random();
		state = ActionState.TRACKING;
	}

	public void findObjective(ActionState state) {
		switch(state){
		case AVOIDING:
			break;
		case FINDING:
			break;
		case TRACKING:
			if (game.getModeID() == GameModeID.LocalMultiplayer && GameSettings.ALLOW_AI_CONTROLL)
				createPath();
			break;
		default:
			break;

		}

	}
	/*
	 * Gets called when the game begins in order
	 * to initiate the simulation
	 */
	public void startSimulation() {
		if (game.getModeID() == GameModeID.LocalMultiplayer && GameSettings.ALLOW_AI_CONTROLL)
			findClosest();
			createPath();
	}
	/*
	 * this method gets called from the game loop and it
	 * is called at 60fps. The method update and keeps track
	 * of things
	 */
	public void updateSimulation() {
		if (game.getModeID() == GameModeID.LocalMultiplayer) {
			if (game.getStateID() == GameStateID.GAMEPLAY && closestObjective != null) {
				checkCurrentLocation();
				checkObjectiveStatus();
				addRandomBoost(true);
				reRoute();
			}
		}
	}
	/**
	 * Method which under certain conditions will activate
	 * the speed boost of the snake
	 * @param random
	 */
	public void addRandomBoost(boolean random) {
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
	/**
	 * Method which when called will attempt to find the apple which is closest
	 * to the current position of the snake!
	 * @return
	 */
	public AbstractObject findClosest() {
		switch(state){
		case AVOIDING:
			break;
		case FINDING:
			break;
		case TRACKING:
			Distance[] distance = new Distance[game.getGameObjectController().getFruitList().size()];

			for (int i = 0; i < game.getGameObjectController().getFruitList().size(); i++) {
			//	distance[i] = new Distance(Math.hypot(snakeAI.getX() - game.getGameObjectController().getFruitList().get(i).getX(),snakeAI.getY() - game.getGameObjectController().getFruitList().get(i).getY()),game.getGameObjectController().getFruitList().get(i));
				distance[i] = new Distance(Math.abs(snakeAI.getX()-game.getGameObjectController().getFruitList().get(i).getX()) + Math.abs(snakeAI.getY()- game.getGameObjectController().getFruitList().get(i).getY()),game.getGameObjectController().getFruitList().get(i));
			}

			if(distance.length>0){
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
						closestObjective = distance[i].getObject();
						positionX = distance[i].getObject().getX();
						positionY = distance[i].getObject().getY();
					}
				}
			}
			if (closestObjective != null && !GameSettings.DEBUG_MODE){
				closestObjective.blowUpAlt();
			}
			break;
		default:
			break;

		}

		return closestObjective;
	}
	/**
	 * Method which gets called in the update method and
	 * will create a new path after the snake has perform
	 * a uTurn!
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
	 * Method which attempts to determine the best course of action in order
	 * to move towards the objective! The method will first check if the x distance
	 * is less or greater than the y distance and based on that it will decide to perform
	 * a horizontal or vertical move.  if the method to be perform is a vertical move
	 * the method will check if the objective is above or below and then perform a move
	 * based on the objectives coordinates!
	 */
	private void createPath() {
		switch(state){
		case AVOIDING:
			break;
		case FINDING:
			break;
		case TRACKING:
			if (Math.abs(snakeAI.getX() - closestObjective.getX()) < Math.abs(snakeAI.getY() - closestObjective.getY())) {
				if (closestObjective.getY() > snakeAI.getY()) {
					if(closestObjective.getYDistance(snakeAI.getY())>GameSettings.HEIGHT*.45){
						location = ObjectivePosition.SOUTH;
						performMove(PlayerMovement.MOVE_UP);
					}
					else{
						location = ObjectivePosition.SOUTH;
						performMove(PlayerMovement.MOVE_DOWN);
					}
				}
				else{
					if(closestObjective.getYDistance(snakeAI.getY())>GameSettings.HEIGHT*.45){
						location = ObjectivePosition.NORTH;
						performMove(PlayerMovement.MOVE_DOWN);
					}
					else{
						location = ObjectivePosition.NORTH;
						performMove(PlayerMovement.MOVE_UP);
					}
				}
			} else {
				if (closestObjective.getX() > snakeAI.getX()) {
					if(closestObjective.getXDistance(snakeAI.getX())>GameSettings.WIDTH*.45){
						location = ObjectivePosition.EAST;
						performMove(PlayerMovement.MOVE_LEFT);
					}
					else{
						location = ObjectivePosition.EAST;
						performMove(PlayerMovement.MOVE_RIGHT);
					}
				}
				else{
					if(closestObjective.getXDistance(snakeAI.getX())>GameSettings.WIDTH*.45){
						location = ObjectivePosition.WEST;
						performMove(PlayerMovement.MOVE_RIGHT);
					}
					else{
						location = ObjectivePosition.WEST;
						performMove(PlayerMovement.MOVE_LEFT);
					}
				}
			}

			break;
		default:
			break;

		}

	}
	/**BUGGGY!!
	 *
	 * This method attempts to determine a course of action once a special bound has intersected any
	 * obstacle! the method attempts to determine what action the snake should take base on the position
	 * of the snake relative to the coordinates and dimensions of the obstacle encountered! The method does
	 * not work as intended. NEED HELP =(
	 *
	 * @param obstacle
	 */
	public void avoidObstacle(AbstractTile obstacle) {
		this.obstacle = obstacle;
//		this.state = ActionState.AVOIDING;
		if (game.getModeID() == GameModeID.LocalMultiplayer && game.getStateID() == GameStateID.GAMEPLAY) {
			switch (snakeAI.getCurrentDirection()) {
			case MOVE_DOWN:
				if (closestObjective.getX()<snakeAI.getX()){
					snakeAI.setDirection(PlayerMovement.MOVE_LEFT);
				}
				else{
					snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);
				}
					break;
			case MOVE_LEFT:
				if(closestObjective.getY()<snakeAI.getY()){
					snakeAI.setDirection(PlayerMovement.MOVE_UP);
				}
				else{
					snakeAI.setDirection(PlayerMovement.MOVE_DOWN);
				}
				break;
			case MOVE_RIGHT:
				if(closestObjective.getY()<snakeAI.getY()){
					snakeAI.setDirection(PlayerMovement.MOVE_UP);
				}
				else{
					snakeAI.setDirection(PlayerMovement.MOVE_DOWN);
				}
				break;
			case MOVE_UP:
				if (closestObjective.getX()<snakeAI.getX()){
					snakeAI.setDirection(PlayerMovement.MOVE_LEFT);
				}
				else{
					snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);
				}
				break;
			case STANDING_STILL:
				break;
			default:
				break;

			}
		}
	}

	/**
	 * Method which performs actions based on the current location of the snake and the objective!
	 * if the snake is within a predetermined threshold the snake will perform the appropriate turn
	 * in order to collect the objective!
	 */
	private void checkCurrentLocation() {
		switch(state){
		case AVOIDING:
			break;
		case FINDING:
			break;
		case TRACKING:
			if (snakeAI.getX() > closestObjective.getX() - closestObjective.getRadius()*.25 && snakeAI.getX() < closestObjective.getX() + closestObjective.getRadius()*.25) {
				if (closestObjective.getY() < snakeAI.getY()) {
					snakeAI.setDirection(PlayerMovement.MOVE_UP);applyThrust();
				} else {
					snakeAI.setDirection(PlayerMovement.MOVE_DOWN);applyThrust();
				}
			}
			else if (snakeAI.getY() > closestObjective.getY() - closestObjective.getRadius()*.25 && snakeAI.getY() < closestObjective.getY() + closestObjective.getRadius()*.25) {
				if (closestObjective.getX() < snakeAI.getX()) {
					snakeAI.setDirection(PlayerMovement.MOVE_LEFT);applyThrust();
				} else {
					snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);applyThrust();
				}
			}
			break;
		default:
			break;

		}

	}
	private void applyThrust(){
		if (game.getEnergyBarTwo().getEnergyLevel() > 50) {
			if (snakeAI.isAllowThrust() && !snakeAI.getSpeedThrust()) {
				snakeAI.setSpeedThrust(true);
			}
		} else {
			snakeAI.setSpeedThrust(false);
		}
	}
	/**
	 * Method which checks the status of the current objective and base on the objective's status it will try
	 * to re-determine a new objective once the current objective has been collected or it has moved!
	 */
	private void checkObjectiveStatus() {
		switch(state){
		case AVOIDING:
			break;
		case FINDING:
			break;
		case TRACKING:
			if (closestObjective.isRemovable()) {
				findClosest();
				createPath();
			}
			if (closestObjective.getX() != positionX || closestObjective.getY() != positionY) {
				findClosest();
			}
			break;
		default:
			break;

		}

	}
	/**
	 * Method which when called will determine if the snake has to make an u-turn or
	 * if the snake can perform the desired turn without complications! NEEDS ANALYSIS!!!!
	 * @param move: Move which the AI desires to perform
	 */
	public void performMove(PlayerMovement move) {
		switch(state){
		case AVOIDING:
			break;
		case FINDING:
			break;
		case TRACKING:
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
			break;
		default:
			break;

		}
	}
	/**
	 * Method which when called will perform a turn based on the location of the objective! once the turn
	 * is made the path will be recalculated by the reRoute method! The method only gets called when the snake
	 * attempts to perform an illegal turn!
	 * @param currentDirection
	 */

	private void makeUTurn(PlayerMovement currentDirection) {
		switch(state){
		case AVOIDING:
			break;
		case FINDING:
			break;
		case TRACKING:
			if (currentDirection == PlayerMovement.MOVE_DOWN || currentDirection == PlayerMovement.MOVE_UP) {
				if (closestObjective.getX() < snakeAI.getX()) {
					snakeAI.setDirection(PlayerMovement.MOVE_LEFT);
					makingUTurn = true;
					turnOffset = snakeAI.getRadius()*2;
				} else {
					snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);
					makingUTurn = true;
					turnOffset = snakeAI.getRadius()*2;
				}
			} else if (currentDirection == PlayerMovement.MOVE_RIGHT || currentDirection == PlayerMovement.MOVE_LEFT) {
				if (closestObjective.getY() < snakeAI.getY()) {
					snakeAI.setDirection(PlayerMovement.MOVE_UP);
					makingUTurn = true;
					turnOffset = snakeAI.getRadius()*2;
				} else {
					snakeAI.setDirection(PlayerMovement.MOVE_DOWN);
					makingUTurn = true;
					turnOffset = snakeAI.getRadius()*2;
				}
			}
			break;
		default:
			break;

		}

	}
	/**
	 * Class which holds the distance and the nearest object
	 * and the object!
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
	public double getX(){
		return snakeAI.getX();
	}
	public double getY(){
		return snakeAI.getY();
	}
	public double getWidth(){
		return snakeAI.getBounds().getWidth();
	}
	public double getHeight(){
		return snakeAI.getBounds().getHeight();
	}
	public void setPlayer() {
		this.snakeAI = null;
		this.snakeAI = game.getGameLoader().getPlayerTwo();
	}
	public PlayerMovement getDirection(){
		return snakeAI.getCurrentDirection();
	}
	public ObjectivePosition getLocation() {
		return location;
	}

	public void setLocation(ObjectivePosition location) {
		this.location = location;
	}

	public AbstractTile getObstacle() {
		return obstacle;
	}

	public void setObstacle(AbstractTile obstacle) {
		this.obstacle = obstacle;
	}

	private enum ObjectivePosition {
		NORTH, SOUTH, WEST, EAST
	}

	public enum ActionState{
		TRACKING, AVOIDING, FINDING,
	}

	public enum LegalTurns{
		LEFT,RIGHT,UP,DOWN
	}
	public static double calcDistance(double x1, double y1, double x2, double y2) {
	    return Math.hypot(x2 - x1, y2 - y1);
	}
	public static double calcDistanceAlt(double x1, double y1, double x2, double y2) {
		return Math.abs(x1 - x2)+Math.abs(y1 - y2);
	}
}
