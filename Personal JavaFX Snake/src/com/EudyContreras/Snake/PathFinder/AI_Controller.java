package com.EudyContreras.Snake.PathFinder;

import java.util.Iterator;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.collections.ObservableList;

/**
 * Class than handles logic behind all the classes that the Object evasion AI depends on.
 *
 * <h1><li>NOTES:</h1>
 * <li>The AI tells the snake to move towards an objective.
 * <li>The snake moves towards the specified location.
 * <li>While the snake moves it will cast warning which will identify possible collisions. This warnings will
 * be sent to specified locations. If the vector collides with an object it will identify the object as a potential collide target. Once
 * the object is identified as a target a CollideObject is created and added to a collection containing a list of unique collide objects. These objects contain
 * information regarding the dimension, position, the relative location, and the distance between the collide target and the snake.
 * <li>The AI controller checks all collide objects and monitors each of the object's position relative to the snake.
 * It also constantly monitors the distance of the objects relative to the snake.
 * <li>Base on the potential collision targets and the position of the snake, a Turn Monitor will determine all the allowed moves that
 * the snake is allowed to make. The turn monitor will only accept information from collision targets that are within a specified distance
 * range from the snake.
 * <li>If the snake's direction is a direction which the turn monitor has labeled as unsafe or not allowed, the snake will change direction to an allowed direction
 * <li>While the snake is only setting its directions to the allowed ones, it will also consider the best direction among all allowed directions. The best
 * direction is determined by calculating the position of the objective and the current position of the snake.
 * <h1><li>NOTES:</h1>
 * <li>Rethinks the process.
 * <li>Maybe it is best to pre-calculate turns.
 * <li>Make a mechanism that finds the closest apple.
 * <li>Once the apple has been found calculate how many turns it will take for the snake to reach the apple
 * and where will the turns be made! The pixels between turns must also be calculated. If a turn leads to a collision with an object
 * calculate the objects dimension and the position of the objective. if the object is met while traveling to the right calculate the
 * height of the object. Determine the distance between the snake and the apple if the snake travels up to avoid the object and the distance
 * if the snake travels down. Compute closest distance and place the turn marker and direction
 * <li>Always start trajectory based on the closest path. Calculate both the x and the y distance to the object.
 * @author Eudy Contreras
 *
 */
public class AI_Controller {

	private LinkedList<AbstractCollisionMonitor> collisionAwarenessList;
	private LinkedList<CollideObject> possibleColliders;
	private ObservableList<AbstractObject> targetList;
	private PathFindingGrid pathFindingGrid;
	private ObjectEvasionAI evasiveAI;
	private TurnMonitor turnMonitor;
	private PlayerTwo snakeAI;
	private GameManager game;


	public AI_Controller(GameManager game) {
		this.game = game;
		this.snakeAI = game.getGameLoader().getPlayerTwo();
		this.turnMonitor = new TurnMonitor();
		this.collisionAwarenessList = new LinkedList<AbstractCollisionMonitor>();
		this.possibleColliders = new LinkedList<CollideObject>();
		this.evasiveAI = new ObjectEvasionAI(game, snakeAI, turnMonitor, possibleColliders);
		this.initialize();
	}

