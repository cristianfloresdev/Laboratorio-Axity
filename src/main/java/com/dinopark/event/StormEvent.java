package com.dinopark.event;

import com.dinopark.model.Tourist;
import com.dinopark.model.records.EventRecord;
import com.dinopark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class StormEvent implements SimulationEvent
{
    private final double probability;

    public StormEvent(double probability)
    {
        this.probability = probability;
    }

    @Override
    public double getProbability() { return probability; }

    @Override
    public String getName() { return "TORMENTA_TORRENCIAL"; }

    @Override
    public String getDescription() { return "Tormenta obliga evacuación"; }

    @Override
    public void execute(ParkState state, Random rng)
    {
        for (Tourist t : state.getActiveTourists())
        {
            t.recordVisit("Evacuación");
        }

        System.out.println("[EVENT] Storm evacuation! Cost: 500");

        // Persistir en BD
        state.getDb().appendEvent(toRecord(state.getCurrentStep()));
    }

    @Override
    public EventRecord toRecord(long step)
    {
        return new EventRecord(
                step,
                getName(),
                getDescription(),
                "All tourists",
                LocalDateTime.now()
        );
    }
}