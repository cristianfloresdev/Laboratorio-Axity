package com.dinopark.zone;

import com.dinopark.model.Tourist;
import com.dinopark.persistence.CsvWriter;
import com.dinopark.persistence.ParkDataWriter;

import java.util.Random;

public class PowerPlant implements ParkZone
{
    private final String name;
    private final double failureProbability;
    private double energy;
    private final double maxEnergy;

    private boolean working = true;

    public PowerPlant(String name, double failureProbability, double initialEnergy)
    {
        this.name = name;
        this.failureProbability = failureProbability;
        this.energy = initialEnergy;
        this.maxEnergy = initialEnergy;
    }

    //*Getters
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean hasCapacity()
    {
        return true;
    }

    @Override
    public int getCurrentOccupancy()
    {
        return 0;
    }

    @Override
    public int getMaxCapacity()
    {
        return 0;
    }

    public double getMaxEnergy()
    {
        return maxEnergy;
    }

    @Override
    public void enter(Tourist tourist)
    {

    }

    @Override
    public void exit(Tourist tourist)
    {

    }

    @Override
    public void visit(Tourist tourist, Random random, ParkDataWriter db)
    {
    }

    @Override
    public void tick(Random random)
    {
        if (!working) return;

        energy -= 5;

        if (random.nextDouble() < failureProbability)
        {
            working = false;
            System.out.println("[PowerPlant] FAILURE!");
        }
    }

    public void triggerFailure()
    {
        this.working = false;
        System.out.println("[PowerPlant] forced failure (event)");
    }
    public void repair()
    {
        working = true;
        System.out.println("[PowerPlant] Repaired");
    }

    public boolean isWorking()
    {
        return working;
    }

    public double getEnergy()
    {
        return energy;
    }
}
