package com.dinopark.zone;

import com.dinopark.enums.ExperienceType;
import com.dinopark.model.Tourist;
import com.dinopark.model.records.RevenueRecord;
import com.dinopark.persistence.CsvWriter;
import com.dinopark.persistence.ParkDataWriter;

import java.util.Random;

public class ObservationEnclosure implements ParkZone
{
    private final String name;
    private final int capacity;
    private final ExperienceType type;
    private int currentVisitors = 0;

    public ObservationEnclosure(String name, int capacity, ExperienceType type)
    {
        this.name = name;
        this.capacity = capacity;
        this.type = type;
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
        return currentVisitors < capacity;
    }

    @Override
    public int getCurrentOccupancy()
    {
        return currentVisitors;
    }

    @Override
    public int getMaxCapacity()
    {
        return capacity;
    }

    @Override
    public void enter(Tourist tourist)
    {
        currentVisitors++;
    }

    @Override
    public void exit(Tourist tourist)
    {
        currentVisitors--;
    }

    public void visit(Tourist tourist, Random random, ParkDataWriter db)
    {
        if (!hasCapacity()) return;

        enter(tourist);

        double price = type.getPrice();
        int score = type.generateScore(random);

        //* registrar visita
        tourist.recordVisit("Enclosure-" + type);

        //* registrar gasto del turista
        tourist.spend(price);

        System.out.println("[Enclosure-" + type + "] Paid: " + price + " Score: " + score);

        //* guardar en CSV
        db.appendRevenue(
                new RevenueRecord(
                        System.nanoTime(),
                        "ENCLOSURE_" + type,
                        price,
                        tourist.getId(),
                        "Enclosure-" + type,
                        java.time.LocalDateTime.now()
                )
        );

        exit(tourist);
    }
}
