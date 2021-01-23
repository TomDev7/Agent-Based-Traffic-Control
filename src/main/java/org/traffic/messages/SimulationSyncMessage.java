package org.traffic.messages;

public class SimulationSyncMessage implements TrafficMessage {

    private int simulationIterationNumber;

    public SimulationSyncMessage(int simulationIterationNumber) {
        this.simulationIterationNumber = simulationIterationNumber;
    }

    public int getSimulationIterationNumber() {
        return simulationIterationNumber;
    }

    public void setSimulationIterationNumber(int simulationIterationNumber) {
        this.simulationIterationNumber = simulationIterationNumber;
    }
}
