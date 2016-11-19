package com.EudyContreras.Snake.PathFindingAI_BK;

import java.util.ArrayList;

import com.EudyContreras.Snake.PathFindingAI.CellNode.Direction;

import javafx.geometry.Point2D;

public class PathTransition {

	private ArrayList<WayPoint> wayPoints = new ArrayList<>();

	public PathTransition(ArrayList<CellNode> cellNodes) {  
		processList(cellNodes);
	}

	public void processList(ArrayList<CellNode> list) {
		for (int i = list.size() - 1; i >= 0; i--) {
			wayPoints.add(new WayPoint(list.get(i).getLocation(),list.get(i).getDirection()));
		}
	}

	public class WayPoint extends Point2D {
		Direction direction;
		
		public WayPoint(double x, double y) {
			super(x, y);
		}
		public WayPoint(Point2D point2d, Direction direction) {
			super(point2d.getX(), point2d.getY());
			this.direction = direction;
		}
	}
}
