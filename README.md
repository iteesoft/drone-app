### Drones Management API

### Introduction
This is a REST API that help monitors and manages drone activities.
It allows clients to communicate with their registered drones. 
Each drone is capable of carrying devices and delivering small loads, in this case the load is medications.

### Technologies Used
H2 Database,
Spring JPA,
OpenAPI,
Lombok


A Drone can be registered with the properties below:
- serial number;
- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
- weight limit (500gr max);
- battery capacity (%);
- state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

A Medication can be registered with the properties below:
- name;
- weight;
- code;
- image (picture of the medication case).

### Functionalities
Users can:
- register a drone;
- loading a drone with medication items;
- check loaded medication items for a given drone;
- check available drones for loading;
- check drone battery level for a given drone;


### Build/Run Instructions

This project was built with MAVEN.
Once cloned, run on your IDE and access the endpoints via postman or open-api
http://localhost:8080/api-docs

:scroll: END
