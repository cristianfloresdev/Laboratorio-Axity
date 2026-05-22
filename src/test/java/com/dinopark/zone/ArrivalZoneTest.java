package com.dinopark.zone;
// 5 puntos sumo al tests
import com.dinopark.enums.TouristStatus;
import com.dinopark.model.Tourist;
import com.dinopark.persistence.CsvWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrivalZoneTest
{
    private ArrivalZone zone;
    private CsvWriter csvWriter;

    @BeforeEach
    void setUp()
    {
        zone      = new ArrivalZone("Arrival", 30);
        csvWriter = Mockito.mock(CsvWriter.class);
    }

    @Test
    void touristChangesToInParkAfterProcessing()
    {
        Tourist t = new Tourist(1, "Alice");
        zone.addToQueue(t);
        zone.processBatch(1, csvWriter);
        assertEquals(TouristStatus.IN_PARK, t.getStatus());
    }

    @Test
    void correctTicketPriceCharged()
    {
        Tourist t = new Tourist(1, "Alice");
        zone.addToQueue(t);
        zone.processBatch(1, csvWriter);
        assertEquals(50.0, t.getMoneySpent());
    }

    @Test
    void batchSizeRespected()
    {
        for (int i = 0; i < 5; i++) {
            zone.addToQueue(new Tourist(i, "Tourist-" + i));
        }
        List<Tourist> admitted = zone.processBatch(3, csvWriter);
        assertEquals(3, admitted.size());
    }

    @Test
    void emptyQueueProcessesZeroTourists()
    {
        List<Tourist> admitted = zone.processBatch(5, csvWriter);
        assertEquals(0, admitted.size());
    }

    @Test
    void csvWriterCalledForEachAdmittedTourist()
    {
        Tourist t = new Tourist(1, "Alice");
        zone.addToQueue(t);
        zone.processBatch(1, csvWriter);
        Mockito.verify(csvWriter, Mockito.times(1))
                .appendRevenue(Mockito.any());
    }
}
