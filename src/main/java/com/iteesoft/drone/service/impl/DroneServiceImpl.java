package com.iteesoft.drone.service.impl;

import com.iteesoft.drone.dto.DroneDto;
import com.iteesoft.drone.enums.State;
import com.iteesoft.drone.exceptions.AlreadyExistException;
import com.iteesoft.drone.exceptions.ResourceNotFoundException;
import com.iteesoft.drone.model.Constant;
import com.iteesoft.drone.model.Drone;
import com.iteesoft.drone.model.Medication;
import com.iteesoft.drone.repository.DroneRepository;
import com.iteesoft.drone.repository.MedicationRepository;
import com.iteesoft.drone.service.DroneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    @Override
    public Drone register(DroneDto droneInfo) {
        log.info("Registering Drone with s/n: {}", droneInfo.getSerialNumber());
        Optional<Drone> existingDrone = droneRepository.findBySerialNumber(droneInfo.getSerialNumber());
        Drone drone;
        if (existingDrone.isPresent()) {
            throw new AlreadyExistException("drone with serial number has already been registered");
        } else {
            drone = Drone.builder()
                    .serialNumber(droneInfo.getSerialNumber()).model(droneInfo.getModel())
                    .batteryCapacity(droneInfo.getBatteryCapacity()).weightLimit(droneInfo.getWeightLimit()).state(State.IDLE).items(new ArrayList<>()).build();
        }
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

    private void decreaseBatteryLevel(int droneId) {
        var drone = getDroneById(droneId);
        final int newBatteryLevel = drone.getBatteryCapacity() - Constant.BATTERY_DECREMENT_VALUE;
        drone.setBatteryCapacity(newBatteryLevel);
        droneRepository.save(drone);
        log.info("Drone s/n: {} new battery level, {}%", drone.getSerialNumber(), newBatteryLevel);
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
        final int totalWeight = medication.getWeight() + totalLoadWeight(droneId);
        drone.setState(State.LOADING);
        log.info("Medication with code: {} is been loaded on Drone s/n: {}", medication.getCode(), drone.getSerialNumber());


        if (totalWeight <= drone.getWeightLimit() && drone.getBatteryCapacity() > Constant.MINIMUM_LOAD_BATTERY) {
            drone.getItems().add(medication);
            drone.setState(State.LOADED);
            decreaseBatteryLevel(droneId);
            droneRepository.save(drone);
            log.info("Loading successful, Total load weight on Drone s/n: {}:, {}Kg", drone.getSerialNumber(), totalWeight);
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
        log.info("Fetching all drones in idle state... ");
        return droneRepository.findAvailableDrones();
    }

    @Override
    public String viewDroneBattery(int droneId) {
        Drone drone = droneRepository.findById(droneId).orElseThrow(()-> new ResourceNotFoundException("Drone not found"));
        var batteryCapacity = drone.getBatteryCapacity();
        log.info("Drone s/n: {}, Battery level: {}%", drone.getSerialNumber(), batteryCapacity);
        return batteryCapacity+"%";
    }

    @Override
    public List<Medication> viewAllMedications() {
        log.info("Fetching all registered Medications... ");
        return medicationRepository.findAll();
    }

    @Override
    public List<Drone> viewAllDrones() {
        log.info("Fetching all registered Drones... ");
        return droneRepository.findAll();
    }

    @Scheduled(initialDelay = 4000, fixedRate = 500000) // cron = "0 1 1 * * ?"
    public void viewAllDroneBattery() {
        log.info("Checking all drones Battery level @"+ LocalDateTime.now());
        int index = 0;
        droneRepository.findBatteryLevelOfDrones().forEach(d -> log.info("S/N: "+d.get(index)+" -- "+d.get(index+1)+"%"));
    }

//    @Scheduled(initialDelay = 40000, fixedRate = 500000) // cron = "0 1 1 * * ?"
//    public void viewAllDroneBattery() {
//        log.info("Checking drones Battery level...");
//        List<Drone> drones = droneRepository.findAll();
//        for (Drone d: drones) {
//            log.info("Drone s/n: "+d.getSerialNumber()+", Battery level: "+d.getBatteryCapacity()+"%, Status: "+ d.getState());
//        }
//    }

    @Override
    public int totalLoadWeight(int droneId) {
        return viewDroneItems(droneId).stream().map(Medication::getWeight).reduce(0, Integer::sum);
    }
}
