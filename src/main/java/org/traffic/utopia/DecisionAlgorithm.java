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
            if(trafficEdge.left == trafficActor.trafficNode) {
                costFunction += trafficEdge.carAmountLeftToRight * trafficEdge.trafficWage;
                costFunction += trafficEdge.redLightTimer * trafficEdge.timeWage;
            }

            if(trafficEdge.right == trafficActor.trafficNode) {
                costFunction += trafficEdge.carAmountRightToLeft * trafficEdge.trafficWage;
                costFunction += trafficEdge.redLightTimer * trafficEdge.timeWage;
            }
        }
    return costFunction;
    }

    public static boolean changeState(TrafficNode trafficNode){
        double disabledTrafficCost = 0;
        double activeTrafficCost =0;
        for (TrafficEdge trafficEdge : MainActor.trafficEdgesList
        ){
            if(trafficEdge.left == trafficNode) {
                if (trafficEdge.redLightTimer == 0)
                    disabledTrafficCost += trafficEdge.redLightTimer * trafficEdge.timeWage + trafficEdge.trafficWage * trafficEdge.carAmountLeftToRight;
                else
                    activeTrafficCost += trafficEdge.trafficWage * trafficEdge.carAmountLeftToRight;
            } else if (trafficEdge.right == trafficNode) {
                if (trafficEdge.redLightTimer == 0)
                    disabledTrafficCost += trafficEdge.redLightTimer * trafficEdge.timeWage + trafficEdge.trafficWage * trafficEdge.carAmountRightToLeft;
                else
                    activeTrafficCost += trafficEdge.trafficWage * trafficEdge.carAmountRightToLeft;
            }
        }
        if(disabledTrafficCost > activeTrafficCost){
            return true; //zmien stan
        }
        else
            return false; //nie zmieniaj
    }
}
