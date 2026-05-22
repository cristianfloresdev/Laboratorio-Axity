package com.dinopark.model;

import com.dinopark.enums.DinosaurStatus;

public abstract class Dinosaur
{
    private final int id;
    private final String name;
    private final String species;
    private DinosaurStatus status;
    private final double feedingCostPerDay;

    public Dinosaur(int id, String name, String species, double feedingCostPerDay)
    {
        this.id = id;
        this.name = name;
        this.species = species;
        this.feedingCostPerDay = feedingCostPerDay;
        this.status = DinosaurStatus.IN_ENCLOSURE;
    }

    //funciones abstractas para definir cada subclase su comportamiento
    public abstract String getDiet();
    public abstract double getDangerLevel();

    //Funciones concretas iguales a todos
    public void escape()
    {
        status = DinosaurStatus.ESCAPED;
    }

    public void recapture()
    {
        status = DinosaurStatus.RECAPTURED;
    }

    public void returnToEnclosure()
    {
        status = DinosaurStatus.IN_ENCLOSURE;
    }

    //*Getters
    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getSpecies()
    {
        return species;
    }

    public DinosaurStatus getStatus()
    {
        return status;
    }

    public double getFeedingCostPerDay()
    {
        return feedingCostPerDay;
    }

    @Override
    public String toString()
    {
        return "Dinosaur{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", status=" + status +
                ", feedingCostPerDay=" + feedingCostPerDay +
                '}';
    }
}
