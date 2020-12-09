package org.traffic.graph;

public class TrafficEdge {
  public TrafficNode source;
  public TrafficNode destination;

  public TrafficEdge(TrafficNode source, TrafficNode destination) {
    this.source = source;
    this.destination = destination;
  }
}
