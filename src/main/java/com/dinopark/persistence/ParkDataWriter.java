package com.dinopark.persistence;

import com.dinopark.model.records.EventRecord;
import com.dinopark.model.records.ExpenseRecord;
import com.dinopark.model.records.RevenueRecord;

public interface ParkDataWriter
{
    void appendRevenue(RevenueRecord record);
    void appendExpense(ExpenseRecord record);
    void appendEvent(EventRecord record);
    void close();
}
