package com.dinopark.event;

import com.dinopark.enums.TouristStatus;
import com.dinopark.model.Dinosaur;
import com.dinopark.model.Tourist;
import com.dinopark.model.records.EventRecord;
import com.dinopark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class DinosaurEscapeEvent implements SimulationEvent
{
    private final double probability;

    public DinosaurEscapeEvent(double probability)
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
        return "ESCAPE_DINOSAURIO";
    }

    @Override
    public String getDescription()
    {
        return "Un dinosaurio ha escapado";
    }

    @Override
    public void execute(ParkState state, Random rng)
    {
        List<Dinosaur> dinos = state.getEnclosedDinosaurs();
        if (dinos.isEmpty()) return;

        Dinosaur dino = dinos.get(rng.nextInt(dinos.size()));
        dino.escape();

        if (rng.nextDouble() < dino.getDangerLevel())
        {
            List<Tourist> tourists = state.getActiveTourists();
            if (!tourists.isEmpty())
            {
                Tourist t = tourists.get(rng.nextInt(tourists.size()));
                t.setStatus(TouristStatus.ATTACKED);

                System.out.println("[EVENT] Tourist attacked!");
            }
        }

        System.out.println("[EVENT] Dinosaur escaped!");
    }

    @Override
    public EventRecord toRecord(long step)
    {
        return new EventRecord(
                step,
                getName(),
                getDescription(),
                "Dinosaur",
                LocalDateTime.now()
        );
    }
}
