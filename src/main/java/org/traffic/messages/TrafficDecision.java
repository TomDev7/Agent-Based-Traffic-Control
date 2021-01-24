package org.traffic.messages;

import org.traffic.steering.TrafficLight;

public class TrafficDecision {

    public TrafficLight trafficLight;
    public TrafficAction trafficAction;

    public TrafficDecision(TrafficLight trafficLight, TrafficAction trafficAction) {
        this.trafficLight = trafficLight;
        this.trafficAction = trafficAction;
    }
}
