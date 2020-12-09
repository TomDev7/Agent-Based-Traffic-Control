package org.traffic.graph;

import akka.actor.typed.ActorRef;
import org.traffic.actors.TrafficActor;
import org.traffic.messages.TrafficMessage;

import java.util.ArrayList;

public class TrafficNode {

    private int nodeId;
    public ActorRef<TrafficMessage> nodeActor;
    public ArrayList<TrafficNode> neighborNodes = new ArrayList<>();
    public ArrayList<TrafficManoeuvre> availableManoeuvres = new ArrayList<>();



    public TrafficNode(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }
}
