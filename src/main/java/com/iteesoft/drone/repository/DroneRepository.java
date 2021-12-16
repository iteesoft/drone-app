package com.iteesoft.drone.repository;

import com.iteesoft.drone.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Integer> {

    Optional<Drone> findBySerialNumber(String serialNumber);

//    @Query(value = "select * from drone where state = 'IDLE'", nativeQuery = true)
    @Query("from Drone d where d.state='IDLE'")
    List<Drone> findAvailableDrones();
}