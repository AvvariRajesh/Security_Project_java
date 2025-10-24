package com.udasecurity.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.udasecurity.imageservice.ImageService;

public class SecurityServiceTest {

    private SecurityService securityService;
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        // Mock ImageService
        imageService = mock(ImageService.class);

        // Initialize SecurityService with mocked ImageService
        securityService = new SecurityService(imageService);

        // Add some sensors
        securityService.addSensor("sensor1");
        securityService.addSensor("sensor2");
    }

    @Test
    void testAlarmActivatesWhenCatDetectedAndArmedHome() {
        // Set system to ARMED_HOME
        securityService.setArmingStatus(ArmingStatus.ARMED_HOME);

        // Mock image detection
        when(imageService.imageContainsCat(any())).thenReturn(true);

        securityService.processCameraImage(new Object());

        assertEquals(AlarmStatus.ALARM, securityService.getAlarmStatus());
    }

    @Test
    void testAlarmNoAlarmWhenNoCatAndSensorsInactive() {
        securityService.setArmingStatus(ArmingStatus.ARMED_HOME);

        when(imageService.imageContainsCat(any())).thenReturn(false);

        securityService.processCameraImage(new Object());

        assertEquals(AlarmStatus.NO_ALARM, securityService.getAlarmStatus());
    }

    @Test
    void testPendingAlarmWhenSensorActivated() {
        securityService.setArmingStatus(ArmingStatus.ARMED_HOME);

        securityService.activateSensor("sensor1");

        assertEquals(AlarmStatus.PENDING_ALARM, securityService.getAlarmStatus());
    }

    @Test
    void testAlarmFromPendingWhenAnotherSensorActivated() {
        securityService.setArmingStatus(ArmingStatus.ARMED_HOME);

        // First sensor triggers pending alarm
        securityService.activateSensor("sensor1");
        assertEquals(AlarmStatus.PENDING_ALARM, securityService.getAlarmStatus());

        // Second sensor triggers full alarm
        securityService.activateSensor("sensor2");
        assertEquals(AlarmStatus.ALARM, securityService.getAlarmStatus());
    }

    @Test
    void testDisarmResetsSensorsAndAlarm() {
        securityService.setArmingStatus(ArmingStatus.ARMED_HOME);
        securityService.activateSensor("sensor1");

        // Disarm system
        securityService.setArmingStatus(ArmingStatus.DISARMED);

        assertEquals(AlarmStatus.NO_ALARM, securityService.getAlarmStatus());
        assertTrue(securityService.getAlarmStatus() == AlarmStatus.NO_ALARM);
    }

    @Test
    void testDeactivateSensorResetsPendingAlarm() {
        securityService.setArmingStatus(ArmingStatus.ARMED_HOME);

        // Activate and then deactivate sensor
        securityService.activateSensor("sensor1");
        securityService.deactivateSensor("sensor1");

        assertEquals(AlarmStatus.NO_ALARM, securityService.getAlarmStatus());
    }
}
