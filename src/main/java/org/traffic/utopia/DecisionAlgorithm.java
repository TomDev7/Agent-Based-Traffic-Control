package org.traffic.utopia;

import org.traffic.actors.TrafficActor;
import org.traffic.actors.MainActor;
import org.traffic.graph.TrafficEdge;
import org.traffic.graph.TrafficNode;

public class DecisionAlgorithm {
    public static double costFunction(TrafficActor trafficActor)
    {
        double costFunction = 0;

        for (TrafficEdge trafficEdge : MainActor.trafficEdgesList
             ) {
            if(trafficEdge.left == trafficActor.trafficNode || trafficEdge.right == trafficActor.trafficNode){
                costFunction += trafficEdge.carAmount * trafficEdge.trafficWage;
                costFunction += trafficEdge.redLightTimer * trafficEdge.timeWage;
            }
        }
    return costFunction;
    }

    public static void minimizeCostFunction(TrafficActor trafficActor){
        double disabledTrafficCost = 0;
        double activeTrafficCost =0;
        for (TrafficEdge trafficEdge : MainActor.trafficEdgesList
        ){
            if(trafficEdge.left == trafficActor.trafficNode || trafficEdge.right == trafficActor.trafficNode) {
                if (trafficEdge.redLightTimer == 0)
                    disabledTrafficCost += trafficEdge.redLightTimer * trafficEdge.timeWage + trafficEdge.trafficWage * trafficEdge.carAmount;
                else
                    activeTrafficCost += trafficEdge.trafficWage * trafficEdge.carAmount;
            }
        }
        if(disabledTrafficCost > activeTrafficCost){
            // TODO zmien stan swiatel
        }
    }
}
