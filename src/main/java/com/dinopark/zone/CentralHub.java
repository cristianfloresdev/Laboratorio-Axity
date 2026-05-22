package com.dinopark.zone;

import com.dinopark.model.Tourist;
import com.dinopark.model.records.RevenueRecord;
import com.dinopark.persistence.ParkDataWriter;

import java.util.Random;

public class CentralHub implements ParkZone
{
    private final String name;
    private final double souvenirProbability;

    public CentralHub(String name, double souvenirProbability)
    {
        this.name = name;
        this.souvenirProbability = souvenirProbability;
    }

    @Override
    public String getName()
    { return name; }

    @Override
    public boolean hasCapacity()
    { return true; }

    @Override
    public int getCurrentOccupancy()
    { return 0; }

    @Override
    public int getMaxCapacity()
    { return Integer.MAX_VALUE; }

    @Override
    public void enter(Tourist tourist)
    {}

    @Override
    public void exit(Tourist tourist)
    {}

    @Override
    public void visit(Tourist tourist, Random random, ParkDataWriter db)
    {
        visit(tourist, random, db, 0.0);
    }

    //* Versión con descuento — usada por SimulationEngine
    public void visit(Tourist tourist, Random random,
                      ParkDataWriter db, double discount)
    {
        tourist.recordVisit("CentralHub");

        if (random.nextDouble() < souvenirProbability)
        {
            double basePrice = 20.0;
            double price     = basePrice * (1.0 - discount);

            tourist.spend(price);

            System.out.println("[CentralHub] Tourist bought souvenir $"
                    + price + (discount > 0 ? " (discount applied)" : ""));

            db.appendRevenue(
                    new RevenueRecord(
                            System.nanoTime(),
                            "SOUVENIR",
                            price,
                            tourist.getId(),
                            "CentralHub",
                            java.time.LocalDateTime.now()
                    )
            );
        }
    }
}