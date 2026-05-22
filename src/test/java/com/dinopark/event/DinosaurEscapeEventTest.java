package com.dinopark.event;
//4 puntos sumo al test
import com.dinopark.enums.DinosaurStatus;
import com.dinopark.model.CarnivoreDinosaur;
import com.dinopark.model.Dinosaur;
import com.dinopark.simulation.ParkState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DinosaurEscapeEventTest
{
    private DinosaurEscapeEvent event;
    private ParkState state;

    @BeforeEach
    void setUp()
    {
        event = new DinosaurEscapeEvent(1.0);
        state = Mockito.mock(ParkState.class);
    }

    @Test
    void getNameReturnsCorrectValue()
    {
        assertEquals("ESCAPE_DINOSAURIO", event.getName());
    }

    @Test
    void getProbabilityReturnsCorrectValue()
    {
        assertEquals(1.0, event.getProbability());
    }

    @Test
    void afterExecuteSomeDinosaurIsEscaped()
    {
        Dinosaur dino = new CarnivoreDinosaur(1, "Rex", "T-Rex");
        Mockito.when(state.getEnclosedDinosaurs())
                .thenReturn(List.of(dino));
        Mockito.when(state.getActiveTourists())
                .thenReturn(Collections.emptyList());

        event.execute(state, new Random(42));

        assertEquals(DinosaurStatus.ESCAPED, dino.getStatus());
    }

    @Test
    void emptyDinosaurListDoesNotThrow()
    {
        Mockito.when(state.getEnclosedDinosaurs())
                .thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> event.execute(state, new Random(42)));
    }

    @Test
    void toRecordReturnsCorrectName()
    {
        assertEquals("ESCAPE_DINOSAURIO", event.toRecord(1L).eventName());
    }
}
