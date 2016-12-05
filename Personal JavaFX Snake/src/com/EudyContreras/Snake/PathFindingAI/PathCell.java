package com.EudyContreras.Snake.PathFindingAI;

import com.EudyContreras.Snake.PathFindingAI.CellNode.Direction;

public class PathCell {

	private IndexWrapper index;
	private Direction direction;
	private PathFlag flag;

	public PathCell(IndexWrapper index) {
		this(index, Direction.NONE, PathFlag.PATH_CELL);
	}

	public PathCell(IndexWrapper index, Direction direction) {
		this(index, direction, PathFlag.PATH_CELL);
	}

	public PathCell(IndexWrapper index, Direction direction, PathFlag flag) {
		super();
		this.index = index;
		this.direction = direction;
		this.flag = flag;
	}

	public IndexWrapper getIndex() {
		return index;
	}

	public void setIndex(IndexWrapper index) {
		this.index = index;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public PathFlag getFlag() {
		return flag;
	}

	public void setFlag(PathFlag flag) {
		this.flag = flag;
	}

	public enum PathFlag{
		OBJECTIVE_CELL, PATH_CELL
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((flag == null) ? 0 : flag.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathCell other = (PathCell) obj;
		if (direction != other.direction)
			return false;
		if (flag != other.flag)
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		return true;
	}


}
