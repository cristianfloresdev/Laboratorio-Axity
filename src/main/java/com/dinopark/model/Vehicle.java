package com.dinopark.model;

import com.dinopark.enums.VehicleStatus;

public class Vehicle
{
    private final int    id;
    private final String name;
    private VehicleStatus status;
    private final int    repairSteps;
    private int          repairCountdown;

    public Vehicle(int id, String name, int repairSteps)
    {
        this.id             = id;
        this.name           = name;
        this.repairSteps    = repairSteps;
        this.repairCountdown = 0;
        this.status         = VehicleStatus.AVAILABLE;
    }

    public void use()
    {
        if (status == VehicleStatus.AVAILABLE) {
            status = VehicleStatus.IN_USE;
        }
    }

    public void free()
    {
        if (status == VehicleStatus.IN_USE)
        {
            status = VehicleStatus.AVAILABLE;
        }
    }

    public void markBroken()
    {
        status           = VehicleStatus.BROKEN;
        repairCountdown  = repairSteps;
    }

    public void tick()
    {
        if (status == VehicleStatus.BROKEN)
        {
            repairCountdown--;
            if (repairCountdown <= 0)
            {
                status = VehicleStatus.AVAILABLE;
                System.out.println("[Vehicle] " + name
                        + " repaired automatically");
            }
        }
    }

    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public VehicleStatus getStatus()
    {
        return status;
    }
    public int getRepairCountdown()
    {
        return repairCountdown;
    }

    @Override
    public String toString()
    {
        return "Vehicle{id=" + id + ", name='" + name
                + "', status=" + status + "}";
    }
}
