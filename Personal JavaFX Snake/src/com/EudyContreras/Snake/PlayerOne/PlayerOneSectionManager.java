package com.EudyContreras.Snake.PlayerOne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;

import javafx.geometry.Point2D;

/**
 * This manager class is the core of every game section and is responsible for
 * updating, drawing, adding physics, animating, removing, moving every section
 * and checking whether the sections is alive or not meaning no longer used. the
 * sections updated by this class are mob sections meaning sections that move,
 * interact and collide.
 *
 * @author Eudy Contreras
 *
 */
public class PlayerOneSectionManager {

	private ArrayList<AbstractSection> sectionList;
	private AbstractSection tempSection;

	public PlayerOneSectionManager(GameManager gameJavaFX) {
		initialize();
	}

	public void initialize() {
		this.sectionList = new ArrayList<>();
	}

	public void addNewDirection(PlayerMovement direction) {
		for (AbstractSection sect : sectionList) {
			sect.setNewDirection(direction);
		}
	}

	public void addNewLocation(Point2D location) {
		for (AbstractSection sect : sectionList) {
			sect.setNewLocation(location);
		}
	}

	public void addNewCoordinates(Point2D location, PlayerMovement direction, int ID) {
		for (AbstractSection sect : sectionList) {
			if (sect.getNumericID() == ID) {
				sect.setNewDirection(direction);
				sect.setNewLocation(location);
			}
		}
	}
	/**
	 * Method used to update every section in the game. this method uses a
	 * conventional for loop and allows the list to be modified from an outside
	 * source without provoking a break.
	 */
	public void updateAllLogic(long timePassed) {

		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.checkCollision();
			tempSection.logicUpdate();
			tempSection.addPhysics();
			tempSection.updateAnimation(timePassed);
			tempSection.draw();
			tempSection.checkRemovability();
			if (tempSection.isRemovable() || !tempSection.isAlive()) {
				tempSection.removeFromLayer();
				sectionList.remove(i);
			}
		}
	}

	public void updateAllMovement(long timePassed) {
		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.move();
			tempSection.updateUI();
		}
	}
	/**
	 * Method used to update every section in the game. this method uses a
	 * conventional for loop and allows the list to be modified from an outside
	 * source without provoking a break.
	 */
	public void updateAllLogicIter(long timePassed) {
		Iterator<AbstractSection> sectionIter = sectionList.iterator();

		while (sectionIter.hasNext()) {
			tempSection = sectionIter.next();
			tempSection.checkCollision();
			tempSection.logicUpdate();
			tempSection.addPhysics();
			tempSection.updateAnimation(timePassed);
			tempSection.draw();
			tempSection.checkRemovability();
			if (tempSection.isRemovable() || !tempSection.isAlive()) {
				tempSection.removeFromLayer();
				sectionIter.remove();
			}
		}
	}

	public void updateAllMovementIter(long timePassed) {
		Iterator<AbstractSection> sectionIter = sectionList.iterator();

		while (sectionIter.hasNext()) {
			tempSection = sectionIter.next();
			tempSection.move();
			tempSection.updateUI();
		}
	}
	/**
	 * Method used to explicitly update the graphics
	 */
	public void updateUI() {
		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.updateUI();
		}
	}

	/**
	 * Method used to explicitly update animations
	 */
	public void updateAnimation(long timePassed) {
		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.updateAnimation(timePassed);
		}
	}

	/**
	 * Method used to explicitly move the sections
	 */
	public void move() {
		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.move();
		}
	}

	/**
	 * Method used to explicitly draw the graphics
	 */
	public void draw() {
		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.draw();
		}
	}

	/**
	 * Method used to explicitly add physics to the sections
	 */
	public void addPhysics() {
		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.addPhysics();
		}
	}

	/**
	 * Method used to check if the section should be removed
	 */
	public void checkIfRemoveable() {
		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.checkRemovability();
		}
	}

	/**
	 * Method used to check if the section has collied with another section
	 */
	public void checkCollisions() {
		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.checkCollision();
		}
	}

	/**
	 * Procedurally places the sections in the level
	 */
	public ArrayList<AbstractSection> getSectionList() {
		return sectionList;
	}

	public void addSection(AbstractSection... sect) {
		Collections.addAll(sectionList,sect);
	}

	public void removeSection(AbstractSection section) {
		this.sectionList.remove(section);
	}

	public void clearAll() {
		this.sectionList.clear();
	}

}
