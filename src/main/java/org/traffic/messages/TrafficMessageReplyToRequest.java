package org.traffic.messages;

public class TrafficMessageReplyToRequest extends TrafficMessage {

    private TrafficCallback callbackToRequest;

    public TrafficMessageReplyToRequest(TrafficCallback callbackToRequest) {
        this.callbackToRequest = callbackToRequest;
    }
}
