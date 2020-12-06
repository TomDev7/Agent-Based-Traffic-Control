package org.traffic.messages;

import org.traffic.graph.TrafficManoeuvre;

public class InformationMessage implements TrafficMessage {

    private TrafficAction actionTaken;
    private TrafficManoeuvre trafficManoeuvre;

    public InformationMessage(TrafficAction actionTaken, TrafficManoeuvre trafficManoeuvre) {
        this.actionTaken = actionTaken;
        this.trafficManoeuvre = trafficManoeuvre;
    }

    public TrafficAction getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(TrafficAction actionTaken) {
        this.actionTaken = actionTaken;
    }

    public TrafficManoeuvre getTrafficManoeuvre() {
        return trafficManoeuvre;
    }

    public void setTrafficManoeuvre(TrafficManoeuvre trafficManoeuvre) {
        this.trafficManoeuvre = trafficManoeuvre;
    }
}