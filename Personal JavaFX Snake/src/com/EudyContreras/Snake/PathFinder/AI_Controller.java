package com.EudyContreras.Snake.PathFinder;

import java.util.Iterator;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
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

	private ObservableList<AbstractObject> predicters;
	
	private ObjectEvasionAI evasiveAI;
	private AbstractObject tempPredicter;
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
		LinkedList<AbstractObject> fruitList = new LinkedList<AbstractObject>();
		predicters = FXCollections.observableList(fruitList);
		predicters.addListener(new ListChangeListener<AbstractObject>() {
			@Override
			public void onChanged(@SuppressWarnings("rawtypes") ListChangeListener.Change change) {
				if(game.getStateID() == GameStateID.GAMEPLAY)
				game.getPathFinder().findObjective(ActionState.TRACKING);
			}
		});

	}

	/**
	 * This method updates the objects using and iterator which only iterates
	 * through every fruits once. faster in some cases but it makes it so the
	 * list can only be modified through this method or else an exception will
	 * be thrown
	 */
	public void updateFruits(long timePassed) {
		Iterator<AbstractObject> fruitIter = predicters.iterator();
		while (fruitIter.hasNext()) {
			AbstractObject tempFruit = fruitIter.next();
			tempFruit.move();
			tempFruit.updateUI();
			tempFruit.checkCollision();
			tempFruit.addPhysics();
			tempFruit.updateAnimation(timePassed);
			tempFruit.logicUpdate();
			tempFruit.draw();
			tempFruit.checkRemovability();
			if (tempFruit.isRemovable() || !tempFruit.isAlive() ) {
				tempFruit.removeFromLayer();
				fruitIter.remove();
				continue;
			}
		}

	}

	/**
	 * Method used to update every fruits in the game. this method uses a
	 * conventional for loop and allows the list to be modified from an outside
	 * source without provoking a break.
	 */
	public void updateAll(long timePassed) {

		for (int i = 0; i < predicters.size(); i++) {
			tempPredicter = predicters.get(i);
			tempPredicter.move();
			tempPredicter.updateUI();
			tempPredicter.checkCollision();
			tempPredicter.addPhysics();
			tempPredicter.updateAnimation(timePassed);
			tempPredicter.logicUpdate();
			tempPredicter.draw();
			tempPredicter.checkRemovability();
			if (tempPredicter.isRemovable() || !tempPredicter.isAlive()) {
				tempPredicter.removeFromLayer();
				predicters.remove(i);
			}
		}
		if(predicters.size() > 4){
			predicters.get(predicters.size()-1).setRemovable(true);
		}
		if(GameSettings.APPLE_COUNT <= 4){
			if(predicters.size() > GameSettings.APPLE_COUNT){
				predicters.get(predicters.size()-1).setRemovable(true);
			}
		}
	}

	public ObservableList<AbstractObject> getFruitList() {
		return predicters;
	}

	public LinkedList<AbstractObject> getBuffList() {
		return buffs;
	}

	public void addFruit(AbstractObject object) {
		this.predicters.add(object);
	}

	public void removeFruit(AbstractObject object) {
		this.predicters.remove(object);
	}

	public void addBuff(AbstractObject object) {
		this.predicters.add(object);
	}

	public void removeBuff(AbstractObject object) {
		this.predicters.remove(object);
	}
	/**
	 * Clears the fruits list.
	 */
	public void clearAll() {
		this.predicters.clear();
	}
	/**
	 * Finds a specified fruits with a given id
	 * and returns that fruits.
	 * @param id
	 * @return
	 */
	public AbstractObject findObject(GameObjectID id) {
		for (AbstractObject go : predicters) {
			if (go.getId() == id) {
				return go;
			}
		}
		return null;
	}

}
