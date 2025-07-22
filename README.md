# 🚦 Traffic Simulation with Akka and GraphStream

This Java-based project simulates traffic flow using an **actor-based model** powered by **Akka Typed** and visualized with **GraphStream**. It demonstrates concurrency, message-passing, and dynamic graph visualization to model intersections, traffic lights, and vehicle behavior. Project developed for the purposes of AASD subject at Warsaw University of Technology.

## ⚙️ How It Works
- The simulation uses **actors** to represent different components (e.g., traffic lights, roads, and control systems).
- **Messages** between actors drive state changes, such as traffic light switching and vehicle decision-making.
- The **GraphStream library** visualizes the road network, including nodes (intersections) and edges (roads).
- The simulation starts from `Main.java`, which triggers the `MainActor` and initiates the traffic system.

## ✨ Features
- Actor-based simulation with **Akka Typed**.
- Real-time visualization using **GraphStream**.
- Simulation of **traffic lights**, roads, and intersections.
- Message-based communication for scalable and concurrent simulation.
- Modular architecture for extending traffic rules or adding more features.

## 🛠️ Tech Stack
- **Language:** Java 11+
- **Concurrency & Messaging:** Akka Typed
- **Visualization:** GraphStream
- **Build Tool:** Maven

## 🚀 Running the Simulation
To run the traffic simulation, follow these steps:

1. **Ensure Java 11+ and Maven are installed**  
   Check your versions:
   ```bash
   java -version
   mvn -version
   
2. **Navigate to the project root (where pom.xml is located):**
   ```bash
   cd AASD_projekt-main

3. **Build the project:**  
   ```bash
   mvn clean install

4. **Run the simulation main class:**  
   ```bash
   mvn exec:java -Dexec.mainClass="org.traffic.Main"
