package com.dinopark.zone;

import com.dinopark.model.Tourist;
import com.dinopark.model.records.RevenueRecord;
import com.dinopark.persistence.CsvWriter;
import com.dinopark.persistence.ParkDataWriter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class BathroomZone implements ParkZone
{
    private final String name;
    private final int capacity;
    private final int useDurationSteps;
    private final double spaProbability;

    private final Map<Tourist, Integer> occupiedSlots = new HashMap<>();

    public BathroomZone(String name, int capacity, int useDurationSteps, double spaProbability)
    {
        this.name = name;
        this.capacity = capacity;
        this.useDurationSteps = useDurationSteps;
        this.spaProbability = spaProbability;
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
        return occupiedSlots.size() < capacity;
    }

    @Override
    public int getCurrentOccupancy()
    {
        return occupiedSlots.size();
    }

    @Override
    public int getMaxCapacity()
    {
        return capacity;
    }

    @Override
    public void enter(Tourist tourist)
    {
        occupiedSlots.put(tourist, useDurationSteps);
    }

    @Override
    public void exit(Tourist tourist)
    {
        occupiedSlots.remove(tourist);
    }

    @Override
    public void visit(Tourist tourist, Random random, ParkDataWriter db)
    {
        tryEnter(tourist, random, db);
    }

    public void tryEnter(Tourist tourist, Random random, ParkDataWriter db)
    {
        if (!hasCapacity()) return;

        //* evitar duplicados
        if (occupiedSlots.containsKey(tourist)) return;

        enter(tourist);

        tourist.recordVisit("Bathroom");

        if (random.nextDouble() < spaProbability)
        {
            double price = 15.0;

            tourist.spend(price);

            System.out.println("[Bathroom] Tourist bought SPA service");

            db.appendRevenue(
                    new RevenueRecord(
                            System.nanoTime(),
                            "SPA_SERVICE",
                            price,
                            tourist.getId(),
                            "Bathroom",
                            java.time.LocalDateTime.now()
                    )
            );
        }
    }
    @Override
    public void tick(Random random)
    {
        Iterator<Map.Entry<Tourist, Integer>> iterator = occupiedSlots.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<Tourist, Integer> entry = iterator.next();

            int remaining = entry.getValue() - 1;

            if (remaining <= 0)
            {
                iterator.remove();
            }
            else
            {
                entry.setValue(remaining);
            }
        }
    }
}
