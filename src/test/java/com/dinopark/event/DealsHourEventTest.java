package com.dinopark.event;
//2 puntos sumo al test
import com.dinopark.model.Dinosaur;
import com.dinopark.model.Tourist;
import com.dinopark.persistence.DatabaseService;
import com.dinopark.simulation.ParkState;
import com.dinopark.zone.PowerPlant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DealsHourEventTest
{
    private DealsHourEvent event;
    private ParkState state;
    private DatabaseService db;

    @BeforeEach
    void setUp()
    {
        event = new DealsHourEvent(1.0);
        db = new DatabaseService(
                "./data/test-deals-" + System.currentTimeMillis());

        List<Tourist> tourists = new ArrayList<>();
        List<Dinosaur> dinosaurs = new ArrayList<>();
        PowerPlant plant = new PowerPlant("Plant", 0.0, 100.0);

        state = new ParkState(tourists, dinosaurs,
                Collections.emptyList(), plant, db);
    }

    @AfterEach
    void tearDown()
    {
        db.close();
    }

    @Test
    void afterExecuteDealsHourIsActive()
    {
        event.execute(state, new Random());
        assertTrue(state.isDealsHourActive());
    }

    @Test
    void afterExecuteDiscountIs30Percent()
    {
        event.execute(state, new Random());
        assertEquals(0.30, state.getCurrentDiscount(), 0.0001);
    }

    @Test
    void afterClearActiveEventsDiscountResets()
    {
        event.execute(state, new Random());
        state.clearActiveEvents();

        assertEquals(0.0, state.getCurrentDiscount(), 0.0001);
        assertFalse(state.isDealsHourActive());
    }

    @Test
    void getNameReturnsCorrectValue()
    {
        assertEquals("HORA_DE_OFERTAS", event.getName());
    }

    @Test
    void getProbabilityReturnsCorrectValue()
    {
        assertEquals(1.0, event.getProbability(), 0.0001);
    }

    @Test
    void toRecordReturnsCorrectEventName()
    {
        assertEquals("HORA_DE_OFERTAS", event.toRecord(1L).eventName());
    }
}