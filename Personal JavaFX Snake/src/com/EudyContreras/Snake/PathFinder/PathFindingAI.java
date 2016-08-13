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
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

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
public class PathFindingAI {


    private GameManager game;
    private PlayerTwo snakeAI;
    private AbstractObject objective = null;

    private double positionX = 0;
    private double positionY = 0;

    private boolean above = false;
    private boolean toLeft = false;

    private double turnOffset = 50;
    private boolean makingUTurn = false;

    private PlayerMovement turn;

    public PathFindingAI(GameManager game, PlayerTwo snakeAI) {
        this.game = game;
        this.snakeAI = snakeAI;
        initialize();
    }

    public void initialize() {

    }
    public void startSimulation(){
        createPath(findClosest());
    }
    public void updateSimulation(){
        if(game.getStateID()==GameStateID.GAMEPLAY){
			checkCurrentLocation();
			checkObjectiveStatus();
			reRoute();
        }
    }
    private AbstractObject findClosest(){

        Distance[] distance = new Distance[4];

        for(int i = 0; i<game.getGameObjectController().getFruitList().size(); i++){
            distance[i] = new Distance(Math.hypot(snakeAI.getX()-game.getGameObjectController().getFruitList().get(i).getX(), snakeAI.getY()-game.getGameObjectController().getFruitList().get(i).getY()), game.getGameObjectController().getFruitList().get(i));
        }
        double closest = distance[0].distance;

        for(int i = 0; i<distance.length; i++){
            if(distance[i].distance<closest){
                closest = distance[i].distance;
                positionX = distance[i].object.getX();
                positionY = distance[i].object.getY();
                objective = distance[i].object;
            }
        }
//        objective.blowUpAlt();
        return objective;
    }
    private void reRoute(){
        if(makingUTurn){
            turnOffset--;
            if(turnOffset<=0){
                makingUTurn = false;
                createPath(findClosest());
            }
        }
    }
    private void log(String str){
    	System.out.println(str);
    }
    private void createPath(AbstractObject objective){

        toLeft = objective.getX()<snakeAI.getX() ? true : false;
        above = objective.getY()<snakeAI.getY() ? true : false;

        if(Math.abs(snakeAI.getX()-objective.getX())<Math.abs(snakeAI.getY()-objective.getY())){
        	log("x is closest");
//            if(Math.abs(snakeAI.getX()-objective.getX())>GameSettings.WIDTH/2){
//                if(toLeft){
//                    performMove(PlayerMovement.MOVE_RIGHT);
//                }
//                else{
//                    performMove(PlayerMovement.MOVE_LEFT);
//                }
//            }
//            else{
                if(toLeft){
                    performMove(PlayerMovement.MOVE_LEFT);
                }
                else{
                    performMove(PlayerMovement.MOVE_RIGHT);
                }
//            }
        }
        else{
        	log("y is closest");
//            if(Math.abs(snakeAI.getY()-objective.getY())>GameSettings.HEIGHT/2){
//                if(above){
//                    performMove(PlayerMovement.MOVE_DOWN);
//                }
//                else{
//                    performMove(PlayerMovement.MOVE_UP);
//                }
//            }
//            else{
                if(above){
                    performMove(PlayerMovement.MOVE_UP);
                }
                else{
                    performMove(PlayerMovement.MOVE_DOWN);
                }
//            }
        }

    }
    private void checkCurrentLocation(){
        if (objective != null) {
            if (snakeAI.getX() > objective.getX()-objective.getRadius()/2 && snakeAI.getX()< objective.getX()+objective.getRadius()/2) {
                if (above) {
                    snakeAI.setDirection(PlayerMovement.MOVE_UP);
                } else {
                    snakeAI.setDirection(PlayerMovement.MOVE_DOWN);
                }
            } else if (snakeAI.getY() > objective.getY()-objective.getRadius()/2 && snakeAI.getY()< objective.getY()+objective.getRadius()/2) {
                if (toLeft) {
                    snakeAI.setDirection(PlayerMovement.MOVE_LEFT);
                } else {
                    snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);
                }
            }
        }
    }
    private void checkObjectiveStatus(){
        if(objective==null || !objective.isAlive()){
            createPath(findClosest());
        }
        else{
            if(objective.getX()!=positionX || objective.getY()!=positionY){
                createPath(findClosest());
            }
        }
    }
    private void performMove(PlayerMovement move){
        if(move == PlayerMovement.MOVE_UP && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_DOWN){
            makeUTurn(snakeAI.getCurrentDirection());
        }
        if(move == PlayerMovement.MOVE_DOWN && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_UP){
            makeUTurn(snakeAI.getCurrentDirection());
        }
        if(move == PlayerMovement.MOVE_LEFT && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_RIGHT){
            makeUTurn(snakeAI.getCurrentDirection());
        }
        if(move == PlayerMovement.MOVE_RIGHT && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_LEFT){
            makeUTurn(snakeAI.getCurrentDirection());
        }
        else{
        	snakeAI.setDirection(move);
        }
    }
    private void makeUTurn(PlayerMovement move){
        if(move == PlayerMovement.MOVE_DOWN || move == PlayerMovement.MOVE_UP){
            if(toLeft){
                snakeAI.setDirection(PlayerMovement.MOVE_LEFT);
                makingUTurn = true;
                turnOffset = snakeAI.getRadius();
            }
            else{
                snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);
                makingUTurn = true;
                turnOffset = snakeAI.getRadius();
            }
        }
        if(move == PlayerMovement.MOVE_RIGHT || move == PlayerMovement.MOVE_LEFT){
            if(above){
                snakeAI.setDirection(PlayerMovement.MOVE_UP);
                makingUTurn = true;
                turnOffset = snakeAI.getRadius();
            }
            else{
                snakeAI.setDirection(PlayerMovement.MOVE_DOWN);
                makingUTurn = true;
                turnOffset = snakeAI.getRadius();
            }
        }
    }
    private class Distance{
        double distance;
        AbstractObject object;
        public Distance(double distance, AbstractObject object){
            this.distance = distance;
            this.object = object;
        }
    }
    public void setPlayer() {
        this.snakeAI = null;
        this.snakeAI = game.getGameLoader().getPlayerTwo();

    }


}
