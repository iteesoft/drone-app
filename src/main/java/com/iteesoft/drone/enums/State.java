package com.iteesoft.drone.enums;

public enum State {
    IDLE("IDLE"),
    LOADING("LOADING"),
    LOADED("LOADED"),
    DELIVERING("DELIVERING"),
    DELIVERED("DELIVERED"),
    RETURNING("RETURNING");

    public final String label;

    State(String label) {
        this.label = label;
    }
}
