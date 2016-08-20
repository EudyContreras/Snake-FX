package com.EudyContreras.Snake.PathFinder;

import java.util.Iterator;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.PathFinder.CollideObject.RangeFactor;
import com.EudyContreras.Snake.PathFinder.CollideObject.RiskFactor;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

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
 *
 * @author Eudy Contreras
 *
 */
public class AI_Controller {

	private LinkedList<AbstractCollisionMonitor> collisionAwarenessList;
	private LinkedList<CollideObject> possibleColliders;
	private ObjectEvasionAI evasiveAI;
	private TurnMonitor turnMonitor;
	private PlayerTwo snakeAI;
	private GameManager game;

	public AI_Controller(GameManager game) {
		this.game = game;
		this.snakeAI = game.getGameLoader().getPlayerTwo();
		this.initialize();
	}

	public void initialize() {
		turnMonitor = new TurnMonitor();
		evasiveAI = new ObjectEvasionAI(game, snakeAI, turnMonitor, possibleColliders);
		collisionAwarenessList = new LinkedList<AbstractCollisionMonitor>();
		possibleColliders = new LinkedList<CollideObject>();

	}

	public void update_AI_Simulation(long timePassed) {
		updateCollisionAwareness(timePassed);
		updatePotentialColliders(timePassed);
		processAIEvents();

	}
	private void processAIEvents() {
		evasiveAI.updateSimulation();
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

	private void updatePotentialColliders(long timePassed) {
		Iterator<CollideObject> collider = possibleColliders.iterator();
		while (collider.hasNext()) {
			CollideObject objects = collider.next();
			objects.updateProperties();
			computePossibleDirections(objects);
		}
	}

	private void computePossibleDirections(CollideObject collider) {
		if (collider.getRiskFactor() == RiskFactor.HIGH) {
			if(collider.getRange() == RangeFactor.WITHIN_RANGE){
				switch(collider.getLocation()){
				case EAST_OF_PlAYER:
					computeDecision(true,true,false,true);
					break;
				case NORTH_OF_PLAYER:
					computeDecision(true,true,false,true);
					break;
				case SOUTH_OF_PLAYER:
					computeDecision(true,true,false,true);
					break;
				case WEST_OF_PLAYER:
					computeDecision(true,true,false,true);
					break;
				default:
					break;
				}
			}
		}
	}

	private void computeDecision(boolean up, boolean down, boolean right, boolean left) {
		turnMonitor.setAllowMoveUp(up);
		turnMonitor.setAllowMoveDown(down);
		turnMonitor.setAllowMoveLeft(left);
		turnMonitor.setAllowMoveRight(right);
	}
	private void obtainAllColliders(){
		for(int i = 0; i<game.getGameLoader().getTileManager().getBlock().size(); i++){
			AbstractTile blocks = game.getGameLoader().getTileManager().getBlock().get(i);
			addPossibleCollideBlock(new CollideObject(evasiveAI, blocks));
		}
		for(int i = 0; i<game.getGameLoader().getTileManager().getTrap().size(); i++){
			AbstractTile traps = game.getGameLoader().getTileManager().getTrap().get(i);
			addPossibleCollideBlock(new CollideObject(evasiveAI, traps));
		}
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
		this.collisionAwarenessList.clear();
	}

}
