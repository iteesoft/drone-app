package com.iteesoft.drone.controller;

import com.iteesoft.drone.dto.DroneDto;
import com.iteesoft.drone.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/drones")
public class DispatchController {

    @Autowired
    private DroneService droneService;

    @GetMapping("/")
    public ResponseEntity<?> viewAll() {
        return new ResponseEntity<>(droneService.viewAllDrones(), OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerDrone(@RequestBody DroneDto droneInfo) {
        return new ResponseEntity<>(droneService.register(droneInfo), HttpStatus.CREATED);
    }

    @PostMapping("/{droneId}/load/{medicId}")
    public ResponseEntity<?> loadDrone(@PathVariable int droneId, @PathVariable int medicId) {
        return new ResponseEntity<>(droneService.loadWithMedication(droneId, medicId), OK);
    }

    @GetMapping("/id/{droneId}")
    public ResponseEntity<?> viewInfo(@PathVariable int droneId) {
        return new ResponseEntity<>(droneService.getDroneById(droneId), OK);
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<?> viewBySerialNumber(@PathVariable String serialNumber) {
        return new ResponseEntity<>(droneService.getDrone(serialNumber), OK);
    }

    @GetMapping("/viewLoad/{droneId}")
    public ResponseEntity<?> viewLoadedMedication(@PathVariable int droneId) {
        return new ResponseEntity<>(droneService.viewDroneItems(droneId), OK);
    }

    @GetMapping("/viewAvailable")
    public ResponseEntity<?> viewAvailableDrone() {
        return new ResponseEntity<>(droneService.viewAvailableDrone(), OK);
    }

    @GetMapping("/viewBattery/{droneId}")
    public ResponseEntity<?> viewBattery(@PathVariable int droneId) {
        return new ResponseEntity<>(droneService.viewDroneBattery(droneId), OK);
    }

    @GetMapping("/medications")
    public ResponseEntity<?> viewAllMedications() {
        return new ResponseEntity<>(droneService.viewAllMedications(), OK);
    }
}
