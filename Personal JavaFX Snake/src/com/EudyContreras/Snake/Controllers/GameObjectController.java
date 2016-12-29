package com.EudyContreras.Snake.Controllers;

import java.util.Iterator;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;

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
public class GameObjectController {

	private ObservableList<AbstractObject> fruits;
	private LinkedList<AbstractObject> fruitList;
	private LinkedList<AbstractObject> buffs;
	private GameManager game;

	public GameObjectController(GameManager game) {
		this.game = game;
		initialize();
	}
	/**
	 * method used to initialize the list.
	 */
	public void initialize() {
		fruitList = new LinkedList<AbstractObject>();
		fruits = FXCollections.observableList(fruitList);
		fruits.addListener(new ListChangeListener<AbstractObject>() {
			@Override
			public void onChanged(@SuppressWarnings("rawtypes") ListChangeListener.Change change) {
				if(game.getStateID() == GameStateID.GAMEPLAY){
					game.getAIController().nofifyAI();
				}
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
		Iterator<AbstractObject> fruitIter = fruits.iterator();

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
		if(fruits.size() > 4){
			fruits.get(fruits.size()-1).setRemovable(true);
		}
		if(GameSettings.APPLE_COUNT <= 4){
			if(fruits.size() > GameSettings.APPLE_COUNT){
				fruits.get(fruits.size()-1).setRemovable(true);
			}
		}
	}

	public void updateBuffs(long timePassed) {
		Iterator<AbstractObject> buffIter = buffs.iterator();

		while (buffIter.hasNext()) {
			AbstractObject tempBuff = buffIter.next();
			tempBuff.move();
			tempBuff.updateUI();
			tempBuff.checkCollision();
			tempBuff.addPhysics();
			tempBuff.updateAnimation(timePassed);
			tempBuff.logicUpdate();
			tempBuff.draw();
			tempBuff.checkRemovability();
			if (tempBuff.isRemovable() || !tempBuff.isAlive() ) {
				tempBuff.removeFromLayer();
				buffIter.remove();
				continue;
			}
		}
		if(buffs.size() > 4){
			buffs.get(buffs.size()-1).setRemovable(true);
		}
		if(GameSettings.APPLE_COUNT <= 4){
			if(buffs.size() > GameSettings.BUFF_COUNT){
				buffs.get(buffs.size()-1).setRemovable(true);
			}
		}
	}

	/**
	 * Method used to updates every fruits in the game. this method uses a
	 * conventional for loop and allows the list to be modified from an outside
	 * source without provoking a break.
	 */
	public void updateAll(long timePassed) {

		for (int i = 0; i < fruits.size(); i++) {
			AbstractObject tempFruit = fruits.get(i);
			tempFruit.move();
			tempFruit.updateUI();
			tempFruit.checkCollision();
			tempFruit.addPhysics();
			tempFruit.updateAnimation(timePassed);
			tempFruit.logicUpdate();
			tempFruit.draw();
			tempFruit.checkRemovability();
			if (tempFruit.isRemovable() || !tempFruit.isAlive()) {
				tempFruit.removeFromLayer();
				fruits.remove(i);
			}
		}
		if(fruits.size() > 4){
			fruits.get(fruits.size()-1).setRemovable(true);
		}
		if(GameSettings.APPLE_COUNT <= 4){
			if(fruits.size() > GameSettings.APPLE_COUNT){
				fruits.get(fruits.size()-1).setRemovable(true);
			}
		}
	}

	/**
	 * Check the status of the parent node
	 */
	public void checkStatus() {
		if (game.getThirdLayer().getChildren().size() > 50) {
			game.getThirdLayer().getChildren().remove(2, 35);
		}
	}

	/**
	 * Procedurally places the objects in the level
	 */
	public void procedurallyCreateLevel() {
		Iterator<? extends AbstractObject> spriteIter = fruits.iterator();
		while (spriteIter.hasNext()) {
			AbstractObject sprite = spriteIter.next();
			sprite.createLevel();
		}
	}

	public ObservableList<AbstractObject> getObsFruitList() {
		return fruits;
	}

	public LinkedList<AbstractObject> getBuffList() {
		return buffs;
	}

	public void addFruit(AbstractObject object) {
		this.fruits.add(object);
	}

	public void removeFruit(AbstractObject object) {
		this.fruits.remove(object);
	}

	public void addBuff(AbstractObject object) {
		this.fruits.add(object);
	}

	public void removeBuff(AbstractObject object) {
		this.fruits.remove(object);
	}
	/**
	 * Clears the fruits list.
	 */
	public void clearAll() {
		this.fruits.clear();
	}
	/**
	 * Finds a specified fruits with a given id
	 * and returns that fruits.
	 * @param id
	 * @return
	 */
	public AbstractObject findObject(GameObjectID id) {
		for (AbstractObject go : fruits) {
			if (go.getId() == id) {
				return go;
			}
		}
		return null;
	}

}
