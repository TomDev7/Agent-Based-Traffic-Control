package org.traffic.simulation;


import akka.actor.typed.ActorRef;
import org.traffic.graph.TrafficEdge;
import org.traffic.graph.TrafficNode;
import org.traffic.messages.TrafficMessage;

import java.util.ArrayList;

//this class runs the simulation - "moves" cars, decides about traffic direction and changes
//usage: initSimulation() -> startSimulation() -> pauseSimulation() -> clearSimulation()
public class TrafficSimulationSupervisor {


    ArrayList<TrafficNode> trafficNodesList = new ArrayList<>();
    ArrayList<TrafficEdge> trafficEdgesList = new ArrayList<>();
    ArrayList<ActorRef<TrafficMessage>> actorRefsList = new ArrayList<>();

    //define simulation variables:
    public TrafficNode startNode;   //TODO
    public int numOfCars;   //TODO
    public static int TIME_TICK_IN_MILLISECONDS = 5000;
    public static int CARS_PER_TICK = 3;    //how many cars can go through a node (take a manoeuvre) during one simulation clock tick

    //simulation control variables:
    private boolean runSimulation;

    public TrafficSimulationSupervisor(ArrayList<TrafficNode> trafficNodesList, ArrayList<TrafficEdge> trafficEdgesList, ArrayList<ActorRef<TrafficMessage>> actorRefsList) {

        this.trafficNodesList = trafficNodesList;
        this.trafficEdgesList = trafficEdgesList;
        this.actorRefsList = actorRefsList;
    }

    public int initSimulation() {

        //TODO set traffic to given nodes
        return 1;
    }

    public int startSimulation() {

        runSimulation = true;
        simulationLoopCycle();
        return 1;
    }

    public int pauseSimulation() {

        runSimulation = false;
        return 1;
    }

    public int clearSimulation() {

        //TODO iterate over all edges and nodes and clear traffic
        return 1;
    }

    public int simulationLoop() {

        while (runSimulation == true) {

            simulationLoopCycle();

            try {
                Thread.sleep(TIME_TICK_IN_MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return 1;
    }

    public int simulationLoopCycle() {



        return 1;
    }
}
