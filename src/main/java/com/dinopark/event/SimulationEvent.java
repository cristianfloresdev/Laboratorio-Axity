package com.dinopark.event;

import com.dinopark.model.records.EventRecord;
import com.dinopark.simulation.ParkState;

import java.util.Random;

public interface SimulationEvent
{
    String getName();

    String getDescription();

    void execute(ParkState state, Random rng);

    EventRecord toRecord(long step);

    default double getProbability()
    {
        return 0.0;
    }
}
