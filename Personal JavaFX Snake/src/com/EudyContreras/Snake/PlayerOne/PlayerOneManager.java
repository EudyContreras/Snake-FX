package com.EudyContreras.Snake.PlayerOne;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.EnumIDs.GameObjectID;
import com.EudyContreras.Snake.FrameWork.GameManager;

import javafx.scene.canvas.GraphicsContext;

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
public class PlayerOneManager {

	private LinkedList<AbstractObject> playerOneList;
	private AbstractObject tempPlayerOneObject;
	private GameManager game;

	public PlayerOneManager(GameManager gameJavaFX) {
		this.game = gameJavaFX;
		initialize();
	}
	/**
	 * method used to initialize the list.
	 */
	public void initialize() {
		this.playerOneList = new LinkedList<AbstractObject>();
	}

	/**
	 * This method updates the logic of all objects using and iterator which only iterates
	 * through every object once. faster in some cases but it makes it so the
	 * list can only be modified through this method or else an exception will
	 * be thrown
	 */
	public void updateAllLogicI(GraphicsContext gc, long timePassed) {
		Iterator<? extends AbstractObject> objectIterator = playerOneList.iterator();
		while (objectIterator.hasNext()) {
			AbstractObject tempObject = objectIterator.next();;
			tempObject.checkCollision();
			tempObject.addPhysics();
			tempObject.updateAnimation(timePassed);
			tempObject.logicUpdate();
			tempObject.draw(gc);
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
	public void updateAllMovementI() {
		Iterator<? extends AbstractObject> objectIterator = playerOneList.iterator();
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
	public void updateAllLogic(GraphicsContext gc, long timePassed) {
		for (int i = 0; i < playerOneList.size(); i++) {
			tempPlayerOneObject = playerOneList.get(i);
			tempPlayerOneObject.checkCollision();
			tempPlayerOneObject.addPhysics();
			tempPlayerOneObject.updateAnimation(timePassed);
			tempPlayerOneObject.logicUpdate();
			tempPlayerOneObject.draw(gc);
			tempPlayerOneObject.checkRemovability();
			if (tempPlayerOneObject.isRemovable() || !tempPlayerOneObject.isAlive()) {
				tempPlayerOneObject.removeFromLayer();
				playerOneList.remove(i);
			}
		}
	}
	/**
	 * Method used to update the movement of every object in the game. this method uses a
	 * conventional for loop and allows the list to be modified from an outside
	 * source without provoking a break.
	 */
	public void updateAllMovement(){
		for (int i = 0; i < playerOneList.size(); i++) {
			tempPlayerOneObject = playerOneList.get(i);
			tempPlayerOneObject.move();
			tempPlayerOneObject.updateUI();
		}
	}

	/**
	 * Method used to explicitly update the graphics
	 */
	public void updateUI() {

		for (int i = 0; i < playerOneList.size(); i++) {
			tempPlayerOneObject = playerOneList.get(i);
			tempPlayerOneObject.updateUI();
		}
	}

	/**
	 * Method used to explicitly update animations
	 */
	public void updateAnimation(long timePassed) {

		for (int i = 0; i < playerOneList.size(); i++) {
			tempPlayerOneObject = playerOneList.get(i);
			tempPlayerOneObject.updateAnimation(timePassed);
		}
	}

	/**
	 * Method used to explicitly move the objects
	 */
	public void move() {

		for (int i = 0; i < playerOneList.size(); i++) {
			tempPlayerOneObject = playerOneList.get(i);
			tempPlayerOneObject.move();
		}
	}

	/**
	 * Method used to explicitly draw the graphics
	 */
	public void draw(GraphicsContext gc) {

		for (int i = 0; i < playerOneList.size(); i++) {
			tempPlayerOneObject = playerOneList.get(i);
			tempPlayerOneObject.draw(gc);
		}
	}

	/**
	 * Method used to explicitly add physics to the objects
	 */
	public void addPhysics() {

		for (int i = 0; i < playerOneList.size(); i++) {
			tempPlayerOneObject = playerOneList.get(i);
			tempPlayerOneObject.addPhysics();
		}
	}
	/**
	 * Method used to check if the object has collied with another object
	 */
	public void updateLogic() {
		for (int i = 0; i < playerOneList.size(); i++) {
			tempPlayerOneObject = playerOneList.get(i);
			tempPlayerOneObject.logicUpdate();
		}
	}
	/**
	 * Method used to check if the object should be removed
	 */
	public void checkIfRemoveable() {
		for (int i = 0; i < playerOneList.size(); i++) {
			tempPlayerOneObject = playerOneList.get(i);
			tempPlayerOneObject.checkRemovability();
		}
	}

	/**
	 * Method used to check if the object has collied with another object
	 */
	public void checkCollisions() {
		for (int i = 0; i < playerOneList.size(); i++) {
			tempPlayerOneObject = playerOneList.get(i);
			tempPlayerOneObject.checkCollision();
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
		Iterator<? extends AbstractObject> spriteIter = playerOneList.iterator();
		while (spriteIter.hasNext()) {
			AbstractObject sprite = spriteIter.next();
			sprite.createLevel();
		}
	}

	public LinkedList<AbstractObject> getObjectList() {
		return playerOneList;
	}

	public void addObject(AbstractObject object) {
		Collections.addAll(playerOneList,object);
	}

	public void removeObject(AbstractObject object) {
		this.playerOneList.remove(object);
	}
	/**
	 * Clears the object list.
	 */
	public void clearAll() {
		this.playerOneList.clear();
	}
	/**
	 * Finds a specified object with a given id
	 * and returns that object.
	 * @param id
	 * @return
	 */
	public AbstractObject findObject(GameObjectID id) {
		for (AbstractObject go : playerOneList) {
			if (go.getId() == id) {
				return go;
			}
		}
		return null;
	}

}
