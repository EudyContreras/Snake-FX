package com.EudyContreras.Snake.PathFindingAI;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.Managers.ThreadManager;
import com.EudyContreras.Snake.PathFindingAI.CellNode.Direction;
import com.EudyContreras.Snake.PathFindingAI.LinkedPath.ConnectionType;
import com.EudyContreras.Snake.PathFindingAI.Objective.SortingType;
import com.EudyContreras.Snake.PathFindingAI.PathWrapper.PathFlag;
import com.EudyContreras.Snake.PathFindingAI.SearchAlgorithm.PathType;
import com.EudyContreras.Snake.PathFindingAI.SearchAlgorithm.Reach;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.geometry.Rectangle2D;

/**
 *
 * @author Eudy Contreras
 * TODO: Trigger PATH finding AI only when a collision happens !!!
 *
 * Make it so that if the snake is close to an obstacle it will activate
 * path finding else it will deactivate it!
 *
 * TODO: Make the snake go for the closest only if the path is available
 * if the path is not available use a hierarchy system. If the path is close
 * check if the closest border is opened and if open check if the farthest border
 * is open! if both borders are open teleport!
 *
 * TODO: Do not allow tracking apples which are next to the snakes body. if the closest
 * apple is one cell away from snakes body. Check next closest!
 */
public class AIPathFinder {

    private LinkedPath<PathWrapper> pathCoordinates;
    private LinkedList<PathRequest> pathRequests;
    private SearchAlgorithm pathFinder;
    private AIController controller;
    private GameManager game;
    private PlayerTwo snakeAI;
    private GridNode grid;
    private Random rand;

    private boolean logDirections = false;
    private boolean allowChecks = false;
    private boolean teleporting = false;
    private boolean safetyCheck = false;
    private boolean allowUpdate = false;
    private boolean onPath = true;

    private int checkTimer = 0;
    private int updateTimer = 0;
    private int safetyCheckTimer = 0;
    private int randomBoost = 200;

    private DistressLevel distressLevel;
    private CurrentGoal currentGoal;
    private ActionType currentState;
    private Direction lastStep;

    public AIPathFinder(GameManager game, AIController controller, PlayerTwo snakeAI, LinkedList<CollideNode> possibleColliders) {
        this.game = game;
        this.controller = controller;
        this.snakeAI = snakeAI;
        this.grid = controller.getGrid();
        this.initialize();
    }

    public void initialize() {
        rand = new Random();
        pathRequests = new LinkedList<>();
        pathFinder = new SearchAlgorithm(game);
        currentGoal = CurrentGoal.OBJECTIVE;
        distressLevel = DistressLevel.NORMAL;
        currentState = ActionType.FIND_PATH;
        lastStep = Direction.NONE;
    }

    public void findObjective() {
        switch (currentState) {
        case DODGE_OBSTACLES:
            break;
        case FIND_PATH:
            break;
        case FREE_MODE:
            if (game.getModeID() == GameModeID.LocalMultiplayer && GameSettings.ALLOW_AI_CONTROLL)
                createPath();
            break;
        default:
            break;

        }
    }

    /*
     * Gets called when the game begins in order to initiate the simulation
     */
    public void startSimulation() {
        if (game.getModeID() == GameModeID.LocalMultiplayer && GameSettings.ALLOW_AI_CONTROLL){
			snakeAI.setDirectCoordinates(PlayerMovement.MOVE_DOWN);
            currentGoal = CurrentGoal.OBJECTIVE;
            distressLevel = DistressLevel.NORMAL;
            currentState = ActionType.FIND_PATH;
            lastStep = Direction.NONE;
            logDirections = false;
            allowChecks = false;
            teleporting = false;
            safetyCheck = true;
            onPath = true;
            checkTimer = 0;
            safetyCheckTimer = 0;
            randomBoost = 200;
            computePath();
        }
    }

    /*
     * this method gets called from the game loop and it is called at 60fps. The
     * method update and keeps track of things
     */
    public void updateSimulation() {
        if (game.getModeID() == GameModeID.LocalMultiplayer) {
            if (game.getStateID() != GameStateID.GAME_MENU) {

                performLocationBasedAction();
//                addRandomBoost(true);
                if (allowChecks && currentGoal == CurrentGoal.OBJECTIVE) {
                    checkTimer++;
                    if (checkTimer >= 200) {
                        pathRequests.add(new PathRequest(this,CurrentGoal.OBJECTIVE));
                        checkTimer = 0;
                    }
                }if (safetyCheck && currentGoal == CurrentGoal.TAIL) {
                    safetyCheckTimer++;

                    if (safetyCheckTimer >= 600) {
                        currentGoal = CurrentGoal.OBJECTIVE;
                        pathRequests.add(new PathRequest(this,CurrentGoal.OBJECTIVE));
                        safetyCheckTimer = 0;
                    }
                }if (safetyCheck && currentGoal == CurrentGoal.FARTHEST_CELL) {
                    safetyCheckTimer++;

                    if (safetyCheckTimer >= 300) {
                    	currentGoal = CurrentGoal.TAIL;
                        pathRequests.add(new PathRequest(this,CurrentGoal.TAIL));
                        safetyCheckTimer = 0;
                    }
                }
            }
        }
    }

    public CellNode GET_FARTHEST_CELL(CellNode from) {
    	return ThreadManager.computeValue(()->{
    		  return pathFinder.GET_FARTHEST_CELL(grid, from);
    	});
    }

    public CellNode GET_AVAILABLE_CELL(CellNode from, Reach reach) {
    	return ThreadManager.computeValue(()->{
    		 return pathFinder.GET_AVAILABLE_CELL(grid, from, reach);
    	});
    }

    public LinkedList<PathWrapper> GET_BRUTE_PATH(CellNode from) {
    	return ThreadManager.computeValue(()->{
    		return pathFinder.GET_BRUTE_PATH(snakeAI, grid, from, 5);
    	});
    }

    public LinkedList<PathWrapper> GET_LONGEST_LIST(List<LinkedList<PathWrapper>> lists) {
    	return ThreadManager.computeValue(()->{
    		return pathFinder.GET_LONGEST_LIST(lists);
    	});
    }

    public LinkedList<PathWrapper> GET_SHORTEST_LIST(List<LinkedList<PathWrapper>> lists) {
    	return ThreadManager.computeValue(()->{
    		return pathFinder.GET_SHORTEST_LIST(lists);
    	});
    }

