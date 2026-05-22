package com.dinopark.persistence;

import com.dinopark.config.ParkConfig;
import com.dinopark.model.records.EventRecord;
import com.dinopark.model.records.ExpenseRecord;
import com.dinopark.model.records.RevenueRecord;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseService implements ParkDataWriter
{
    private final Connection connection;

    public DatabaseService(String dbPath)
    {
        try
        {
            new java.io.File("./data").mkdirs();

            String user = ParkConfig.getInstance()
                    .getString("db.user", "sa");
            String pass = ParkConfig.getInstance()
                    .getString("db.password", "");

            connection = DriverManager.getConnection(
                    "jdbc:h2:" + dbPath + ";DB_CLOSE_DELAY=-1",
                    user, pass
            );
            runLiquibase();
            System.out.println("[DB] DatabaseService iniciado correctamente");
        }
        catch (Exception e)
        {
            throw new RuntimeException(
                    "Error iniciando DatabaseService: " + e.getMessage(), e);
        }
    }

    private void runLiquibase() throws Exception
    {
        Database db = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(
                        new JdbcConnection(connection)
                );
        new Liquibase(
                "db/changelog/db.changelog-master.xml",
                new ClassLoaderResourceAccessor(),
                db
        ).update(new Contexts());
    }

    // --- mismos nombres que CsvWriter ---

    public void appendRevenue(RevenueRecord r)
    {
        String sql = """
            INSERT INTO revenues (type, amount, tourist_id, zone, timestamp)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, r.type());
            ps.setDouble(2, r.amount());
            ps.setInt(3, r.touristId());
            ps.setString(4, r.zone());
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(r.timestamp()));
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.println("[DB] Error insertando revenue: "
                    + e.getMessage());
        }
    }

    public void appendExpense(ExpenseRecord e)
    {
        String sql = """
            INSERT INTO expenses (type, amount, description, timestamp)
            VALUES (?, ?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, e.type());
            ps.setDouble(2, e.amount());
            ps.setString(3, e.description());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(e.timestamp()));
            ps.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.err.println("[DB] Error insertando expense: "
                    + ex.getMessage());
        }
    }

    public void appendEvent(EventRecord ev)
    {
        String sql = """
            INSERT INTO events
              (step, event_name, description, affected_entities, timestamp)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1, ev.step());
            ps.setString(2, ev.eventName());
            ps.setString(3, ev.description());
            ps.setString(4, ev.affectedEntities());
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(ev.timestamp()));
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.println("[DB] Error insertando event: "
                    + e.getMessage());
        }
    }

    public void close()
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                connection.close();
                System.out.println("[DB] Conexión cerrada");
            }
        }
        catch (SQLException e)
        {
            System.err.println("[DB] Error cerrando conexión: "
                    + e.getMessage());
        }
    }

    public Connection getConnection()
    {
        return connection;
    }
}