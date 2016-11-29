package com.EudyContreras.Snake.PathFindingAI;

import java.util.LinkedList;
import java.util.List;

public class LinkedPath<T> implements Comparable<LinkedPath<T>>{

	private ConnectionType type;
	private Objective objective;
	private List<T> pathOne;
	private List<T> pathTwo;

	public LinkedPath() {
		this(new LinkedList<>(),new LinkedList<>());
	}

	public LinkedPath(ConnectionType type) {
		this(new LinkedList<>(),new LinkedList<>());
		this.type = type;
	}

	public LinkedPath(List<T> pathOne, List<T> pathTwo) {
		this(pathOne, pathTwo, null);
	}

	public LinkedPath(List<T> pathOne, List<T> pathTwo, Objective objective) {
		super();
		this.objective = objective;
		this.type = ConnectionType.INTERPOLAR_PATH;
		if (pathOne != null && pathTwo != null) {
			this.pathOne = !pathOne.isEmpty() ? pathOne : new LinkedList<>();
			this.pathTwo = !pathTwo.isEmpty() ? pathTwo : new LinkedList<>();
		}
		else{
			this.pathOne = new LinkedList<>();
			this.pathTwo = new LinkedList<>();
		}
	}

	public List<T> getJoinedPath(){
		List<T> joinPath = new LinkedList<>();

		if(!pathOne.isEmpty()){
			joinPath.addAll(pathOne);
		}
		if(!pathTwo.isEmpty()){
			joinPath.addAll(pathTwo);
		}
		return joinPath;
	}

	public void setPathOne(List<T> pathOne){
		this.pathOne = pathOne;
	}

	public void setPathTwo(List<T> pathTwo){
		this.pathTwo = pathTwo;
	}

	public List<T> getPathOne() {
		return pathOne;
	}

	public List<T> getPathTwo() {
		return pathTwo;
	}

	public boolean isPathSafe(){
		return !pathOne.isEmpty() && !pathTwo.isEmpty();
	}

	public Objective getObjective() {
		return objective;
	}

	public ConnectionType getType() {
		return type;
	}

	public void setType(ConnectionType type) {
		this.type = type;
	}

	public void setObjective(Objective objective) {
		this.objective = objective;
	}

	public int getPathOneLength(){
		return pathOne.size();
	}

	public int getPathTwoLength(){
		return pathTwo.size();
	}

	public void clearPaths(){
		pathOne.clear();
		pathTwo.clear();
	}

	public boolean isPathOneEmpty(){
		return pathOne.isEmpty();
	}

	public boolean isPathTwoEmpty(){
		return pathTwo.isEmpty();
	}

	public enum ConnectionType{
		SAFE_PATH_CHECK, INTERPOLAR_PATH
	}

	@Override
	public int compareTo(LinkedPath<T> path) {
		return Double.compare(getPathOneLength(),path.getPathOneLength());
	}

}
