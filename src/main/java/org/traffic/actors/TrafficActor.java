package org.traffic.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.traffic.graph.TrafficEdge;
import org.traffic.graph.TrafficManoeuvre;
import org.traffic.graph.TrafficNode;
import org.traffic.messages.*;

public class TrafficActor extends AbstractBehavior<TrafficMessage> {


    // ===== Agent setup:

    private String agentState = "state 1";
    private TrafficNode trafficNode;

    public static Behavior<TrafficMessage> create(TrafficNode tn) {
        return Behaviors.setup(context -> new TrafficActor(context, tn));
    }

    private TrafficActor(ActorContext<TrafficMessage> context, TrafficNode tn) {
        super(context);

        this.trafficNode = tn;
        System.out.println("Actor " + this + " is set to node " + this.trafficNode + " (id: " + this.trafficNode.getNodeId() + ")");
    }

    // methods to send the message to another Actor

    private int sendInformationMessage(ActorRef trafficActorRef, TrafficAction trafficAction, TrafficManoeuvre trafficManoeuvre) {

        trafficActorRef.tell(new InformationMessage(trafficAction, trafficManoeuvre));
        return 0;
    }

    private int sendRequestMessage(ActorRef trafficActorRef, TrafficAction actionToBeTaken, TrafficEdge trafficEdge) {

        trafficActorRef.tell(new RequestMessage(actionToBeTaken, trafficEdge));
        return 0;
    }

    private int sendRequestReplyMessage(ActorRef trafficActorRef, TrafficCallback trafficCallback) {

        trafficActorRef.tell(new RequestReplyMessage(trafficCallback));
        return 0;
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

        System.out.println("Traffic Information: " + informationMessage.getTrafficManoeuvre().toString() + " through " + this.toString() + " is now " + informationMessage.getActionTaken());

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }

    private Behavior<TrafficMessage> onRequestMessage(RequestMessage requestMessage) {

        System.out.println("request message received: " + requestMessage.getActionToBeTaken().toString() + " " + requestMessage.getEdge().toString());

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }

    private Behavior<TrafficMessage> onRequestReplyMessage(RequestReplyMessage requestReplyMessage) {

        System.out.println("Request replied: " + requestReplyMessage.getCallbackToRequest().toString());

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }
}
