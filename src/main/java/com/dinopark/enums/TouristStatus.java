package com.dinopark.enums;

public enum TouristStatus
{
    WAITING,
    IN_PARK,
    ATTACKED,
    EXITED;

    public boolean isActive()
    {
        return this == WAITING || this == IN_PARK;
    }
}
