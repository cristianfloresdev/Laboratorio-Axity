package com.dinopark.persistence;

import com.dinopark.model.records.EventRecord;
import com.dinopark.model.records.ExpenseRecord;
import com.dinopark.model.records.RevenueRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvWriter implements ParkDataWriter
{
    private final Path revenuePath;
    private final Path expensePath;
    private final Path eventPath;

    public CsvWriter(String outputDir)
    {
        try
        {
            Path dir = Paths.get(outputDir);

            if (!Files.exists(dir))
            {
                Files.createDirectories(dir);
            }

            this.revenuePath = dir.resolve("revenue.csv");
            this.expensePath = dir.resolve("expense.csv");
            this.eventPath = dir.resolve("events.csv");

            initFile(revenuePath, "id,type,amount,touristId,zone,timestamp");
            initFile(expensePath, "id,type,amount,description,timestamp");
            initFile(eventPath, "step,eventName,description,affectedEntities,timestamp");

        }
        catch (IOException e)
        {
            throw new RuntimeException("Error initializing CSV files", e);
        }
    }

    private void initFile(Path path, String header) throws IOException
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), false)))
        {
            writer.write(header);
            writer.newLine();
        }
    }

    // APPEND METHODS
    public void appendRevenue(RevenueRecord record)
    {
        writeLine(revenuePath, record.toCsvLine());
    }

    public void appendExpense(ExpenseRecord record)
    {
        writeLine(expensePath, record.toCsvLine());
    }

    public void appendEvent(EventRecord record)
    {
        writeLine(eventPath, record.toCsvLine());
    }

    // UTIL
    private void writeLine(Path path, String line)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), true)))
        {
            writer.write(line);
            writer.newLine();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error writing CSV line", e);
        }
    }

    @Override
    public void close()
    {
        System.out.println("[CSV] Writer cerrado");
    }
}
