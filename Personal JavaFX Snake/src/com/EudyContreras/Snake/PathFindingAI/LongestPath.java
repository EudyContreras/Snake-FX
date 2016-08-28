package com.EudyContreras.Snake.PathFindingAI;

import java.util.*;
import java.io.*;

public class LongestPath  {
  int 		n;	    // number of nodes
  int	    target;    // destination node
  int 	    minLength;  // the minimal length of each path
  Node[]	v;	    //used to store Nodes
  Edge[]	e;	    //used to store Edges

  int[]	path;	    //used to store temporary path
  int 	length=0;	    // length of the path
  int   distance=0;    // distance of the path
  int[]	bestPath;	    //used to store temporary path
  int 	bestLength=0;	    // length of the longest path
  int   bestDistance=-1000000;     // distance of the longest path

  int[] visited;  //used to mark a node as visited if set as 1

  public LongestPath () {}

  public LongestPath (String filename) throws IOException {
    //this function read edge distances from a file
    BufferedReader br = new BufferedReader (
			   new FileReader (filename));
    String line = br.readLine();
    StringTokenizer st = new StringTokenizer (line);
    //1st token is the # of nodes
    n = Integer.parseInt (st.nextToken());

    //2nd token is the # of edges
    int m = Integer.parseInt (st.nextToken());

    //create storage for Nodes and Edges
    v = new Node[n];
    e = new Edge[m];
    System.out.println(n+" nodes and " + m + " edges.");

    //create Node objects for graph
    //numbering them as 0, 1, 2, ..., n-1
    for (int i = 0; i < n; i++)
        v[i] = new Node(i);

    int i = 0;
    while ((line=br.readLine()) != null){
      //check input file
      if (i >= e.length){
        System.out.println ("# of lines is greater than # of edges in G, exit...");
        System.exit(1);
      }

      Edge edge = new Edge(i);
      st = new StringTokenizer (line);

      //first token is the start point
      String  start = st.nextToken();
      int sVal = Integer.parseInt (start);
      edge.start = sVal;

      //2nd token is the end point
      String  end = st.nextToken();
      int eVal = Integer.parseInt (end);
      edge.end = eVal;

      //3rd token is weight, the same as capacity
      String  capacity = st.nextToken();
      edge.capacity = Integer.parseInt (capacity);

      System.out.println(" edge: "+edge.start+" "+edge.end+" "+edge.capacity);

      edge.flow = 0;
      e[i] = edge;
      //map[sVal][eVal] = i;

      //now save edge information in nodes start and end
      v[sVal].fors.add(i);
      v[eVal].backs.add(i);

      i++;
      if (i == m) break;
    }

    visited = new int[v.length];
    path = new int[v.length];
    bestPath = new int[v.length];
  }

  // this function looks for a longest path starting from being to end,
  // using the backtrack depth-first search.
  public boolean findLongestPath(int begin, int end, int minLen){
      // compute a longest path from begin to end
      target = end;
      bestDistance = -100000000;
      minLength = minLen;
      dfsLongestPath(begin);
      if (bestDistance == -100000000) return false; else return true;
  }

  private void dfsLongestPath(int current) {
    visited[current] = 1;
    path[length++] = current;

    if (current == target && length >= minLength) { // a path from source to target is found.
    	if (distance > bestDistance) { // if a longer path is found
    		for (int i = 0; i < length; i++) bestPath[i] = path[i];
    		bestLength = length;
    		bestDistance = distance;
    	}
    } else {
    	//get current's edges
    	Vector<Integer> fors = v[current].fors;

    	//select an unvisited forward edge
    	for (int i = 0; i < fors.size(); i++) {
    		Integer edge_obj = (Integer)fors.elementAt(i);
    		int edge = edge_obj.intValue();

    		if (visited[e[edge].end] == 0) {//not yet visited
    			distance += e[edge].capacity;
    			dfsLongestPath(e[edge].end);
    			distance -= e[edge].capacity;
    		}
    	}
    }

    // finish with current and unmark current.
    visited[current] = 0;
    length--;
  }

  public String toString() {
	  String output = "v" + bestPath[0];
	  for (int i = 1; i < bestLength; i++) output = output + " -> v" + bestPath[i];
	  return output;
  }

  public static void main (String arg[]){
	  LongestPath lp = new LongestPath();
	  try {
		  lp = new LongestPath("graph1.txt");
	  }
	  catch (IOException e){
		  System.out.println ("Read file error, exit....");
		  System.exit(1);
	  }

	  // find a longest path from vertex 0 to vertex n-1.
	  if (lp.findLongestPath(0, lp.n-1, 1))
	      System.out.println ("Longest Path is " + lp + " and the distance is " + lp.bestDistance);
	  else System.out.println("No path from v0 to v" + (lp.n-1));

	  // find a longest path from vertex 3 to vertex 5.
	  if (lp.findLongestPath(3, 5, 1))
	      System.out.println ("Longest Path is " + lp + " and the distance is " + lp.bestDistance);
	  else System.out.println("No path from v3 to v5");

	  // find a longest path from vertex 5 to vertex 3.
	  if (lp.findLongestPath(lp.n-1, 3, 1))
	      System.out.println ("Longest Path is " + lp + " and the distance is " + lp.bestDistance);
	  else System.out.println("No path from v5 to v3");

  }
}