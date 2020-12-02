package org.traffic;

import akka.actor.typed.ActorSystem;
import org.traffic.agents.TrafficAgent;
import org.traffic.graph.TrafficManoeuvre;
import org.traffic.graph.TrafficNode;
import org.traffic.messages.TrafficAction;
import org.traffic.messages.TrafficCallback;

public class Main {


  public static void main(String[] args) {

    ActorSystem<TrafficAgent.TrafficMessage> trafficActorSystem = ActorSystem.create(TrafficAgent.create(), "TrafficActorSystem");


    // ===== sending mock messages


    TrafficNode tn1 = new TrafficNode(1);
    TrafficNode tn2 = new TrafficNode(2);
    TrafficAction ta = TrafficAction.OPEN;
    TrafficManoeuvre tm = new TrafficManoeuvre(tn1, tn2);

    trafficActorSystem.tell(new TrafficAgent.InformationMessage(ta, tm));
  }
}

