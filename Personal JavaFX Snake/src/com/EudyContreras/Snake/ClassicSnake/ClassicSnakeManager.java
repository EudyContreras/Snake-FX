package com.EudyContreras.Snake.ClassicSnake;

import java.util.Iterator;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Identifiers.GameObjectID;

/**
 * This manager class is the core of every game object and is responsible for
 * updating, drawing, adding physics, animating, removing, moving every object
 * and checking whether the objects is alive or not meaning no longer used. the
 * objects updated by this class are mob objects meaning objects that move,
 * interact and collide.
 *
 * @author Eudy Contreras
 *
 */
public class ClassicSnakeManager {

	private LinkedList<AbstractObject> classicPlayerList;
	private AbstractObject classicPlayerObject;
	private GameManager game;

	public ClassicSnakeManager(GameManager gameJavaFX) {
		this.game = gameJavaFX;
		initialize();
	}
	/**
	 * method used to initialize the list.
	 */
	public void initialize() {
		this.classicPlayerList = new LinkedList<AbstractObject>();
	}

	/**
	 * This method updates the logic of all objects using and iterator which only iterates
	 * through every object once. faster in some cases but it makes it so the
	 * list can only be modified through this method or else an exception will
	 * be thrown
	 */
	public void updateAllLogicI(long timePassed) {
		Iterator<? extends AbstractObject> objectIterator = classicPlayerList.iterator();
		while (objectIterator.hasNext()) {
			AbstractObject tempObject = objectIterator.next();
			tempObject.checkCollision();
			tempObject.addPhysics();
			tempObject.updateAnimation(timePassed);
			tempObject.logicUpdate();
			tempObject.draw();
			tempObject.checkRemovability();
			if (tempObject.isRemovable() || !tempObject.isAlive()) {
				tempObject.removeFromLayer();
				objectIterator.remove();
				continue;
			}
		}
	}
	/**
	 * This method moves the objects using and iterator which only iterates
	 * through every object once. faster in some cases but it makes it so the
	 * list can only be modified through this method or else an exception will
	 * be thrown
	 */
	public void updateAllMovementI(long timePassed) {
		Iterator<? extends AbstractObject> objectIterator = classicPlayerList.iterator();
		while (objectIterator.hasNext()) {
			AbstractObject tempObject = objectIterator.next();
			tempObject.move();
			tempObject.updateUI();
		}
	}

	/**
	 * Method used to update every the logic object in the game. this method uses a
	 * conventional for loop and allows the list to be modified from an outside
	 * source without provoking a break.
	 */
	public void updateAllLogic(long timePassed) {
		for (int i = 0; i < classicPlayerList.size(); i++) {
			classicPlayerObject = classicPlayerList.get(i);
			classicPlayerObject.checkCollision();
			classicPlayerObject.addPhysics();
			classicPlayerObject.updateAnimation(timePassed);
			classicPlayerObject.logicUpdate();
			classicPlayerObject.draw();
			classicPlayerObject.checkRemovability();
			if (classicPlayerObject.isRemovable() || !classicPlayerObject.isAlive()) {
				classicPlayerObject.removeFromLayer();
				classicPlayerList.remove(i);
			}
		}
	}
	/**
	 * Method used to update the movement of every object in the game. this method uses a
	 * conventional for loop and allows the list to be modified from an outside
	 * source without provoking a break.
	 */
	public void updateAllMovement(){
		for (int i = 0; i < classicPlayerList.size(); i++) {
			classicPlayerObject = classicPlayerList.get(i);
			classicPlayerObject.move();
			classicPlayerObject.updateUI();
		}
	}

	/**
	 * Method used to explicitly update the graphics
	 */
	public void updateUI() {

		for (int i = 0; i < classicPlayerList.size(); i++) {
			classicPlayerObject = classicPlayerList.get(i);
			classicPlayerObject.updateUI();
		}
	}

	/**
	 * Method used to explicitly update animations
	 */
	public void updateAnimation(long timePassed) {

		for (int i = 0; i < classicPlayerList.size(); i++) {
			classicPlayerObject = classicPlayerList.get(i);
			classicPlayerObject.updateAnimation(timePassed);
		}
	}

	/**
	 * Method used to explicitly move the objects
	 */
	public void move() {

		for (int i = 0; i < classicPlayerList.size(); i++) {
			classicPlayerObject = classicPlayerList.get(i);
			classicPlayerObject.move();
		}
	}

	/**
	 * Method used to explicitly draw the graphics
	 */
	public void draw() {

		for (int i = 0; i < classicPlayerList.size(); i++) {
			classicPlayerObject = classicPlayerList.get(i);
			classicPlayerObject.draw();
		}
	}

	/**
	 * Method used to explicitly add physics to the objects
	 */
	public void addPhysics() {

		for (int i = 0; i < classicPlayerList.size(); i++) {
			classicPlayerObject = classicPlayerList.get(i);
			classicPlayerObject.addPhysics();
		}
	}
	/**
	 * Method used to check if the object has collied with another object
	 */
	public void updateLogic() {
		for (int i = 0; i < classicPlayerList.size(); i++) {
			classicPlayerObject = classicPlayerList.get(i);
			classicPlayerObject.logicUpdate();
		}
	}
	/**
	 * Method used to check if the object should be removed
	 */
	public void checkIfRemoveable() {
		for (int i = 0; i < classicPlayerList.size(); i++) {
			classicPlayerObject = classicPlayerList.get(i);
			classicPlayerObject.checkRemovability();
		}
	}

	/**
	 * Method used to check if the object has collied with another object
	 */
	public void checkCollisions() {
		for (int i = 0; i < classicPlayerList.size(); i++) {
			classicPlayerObject = classicPlayerList.get(i);
			classicPlayerObject.checkCollision();
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
		Iterator<? extends AbstractObject> spriteIter = classicPlayerList.iterator();
		while (spriteIter.hasNext()) {
			AbstractObject sprite = spriteIter.next();
			sprite.createLevel();
		}
	}

	public LinkedList<AbstractObject> getObjectList() {
		return classicPlayerList;
	}

	public void addObject(AbstractObject object) {
		this.classicPlayerList.add(object);
	}

	public void removeObject(AbstractObject object) {
		this.classicPlayerList.remove(object);
	}
	/**
	 * Clears the object list.
	 */
	public void clearAll() {
		this.classicPlayerList.clear();
	}
	/**
	 * Finds a specified object with a given id
	 * and returns that object.
	 * @param id
	 * @return
	 */
	public AbstractObject findObject(GameObjectID id) {
		for (AbstractObject go : classicPlayerList) {
			if (go.getId() == id) {
				return go;
			}
		}
		return null;
	}

}
