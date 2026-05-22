package com.dinopark.model.records;

import java.time.LocalDateTime;

    public record RevenueRecord(long id, String type, double amount, int touristId, String zone, LocalDateTime timestamp)
    {
        public String toCsvLine()
        {
            return id + "," + type + "," + amount + "," + touristId + "," + zone + "," + timestamp;
        }
    }

