package com.dinopark.zone;
//5 puntos sumo al tests
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PowerPlantTest
{
    private PowerPlant plant;

    @BeforeEach
    void setUp()
    {
        plant = new PowerPlant("PowerPlant", 0.0, 100.0);
    }

    @Test
    void initiallyOperational()
    {
        assertTrue(plant.isWorking());
    }

    @Test
    void triggerFailureMakesNotWorking()
    {
        plant.triggerFailure();
        assertFalse(plant.isWorking());
    }

    @Test
    void repairRestoresWorking()
    {
        plant.triggerFailure();
        plant.repair();
        assertTrue(plant.isWorking());
    }

    @Test
    void energyDecreasesAfterTick()
    {
        double before = plant.getEnergy();
        plant.tick(new Random(42));
        assertTrue(plant.getEnergy() < before);
    }

    @Test
    void tickDoesNothingWhenNotWorking()
    {
        plant.triggerFailure();
        double energyBefore = plant.getEnergy();
        plant.tick(new Random(42));
        assertEquals(energyBefore, plant.getEnergy());
    }

    @Test
    void forcedFailureWithProbabilityOne()
    {
        PowerPlant alwaysFails = new PowerPlant("P", 1.0, 100.0);
        alwaysFails.tick(new Random(42));
        assertFalse(alwaysFails.isWorking());
    }
}
