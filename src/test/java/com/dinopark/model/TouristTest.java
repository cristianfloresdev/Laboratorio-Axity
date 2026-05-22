package com.dinopark.model;
// 4 Puntos sumo al tests
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TouristTest
{

    @Test
    void shouldStartWaiting()
    {
        Tourist t = new Tourist(1, "Test");
        assertEquals("WAITING", t.getStatus().name());
    }

    @Test
    void shouldAccumulateMoney()
    {
        Tourist t = new Tourist(1, "Test");

        t.spend(10);
        t.spend(5);

        assertEquals(15, t.getMoneySpent());
    }

    @Test
    void shouldRecordVisit()
    {
        Tourist t = new Tourist(1, "Test");

        t.recordVisit("ZoneA");

        assertTrue(t.getVisitedZones().contains("ZoneA"));
    }
}