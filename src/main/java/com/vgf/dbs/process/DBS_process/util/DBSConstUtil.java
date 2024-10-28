package com.vgf.dbs.process.DBS_process.util;

public class DBSConstUtil {

    public static final String KEY_CONNECTION_STRING = "CONNECTION_STRING";
    public static final String KEY_NOTIFICATION_ACTIF = "NOTIFICATION_ACTIF";
    public static final String KEY_SMTP_HOST = "SMTP_HOST";
    public static final String KEY_SMTP_SENDER = "SMTP_SENDER";
    public static final String KEY_SMTP_PORT = "SMTP_PORT";
    public static final String KEY_SMTP_LOGIN = "SMTP_LOGIN";
    public static final String KEY_SMTP_PASSWORD = "SMTP_PASSWORD";
    public static final String KEY_TRACE_DEBUG = "TRACE_DEBUG";
    public static final String KEY_APPLICATION_ID = "APPLICATION_ID";
    public static final String KEY_NB_DBS_DEPARTEMENT_MAX = "NB_DBS_DEPARTEMENT_MAX";
    public static final String KEY_NB_MARVIN_DEPARTEMENT_MAX = "NB_MARVIN_DEPARTEMENT_MAX";
    public static final String KEY_ERREUR_DESTINATAIRES = "ERREUR_DESTINATAIRES";
    public static final String KEY_IMPORT_RR_IN_FOLDER = "IMPORT_RR_IN_FOLDER";
    public static final String KEY_IMPORT_PART_IN_FOLDER = "IMPORT_PART_IN_FOLDER";
    public static final String KEY_EXPORT_DBS_OUT_FOLDER = "EXPORT_DBS_OUT_FOLDER";
    public static final String KEY_EXPORT_MARVIN_OUT_FOLDER = "EXPORT_MARVIN_OUT_FOLDER";
    public static final String KEY_NB_JOURS_ARCHIVE = "NB_JOURS_ARCHIVE";
    public static final String KEY_SMTP_DEBUG = "SMTP_DEBUG";
    public static final String KEY_SMTP_DEBUG_DEST = "SMTP_DEBUG_DEST";
    public static final String KEY_FILENAME_IMPORT_RR = "FILENAME_IMPORT_RR";
    public static final String KEY_CODE_PAYS_DEFAULT = "CODE_PAYS_DEFAULT";
    public static final String KEY_DEALER_COUNTRY_CODE_DEFAULT = "DEALER_COUNTRY_CODE_DEFAULT";
    public static final String KEY_DECRYPT_INPUT_PATH = "DECRYPT_INPUT_PATH";
    public static final String KEY_DECRYPT_OUTPUT_PATH = "DECRYPT_OUTPUT_PATH";
    public static final String KEY_DECRYPT_TEMP_PATH = "DECRYPT_TEMP_PATH";
    public static final String KEY_DECRYPT_OUTPUT_PATH_VW = "DECRYPT_OUTPUT_PATH_VW";
    public static final String KEY_DECRYPT_OUTPUT_PATH_VU = "DECRYPT_OUTPUT_PATH_VU";
    public static final String KEY_DECRYPT_PRIVATE_KEY_PATH = "DECRYPT_PRIVATE_KEY_PATH";
    public static final String DEFAULT_NB_JOUR_ARCHIVES = "30";
    public static final String DEFAULT_NB_DEPARTEMENT_MAX = "30";
    public static final String DEFAULT_NOTIF = "1";
    public static final String DEFAULT_DEBUG = "0";
    public static final String LIB_BRAND_VW = "VW";
    public static final String FILTREDEPARTEMENT = "FILTREDEPARTEMENT_[BRAND]";
    public static final String SEPARATORDEPARTEMENT = ";";
    public static final String EXPORTMARQUE = "EXPORT_DEALERCODE";
    public static final String EXPORTMARQUEMARVIN = "EXPORT_MARVIN";
    public static final String SEPARATORMARQUE = ";";
    public static final String NOMCOURTMARQUE = "NOM_COURT_[BRAND]";
    public static final String CODEENTETEMARQUE = "CODE_ENTETE_[BRAND]";
    public static final String SITEWEBDEFAUTMARQUE = "SITE_WEB_DEFAUT_[BRAND]";
    public static final String DEFAULT_EXPORT_DBS_FILENAME = "mdm_[COUNTRYCODE]_[BRAND].xml";
    public static final String DEFAULT_EXPORT_DBS_ARCHIVE_NAME = "DDX.[FROMSTATION][TOSTATION].[FORMAT].[COUNTRYCODE].[BRAND].ZIP";
    public static final String DEFAULT_EXPORT_MARVIN_FILENAME = "DDM_[IMPORTE_BID]_[MARVIN]_[COUNTRYCODEFRA].xml";
    public static final String DEFAULT_EXPORT_MARVIN_ARCHIVE_NAME = "DDX.[IMPORTE_BID].[MARVIN].[COUNTRYCODEFRA].ZIP";
    public static final String NOMCOURTMARQUEMARVIN = "NOM_COURT_MARVIN_[BRAND]";

    public static final String NOTIFICATION_MESSAGE = "Bonjour,<br/><br/>"
            + "L'export DBS a rencontré les erreurs suivantes :<br/>"
            + "{0}<br/>"
            + "{1}<br/>"
            + "<br/>Merci de contacter l'administrateur.<br/><br/>"
            + "Cordialement,<br/>"
            + "DBS Support Team";
}
