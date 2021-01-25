package org.traffic.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.traffic.graph.TrafficManoeuvre;
import org.traffic.graph.TrafficNode;
import org.traffic.messages.*;
import org.traffic.steering.TrafficLight;
import org.traffic.steering.TrafficLightState;

import java.util.ArrayList;

import static org.traffic.utopia.DecisionAlgorithm.changeState;

public class TrafficActor extends AbstractBehavior<TrafficMessage> {


    // ===== Agent setup:

    private String agentState = "state 1";
    public TrafficNode trafficNode;
    //private ArrayList<Pair<TrafficLight, TrafficCondition>> trafficLevelForManoeuvre = new ArrayList<>();   //pairs particular manoeuvre with number of cars awaiting for it
    private ArrayList<RequestMessage> requestBuffer = new ArrayList<>();
    private ArrayList<TrafficDecision>[] decisionsHistory;

    final int DECISION_HISTORY_LENGTH = 3;


    public static Behavior<TrafficMessage> create(TrafficNode tn) {
        return Behaviors.setup(context -> new TrafficActor(context, tn));
    }

    private TrafficActor(ActorContext<TrafficMessage> context, TrafficNode tn) {
        super(context);

        this.trafficNode = tn;
        System.out.println("Actor " + this + " is set to node " + this.trafficNode + " (id: " + this.trafficNode.getNodeId() + ")");

        decisionsHistory = new ArrayList[DECISION_HISTORY_LENGTH];
        for (int i = 0; i < DECISION_HISTORY_LENGTH; i++) {

            decisionsHistory[i] = new ArrayList<>();
        }
    }


    // defining methods to be invoked after specific message type has been received
    @Override
    public Receive<TrafficMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(InformationMessage.class, this::onInformationMessage)
                .onMessage(RequestMessage.class, this::onRequestMessage)
                .onMessage(RequestReplyMessage.class, this::onRequestReplyMessage)
                .onMessage(SimulationSyncMessage.class, this::onSimulationSyncMessage)
                .build();
    }

    // ===== Behaviors after specific message type received:

    private Behavior<TrafficMessage> onInformationMessage(InformationMessage informationMessage) {

        System.out.println("Traffic Information: " + informationMessage.getTrafficManoeuvre().toString() + " through " + this.toString() + " is now " + informationMessage.getActionTaken());

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }

    private Behavior<TrafficMessage> onRequestMessage(RequestMessage requestMessage) {

        System.out.println("request message received: " + requestMessage.getActionToBeTaken().toString() + " from " + requestMessage.getSourceNode().toString());

        requestBuffer.add(requestMessage);  //messages will be used at the end of simulation iteration for decision calculation

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }

    private Behavior<TrafficMessage> onRequestReplyMessage(RequestReplyMessage requestReplyMessage) {

        System.out.println("Request replied: " + requestReplyMessage.getCallbackToRequest().toString());

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }

    private Behavior<TrafficMessage> onSimulationSyncMessage(SimulationSyncMessage simulationSyncMessage) {

        System.out.println("Simulation Sync Message Received. Node: " + this.trafficNode.getNodeId() + ", iteration number: " + simulationSyncMessage.getSimulationIterationNumber());

        // here main logic of Actor is triggerd - traffic steering calculation, taking into account previous messages received during current iteration

        // ----- execute remembered decision:
        for (TrafficDecision td : decisionsHistory[0]) {

            if (td.trafficAction == TrafficAction.OPEN) {
                td.trafficLight.setTrafficLightsState(TrafficLightState.GREEN);
            } else if (td.trafficAction == TrafficAction.CLOSE) {
                td.trafficLight.setTrafficLightsState(TrafficLightState.RED);
            }
        }

        // ----- move remembered decision one step further in the queue. oldest will be overwritten later
        for (int i = 1; i < DECISION_HISTORY_LENGTH; i ++) {
            decisionsHistory[i-1] = decisionsHistory[i];
        }

        // ----- TODO sum up current traffic

        int summedTraffic[] = new int[trafficNode.neighborNodes.size()];

        for (TrafficManoeuvre tm : trafficNode.availableManoeuvres) {

        }

        if(changeState(trafficNode))
        {
//            java.awt.Toolkit.getDefaultToolkit().beep();
            for(TrafficLight tl : trafficNode.trafficLights){
                if(tl.getTrafficLightsState() ==  TrafficLightState.GREEN)
                    tl.setTrafficLightsState(TrafficLightState.RED);
                else
                    tl.setTrafficLightsState(TrafficLightState.GREEN);
            }
        }



        // ----- TODO calculate decision based on own traffic data

        int tempMaxVoteFromOwnDecision = 1; //TODO replace with actual calculated value

        // -----  taking into account requests from neighbouring nodes:

        int decisionCounter[] = new int[trafficNode.trafficLights.size()];  // the bigger the number, the higher priority to OPEN manoeuvres

        for(RequestMessage request : requestBuffer) {

            int i = 0;
            for(TrafficLight trafficLight : trafficNode.trafficLights) {        // in theory, all traffic lights for maneouvres with the same sourceNode schould end up with the same 'vote' number

                if (trafficLight.getTrafficManoeuvre().sourceTrafficNode.equals(request.getSourceNode())) {

                    if (request.getActionToBeTaken() == TrafficAction.OPEN) {
                        decisionCounter[i]++;
                    } else if (request.getActionToBeTaken() == TrafficAction.CLOSE) {
                        decisionCounter[i]--;
                    }
                }
                i++;
            }
        }

        int maxVote = tempMaxVoteFromOwnDecision;

        for (int i = 0; i < trafficNode.trafficLights.size(); i++) {

            maxVote = Math.max(maxVote, decisionCounter[i]);
        }

        for (int i = 0; i < trafficNode.trafficLights.size(); i++) {


            if (decisionCounter[i] == maxVote) {
                decisionsHistory[3].add(new TrafficDecision(trafficNode.trafficLights.get(i), TrafficAction.OPEN));  // remember to OPEN all manouevres with highest votes
            } else {
                decisionsHistory[3].add(new TrafficDecision(trafficNode.trafficLights.get(i), TrafficAction.CLOSE)); // remember to CLOSE all other manouevres
            }
        }

        requestBuffer.clear();  //clearing request buffer for next iteration

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }

    int sendRequest(ActorRef actorRef,TrafficAction trafficAction) {

        actorRef.tell(new RequestMessage(trafficAction, this.trafficNode)); //send request to node B so that it opens all manouevres coming from this node

        return 0;
    }
}
