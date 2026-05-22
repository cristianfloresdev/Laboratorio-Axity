package com.dinopark.event;

import com.dinopark.model.records.EventRecord;
import com.dinopark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class DealsHourEvent implements SimulationEvent
{
    private final double probability;

    public DealsHourEvent(double probability)
    {
        this.probability = probability;
    }

    @Override
    public double getProbability()
    {
        return probability;
    }

    @Override
    public String getName()
    {
        return "HORA_DE_OFERTAS";
    }

    @Override
    public String getDescription()
    {
        return "Hora de ofertas — 30% de descuento activo";
    }

    @Override
    public void execute(ParkState state, Random rng)
    {
        state.setDealsHourActive(true);
        state.setCurrentDiscount(0.30);

        System.out.println("[EVENT] Deals Hour! 30% discount active this step");

        state.getDb().appendEvent(toRecord(state.getCurrentStep()));
    }

    @Override
    public EventRecord toRecord(long step)
    {
        return new EventRecord(
                step,
                getName(),
                getDescription(),
                "AllZones",
                LocalDateTime.now()
        );
    }
}