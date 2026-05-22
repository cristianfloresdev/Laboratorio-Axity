package com.dinopark.model;

/*
public class Technician extends Worker
{
    public Technician(int id, String name, double dailySalary)
    {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole()
    {
        return "TECHNICIAN";
    }

    public void repairIfNeeded(PowerPlant plant, List<Vehicle> vehicles)
    {
        if (!plant.isOperational())
        {
            Optional<Vehicle> available = vehicles.stream()
                    .filter(v -> v.getStatus() == VehicleStatus.AVAILABLE)
                    .findFirst();
            if (available.isPresent())
            {
                available.get().use();
                plant.repair();
                available.get().free();
                System.out.println("[Technician] " + getName()
                        + " repaired the power plant");
            }
            else
            {
                System.out.println("[Technician] " + getName()
                        + " could not repair — no vehicle available");
            }
        }
    }


}
 */

