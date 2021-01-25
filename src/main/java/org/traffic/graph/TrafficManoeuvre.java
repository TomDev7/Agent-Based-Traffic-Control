package org.traffic.graph;

public class TrafficManoeuvre {

    public TrafficNode sourceTrafficNode;
    public TrafficNode destinationTrafficNode;
    TrafficEdge destinationTrafficEdge; //TODO leave one of these two - only edge required
    public int awaitingCarsNumber;
    public int waitingTime;
    public int wage1, wage2, wage3;

    public TrafficManoeuvre(TrafficNode sourceTrafficNode, TrafficNode destinationTrafficNode) {
        this.sourceTrafficNode = sourceTrafficNode;
        this.destinationTrafficNode = destinationTrafficNode;
        this.waitingTime = 0;
        this.wage1 = 1;
        this.wage2 = 1;
        this.wage3 = 1;
    }
    public void incrementTime()
    {
        waitingTime += 1;
    }
    @Override
    public String toString() {
        return "from " + sourceTrafficNode.getNodeId() + " to " + destinationTrafficNode.getNodeId();
    }
}
