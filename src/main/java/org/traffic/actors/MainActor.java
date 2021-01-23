package org.traffic.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.traffic.graph.TrafficEdge;
import org.traffic.graph.TrafficManoeuvre;
import org.traffic.graph.TrafficNode;
import org.traffic.messages.InformationMessage;
import org.traffic.messages.TrafficAction;
import org.traffic.messages.TrafficMessage;
import org.traffic.simulation.TrafficSimulationSupervisor;
import org.traffic.steering.TrafficLight;
import org.traffic.steering.TrafficLightState;
import java.awt.*;
import java.util.ArrayList;
import java.lang.reflect.Array;
import java.util.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;



public class MainActor extends AbstractBehavior<String> {

    ArrayList<TrafficNode> trafficNodesList = new ArrayList<>();
    public static ArrayList<TrafficEdge> trafficEdgesList = new ArrayList<>(); //
    ArrayList<ActorRef<TrafficMessage>> actorRefsList = new ArrayList<>();

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

        createTrafficNetworkNodes();
        createTrafficNetworkEdges();
        addNeighborsToNetworkNodes();
        addAvailableManoeuvresToNetworkNodes();
        createActorsForTrafficNodes();
        createTrafficLights();


        // ===== sending mock messages

        TrafficAction ta = TrafficAction.OPEN;
        TrafficManoeuvre tm = new TrafficManoeuvre(trafficNodesList.get(0), trafficNodesList.get(1));

        actorRefsList.get(0).tell(new InformationMessage(ta, tm));

        TrafficSimulationSupervisor tss = new TrafficSimulationSupervisor(trafficNodesList, trafficEdgesList, actorRefsList);
        tss.initSimulation();
        tss.startSimulation();

