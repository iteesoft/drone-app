package com.iteesoft.drone.dto;

import com.iteesoft.drone.enums.Model;
import com.iteesoft.drone.enums.State;
import lombok.Data;

@Data
public class DroneDto {
    private String serialNumber;
    private Model model;
    private Integer weightLimit;
    private int batteryCapacity;
}
