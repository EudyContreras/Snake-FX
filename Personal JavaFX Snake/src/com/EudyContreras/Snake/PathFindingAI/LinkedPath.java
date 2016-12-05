package com.EudyContreras.Snake.PathFindingAI;

public class LinkedPath<PATH> implements Comparable<LinkedPath<PATH>>{

	private ConnectionType type;

	private Objective objective;

	private PathWrapper<PATH> pathOne;
	private PathWrapper<PATH> pathTwo;

	public LinkedPath() {
		this(new PathWrapper<PATH>(), new PathWrapper<PATH>());
	}

	public LinkedPath(ConnectionType type) {
		this(new PathWrapper<PATH>(), new PathWrapper<PATH>());
		this.type = type;
	}

	public LinkedPath(PathWrapper<PATH> pathOne, PathWrapper<PATH> pathTwo) {
		this(pathOne, pathTwo, null);
	}

	public LinkedPath(PathWrapper<PATH> pathOne, PathWrapper<PATH> pathTwo, Objective objective) {
		super();
		this.objective = objective;
		this.type = ConnectionType.INTERPOLAR_PATH;
		if (pathOne != null && pathTwo != null) {
			this.pathOne = !pathOne.isEmpty() ? pathOne : new  PathWrapper<PATH>();
			this.pathTwo = !pathTwo.isEmpty() ? pathTwo : new  PathWrapper<PATH>();
		}
		else{
			this.pathOne = new  PathWrapper<PATH>();
			this.pathTwo = new  PathWrapper<PATH>();
		}
	}

	public PathWrapper<PATH> getJoinedPath(){
		 PathWrapper<PATH> joinPath = new  PathWrapper<PATH>();

		if(!pathOne.isEmpty()){
			joinPath.addPath2D(pathOne);
		}
		if(!pathTwo.isEmpty()){
			joinPath.addPath2D(pathTwo);
		}
		return joinPath;
	}

	public void setPathOne(PathWrapper<PATH> pathOne){
		this.pathOne = pathOne;
	}

	public void setPathTwo(PathWrapper<PATH> pathTwo){
		this.pathTwo = pathTwo;
	}

	public PathWrapper<PATH> getPathOne() {
		return pathOne;
	}

	public PathWrapper<PATH> getPathTwo() {
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
	public int compareTo(LinkedPath<PATH> path) {
		return Double.compare(getPathOneLength(),path.getPathOneLength());
	}
}
