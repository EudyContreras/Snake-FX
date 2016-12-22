package com.EudyContreras.Snake.PathFindingAI;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;

/**
 * Class which holds the distance and the nearest object and the object!
 *
 * @author Eudy Contreras
 *
 */
public class DistanceWrapper implements Comparable<DistanceWrapper>{

	private Double distance;
	private AbstractObject object;

	public DistanceWrapper(double distance, AbstractObject object) {
		this.distance = distance;
		this.object = object;
	}

	public DistanceWrapper(CellNode start, AbstractObject objective){
		this.distance = NodeHeuristic.calculateManhathanDistance(start.getLocation().getX(), objective.getX(),start.getLocation().getY(),objective.getY());
		this.object = objective;
	}

	public double getDistance() {
		return distance;
	}

	public AbstractObject getObject() {
		return object;
	}

	@Override
	public int compareTo(DistanceWrapper distanceWrapper) {
		return Double.compare(this.getDistance(),distanceWrapper.getDistance());
	}
}