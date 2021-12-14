package com.iteesoft.drone.service.impl;

import com.iteesoft.drone.dto.DroneDto;
import com.iteesoft.drone.enums.State;
import com.iteesoft.drone.exceptions.ResourceNotFoundException;
import com.iteesoft.drone.model.Drone;
import com.iteesoft.drone.model.Medication;
import com.iteesoft.drone.repository.DroneRepository;
import com.iteesoft.drone.repository.MedicationRepository;
import com.iteesoft.drone.service.DroneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    @Override
    public Drone register(DroneDto droneInfo) {
        Drone drone = Drone.builder()
                .serialNumber(droneInfo.getSerialNumber())
                .model(droneInfo.getModel())
                .batteryCapacity(droneInfo.getBatteryCapacity())
                .weightLimit(droneInfo.getWeightLimit())
                .state(State.IDLE).items(new ArrayList<>())
                .build();
        log.info("Drone with s/n -> {} registered", drone.getSerialNumber());
        return droneRepository.save(drone);
    }

    @Override
    public Drone getDroneById(int droneId) {
        var drone = droneRepository.findById(droneId).orElseThrow(()-> new ResourceNotFoundException("Drone not found"));
        log.info("Fetching Drone with id: {} and s/n: {}", droneId, drone.getSerialNumber());
        return drone;
    }

    @Override
    public Drone getDrone(String serialNumber) {
        var drone = droneRepository.findBySerialNumber(serialNumber).orElseThrow(()-> new ResourceNotFoundException("Drone not found"));
        log.info("Fetching Drone with s/n: {}", drone.getSerialNumber());
        return drone;
    }

    public void decreaseBatteryLevel(int droneId) {
        var drone = getDroneById(droneId);
        int i = drone.getBatteryCapacity() - 5;
        drone.setBatteryCapacity(i);
        droneRepository.save(drone);
        log.info("Drone s/n: {} new battery level, {}%", drone.getSerialNumber(), i);
    }

    public Medication getMedication(int medicId) {
        var medic = medicationRepository.findById(medicId).orElseThrow(()-> new ResourceNotFoundException("Medication not found"));
        log.info("Fetching Medication with id: {} and name: {}", medicId, medic.getName());
        return medic;
    }

    @Override
    public Drone loadWithMedication(int droneId, int medicationId) {
        var drone = getDroneById(droneId);
        var medication = getMedication(medicationId);
        var totalWeight = medication.getWeight() + totalLoadWeight(droneId);
        drone.setState(State.LOADING);

        if (totalWeight <= drone.getWeightLimit() && drone.getBatteryCapacity() > 25) {
            log.info("Medication with code: {} is been loaded on Drone s/n: {}", medication.getCode(), drone.getSerialNumber());
            drone.getItems().add(medication);
            drone.setState(State.LOADED);
            decreaseBatteryLevel(droneId);
            droneRepository.save(drone);
        } else {
            throw new ResourceNotFoundException("Error loading drone, weight limit exceeded");
        }
        return drone;
    }

    @Override
    public List<Medication> viewDroneItems(int droneId) {
        Drone drone = droneRepository.findById(droneId).orElseThrow(()-> new ResourceNotFoundException("Drone not found"));
        return drone.getItems();
    }

    @Override
    public List<Drone> viewAvailableDrone() {
        List<Drone> drones = droneRepository.findAll();
        List<Drone> availableDrones = new ArrayList<>();

        for(Drone d : drones) {
            if (d.getState() == State.IDLE) {
                availableDrones.add(d);
            }
        }
        return availableDrones;
    }

    @Override
    public int viewDroneBattery(int droneId) {
        Drone drone = droneRepository.findById(droneId).orElseThrow(()-> new ResourceNotFoundException("Drone not found"));
        var batteryCapacity = drone.getBatteryCapacity();
        log.info("Drone s/n: {}, Battery level: {}%", drone.getSerialNumber(), batteryCapacity);
        return batteryCapacity;
    }

    @Override
    public List<Medication> viewAllMedications() {
        log.info("Fetching all Medications ");
        return medicationRepository.findAll();
    }

    @Override
    public List<Drone> viewAllDrones() {
        log.info("Fetching all Drones ");
        return droneRepository.findAll();
    }

    @Scheduled(initialDelay = 40000, fixedRate = 500000) // cron = "0 1 1 * * ?"
    public void viewAllDroneBattery() {
        var time = LocalDateTime.now().toString();
        List<Drone> drones = droneRepository.findAll();
        for (Drone d: drones) {
            log.info("Current Time: {}, Drone s/n: {}, Battery level: {}%, Status: {}", time, d.getSerialNumber(), d.getBatteryCapacity(), d.getState());
        }
    }

    public int totalLoadWeight(int droneId) {
        List<Medication> medications = viewDroneItems(droneId);
        int totalWeight = 0;
        for (Medication m : medications) {
            totalWeight += m.getWeight();
        }
        return totalWeight;
    }
}
