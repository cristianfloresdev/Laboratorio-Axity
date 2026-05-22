package com.dinopark.model;

public class CarnivoreDinosaur extends Dinosaur
{
    public CarnivoreDinosaur(int id, String name, String species)
    {
        super(id, name, species, 500.0);
    }

    @Override
    public String getDiet()
    {
        return "CARNIVORE";
    }

    @Override
    public double getDangerLevel()
    {
        return 0.9;
    }
}
