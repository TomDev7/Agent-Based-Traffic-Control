package org.traffic.agents;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class TemporaryAgent extends AbstractBehavior<TemporaryAgent.TrafficMessage> {

    public interface TrafficMessage {}

    public static class InformationMessage implements TrafficMessage {

        public final String someText;

        public InformationMessage(String someText) {
            this.someText = someText;
        }
    }

    private String agentState = "state 1";

    public static Behavior<TrafficMessage> create() {
        return Behaviors.setup(context -> new TemporaryAgent(context));
    }

    private TemporaryAgent(ActorContext<TrafficMessage> context) {
        super(context);
    }

    @Override
    public Receive<TrafficMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(InformationMessage.class, this::onInformationMessage)
                .build();
    }

    private Behavior<TrafficMessage> onInformationMessage(InformationMessage trafficMessage) {


        System.out.println("State: " + agentState + " -> " + trafficMessage.someText);
        agentState = trafficMessage.someText;

        return this;
    }
}
