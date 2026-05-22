package com.dinopark.persistence;
//4 puntos sumo al tests
import com.dinopark.model.records.EventRecord;
import com.dinopark.model.records.ExpenseRecord;
import com.dinopark.model.records.RevenueRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CsvWriterTest
{
    private CsvWriter writer;
    private final String testDir = "output/test-"
            + System.currentTimeMillis();

    @BeforeEach
    void setUp()
    {
        writer = new CsvWriter(testDir);
    }

    @AfterEach
    void tearDown() throws IOException
    {
        // Limpiar archivos de test
        Files.deleteIfExists(Paths.get(testDir, "revenue.csv"));
        Files.deleteIfExists(Paths.get(testDir, "expense.csv"));
        Files.deleteIfExists(Paths.get(testDir, "events.csv"));
        Files.deleteIfExists(Paths.get(testDir));
    }

    @Test
    void appendRevenueDoesNotThrow()
    {
        RevenueRecord r = new RevenueRecord(
                1L, "TICKET", 25.0, 1, "Arrival", LocalDateTime.now());
        assertDoesNotThrow(() -> writer.appendRevenue(r));
    }

    @Test
    void appendRevenueWritesToFile() throws IOException
    {
        RevenueRecord r = new RevenueRecord(
                1L, "TICKET", 25.0, 1, "Arrival", LocalDateTime.now());
        writer.appendRevenue(r);

        Path path = Paths.get(testDir, "revenue.csv");
        String content = Files.readString(path);
        assertTrue(content.contains("TICKET"));
    }

    @Test
    void appendExpenseDoesNotThrow()
    {
        ExpenseRecord e = new ExpenseRecord(
                1L, "ENERGY", 10.0, "Consumption", LocalDateTime.now());
        assertDoesNotThrow(() -> writer.appendExpense(e));
    }

    @Test
    void appendExpenseWritesToFile() throws IOException
    {
        ExpenseRecord e = new ExpenseRecord(
                1L, "ENERGY", 10.0, "Consumption", LocalDateTime.now());
        writer.appendExpense(e);

        Path path = Paths.get(testDir, "expense.csv");
        String content = Files.readString(path);
        assertTrue(content.contains("ENERGY"));
    }

    @Test
    void appendEventDoesNotThrow()
    {
        EventRecord ev = new EventRecord(
                1L, "BLACKOUT", "Falla total",
                "PowerPlant", LocalDateTime.now());
        assertDoesNotThrow(() -> writer.appendEvent(ev));
    }

    @Test
    void appendEventWritesToFile() throws IOException
    {
        EventRecord ev = new EventRecord(
                1L, "BLACKOUT", "Falla total",
                "PowerPlant", LocalDateTime.now());
        writer.appendEvent(ev);

        Path path = Paths.get(testDir, "events.csv");
        String content = Files.readString(path);
        assertTrue(content.contains("BLACKOUT"));
    }

    @Test
    void closeDoesNotThrow()
    {
        assertDoesNotThrow(() -> writer.close());
    }

    @Test
    void csvFilesCreatedOnInit()
    {
        assertTrue(Files.exists(Paths.get(testDir, "revenue.csv")));
        assertTrue(Files.exists(Paths.get(testDir, "expense.csv")));
        assertTrue(Files.exists(Paths.get(testDir, "events.csv")));
    }
}