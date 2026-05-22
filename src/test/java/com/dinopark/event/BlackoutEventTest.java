package com.dinopark.event;
// 2 puntos sumo al test
import com.dinopark.simulation.ParkState;
import com.dinopark.zone.PowerPlant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BlackoutEventTest
{
    private BlackoutEvent event;
    private ParkState state;
    private PowerPlant plant;

    @BeforeEach
    void setUp()
    {
        event = new BlackoutEvent(1.0);
        plant = new PowerPlant("PowerPlant", 0.0, 100.0);
        state = Mockito.mock(ParkState.class);
        Mockito.when(state.getPowerPlant()).thenReturn(plant);
    }

    @Test
    void getNameReturnsCorrectValue()
    {
        assertEquals("APAGON_MASIVO", event.getName());
    }

    @Test
    void afterExecutePlantIsNotWorking()
    {
        event.execute(state, new Random(42));
        assertFalse(plant.isWorking());
    }

    @Test
    void getProbabilityReturnsCorrectValue()
    {
        assertEquals(1.0, event.getProbability());
    }

    @Test
    void toRecordReturnsCorrectName()
    {
        assertEquals("APAGON_MASIVO", event.toRecord(1L).eventName());
    }
}
