package org.traffic.agents;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.traffic.graph.TrafficManoeuvre;
import org.traffic.messages.TrafficAction;
import org.traffic.messages.TrafficCallback;

public class TrafficAgent extends AbstractBehavior<TrafficAgent.TrafficMessage> {

    // ===== Message Classes:

    public interface TrafficMessage {}

    public static class InformationMessage implements TrafficMessage {

        private TrafficAction actionTaken;
        private TrafficManoeuvre trafficManoeuvre;

        public InformationMessage(TrafficAction actionTaken, TrafficManoeuvre trafficManoeuvre) {
            this.actionTaken = actionTaken;
            this.trafficManoeuvre = trafficManoeuvre;
        }
    }

    public static class RequestMessage implements TrafficMessage {

        private TrafficAction actionToBeTaken;
        private TrafficManoeuvre manoeuvre;

        public RequestMessage(TrafficAction actionToBeTaken, TrafficManoeuvre manoeuvre) {
            this.actionToBeTaken = actionToBeTaken;
            this.manoeuvre = manoeuvre;
        }
    }

    public static class RequestReplyMessage implements TrafficMessage {

        private TrafficCallback callbackToRequest;

        public RequestReplyMessage(TrafficCallback callbackToRequest) {
            this.callbackToRequest = callbackToRequest;
        }
    }



    // ===== Agent setup:

    private String agentState = "state 1";

    public static Behavior<TrafficMessage> create() {
        return Behaviors.setup(context -> new TrafficAgent(context));
    }

    private TrafficAgent(ActorContext<TrafficMessage> context) {
        super(context);
    }


    // defining methods to be invoked after specific message type has been received
    @Override
    public Receive<TrafficMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(InformationMessage.class, this::onInformationMessage)
                .onMessage(RequestMessage.class, this::onRequestMessage)
                .onMessage(RequestReplyMessage.class, this::onRequestReplyMessage)
                .build();
    }

    // ===== Behaviors after specific message type received:

    private Behavior<TrafficMessage> onInformationMessage(InformationMessage informationMessage) {

        System.out.println("Traffic Information: " + informationMessage.trafficManoeuvre.toString() + " through " + this.toString() + " is now " + informationMessage.actionTaken);

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }

    private Behavior<TrafficMessage> onRequestMessage(RequestMessage requestMessage) {

        System.out.println("request message received: " + requestMessage.actionToBeTaken.toString() + " " + requestMessage.manoeuvre.toString());

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }

    private Behavior<TrafficMessage> onRequestReplyMessage(RequestReplyMessage requestReplyMessage) {

        System.out.println("Request replied: " + requestReplyMessage.callbackToRequest.toString());

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }
}
