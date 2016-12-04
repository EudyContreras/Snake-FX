package com.EudyContreras.Snake.PathFindingAI;

import java.util.LinkedList;
import java.util.List;

public class Path2D<INDEX,DIRECTION> implements Comparable<Path2D<INDEX,DIRECTION>> {

	private List<INDEX> nodes;
	private List<DIRECTION> directions;

	public Path2D(){
		this.nodes = new LinkedList<>();
		this.directions = new LinkedList<>();
	}

	public Path2D<INDEX, DIRECTION> addNode(INDEX node){
		this.nodes.add(node);
		return this;
	}

	public Path2D<INDEX, DIRECTION> addNode(int i,INDEX node){
		this.nodes.add(i,node);
		return this;
	}

	public Path2D<INDEX, DIRECTION> addDirection(DIRECTION direction){
		this.directions.add(direction);
		return this;
	}

	public Path2D<INDEX, DIRECTION> addDirection(int i, DIRECTION direction){
		this.directions.add(i,direction);
		return this;
	}

	public INDEX getNode(int i){
		return nodes.get(i);
	}

	public DIRECTION getDirection(int i){
		return directions.get(i);
	}

	public INDEX removeNode(int i){
		return nodes.remove(i);
	}

	public boolean removeNode(INDEX i){
		return nodes.remove(i);
	}

	public DIRECTION removeDirection(int i){
		return directions.remove(i);
	}

	public boolean removeDirection(DIRECTION i){
		return directions.remove(i);
	}

	public List<INDEX> getNodes() {
		return nodes;
	}

	public void setNodes(List<INDEX> nodes) {
		this.nodes = nodes;
	}

	public List<DIRECTION> getDirections() {
		return directions;
	}

	public void setDirections(List<DIRECTION> directions) {
		this.directions = directions;
	}

	public void addPath2D(Path2D<INDEX,DIRECTION> path){
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
	public int compareTo(Path2D<INDEX, DIRECTION> path) {
		return Double.compare(size(), path.size());
	}


}
