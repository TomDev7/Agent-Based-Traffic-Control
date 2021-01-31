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

            if (td.trafficAction == TrafficAction.CHANGE) {
                changeNodeLights(trafficNode);
            }
        }

        // ----- move remembered decision one step further in the queue. oldest will be overwritten later
        for (int i = 1; i < DECISION_HISTORY_LENGTH; i ++) {
            decisionsHistory[i-1] = decisionsHistory[i];
        }


        if(requestBuffer.size() > 1) // jesli 2 lub wiecej requestow
        {
            decisionsHistory[DECISION_HISTORY_LENGTH -1].add(new TrafficDecision(trafficNode, TrafficAction.CHANGE));
        }
        else{ // jesli nie to utopia
            if(changeState(trafficNode))
            {
                decisionsHistory[DECISION_HISTORY_LENGTH -1].add(new TrafficDecision(trafficNode, TrafficAction.CHANGE));
            }
            else
                decisionsHistory[DECISION_HISTORY_LENGTH -1].add(new TrafficDecision(trafficNode, TrafficAction.STAY));

        }

        for(TrafficManoeuvre tm : trafficNode.availableManoeuvres) // wysylanie requestu
        {
            if(tm.awaitingCarsNumber > 50)
            {
                for(TrafficLight destLight : tm.destinationTrafficNode.trafficLights)
                {
                    TrafficNode sourceNode = destLight.getTrafficManoeuvre().sourceTrafficNode;
                    if(sourceNode == trafficNode && destLight.getTrafficLightsState() == TrafficLightState.RED)
                        sendRequest(tm.destinationTrafficNode.nodeActor, TrafficAction.CHANGE);
                }

            }
        }
        requestBuffer.clear();  //clearing request buffer for next iteration

        return this;    // 'this' because the Agent's behaviour does not change for the next message (we can call it a state)
    }

    int sendRequest(ActorRef actorRef,TrafficAction trafficAction) {

        actorRef.tell(new RequestMessage(trafficAction, this.trafficNode)); //send request to node B so that it opens all manouevres coming from this node

        return 0;
    }

    void changeNodeLights(TrafficNode trafficNode){
        for(TrafficLight tl : trafficNode.trafficLights){
            if(tl.getTrafficLightsState() ==  TrafficLightState.GREEN)
                tl.setTrafficLightsState(TrafficLightState.RED);
            else
                tl.setTrafficLightsState(TrafficLightState.GREEN);
        }

    }
}
