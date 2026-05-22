package com.dinopark.simulation;

import com.dinopark.model.Vehicle;
import com.dinopark.persistence.DatabaseService;
import com.dinopark.zone.PowerPlant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ParkStateTest
{
    private ParkState state;
    private DatabaseService db;

    @BeforeEach
    void setUp()
    {
        db = new DatabaseService(
                "./data/test-state-" + System.currentTimeMillis());

        state = new ParkState(
                new ArrayList<>(),
                new ArrayList<>(),
                Collections.emptyList(),
                new PowerPlant("Plant", 0.0, 100.0),
                db
        );
    }

    @AfterEach
    void tearDown()
    {
        db.close();
    }

    @Test
    void initialStepIsZero()
    {
        assertEquals(0, state.getCurrentStep());
    }

    @Test
    void incrementStepWorks()
    {
        state.incrementStep();
        assertEquals(1, state.getCurrentStep());
    }

    @Test
    void addAndGetVehicles()
    {
        state.addVehicle(new Vehicle(1, "Truck", 5));
        assertEquals(1, state.getVehicles().size());
    }

    @Test
    void countVehiclesInUse()
    {
        Vehicle v1 = new Vehicle(1, "Truck-1", 5);
        Vehicle v2 = new Vehicle(2, "Truck-2", 5);
        v1.markBroken();
        v2.use();
        state.addVehicle(v1);
        state.addVehicle(v2);
        assertEquals(2, state.countVehiclesInUse());
    }

    @Test
    void addAndClearActiveEvents()
    {
        state.addActiveEvent("BLACKOUT");
        assertEquals(1, state.getActiveEventNames().size());
        state.clearActiveEvents();
        assertEquals(0, state.getActiveEventNames().size());
    }

    @Test
    void clearActiveEventsResetsDiscount()
    {
        state.setCurrentDiscount(0.30);
        state.setDealsHourActive(true);
        state.clearActiveEvents();
        assertEquals(0.0, state.getCurrentDiscount());
        assertFalse(state.isDealsHourActive());
    }

    @Test
    void addRevenueAccumulates()
    {
        state.addRevenue(100.0);
        state.addRevenue(50.0);
        assertEquals(150.0, state.getTotalRevenue());
    }

    @Test
    void addExpenseAccumulates()
    {
        state.addExpense(30.0);
        assertEquals(30.0, state.getTotalExpenses());
    }
}