package com.EudyContreras.Snake.SlitherSnake;

import java.util.ArrayList;

import com.EudyContreras.Snake.AbstractModels.AbstractSlitherSection;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

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
public class SlitherSectionManager {
	private ArrayList<AbstractSlitherSection> sectionList;
	private AbstractSlitherSection tempSection;

	public SlitherSectionManager(GameManager gameJavaFX) {
		initialize();
	}

	public void initialize() {
		this.sectionList = new ArrayList<>();
	}

	/**
	 * Method used to updates every section in the game. this method uses a
	 * conventional for loop and allows the list to be modified from an outside
	 * source without provoking a break.
	 */
	public void updateAll(GraphicsContext gc, long timePassed) {
		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.updateUI();
			tempSection.addPhysics();
			tempSection.updateAnimation(timePassed);
			tempSection.draw(gc);
			tempSection.move();
			tempSection.checkRemovability();
			if (tempSection.isRemovable() || !tempSection.isAlive()) {
				tempSection.removeFromLayer();
				sectionList.remove(i);
			}
		}
	}

	public void addNewDirection(PlayerMovement direction) {
		for (AbstractSlitherSection sect : sectionList) {
			sect.setNewDirection(direction);
		}
	}

	public void addNewLocation(Point2D location) {
		for (AbstractSlitherSection sect : sectionList) {
			sect.setNewLocation(location);
		}
	}

	public void addNewCoordinates(Point2D location, PlayerMovement direction, int ID) {
		for (AbstractSlitherSection sect : sectionList) {
			if (sect.getNumericID() == ID) {
				sect.setNewDirection(direction);
				sect.setNewLocation(location);
			}
		}
	}

	/**
	 * Method used to explicitly updates the graphics
	 */
	public void updateUI() {

		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.updateUI();
		}
	}

	/**
	 * Method used to explicitly updates animations
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
	public void draw(GraphicsContext gc) {
		for (int i = 0; i < sectionList.size(); i++) {
			tempSection = sectionList.get(i);
			tempSection.draw(gc);
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
	public ArrayList<AbstractSlitherSection> getSectionList() {
		return sectionList;
	}

	public void addSection(AbstractSlitherSection sect) {
		sectionList.add(sect);
	}

	public void removeSection(AbstractSlitherSection section) {
		this.sectionList.remove(section);
	}

	public void clearAll() {
		this.sectionList.clear();
	}

}
