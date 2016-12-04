package com.EudyContreras.Snake.PathFindingAI;

public class LinkedPath<INDEX,DIRECTION> implements Comparable<LinkedPath<INDEX, DIRECTION>>{

	private ConnectionType type;

	private Objective objective;

	private Path2D<INDEX, DIRECTION> pathOne;
	private Path2D<INDEX, DIRECTION> pathTwo;

	public LinkedPath() {
		this(new Path2D<INDEX, DIRECTION>(), new Path2D<INDEX, DIRECTION>());
	}

	public LinkedPath(ConnectionType type) {
		this(new Path2D<INDEX, DIRECTION>(), new Path2D<INDEX, DIRECTION>());
		this.type = type;
	}

	public LinkedPath(Path2D<INDEX, DIRECTION> pathOne, Path2D<INDEX, DIRECTION> pathTwo) {
		this(pathOne, pathTwo, null);
	}

	public LinkedPath(Path2D<INDEX, DIRECTION> pathOne, Path2D<INDEX, DIRECTION> pathTwo, Objective objective) {
		super();
		this.objective = objective;
		this.type = ConnectionType.INTERPOLAR_PATH;
		if (pathOne != null && pathTwo != null) {
			this.pathOne = !pathOne.isEmpty() ? pathOne : new  Path2D<INDEX, DIRECTION>();
			this.pathTwo = !pathTwo.isEmpty() ? pathTwo : new  Path2D<INDEX, DIRECTION>();
		}
		else{
			this.pathOne = new  Path2D<INDEX, DIRECTION>();
			this.pathTwo = new  Path2D<INDEX, DIRECTION>();
		}
	}

	public Path2D<INDEX, DIRECTION> getJoinedPath(){
		 Path2D<INDEX, DIRECTION> joinPath = new  Path2D<INDEX, DIRECTION>();

		if(!pathOne.isEmpty()){
			joinPath.addPath2D(pathOne);
		}
		if(!pathTwo.isEmpty()){
			joinPath.addPath2D(pathTwo);
		}
		return joinPath;
	}

	public void setPathOne(Path2D<INDEX, DIRECTION> pathOne){
		this.pathOne = pathOne;
	}

	public void setPathTwo(Path2D<INDEX, DIRECTION> pathTwo){
		this.pathTwo = pathTwo;
	}

	public Path2D<INDEX, DIRECTION> getPathOne() {
		return pathOne;
	}

	public Path2D<INDEX, DIRECTION> getPathTwo() {
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
	public int compareTo(LinkedPath<INDEX, DIRECTION> path) {
		return Double.compare(getPathOneLength(),path.getPathOneLength());
	}
}
