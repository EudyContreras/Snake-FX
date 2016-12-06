package com.EudyContreras.Snake.PathFindingAI;

import java.util.ArrayList;
import java.util.Collections;

public class LongestPath {
	  private final int maxX = 3;
	  private final int maxY = 3;
	  private final int[][] board = new int[][]{
	      {1, 2, 3, 4},
	      {2, 3, -1, 5},
	      {3, 2, -1, 6},
	      {6, 1, 2, 3}
	  };
	  int[][] pathLength;
	  ArrayList<ArrayList<ArrayList<Integer>>> paths;

	  private ArrayList<Integer> findSequence(int xPos,
	      int yPos) {
	      if(pathLength[yPos][xPos] >= 0)
	      {
	          ArrayList<Integer> ans = new ArrayList<Integer>();
	          int length =  pathLength[yPos][xPos];
	          ArrayList<Integer> path = paths.get(yPos).get(xPos);
	          for(int i = 0; i < length; i++)
	              ans.add(path.get(i));
	          return ans;
	      }

	      ArrayList<Integer> pathRight = new ArrayList<Integer>();
	      ArrayList<Integer> pathDown = new ArrayList<Integer>();

	      if (yPos < maxY && (board[yPos + 1][xPos] + 1 == board[yPos][xPos] ||
	                          board[yPos + 1][xPos] - 1 == board[yPos][xPos])) {
	        pathDown = findSequence(xPos, yPos + 1);
	      }
	      if (xPos < maxX && (board[yPos][xPos + 1] + 1 == board[yPos][xPos] ||
	                          board[yPos][xPos + 1] - 1 == board[yPos][xPos])) {
	        pathRight = findSequence(xPos + 1, yPos);
	      }
	      ArrayList<Integer> ans;
	      if (pathDown.size() > pathRight.size()) {
	          ans = pathDown;
	      } else {
	          ans = pathRight;
	      }
	      ans.add(board[yPos][xPos]);
	      paths.get(yPos).set(xPos,ans);
	      pathLength[yPos][xPos] = ans.size();
	      return ans;
	  }

	  private void getSequence() {
	     ArrayList<Integer> result;
	     pathLength = new int[maxX + 1][maxY + 1];
	     paths = new ArrayList<ArrayList<ArrayList<Integer>>>();
	     for(int y = 0; y <= maxY; y++)
	     {
	         ArrayList<ArrayList<Integer>> line = new ArrayList<ArrayList<Integer>>();
	         for(int x = 0; x <= maxX; x++)
	         {
	             line.add(null);
	             pathLength[y][x] = -1;
	         }
	         paths.add(line);
	     }
	     result = findSequence(0, 0);
	     Collections.reverse(result);
	     for (int i = 0; i < result.size(); i++) {
	       System.out.println(result.get(i));
	    }
	  }

	  public static void main(String[] args) {
	    LongestPath sequence = new LongestPath();
	    sequence.getSequence();
	  }
	}