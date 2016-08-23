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

	private LinkedList<AbstractObject> fruitList;
	private LinkedList<AbstractObject> buffs;
	private AbstractObject tempFruit;
	private AbstractObject tempBuff;
	private GameManager game;

	public GameObjectController(GameManager gameJavaFX) {
		this.game = gameJavaFX;
		initialize();
	}
	/**
	 * method used to initialize the list.
	 */
	public void initialize() {
		fruitList = new LinkedList<AbstractObject>();
		ObservableList<AbstractObject> fruits;
		fruits = FXCollections.observableList(fruitList);
		fruits.addListener(new ListChangeListener<AbstractObject>() {
			@Override
			public void onChanged(@SuppressWarnings("rawtypes") ListChangeListener.Change change) {
				if(game.getStateID() == GameStateID.GAMEPLAY){
				game.getAIController().nofifyAI();
				game.getAIController().getPathFindingAI().computeClosestPath(5,5);
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
		Iterator<AbstractObject> fruitIter = fruitList.iterator();
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
	public void updateBufss(long timePassed) {
		Iterator<AbstractObject> buffIter = buffs.iterator();
		while (buffIter.hasNext()) {
			tempBuff = buffIter.next();
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
	 * Method used to update every fruits in the game. this method uses a
	 * conventional for loop and allows the list to be modified from an outside
	 * source without provoking a break.
	 */
	public void updateAll(long timePassed) {

		for (int i = 0; i < fruitList.size(); i++) {
			tempFruit = fruitList.get(i);
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
				fruitList.remove(i);
			}
		}
		if(fruitList.size() > 4){
			fruitList.get(fruitList.size()-1).setRemovable(true);
		}
		if(GameSettings.APPLE_COUNT <= 4){
			if(fruitList.size() > GameSettings.APPLE_COUNT){
				fruitList.get(fruitList.size()-1).setRemovable(true);
			}
		}
	}

	/**
	 * Method used to explicitly update the graphics
	 */
	public void updateUI() {

		for (int i = 0; i < fruitList.size(); i++) {
			tempFruit = fruitList.get(i);
			tempFruit.updateUI();
		}
	}

	/**
	 * Method used to explicitly update animations
	 */
	public void updateAnimation(long timePassed) {

		for (int i = 0; i < fruitList.size(); i++) {
			tempFruit = fruitList.get(i);
			tempFruit.updateAnimation(timePassed);
		}
	}

	/**
	 * Method used to explicitly move the objects
	 */
	public void move() {

		for (int i = 0; i < fruitList.size(); i++) {
			tempFruit = fruitList.get(i);
			tempFruit.move();
		}
	}

	/**
	 * Method used to explicitly draw the graphics
	 */
	public void draw() {

		for (int i = 0; i < fruitList.size(); i++) {
			tempFruit = fruitList.get(i);
			tempFruit.draw();
		}
	}

	/**
	 * Method used to explicitly add physics to the objects
	 */
	public void addPhysics() {

		for (int i = 0; i < fruitList.size(); i++) {
			tempFruit = fruitList.get(i);
			tempFruit.addPhysics();
		}
	}

	/**
	 * Method used to check if the fruitList should be removed
	 */
	public void checkIfRemoveable() {
		for (int i = 0; i < fruitList.size(); i++) {
			tempFruit = fruitList.get(i);
			tempFruit.checkRemovability();
		}
	}

	/**
	 * Method used to check if the fruitList has collied with another fruitList
	 */
	public void checkCollisions() {
		for (int i = 0; i < fruitList.size(); i++) {
			tempFruit = fruitList.get(i);
			tempFruit.checkCollision();
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
		Iterator<? extends AbstractObject> spriteIter = fruitList.iterator();
		while (spriteIter.hasNext()) {
			AbstractObject sprite = spriteIter.next();
			sprite.createLevel();
		}
	}

	public LinkedList<AbstractObject> getFruitList() {
		return fruitList;
	}

	public LinkedList<AbstractObject> getBuffList() {
		return buffs;
	}

	public void addFruit(AbstractObject object) {
		this.fruitList.add(object);
	}

	public void removeFruit(AbstractObject object) {
		this.fruitList.remove(object);
	}

	public void addBuff(AbstractObject object) {
		this.fruitList.add(object);
	}

	public void removeBuff(AbstractObject object) {
		this.fruitList.remove(object);
	}
	/**
	 * Clears the fruitList list.
	 */
	public void clearAll() {
		this.fruitList.clear();
	}
	/**
	 * Finds a specified fruitList with a given id
	 * and returns that fruitList.
	 * @param id
	 * @return
	 */
	public AbstractObject findObject(GameObjectID id) {
		for (AbstractObject go : fruitList) {
			if (go.getId() == id) {
				return go;
			}
		}
		return null;
	}

}
