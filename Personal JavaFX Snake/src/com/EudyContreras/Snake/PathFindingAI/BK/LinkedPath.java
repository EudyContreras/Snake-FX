package com.EudyContreras.Snake.PathFindingAI.BK;

public class LinkedPath<NODE,DIRECTION> implements Comparable<LinkedPath<NODE, DIRECTION>>{

	private ConnectionType type;

	private Objective objective;

	private Path2D<NODE, DIRECTION> pathOne;
	private Path2D<NODE, DIRECTION> pathTwo;

	public LinkedPath() {
		this(new Path2D<NODE, DIRECTION>(), new Path2D<NODE, DIRECTION>());
	}

	public LinkedPath(ConnectionType type) {
		this(new Path2D<NODE, DIRECTION>(), new Path2D<NODE, DIRECTION>());
		this.type = type;
	}

	public LinkedPath(Path2D<NODE, DIRECTION> pathOne, Path2D<NODE, DIRECTION> pathTwo) {
		this(pathOne, pathTwo, null);
	}

	public LinkedPath(Path2D<NODE, DIRECTION> pathOne, Path2D<NODE, DIRECTION> pathTwo, Objective objective) {
		super();
		this.objective = objective;
		this.type = ConnectionType.INTERPOLAR_PATH;
		if (pathOne != null && pathTwo != null) {
			this.pathOne = !pathOne.isEmpty() ? pathOne : new  Path2D<NODE, DIRECTION>();
			this.pathTwo = !pathTwo.isEmpty() ? pathTwo : new  Path2D<NODE, DIRECTION>();
		}
		else{
			this.pathOne = new  Path2D<NODE, DIRECTION>();
			this.pathTwo = new  Path2D<NODE, DIRECTION>();
		}
	}

	public Path2D<NODE, DIRECTION> getJoinedPath(){
		 Path2D<NODE, DIRECTION> joinPath = new  Path2D<NODE, DIRECTION>();

		if(!pathOne.isEmpty()){
			joinPath.addPath2D(pathOne);
		}
		if(!pathTwo.isEmpty()){
			joinPath.addPath2D(pathTwo);
		}
		return joinPath;
	}

	public void setPathOne(Path2D<NODE, DIRECTION> pathOne){
		this.pathOne = pathOne;
	}

	public void setPathTwo(Path2D<NODE, DIRECTION> pathTwo){
		this.pathTwo = pathTwo;
	}

	public Path2D<NODE, DIRECTION> getPathOne() {
		return pathOne;
	}

	public Path2D<NODE, DIRECTION> getPathTwo() {
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
	public int compareTo(LinkedPath<NODE, DIRECTION> path) {
		return Double.compare(getPathOneLength(),path.getPathOneLength());
	}
}
