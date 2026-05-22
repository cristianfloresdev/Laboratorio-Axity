package com.dinopark;
//Nota llega al 68%
import com.dinopark.config.ParkConfig;
import com.dinopark.enums.ExperienceType;
import com.dinopark.model.CarnivoreDinosaur;
import com.dinopark.model.Dinosaur;
import com.dinopark.model.HerbivoreDinosaur;
import com.dinopark.model.Tourist;
import com.dinopark.persistence.CsvWriter;
import com.dinopark.persistence.DatabaseService;
import com.dinopark.persistence.ParkDataWriter;
import com.dinopark.simulation.ParkState;
import com.dinopark.simulation.SimulationEngine;
import com.dinopark.zone.*;

import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        //* CONFIG
        ParkConfig config = ParkConfig.getInstance();

        //* PERSISTENCIA — CSV o BD según park.properties
        String mode = config.getString("output.mode", "csv");
        ParkDataWriter db;

        if (mode.equals("db"))
        {
            db = new DatabaseService(config.getString("db.path", "./data/parkdb"));
        }
        else
        {
            db = new CsvWriter(config.getString("output.directory", "output"));
        }

        //* TURISTAS
        int totalTourists = config.getInt("tourists", 50);
        List<Tourist> tourists = new ArrayList<>();

        for (int i = 0; i < totalTourists; i++)
        {
            tourists.add(new Tourist(i, "Tourist-" + i));
        }

        //* DINOSAURIOS
        int carnivores = config.getInt("dinosaurs.carnivores", 5);
        int herbivores = config.getInt("dinosaurs.herbivores", 15);
        List<Dinosaur> dinosaurs = new ArrayList<>();

        for (int i = 0; i < carnivores; i++)
        {
            dinosaurs.add(new CarnivoreDinosaur(i, "Rex-" + i, "T-Rex"));
        }
        for (int i = 0; i < herbivores; i++)
        {
            dinosaurs.add(new HerbivoreDinosaur(carnivores + i, "Bronto-" + i, "Brontosaurus"));
        }

        //* ZONAS
        ArrivalZone arrivalZone = new ArrivalZone("Arrival", config.getInt("arrival.maxCapacity", 30));
        CentralHub centralHub = new CentralHub("Central", config.getDouble("hub.souvenirPurchaseProbability", 0.4));
        BathroomZone bathroomZone = new BathroomZone(
                "Bathroom",
                config.getInt("bathroom.maxCapacity", 10),
                config.getInt("bathroom.useDurationSteps", 3),
                config.getDouble("bathroom.spaPurchaseProbability", 0.2)
        );
        ObservationEnclosure enclosure = new ObservationEnclosure(
                "Enclosure",
                config.getInt("enclosure.basic.maxVisitors", 20),
                ExperienceType.BASIC
        );
        PowerPlant powerPlant = new PowerPlant(
                "Plant",
                config.getDouble("powerplant.failureProbability", 0.05),
                config.getDouble("powerplant.initialEnergy", 100.0)
        );

        //* Cargar turistas a la cola
        tourists.forEach(arrivalZone::addToQueue);

        //* LISTA DE ZONAS
        List<ParkZone> zones = List.of(arrivalZone, centralHub, bathroomZone, enclosure, powerPlant);

        //* ESTADO GLOBAL
        ParkState state = new ParkState(tourists, dinosaurs, zones, powerPlant, db);

        //* ENGINE
        SimulationEngine engine = new SimulationEngine(
                state,
                arrivalZone,
                centralHub,
                bathroomZone,
                enclosure,
                powerPlant,
                db
        );

        //* RUN
        engine.run();

        //* CERRAR RECURSOS
        db.close();

        System.out.println("\n[Main] Simulation complete.");
    }
}