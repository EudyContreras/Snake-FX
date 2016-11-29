package com.EudyContreras.Snake.PathFindingAI;

import java.util.LinkedList;
import java.util.List;

public  class LongestPathInMaze {
	public static void main(String[] args) {
		boolean[][] maze = new boolean[10][10];
		makeRandomMaze(maze);
		printMaze(maze);
		List<Cell> path = findLongestPath(maze);
		if (path == null) {
			System.out.println("No path possible");
			return;
		}
		for (Cell Cell : path)
			System.out.print(Cell + ",");
		System.out.println();
	}


	private static List<Cell> findLongestPath(boolean[][] maze) {
		Cell start = new Cell(0, 0);
		Cell end = new Cell(maze.length - 1, maze[0].length - 1);
		List<Cell> path = findLongestPath(maze, start, end);
		return path;
	}

	private static LinkedList<Cell> findLongestPath(boolean[][] maze, Cell start, Cell end) {
		LinkedList<Cell> result = null;
		int rows = maze.length;
		int cols = maze[0].length;
		if (start.row < 0 || start.col < 0)
			return null;
		if (start.row == rows || start.col == cols)
			return null;
		if (maze[start.row][start.col] == true)
			return null;
		if (start.equals(end)) {
			LinkedList<Cell> path = new LinkedList<Cell>();
			path.add(start);
			return path;
		}
		maze[start.row][start.col] = true;
		Cell[] nextCells = new Cell[4];
		nextCells[0] = new Cell(start.row + 1, start.col);
		nextCells[2] = new Cell(start.row, start.col + 1);
		nextCells[1] = new Cell(start.row - 1, start.col);
		nextCells[3] = new Cell(start.row, start.col - 1);
		int maxLength = -1;
		for (Cell nextCell : nextCells) {
			LinkedList<Cell> path = findLongestPath(maze, nextCell, end);
			if (path != null && path.size() > maxLength) {
				maxLength = path.size();
				path.addFirst(start);;
				result = path;
			}
		}
		maze[start.row][start.col] = false;
		if (result == null || result.size() == 0)
			return null;
		return result;
	}



	private static void printMaze(boolean[][] maze) {
		for (int i = 0; i < maze.length; ++i) {
			for (int j = 0; j < maze[i].length; ++j) {
				if (maze[i][j])
					System.out.print("#|");
				else
					System.out.print("_|");
			}
			System.out.println();
		}
	}

	private static void makeRandomMaze(boolean[][] maze) {
		for (int i = 0; i < maze.length; ++i) {
			for (int j = 0; j < maze[0].length; ++j) {
				maze[i][j] = (int) (Math.random() * 3) == 1;
			}
		}
		maze[0][0] = false;
		maze[maze.length - 1][maze[0].length - 1] = false;

	}
}