        return Behaviors.same();
    }

    void createTrafficNetworkNodes() {

        trafficNodesList.add(new TrafficNode(0));
        trafficNodesList.add(new TrafficNode(1));
        trafficNodesList.add(new TrafficNode(2));
        trafficNodesList.add(new TrafficNode(3));
        trafficNodesList.add(new TrafficNode(4));
        trafficNodesList.add(new TrafficNode(5));
        trafficNodesList.add(new TrafficNode(6));
        trafficNodesList.add(new TrafficNode(7));
        trafficNodesList.add(new TrafficNode(8));
        trafficNodesList.add(new TrafficNode(9));
        trafficNodesList.add(new TrafficNode(10));
        trafficNodesList.add(new TrafficNode(11));
        trafficNodesList.add(new TrafficNode(12));
        trafficNodesList.add(new TrafficNode(13));
        trafficNodesList.add(new TrafficNode(14));
        trafficNodesList.add(new TrafficNode(15));
        trafficNodesList.add(new TrafficNode(16));
        trafficNodesList.add(new TrafficNode(17));
        trafficNodesList.add(new TrafficNode(18));
    }

    void createTrafficNetworkEdges() {

        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(1), trafficNodesList.get(2), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(2), trafficNodesList.get(3), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(2), trafficNodesList.get(16), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(3), trafficNodesList.get(4), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(3), trafficNodesList.get(17), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(4), trafficNodesList.get(5), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(5), trafficNodesList.get(6), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(6), trafficNodesList.get(7), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(7), trafficNodesList.get(8), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(8), trafficNodesList.get(9), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(8), trafficNodesList.get(8), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(8), trafficNodesList.get(18), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(9), trafficNodesList.get(10), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(10), trafficNodesList.get(11), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(11), trafficNodesList.get(12), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(12), trafficNodesList.get(13), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(12), trafficNodesList.get(14), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(13), trafficNodesList.get(14), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(14), trafficNodesList.get(15), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(15), trafficNodesList.get(16), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(16), trafficNodesList.get(17), 0.1, 0.5));
        trafficEdgesList.add(new TrafficEdge(trafficNodesList.get(17), trafficNodesList.get(18), 0.1, 0.5));
        createGraph();
    }

    void addNeighborsToNetworkNodes() {

        for (TrafficEdge te : trafficEdgesList) {

            te.left.neighborNodes.add(te.right);
            te.right.neighborNodes.add(te.left);
        }
    }

    void addAvailableManoeuvresToNetworkNodes() {

        for (TrafficNode tn : trafficNodesList) {

            for (TrafficNode tnn1 : tn.neighborNodes) {

                for (TrafficNode tnn2 : tn.neighborNodes) {

                    if (tnn1.getNodeId() != tnn2.getNodeId()) {

                        tn.availableManoeuvres.add(new TrafficManoeuvre(tnn1, tnn2));
                    }
                }
            }
        }
    }

    void createActorsForTrafficNodes() {

        for (TrafficNode tn : trafficNodesList) {

            actorRefsList.add(getContext().spawn(TrafficActor.create(tn), "actor" + tn.getNodeId()));
            tn.nodeActor = actorRefsList.get(actorRefsList.size() - 1);
        }
    }


    void createTrafficLights() {

        for (TrafficNode tn : trafficNodesList) {

            for (TrafficManoeuvre tm : tn.availableManoeuvres) {

                tn.trafficLights.add(new TrafficLight(tm, TrafficLightState.GREEN));
            }
        }
    }
        public void createGraph () {
            HashMap<String, Integer[]> coordinates = new HashMap<String, Integer[]>();
            coordinates.put("0", new Integer[]{0, 0});
            coordinates.put("1", new Integer[]{100, 100});
            coordinates.put("2", new Integer[]{100, 70});
            coordinates.put("3", new Integer[]{100, 50});
            coordinates.put("4", new Integer[]{100, 20});
            coordinates.put("5", new Integer[]{100, 0});
            coordinates.put("6", new Integer[]{80, 0});
            coordinates.put("7", new Integer[]{50, 0});
            coordinates.put("8", new Integer[]{-20, 0});
            coordinates.put("9", new Integer[]{-80, 0});
            coordinates.put("10", new Integer[]{-80, 20});
            coordinates.put("11", new Integer[]{-80, 100});
            coordinates.put("12", new Integer[]{-50, 100});
            coordinates.put("13", new Integer[]{-40, 80});
            coordinates.put("14", new Integer[]{-30, 100});
            coordinates.put("15", new Integer[]{0, 100});
            coordinates.put("16", new Integer[]{0, 70});
            coordinates.put("17", new Integer[]{0, 50});
            coordinates.put("18", new Integer[]{-20, 50});

            System.setProperty("org.graphstream.ui", "swing");
            System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

            Graph graph = new SingleGraph("Tutorial 1");
            graph.setAttribute("ui.quality");
            graph.setAttribute("ui.antialias");
            graph.setAttribute("ui.stylesheet", "url('file://src/main/java/org/traffic/actors/stylesheet.css')");
            SpriteManager sman = new SpriteManager(graph);
            for (TrafficNode tn : trafficNodesList) {
                String id = tn.getNodeId() + "";
                graph.addNode(id);
                Node temp = graph.getNode(id);
                temp.setAttribute("ui.label", "Node:" + id);
                temp.setAttribute("ui.frozen");
                temp.setAttribute("x", coordinates.get(id)[0]);
                temp.setAttribute("y", coordinates.get(id)[1]);


            }

            float speedMax;
            Random rand = new Random();
            for (TrafficEdge te : trafficEdgesList) {
                String id = "" + te.left.getNodeId() + te.right.getNodeId();
                graph.addEdge(id, te.left.getNodeId() + "", te.right.getNodeId() + "");
                Edge edge = graph.getEdge(id);
                edge.setAttribute("ui.class", "Edge" + id);
                edge.setAttribute("speedMax", rand.nextInt(100));
                speedMax = (float) edge.getNumber("speedMax");
                edge.setAttribute("ui.color", speedMax / 100);
            }
            Viewer viewer = graph.display(false);
            View view = viewer.getDefaultView();
        }
    }
