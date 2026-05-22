package com.dinopark.event;

import com.dinopark.model.records.EventRecord;
import com.dinopark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class BlackoutEvent implements SimulationEvent
{
    private final double probability;

    public BlackoutEvent(double probability)
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
        return "APAGON_MASIVO";
    }

    @Override
    public String getDescription()
    {
        return "Falla total de energía";
    }

    @Override
    public void execute(ParkState state, Random rng)
    {
        state.getPowerPlant().triggerFailure();

        System.out.println("[EVENT] Blackout occurred! Cost: 2000");
    }

    @Override
    public EventRecord toRecord(long step)
    {
        return new EventRecord(step, getName(), getDescription(), "PowerPlant", LocalDateTime.now());
    }
}
