package org.traffic.simulation;


import akka.actor.typed.ActorRef;
import org.traffic.actors.TrafficActor;
import org.traffic.graph.TrafficEdge;
import org.traffic.graph.TrafficManoeuvre;
import org.traffic.graph.TrafficNode;
import org.traffic.messages.SimulationSyncMessage;
import org.traffic.messages.TrafficMessage;
import org.traffic.steering.TrafficLight;
import org.traffic.steering.TrafficLightState;

import java.util.ArrayList;
import java.util.Random;

//this class runs the simulation - "moves" cars, decides about traffic direction and changes
//usage: initSimulation() -> startSimulation() -> pauseSimulation() -> clearSimulation()
public class TrafficSimulationSupervisor {


    ArrayList<TrafficNode> trafficNodesList = new ArrayList<>();
    ArrayList<TrafficEdge> trafficEdgesList = new ArrayList<>();
    ArrayList<ActorRef<TrafficMessage>> actorRefsList = new ArrayList<>();

    //define simulation variables:
    public TrafficNode startNode;   //TODO assign appropriate value
    public int numOfCars;   //TODO assign appropriate value
    public static int TIME_TICK_IN_MILLISECONDS = 5000;
    public static int CARS_PER_TICK = 3;    //how many cars can go through a node (take a manoeuvre) during one simulation clock tick

    //simulation control variables:
    private boolean runSimulation;
    private int cycleNumber = 0;

    public TrafficSimulationSupervisor(ArrayList<TrafficNode> trafficNodesList, ArrayList<TrafficEdge> trafficEdgesList, ArrayList<ActorRef<TrafficMessage>> actorRefsList) {

        this.trafficNodesList = trafficNodesList;
        this.trafficEdgesList = trafficEdgesList;
        this.actorRefsList = actorRefsList;
    }

    public int initSimulation() {

        //TODO set traffic to given nodes
        trafficNodesList.get(1).availableManoeuvres.get(0).awaitingCarsNumber = 10;

        return 1;
    }

    public int startSimulation() {

        runSimulation = true;
        simulationLoop();
        return 1;
    }

    public int pauseSimulation() {

        runSimulation = false;
        return 1;
    }

    public int clearSimulation() {

        for (TrafficNode tn : trafficNodesList) {

            for (TrafficManoeuvre tm : tn.availableManoeuvres) {

                tm.awaitingCarsNumber = 0;
            }
        }

        for (TrafficEdge te : trafficEdgesList) {

            te.carAmountLeftToRight = 0;
            te.carAmountRightToLeft = 0;
        }

        cycleNumber = 0;

        return 1;
    }

    public int simulationLoop() {

        while (runSimulation == true) {

            simulationLoopCycle();
            sendNewIterationMessages();

            try {
                Thread.sleep(TIME_TICK_IN_MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return 1;
    }

    public int simulationLoopCycle() {

        System.out.println("Simulation cycle " + cycleNumber);
        printGraphReport();

        // moves cars from lanes (awaiting for manoeuvre) to the edge after the node
        for (TrafficNode tn : trafficNodesList) {

            for (TrafficLight tl : tn.trafficLights) {

                if (tl.getTrafficLightsState() == TrafficLightState.GREEN) {



                    int carsMoved = 0;

                    if (tl.getTrafficManoeuvre().awaitingCarsNumber == 0) {

                    }
                    else if (tl.getTrafficManoeuvre().awaitingCarsNumber >= CARS_PER_TICK) {
                        tl.getTrafficManoeuvre().awaitingCarsNumber -= CARS_PER_TICK;
                        carsMoved = CARS_PER_TICK;
                    } else {
                        carsMoved = tl.getTrafficManoeuvre().awaitingCarsNumber;
                        tl.getTrafficManoeuvre().awaitingCarsNumber = 0;
                    }

                    //TODO needs a lot of optimisation
                    for (TrafficEdge te : trafficEdgesList) {

                        int te_left_nodeID = te.left.getNodeId();
                        int te_right_nodeID = te.right.getNodeId();
                        int tn_nodeID = tn.getNodeId();
                        int tl_traffic_manoeuvre_destination_traffic_node_ID = tl.getTrafficManoeuvre().destinationTrafficNode.getNodeId();

                        if (te.left.getNodeId() == tn.getNodeId() && te.right.getNodeId() == tl.getTrafficManoeuvre().destinationTrafficNode.getNodeId()) {

                            te.carAmountLeftToRight += carsMoved;
                            break;
                        } else if (te.right.getNodeId() == tn.getNodeId() && te.left.getNodeId() == tl.getTrafficManoeuvre().destinationTrafficNode.getNodeId()) {

                            te.carAmountRightToLeft += carsMoved;
                            break;
                        }
                    }

                    System.out.println(carsMoved + " cars moved from " + tl.getTrafficManoeuvre().sourceTrafficNode + " to " + tl.getTrafficManoeuvre().destinationTrafficNode);

                }
            }
        }

        //shuffles cars waiting at the edge to lanes awaiting for manoeuvre
        Random rand = new Random();

        for (TrafficEdge te : trafficEdgesList) {

            //for cars moving A to B along the edge
            if (te.carAmountLeftToRight > 0) {

                for (TrafficManoeuvre tm : te.right.availableManoeuvres) {

                    int carsToMove = rand.nextInt(te.carAmountLeftToRight);
                    te.carAmountLeftToRight -= carsToMove;
                    tm.awaitingCarsNumber += carsToMove;
                }

                te.right.availableManoeuvres.get(0).awaitingCarsNumber += te.carAmountLeftToRight; //adding remaining cars to some manoeuvre
            }

            //for cars moving B to A along the edge
            if (te.carAmountRightToLeft > 0) {

                for (TrafficManoeuvre tm : te.left.availableManoeuvres) {

                    int carsToMove = rand.nextInt(te.carAmountRightToLeft);
                    te.carAmountRightToLeft -= carsToMove;
                    tm.awaitingCarsNumber += carsToMove;
                }

                te.left.availableManoeuvres.get(0).awaitingCarsNumber += te.carAmountRightToLeft; //adding remaining cars to some manoeuvre
            }
        }

        cycleNumber++;

        return 1;
    }

    private int sendNewIterationMessages() {

        for (ActorRef actorRef : actorRefsList) {

            actorRef.tell(new SimulationSyncMessage(cycleNumber));
        }

        return 0;
    }

    int printGraphReport() {

        for (TrafficNode tn : trafficNodesList) {
            for (TrafficManoeuvre tm : tn.availableManoeuvres) {

                System.out.println("Node: " + tn.getNodeId() + ", manoeuvre: " + tm.toString() + ", cars waiting: " + tm.awaitingCarsNumber);
            }
        }

        for (TrafficEdge te : trafficEdgesList) {
            System.out.println("Edge: " + te.toString() + ", cars A to B: " + te.carAmountLeftToRight);
            System.out.println("Edge: " + te.toString() + ", cars B to A: " + te.carAmountRightToLeft);
        }

        return 1;
    }

}
