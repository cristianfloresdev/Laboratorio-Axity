package com.dinopark.simulation;

import com.dinopark.enums.DinosaurStatus;
import com.dinopark.enums.TouristStatus;
import com.dinopark.enums.VehicleStatus;
import com.dinopark.model.Dinosaur;
import com.dinopark.model.Tourist;
import com.dinopark.model.Vehicle;
import com.dinopark.persistence.CsvWriter;
import com.dinopark.persistence.ParkDataWriter;
import com.dinopark.zone.ParkZone;
import com.dinopark.zone.PowerPlant;

import java.util.ArrayList;
import java.util.List;

public class ParkState
{
    private final List<Tourist> tourists;
    private final List<Dinosaur> dinosaurs;
    private final List<ParkZone> zones;
    private final List<String> activeEventNames = new ArrayList<>();

    private final PowerPlant powerPlant;

    private int currentStep;

    private double totalRevenue;
    private double totalExpenses;

    public ParkState(List<Tourist> tourists, List<Dinosaur> dinosaurs, List<ParkZone> zones, PowerPlant powerPlant,
                     ParkDataWriter db)
    {
        this.tourists = tourists;
        this.dinosaurs = dinosaurs;
        this.zones = zones;
        this.powerPlant = powerPlant;
        this.db = db;
        this.currentStep = 0;
    }

    public void incrementStep()
    {
        currentStep++;
    }

    public int getCurrentStep()
    {
        return currentStep;
    }

    //* Getters
    public List<Tourist> getTourists()
    {
        return tourists;
    }

    public List<Dinosaur> getDinosaurs()
    {
        return dinosaurs;
    }

    public List<ParkZone> getZones()
    {
        return zones;
    }

    public PowerPlant getPowerPlant()
    {
        return powerPlant;
    }


    //*MÉTRICAS
    public int countActiveTourists()
    {
        return (int) tourists.stream()
                .filter(t -> t.getStatus() == TouristStatus.IN_PARK)
                .count();
    }

    public int countDinosaursInEnclosure()
    {
        return (int) dinosaurs.stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
                .count();
    }

    public void addRevenue(double amount)
    {
        totalRevenue += amount;
    }

    public void addExpense(double amount)
    {
        totalExpenses += amount;
    }

    public double getTotalRevenue()
    {
        return totalRevenue;
    }

    public double getTotalExpenses()
    {
        return totalExpenses;
    }

    public List<Tourist> getActiveTourists()
    {
        return tourists.stream()
                .filter(t -> t.getStatus() == TouristStatus.IN_PARK)
                .toList();
    }

    public List<Dinosaur> getEnclosedDinosaurs()
    {
        return dinosaurs.stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
                .toList();
    }

    public void addActiveEvent(String name)
    {
        activeEventNames.add(name);
    }

    public void clearActiveEvents()
    {
        activeEventNames.clear();
        dealsHourActive = false;
        currentDiscount = 0.0;
    }

    public List<String> getActiveEventNames()
    {
        return activeEventNames;
    }

    private boolean dealsHourActive = false;
    private double  currentDiscount = 0.0;

    public boolean isDealsHourActive()       { return dealsHourActive; }
    public double  getCurrentDiscount()      { return currentDiscount; }

    public void setDealsHourActive(boolean active)
    {
        this.dealsHourActive = active;
    }

    public void setCurrentDiscount(double discount)
    {
        this.currentDiscount = discount;
    }

    //*VEHICLES
    private final List<Vehicle> vehicles = new ArrayList<>();

    public List<Vehicle> getVehicles() { return vehicles; }

    public void addVehicle(Vehicle vehicle)
    {
        vehicles.add(vehicle);
    }

    public int countVehiclesInUse()
    {
        return (int) vehicles.stream()
                .filter(v -> v.getStatus() == VehicleStatus.BROKEN
                        || v.getStatus() == VehicleStatus.IN_USE)
                .count();
    }

    //*DB (Strategy)
    private ParkDataWriter db;

    public ParkDataWriter getDb()  { return db; }

    public void setDb(ParkDataWriter db) { this.db = db; }

}
