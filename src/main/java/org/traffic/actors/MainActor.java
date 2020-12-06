package org.traffic.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.traffic.graph.TrafficManoeuvre;
import org.traffic.graph.TrafficNode;
import org.traffic.messages.InformationMessage;
import org.traffic.messages.TrafficAction;
import org.traffic.messages.TrafficMessage;

import java.util.ArrayList;

public class MainActor extends AbstractBehavior<String> {

    public static Behavior<String> create() {

        return Behaviors.setup(MainActor::new);
    }

    private MainActor(ActorContext<String> context) {
        super(context);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder().onMessageEquals("start", this::start).build();
    }

    private Behavior<String> start() {

        ArrayList<TrafficNode> trafficNodesList = new ArrayList<>();

        trafficNodesList.add(new TrafficNode(1));

        TrafficNode tn1 = new TrafficNode(1);
        TrafficNode tn2 = new TrafficNode(2);

        ActorRef<TrafficMessage> actor1Ref = getContext().spawn(TrafficActor.create(tn1), "tn1");
        ActorRef<TrafficMessage> actor2Ref = getContext().spawn(TrafficActor.create(tn2), "tn1");

        System.out.println("tn1: " + actor1Ref);
        System.out.println("tn2: " + actor2Ref);

        // ===== sending mock messages


        TrafficAction ta = TrafficAction.OPEN;
        TrafficManoeuvre tm = new TrafficManoeuvre(tn1, tn2);

        actor1Ref.tell(new InformationMessage(ta, tm));

        return Behaviors.same();
    }
}
