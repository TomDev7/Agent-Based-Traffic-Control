package org.traffic.graph;

public class TrafficEdge {
  public TrafficNode left;
  public TrafficNode right;
  public double trafficWage;
  public double timeWage;
  public int redLightTimer;
  public int carAmountLeftToRight;
  public int carAmountRightToLeft;

  public TrafficEdge(TrafficNode left, TrafficNode right, double trafficWage, double timeWage) {
    this.left = left;
    this.right = right;
    this.trafficWage = trafficWage;
    this.timeWage = timeWage;
    this.carAmountLeftToRight = 0;
    this.carAmountRightToLeft = 0;
    this.redLightTimer = 0;
  }
  public void changeTimeWage(double value){
    this.timeWage += value;
  }
  public void changeTrafficWage(double value){
    this.trafficWage += value;
  }

}
