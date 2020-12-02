package org.traffic.graph;

public class TrafficEdge {
  private TrafficNode left;
  private TrafficNode right;

  public TrafficEdge(TrafficNode left, TrafficNode right) {
    this.left = left;
    this.right = right;
  }
}
