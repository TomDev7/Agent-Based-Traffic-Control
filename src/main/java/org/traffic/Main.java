package org.traffic;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.traffic.actors.MainActor;
import javax.swing.*;


public class Main {


  public static void main(String[] args) {

    ActorRef<String> trafficSystem = ActorSystem.create(MainActor.create(), "MainSystem");
    trafficSystem.tell("start");

  }
}

