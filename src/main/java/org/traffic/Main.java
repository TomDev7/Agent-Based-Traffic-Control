package org.traffic;

import akka.actor.typed.ActorSystem;
import org.traffic.agents.TemporaryAgent;

import java.io.IOException;

public class Main {

  public static void main(String[] args) {

    ActorSystem<TemporaryAgent.TrafficMessage> temporarySystem = ActorSystem.create(TemporaryAgent.create(), "TemporarySystem");


    temporarySystem.tell(new TemporaryAgent.InformationMessage("State 2"));
  }
}

