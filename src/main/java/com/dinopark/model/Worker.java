package com.dinopark.model;

public abstract class Worker
{
    private final int id;
    private final String name;
    private final double dailySalary;

    protected Worker(int id, String name, double dailySalary)
    {
        this.id = id;
        this.name = name;
        this.dailySalary = dailySalary;
    }

    public abstract String getRole();

    //*Getters
    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public double getDailySalary()
    {
        return dailySalary;
    }

    @Override
    public String toString()
    {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dailySalary=" + dailySalary +
                '}';
    }
}
