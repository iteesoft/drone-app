:scroll: START

### Drones Management API

### Introduction
This is a REST API that help monitors and manages drone activities.
It allows clients to communicate with their registered drones. 
Each drone is capable of carrying devices and delivering small loads, in this case the load is medications.

### Technologies Used

Springboot,
H2 Database,
Spring JPA,
OpenAPI,
Lombok,
Slf4j


A Drone can be registered with the properties below:
- Serial number.
- Model (e.g Lightweight, Middleweight, Cruiserweight, Heavyweight);
- Weight limit (500gr max);
- Battery capacity (%);
- State (e.g IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

A Medication can be registered with the properties below:
- name.
- weight.
- code.
- image (picture of the medication case).

### Functionalities

Users can:
- Register a drone.
- Load a drone with medication items (not more than the drone weight limit).
- Check loaded medication items for a given drone.
- Check available drones for loading.
- Check drone battery level for a given drone.

- There's also a periodic check on the drones' battery level which is logged into a file : "drone-logs.log"

### Build/Run Instructions

This project was built with MAVEN.
Once cloned, run on your IDE and access the endpoints via postman or open-api
http://localhost:8080/api-docs

:scroll: END
