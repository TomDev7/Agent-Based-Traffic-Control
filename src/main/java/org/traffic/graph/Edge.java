package org.traffic.graph;

public class Edge {
  private Node left;
  private Node right;

  public Edge(Node left, Node right) {
    this.left = left;
    this.right = right;
  }
}
