package com.udasecurity.security;

import com.udasecurity.imageservice.ImageService;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class SecurityService {

    private final Set<Sensor> sensors = new HashSet<>();
    private ArmingStatus armingStatus = ArmingStatus.DISARMED;
    private AlarmStatus alarmStatus = AlarmStatus.NO_ALARM;
    private final ImageService imageService;

    private boolean catDetected = false;

    public SecurityService(ImageService imageService) {
        this.imageService = imageService;
    }

    // --- Sensor Management ---
    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    // --- Alarm Control ---
    public void setArmingStatus(ArmingStatus status) {
        this.armingStatus = status;
        System.out.println("System Arming Status: " + status);

        if (status == ArmingStatus.DISARMED) {
            setAlarmStatus(AlarmStatus.NO_ALARM);
        } else if (status == ArmingStatus.ARMED_HOME && catDetected) {
            setAlarmStatus(AlarmStatus.ALARM);
        }
    }

    public ArmingStatus getArmingStatus() {
        return armingStatus;
    }

    public void setAlarmStatus(AlarmStatus status) {
        this.alarmStatus = status;
        System.out.println("Alarm Status: " + status);
    }

    public AlarmStatus getAlarmStatus() {
        return alarmStatus;
    }

    // --- Image Processing ---
    public void processImage(BufferedImage image) {
        boolean catFound = imageService.imageContainsCat(image, 50.0f);
        setCatDetected(catFound);

        if (catFound && armingStatus == ArmingStatus.ARMED_HOME) {
            setAlarmStatus(AlarmStatus.ALARM);
        } else if (!catFound && sensors.stream().noneMatch(Sensor::isActive)) {
            setAlarmStatus(AlarmStatus.NO_ALARM);
        }
    }

    // --- Cat Detection ---
    public void setCatDetected(boolean detected) {
        this.catDetected = detected;
        System.out.println("Cat detected: " + detected);
    }

    public boolean isCatDetected() {
        return catDetected;
    }
}
