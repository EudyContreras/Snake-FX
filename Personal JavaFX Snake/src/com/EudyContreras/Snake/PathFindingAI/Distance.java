package com.EudyContreras.Snake.PathFindingAI;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;

/**
 * Class which holds the distance and the nearest object and the object!
 *
 * @author Eudy Contreras
 *
 */
public class Distance implements Comparable<Distance>{

	private Double distance;
	private AbstractObject object;

	public Distance(double distance, AbstractObject object) {
		this.distance = distance;
		this.object = object;
	}

	public Distance(CellNode start, AbstractObject objective){
		this.distance = SearchAlgorithm.calculateManhathanDistance(start.getLocation().getX(), objective.getX(),start.getLocation().getY(),objective.getY());
		this.object = objective;
	}

	public double getDistance() {
		return distance;
	}

	public AbstractObject getObject() {
		return object;
	}

	@Override
	public int compareTo(Distance distance) {
		return Double.compare(this.getDistance(),distance.getDistance());
	}
}