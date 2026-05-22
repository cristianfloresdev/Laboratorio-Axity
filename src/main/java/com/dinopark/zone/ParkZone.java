package com.dinopark.zone;

import com.dinopark.model.Tourist;
import com.dinopark.persistence.CsvWriter;
import com.dinopark.persistence.ParkDataWriter;

import java.util.Random;

public interface ParkZone
{
    String getName();

    boolean hasCapacity();

    int getCurrentOccupancy();

    int getMaxCapacity();

    void enter(Tourist tourist);

    void exit(Tourist tourist);

    //* Interacción principal (clave para eliminar if/else)
    void visit(Tourist tourist, Random random, ParkDataWriter db);

    //* Simulación del paso del tiempo
    default void tick(Random random)
    {
    }
}
