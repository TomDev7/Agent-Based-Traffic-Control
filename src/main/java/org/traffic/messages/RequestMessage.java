package org.traffic.messages;

import org.traffic.graph.TrafficManoeuvre;

public class RequestMessage implements TrafficMessage {

    private TrafficAction actionToBeTaken;
    private TrafficManoeuvre manoeuvre;

    public RequestMessage(TrafficAction actionToBeTaken, TrafficManoeuvre manoeuvre) {
        this.actionToBeTaken = actionToBeTaken;
        this.manoeuvre = manoeuvre;
    }

    public TrafficAction getActionToBeTaken() {
        return actionToBeTaken;
    }

    public void setActionToBeTaken(TrafficAction actionToBeTaken) {
        this.actionToBeTaken = actionToBeTaken;
    }

    public TrafficManoeuvre getManoeuvre() {
        return manoeuvre;
    }

    public void setManoeuvre(TrafficManoeuvre manoeuvre) {
        this.manoeuvre = manoeuvre;
    }
}
