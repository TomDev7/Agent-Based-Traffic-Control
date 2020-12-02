package org.traffic.messages;

import org.traffic.graph.TrafficManoeuvre;

public class TrafficMessageInformation extends TrafficMessage {

    private TrafficAction actionTaken;
    private TrafficManoeuvre trafficManoeuvre;

    public TrafficMessageInformation(TrafficAction actionTaken, TrafficManoeuvre trafficManoeuvre) {
        this.actionTaken = actionTaken;
        this.trafficManoeuvre = trafficManoeuvre;
    }
}
