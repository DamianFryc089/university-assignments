# Car Wash On Threads
A multithreaded application simulating a self-service car wash with thread synchronization and resource sharing.

---

## Overview

This project demonstrates the simulation of a self-service car wash system using Java's multithreading capabilities. The system includes:

- **Entrance Gates**: Two gates, each with its own queue, where vehicles wait for a free wash bay.
- **Queue Management**: Vehicles choose the shortest queue to minimize wait time.
- **Wash Bays**: Multiple bays with shared resources (water and soap nozzles).
- **Washing Phases**: Vehicles go through three phases of washing:
  - Water nozzle
  - Soap nozzle
  - Water nozzle
- **Controller**: Alternates between carQueues to allow vehicles into free bays.
- **Dynamic Behavior**: Vehicles repeatedly enter, wash, and leave the car wash.
- **Visualization**: Real-time display of carQueues, vehicle states, and wash bay activity.

---

## Features

- **Thread Synchronization**: Manages access to shared resources (nozzles) using mutual exclusion mechanisms.
- **Configurable Parameters**: Flexible setup for the number of wash bays, vehicles, and simulation behavior.
- **Visualization**: A simple GUI panel showing the states of carQueues, vehicles, and wash bays.
- **Concurrency Control**: Uses Java concurrency primitives (`synchronized`, `ReentrantLock`, etc.).

