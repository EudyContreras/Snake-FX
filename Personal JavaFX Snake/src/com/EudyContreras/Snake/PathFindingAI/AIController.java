package com.EudyContreras.Snake.PathFindingAI;

import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.PathFindingAI.CollideNode.RiskFactor;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

/**
 * Class than handles logic behind all the classes that the Object evasion AI
 * depends on.
 *
 * <h1>
 * <li>NOTES:</h1>
 * <li>The AI tells the snake to move towards an objective.
 * <li>The snake moves towards the specified location.
 * <li>While the snake moves it will cast warning which will identify possible
 * collisions. This warnings will be sent to specified locations. If the vector
 * collides with an object it will identify the object as a potential collide
 * target. Once the object is identified as a target a CollideObject is created
 * and added to a collection containing a list of unique collide objects. These
 * objects contain information regarding the dimension, position, the relative
 * location, and the distance between the collide target and the snake.
 * <li>The AI controller checks all collide objects and monitors each of the
 * object's position relative to the snake. It also constantly monitors the
 * distance of the objects relative to the snake.
 * <li>Base on the potential collision targets and the position of the snake, a
 * Turn Monitor will determine all the allowed moves that the snake is allowed
 * to make. The turn monitor will only accept information from collision targets
 * that are within a specified distance range from the snake.
 * <li>If the snake's direction is a direction which the turn monitor has
 * labeled as unsafe or not allowed, the snake will change direction to an
 * allowed direction
 * <li>While the snake is only setting its directions to the allowed ones, it
 * will also consider the best direction among all allowed directions. The best
 * direction is determined by calculating the position of the objective and the
 * current position of the snake.
 * <h1>
 * <li>NOTES:</h1>
 * <li>Rethinks the process.
 * <li>Maybe it is best to pre-calculate turns.
 * <li>Make a mechanism that finds the closest apple.
 * <li>Once the apple has been found calculate how many turns it will take for
 * the snake to reach the apple and where will the turns be made! The pixels
 * between turns must also be calculated. If a turn leads to a collision with an
 * object calculate the objects dimension and the position of the objective. if
 * the object is met while traveling to the right calculate the height of the
 * object. Determine the distance between the snake and the apple if the snake
 * travels up to avoid the object and the distance if the snake travels down.
 * Compute closest distance and place the turn marker and direction
 * <li>Always start trajectory based on the closest path. Calculate both the x
 * and the y distance to the object.
 *
 * @author Eudy Contreras
 *
 */
public class AIController {

    private LinkedList<CollideNode> collideNodes;
    private LinkedList<CollideNode> penaltyNodes;
    private GridNode pathFindingGrid;
    private AIPathFinder pathFindingAI;
    private PlayerTwo snakeAI;
    private GameManager game;

    private boolean hasBeenNotified;

    public AIController(GameManager game) {
        this.game = game;
        this.snakeAI = game.getGameLoader().getPlayerTwo();
        this.collideNodes = new LinkedList<CollideNode>();
        this.penaltyNodes = new LinkedList<CollideNode>();
        this.initialize();
    }

    public void initialize() {
    	hasBeenNotified = false;
        pathFindingGrid = new GridNode(game, this, GameSettings.WIDTH, GameSettings.HEIGHT,GameSettings.PATH_FINDING_CELL_SIZE, 0);
        pathFindingAI = new AIPathFinder(game, this, snakeAI, collideNodes);
        updateGrid();
    }

    public void updateGrid() {
        obtainAllCollideNodes();
        obtainAllPenaltyNodes();

        pathFindingGrid.setColliderList(collideNodes);
        pathFindingGrid.setPenaltiesList(penaltyNodes);

        pathFindingGrid.computeValidCells();
    }

    public void update_AI_Simulation(long timePassed) {
    	if(game.getStateID()==GameStateID.GAMEPLAY){
    		updateAIEvents();
        	updateGridEvents();
    	}
    }
    private void updateGridEvents(){
    	getGrid().markKeyCells();
    }

    public void nofifyAI() {
    	if(!hasBeenNotified){
    		hasBeenNotified = true;
    		pathFindingAI.computePath();
    	}
    }

    private void updateAIEvents() {
        pathFindingAI.updateSimulation();
    }

    public GridNode getGrid() {
        return pathFindingGrid;
    }

    public void obtainAllCollideNodes() {
        clearColliders();
        for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
            AbstractTile blocks = game.getGameLoader().getTileManager().getBlock().get(i);
            addPossibleCollideBlock(new CollideNode(pathFindingAI, blocks,RiskFactor.VERY_HIGH));
        }
        for (int i = 0; i < game.getGameLoader().getTileManager().getTrap().size(); i++) {
            AbstractTile traps = game.getGameLoader().getTileManager().getTrap().get(i);
            addPossibleCollideBlock(new CollideNode(pathFindingAI, traps,RiskFactor.VERY_HIGH));
        }
    }

    public void obtainAllPenaltyNodes(){
        clearPenalties();
        for(AbstractTile dangers: game.getGameLoader().getTileManager().getTile()){
            if(dangers.getId() == GameLevelObjectID.CACTUS){
            	addPossiblePenaltyNode(new CollideNode(pathFindingAI, dangers,RiskFactor.HIGH));
            }
            else if(dangers.getId() == GameLevelObjectID.BUSH){
            	addPossiblePenaltyNode(new CollideNode(pathFindingAI, dangers,RiskFactor.MEDIUM));
            }
            else if(dangers.getId() == GameLevelObjectID.TREE){
            	addPossiblePenaltyNode(new CollideNode(pathFindingAI, dangers,RiskFactor.MEDIUM));
            }
            else if(dangers.getId() == GameLevelObjectID.NO_SPAWN_ZONE){
            	addPossiblePenaltyNode(new CollideNode(pathFindingAI, dangers,RiskFactor.NO_SPAWN_ZONE));
            }
            else{
            	addPossiblePenaltyNode(new CollideNode(pathFindingAI, dangers,RiskFactor.LOW));
            }
        }
    }

    public void addPossibleCollideBlock(CollideNode collideObject) {
        if (!collideNodes.contains(collideObject)) {
            collideNodes.add(collideObject);
        }
    }

    public void addPossiblePenaltyNode(CollideNode penaltyObject) {
        if (!penaltyNodes.contains(penaltyObject)){
            penaltyNodes.add(penaltyObject);
        }
    }

    public boolean isHasBeenNotified() {
		return hasBeenNotified;
	}

	public void setHasBeenNotified(boolean hasBeenNotified) {
		this.hasBeenNotified = hasBeenNotified;
	}

	public void clearColliders() {
        this.collideNodes.clear();
    }

    public void clearPenalties(){
        this.penaltyNodes.clear();
    }

    public LinkedList<CollideNode> getPenaltyNodes() {
        return penaltyNodes;
    }

    public LinkedList<CollideNode> getCollideNodes() {
    	return collideNodes;
    }

    public GridNode getPathFindingGrid() {
        return pathFindingGrid;
    }

    public AIPathFinder getPathFindingAI() {
        return pathFindingAI;
    }

    public CellNode getHeadCell(PlayerTwo snake, int r, int c) {
        return pathFindingGrid.getRelativeHeadCell(snake,r,c);
    }
}
