package com.dinopark.model;
//un punto 1 sumo al tests
import com.dinopark.enums.VehicleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VehicleTest
{
    private Vehicle vehicle;

    @BeforeEach
    void setUp()
    {
        vehicle = new Vehicle(1, "Truck-1", 5);
    }

    @Test
    void initialStatusIsAvailable()
    {
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void useChangesToInUse()
    {
        vehicle.use();
        assertEquals(VehicleStatus.IN_USE, vehicle.getStatus());
    }

    @Test
    void useDoesNothingWhenNotAvailable()
    {
        vehicle.markBroken();
        vehicle.use();
        assertEquals(VehicleStatus.BROKEN, vehicle.getStatus());
    }

    @Test
    void freeChangesToAvailable()
    {
        vehicle.use();
        vehicle.free();
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void freeDoesNothingWhenNotInUse()
    {
        vehicle.free();
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void markBrokenSetsStatusAndCountdown()
    {
        vehicle.markBroken();
        assertEquals(VehicleStatus.BROKEN, vehicle.getStatus());
        assertEquals(5, vehicle.getRepairCountdown());
    }

    @Test
    void tickDecrementsCountdown()
    {
        vehicle.markBroken();
        vehicle.tick();
        assertEquals(4, vehicle.getRepairCountdown());
    }

    @Test
    void tickRestoresAvailableWhenCountdownReachesZero()
    {
        vehicle.markBroken();
        for (int i = 0; i < 5; i++) vehicle.tick();
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void tickDoesNothingWhenAvailable()
    {
        vehicle.tick();
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }
}


