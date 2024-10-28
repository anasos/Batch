package com.vgf.dbs.process.DBS_process.helper;

import com.vgf.dbs.process.DBS_process.service.ConfigurationService;
import com.vgf.dbs.process.DBS_process.util.DBSConstUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class DBSHelper {

    private String connectionString;
    private Map<String, String> configurationEntries;
    private boolean isInitialized;
    @Autowired
    private ConfigurationService configurationService;

    public DBSHelper(String connectionString) {
        this.connectionString = connectionString;
        this.configurationEntries = new HashMap<>();
        initializeConfiguration();
    }

    public String getConnectionString() {
        return connectionString;
    }

    public boolean isInitialized() {
        return isInitialized = (configurationEntries.containsKey(DBSConstUtil.KEY_CONNECTION_STRING) &&
                configurationEntries.containsKey(DBSConstUtil.KEY_APPLICATION_ID) &&
                configurationEntries.containsKey(DBSConstUtil.KEY_IMPORT_RR_IN_FOLDER) &&
                configurationEntries.containsKey(DBSConstUtil.KEY_IMPORT_PART_IN_FOLDER) &&
                configurationEntries.containsKey(DBSConstUtil.KEY_EXPORT_DBS_OUT_FOLDER) &&
                configurationEntries.containsKey(DBSConstUtil.KEY_EXPORT_MARVIN_OUT_FOLDER) &&
                configurationEntries.containsKey(DBSConstUtil.KEY_FILENAME_IMPORT_RR));
    }

    private void addConfigurationEntry(String key, String value) {
        if (value != null) {
            configurationEntries.put(key, value);
        }
    }

    private Integer getIntValue(String value) {
        try {
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void initializeConfiguration() {
        try {
            addConfigurationEntry(DBSConstUtil.KEY_CONNECTION_STRING, connectionString);
            addConfigurationEntry(DBSConstUtil.KEY_SMTP_HOST, configurationService.fetchConfigValue(DBSConstUtil.KEY_SMTP_HOST));
            addConfigurationEntry(DBSConstUtil.KEY_SMTP_SENDER, configurationService.fetchConfigValue( DBSConstUtil.KEY_SMTP_SENDER));
            addConfigurationEntry(DBSConstUtil.KEY_SMTP_PORT, configurationService.fetchConfigValue( DBSConstUtil.KEY_SMTP_PORT));
            addConfigurationEntry(DBSConstUtil.KEY_SMTP_LOGIN, configurationService.fetchConfigValue( DBSConstUtil.KEY_SMTP_LOGIN));
            addConfigurationEntry(DBSConstUtil.KEY_SMTP_PASSWORD, configurationService.fetchConfigValue( DBSConstUtil.KEY_SMTP_PASSWORD));
            addConfigurationEntry(DBSConstUtil.KEY_SMTP_DEBUG_DEST, configurationService.fetchConfigValue( DBSConstUtil.KEY_SMTP_DEBUG_DEST));

            addConfigurationEntry(DBSConstUtil.KEY_APPLICATION_ID, configurationService.fetchConfigValue( DBSConstUtil.KEY_APPLICATION_ID));
            addConfigurationEntry(DBSConstUtil.KEY_ERREUR_DESTINATAIRES, configurationService.fetchConfigValue( DBSConstUtil.KEY_ERREUR_DESTINATAIRES));
            addConfigurationEntry(DBSConstUtil.KEY_IMPORT_RR_IN_FOLDER, configurationService.fetchConfigValue( DBSConstUtil.KEY_IMPORT_RR_IN_FOLDER));
            addConfigurationEntry(DBSConstUtil.KEY_IMPORT_PART_IN_FOLDER, configurationService.fetchConfigValue( DBSConstUtil.KEY_IMPORT_PART_IN_FOLDER));
            addConfigurationEntry(DBSConstUtil.KEY_EXPORT_DBS_OUT_FOLDER, configurationService.fetchConfigValue( DBSConstUtil.KEY_EXPORT_DBS_OUT_FOLDER));
            addConfigurationEntry(DBSConstUtil.KEY_EXPORT_MARVIN_OUT_FOLDER, configurationService.fetchConfigValue( DBSConstUtil.KEY_EXPORT_MARVIN_OUT_FOLDER));
            addConfigurationEntry(DBSConstUtil.KEY_FILENAME_IMPORT_RR, configurationService.fetchConfigValue( DBSConstUtil.KEY_FILENAME_IMPORT_RR));

            addConfigurationEntry(DBSConstUtil.KEY_NOTIFICATION_ACTIF, String.valueOf(getIntValueOrDefault(configurationService.fetchConfigValue( DBSConstUtil.KEY_NOTIFICATION_ACTIF), 0)));
            addConfigurationEntry(DBSConstUtil.KEY_SMTP_DEBUG, String.valueOf(getIntValueOrDefault(configurationService.fetchConfigValue( DBSConstUtil.KEY_SMTP_DEBUG), 0)));
            addConfigurationEntry(DBSConstUtil.KEY_TRACE_DEBUG, String.valueOf(getIntValueOrDefault(configurationService.fetchConfigValue( DBSConstUtil.KEY_TRACE_DEBUG), 0)));

            addConfigurationEntry(DBSConstUtil.KEY_NB_DBS_DEPARTEMENT_MAX, String.valueOf(getIntValueOrDefault(configurationService.fetchConfigValue( DBSConstUtil.KEY_NB_DBS_DEPARTEMENT_MAX), Integer.parseInt(DBSConstUtil.DEFAULT_NB_DEPARTEMENT_MAX))));
            addConfigurationEntry(DBSConstUtil.KEY_NB_MARVIN_DEPARTEMENT_MAX, String.valueOf(getIntValueOrDefault(configurationService.fetchConfigValue( DBSConstUtil.KEY_NB_MARVIN_DEPARTEMENT_MAX), Integer.parseInt(DBSConstUtil.DEFAULT_NB_DEPARTEMENT_MAX))));
            addConfigurationEntry(DBSConstUtil.KEY_NB_JOURS_ARCHIVE, String.valueOf(getIntValueOrDefault(configurationService.fetchConfigValue( DBSConstUtil.KEY_NB_JOURS_ARCHIVE), Integer.parseInt(DBSConstUtil.DEFAULT_NB_JOUR_ARCHIVES))));

            addConfigurationEntry(DBSConstUtil.KEY_CODE_PAYS_DEFAULT, configurationService.fetchConfigValue( DBSConstUtil.KEY_CODE_PAYS_DEFAULT));
            addConfigurationEntry(DBSConstUtil.KEY_DECRYPT_INPUT_PATH, configurationService.fetchConfigValue( DBSConstUtil.KEY_DECRYPT_INPUT_PATH));
            addConfigurationEntry(DBSConstUtil.KEY_DECRYPT_OUTPUT_PATH, configurationService.fetchConfigValue( DBSConstUtil.KEY_DECRYPT_OUTPUT_PATH));
            addConfigurationEntry(DBSConstUtil.KEY_DECRYPT_PRIVATE_KEY_PATH, configurationService.fetchConfigValue( DBSConstUtil.KEY_DECRYPT_PRIVATE_KEY_PATH));
            addConfigurationEntry(DBSConstUtil.KEY_DECRYPT_TEMP_PATH, configurationService.fetchConfigValue( DBSConstUtil.KEY_DECRYPT_TEMP_PATH));
            addConfigurationEntry(DBSConstUtil.KEY_DECRYPT_OUTPUT_PATH_VU, configurationService.fetchConfigValue( DBSConstUtil.KEY_DECRYPT_OUTPUT_PATH_VU));
            addConfigurationEntry(DBSConstUtil.KEY_DECRYPT_OUTPUT_PATH_VW, configurationService.fetchConfigValue( DBSConstUtil.KEY_DECRYPT_OUTPUT_PATH_VW));
            addConfigurationEntry(DBSConstUtil.KEY_DEALER_COUNTRY_CODE_DEFAULT, configurationService.fetchConfigValue( DBSConstUtil.KEY_DEALER_COUNTRY_CODE_DEFAULT));

            isInitialized = true;
        } catch (Exception ex) {
            System.out.println("Error initializing configuration: " + ex.getMessage());
        }
    }

    public String getValue(String key) {
        return isInitialized && configurationEntries.containsKey(key) ? configurationEntries.get(key) : null;
    }

    private int getIntValueOrDefault(String value, int defaultValue) {
        Integer result = getIntValue(value);
        return result != null ? result : defaultValue;
    }
}
