package com.iteesoft.drone.enums;

public enum Model {
    LIGHT_WEIGHT("LIGHTWEIGHT"),
    MIDDLE_WEIGHT("MIDDLEWEIGHT"),
    CRUISER_WEIGHT("CRUISERWEIGHT"),
    HEAVY_WEIGHT("HEAVYWEIGHT");

    public final String label;

    Model(String label){
        this.label = label;
    }
}
