package com.iteesoft.drone.repository;

import com.iteesoft.drone.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Integer> {

    Optional<Drone> findBySerialNumber(String serialNumber);

//    @Query(value = "select * from drone where state = 'IDLE'", nativeQuery = true)
    @Query("FROM Drone d WHERE d.state='IDLE'")
    List<Drone> findAvailableDrones();

    @Query(value = "SELECT serial_Number, battery_Capacity FROM drone", nativeQuery = true)
    List<List<Object>> findBatteryLevelOfDrones();

    @Query(value = "SELECT sum(weight) FROM medication LEFT JOIN drone ON medication.id=drone.id", nativeQuery = true)
    int findTotalDroneMedicationWeight(int id);
}