package org.traffic.simulation;


import akka.actor.typed.ActorRef;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
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
    Graph graph;


    //define simulation variables:
    public TrafficNode startNode;   //TODO assign appropriate value
    public int numOfCars;   //TODO assign appropriate value
    public static int TIME_TICK_IN_MILLISECONDS = 2000;
    public static int CARS_PER_TICK = 10;    //how many cars can go through a node (take a manoeuvre) during one simulation clock tick

    //simulation control variables:
    private boolean runSimulation;
    private int cycleNumber = 0;

    public TrafficSimulationSupervisor(ArrayList<TrafficNode> trafficNodesList, ArrayList<TrafficEdge> trafficEdgesList, ArrayList<ActorRef<TrafficMessage>> actorRefsList, Graph graph) {

        this.trafficNodesList = trafficNodesList;
        this.trafficEdgesList = trafficEdgesList;
        this.actorRefsList = actorRefsList;
        this.graph = graph;
    }

    public int initSimulation() {

        //TODO set traffic to given nodes
        trafficNodesList.get(1).availableManoeuvres.get(0).awaitingCarsNumber = 100;

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
            refreshgraph();
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
                if(te.right.availableManoeuvres.size() != 0 && te.right.availableManoeuvres.get(0) != null)
                    te.right.availableManoeuvres.get(0).awaitingCarsNumber += te.carAmountLeftToRight; //adding remaining cars to some manoeuvre
                te.carAmountLeftToRight = 0;
            }

            //for cars moving B to A along the edge
            if (te.carAmountRightToLeft > 0) {

                for (TrafficManoeuvre tm : te.left.availableManoeuvres ) {

                    int carsToMove = rand.nextInt(te.carAmountRightToLeft);
                    te.carAmountRightToLeft -= carsToMove;
                    tm.awaitingCarsNumber += carsToMove;
                }
                if(te.left.availableManoeuvres.size() != 0 && te.left.availableManoeuvres.get(0) != null)
                    te.left.availableManoeuvres.get(0).awaitingCarsNumber += te.carAmountRightToLeft; //adding remaining cars to some manoeuvre
                te.carAmountRightToLeft = 0;
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


    void refreshgraph(){

        int carAmount;
        for (TrafficNode tn : trafficNodesList)
        {
            String label ="";
            SpriteManager spriteManager = new SpriteManager(graph);
            String id = tn.getNodeId() + "";
            int i=0;
            int j=0;
            for (TrafficManoeuvre tm : tn.availableManoeuvres) {

                label = tm.toString() + "\n cars waiting: " + tm.awaitingCarsNumber + " \n";
                Sprite sprite = spriteManager.addSprite(id + "" + i);
                sprite.attachToNode(id);
                sprite.setPosition(StyleConstants.Units.PX, 75, 10 + 15 * i, 0);
                i+=1;
                sprite.setAttribute("ui.label", label);
                Sprite lightSprite = spriteManager.addSprite("tl" + id + "" + i);
                lightSprite.attachToNode(id);
                Node destNode = graph.getNode(tm.destinationTrafficNode.getNodeId());
                Node sourceNode = graph.getNode(tm.sourceTrafficNode.getNodeId());
                int z=0;
                int x =Integer.parseInt(destNode.getAttribute("x").toString());
                int y =Integer.parseInt(destNode.getAttribute("y").toString());
                if(j<1 || j>4) {
                    if (x > Integer.parseInt(sourceNode.getAttribute("x").toString())) {
                        lightSprite.setPosition(StyleConstants.Units.PX, -10, 10, 0);
                    }
                    else {
                        if (x < Integer.parseInt(sourceNode.getAttribute("x").toString()) && y == Integer.parseInt(sourceNode.getAttribute("y").toString())) {
                            lightSprite.setPosition(StyleConstants.Units.PX, -10, -10, 0);
                        }
                        else {
                                if (y < Integer.parseInt(sourceNode.getAttribute("y").toString())) {
                                            lightSprite.setPosition(StyleConstants.Units.PX, 10, -10, 0);
                                    }
                                else  {
                                        lightSprite.setPosition(StyleConstants.Units.PX, 10, 10, 0);
                                    }

                            }
                        }
                    if (tn.trafficLights.get(j).getTrafficLightsState() == TrafficLightState.GREEN)
                        lightSprite.setAttribute("ui.class", "greenlight");
                    else
                        lightSprite.setAttribute("ui.class", "redlight");
                }
                j+=1;
            }
        }
        for (TrafficEdge te : trafficEdgesList) {
            String id = "" + te.left.getNodeId() + te.right.getNodeId();
            Edge edge = graph.getEdge(id);
            edge.setAttribute("ui.class", "Edge" + id);
            edge.setAttribute("carAmount", te.carAmountLeftToRight + te.carAmountRightToLeft);
            carAmount = (int)edge.getNumber("carAmount");
            float edgeColor = carAmount / 100;
            edge.setAttribute("ui.color", edgeColor );
            edge.setAttribute("ui.label", carAmount);
        }

    }

}
