package com.dinopark.persistence;

import com.dinopark.model.records.EventRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EventDbRepository
{
    private final Connection conn;

    public EventDbRepository()
    {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    public void save(EventRecord record)
    {
        String sql = """
            INSERT INTO events
              (step, event_name, description, affected_entities, timestamp)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setLong(1, record.step());
            ps.setString(2, record.eventName());
            ps.setString(3, record.description());
            ps.setString(4, record.affectedEntities());
            ps.setTimestamp(5,
                    java.sql.Timestamp.valueOf(record.timestamp()));
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.println("[DB] Error guardando event: "
                    + e.getMessage());
        }
    }
}
