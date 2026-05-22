package com.dinopark.simulation;

import com.dinopark.config.ParkConfig;
import com.dinopark.event.*;
import com.dinopark.model.Tourist;
import com.dinopark.model.Vehicle;
import com.dinopark.monitoring.ParkMonitor;
import com.dinopark.persistence.DatabaseService;
import com.dinopark.persistence.ParkDataWriter;
import com.dinopark.zone.*;

import java.util.List;
import java.util.Random;

public class SimulationEngine
{
    private final ParkState          state;
    private final Random             rng;
    private final List<SimulationEvent> allEvents;

    private final int totalSteps;
    private final int batchSize;
    private final int monitoringInterval;

    private final ParkDataWriter db;
    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final ObservationEnclosure enclosure;
    private final PowerPlant powerPlant;
    //private final DatabaseService db;

    public SimulationEngine(ParkState state,
                            ArrivalZone arrivalZone,
                            CentralHub centralHub,
                            BathroomZone bathroomZone,
                            ObservationEnclosure enclosure,
                            PowerPlant powerPlant,
                            ParkDataWriter db)
    {
        this.state = state;
        this.rng = new Random();
        this.db = db;

        ParkConfig config = ParkConfig.getInstance();

        this.totalSteps = config.getInt("simulation.totalSteps", 100);
        this.batchSize = config.getInt("simulation.arrivalBatchSize", 5);
        this.monitoringInterval = config.getInt("monitoring.intervalSteps", 10);

        this.arrivalZone = arrivalZone;
        this.centralHub = centralHub;
        this.bathroomZone = bathroomZone;
        this.enclosure = enclosure;
        this.powerPlant = powerPlant;

        // Crear vehículos desde properties
        int vehicleCount = config.getInt("vehicles.count", 4);
        int repairSteps  = config.getInt("vehicles.repairSteps", 5);
        for (int i = 0; i < vehicleCount; i++)
        {
            state.addVehicle(new Vehicle(i + 1, "Vehicle-" + (i + 1), repairSteps));
        }

        //* Creamos los 5 eventos con sus probabilidades
        this.allEvents = List.of(
                new DinosaurEscapeEvent(
                        config.getDouble("event.escape.probability", 0.05)),
                new BlackoutEvent(
                        config.getDouble("event.blackout.probability", 0.03)),
                new StormEvent(
                        config.getDouble("event.storm.probability", 0.04)),
                new DealsHourEvent(
                        config.getDouble("event.deals.probability", 0.08)),
                new VehicleFailureEvent(
                        config.getDouble("event.vehicleFailure.probability", 0.06))
        );
    }

    public void run()
    {
        for (int step = 0; step < totalSteps; step++)
        {
            state.incrementStep();

            System.out.println("\n=== STEP " + step + " ===");

            //* Limpiar eventos del step anterior
            state.clearActiveEvents();

            //* LLEGADAS
            List<Tourist> arrived = arrivalZone.processBatch(
                    batchSize, db);

            //* MOVIMIENTO
            List<Tourist> active = state.getActiveTourists();
            for (Tourist t : active)
            {
                double discount = state.getCurrentDiscount();
                centralHub.visit(t, rng, db, discount);
                bathroomZone.tryEnter(t, rng, db);
                enclosure.visit(t, rng, db);
            }

            //* TICKS
            bathroomZone.tick(rng);
            powerPlant.tick(rng);
            state.getVehicles().forEach(Vehicle::tick);

            //* EVENTOS ALEATORIOS
            checkAndFireEvents();

            //* MONITOREO
            if (state.getCurrentStep() % monitoringInterval == 0)
            {
                ParkMonitor.displaySnapshot(state);
            }
        }

        System.out.println("\n[Engine] Simulación finalizada.");
    }

    private void checkAndFireEvents()
    {
        for (SimulationEvent event : allEvents)
        {
            if (rng.nextDouble() < event.getProbability())
            {
                event.execute(state, rng);
                state.addActiveEvent(event.getName());
            }
        }
    }
}