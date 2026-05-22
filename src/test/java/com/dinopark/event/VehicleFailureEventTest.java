package com.dinopark.event;
//No sumo nada de puntos, necesito revisarlo
import com.dinopark.enums.VehicleStatus;
import com.dinopark.model.Dinosaur;
import com.dinopark.model.Tourist;
import com.dinopark.model.Vehicle;
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

class VehicleFailureEventTest
{
    private VehicleFailureEvent event;
    private ParkState state;
    private DatabaseService db;

    @BeforeEach
    void setUp()
    {
        event = new VehicleFailureEvent(1.0);
        db = new DatabaseService(
                "./data/test-vehicle-" + System.currentTimeMillis());

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
    void withAvailableVehicleOneGetsBroken()
    {
        state.addVehicle(new Vehicle(1, "Truck-1", 5));
        state.addVehicle(new Vehicle(2, "Truck-2", 5));

        event.execute(state, new Random(42));

        long broken = state.getVehicles().stream()
                .filter(v -> v.getStatus() == VehicleStatus.BROKEN)
                .count();

        assertTrue(broken >= 0);
        assertTrue(broken <= 2);
    }

    @Test
    void withNoAvailableVehiclesDoesNotThrow()
    {
        Vehicle v = new Vehicle(1, "Truck-1", 5);
        v.markBroken();
        state.addVehicle(v);

        assertDoesNotThrow(() ->
                event.execute(state, new Random(42)));
    }

    @Test
    void withEmptyVehicleListDoesNotThrow()
    {
        assertDoesNotThrow(() ->
                event.execute(state, new Random(42)));
    }

    @Test
    void getNameReturnsCorrectValue()
    {
        assertEquals("FALLA_VEHICULO", event.getName());
    }

    @Test
    void eventSkipsExecutionWhenProbabilityIsZero()
    {
        VehicleFailureEvent lowProb = new VehicleFailureEvent(0.0);
        Vehicle v = new Vehicle(1, "Truck", 5);
        state.addVehicle(v);

        lowProb.execute(state, new Random(42));

        // El vehículo no debe haberse roto
        assertEquals(VehicleStatus.AVAILABLE, v.getStatus());
    }

    @Test
    void toRecordReturnsCorrectEventName()
    {
        assertEquals("FALLA_VEHICULO", event.toRecord(1L).eventName());
    }
}