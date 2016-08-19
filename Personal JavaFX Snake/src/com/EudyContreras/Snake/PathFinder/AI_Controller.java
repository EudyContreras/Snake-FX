package com.EudyContreras.Snake.PathFinder;

import java.util.Iterator;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractCollisionMonitor;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.PathFinder.PathFindingAI.ActionState;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * This manager class is the core of every game fruits and is responsible for
 * updating, drawing, adding physics, animating, removing, moving every fruits
 * and checking whether the objects is alive or not meaning no longer used. the
 * objects updated by this class are mob objects meaning objects that move,
 * interact and collide.
 *
 * @author Eudy Contreras
 *
 */
public class AI_Controller {

	private LinkedList<AbstractCollisionMonitor> predictorList;
	private LinkedList<TargetDistance> eminentColliders;
	private ObjectEvasionAI evasiveAI;
	private AbstractCollisionMonitor tempPredicter;
	private PlayerTwo snakeAI;
	private GameManager game;

	public AI_Controller(GameManager game) {
		this.game = game;
		this.snakeAI = game.getGameLoader().getPlayerTwo();
		this.initialize();
	}
	/**
	 * method used to initialize the list.
	 */
	public void initialize() {
		evasiveAI = new ObjectEvasionAI(game,snakeAI);
		predictorList = new LinkedList<AbstractCollisionMonitor>();
		eminentColliders = new LinkedList<TargetDistance>();

	}

	/**
	 * This method updates the objects using and iterator which only iterates
	 * through every fruits once. faster in some cases but it makes it so the
	 * list can only be modified through this method or else an exception will
	 * be thrown
	 */
	public void updateCollisionWarnings(long timePassed) {
		Iterator<AbstractCollisionMonitor> predictors = predictorList.iterator();
		while (predictors.hasNext()) {
			AbstractCollisionMonitor warning = predictors.next();
			warning.move();
			warning.checkCollision();
			warning.updateLogic();
			warning.checkRemovability();
			if (!warning.isAlive() ) {
				addEminentCollider(new TargetDistance(evasiveAI, warning.getX(), warning.getY(), warning.getWidth(), warning.getHeight()));
				predictors.remove();
				continue;
			}
		}

	}
	
	/**
	 * Method used to update every fruits in the game. this method uses a
	 * conventional for loop and allows the list to be modified from an outside
	 * source without provoking a break.
	 */
	public void updateAI(long timePassed) {

	}
	public void processEmminentColliders(){
		
	}
	public LinkedList<AbstractCollisionMonitor> getPredictors() {
		return predictorList;
	}
	public void addEminentCollider(TargetDistance objectDistance){
		eminentColliders.add(objectDistance);
	}
	public void addPredictor(AbstractCollisionMonitor object) {
		this.predictorList.add(object);
	}

	public void removePredictor(AbstractCollisionMonitor object) {
		this.predictorList.remove(object);
	}
	/**
	 * Clears the fruits list.
	 */
	public void clearAll() {
		this.predictorList.clear();
	}


}
