package com.dinopark.config;

import com.dinopark.exception.ParkNotFoundException;

import com.dinopark.exception.WarningLoadPropertiesException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public final class ParkConfig
{
    private static ParkConfig instance;
    private final Properties props;

    private static final Logger log = LoggerFactory.getLogger(ParkConfig.class);
    private ParkConfig()
    {
        props = new Properties();
        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("park.properties"))
        {
            if (is == null)
            {
                throw new ParkNotFoundException();
            }
            props.load(is);

            log.info("[ParkConfig] park.properties cargado correctamente");
        }
        catch (Exception e)
        {
            throw new WarningLoadPropertiesException("Error cargando park.properties: " + e.getMessage());
        }
    }

    public static ParkConfig getInstance()
    {
        if (instance == null)
        {
            instance = new ParkConfig();
        }
        return instance;
    }

    public int getInt(String key, int defaultValue)
    {
        try
        {
            return Integer.parseInt(props.getProperty(key,
                    String.valueOf(defaultValue)));
        }
        catch (NumberFormatException e)
        {
            log.warn("[ParkConfig] Valor inválido para '{}', usando default: {}", key, defaultValue);

            return defaultValue;
        }
    }

    public double getDouble(String key, double defaultValue)
    {
        try
        {
            return Double.parseDouble(props.getProperty(key,
                    String.valueOf(defaultValue)));
        }
        catch (NumberFormatException e)
        {
            log.warn("[ParkConfig] Valor inválido para '{}', usando default: {}", key, defaultValue);

            return defaultValue;
        }
    }

    public String getString(String key, String defaultValue)
    {
        return props.getProperty(key, defaultValue);
    }

    public long getSeed()
    {
        return Long.parseLong(props.getProperty("simulation.seed", "42"));
    }

    public int getTotalSteps()
    {
        return getInt("simulation.totalSteps", 100);
    }

    public static void resetForTesting()
    {
        instance = null;
    }
}
