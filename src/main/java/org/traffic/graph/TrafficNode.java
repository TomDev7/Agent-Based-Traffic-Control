package org.traffic.graph;

import java.util.ArrayList;

public class TrafficNode {

    private int nodeId;
    public ArrayList<TrafficNode> neighborNodes = new ArrayList<>();
    public ArrayList<TrafficManoeuvre> availableManoeuvres = new ArrayList<>();

    public TrafficNode(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }
}
