package org.traffic.graph;

public class TrafficManoeuvre {

    TrafficNode sourceTrafficNode;
    TrafficNode destinationTrafficNode;

    public TrafficManoeuvre(TrafficNode sourceTrafficNode, TrafficNode destinationTrafficNode) {
        this.sourceTrafficNode = sourceTrafficNode;
        this.destinationTrafficNode = destinationTrafficNode;
    }

    @Override
    public String toString() {
        return "from " + sourceTrafficNode.getNodeId() + " to " + destinationTrafficNode.getNodeId();
    }
}