	public void initialize() {
		obtainAllColliders();
		pathFindingGrid = null;
		pathFindingGrid = new PathFindingGrid(game,this,GameSettings.WIDTH,GameSettings.HEIGHT,45,1,true,possibleColliders, targetList);
		pathFindingGrid.placeCells();
	}
	public void updateGrid(){
		obtainAllColliders();
		pathFindingGrid.setColliderList(possibleColliders);
		pathFindingGrid.computeValidCells();
	}
	public void update_AI_Simulation(long timePassed) {
//		updatePotentialColliders(timePassed);
		processAIEvents();

	}
	public void nofifyAI(){
		evasiveAI.findObjective();
	}
	private void processAIEvents() {
		evasiveAI.updateSimulation();
	}
	public PathFindingGrid getGrid(){
		return pathFindingGrid;
	}
	public void updateCollisionAwarenessI(long timePassed) {
		Iterator<AbstractCollisionMonitor> awarenessList = collisionAwarenessList.iterator();
		while (awarenessList.hasNext()) {
			AbstractCollisionMonitor awareNess = awarenessList.next();
			awareNess.move();
			awareNess.checkCollision();
			awareNess.updateLogic();
			awareNess.checkRemovability();
			if (awareNess.hasTarget()) {
				addPossibleCollideBlock(new CollideObject(evasiveAI, awareNess.getEminentCollider()));
				awarenessList.remove();
				continue;
			} else if (!awareNess.isAlive()) {
				awarenessList.remove();
				continue;
			}
		}
	}

	public void updateCollisionAwareness(long timePassed) {
		for (int i = 0; i < collisionAwarenessList.size(); i++) {
			AbstractCollisionMonitor awareness = collisionAwarenessList.get(i);
			awareness.move();
			awareness.checkCollision();
			awareness.updateLogic();
			awareness.checkRemovability();
			if (awareness.hasTarget()) {
				addPossibleCollideBlock(new CollideObject(evasiveAI, awareness.getEminentCollider()));
				collisionAwarenessList.remove(i);
			} else if (!awareness.isAlive()) {
				collisionAwarenessList.remove(i);
				continue;
			}
		}
	}

//	private void computePossibleDirections(CollideObject collider) {
//		if (collider.getRiskFactor() == RiskFactor.HIGH) {
//			if(collider.getRange() == RangeFactor.WITHIN_RANGE){
//				switch(collider.getLocation()){
//				case EAST_OF_PlAYER:
//					computeDecision(true,true,false,true);
//					break;
//				case NORTH_OF_PLAYER:
//					computeDecision(false,true,true,true);
//					break;
//				case SOUTH_OF_PLAYER:
//					computeDecision(true,false,true,true);
//					break;
//				case WEST_OF_PLAYER:
//					computeDecision(true,true,true,false);
//					break;
//				default:
//					break;
//				}
//			}
//			else{
//				computeDecision(true,true,true,true);
//			}
//		}
//	}

//	private void computeDecision(boolean up, boolean down, boolean right, boolean left) {
//		turnMonitor.setAllowMoveUp(up);
//		turnMonitor.setAllowMoveDown(down);
//		turnMonitor.setAllowMoveLeft(left);
//		turnMonitor.setAllowMoveRight(right);
//	}
	public void obtainAllColliders(){
		clearAll();
		for(int i = 0; i<game.getGameLoader().getTileManager().getBlock().size(); i++){
			AbstractTile blocks = game.getGameLoader().getTileManager().getBlock().get(i);
			addPossibleCollideBlock(new CollideObject(evasiveAI, blocks));
		}
		for(int i = 0; i<game.getGameLoader().getTileManager().getTrap().size(); i++){
			AbstractTile traps = game.getGameLoader().getTileManager().getTrap().get(i);
			addPossibleCollideBlock(new CollideObject(evasiveAI, traps));
		}
		this.targetList = game.getGameObjectController().getFruitList();
	}
	public LinkedList<AbstractCollisionMonitor> getAwarenessList() {
		return collisionAwarenessList;
	}

	public void addPossibleCollideBlock(CollideObject collideObject) {
		if (!possibleColliders.contains(collideObject)) {
			possibleColliders.add(collideObject);
		}
	}

	public void addAwarenessVector(AbstractCollisionMonitor object) {
		this.collisionAwarenessList.add(object);
	}

	public void removeAwarenessVector(AbstractCollisionMonitor object) {
		this.collisionAwarenessList.remove(object);
	}

	public void clearAll() {
//		this.collisionAwarenessList.clear();
		this.possibleColliders.clear();
	}
	public ObjectEvasionAI getEvasiveAI(){
		return evasiveAI;
	}
}
