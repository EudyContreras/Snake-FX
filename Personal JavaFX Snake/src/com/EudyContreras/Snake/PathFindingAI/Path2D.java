package com.EudyContreras.Snake.PathFindingAI;

import java.util.LinkedList;
import java.util.List;

public class Path2D<NODE,DIRECTION> implements Comparable<Path2D<NODE,DIRECTION>> {

	private List<NODE> nodes;
	private List<DIRECTION> directions;

	public Path2D(){
		this.nodes = new LinkedList<>();
		this.directions = new LinkedList<>();
	}

	public Path2D<NODE, DIRECTION> addNode(NODE node){
		this.nodes.add(node);
		return this;
	}

	public Path2D<NODE, DIRECTION> addNode(int i,NODE node){
		this.nodes.add(i,node);
		return this;
	}

	public Path2D<NODE, DIRECTION> addDirection(DIRECTION direction){
		this.directions.add(direction);
		return this;
	}

	public Path2D<NODE, DIRECTION> addDirection(int i, DIRECTION direction){
		this.directions.add(i,direction);
		return this;
	}

	public NODE getNode(int i){
		return nodes.get(i);
	}

	public DIRECTION getDirection(int i){
		return directions.get(i);
	}

	public NODE removeNode(int i){
		return nodes.remove(i);
	}

	public boolean removeNode(NODE i){
		return nodes.remove(i);
	}

	public DIRECTION removeDirection(int i){
		return directions.remove(i);
	}

	public boolean removeDirection(DIRECTION i){
		return directions.remove(i);
	}

	public List<NODE> getNodes() {
		return nodes;
	}

	public void setNodes(List<NODE> nodes) {
		this.nodes = nodes;
	}

	public List<DIRECTION> getDirections() {
		return directions;
	}

	public void setDirections(List<DIRECTION> directions) {
		this.directions = directions;
	}

	public void addPath2D(Path2D<NODE,DIRECTION> path){
		nodes.addAll(path.getNodes());
		directions.addAll(path.getDirections());
	}

	public int size(){
		return nodes.size();
	}

	public void clear(){
		nodes.clear();
		directions.clear();
	}

	public boolean isEmpty(){
		return nodes.isEmpty();
	}

	@Override
	public int compareTo(Path2D<NODE, DIRECTION> path) {
		return Double.compare(size(), path.size());
	}


}
