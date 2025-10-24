package com.udasecurity.security;

import java.util.UUID;

public class Sensor {
    private String id;
    private String name;
    private String sensorType;
    private boolean active;

    public Sensor(String name, String sensorType) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.sensorType = sensorType;
        this.active = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSensorType() {
        return sensorType;
    }

    public boolean getActive() {
        return active;
    }

    // Added for compatibility with "Sensor::isActive"
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return name + " (" + sensorType + "): " + (active ? "Active" : "Inactive");
    }
}
