package com.iteesoft.drone.repository;

import com.iteesoft.drone.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Integer> {
    Optional<Drone> findBySerialNumber(String serialNumber);
}
