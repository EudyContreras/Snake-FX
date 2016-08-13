package com.EudyContreras.Snake.PathFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.ComputerPlayer.SnakeAI;
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
public class PlayerTwoSectionManager {

	private ArrayList<AbstractSection> sectionList;
	private AbstractSection tempSection;
	private GameManager game;
	private LinkedList<AbstractObject> objectList;
	private Point2D[] coordinates = new Point2D[4];
	private SnakeAI snakeAI;
	private double moveDelay = 0;
	private AbstractObject objective = null;

	private boolean above = false;
	private boolean toLeft = false;

	public PlayerTwoSectionManager(GameManager game, SnakeAI snakeAI) {
		this.game = game;
		this.snakeAI = snakeAI;
		initialize();
	}

	public void initialize() {
		this.objectList = game.getGameObjectController().getFruitList();
		this.sectionList = new ArrayList<>();
	}


	private AbstractObject findClosest(){

		Distance[] distance = new Distance[4];

		for(int i = 0; i<objectList.size(); i++){
			distance[i] = new Distance(Math.hypot(snakeAI.getX()-objectList.get(i).getX(), snakeAI.getY()-objectList.get(i).getY()), objectList.get(i));
		}
		double closest = distance[0].distance;

		for(int i = 0; i<distance.length; i++){
			if(distance[i].distance<closest){
				closest = distance[i].distance;
				objective = distance[i].object;
			}
		}
		return objective;
	}
	private void reRoute(){

	}
	private void createPath(AbstractObject objective){

		toLeft = objective.getX()<snakeAI.getX() ? true : false;
		above = objective.getY()<snakeAI.getY() ? true : false;

		if(Math.abs(snakeAI.getX()-objective.getX())<Math.abs(snakeAI.getY()-objective.getY())){
			if(Math.abs(snakeAI.getX()-objective.getX())>GameSettings.WIDTH/2){
				if(toLeft){
					performMove();
				}
				else{
					performMove();
				}
			}
			else{

			}
		}
		else{
			if(Math.abs(snakeAI.getY()-objective.getY())>GameSettings.HEIGHT/2){
				if(above){
					performMove();
				}
				else{
					performMove();
				}
			}
			else{

			}
		}

	}
	private void checkCurrentLocation(){
		if(snakeAI.getX()==objective.getX()){
			if(above){
				snakeAI.setDirection(PlayerMovement.MOVE_UP);
			}else{
				snakeAI.setDirection(PlayerMovement.MOVE_DOWN);
			}
		}
		else if(snakeAI.getY()==objective.getY()){
			if(toLeft){
				snakeAI.setDirection(PlayerMovement.MOVE_LEFT);
			}else{
				snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);
			}
		}
	}
	private void performMove(){

	}
	private class Distance{
		double distance;
		AbstractObject object;
		public Distance(double distance, AbstractObject object){
			this.distance = distance;
			this.object = object;
		}
	}


}
