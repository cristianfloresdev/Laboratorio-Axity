package com.dinopark.event;

import com.dinopark.enums.VehicleStatus;
import com.dinopark.model.Vehicle;
import com.dinopark.model.records.EventRecord;
import com.dinopark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class VehicleFailureEvent implements SimulationEvent
{
    private final double probability;

    public VehicleFailureEvent(double probability)
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
        return "FALLA_VEHICULO";
    }

    @Override
    public String getDescription()
    {
        return "Un vehículo ha sufrido una falla mecánica";
    }

    @Override
    public void execute(ParkState state, Random rng)
    {
        // respetar probabilidad del evento
        if (rng.nextDouble() > probability)
        {
            return;
        }

        List<Vehicle> available = state.getVehicles().stream()
                .filter(v -> v.getStatus() == VehicleStatus.AVAILABLE)
                .toList();

        if (available.isEmpty())
        {
            return;
        }

        Vehicle target = available.get(rng.nextInt(available.size()));
        target.markBroken();

        state.getDb().appendEvent(toRecord(state.getCurrentStep()));
    }

    @Override
    public EventRecord toRecord(long step)
    {
        return new EventRecord(
                step,
                getName(),
                getDescription(),
                "Vehicle",
                LocalDateTime.now()
        );
    }
}