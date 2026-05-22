package com.dinopark.model.records;

import java.time.LocalDateTime;

public record EventRecord(long step, String eventName, String description, String affectedEntities,
                          LocalDateTime timestamp)
{
    public String toCsvLine()
    {
        return step + "," + eventName + "," + description + "," + affectedEntities + "," + timestamp;
    }
}