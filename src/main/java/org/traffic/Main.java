package org.traffic;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.traffic.actors.MainActor;
import javax.swing.*;


public class Main {


  public static void main(String[] args) {

    ActorRef<String> trafficSystem = ActorSystem.create(MainActor.create(), "MainSystem");
    trafficSystem.tell("start");


    System.setProperty("org.graphstream.ui", "swing");

    Graph graph = new SingleGraph("Tutorial 1");

    graph.addNode("A");
    graph.addNode("B");
    graph.addNode("C");
    graph.addEdge("AB", "A", "B");
    graph.addEdge("BC", "B", "C");
    graph.addEdge("CA", "C", "A");

    graph.display();


  }
}

