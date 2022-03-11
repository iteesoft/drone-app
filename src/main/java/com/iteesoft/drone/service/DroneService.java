package com.iteesoft.drone.service;

import com.iteesoft.drone.dto.DroneDto;
import com.iteesoft.drone.model.Drone;
import com.iteesoft.drone.model.Medication;

import java.util.List;

public interface DroneService {

    Drone getDroneById(int droneId);
    Drone register(DroneDto droneInfo);

    Drone getDrone(String serialNumber);

    Drone loadWithMedication(int droneId, int medicationId);
    List<Medication> viewDroneItems(int droneId);
    List<Drone> viewAvailableDrone();
    String viewDroneBattery(int droneId);
    List<Medication> viewAllMedications();
    List<Drone> viewAllDrones();

    int totalLoadWeight(int droneId);
}
