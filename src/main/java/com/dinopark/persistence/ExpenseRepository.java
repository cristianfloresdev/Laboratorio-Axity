package com.dinopark.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ExpenseRepository
{
    private final Connection conn;

    public ExpenseRepository()
    {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    public void save(String type, double amount, String description)
    {
        String sql = """
            INSERT INTO expenses (type, amount, description, timestamp)
            VALUES (?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, type);
            ps.setDouble(2, amount);
            ps.setString(3, description);
            ps.setTimestamp(4,
                    java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.println("[DB] Error guardando expense: "
                    + e.getMessage());
        }
    }
}
