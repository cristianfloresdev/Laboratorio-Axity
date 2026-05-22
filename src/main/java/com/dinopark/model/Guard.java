package com.dinopark.model;

import com.dinopark.enums.DinosaurStatus;

import java.util.List;

public class Guard extends Worker
{
    public Guard(int id, String name, double dailySalary)
    {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole()
    {
        return "GUARD";
    }

    public void recaptureEscapedDinosaurs(List<Dinosaur> dinosaurs)
    {
        for (Dinosaur d : dinosaurs)
        {
            if (d.getStatus() == DinosaurStatus.ESCAPED)
            {
                d.returnToEnclosure();
                System.out.println("[Guard] " + getName()
                        + " recaptured " + d.getName());
            }
        }
    }
}
