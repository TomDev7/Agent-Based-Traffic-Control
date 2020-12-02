package org.traffic.steering;

import org.traffic.graph.TrafficManoeuvre;

public class TrafficLight {

    private final TrafficManoeuvre trafficManoeuvre;
    private TrafficLightState trafficLightState;

    public TrafficLight(TrafficManoeuvre trafficManoeuvre, TrafficLightState trafficLightState) {
        this.trafficManoeuvre = trafficManoeuvre;
        this.trafficLightState = trafficLightState; //TODO maybe RED as default value when light created?
    }

    public TrafficLightState getTrafficLightsState() {
        return trafficLightState;
    }

    public void setTrafficLightsState(TrafficLightState trafficLightState) {
        this.trafficLightState = trafficLightState;
    }
}
