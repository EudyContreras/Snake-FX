package com.EudyContreras.Snake.PathFindingAI;

import java.util.*;

public class LongestPath {
    public static void main(String[] args) {
        // construct an n-by-n grid
        int n = 12;
        Node[][] node = new Node[n][n];
        List<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                nodes.add((node[i][j] = new Node()));
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i >= 1) {
                    if (j >= 1) {
                        node[i - 1][j - 1].addEdge(node[i][j]);
                    }
                    node[i - 1][j].addEdge(node[i][j]);
                    if (j < n - 1) {
                        node[i - 1][j + 1].addEdge(node[i][j]);
                    }
                }
                if (j >= 1) {
                    node[i][j - 1].addEdge(node[i][j]);
                }
            }
        }
        findPath(nodes);
        labelPath(nodes);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%4d", node[i][j].label);
            }
            System.out.println();
        }
    }

    private static void findPath(List<Node> nodes) {
        for (Node node : nodes) {
            node.isOnPath = false;
        }
        Random random = new Random();
        Node sink = nodes.get(random.nextInt(nodes.size()));
        sink.isOnPath = true;
        int isNotOnPathCount = nodes.size() - 1;
        while (isNotOnPathCount > 0) {
            sink.pathOut = sink.out.get(random.nextInt(sink.out.size()));
            sink = sink.pathOut.head;
            if (sink.isOnPath) {
                // rotate
                sink = sink.pathOut.head;
                Arc reverse = null;
                Node node = sink;
                do {
                    Arc temp = node.pathOut;
                    node.pathOut = reverse;
                    reverse = temp.reverse;
                    node = temp.head;
                } while (node != sink);
            } else {
                // extend
                sink.isOnPath = true;
                isNotOnPathCount--;
            }
        }
    }

    private static void labelPath(Collection<Node> nodes) {
        for (Node node : nodes) {
            node.isSource = true;
        }
        for (Node node : nodes) {
            if (node.pathOut != null) {
                node.pathOut.head.isSource = false;
            }
        }
        Node source = null;
        for (Node node : nodes) {
            if (node.isSource) {
                source = node;
                break;
            }
        }
        int count = 0;
        while (true) {
            source.label = ++count;
            if (source.pathOut == null) {
                break;
            }
            source = source.pathOut.head;
        }
    }
}

class Node {
    public final List<Arc> out = new ArrayList<Arc>();
    public boolean isOnPath;
    public Arc pathOut;
    public boolean isSource;
    public int label;

    public void addEdge(Node that) {
        Arc arc = new Arc(this, that);
        this.out.add(arc.reverse);
        that.out.add(arc);
    }
}

class Arc {
    public final Node head;
    public final Arc reverse;

    private Arc(Node head, Arc reverse) {
        this.head = head;
        this.reverse = reverse;
    }

    public Arc(Node head, Node tail) {
        this.head = head;
        this.reverse = new Arc(tail, this);
    }
}