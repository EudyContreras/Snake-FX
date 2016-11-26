package com.EudyContreras.Snake.PathFindingAI;

import java.util.ArrayList;
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
import com.EudyContreras.Snake.PathFindingAI.CellNode.Direction;
import com.EudyContreras.Snake.PathFindingAI.LinkedPath.ConnectionType;
import com.EudyContreras.Snake.PathFindingAI.PathAlgorithms.PathType;
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

	private AIController controller;
	private PathAlgorithms pathFinder;
	private GameManager game;
	private PlayerTwo snakeAI;
	private Random rand;

	private boolean logDirections = false;
	private boolean teleporting = false;
	private boolean searching = false;
	private boolean allowTrace = false;
	private boolean safetyCheck = false;
	private boolean onPath = false;

	private double checkTimer = 100;

	private int safetyCheckTimer = 0;
	private int randomBoost = 200;

	private SearchType searchType;
	private DistressLevel distressLevel;
	private CurrentGoal currentGoal;
	private ActionType state;
	private Direction lastStep;

	private LinkedPath<CellNode> pathCoordinates;


	public AIPathFinder(GameManager game, PlayerTwo snakeAI) {
		this.game = game;
		this.snakeAI = snakeAI;
		this.pathFinder = new PathAlgorithms();
		this.initialize();
	}

	public AIPathFinder(GameManager game, AIController controller, PlayerTwo snakeAI, LinkedList<CollideNode> possibleColliders) {
		this.game = game;
		this.snakeAI = snakeAI;
		this.controller = controller;
		this.pathFinder = new PathAlgorithms();
		this.initialize();
	}

	public void initialize() {
		rand = new Random();
		state = ActionType.FIND_PATH;
		lastStep = Direction.NONE;
		searchType = SearchType.CLOSEST_OBJECTIVE;
		distressLevel = DistressLevel.LEVEL_TWO;
		currentGoal = CurrentGoal.OBJECTIVE;
	}

	public void findObjective() {
		switch (state) {
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
			computePath();
		}
	}

	/*
	 * this method gets called from the game loop and it is called at 60fps. The
	 * method update and keeps track of things
	 */
	public void updateSimulation() {
		if (game.getModeID() == GameModeID.LocalMultiplayer) {
			if (game.getStateID() == GameStateID.GAMEPLAY) {

				performLocationBasedAction();
				addRandomBoost(true);

				checkTimer --;

				if(!onPath){
					if(checkTimer<=0){
						computePath();
						if(currentGoal != CurrentGoal.TAIL){
							checkTimer = 50;
							if(safetyCheck == true){
								safetyCheck = false;
							}
						}else{
							checkTimer = 50;
							if(safetyCheck == false){
								safetyCheck = true;
								safetyCheckTimer = 0;
							}
						}
					}
				}


				if(safetyCheck && currentGoal == CurrentGoal.TAIL){
					safetyCheckTimer++;

					if(safetyCheckTimer>=1000){
						safetyCheckTimer = 0;
						currentGoal = CurrentGoal.OBJECTIVE;
					}
				}
			}
		}
	}

	/**
	 * Find a path from start to goal using the A* algorithm
	 */
	public List<CellNode> GET_ASTAR_PATH(GridNode grid, CellNode start, CellNode objective) {
		return pathFinder.GET_ASTAR_PATH(snakeAI,grid,start,objective,distressLevel);
	}

	public LinkedPath<CellNode> GET_SAFE_ASTAR_PATH(GridNode grid, CellNode start, CellNode objective, CellNode tail){
		return pathFinder.GET_SAFE_ASTAR_PATH(snakeAI,grid, start,objective,tail,distressLevel);
	}

	public List<CellNode> GET_DFS_PATH(GridNode grid, CellNode start, CellNode objective) {
		return pathFinder.GET_DFS_PATH(snakeAI,grid,start,objective,distressLevel);
	}

	@SuppressWarnings("unused")
	private void findClosestObjective(Objective[] objectives){
		PriorityQueue<Distance>  distances = new PriorityQueue<Distance>(4, new DistanceComparator());

		for (int i = 0; i < getObjectiveCount(); i++) {
			distances.add(new Distance(PathAlgorithms.calculateManhathanDistance(
					snakeAI.getX(), game.getGameObjectController().getObsFruitList().get(i).getX(),
					snakeAI.getY(), game.getGameObjectController().getObsFruitList().get(i).getY()),
					game.getGameObjectController().getObsFruitList().get(i)));
		}

		if(distances.poll().getObject().getNumericCode()!=objectives[0].getObject().getNumericCode()){
			computePath();
		}
	}

	public synchronized void computePath(){
		teleporting = false;

		LinkedList<Objective> newObjectives = new LinkedList<>();
		PriorityQueue<LinkedPath<CellNode>>  paths = new PriorityQueue<LinkedPath<CellNode>>(getObjectiveCount(), new PathLengthComparator());

		CellNode start = controller.getHeadCell(snakeAI);
		CellNode tail = controller.getTailCell(game.getSectManagerTwo().getSectionList().getLast());
		CellNode goal = null;

		LinkedPath<CellNode> path = new LinkedPath<CellNode>();

		if (searchType == SearchType.SHORTEST_PATH) {

			for (int i = 0; i < getObjectiveCount(); i++) {
				AbstractObject object = game.getGameObjectController().getObsFruitList().get(i);
				Objective objective = new Objective(snakeAI, object);
				paths.add(new LinkedPath<CellNode>(GET_ASTAR_PATH(controller.getGrid(), start, objective.getCell()),new ArrayList<>(), objective));
			}

			if (!start.isDangerZone()) {
				distressLevel = DistressLevel.LEVEL_TWO;
			}

			boolean checkingPaths = true;

			while(checkingPaths && paths.peek()!=null){
				path = paths.poll();

				if(!path.isPathOneEmpty()){
					pathFinder.setPathType(PathType.SHORTEST_PATH);
					showPathToObjective(path);
					checkingPaths = false;
				}

			}
		} else if (searchType == SearchType.CLOSEST_OBJECTIVE) {

			switch(currentGoal){
			case OBJECTIVE:
//				for (int i = 0; i<getObjectiveCount(); i++){
//					AbstractObject object = game.getGameObjectController().getObsFruitList().get(i);
//					Objective objective = new Objective(snakeAI, object);
//					newObjectives.add(objective);
//				}
//
//				Collections.sort(newObjectives);

				pathFinder.setPathType(PathType.SHORTEST_PATH);

				for (int i = 0; i < getObjectiveCount(); i++) {
					AbstractObject object = game.getGameObjectController().getObsFruitList().get(i);
					Objective objective = new Objective(snakeAI, object);
					paths.add(new LinkedPath<CellNode>(GET_ASTAR_PATH(controller.getGrid(), start, objective.getCell()),new ArrayList<>(), objective));
				}

				while (paths.peek() != null) {
					newObjectives.add(paths.poll().getObjective());
				}

				if (newObjectives.size() > 0) {
					if (newObjectives.get(0) != null && GameSettings.SHOW_ASTAR_GRAPH) {
						newObjectives.get(0).getObject().blowUpAlt();
					}

					if (start != null) {
						if (!start.isDangerZone()) {
							distressLevel = DistressLevel.LEVEL_TWO;
						}

						for(int i = 0; i < newObjectives.size(); i++){
							path = checkObjectiveReach(start, newObjectives.get(i),tail, i, newObjectives);

							if(path.isPathSafe()){
								break;
							}
						}

						if(!path.isPathSafe()){
							distressLevel = DistressLevel.LEVEL_THREE;

							for(int i = 0; i < newObjectives.size(); i++){
								path = checkObjectiveReach(start, newObjectives.get(i),tail, i, newObjectives);

								if(path.isPathSafe()){
									break;
								}
							}
						}

						if(path.isPathSafe()){
							log("path is safe!!");
							showPathToObjective(path);
						}else{
							currentGoal = CurrentGoal.TAIL;
							log("path is not safe!!");
							computePath();
						}
					}
				}

				break;
			case TAIL:
				pathFinder.setPathType(PathType.LONGEST_PATH);

				if (tail != null) {
					if (!start.isDangerZone()) {
						distressLevel = DistressLevel.LEVEL_TWO;
						path = new LinkedPath<CellNode>(GET_DFS_PATH(controller.getGrid(), start, tail), new ArrayList<>());
					}
					if (!path.getPathOne().isEmpty()) {
						showPathToObjective(path);
					} else {

						distressLevel = DistressLevel.LEVEL_THREE;
						path = new LinkedPath<CellNode>(GET_DFS_PATH(controller.getGrid(), start, tail), new ArrayList<>());

						if (!path.getPathOne().isEmpty()) {

							showPathToObjective(path);
						} else {
							log("Emergency path to tail empty!");

							distressLevel = DistressLevel.LEVEL_THREE;
							path = emergencyTeleport(controller.getGrid(), start, tail);

							if (!path.getPathOne().isEmpty()) {

								showPathToObjective(path);

							} else {
								// TODO: Stall until path is found or die!
							}
						}
					}
				}break;
			}
		}
	}

	public LinkedPath<CellNode> checkObjectiveReach(CellNode start, Objective objective, CellNode tail, int index, LinkedList<Objective> objectives){
		if(start!=null){

//			if((objectives.get(index).getDistance()) > objectives.get(objectives.size()-1).getInterpolarDistance(start.getLocation().getX(),start.getLocation().getY())){
//				return computeInterpolarDirection(controller.getGrid(),start,objective,objectives);
//			}
//			else if((objectives.get(index).getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.4) && objectives.get(index).getYDistance(start.getLocation().getY())<GameSettings.HEIGHT*.4){
//				return computeInterpolarDirection(controller.getGrid(),start,objective,objectives);
//			}
//			else if(objectives.get(index).getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.5 && objectives.get(index).getXDistance(start.getLocation().getX())<GameSettings.WIDTH*.5){
//				return computeInterpolarDirection(controller.getGrid(),start,objective,objectives);
//			}
//			else if(objectives.get(index).getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.5 && objectives.get(index).getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.5){
//				return computeInterpolarDirection(controller.getGrid(),start,objective,objectives);
//			}
//			//TODO: Find additional conditions that may qualify for interpolation
//
//			else{
				if(start!=null && objective!=null){

					LinkedPath<CellNode> path = GET_SAFE_ASTAR_PATH(controller.getGrid(), start, objective.getCell(), tail);

					if(path.isPathSafe()){
						return path;
					}
				}
			}
//		}
		return new LinkedPath<CellNode>();
	}
	/*
	 * Unsafe amd unchecked teleport method which triggers a teleportation when a the distance
	 * between the snake and the objective is above a certain threshold. The calculations are made
	 * based on relational distance planes.
	 */

	private LinkedPath<CellNode> computeInterpolarDirection(GridNode grid, CellNode start, Objective objective, LinkedList<Objective> objectives) {
		LinkedPath<CellNode> path;
		CellNode portalIn;
		CellNode portalOut;

		if((objective.getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.45) && objective.getYDistance(start.getLocation().getY())<GameSettings.HEIGHT*.45){

			if(start.getLocation().getX() > GameSettings.WIDTH*.65){

				if(!grid.getCell(grid.getRowCount()-1, start.getIndex().getCol()).isOccupied()){
					portalIn = grid.getCell(grid.getRowCount()-1, start.getIndex().getCol());
					if(!grid.getCell(grid.getMinRow(), start.getIndex().getCol()).isOccupied()){
						portalOut = grid.getCell(grid.getMinRow(), start.getIndex().getCol());
						path = findPortalCell(grid, portalIn, portalOut, start, objectives);
						if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
							lastStep = Direction.RIGHT;
							teleporting = true;
							return path;
						}
					}
				}else{
					if(!grid.getCell(grid.getRowCount()-1, start.getIndex().getCol() + (start.getIndex().getCol()+2)<grid.getColumnCount()-1 ? 2 : 0).isOccupied()){
						portalIn = grid.getCell(grid.getRowCount()-1, start.getIndex().getCol() + 2);
						if(!grid.getCell(grid.getMinRow(), start.getIndex().getCol() + 2).isOccupied()){
							portalOut = grid.getCell(grid.getMinRow(), start.getIndex().getCol() + 2);
							path = findPortalCell(grid, portalIn, portalOut, start, objectives);
							if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
								lastStep = Direction.RIGHT;
								teleporting = true;
								return path;
							}
						}
					}else{
						if(!grid.getCell(grid.getRowCount()-1, start.getIndex().getCol() - (start.getIndex().getCol()+2)>grid.getMinCol() ? 2 : 0).isOccupied()){
							portalIn = grid.getCell(grid.getRowCount()-1, start.getIndex().getCol() - 2);
							if(!grid.getCell(grid.getMinRow(), start.getIndex().getCol() - 2).isOccupied()){
								portalOut = grid.getCell(grid.getMinRow(), start.getIndex().getCol() - 2);
								path = findPortalCell(grid, portalIn, portalOut, start, objectives);
								if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
									lastStep = Direction.RIGHT;
									teleporting = true;
									return path;
								}
							}
						}else{
							List<CellNode> eastBorder =  grid.getTeleportZoneEast();
							for (CellNode cell : eastBorder) {
								if (!cell.isOccupied() && !grid.getCell(grid.getMinRow(), cell.getIndex().getCol()).isOccupied()) {
									if(cell.getLocation().getY()>=start.getLocation().getY()-100){
										distressLevel = DistressLevel.LEVEL_THREE;
										path = findPortalCell(grid, cell, grid.getCell(grid.getMinRow(), cell.getIndex().getCol()), start, objectives);
										if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
											lastStep = Direction.RIGHT;
											teleporting = true;
											return path;

										}
										else {
											continue; //TODO: Marked out needs reasesstment
										}
									}
								}
							}
						}
					}
				}
			}
			if(start.getLocation().getX() < GameSettings.WIDTH*.35){

				if(!grid.getCell(grid.getMinRow(),start.getIndex().getCol()).isOccupied()){
					portalIn = grid.getCell(grid.getMinRow(),start.getIndex().getCol());
					if(!grid.getCell(grid.getRowCount()-1,start.getIndex().getCol()).isOccupied()){
						portalOut = grid.getCell(grid.getRowCount()-1,start.getIndex().getCol());
						path = findPortalCell(grid, portalIn, portalOut, start, objectives);
						if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
							lastStep = Direction.LEFT;
							teleporting = true;
							return path;
						}
					}
				}else{

					/*
					 *TODO: Find out why I get a null pointer at the line below!!!!!!!!!!!!!!!!!!!!
					 */
					int thereshold = (start.getIndex().getCol()+2)<(grid.getColumnCount()-1) ? 2 : 0;
					if(!grid.getCell(grid.getMinRow(),start.getIndex().getCol() + thereshold).isOccupied()){
						portalIn = grid.getCell(grid.getMinRow(),start.getIndex().getCol() + thereshold);
						if(!grid.getCell(grid.getRowCount()-1,start.getIndex().getCol() + thereshold).isOccupied()){
							portalOut = grid.getCell(grid.getRowCount()-1,start.getIndex().getCol() + thereshold);
							path = findPortalCell(grid, portalIn, portalOut, start, objectives);
							if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
								log("HALLELOYOU!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
								lastStep = Direction.LEFT;
								teleporting = true;
								return path;
							}
						}
					}else{
						if(!grid.getCell(grid.getMinRow(),start.getIndex().getCol()-(start.getIndex().getCol()+2)>grid.getMinCol() ? 2 : 0).isOccupied()){
							portalIn = grid.getCell(grid.getMinRow(),start.getIndex().getCol() - 2);
							if(!grid.getCell(grid.getRowCount()-1,start.getIndex().getCol() - 2).isOccupied()){
								portalOut = grid.getCell(grid.getRowCount()-1,start.getIndex().getCol() - 2);
								path = findPortalCell(grid, portalIn, portalOut, start, objectives);
								if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
									lastStep = Direction.LEFT;
									teleporting = true;
									return path;
								}
							}
						}else{
							List<CellNode> westBorder =  grid.getTeleportZoneWest();
							for (CellNode cell : westBorder) {
								if (!cell.isOccupied() && !grid.getCell(grid.getRowCount()-1,cell.getIndex().getCol()).isOccupied()) {
									if(cell.getLocation().getY()>=start.getLocation().getY()-100){
										distressLevel = DistressLevel.LEVEL_THREE;
										path = findPortalCell(grid, cell, grid.getCell(grid.getRowCount()-1,cell.getIndex().getCol()), start, objectives);
										if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
											lastStep = Direction.LEFT;
											teleporting = true;
											return path;
										}
										else {
											continue; //TODO: needs reassestment
										}
									}
								}
							}
						}
					}
				}
			}
		}
		else if(objective.getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.45  && objective.getXDistance(start.getLocation().getX())<GameSettings.WIDTH*.45){

			//TODO: Incomplete! Implement the rest of the conditions!!
			if(start.getLocation().getY() > GameSettings.HEIGHT*.65){
				List<CellNode> southBorder =  grid.getTeleportZoneSouth();
				for (CellNode cell : southBorder) {
					if (!cell.isOccupied() && cell.getLocation().getX()>=start.getLocation().getX() && !grid.getCell(cell.getIndex().getRow(),grid.getMinCol()).isOccupied()) {
						distressLevel = DistressLevel.LEVEL_THREE;
						path = findPortalCell(grid, cell, grid.getCell(cell.getIndex().getRow(),grid.getMinCol()), start, objectives);
						if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
							lastStep = Direction.DOWN;
							teleporting = true;
							return path;

						} else {
							continue;
						}
					}
				}
			}
			if(start.getLocation().getY() < GameSettings.HEIGHT*.35){
				List<CellNode> northBorder =  grid.getTeleportZoneNorth();
				for (CellNode cell : northBorder) {
					if (!cell.isOccupied() && cell.getLocation().getX()>=start.getLocation().getX() && !grid.getCell(cell.getIndex().getRow(),grid.getColumnCount()-1).isOccupied()) {
						distressLevel = DistressLevel.LEVEL_THREE;
						path = findPortalCell(grid, cell, grid.getCell(cell.getIndex().getRow(),grid.getColumnCount()-1), start, objectives);
						if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
							lastStep = Direction.UP;
							teleporting = true;
							return path;

						} else {
							continue;
						}
					}
				}
			}
		}
		else if(objective.getXDistance(start.getLocation().getX())>GameSettings.WIDTH*.45 && objective.getYDistance(start.getLocation().getY())>GameSettings.HEIGHT*.45){
			new LinkedPath<CellNode>(GET_ASTAR_PATH(controller.getGrid(), start, objective.getCell()),new ArrayList<>());
		}
		return new LinkedPath<CellNode>(GET_ASTAR_PATH(controller.getGrid(), start, objective.getCell()),new ArrayList<>());
	}

	public LinkedPath<CellNode> emergencyTeleport(GridNode grid, CellNode start, CellNode  end) {
		LinkedPath<CellNode> path;

		if(start.getLocation().getX() > GameSettings.WIDTH*.65 && start.getLocation().getY() > GameSettings.HEIGHT*.35 && start.getLocation().getY() < GameSettings.HEIGHT*.65){
			List<CellNode> eastBorder =  grid.getTeleportZoneEast();
			for (CellNode cell : eastBorder) {
				if (!cell.isOccupied() && !grid.getCell(grid.getMinRow(), cell.getIndex().getCol()).isOccupied()) {
					distressLevel = DistressLevel.LEVEL_THREE;
					path = findPortalCell(grid, cell, grid.getCell(grid.getMinRow(), cell.getIndex().getCol()), start, end);
					if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
						lastStep = Direction.RIGHT;
						teleporting = true;
						return path;

					} else {
						continue;
					}
				}
			}
		}
		if(start.getLocation().getX() < GameSettings.WIDTH*.35 && start.getLocation().getY() > GameSettings.HEIGHT*.35 && start.getLocation().getY() < GameSettings.HEIGHT*.65){
			List<CellNode> westBorder =  grid.getTeleportZoneWest();
			for (CellNode cell : westBorder) {
				if (!cell.isOccupied() && !grid.getCell(grid.getRowCount()-1,cell.getIndex().getCol()).isOccupied()) {
					distressLevel = DistressLevel.LEVEL_THREE;
					path = findPortalCell(grid, cell, grid.getCell(grid.getRowCount()-1,cell.getIndex().getCol()), start, end);
					if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
						lastStep = Direction.LEFT;
						teleporting = true;
						return path;

					} else {
						continue;
					}
				}
			}
		}

		if(start.getLocation().getY() > GameSettings.HEIGHT*.65 && start.getLocation().getX() > GameSettings.WIDTH && start.getLocation().getX() < GameSettings.WIDTH*.65){
			List<CellNode> southBorder =  grid.getTeleportZoneSouth();
			for (CellNode cell : southBorder) {
				if (!cell.isOccupied() && !grid.getCell(cell.getIndex().getRow(),grid.getMinCol()).isOccupied()) {
					distressLevel = DistressLevel.LEVEL_THREE;
					path = findPortalCell(grid, cell, grid.getCell(cell.getIndex().getRow(),grid.getMinCol()), start, end);
					if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
						lastStep = Direction.DOWN;
						teleporting = true;
						return path;

					} else {
						continue;
					}
				}
			}
		}
		if(start.getLocation().getY() < GameSettings.HEIGHT*.35 && start.getLocation().getX() > GameSettings.WIDTH && start.getLocation().getX() < GameSettings.WIDTH*.65){
			List<CellNode> northBorder =  grid.getTeleportZoneNorth();
			for (CellNode cell : northBorder) {
				if (!cell.isOccupied() && !grid.getCell(cell.getIndex().getRow(),grid.getColumnCount()-1)  .isOccupied()) {
					distressLevel = DistressLevel.LEVEL_THREE;
					path = findPortalCell(grid, cell, grid.getCell(cell.getIndex().getRow(),grid.getColumnCount()-1), start, end);
					if (!path.getPathOne().isEmpty() && !path.getPathTwo().isEmpty()) {
						lastStep = Direction.UP;
						teleporting = true;
						return path;

					} else {
						continue;
					}
				}
			}
		}

		return new LinkedPath<CellNode>(GET_ASTAR_PATH(controller.getGrid(), start, end),new ArrayList<>());
	}

	private LinkedPath<CellNode> findPortalCell(GridNode grid, CellNode portalIn, CellNode portalOut, CellNode start, LinkedList<Objective> objectives){

		List<CellNode> pathToPortal = null;
		List<CellNode> pathFromPortal = null;

		PriorityQueue<Distance>  distances = new PriorityQueue<Distance>(objectives.size(), new DistanceComparator());

		for (int i = 0; i < objectives.size(); i++) {
			distances.add(new Distance(portalOut,game.getGameObjectController().getObsFruitList().get(i)));
		}

		distressLevel = DistressLevel.LEVEL_THREE;

		if(!portalOut.isOccupied()){

//			for (int i = 0; i < distances.size(); i++) {
//				if (distances.peek() != null) {
//					pathToPortal = GET_ASTAR_PATH(controller.getGrid(), start, portalIn);
//					if (!pathToPortal.isEmpty()) {
//						pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut,distances.poll().getObject().getCell());
//						return new LinkedPath(pathToPortal, pathFromPortal);
//					}
//				}
//			}
			pathToPortal = GET_ASTAR_PATH(controller.getGrid(), start, portalIn);

			if (!pathToPortal.isEmpty()) {
				pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, distances.poll().getObject().getCell());

				if(!pathFromPortal.isEmpty()){
					return new LinkedPath<CellNode>(pathToPortal,pathFromPortal);
				}
				else{
					pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, distances.poll().getObject().getCell());

					if(!pathFromPortal.isEmpty()){
						return new LinkedPath<CellNode>(pathToPortal,pathFromPortal);
					}
					else{
						pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, distances.poll().getObject().getCell());

						if(!pathFromPortal.isEmpty()){
							return new LinkedPath<CellNode>(pathToPortal,pathFromPortal);
						}
						else{
							pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, distances.poll().getObject().getCell());

							if(!pathFromPortal.isEmpty()){
								return new LinkedPath<CellNode>(pathToPortal,pathFromPortal);
							}
						}
					}
				}
			}
		}
		return new LinkedPath<CellNode>(pathToPortal, pathFromPortal);
	}

	private LinkedPath<CellNode> findPortalCell(GridNode grid, CellNode portalIn, CellNode portalOut, CellNode start, CellNode objective){

		List<CellNode> pathToPortal = null;
		List<CellNode> pathFromPortal = null;

		distressLevel = DistressLevel.LEVEL_THREE;

		if(!portalOut.isOccupied()){

			pathToPortal = GET_ASTAR_PATH(controller.getGrid(), start, portalIn);

			if (!pathToPortal.isEmpty()) {
				pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, objective);

				if(!pathFromPortal.isEmpty()){
					return new LinkedPath<CellNode>(pathToPortal,pathFromPortal);
				}
				else{

					PriorityQueue<Distance>  distances = new PriorityQueue<Distance>(getObjectiveCount(), new DistanceComparator());

					for (int i = 0; i < getObjectiveCount(); i++) {
						distances.add(new Distance(portalOut,game.getGameObjectController().getObsFruitList().get(i)));
					}

//					for (int i = 0; i < distances.size(); i++) {
//						if (distances.peek() != null) {
//							if (!pathToPortal.isEmpty()) {
//								pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, distances.poll().getObject().getCell());
//								return new LinkedPath(pathToPortal, pathFromPortal);
//							}
//						}
//					}
					pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut, distances.poll().getObject().getCell());

					if (!pathFromPortal.isEmpty()) {
						return new LinkedPath<CellNode>(pathToPortal, pathFromPortal);
					} else {
						pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut,distances.poll().getObject().getCell());

						if (!pathFromPortal.isEmpty()) {
							return new LinkedPath<CellNode>(pathToPortal, pathFromPortal);
						} else {
							pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut,distances.poll().getObject().getCell());

							if (!pathFromPortal.isEmpty()) {
								return new LinkedPath<CellNode>(pathToPortal, pathFromPortal);
							} else {
								pathFromPortal = GET_ASTAR_PATH(controller.getGrid(), portalOut,distances.poll().getObject().getCell());

								if (!pathFromPortal.isEmpty()) {
									return new LinkedPath<CellNode>(pathToPortal, pathFromPortal);
								}
							}
						}
					}
				}
			}
		}
		return new LinkedPath<CellNode>(pathToPortal,pathFromPortal);
	}



	/**
	 * TODO: Perform a check to determine if the computed path to the objective is a safe path
	 * by computing a path from the start to the objective and from the objective to the tail of the snake.
	 * The path must be computed as a special path which considers the path to the objective to be an obstacle.
	 * Create a special path made out of special obstacles from the start to goal. The path must be an abstract path. Once
	 * that path is created then compute a path from the goal to the tail of the snake, the path must ignore nodes that
	 * belong to the path from start to goal. If a path can be created from goal to tail then the given a path can
	 * be consider somewhat "Safe"!!. If the path is safe allow the snake to go for the apple. but if the path isnt safe
	 *
	 * @param start
	 * @param goal
	 * @param tail
	 * @return
	 */
	public boolean isPathSafe(CellNode start, CellNode goal, CellNode tail){

		return true;
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


	/**
	 * TODO: Perform a check to determine if the computed path to the objective is a safe path
	 * by computing a path from the start to the objective and from the objective to the tail of the snake.
	 * The path must be computed as a special path which considers the path to the objective to be an obstacle.
	 * Create a special path made out of special obstacles from the start to goal. The path must be an abstract path. Once
	 * that path is created then compute a path from the goal to the tail of the snake, the path must ignore nodes that
	 * belong to the path from start to goal. If a path can be created from goal to tail then the given a path can
	 * be consider somewhat "Safe"!!. If the path is safe allow the snake to go for the apple. but if the path isnt safe
	 *
	 * @param start
	 * @param goal
	 * @param tail
	 * @return
	 */
	private boolean isPathSafe(GridNode grid, CellNode start, CellNode goal, CellNode tail) {
		LinkedPath<CellNode> path = GET_SAFE_ASTAR_PATH(controller.getGrid(), start,  goal, tail);
		if(path.isPathSafe()){
			return true;
		}else{
			return false;
		}

	}

	public List<CellNode> getShortestPath(List<List<CellNode>> paths) {

		List<CellNode> smallest = paths.get(0);

		int minSize = Integer.MAX_VALUE;

		for (int i = paths.size() - 1; i >= 0; i--) {

			if (paths.get(i).size() < minSize && !paths.get(i).isEmpty()) {

				minSize = paths.get(i).size();
				smallest = paths.get(i);
			}
		}
		return smallest;
	}

	private void showPathToObjective(LinkedPath<CellNode> cells){
		setAllowTrace(true);
		setPathCoordinates(calculateDirection(cells));
		if (logDirections) {
			for (int i = cells.getPathOne().size() - 1; i >= 0; i--) {
				log("Direction: "+i+ "  = " + cells.getPathOne().get(i).getDirection().toString());
			}
			log("");
			for (int i = cells.getPathTwo().size() - 1; i >= 0; i--) {
				log("Direction: "+i+ "  = " + cells.getPathTwo().get(i).getDirection().toString());
			}
			log("");
		}
	}

	private void setAllowTrace(boolean state) {
		this.allowTrace = state;
	}

	private LinkedPath<CellNode> calculateDirection(LinkedPath<CellNode> paths) {
		for (CellNode node : paths.getPathOne()) {
			if (node.getParentNode() != null) {
				node.setPathCell(true);
				if (node.getIndex().getRow() > node.getParentNode().getIndex().getRow()) {
					node.getParentNode().setDirection(Direction.RIGHT);
				} else if (node.getIndex().getRow() < node.getParentNode().getIndex().getRow()) {
					node.getParentNode().setDirection(Direction.LEFT);
				} else if (node.getIndex().getCol() > node.getParentNode().getIndex().getCol()) {
					node.getParentNode().setDirection(Direction.DOWN);
				} else if (node.getIndex().getCol() < node.getParentNode().getIndex().getCol()) {
					node.getParentNode().setDirection(Direction.UP);
				}
			}
		}
		if (!paths.getPathTwo().isEmpty() && paths.getType() == ConnectionType.INTERPOLAR_PATH){
			for (CellNode node : paths.getPathTwo()) {
				if (node.getParentNode() != null) {
					node.setPathCell(true);
					if (node.getLocation().getX() > node.getParentNode().getLocation().getX()) {
						node.getParentNode().setDirection(Direction.RIGHT);
					} else if (node.getLocation().getX() < node.getParentNode().getLocation().getX()) {
						node.getParentNode().setDirection(Direction.LEFT);
					} else if (node.getLocation().getY() > node.getParentNode().getLocation().getY()) {
						node.getParentNode().setDirection(Direction.DOWN);
					} else if (node.getLocation().getY() < node.getParentNode().getLocation().getY()) {
						node.getParentNode().setDirection(Direction.UP);
					}
				}
			}
		}

		paths.getPathOne().get(0).setDirection(lastStep);
		lastStep = Direction.NONE;
		return paths;
	}
	/**
	 * TODO: Build a list containing coordinates and directions.
	 * make the snake move towards the first direction on the list
	 * if the snake moves reaches the coordinate on the list make the
	 * snake take the next turn and so forth:....
	 */
	public void steerAI() {
		CellNode cell = null;
		CellNode head = controller.getGrid().getRelativeHeadCell(snakeAI);

		if (pathCoordinates != null) {
			for (int index = 0; index < pathCoordinates.getPathOne().size(); index++) {
				cell = pathCoordinates.getPathOne().get(index);
				if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
					switch (cell.getDirection()) {
					case DOWN:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_DOWN);
						cell.setPathCell(false);
						onPath = true;
						break;
					case LEFT:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_LEFT);
						cell.setPathCell(false);
						onPath = true;
						break;
					case RIGHT:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_RIGHT);
						cell.setPathCell(false);
						onPath = true;
						break;
					case UP:
						game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_UP);
						cell.setPathCell(false);
						onPath = true;
						break;
					case NONE:
						onPath = false;
						break;
					}
				}
			}
			if(!pathCoordinates.getPathTwo().isEmpty() && pathCoordinates.getType() == ConnectionType.INTERPOLAR_PATH){
				for (int index = 0; index < pathCoordinates.getPathTwo().size(); index++) {
					cell = pathCoordinates.getPathTwo().get(index);
					if (cell.getBoundsCheck().contains(snakeAI.getBounds())) {
						switch (cell.getDirection()) {
						case DOWN:
							game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_DOWN);
							cell.setPathCell(false);
							onPath = true;
							break;
						case LEFT:
							game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_LEFT);
							cell.setPathCell(false);
							onPath = true;
							break;
						case RIGHT:
							game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_RIGHT);
							cell.setPathCell(false);
							onPath = true;
							break;
						case UP:
							game.getGameLoader().getPlayerTwo().setDirectCoordinates(PlayerMovement.MOVE_UP);
							cell.setPathCell(false);
							onPath = true;
							break;
						case NONE:
							onPath = false;
							break;
						}
					}
				}
			}
		}
		if(head != null){
			if(head.isPathToGoal()){
				onPath = true;
			}else{
				onPath = false;
			}
		}
	}

	/**
	 * Method which under certain conditions will activate the speed boost of
	 * the snake
	 *
	 * @param random
	 */
	public void addRandomBoost(boolean random) {
		if (currentGoal == CurrentGoal.OBJECTIVE) {
			if (state == ActionType.FREE_MODE) {
				if (random && rand.nextInt(randomBoost) != 0) {
					return;
				}
				if (snakeAI != null) {
					applyThrust();
				}
			} else if (state == ActionType.FIND_PATH) {
				if (random && rand.nextInt(randomBoost) != 0) {
					return;
				}
				if (snakeAI != null) {
					applyThrust();
				}
			}
		}
		else{
			snakeAI.setSpeedThrust(false);
		}
	}

	private void log(String str) {
		System.out.println(str+"\n");
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
		switch (state) {
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
		switch (state) {
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


	/**
	 * A program which takes a starting cell and the cell to flee from!
	 * The program computes the which cell is farthest away from the cell it wishes to flee from
	 * and computes a path to that cell!
	 * @author Eudy
	 *
	 */
	public class AFleePathFinding{

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

	private void setPathCoordinates(LinkedPath<CellNode> coordinates){
		this.pathCoordinates = coordinates;
	}

	public PlayerMovement getDirection() {
		return snakeAI.getCurrentDirection();
	}

	public Rectangle2D getCollisionBounds() {
		return snakeAI.getAIBounds();
	}

	public enum ActionType {
		FREE_MODE, STALL, FIND_PATH, DODGE_OBSTACLES
	}

	public enum DistressLevel{
		LEVEL_ONE,LEVEL_TWO,LEVEL_THREE,SAFETY_CHECK,CAUTIOUS_CHECK_EMERGENCY
	}

	private enum CurrentGoal{
		OBJECTIVE, TAIL,
	}

	private enum SearchType{
		CLOSEST_OBJECTIVE, SHORTEST_PATH;
	}

	public int getObjectiveCount(){
		return game.getGameObjectController().getObsFruitList().size();
	}

	private class DistanceComparator implements Comparator<Distance>{
		@Override
		public int compare(Distance a, Distance b){
			return Double.compare(a.getDistance(), b.getDistance());
		}
	}

	private class PathLengthComparator implements Comparator<LinkedPath<CellNode>>{
		@Override
		public int compare(LinkedPath<CellNode> a, LinkedPath<CellNode> b){
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
