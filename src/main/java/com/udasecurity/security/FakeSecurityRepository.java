package com.udasecurity.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple in-memory fake repository used for tests and for the app
 */
public class FakeSecurityRepository implements SecurityRepository {
    private final Set<Sensor> sensors = new HashSet<>();

    @Override
    public Set<Sensor> getSensors() {
        return Collections.unmodifiableSet(sensors);
    }

    @Override
    public void addSensor(Sensor s) {
        sensors.add(s);
    }

    @Override
    public void removeSensor(Sensor s) {
        sensors.removeIf(x -> x.getId().equals(s.getId()));
    }

    @Override
    public void updateSensor(Sensor s) {
        sensors.removeIf(x -> x.getId().equals(s.getId()));
        sensors.add(s);
    }
}
