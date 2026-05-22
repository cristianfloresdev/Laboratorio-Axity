package com.dinopark.zone;

import com.dinopark.enums.TouristStatus;
import com.dinopark.model.Tourist;
import com.dinopark.model.records.RevenueRecord;
import com.dinopark.persistence.CsvWriter;
import com.dinopark.persistence.ParkDataWriter;

import java.util.*;

public class ArrivalZone implements ParkZone
{
    private final String name;
    private final int capacity;
    private final Queue<Tourist> queue = new LinkedList<>();

    public ArrivalZone(String name, int capacity)
    {
        this.name = name;
        this.capacity = capacity;
    }

    public void addToQueue(Tourist tourist)
    {
        queue.offer(tourist);
    }

    public List<Tourist> processBatch(int batchSize, ParkDataWriter db)
    {
        List<Tourist> admitted = new ArrayList<>();

        for (int i = 0; i < batchSize && !queue.isEmpty(); i++)
        {
            Tourist t = queue.poll();

            //* cambiar estado
            t.setStatus(TouristStatus.IN_PARK);

            admitted.add(t);

            double ticketPrice = 50.0;

            t.spend(ticketPrice);

            System.out.println("[Arrival] Tourist entered park");

            //* guardar ingreso
           db.appendRevenue(
                    new RevenueRecord(
                            System.nanoTime(),
                            "TICKET",
                            ticketPrice,
                            t.getId(),
                            "ArrivalZone",
                            java.time.LocalDateTime.now()
                    )
            );
        }
        return admitted;
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
        return true;
    }

    @Override
    public int getCurrentOccupancy()
    {
        return queue.size();
    }

    @Override
    public int getMaxCapacity()
    {
        return capacity;
    }

    @Override
    public void enter(Tourist tourist)
    {
        addToQueue(tourist);
    }

    @Override
    public void exit(Tourist tourist)
    {

    }

    @Override
    public void visit(Tourist tourist, Random random, ParkDataWriter db)
    {
    }
}
