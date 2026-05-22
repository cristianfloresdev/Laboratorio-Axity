package com.dinopark.exception;

public class ParkNotFoundException extends RuntimeException
{
    public ParkNotFoundException()
    {
        super("park.properties no encontrado en resources");
    }
}
