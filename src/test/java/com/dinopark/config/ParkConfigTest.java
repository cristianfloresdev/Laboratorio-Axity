package com.dinopark.config;
// 4 puntos sumo al test
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ParkConfigTest
{
    @BeforeEach
    void setUp()
    {
        ParkConfig.resetForTesting();
    }

    @Test
    void singletonReturnsSameInstance()
    {
        ParkConfig a = ParkConfig.getInstance();
        ParkConfig b = ParkConfig.getInstance();
        assertSame(a, b);
    }

    @Test
    void readsIntProperty()
    {
        ParkConfig config = ParkConfig.getInstance();
        assertEquals(50, config.getInt("tourists", 0));
    }

    @Test
    void readsDoubleProperty()
    {
        ParkConfig config = ParkConfig.getInstance();
        assertEquals(25.0, config.getDouble("arrival.ticketPrice", 0.0));
    }

    @Test
    void returnsDefaultIfKeyMissing()
    {
        ParkConfig config = ParkConfig.getInstance();
        assertEquals(99, config.getInt("key.that.does.not.exist", 99));
    }

    @Test
    void readsSeed()
    {
        ParkConfig config = ParkConfig.getInstance();
        assertEquals(42L, config.getSeed());
    }
}
