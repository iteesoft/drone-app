package com.iteesoft.drone.model;

import com.iteesoft.drone.enums.Model;
import com.iteesoft.drone.enums.State;

public class Drone {
    private String serialNumber;
    private Model model;
    private Integer weightLimit;
    private int batteryCapacity;
    private State state;
}
