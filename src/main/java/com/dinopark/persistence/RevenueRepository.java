package com.dinopark.persistence;

import com.dinopark.model.records.RevenueRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RevenueRepository
{
    private final Connection conn;

    public RevenueRepository()
    {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    public void save(RevenueRecord record)
    {
        String sql = """
            INSERT INTO revenues (type, amount, tourist_id, zone, timestamp)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, record.type());
            ps.setDouble(2, record.amount());
            ps.setInt   (3, record.touristId());
            ps.setString(4, record.zone());
            ps.setTimestamp(5,
                    java.sql.Timestamp.valueOf(record.timestamp()));
            ps.executeUpdate();
        }
        catch
        (SQLException e)
        {
            System.err.println("[DB] Error guardando revenue: "
                    + e.getMessage());
        }
    }
}
