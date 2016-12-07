package com.EudyContreras.Snake.PathFindingAI;

import java.util.LinkedList;
import java.util.List;

public class PathWrapper<PATH> implements Comparable<PathWrapper<PATH>> {

	private List<PATH> nodes;

	public PathWrapper(){
		this.nodes = new LinkedList<>();
	}

	public PathWrapper<PATH> addNode(PATH node){
		this.nodes.add(node);
		return this;
	}

	public PathWrapper<PATH> addNode(int i,PATH node){
		this.nodes.add(i,node);
		return this;
	}

	public PathWrapper<PATH> addFirst(PATH node){
		this.nodes.add(0,node);
		return this;
	}

	public PathWrapper<PATH> addLast(PATH node){
		this.nodes.add(nodes.size()-1,node);
		return this;
	}

	public int getIndex(PATH node){
		return nodes.indexOf(node);
	}

	public PATH getNode(int i){
		return nodes.get(i);
	}

	public PATH getLastNode(){
		return nodes.get(nodes.size()-1);
	}

	public PATH getFirstNode(){
		return nodes.get(0);
	}

	public PATH removeNode(int i){
		return nodes.remove(i);
	}

	public boolean removeNode(PATH i){
		return nodes.remove(i);
	}

	public List<PATH> getNodes() {
		return nodes;
	}

	public void setNodes(List<PATH> nodes) {
		this.nodes = nodes;
	}

	public void addPath2D(PathWrapper<PATH> path){
		nodes.addAll(path.getNodes());
	}

	public int size(){
		return nodes.size();
	}

	public void clear(){
		nodes.clear();
	}

	public boolean isEmpty(){
		return nodes.isEmpty();
	}

	@Override
	public int compareTo(PathWrapper<PATH> path) {
		return Double.compare(size(), path.size());
	}


}
