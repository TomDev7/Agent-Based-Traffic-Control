package org.traffic.messages;

import org.traffic.graph.TrafficManoeuvre;

public class TrafficMessageRequest extends TrafficMessage {

    private TrafficAction actionToBeTaken;
    private TrafficManoeuvre manoeuvre;

    public TrafficMessageRequest(TrafficAction actionToBeTaken, TrafficManoeuvre manoeuvre) {
        this.actionToBeTaken = actionToBeTaken;
        this.manoeuvre = manoeuvre;
    }
}
