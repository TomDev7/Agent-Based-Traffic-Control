package org.traffic.graph;

public class TrafficEdge {
  public TrafficNode left;
  public TrafficNode right;

  public TrafficEdge(TrafficNode left, TrafficNode right) {
    this.left = left;
    this.right = right;
  }
}
