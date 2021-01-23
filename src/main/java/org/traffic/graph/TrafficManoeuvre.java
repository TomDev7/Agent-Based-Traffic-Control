package org.traffic.graph;

public class TrafficManoeuvre {

    public TrafficNode sourceTrafficNode;
    public TrafficNode destinationTrafficNode;
    TrafficEdge destinationTrafficEdge; //TODO leave one of these two - only edge required
    public int awaitingCarsNumber;

    public TrafficManoeuvre(TrafficNode sourceTrafficNode, TrafficNode destinationTrafficNode) {
        this.sourceTrafficNode = sourceTrafficNode;
        this.destinationTrafficNode = destinationTrafficNode;
    }

    @Override
    public String toString() {
        return "from " + sourceTrafficNode.getNodeId() + " to " + destinationTrafficNode.getNodeId();
    }
}
