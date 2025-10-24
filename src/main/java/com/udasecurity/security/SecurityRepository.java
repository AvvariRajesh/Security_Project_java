package com.udasecurity.security;

import java.util.Set;

public interface SecurityRepository {
    Set<Sensor> getSensors();
    void addSensor(Sensor sensor);
    void removeSensor(Sensor sensor);
    void updateSensor(Sensor sensor);
}