    public LinkedList<PathWrapper> GET_LONGEST_PATH_POLY(CellNode start, CellNode objective) {
    	return ThreadManager.computeValue(()->{
    		 return pathFinder.GET_LONGEST_PATH_POLY(snakeAI, grid, start, objective, distressLevel);
    	});
    }

    public LinkedList<PathWrapper> GET_BFS_PATH(CellNode start, CellNode objective) {
    	return ThreadManager.computeValue(()->{
    		return pathFinder.GET_BFS_PATH(snakeAI,grid,start,objective,distressLevel);
    	});
    }

    public LinkedList<PathWrapper> GET_DFS_PATH(CellNode start, CellNode objective) {
    	return ThreadManager.computeValue(()->{
    		return pathFinder.GET_DFS_PATH(snakeAI,grid,start,objective,distressLevel);
    	});
    }

    public boolean QUICK_PATH_CHECK(CellNode start, CellNode objective) {
    	return ThreadManager.computeValue(()->{
    		return pathFinder.QUICK_PATH_SEARCH(grid, start, objective);
    	});
    }

    public LinkedList<PathWrapper> GET_ASTAR_PATH(CellNode start, CellNode objective) {
    	return ThreadManager.computeValue(()->{
    		return pathFinder.GET_ASTAR_PATH(snakeAI,grid,start,objective,distressLevel);
    	});
    }

    public LinkedList<PathWrapper> GET_ASTAR_LONGEST_PATH(CellNode start, CellNode objective) {
    	return ThreadManager.computeValue(()->{
    		return pathFinder.GET_ASTAR_LONGEST_HYBRID_PATH(snakeAI,grid,start,objective);
    	});
    }

    public LinkedPath<PathWrapper> GET_SAFE_ASTAR_PATH(CellNode start, CellNode objective, CellNode tail){
    	return ThreadManager.computeValue("Safe Path",()->{
    		return pathFinder.GET_SAFE_ASTAR_PATH(snakeAI,grid, start, objective, tail, distressLevel);
    	});
    }

    public LinkedList<PathWrapper> GET_SAFE_ASTAR_PATH(CellNode start, CellNode objective, CurrentGoal goal, DistressLevel distressLevel){
    	return ThreadManager.computeValue(()->{
    		return pathFinder.GET_SAFE_ASTAR_PATH(snakeAI,grid, start,objective,goal,distressLevel);
    	});
    }

    public LinkedList<Objective> getObjectives(CellNode start, GoalSearch goalSearch, SortingType sorting){

        LinkedList<Objective> objectives = new LinkedList<>();

        switch(goalSearch){
        case CLOSEST_OBJECTIVE:
            for (AbstractObject object : game.getGameObjectController().getObsFruitList()) {
                Objective objective = new Objective(snakeAI, object, sorting);
                objectives.add(objective);
            }

            Collections.sort(objectives);

            return objectives;
        case SHORTEST_PATH:
            PriorityQueue<LinkedPath<PathWrapper>> paths = new PriorityQueue<LinkedPath<PathWrapper>>(getObjectiveCount(), new PathLengthComparator());

            for (int i = 0; i<game.getGameObjectController().getObsFruitList().size(); i++) {
            	AbstractObject object = game.getGameObjectController().getObsFruitList().get(i);
                Objective objective = new Objective(snakeAI, object);
                paths.add(new LinkedPath<PathWrapper>(GET_ASTAR_PATH(start, objective.getCell()),new LinkedList<PathWrapper>(), objective));
            }

            while (paths.peek() != null) {
                objectives.add(paths.poll().getObjective());
            }

            return objectives;
        }
        return objectives;
    }

    public void computePath(){
        log("NOT ACCURATE");
        computePath(null);
    }

