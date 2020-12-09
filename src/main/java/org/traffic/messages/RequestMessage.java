package org.traffic.messages;

import org.traffic.graph.TrafficEdge;
import org.traffic.graph.TrafficManoeuvre;
import org.traffic.graph.TrafficNode;

public class RequestMessage implements TrafficMessage {

    private TrafficAction actionToBeTaken;
    private TrafficEdge edge;

    public RequestMessage(TrafficAction actionToBeTaken, TrafficEdge trafficEdge) {     //OPEN or CLOSE road to or from particular node (edge from src to dst)
        this.actionToBeTaken = actionToBeTaken;
        this.edge = edge;
    }

    public TrafficAction getActionToBeTaken() {
        return actionToBeTaken;
    }

    public void setActionToBeTaken(TrafficAction actionToBeTaken) {
        this.actionToBeTaken = actionToBeTaken;
    }

    public TrafficEdge getEdge() {
        return edge;
    }

    public void setEdge(TrafficEdge edge) {
        this.edge = edge;
    }
}
