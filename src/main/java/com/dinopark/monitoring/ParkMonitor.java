package com.dinopark.monitoring;

import com.dinopark.simulation.ParkState;

import java.util.List;

public class ParkMonitor
{
    public static void displaySnapshot(ParkState state)
    {
        System.out.println("\n PARK MONITOR ");

        // Métrica 1 — Turistas activos
        System.out.printf("  1. Active tourists     : %d%n",
                state.countActiveTourists());

        // Métrica 2 — Dinosaurios en recinto
        System.out.printf("  2. Dinos in enclosure  : %d%n",
                state.countDinosaursInEnclosure());

        // Métrica 3 — Energía disponible
        System.out.printf("  3. Power plant energy  : %.1f / %.1f%n",
                state.getPowerPlant().getEnergy(),
                state.getPowerPlant().getMaxEnergy());

        // Métrica 4 — Eventos activos este step
        List<String> events = state.getActiveEventNames();
        System.out.printf("  4. Active events       : %s%n",
                events.isEmpty() ? "none" : String.join(", ", events));

        // Métrica 5 — Vehículos no disponibles
        System.out.printf("  5. Vehicles unavailable: %d%n",
                state.countVehiclesInUse());

    }
}