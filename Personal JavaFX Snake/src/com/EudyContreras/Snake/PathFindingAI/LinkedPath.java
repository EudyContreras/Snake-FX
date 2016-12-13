package com.EudyContreras.Snake.PathFindingAI;

import java.util.LinkedList;

public class LinkedPath<PATH> implements Comparable<LinkedPath<PATH>>{

	private boolean teleportPath = false;

	private ConnectionType type;

	private Objective objective;

	private LinkedList<PATH> pathOne;
	private LinkedList<PATH> pathTwo;

	public LinkedPath() {
		this(new LinkedList<PATH>(), new LinkedList<PATH>());
	}

	public LinkedPath(ConnectionType type) {
		this(new LinkedList<PATH>(), new LinkedList<PATH>(), type);
	}

	public LinkedPath(LinkedList<PATH> pathOne, LinkedList<PATH> pathTwo) {
		this(pathOne, pathTwo, ConnectionType.INTERPOLAR_PATH);
	}

	public LinkedPath(LinkedList<PATH> pathOne, LinkedList<PATH> pathTwo,ConnectionType connection) {
		this(pathOne, pathTwo, null, connection);
	}

	public LinkedPath(LinkedList<PATH> pathOne, LinkedList<PATH> pathTwo, Objective objective) {
		this(pathOne, pathTwo, objective, null);
	}

	public LinkedPath(LinkedList<PATH> pathOne, LinkedList<PATH> pathTwo, Objective objective, ConnectionType connection) {
		super();
		this.objective = objective;
		this.type = connection;
		if (pathOne != null && pathTwo != null) {
			this.pathOne = !pathOne.isEmpty() ? pathOne : new  LinkedList<PATH>();
			this.pathTwo = !pathTwo.isEmpty() ? pathTwo : new  LinkedList<PATH>();
		}
		else{
			this.pathOne = new  LinkedList<PATH>();
			this.pathTwo = new  LinkedList<PATH>();
		}
	}

	public LinkedList<PATH> getJoinedPath(){
		 LinkedList<PATH> joinPath = new  LinkedList<PATH>();

		if(!pathOne.isEmpty()){
			joinPath.addAll(pathOne);
		}
		if(!pathTwo.isEmpty()){
			joinPath.addAll(pathTwo);
		}
		return joinPath;
	}

	public void setPathOne(LinkedList<PATH> pathOne){
		this.pathOne = pathOne;
	}

	public void setPathTwo(LinkedList<PATH> pathTwo){
		this.pathTwo = pathTwo;
	}

	public LinkedList<PATH> getPathOne() {
		return pathOne;
	}

	public LinkedList<PATH> getPathTwo() {
		return pathTwo;
	}

	public boolean isPathSafe(){
		return (!pathOne.isEmpty() && !pathTwo.isEmpty());
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

	public LinkedPath<PATH> setTeleportPath(boolean teleportPath) {
		this.teleportPath = teleportPath;
		return this;
	}

	public boolean isTeleportPath() {
		return teleportPath;
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
	public int compareTo(LinkedPath<PATH> path) {
		return Double.compare(getPathOneLength(),path.getPathOneLength());
	}

}
