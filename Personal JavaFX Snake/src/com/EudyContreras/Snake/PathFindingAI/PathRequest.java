package com.EudyContreras.Snake.PathFindingAI;

import java.util.function.Consumer;

import com.EudyContreras.Snake.PathFindingAI.AIPathFinder.CurrentGoal;

public class PathRequest {

	private CurrentGoal goal;
	private AIPathFinder pathFinder;

	public PathRequest(AIPathFinder pathFinder, CurrentGoal goal){
		this.goal = goal;
		this.pathFinder = pathFinder;
	}
	public void execute(CellNode start){
		pathFinder.setGoal(goal);
		pathFinder.computePath(start);
	}

	public CurrentGoal getGoal() {
		return goal;
	}
	public void setGoal(CurrentGoal goal) {
		this.goal = goal;
	}
	public void execute(Consumer<AIPathFinder> action){
		action.accept(pathFinder);
	}
}
