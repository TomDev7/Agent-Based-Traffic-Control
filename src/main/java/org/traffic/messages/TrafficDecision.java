package org.traffic.messages;

import org.traffic.graph.TrafficNode;
import org.traffic.steering.TrafficLight;

public class TrafficDecision {

    public TrafficNode trafficNode;
    public TrafficAction trafficAction;

    public TrafficDecision(TrafficNode trafficNode, TrafficAction trafficAction) {
        this.trafficNode = trafficNode;
        this.trafficAction = trafficAction;
    }
}
