package com.vgf.dbs.process.DBS_process.service;

import com.vgf.dbs.process.DBS_process.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.logging.Logger;

@Service
public class ConfigurationService {

    // Logger for logging errors and information
    private static final Logger logger = Logger.getLogger(ConfigurationService.class.getName());

    @Autowired
    private ConfigRepository configRepository;

    public String fetchConfigValue(String key) {
        return configRepository.getConfigValue(key);
    }

    /**
     * Retrieves a configuration value from the database using a stored procedure.
     *
     * @param connectionString The connection string to the Oracle database
     * @param key              The configuration key whose value needs to be fetched
     * @return The configuration value as a String
     */
    public String getConfigValue(String connectionString, String key) {
        String value = null;

        String storedProcedure = "{call PKG_DBS.SP_GET_CONFIG_VALUE(?, ?)}";

        try (Connection connection = DriverManager.getConnection(connectionString);
             CallableStatement stmt = connection.prepareCall(storedProcedure)) {

            // Input parameter for the configuration key
            stmt.setString(1, key);

            // Output parameter to retrieve the configuration value
            stmt.registerOutParameter(2, Types.VARCHAR);

            // Execute the stored procedure
            stmt.execute();

            // Get the configuration value
            value = stmt.getString(2);

        } catch (SQLException ex) {
            logger.severe("Error fetching config value: " + ex.getMessage());
        }

        return value;
    }
}
