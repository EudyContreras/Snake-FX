package com.EudyContreras.Snake.PathFindingAI;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

public class Objective implements Comparable<Objective>{

	private double x;
	private double y;
	private double normalDistance;
	private CellNode cell;
	private PlayerTwo snakeAI;
	private AbstractObject object;
	private SortingType sorting;
	private ObjectivePosition location;

	public Objective(PlayerTwo snake, AbstractObject object) {
		this(snake,object,SortingType.CLOSEST);
	}

	public Objective(PlayerTwo snake, AbstractObject object, SortingType sorting) {
		this.x = object.getX();
		this.y = object.getY();
		this.snakeAI = snake;
		this.object = object;
		this.sorting = sorting;
		this.cell = object.getCell();
		this.computeDistances();
	}

	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}

	private void computeDistances(){
		this.normalDistance = NodeHeuristic.calculateManhathanDistance(snakeAI.getX(), object.getX(), snakeAI.getY(), object.getY());
	}

	public double getXDistance(double x){
		return Math.abs(x-this.x);
	}

	public double getYDistance(double y){
		return Math.abs(y-this.y);
	}

	public double getDistance(double x, double y){
		return Math.abs(x - this.x)+Math.abs(y - this.y);
	}

	public double getInterpolarXDistance(double x){
		double xDistance;

		if(this.x > x){
			xDistance = x + (GameSettings.WIDTH-this.x);
		}
		else{
			xDistance =this.x + (GameSettings.WIDTH-x);
		}
		return xDistance;
	}

	public double getInterpolarYDistance(double y){
		double yDistance;

		if(this.y > y){
			yDistance = y + (GameSettings.HEIGHT-this.y);
		}
		else{
			yDistance = this.y +(GameSettings.HEIGHT-y);
		}
		return yDistance;
	}

	public double getInterpolarDistance(double x, double y){
		double dX = getInterpolarXDistance(x);
		double dY = getInterpolarYDistance(y);

		return getDistance(dX,dY);
	}

	public ObjectivePosition getRelativeLocation(CellNode start){
		if(x > start.getLocation().getX()){
			if(y > start.getLocation().getY()){
				return ObjectivePosition.SOUTH_EAST;
			}
			else if (y < start.getLocation().getY()){
				return ObjectivePosition.NORTH_EAST;
			}
			else{
				return ObjectivePosition.EAST;
			}
		}
		else if(x < start.getLocation().getX()){
			if(y > start.getLocation().getY()){
				return ObjectivePosition.SOUTH_WEST;
			}
			else if (y <  start.getLocation().getY()){
				return ObjectivePosition.NORTH_WEST;
			}
			else{
				return ObjectivePosition.WEST;
			}
		}
		else if(x == start.getLocation().getX()){
			if(y > start.getLocation().getY()){
				return ObjectivePosition.SOUTH;
			}
			else{
				return ObjectivePosition.NORTH;
			}
		}
		return location;
	}


	public ObjectivePosition getLocation() {
		return location;
	}

	public void setLocation(ObjectivePosition location) {
		this.location = location;
	}

	public double getDistance() {
		return normalDistance;
	}

	public CellNode getCell() {
		return cell;
	}

	public AbstractObject getObject(){
		return object;
	}

	@Override
	public String toString(){
		return normalDistance+"";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Objective other = (Objective) obj;
		if (this.object.getX() != other.object.getX() && this.object.getY() != other.object.getY()) {
			return false;
		}
		if (this.normalDistance != other.normalDistance) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Objective distance) {
		switch(sorting){
		case CLOSEST:
			return Double.compare(this.getDistance(),distance.getDistance());
		case FARTHEST:
			return Double.compare(distance.getDistance(),this.getDistance());
		default:
			return Double.compare(this.getDistance(),distance.getDistance());

		}
	}

	public enum SortingType{
		CLOSEST, FARTHEST
	}

	private enum ObjectivePosition {
		NORTH, SOUTH, WEST, EAST, NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST
	}


}