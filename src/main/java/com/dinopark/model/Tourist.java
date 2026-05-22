package com.dinopark.model;

import com.dinopark.enums.TouristStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tourist
{
    private final int id;
    private final String name;
    private TouristStatus status;
    private double moneySpent;
    private final List<String> visitedZones;

    public Tourist(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.status = TouristStatus.WAITING;
        this.moneySpent = 0.0;
        this.visitedZones = new ArrayList<>();
    }

    public void spend(double amount)
    {
        if(amount > 0)
        {
            moneySpent += amount;
        }
    }

    public void recordVisit(String zoneName)
    {
        visitedZones.add(zoneName);
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

    public TouristStatus getStatus()
    {
        return status;
    }

    public double getMoneySpent()
    {
        return moneySpent;
    }

    public List<String> getVisitedZones()
    {
        return Collections.unmodifiableList(visitedZones);
    }

    public void setStatus(TouristStatus status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "Tourist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", moneySpent=" + moneySpent +
                ", visitedZones=" + visitedZones +
                '}';
    }
}
