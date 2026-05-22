package com.dinopark.model;

public class HerbivoreDinosaur extends Dinosaur
{
    public HerbivoreDinosaur(int id, String name, String species)
    {
        super(id, name, species, 200.0);
    }

    @Override
    public String getDiet()
    {
        return "HERBIVORE";
    }

    @Override
    public double getDangerLevel()
    {
        return 0.2;
    }
}
