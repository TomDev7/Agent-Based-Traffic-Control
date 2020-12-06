package org.traffic;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import org.traffic.actors.MainActor;
import org.traffic.graph.TrafficManoeuvre;
import org.traffic.graph.TrafficNode;
import org.traffic.messages.TrafficAction;

public class Main {


  public static void main(String[] args) {

    ActorRef<String> trafficSystem = ActorSystem.create(MainActor.create(), "MainSystem");
    trafficSystem.tell("start");





  }
}

