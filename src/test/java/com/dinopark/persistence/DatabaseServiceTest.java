package com.dinopark.persistence;
//Sumo 1 punto sumo al tests
import com.dinopark.model.records.EventRecord;
import com.dinopark.model.records.ExpenseRecord;
import com.dinopark.model.records.RevenueRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseServiceTest
{
    private DatabaseService db;
    private Connection      connection;

    @BeforeEach
    void setUp()
    {
        db         = new DatabaseService(
                "./data/test-db-" + System.currentTimeMillis());
        connection = db.getConnection();
    }

    @AfterEach
    void tearDown()
    {
        db.close();
    }

    @Test
    void appendRevenueDoesNotThrow()
    {
        RevenueRecord r = new RevenueRecord(
                1L, "TICKET", 25.0, 1, "Arrival", LocalDateTime.now());
        assertDoesNotThrow(() -> db.appendRevenue(r));
    }

    @Test
    void appendRevenueRecordAppearsInDb() throws Exception
    {
        RevenueRecord r = new RevenueRecord(
                1L, "TICKET", 25.0, 1, "Arrival", LocalDateTime.now());
        db.appendRevenue(r);

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT COUNT(*) FROM revenues"))
        {
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    void appendExpenseDoesNotThrow()
    {
        ExpenseRecord e = new ExpenseRecord(
                1L, "ENERGY", 10.0, "Consumption", LocalDateTime.now());
        assertDoesNotThrow(() -> db.appendExpense(e));
    }

    @Test
    void appendExpenseRecordAppearsInDb() throws Exception
    {
        ExpenseRecord e = new ExpenseRecord(
                1L, "ENERGY", 10.0, "Consumption", LocalDateTime.now());
        db.appendExpense(e);

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT COUNT(*) FROM expenses"))
        {
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    void appendEventDoesNotThrow()
    {
        EventRecord ev = new EventRecord(
                1L, "BLACKOUT", "Falla total", "PowerPlant",
                LocalDateTime.now());
        assertDoesNotThrow(() -> db.appendEvent(ev));
    }

    @Test
    void appendEventRecordAppearsInDb() throws Exception
    {
        EventRecord ev = new EventRecord(
                1L, "BLACKOUT", "Falla total", "PowerPlant",
                LocalDateTime.now());
        db.appendEvent(ev);

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT COUNT(*) FROM events"))
        {
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }
}