package org.traffic.messages;

public class RequestReplyMessage implements TrafficMessage {

    private TrafficCallback callbackToRequest;

    public RequestReplyMessage(TrafficCallback callbackToRequest) {
        this.callbackToRequest = callbackToRequest;
    }

    public TrafficCallback getCallbackToRequest() {
        return callbackToRequest;
    }

    public void setCallbackToRequest(TrafficCallback callbackToRequest) {
        this.callbackToRequest = callbackToRequest;
    }
}