    public void computePath(CellNode head){

        teleporting = false;

        CellNode start = head!=null ? head : controller.getHeadCell(snakeAI);

//        boolean inBound = start.getBoundsCheck().contains(snakeAI.getAIBounds());
//
//    	if(!inBound){
//    		log("No Bounds");
//    	}

        CellNode tail = grid.getRelativeTailCell(snakeAI);

        LinkedPath<PathWrapper> path = null;

        switch(currentGoal){
        case OBJECTIVE:

            LinkedList<Objective> newObjectives = getObjectives(start,GoalSearch.SHORTEST_PATH,SortingType.CLOSEST);

            pathFinder.setPathType(PathType.SHORTEST_PATH);

            if (newObjectives.size() > 0) {
//                if (newObjectives.get(0) != null && GameSettings.SHOW_ASTAR_GRAPH) {
//                    newObjectives.get(0).getObject().blowUpAlt();
//                }

                if (start != null) {
                    if (!start.isDangerZone()) {
                        distressLevel = DistressLevel.NORMAL;
                    }

                    for(int i = 0; i < newObjectives.size(); i++){
//						if(!grid.isNeighborNot(newObjectives.get(i).getCell(), Flag.OCCUPIED))
//							continue;
                        path = checkObjectiveReach(start, newObjectives.get(i), i, newObjectives);
                        if(path!=null){
                            break;
                        }
                    }
                    if(path == null){
                        distressLevel = DistressLevel.DISTRESSED;

                        for(int i = 0; i < newObjectives.size(); i++){
//							if(!grid.isNeighborNot(newObjectives.get(i).getCell(), Flag.OCCUPIED))
//								continue;
                            path = checkObjectiveReach(start, newObjectives.get(i), i, newObjectives);
                            if(path!=null){
                                break;
                            }
                        }
                    }

                    if(path!=null){
                        path.setPathType(PathType.SHORTEST_PATH);
                        submitPath(path);
                    }else{
                        currentGoal = CurrentGoal.TAIL;
                        removeThrust();
                        computePath(start);
//                        pathRequests.clear();
//                        pathRequests.add(new PathRequest(this,currentGoal));
                    }
                }
            }

            break;
        case TAIL:

            pathFinder.setPathType(PathType.LONGEST_PATH);

            if (tail != null) {
                  if (!start.isDangerZone()) {
                      distressLevel = DistressLevel.NORMAL;
                  }
                  path = new LinkedPath<>(GET_LONGEST_PATH_POLY(start, tail), new LinkedList<>()).setPathType(PathType.LONGEST_PATH);

                if (!path.getPathOne().isEmpty()) {
                    submitPath(path);
                } else {
                    log("Path to tail empty!");
                      distressLevel = DistressLevel.DISTRESSED;

                      path = new LinkedPath<>(GET_LONGEST_PATH_POLY(start, tail), new LinkedList<>()).setPathType(PathType.LONGEST_PATH);

                      if (!path.getPathOne().isEmpty()) {
                          submitPath(path);
                      } else {
                          log("Emergency path to tail empty!");
                          currentGoal = CurrentGoal.FARTHEST_CELL;
                          computePath(start);
//                          pathRequests.clear();
//                          pathRequests.add(new PathRequest(this,currentGoal));
//
//							distressLevel = DistressLevel.DISTRESSED;
//
//							LinkedList<Objective> objectives = getObjectives(start,GoalSearch.CLOSEST_OBJECTIVE, SortingType.FARTHEST);
//
//							while(!objectives.isEmpty()){
//
//								path = emergencyTeleport(grid, start, objectives.removeFirst().getCell());
//
//								if(path!=null){
//									break;
//								}
//							}
//
//							if (path!=null) {
//								showPathToObjective(path);
//							} else {
//
//							log("Emergency teleport path to tail empty!");
//								currentGoal = CurrentGoal.FARTHEST_CELL;
//								computePath();
//							}
                      }
                  }
            }break;
        case FARTHEST_CELL:

            pathFinder.setPathType(PathType.SHORTEST_PATH);

            if (start != null) {
                LinkedList<Objective> objectives = getObjectives(start, GoalSearch.CLOSEST_OBJECTIVE,SortingType.CLOSEST);

                while (!objectives.isEmpty()) {

                    path = new LinkedPath<>(GET_LONGEST_PATH_POLY(start, GET_FARTHEST_CELL(objectives.poll().getCell())),new LinkedList<>());

                    if (!path.getPathOne().isEmpty()) {
                        break;
                    }
                }if (!path.getPathOne().isEmpty()) {
                    path.setPathType(PathType.LONGEST_PATH);
                    submitPath(path);

                } else {
                    log("Path to farthest cell from tail not found!");
                    path = new LinkedPath<>(GET_LONGEST_PATH_POLY(start, GET_FARTHEST_CELL(start)), new LinkedList<>());

                    if (!path.getPathOne().isEmpty()) {
                        path.setPathType(PathType.LONGEST_PATH);
                        submitPath(path);
                    }else {
                        log("Path to farthest cell from objective not found!");
                        path = new LinkedPath<>(GET_LONGEST_PATH_POLY(start, GET_FARTHEST_CELL(tail)), new LinkedList<>());

                        if (!path.getPathOne().isEmpty()) {
                            path.setPathType(PathType.LONGEST_PATH);
                            submitPath(path);
                        }  else {
                        	 log("Path to farthest cell from tail not found!");
                             path = new LinkedPath<>(GET_DFS_PATH(start, GET_AVAILABLE_CELL(start, Reach.FARTHEST)), new LinkedList<>());

                             if (!path.getPathOne().isEmpty()) {
                                 path.setPathType(PathType.LONGEST_PATH);
                                 submitPath(path);
                             }else{
                                 log("Path to farthest edge cell not found!");
                                 currentGoal = CurrentGoal.TAIL;
                         }
                    }
                }
            }
            }
            break;
        }
        controller.setHasBeenNotified(false);

    }

    /**
     * TODO: Perform a check to determine if the computed path to the objective is a safe path
     * by computing a path from the start to the objective and from the objective to the tail of the snake.
     * The path must be computed as a special path which considers the path to the objective to be an obstacle.
     * Create a special path made out of special obstacles from the start to goal. The path must be an abstract path. Once
     * that path is created then compute a path from the goal to the tail of the snake, the path must ignore nodes that
     * belong to the path from start to goal. If a path can be created from goal to tail then the given a path can
     * be consider somewhat "Safe"!!. If the path is safe allow the snake to go for the apple. but if the path isnt safe
     */

