package com.dinopark.model;
//2 puntos sumo al tests
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SatisfactionSurveyTest
{
    @Test
    void validSurveyCreatedCorrectly()
    {
        SatisfactionSurvey s = new SatisfactionSurvey(1, "BASIC", 4);
        assertEquals(1, s.getTouristId());
        assertEquals("BASIC", s.getEnclosureName());
        assertEquals(4, s.getScore());
    }

    @Test
    void scoreBelowOneThrowsException()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new SatisfactionSurvey(1, "BASIC", 0));
    }

    @Test
    void scoreAboveFiveThrowsException()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new SatisfactionSurvey(1, "BASIC", 6));
    }

    @Test
    void boundaryScoreOneIsValid()
    {
        assertDoesNotThrow(
                () -> new SatisfactionSurvey(1, "VIP", 1));
    }

    @Test
    void boundaryScoreFiveIsValid()
    {
        assertDoesNotThrow(
                () -> new SatisfactionSurvey(1, "VIP", 5));
    }
}
