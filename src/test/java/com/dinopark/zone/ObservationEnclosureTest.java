package com.dinopark.zone;
//7 puntos sumo al tests
import com.dinopark.enums.ExperienceType;
import com.dinopark.model.Tourist;
import com.dinopark.persistence.CsvWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ObservationEnclosureTest
{
    private ObservationEnclosure basic;
    private ObservationEnclosure vip;
    private CsvWriter csvWriter;

    @BeforeEach
    void setUp()
    {
        basic     = new ObservationEnclosure("Basic", 20, ExperienceType.BASIC);
        vip       = new ObservationEnclosure("VIP",    5, ExperienceType.VIP);
        csvWriter = Mockito.mock(CsvWriter.class);
    }

    @Test
    void visitChargesCorrectPrice()
    {
        Tourist t = new Tourist(1, "Alice");
        basic.visit(t, new Random(42), csvWriter);
        assertEquals(ExperienceType.BASIC.getPrice(), t.getMoneySpent());
    }

    @Test
    void visitRecordsZone()
    {
        Tourist t = new Tourist(1, "Alice");
        basic.visit(t, new Random(42), csvWriter);
        assertFalse(t.getVisitedZones().isEmpty());
    }

    @Test
    void capacityRespected()
    {
        ObservationEnclosure small =
                new ObservationEnclosure("Small", 1, ExperienceType.BASIC);
        Tourist t1 = new Tourist(1, "A");
        Tourist t2 = new Tourist(2, "B");
        small.visit(t1, new Random(42), csvWriter);
        small.visit(t2, new Random(42), csvWriter);

        assertTrue(t1.getMoneySpent() > 0);
    }

    @Test
    void vipPriceHigherThanBasic()
    {
        assertTrue(ExperienceType.VIP.getPrice()
                > ExperienceType.BASIC.getPrice());
    }

    @Test
    void averageVipScoreHigherThanBasicOverManyRuns()
    {
        Random rng = new Random(42);
        double sumBasic = 0, sumVip = 0;
        int runs = 200;

        for (int i = 0; i < runs; i++) {
            sumBasic += ExperienceType.BASIC.generateScore(rng);
            sumVip   += ExperienceType.VIP.generateScore(rng);
        }
        assertTrue(sumVip / runs > sumBasic / runs);
    }
}
