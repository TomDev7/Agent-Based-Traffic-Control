package org.traffic.messages;

import org.traffic.graph.TrafficManoeuvre;
import org.traffic.graph.TrafficNode;

public class RequestMessage implements TrafficMessage {

    private TrafficAction actionToBeTaken;
    private TrafficNode sourceNode;

    public RequestMessage(TrafficAction actionToBeTaken, TrafficNode sourceNode) {
        this.actionToBeTaken = actionToBeTaken;
        this.sourceNode = sourceNode;
    }

    public TrafficAction getActionToBeTaken() {
        return actionToBeTaken;
    }

    public void setActionToBeTaken(TrafficAction actionToBeTaken) {
        this.actionToBeTaken = actionToBeTaken;
    }

    public TrafficNode getSourceNode() {
        return sourceNode;
    }

    public void setManoeuvre(TrafficNode sourceNode) {
        this.sourceNode = sourceNode;
    }
}
