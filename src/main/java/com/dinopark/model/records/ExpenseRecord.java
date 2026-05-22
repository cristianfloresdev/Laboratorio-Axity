package com.dinopark.model.records;

import java.time.LocalDateTime;

    public record ExpenseRecord(long id, String type, double amount, String description, LocalDateTime timestamp)
    {
        public String toCsvLine()
        {
            return id + "," + type + "," + amount + "," + description + "," + timestamp;
        }
    }
