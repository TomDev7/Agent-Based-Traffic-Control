package org.traffic.utopia;

import com.typesafe.config.ConfigException;
import org.traffic.actors.TrafficActor;
import org.traffic.actors.MainActor;
import org.traffic.graph.TrafficEdge;
import org.traffic.graph.TrafficManoeuvre;
import org.traffic.graph.TrafficNode;
import org.traffic.steering.TrafficLight;
import org.traffic.steering.TrafficLightState;

import java.util.Random;

public class DecisionAlgorithm {
    public static boolean changeState(TrafficNode trafficNode){
        if(trafficNode == null || trafficNode.availableManoeuvres.size() == 0)
        {
            return false;
        }
        double disabledTrafficCost = 0;
        double activeTrafficCost =0;
        int i=0;
        for(TrafficManoeuvre aw : trafficNode.availableManoeuvres)
        {
            if (trafficNode.trafficLights.get(i).getTrafficLightsState() == TrafficLightState.GREEN)
            {
                aw.waitingTime = 0;
                activeTrafficCost = aw.wage1 * aw.awaitingCarsNumber;
            }
            else
            {
                aw.incrementTime();
                disabledTrafficCost = aw.wage2 * aw.awaitingCarsNumber + aw.wage3 *aw.waitingTime;
            }
                i+=1;
        }

        if(disabledTrafficCost > activeTrafficCost){
            return true; //zmien stan
        }
        else
            return false; //nie zmieniaj

    }
}
