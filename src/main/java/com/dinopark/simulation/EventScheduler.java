/*
package com.dinopark.simulation;

import com.dinopark.event.BlackoutEvent;
import com.dinopark.event.DinosaurEscapeEvent;
import com.dinopark.event.SimulationEvent;
import com.dinopark.event.StormEvent;

import java.util.*;

public class EventScheduler
{
    private final Map<Integer, SimulationEvent> scheduledEvents = new HashMap<>();

    public EventScheduler(long seed, int totalSteps)
    {
        Random rng = new Random(seed);

        List<SimulationEvent> events = List.of(new DinosaurEscapeEvent(), new BlackoutEvent(), new StormEvent());

        for (SimulationEvent event : events)
        {
            int step = rng.nextInt(totalSteps);

            while (scheduledEvents.containsKey(step))
            {
                step = rng.nextInt(totalSteps);
            }

            scheduledEvents.put(step, event);
        }
    }

    public Optional<SimulationEvent> checkForEvent(int step)
    {
        return Optional.ofNullable(scheduledEvents.get(step));
    }
}

 */
