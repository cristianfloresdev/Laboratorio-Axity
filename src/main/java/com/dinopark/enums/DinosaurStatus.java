package com.dinopark.enums;

public enum DinosaurStatus
{
    IN_ENCLOSURE,
    ESCAPED,
    RECAPTURED;

    public boolean isFree()
    {
        return this == ESCAPED;
    }
}
