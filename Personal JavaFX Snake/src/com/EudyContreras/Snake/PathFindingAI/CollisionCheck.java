package com.EudyContreras.Snake.PathFindingAI;

import java.util.function.Consumer;
import java.util.function.Predicate;

import javafx.geometry.Rectangle2D;

public class CollisionCheck<T>{

	private CellNode cell;
	private Rectangle2D bounds;

	public CollisionCheck(CellNode cell, Rectangle2D bounds){
		this.cell = cell;
		this.bounds = bounds;
	}

	public boolean intersect(Consumer<CellNode> action){
		if(cell.getBoundsCheck().intersects(bounds)){
			action.accept(cell);
			return true;
		}else{
			return false;
		}
	}

	public boolean intersect(Consumer<CellNode> action, T type, Predicate<T> predicate){
		if(cell.getBoundsCheck().intersects(bounds)){
			if(predicate.test(type)){
				action.accept(cell);
			}
			return true;
		}else{
			return false;
		}
	}

	public boolean contains(Consumer<CellNode> action, T type, Predicate<T> predicate){
		if(cell.getBoundsCheck().contains(bounds)){
			if(predicate.test(type)){
				action.accept(cell);
			}
			return true;
		}else{
			return false;
		}
	}


}