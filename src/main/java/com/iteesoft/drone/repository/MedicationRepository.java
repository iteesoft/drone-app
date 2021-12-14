package com.iteesoft.drone.repository;

import com.iteesoft.drone.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Integer> {
}
