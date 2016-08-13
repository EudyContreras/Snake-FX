package com.EudyContreras.Snake.PathFinder;

import java.util.Random;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

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

    private AbstractObject objective;
    private GameManager game;
    private PlayerTwo snakeAI;
    private Random rand;

    private double positionX = 0;
    private double positionY = 0;

    private double turnOffset = 100;
    private boolean makingUTurn = false;
    private int randomBoost = 200;

    private ObjectivePosition location;
    private PlayerMovement turn;

    public PathFindingAI(GameManager game, PlayerTwo snakeAI) {
        this.game = game;
        this.snakeAI = snakeAI;
        initialize();
    }

    public void initialize() {
        rand = new Random();
    }
    public void findObjective(){
         createPath(objective);

    }
    public void startSimulation(){
        createPath(findClosest());
    }
    public void updateSimulation(){
        if(game.getStateID()==GameStateID.GAMEPLAY && objective!=null){
            checkCurrentLocation();
            addRandomBoost(true);
            checkObjectiveStatus();
            reRoute();
        }
    }
    public void addRandomBoost(boolean random) {
        if (random && rand.nextInt(randomBoost) != 0) {
            return;
        }
        if(snakeAI!=null){
            if(game.getEnergyBarTwo().getEnergyLevel()>50){
            	snakeAI.setSpeedThrust(true);
            }
            else{
            	snakeAI.setSpeedThrust(false);
            }
        }

    }
    private AbstractObject findClosest(){

        Distance[] distance = new Distance[game.getGameObjectController().getFruitList().size()];

        for(int i = 0; i<game.getGameObjectController().getFruitList().size(); i++){
            distance[i] = new Distance(Math.hypot(snakeAI.getX()-game.getGameObjectController().getFruitList().get(i).getX(), snakeAI.getY()-game.getGameObjectController().getFruitList().get(i).getY()),
                    game.getGameObjectController().getFruitList().get(i));
        }

        double closest = distance[0].getDistance();

        for(int i = 0; i<distance.length; i++){
            if(distance[i].getDistance()<closest){
                closest = distance[i].getDistance();
            }
        }
        for(int i = 0; i<distance.length; i++){
            if(distance[i].getDistance() == closest){
                objective = distance[i].getObject();
                positionX = distance[i].getObject().getX();
                positionY = distance[i].getObject().getY();
            }
        }
//        if(objective!=null)
//        	objective.blowUpAlt();
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
        if (Math.abs(snakeAI.getX() - objective.getX()) < Math.abs(snakeAI.getY() - objective.getY())) {
			if (Math.abs(snakeAI.getX() - objective.getX()) > GameSettings.WIDTH*.75) {
				if (objective.getX() > snakeAI.getX()) {
					log("right");
					location = ObjectivePosition.EAST;
					performMove(PlayerMovement.MOVE_LEFT);

				} else if (objective.getX() < snakeAI.getX()) {
					log("left");
					location = ObjectivePosition.WEST;
					performMove(PlayerMovement.MOVE_RIGHT);
				}
			}
			else {
				if (objective.getX() > snakeAI.getX()) {
					log("right");
					location = ObjectivePosition.EAST;
					performMove(PlayerMovement.MOVE_RIGHT);

				} else if (objective.getX() < snakeAI.getX()) {
					log("left");
					location = ObjectivePosition.WEST;
					performMove(PlayerMovement.MOVE_LEFT);
				}
			}
        }
        else {
			if (Math.abs(snakeAI.getY() - objective.getY()) > GameSettings.HEIGHT*.75) {
				if (objective.getY() > snakeAI.getY()) {
					log("under");
					location = ObjectivePosition.SOUTH;
					performMove(PlayerMovement.MOVE_UP);
				} else if (objective.getY() < snakeAI.getY()) {
					log("above");
					location = ObjectivePosition.NORTH;
					performMove(PlayerMovement.MOVE_DOWN);
				}
			}
			else {
				if (objective.getY() > snakeAI.getY()) {
					log("under");
					location = ObjectivePosition.SOUTH;
					performMove(PlayerMovement.MOVE_DOWN);
				} else if (objective.getY() < snakeAI.getY()) {
					log("above");
					location = ObjectivePosition.NORTH;
					performMove(PlayerMovement.MOVE_UP);
				}
			}
		}
    }
    private void checkCurrentLocation(){
        if (objective != null) {
            if (snakeAI.getX() > objective.getX()-objective.getRadius()/2 && snakeAI.getX()< objective.getX()+objective.getRadius()/2) {
                if (objective.getY()<snakeAI.getY()) {
                    snakeAI.setDirection(PlayerMovement.MOVE_UP);
                } else {
                    snakeAI.setDirection(PlayerMovement.MOVE_DOWN);
                }
            } else if (snakeAI.getY() > objective.getY()-objective.getRadius()/2 && snakeAI.getY()< objective.getY()+objective.getRadius()/2) {
                if (objective.getX()<snakeAI.getX()) {
                    snakeAI.setDirection(PlayerMovement.MOVE_LEFT);
                } else {
                    snakeAI.setDirection(PlayerMovement.MOVE_RIGHT);
                }
            }
        }
    }
    private void checkObjectiveStatus(){
        if(objective.isRemovable()){
             findClosest();
        }
        if(objective.getX()!=positionX || objective.getY()!=positionY){
           findClosest();
        }
    }
    private void performMove(PlayerMovement move){
        if(move == PlayerMovement.MOVE_UP && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_DOWN){
            makeUTurn(snakeAI.getCurrentDirection());
        }
        else if(move == PlayerMovement.MOVE_DOWN && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_UP){
            makeUTurn(snakeAI.getCurrentDirection());
        }
        else if(move == PlayerMovement.MOVE_LEFT && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_RIGHT){
            makeUTurn(snakeAI.getCurrentDirection());
        }
        else if(move == PlayerMovement.MOVE_RIGHT && snakeAI.getCurrentDirection() == PlayerMovement.MOVE_LEFT){
            makeUTurn(snakeAI.getCurrentDirection());
        }
        else{
            snakeAI.setDirection(move);
        }
    }
    private void makeUTurn(PlayerMovement move){
        if(move == PlayerMovement.MOVE_DOWN || move == PlayerMovement.MOVE_UP){
            if(objective.getX()<snakeAI.getX()){
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
        else if(move == PlayerMovement.MOVE_RIGHT || move == PlayerMovement.MOVE_LEFT){
            if(objective.getY()<snakeAI.getY()){
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

        private Double distance;
        private AbstractObject object;

        public Distance(double distance, AbstractObject object){
            this.distance = distance;
            this.object = object;
        }
        public double getDistance(){
        	return distance;
        }
        public AbstractObject getObject(){
        	return object;
        }
    }
    public void setPlayer() {
        this.snakeAI = null;
        this.snakeAI = game.getGameLoader().getPlayerTwo();

    }
    private enum ObjectivePosition{
        NORTH,SOUTH,WEST,EAST
    }

}
