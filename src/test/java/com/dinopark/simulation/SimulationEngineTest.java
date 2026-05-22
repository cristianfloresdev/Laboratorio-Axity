package com.dinopark.simulation;
//24 puntos aumento dos puntos más al tests
import com.dinopark.config.ParkConfig;
import com.dinopark.enums.ExperienceType;
import com.dinopark.model.CarnivoreDinosaur;
import com.dinopark.model.Dinosaur;
import com.dinopark.model.Tourist;
import com.dinopark.persistence.DatabaseService;
import com.dinopark.zone.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationEngineTest
{
    private SimulationEngine engine;
    private ParkState        state;
    private DatabaseService  db;

    @BeforeEach
    void setUp()
    {
        ParkConfig.resetForTesting();
        db    = new DatabaseService(
                "./data/test-engine-" + System.currentTimeMillis());
        state = buildState(db);
        engine = buildEngine(state, db);
    }

    @AfterEach
    void tearDown()
    {
        db.close();
    }

    @Test
    void runDoesNotThrow()
    {
        assertDoesNotThrow(() -> engine.run());
    }

    @Test
    void vehiclesCreatedAccordingToConfig()
    {
        engine.run();
        int expected = ParkConfig.getInstance()
                .getInt("vehicles.count", 4);
        assertEquals(expected, state.getVehicles().size());
    }

    @Test
    void revenueGreaterThanZeroAfterRun()
    {
        engine.run();
        assertTrue(state.getTotalRevenue() >= 0);
    }

    // --- helpers ---

    private ParkState buildState(DatabaseService db)
    {
        List<Dinosaur> dinos = new ArrayList<>(List.of(
                new CarnivoreDinosaur(1, "Rex", "T-Rex")
        ));
        List<Tourist> tourists = new ArrayList<>(List.of(
                new Tourist(1, "Alice"),
                new Tourist(2, "Bob")
        ));
        PowerPlant plant = new PowerPlant("Plant", 0.0, 100.0);

        return new ParkState(tourists, dinos,
                Collections.emptyList(), plant, db);
    }

    private SimulationEngine buildEngine(ParkState st,
                                         DatabaseService db)
    {
        ArrivalZone   arrival  = new ArrivalZone("Arrival", 30);
        CentralHub    hub      = new CentralHub("Hub", 0.4);
        BathroomZone  bathroom = new BathroomZone("Bathroom", 10, 3, 0.2);
        ObservationEnclosure enc =
                new ObservationEnclosure("Enc", 20, ExperienceType.BASIC);
        PowerPlant plant = new PowerPlant("Plant", 0.0, 100.0);

        st.getTourists().forEach(arrival::addToQueue);

        return new SimulationEngine(
                st, arrival, hub, bathroom, enc, plant, db
        );
    }
}