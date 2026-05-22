package com.dinopark.persistence;

import com.dinopark.config.ParkConfig;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager
{
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager()
    {
        try
        {
            String path = ParkConfig.getInstance()
                    .getString("db.path", "./data/parkdb");
            String user = ParkConfig.getInstance()
                    .getString("db.user", "sa");
            String pass = ParkConfig.getInstance()
                    .getString("db.password", "");

            new java.io.File("./data").mkdirs();

            connection = DriverManager.getConnection(
                    "jdbc:h2:" + path + ";DB_CLOSE_DELAY=-1",
                    user, pass
            );

            runLiquibase();
            System.out.println("[DB] H2 iniciada y migraciones aplicadas");
        }
        catch (Exception e)
        {
            throw new RuntimeException(
                    "Error iniciando base de datos: " + e.getMessage(), e);
        }
    }

    public static DatabaseManager getInstance()
    {
        if (instance == null)
        {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void runLiquibase() throws Exception
    {
        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(
                        new JdbcConnection(connection)
                );
        Liquibase liquibase = new Liquibase(
                "db/changelog/db.changelog-master.xml",
                new ClassLoaderResourceAccessor(),
                database
        );
        liquibase.update("");
    }

    public Connection getConnection()
    {
        return connection;
    }

    static void resetForTesting()
    {
        instance = null;
    }
}