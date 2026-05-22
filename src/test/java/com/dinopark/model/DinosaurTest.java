package com.dinopark.model;
//4 puntos sumo al tests
import com.dinopark.enums.DinosaurStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DinosaurTest
{
    @Test
    void initialStatusIsInEnclosure()
    {
        Dinosaur d = new CarnivoreDinosaur(1, "Rex", "T-Rex");
        assertEquals(DinosaurStatus.IN_ENCLOSURE, d.getStatus());
    }

    @Test
    void escapeChangesStatusToEscaped()
    {
        Dinosaur d = new CarnivoreDinosaur(1, "Rex", "T-Rex");
        d.escape();
        assertEquals(DinosaurStatus.ESCAPED, d.getStatus());
    }

    @Test
    void returnToEnclosureRestoresStatus()
    {
        Dinosaur d = new CarnivoreDinosaur(1, "Rex", "T-Rex");
        d.escape();
        d.returnToEnclosure();
        assertEquals(DinosaurStatus.IN_ENCLOSURE, d.getStatus());
    }

    @Test
    void carnivoreDangerLevelIs09()
    {
        Dinosaur d = new CarnivoreDinosaur(1, "Rex", "T-Rex");
        assertEquals(0.9, d.getDangerLevel());
    }

    @Test
    void herbivoreDangerLevelIs02()
    {
        Dinosaur d = new HerbivoreDinosaur(1, "Trike", "Triceratops");
        assertEquals(0.2, d.getDangerLevel());
    }

    @Test
    void carnivoreGetDietReturnsCarnivore()
    {
        Dinosaur d = new CarnivoreDinosaur(1, "Rex", "T-Rex");
        assertEquals("CARNIVORE", d.getDiet());
    }
}