    private boolean safePathChecker(){

        LinkedList<Objective> newObjectives = new LinkedList<>();

        PriorityQueue<LinkedPath<PathWrapper>>  paths = new PriorityQueue<LinkedPath<PathWrapper>>(getObjectiveCount(), new PathLengthComparator());

        CellNode start = controller.getHeadCell(snakeAI);

        LinkedPath<PathWrapper> path = new LinkedPath<>();

        pathFinder.setPathType(PathType.SHORTEST_PATH);

        for (int i = 0; i < getObjectiveCount(); i++) {
            AbstractObject object = game.getGameObjectController().getObsFruitList().get(i);
            Objective objective = new Objective(snakeAI, object);
            paths.add(new LinkedPath<PathWrapper>(GET_ASTAR_PATH(start, objective.getCell()),new LinkedList<PathWrapper>(), objective));
        }

        while (paths.peek() != null) {
            newObjectives.add(paths.poll().getObjective());
        }

        if (newObjectives.size() > 0) {

            if (start != null) {
                distressLevel = DistressLevel.DISTRESSED;

                for(int i = 0; i < newObjectives.size(); i++){
                    path = new LinkedPath<PathWrapper>(GET_DFS_PATH(start, newObjectives.get(i).getCell()), new LinkedList<PathWrapper>());
                    if(!path.isPathOneEmpty()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public LinkedPath<PathWrapper> checkObjectiveReach(CellNode start, Objective objective, int index, LinkedList<Objective> objectives){
        if(start!=null){

            CellNode tail = grid.getRelativeTailCell(snakeAI);

			if((objectives.get(index).getDistance()) > objectives.get(objectives.size()-1).getInterpolarDistance(start.getLocation().getX(),start.getLocation().getY())){
			LinkedPath<PathWrapper> path = computeInterpolarDirection(start,objective,tail,objectives);
			if(path!=null){
				return path;
			}else{
				path = GET_SAFE_ASTAR_PATH(start, objective.getCell(),tail);

				if(path.isPathSafe()){
					return path;
				}
			}
		}
		else if((objectives.get(index).getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.4) && objectives.get(index).getYDistance(start.getLocation().getY())<GameSettings.HEIGHT*.4){
			LinkedPath<PathWrapper> path = computeInterpolarDirection(start,objective,tail,objectives);
			if(path!=null){
				return path;
			}else{
				path = GET_SAFE_ASTAR_PATH(start, objective.getCell(),tail);

				if(path.isPathSafe()){
					return path;
				}
			}
		}
		else if(objectives.get(index).getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.5 && objectives.get(index).getXDistance(start.getLocation().getX())<GameSettings.WIDTH*.5){
			LinkedPath<PathWrapper> path = computeInterpolarDirection(start,objective,tail,objectives);
			if(path!=null){
				return path;
			}else{
				path = GET_SAFE_ASTAR_PATH(start, objective.getCell(),tail);

				if(path.isPathSafe()){
					return path;
				}
			}
		}
		else if(objectives.get(index).getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.5 && objectives.get(index).getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.5){
			LinkedPath<PathWrapper> path = computeInterpolarDirection(start,objective,tail,objectives);
			if(path!=null){
				return path;
			}else{
				path = GET_SAFE_ASTAR_PATH(start, objective.getCell(),tail);

				if(path.isPathSafe()){
					return path;
				}
			}
		}else {

			LinkedPath<PathWrapper> path = GET_SAFE_ASTAR_PATH(start, objective.getCell(), tail);

			if (path.isPathSafe()) {
				return path;
			}
		}
		//TODO: Find additional conditions that may qualify for interpolation


		}
        return null;
    }
    /*
     * Unsafe amd unchecked teleport method which triggers a teleportation when a the distance
     * between the snake and the objective is above a certain threshold. The calculations are made
     * based on relational distance planes.
     */

    private LinkedPath<PathWrapper> computeInterpolarDirection(CellNode start, Objective objective, CellNode tail, LinkedList<Objective> objectives) {

        if((objective.getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.45) && objective.getYDistance(start.getLocation().getY())<GameSettings.HEIGHT*.45){

            if(start.getLocation().getX() > GameSettings.WIDTH*.65){
                return getPortal(grid, start, tail, objective, objectives, CardinalPoint.EAST);
            }
            else if(start.getLocation().getX() < GameSettings.WIDTH*.35){
                return getPortal(grid, start, tail, objective, objectives, CardinalPoint.WEST);
            }
        }
        else if(objective.getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.45  && objective.getXDistance(start.getLocation().getX())<GameSettings.WIDTH*.45){

            if(start.getLocation().getY() < GameSettings.HEIGHT*.35){
                return getPortal(grid, start, tail, objective, objectives, CardinalPoint.NORTH);
            }
            else if(start.getLocation().getY() > GameSettings.HEIGHT*.65){
                return getPortal(grid, start, tail, objective, objectives, CardinalPoint.SOUTH);
            }
        }
//        else if(objective.getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.45 && objective.getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.45){
//
//        }
        return null;
    }

    private LinkedPath<PathWrapper> emergencyTeleport(GridNode grid, CellNode start, CellNode  end) {
        LinkedPath<PathWrapper> path;

        if(start.getLocation().getX() > GameSettings.WIDTH*.55 && start.getLocation().getY() > GameSettings.HEIGHT*.35 && start.getLocation().getY() < GameSettings.HEIGHT*.65){
            path = getSafeBorderPath(grid, start, end, null, 500, CardinalPoint.EAST,TeleportationImportance.EMERGENCY);
            if(path!=null){
                return path;
            }
        }
        if(start.getLocation().getX() < GameSettings.WIDTH*.45 && start.getLocation().getY() > GameSettings.HEIGHT*.35 && start.getLocation().getY() < GameSettings.HEIGHT*.65){
            path = getSafeBorderPath(grid, start, end, null, 500, CardinalPoint.WEST,TeleportationImportance.EMERGENCY);
            if(path!=null){
                return path;
            }
        }
        if(start.getLocation().getY() > GameSettings.HEIGHT*.55 && start.getLocation().getX() > GameSettings.WIDTH*.35 && start.getLocation().getX() < GameSettings.WIDTH*.65){
            path = getSafeBorderPath(grid, start, end, null, 500, CardinalPoint.SOUTH,TeleportationImportance.EMERGENCY);
            if(path!=null){
                return path;
            }
        }
        if(start.getLocation().getY() < GameSettings.HEIGHT*.45 && start.getLocation().getX() > GameSettings.WIDTH*.35 && start.getLocation().getX() < GameSettings.WIDTH*.65){
            path = getSafeBorderPath(grid, start, end, null, 500, CardinalPoint.NORTH,TeleportationImportance.EMERGENCY);
            if(path!=null){
                return path;
            }
        }
        return null;
    }


    public LinkedPath<PathWrapper> getPortal(GridNode grid, CellNode start, CellNode tail, Objective objective, LinkedList<Objective> objectives, CardinalPoint orientation){

        LinkedPath<PathWrapper> path = null;
        CellNode portalIn;
        CellNode portalOut;

        double normalRange = 150;

        int index = 0;
        int searchCount = 0;
        int cellIndex = 0;

        int [] indexes = {0,-1,1,-2,2,-3,3};

        switch(orientation){
        case EAST:
            while (searchCount < indexes.length) {
                cellIndex = start.getIndex().getCol() + (indexes[index]);
                if (cellIndex > grid.getColumnCount() - 1 || cellIndex < grid.getMinCol()) {
                    index++;
                    searchCount++;
                    continue;
                }
                portalIn = grid.getCell(grid.getRowCount() - 1, cellIndex);
                if (!portalIn.isOccupied()) {
                    portalOut = grid.getCell(grid.getMinRow(), cellIndex);
                    if (isPortalOutSafe(grid, portalOut, 2, CardinalPoint.WEST)) {
                        path = findPortalCell(grid, portalIn, portalOut, start, objectives);
                        if (path!=null) {
                            lastStep = Direction.RIGHT;
                            teleporting = true;
                            return path;
                        } else {
                            path = getSafeBorderPath(grid, start, tail, objectives, normalRange, CardinalPoint.EAST, TeleportationImportance.NORMAL);
                            if (path != null) {
                                return path;
                            }
                        }
                    }
                }
                index++;
                searchCount++;
            }
            break;
        case WEST:
            while (searchCount < indexes.length) {
                cellIndex = start.getIndex().getCol() + (indexes[index]);
                if (cellIndex > grid.getColumnCount() - 1 || cellIndex < grid.getMinCol()) {
                    index++;
                    searchCount++;
                    continue;
                }
                portalIn = grid.getCell(grid.getMinRow(), cellIndex);
                if (!portalIn.isOccupied()) {
                    portalOut = grid.getCell(grid.getRowCount() - 1, cellIndex);
                    if (isPortalOutSafe(grid, portalOut, 2, CardinalPoint.EAST)) {
                        path = findPortalCell(grid, portalIn, portalOut, start, objectives);
                        if (path!=null) {
                            lastStep = Direction.LEFT;
                            teleporting = true;
                            return path;
                        } else {
                            path = getSafeBorderPath(grid, start, tail, objectives, normalRange, CardinalPoint.WEST, TeleportationImportance.NORMAL);
                            if (path != null) {
                                return path;
                            }
                        }
                    }
                }
                index++;
                searchCount++;
            }
            break;
        case NORTH:
            while (searchCount < indexes.length) {
                cellIndex = start.getIndex().getRow() + (indexes[index]);
                if (cellIndex > grid.getRowCount() - 1 || cellIndex < grid.getMinRow()) {
                    index++;
                    searchCount++;
                    continue;
                }
                portalIn = grid.getCell(cellIndex,grid.getMinCol());
                if (!portalIn.isOccupied()) {
                    portalOut = grid.getCell(cellIndex,grid.getColumnCount()-1);
                    if (isPortalOutSafe(grid, portalOut, 2, CardinalPoint.SOUTH)) {
                        path = findPortalCell(grid, portalIn, portalOut, start, objectives);
                        if (path!=null) {
                            lastStep = Direction.UP;
                            teleporting = true;
                            return path;
                        } else {
                            path = getSafeBorderPath(grid, start, tail, objectives, normalRange, CardinalPoint.NORTH, TeleportationImportance.NORMAL);
                            if (path != null) {
                                return path;
                            }
                        }
                    }
                }
                index++;
                searchCount++;
            }
            break;
        case SOUTH:
            while (searchCount < indexes.length) {
                cellIndex = start.getIndex().getRow() + (indexes[index]);
                if (cellIndex > grid.getRowCount() - 1 || cellIndex < grid.getMinRow()) {
                    index++;
                    searchCount++;
                    continue;
                }
                portalIn = grid.getCell(cellIndex,grid.getColumnCount()-1);
                if (!portalIn.isOccupied()) {
                    portalOut = grid.getCell(cellIndex,grid.getMinCol());
                    if (isPortalOutSafe(grid, portalOut, 2, CardinalPoint.NORTH)) {
                        path = findPortalCell(grid, portalIn, portalOut, start, objectives);
                        if (path!=null) {
                            lastStep = Direction.DOWN;
                            teleporting = true;
                            return path;
                        } else {
                            path = getSafeBorderPath(grid, start, tail, objectives, normalRange, CardinalPoint.SOUTH, TeleportationImportance.NORMAL);
                            if (path != null) {
                                return path;
                            }
                        }
                    }
                }
                index++;
                searchCount++;
            }
            break;
        }
        return null;
    }
    public LinkedList<PathWrapper> getSafeEnterPath(GridNode grid, PortalWrapper portalWrapper, CellNode start, CardinalPoint orientation){
        int index = 0;
        int searchCount = 0;
        int cellIndex = 0;
        int [] indexes = {0,-1,1,-2,2,-3,3};

        switch(orientation){
        case EAST:
            while(searchCount < indexes.length){
                cellIndex = start.getIndex().getCol() + (indexes[index]);
                if(cellIndex > grid.getColumnCount()-1 || cellIndex < grid.getMinCol()){
                    index++; searchCount++;
                    continue;
                }
                CellNode portal = grid.getCell(grid.getRowCount()-1,cellIndex);
                if(!portal.isOccupied()){
                    LinkedList<PathWrapper> path = GET_ASTAR_PATH(start, portal);
                    if(!path.isEmpty()){
                        portalWrapper.setPortalIn(portal);
                        return path;
                    }
                }
                index++;
                searchCount++;
            }
            break;
        case WEST:
            while(searchCount < indexes.length){
                cellIndex = start.getIndex().getCol() + (indexes[index]);
                if(cellIndex > grid.getColumnCount()-1 || cellIndex < grid.getMinCol()){
                    index++; searchCount++;
                    continue;
                }
                CellNode portal = grid.getCell(grid.getMinRow(),cellIndex);
                if(!portal.isOccupied()){
                    LinkedList<PathWrapper> path = GET_ASTAR_PATH(start, portal);
                    if(!path.isEmpty()){
                        portalWrapper.setPortalIn(portal);
                        return path;
                    }
                }
                index++;
                searchCount++;
            }
            break;
        case NORTH:
            while(searchCount < indexes.length){
                cellIndex = start.getIndex().getRow() + (indexes[index]);
                if(cellIndex > grid.getRowCount()-1 || cellIndex < grid.getMinRow()){
                    index++; searchCount++;
                    continue;
                }
                CellNode portal = grid.getCell(cellIndex,grid.getMinCol());
                if(!portal.isOccupied()){
                    LinkedList<PathWrapper> path = GET_ASTAR_PATH(start, portal);
                    if(!path.isEmpty()){
                        portalWrapper.setPortalIn(portal);
                        return path;
                    }
                }
                index++;
                searchCount++;
            }
            break;
        case SOUTH:
            while(searchCount < indexes.length){
                cellIndex = start.getIndex().getRow() + (indexes[index]);
                if(cellIndex > grid.getRowCount()-1 || cellIndex < grid.getMinRow()){
                    index++; searchCount++;
                    continue;
                }
                CellNode portal = grid.getCell(cellIndex,grid.getColumnCount()-1);
                if(!portal.isOccupied()){
                    LinkedList<PathWrapper> path = GET_ASTAR_PATH(start, portal);
                    if(!path.isEmpty()){
                        portalWrapper.setPortalIn(portal);
                        return path;
                    }
                }
                index++;
                searchCount++;
            }
            break;
        }
        return null;
    }

    public boolean isPortalOutSafe(GridNode grid, CellNode portalOut, int depth, CardinalPoint orientation){
        switch (orientation) {
        case EAST:
            for (int i = 0; i <= depth; i++) {
                if (grid.getCell(portalOut.getIndex().getRow() - i, portalOut.getIndex().getCol()).isOccupied()) {
                    return false;
                }
            }
            return true;
        case WEST:
            for (int i = 0; i <= depth; i++) {
                if (grid.getCell(portalOut.getIndex().getRow() + i, portalOut.getIndex().getCol()).isOccupied()) {
                    return false;
                }
            }
            return true;
        case NORTH:
            for (int i = 0; i <= depth; i++) {
                if (grid.getCell(portalOut.getIndex().getRow(), portalOut.getIndex().getCol() + i).isOccupied()) {
                    return false;
                }
            }
            return true;
        case SOUTH:
            for (int i = 0; i <= depth; i++) {
                if (grid.getCell(portalOut.getIndex().getRow(), portalOut.getIndex().getCol() - i).isOccupied()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Condition which checks the relative location of the player and the calculates which edge
     * the player is closest too. Once the closest edge is determined we check which cells on that edge
     * can be reached! if a cell is found the function then checks if the opposite cell at the same row/column
     * is also accessible if yes a path is created and the player teleports to the opposite side of the screen, at
     * the specified cell. If not the search will continue until a cell has been found. If no cell the is found
     * the player will stall until a path is open or until it dies!
     *
     * TODO: Make sure a path can be found from the cross polar cell to the cross polar objective!
     * @param start
     * @return
     */

    public LinkedPath<PathWrapper> getSafeBorderPath(GridNode grid, CellNode start, CellNode tail, LinkedList<Objective> objectives, double searchRange, CardinalPoint orientation, TeleportationImportance importance){
        LinkedPath<PathWrapper> borderPath = null;

        switch(orientation){
        case EAST:
            List<CellNode> eastBorder =  grid.getTeleportZoneEast();
            for (CellNode portalIn : eastBorder) {
                if(portalIn.isOccupied()){
                    continue;
                }
                if(portalIn.getDistanceFrom(start)<searchRange){
                    CellNode portalOut = grid.getCell(grid.getMinRow(),portalIn.getIndex().getCol());

                    if (!isPortalOutSafe(grid, portalOut, 2, CardinalPoint.WEST)) {
                        continue;
                    }
                    if(importance == TeleportationImportance.NORMAL){
                        borderPath = findPortalCell(grid, portalIn, portalOut, start, objectives);
                    }else if(importance == TeleportationImportance.EMERGENCY){
                        borderPath = findPortalCell(grid, portalIn, portalOut, start, tail);
                    }
                    if (borderPath!=null) {
                        lastStep = Direction.RIGHT;
                        teleporting = true;
                        return borderPath;
                    }
                }
            }
            break;
        case WEST:
            List<CellNode> westBoder =  grid.getTeleportZoneWest();
            for (CellNode portalIn : westBoder) {
                if(portalIn.isOccupied()){
                    continue;
                }
                if(portalIn.getDistanceFrom(start)<searchRange){
                    CellNode portalOut = grid.getCell(grid.getRowCount()-1,portalIn.getIndex().getCol());

                    if (!isPortalOutSafe(grid, portalOut, 2, CardinalPoint.EAST)) {
                        continue;
                    }
                    if(importance == TeleportationImportance.NORMAL){
                        borderPath = findPortalCell(grid, portalIn, portalOut, start, objectives);
                    }else if(importance == TeleportationImportance.EMERGENCY){
                        borderPath = findPortalCell(grid, portalIn, portalOut, start, tail);
                    }
                    if (borderPath!=null) {
                        lastStep = Direction.LEFT;
                        teleporting = true;
                        return borderPath;
                    }
                }
            }
            break;
        case NORTH:
            List<CellNode> northBorder =  grid.getTeleportZoneNorth();
            for (CellNode portalIn : northBorder) {
                if(portalIn.isOccupied()){
                    continue;
                }
                if(portalIn.getDistanceFrom(start)<searchRange){
                    CellNode portalOut = grid.getCell(portalIn.getIndex().getRow(),grid.getColumnCount()-1);

                    if (!isPortalOutSafe(grid, portalOut, 2, CardinalPoint.SOUTH)) {
                        continue;
                    }
                    if(importance == TeleportationImportance.NORMAL){
                        borderPath = findPortalCell(grid, portalIn, portalOut, start, objectives);
                    }else if(importance == TeleportationImportance.EMERGENCY){
                        borderPath = findPortalCell(grid, portalIn, portalOut, start, tail);
                    }
                    if (borderPath!=null) {
                        lastStep = Direction.UP;
                        teleporting = true;
                        return borderPath;
                    }
                }
            }
            break;
        case SOUTH:
            List<CellNode> southBorder =  grid.getTeleportZoneSouth();
            for (CellNode portalIn : southBorder) {
                if(portalIn.isOccupied()){
                    continue;
                }
                if(portalIn.getDistanceFrom(start)<searchRange){
                    CellNode portalOut = grid.getCell(portalIn.getIndex().getRow(),grid.getMinCol());

                    if (!isPortalOutSafe(grid, portalOut, 2, CardinalPoint.NORTH)) {
                        continue;
                    }
                    if(importance == TeleportationImportance.NORMAL){
                        borderPath = findPortalCell(grid, portalIn, portalOut, start, objectives);
                    }else if(importance == TeleportationImportance.EMERGENCY){
                        borderPath = findPortalCell(grid, portalIn, portalOut, start, tail);
                    }
                    if (borderPath!=null) {
                        lastStep = Direction.DOWN;
                        teleporting = true;
                        return borderPath;
                    }
                }
            }break;
        }
        return null;
    }

    private LinkedPath<PathWrapper> findPortalCell(GridNode grid, CellNode portalIn, CellNode portalOut, CellNode start, LinkedList<Objective> objectives){

        LinkedList<PathWrapper> pathToPortal = null;
        LinkedList<PathWrapper> pathFromPortal = null;

        PriorityQueue<DistanceWrapper>  distanceWrappers = new PriorityQueue<DistanceWrapper>(objectives.size(), new DistanceComparator());

        for (int i = 0; i < objectives.size(); i++) {
            distanceWrappers.add(new DistanceWrapper(portalOut,game.getGameObjectController().getObsFruitList().get(i)));
        }

        pathToPortal = GET_ASTAR_PATH(start, portalIn);

        if (!pathToPortal.isEmpty()) {

            while (!distanceWrappers.isEmpty()) {

                LinkedPath<PathWrapper> path = GET_SAFE_ASTAR_PATH(portalOut,distanceWrappers.poll().getObject().getCell(),grid.getTailCell(snakeAI));

                if(path.isPathSafe()){

                    pathFromPortal = path.getPathOne();

                    return new LinkedPath<>(pathToPortal, pathFromPortal);
                }
            }
        }
        return null;
    }

    private LinkedPath<PathWrapper> findPortalCell(GridNode grid, CellNode portalIn, CellNode portalOut, CellNode start, CellNode tail){

        LinkedList<PathWrapper> pathToPortal = null;
        LinkedList<PathWrapper> pathFromPortal = null;

        pathToPortal = GET_ASTAR_PATH(start, portalIn);

        if (!pathToPortal.isEmpty()) {

            pathFromPortal = GET_DFS_PATH(portalOut, tail);

            if(!pathFromPortal.isEmpty()){

                return new LinkedPath<>(pathToPortal, pathFromPortal);
            }
        }

        return null;
    }


    private void submitPath(LinkedPath<PathWrapper> cells){
        ThreadManager.performeTask("Compute Directions", ()-> calculateDirection(cells));
        setPathCoordinates(cells);
    }

     private LinkedPath<PathWrapper> calculateDirection(LinkedPath<PathWrapper> path) {
        switch (path.getPathType()) {
        case LONGEST_PATH:
            for (int index = 1; index<path.getPathOne().size(); index++) {
                PathWrapper parentNode = path.getPathOne().get(index-1);
                PathWrapper currentNode = path.getPathOne().get(index);
                if (currentNode.getIndex().getRow() > parentNode.getIndex().getRow()) {
                    parentNode.setDirection(Direction.RIGHT);
                } else if (currentNode.getIndex().getRow() < parentNode.getIndex().getRow()) {
                    parentNode.setDirection(Direction.LEFT);
                } else if (currentNode.getIndex().getCol() > parentNode.getIndex().getCol()) {
                    parentNode.setDirection(Direction.DOWN);
                } else if (currentNode.getIndex().getCol() < parentNode.getIndex().getCol()) {
                    parentNode.setDirection(Direction.UP);
                }
            }
            break;
        case SHORTEST_PATH:
            if (!path.getPathOne().isEmpty()) {
                for (int index = path.getPathOne().size() - 2; index >= 0; index--) {
                    PathWrapper parentNode = path.getPathOne().get(index + 1);
                    PathWrapper currentNode = path.getPathOne().get(index);
                    if (currentNode.getIndex().getRow() > parentNode.getIndex().getRow()) {
                        parentNode.setDirection(Direction.RIGHT);
                    } else if (currentNode.getIndex().getRow() < parentNode.getIndex().getRow()) {
                        parentNode.setDirection(Direction.LEFT);
                    } else if (currentNode.getIndex().getCol() > parentNode.getIndex().getCol()) {
                        parentNode.setDirection(Direction.DOWN);
                    } else if (currentNode.getIndex().getCol() < parentNode.getIndex().getCol()) {
                        parentNode.setDirection(Direction.UP);
                    }
                }
            }
            if (!path.getPathTwo().isEmpty()) {
                if (path.getType() == ConnectionType.INTERPOLAR_PATH) {
                    for (int index = path.getPathTwo().size() - 2; index >= 0; index--) {
                        PathWrapper parentNode = path.getPathTwo().get(index + 1);
                        PathWrapper currentNode = path.getPathTwo().get(index);
                        if (currentNode.getIndex().getRow() > parentNode.getIndex().getRow()) {
                            parentNode.setDirection(Direction.RIGHT);
                        } else if (currentNode.getIndex().getRow() < parentNode.getIndex().getRow()) {
                            parentNode.setDirection(Direction.LEFT);
                        } else if (currentNode.getIndex().getCol() > parentNode.getIndex().getCol()) {
                            parentNode.setDirection(Direction.DOWN);
                        } else if (currentNode.getIndex().getCol() < parentNode.getIndex().getCol()) {
                            parentNode.setDirection(Direction.UP);
                        }
                    }
                }
            }
            break;
        }
		if (currentGoal == CurrentGoal.OBJECTIVE) {
			if (path.isTeleportPath()) {
				path.getPathOne().get(0).setDirection(lastStep);
				lastStep = Direction.NONE;
			}
		}
        return path;
    }

    /**
     * TODO: Build a list containing coordinates and directions. make the snake
     * move towards the first direction on the list if the snake moves reaches
     * the coordinate on the list make the snake take the next turn and so
     * forth:....
     */

    public void steerAI() {
//        CellNode head = grid.getRelativeHeadCell(snakeAI);

		if (pathCoordinates != null) {
			pathCoordinates.getPathOne().stream().forEach(wrapper -> {
				CellNode cell = grid.getCell(wrapper.getIndex());

				if (wrapper.getFlag() == PathFlag.VISITED)
					return;

				Rectangle2D boundsAI = snakeAI.getAIBounds();

				if (cell.getBoundsCheck().contains(boundsAI.getMinX(),boundsAI.getMinY(),boundsAI.getWidth(),boundsAI.getHeight())) {
					cell.setPathCell(false);
					objectiveReached(cell);
					onPath = true;
				}

				Rectangle2D bounds = snakeAI.getBounds();

				if (cell.getBoundsCheck().contains(bounds.getMinX(),bounds.getMinY(),bounds.getWidth(),bounds.getHeight())) {
				switch (wrapper.getDirection()) {
					case DOWN:
						snakeAI.setDirectCoordinates(PlayerMovement.MOVE_DOWN);
						wrapper.setFlag(PathFlag.VISITED);
						break;
					case LEFT:
						snakeAI.setDirectCoordinates(PlayerMovement.MOVE_LEFT);
						wrapper.setFlag(PathFlag.VISITED);
						break;
					case RIGHT:
						snakeAI.setDirectCoordinates(PlayerMovement.MOVE_RIGHT);
						wrapper.setFlag(PathFlag.VISITED);
						break;
					case UP:
						snakeAI.setDirectCoordinates(PlayerMovement.MOVE_UP);
						wrapper.setFlag(PathFlag.VISITED);
						break;
					case NONE:
						onPath = false;
						objectiveReached(cell, true);
						wrapper.setFlag(PathFlag.VISITED);
						break;
					}
				}
			});

			if (!pathCoordinates.getPathTwo().isEmpty() && pathCoordinates.getType() == ConnectionType.INTERPOLAR_PATH) {
				pathCoordinates.getPathTwo().stream().forEach(wrapper -> {
					CellNode cell = grid.getCell(wrapper.getIndex());

					if (wrapper.getFlag() == PathFlag.VISITED)
						return;

					Rectangle2D boundsAI = snakeAI.getAIBounds();

					if (cell.getBoundsCheck().contains(boundsAI)) {
						cell.setPathCell(false);
						objectiveReached(cell);
						onPath = true;
					}

					Rectangle2D bounds = snakeAI.getBounds();

					if (cell.getBoundsCheck().contains(bounds)) {
						switch (wrapper.getDirection()) {
						case DOWN:
							snakeAI.setDirectCoordinates(PlayerMovement.MOVE_DOWN);
							wrapper.setFlag(PathFlag.VISITED);
							break;
						case LEFT:
							snakeAI.setDirectCoordinates(PlayerMovement.MOVE_LEFT);
							wrapper.setFlag(PathFlag.VISITED);
							break;
						case RIGHT:
							snakeAI.setDirectCoordinates(PlayerMovement.MOVE_RIGHT);
							wrapper.setFlag(PathFlag.VISITED);
							break;
						case UP:
							snakeAI.setDirectCoordinates(PlayerMovement.MOVE_UP);
							wrapper.setFlag(PathFlag.VISITED);
							break;
						case NONE:
							onPath = false;
							objectiveReached(cell);
							wrapper.setFlag(PathFlag.VISITED);
							break;
						}
					}
				});
			}
		}

		// if(head != null){
		// if(head.isPathToGoal()){
		// onPath = true;
		// }else{
		// onPath = false;
		// }
		// }
	}

	private void objectiveReached(CellNode cell) {
		objectiveReached(cell, false);
	}

	private void objectiveReached(CellNode cell, boolean lost) {

		if (!lost) {
			if (cell.isObjective()) {
				cell.setObjective(false);
				computePath(cell);
			} else {
				if (!pathRequests.isEmpty()) {
					PathRequest request = pathRequests.poll();
					request.execute(snake -> {
						snake.setGoal(request.getGoal());
						snake.computePath(cell);
					});
				}
			}
		} else {
			if (cell.isObjective()) {
				cell.setObjective(false);
			}
			if (!pathRequests.isEmpty()) {
				PathRequest request = pathRequests.poll();
				request.execute(snake -> {
					snake.setGoal(request.getGoal());
					snake.computePath(cell);
				});
			} else {
				computePath(null);
			}
		}
	}

	/**
	 * Method which under certain conditions will activate the speed boost of
	 * the snake.
	 *
	 * @param random
	 */
    public void addRandomBoost(boolean random) {
        if (currentGoal == CurrentGoal.OBJECTIVE) {
            if (currentState == ActionType.FREE_MODE) {
                if (random && rand.nextInt(randomBoost) != 0) {
                    return;
                }
                if (snakeAI != null) {
                    applyThrust();
                }
            } else if (currentState == ActionType.FIND_PATH) {
                if (random && rand.nextInt(randomBoost) != 0) {
                    return;
                }
                if (snakeAI != null) {
                    applyThrust();
                }
            }
        }
        else{
            removeThrust();
        }
    }

    private void applyThrust() {
        if (game.getEnergyBarTwo().getEnergyLevel() >150) {
            if (snakeAI.isAllowThrust() && !snakeAI.getSpeedThrust()) {
                snakeAI.setSpeedThrust(true);
            }
        }
        if (game.getEnergyBarTwo().getEnergyLevel() < 50) {
            snakeAI.setSpeedThrust(false);
        }
    }

    private void removeThrust(){
        snakeAI.setSpeedThrust(false);
        snakeAI.setThrustState(false);
    }

    private void log(Object str) {
        System.out.println(str.toString()+"\n");
    }

    /*
     * Method which attempts to determine the best course of action in order to
     * move towards the objective! The method will first check if the x distance
     * is less or greater than the y distance and based on that it will decide
     * to perform a horizontal or vertical move. if the method to be perform is
     * a vertical move the method will check if the objective is above or below
     * and then perform a move based on the objectives coordinates!
     */
    private void createPath() {
        switch (currentState) {
        case DODGE_OBSTACLES:
            break;
        case FIND_PATH:
            break;
        case FREE_MODE:
            break;
        default:
            break;
        }
    }

    /**
     * Method which performs actions based on the current location of the snake
     * and the objective! if the snake is within a predetermined threshold the
     * snake will perform the appropriate turn in order to collect the
     * objective!
     */
    private void performLocationBasedAction() {
        switch (currentState) {
        case DODGE_OBSTACLES:
            break;
        case FIND_PATH:
            steerAI();
            break;
        case FREE_MODE:
            break;
        default:
            break;
        }
    }

    /**
     * A program which takes a starting cell and the cell to flee from!
     * The program computes the which cell is farthest away from the cell it wishes to flee from
     * and computes a path to that cell!
     * @author Eudy
     *
     */

    public void submitPathRequest(PathRequest request){
    	pathRequests.add(request);
    }

    public void setGoal(CurrentGoal goal) {
        this.currentGoal = goal;
    }

    public boolean isAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }

    public int getUpdateTimer() {
        return updateTimer;
    }

    public void setUpdateTimer(int updateTimer) {
        this.updateTimer = updateTimer;
    }

    public double getX() {
        return snakeAI.getX();
    }

    public double getY() {
        return snakeAI.getY();
    }

    public double getWidth() {
        return snakeAI.getAIBounds().getWidth();
    }

    public double getHeight() {
        return snakeAI.getAIBounds().getHeight();
    }

    public void setPlayer() {
        this.snakeAI = null;
        this.snakeAI = game.getGameLoader().getPlayerTwo();
    }

    private void setPathCoordinates(LinkedPath<PathWrapper> coordinates){
        this.pathCoordinates = coordinates;
    }

    public PlayerMovement getDirection() {
        return snakeAI.getCurrentDirection();
    }

//    public Rectangle2D getCollisionBounds() {
//        return snakeAI.getAIBounds();
//    }

    private enum GoalSearch{
        CLOSEST_OBJECTIVE, SHORTEST_PATH
    }

    private enum CardinalPoint{
        WEST, EAST, NORTH, SOUTH
    }

    public enum CurrentGoal{
        OBJECTIVE, TAIL, FARTHEST_CELL
    }

    public enum ActionType {
        FREE_MODE, STALL, FIND_PATH, DODGE_OBSTACLES
    }

    private enum TeleportationImportance{
        NORMAL,EMERGENCY
    }

    public enum DistressLevel{
        NORMAL,DISTRESSED,SAFETY_CHECK_GOAL_LEVEL_TWO,CAUTIOUS_CHECK_EMERGENCY, SAFETY_CHECK_TAIL, SAFETY_CHECK_GOAL_LEVEL_THREE
    }

    public int getObjectiveCount(){
        return game.getGameObjectController().getObsFruitList().size();
    }

    private class DistanceComparator implements Comparator<DistanceWrapper>{
        @Override
        public int compare(DistanceWrapper a, DistanceWrapper b){
            return Double.compare(a.getDistance(), b.getDistance());
        }
    }

    private class PathLengthComparator implements Comparator<LinkedPath<PathWrapper>>{
        @Override
        public int compare(LinkedPath<PathWrapper> a, LinkedPath<PathWrapper> b){
            return Double.compare(a.getPathOneLength(), b.getPathOneLength());
        }
    }

    public class ObjectiveComparator implements Comparator<Objective> {
        @Override
        public int compare(Objective a, Objective b) {
            return Double.compare(a.getDistance(), b.getDistance());
        }
    }

}
