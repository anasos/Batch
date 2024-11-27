package com.vgf.dbs.process.DBS_process.service;


import com.vgf.dbs.process.DBS_process.model.*;
import org.hibernate.dialect.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProcessService {

    @Value("${spring.datasource.url}")
    private String connectionString;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String userPass;

    @Autowired
    private DealerService dealerService;

    public static final String IMPORT_RR = "IMPORT_RR";
    public static final String IMPORT_PART = "IMPORT_PART";
    public static final String EXPORT_FULL_DBS = "EXPORT_FULL_DBS";
    public static final String EXPORT_MARVIN = "EXPORT_MARVIN";

    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_SUCCES_WITH_WARNING = "SUCCES_WITH_WARNING";
    public static final String STATUS_FAILED = "FAILED";

    // Logger
    private static final Logger logger = Logger.getLogger(ProcessService.class.getName());

    // Method to initialize a new process
    public int initializeNewProcess(Connection connection, String type) {
        int processId = -1;

        try (CallableStatement callableStatement = connection.prepareCall("{call PKG_DBS.SP_CREATE_PROCESS(?, ?, ?, ?)}")) {
            // Set the input parameter
            callableStatement.setString(1, type);

            // Register the output parameters
            callableStatement.registerOutParameter(2, Types.INTEGER);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.registerOutParameter(4, Types.VARCHAR);

            // Execute the stored procedure
            callableStatement.execute();

            // Check for errors in the output parameters
            String errorSqlCode = callableStatement.getString(3);
            String errorDescription = callableStatement.getString(4);

            if (errorSqlCode != null && !errorSqlCode.isEmpty()) {
                logger.severe("Error from procedure 'PKG_DBS.SP_CREATE_PROCESS': " + errorDescription);
                processId = -1;
            } else {
                processId = callableStatement.getInt(2);
            }

        } catch (SQLException ex) {
            processId = -1;
            logger.severe("Error while creating process in table TS_PROCESS: " + ex.getMessage());
        }

        return processId;
    }

    public List<ModelExportDepartement> callGetDepartementInfosDBS(Connection connection, int processId, String dealrID) {
        List<ModelExportDepartement> listResult = new ArrayList<>();

        //Connection connection = DriverManager.getConnection(connectionString, user, userPass);

        try (CallableStatement stmt = connection.prepareCall("{call PKG_DBS.SP_GetAllDepartementInfos(?, ?, ?)}")) {

            // Set input parameters
            stmt.setInt(1, processId);
            stmt.setString(2, dealrID);

            // Register output parameter (for the REF_CURSOR)
            stmt.registerOutParameter(3, OracleTypes.CURSOR);

            // Execute the stored procedure
            stmt.execute();

            // Retrieve the cursor and process results
            try (ResultSet resultSet = (ResultSet) stmt.getObject(3)) {
                while (resultSet != null && resultSet.next()) {
                    ModelExportDepartement result = new ModelExportDepartement();
                    result.setDepartmentCode(resultSet.getString("departmentCode"));
                    result.setContactName(resultSet.getString("contactName"));
                    result.setEmail(resultSet.getString("email"));
                    result.setPhoneCountryCode(resultSet.getString("phoneCountryCode"));
                    result.setAreaCode(resultSet.getString("areaCode"));
                    result.setPhoneNumber(resultSet.getString("phoneNumber"));
                    result.setFullPhoneNumber(resultSet.getString("fullPhoneNumber"));
                    result.setPhoneTypeCode(resultSet.getString("phoneTypeCode"));
                    result.setAddressLine1(resultSet.getString("addressLine1"));
                    result.setAddressLine2(resultSet.getString("addressLine2"));
                    result.setCityName(resultSet.getString("cityName"));
                    result.setStateProvinceCode(resultSet.getString("stateProvinceCode"));
                    result.setCountryCode(resultSet.getString("countryCode"));
                    result.setZipPostalCode(resultSet.getString("zipPostalCode"));
                    result.setZipcodeSuffix(resultSet.getString("zipcodeSuffix"));
                    result.setLongitude(resultSet.getString("longitude"));
                    result.setLatitude(resultSet.getString("latitude"));
                    result.setAddressTypeCode(resultSet.getString("addressTypeCode"));

                    listResult.add(result);
                }
            }

        } catch (SQLException ex) {
            logger.severe("Error retrieving department information: " + ex.getMessage());
        }

        return listResult;
    }

    public List<ModelExportDealerService> callGetDealerServicesDBS(Connection connection, int processId, String dealerID) {
        List<ModelExportDealerService> listResult = new ArrayList<>();

        String sql = "SELECT * FROM DEALER_SERVICES WHERE PROCESS_ID = ?";

        String storedProcedure = "{call PKG_DBS.SP_GetAllDealerServices(?, ?, ?)}";

        try (CallableStatement stmt = connection.prepareCall(storedProcedure)) {

            // Set input parameters
            stmt.setInt(1, processId);
            stmt.setString(2, dealerID);

            // Register output parameter
            stmt.registerOutParameter(3, OracleTypes.CURSOR);

            stmt.execute();

            // Retrieve the result set
            try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                if (!rs.isBeforeFirst()) { // Checks if ResultSet is empty
                    return listResult;
                }

                // Get ordinal index for columns
                int ordinalServiceTypeCode = rs.findColumn("serviceTypeCode");


                while (rs.next()) {
                    ModelExportDealerService service = new ModelExportDealerService();
                    service.setServiceTypeCode(rs.getString(ordinalServiceTypeCode));
                    // Map other fields if needed
                    listResult.add(service);
                }
            }

        } catch (SQLException ex) {
            logger.severe("Error fetching dealer services: " + ex.getMessage());
        }

        return listResult;
    }

    public List<ModelExportHoraire> callGetHoraireDBS(Connection connection, int processId, String noContrat, String deptCode) {
        List<ModelExportHoraire> listResult = new ArrayList<>();

        String sql = "SELECT * FROM DEALER_HORAIRES WHERE PROCESS_ID = ?";

        try (CallableStatement stmt = connection.prepareCall("{call PKG_DBS.SP_GETHORAIRES(?, ?, ?, ?)}")) {
            // Set input parameters
            stmt.setInt(1, processId);
            stmt.setString(2, noContrat);
            stmt.setString(3, deptCode);

            // Register output parameter (REF_CURSOR)
            stmt.registerOutParameter(4, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(4)) {
                if (!rs.next()) {
                    return listResult;
                }

                int ordinalDayOfWeekCode = rs.findColumn("dayOfWeekCode");
                int ordinalClosedIndicator = rs.findColumn("closedIndicator");
                int ordinalHrDebut1 = rs.findColumn("HR_DEBUT_1");
                int ordinalHrFin1 = rs.findColumn("HR_FIN_1");
                int ordinalHrDebut2 = rs.findColumn("HR_DEBUT_2");
                int ordinalHrFin2 = rs.findColumn("HR_FIN_2");

                do {

                    ModelExportHoraire horaire = new ModelExportHoraire();
                    horaire.setDayOfWeekCode(rs.getString(ordinalDayOfWeekCode));
                    horaire.setClosedIndicator(rs.getString(ordinalClosedIndicator));
                    horaire.setHR_DEBUT_1(rs.getString(ordinalHrDebut1));
                    horaire.setHR_FIN_1(rs.getString(ordinalHrFin1));
                    horaire.setHR_DEBUT_2(rs.getString(ordinalHrDebut2));
                    horaire.setHR_FIN_2(rs.getString(ordinalHrFin2));

                    // Map other fields
                    listResult.add(horaire);
                } while (rs.next());

            }

        } catch (SQLException ex) {
            logger.severe("Error fetching dealer schedules: " + ex.getMessage());
        }

        return listResult;
    }

    public List<ModelExportTypeCode> callGetTypeCodesDBS(Connection connection, int processId) {
        List<ModelExportTypeCode> listResult = new ArrayList<>();

//        String sql = "SELECT * FROM DEALER_TYPE_CODES WHERE PROCESS_ID = ?";
        try (CallableStatement stmt = connection.prepareCall("{call PKG_DBS.SP_GetAllTypeCodes(?, ?)}")) {

            // Set input parameters
            stmt.setInt(1, processId);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);

            stmt.execute();

            try (ResultSet resultSet = (ResultSet) stmt.getObject(2)) {
                if (!resultSet.next()) {
                    return listResult; // No rows
                }

                // Get column indices
                int ordinalTypeCodeCategory = resultSet.findColumn("typeCodeCategory");
                int ordinalTypeCodeValue = resultSet.findColumn("typeCodeValue");
                int ordinalTypeCodeDesc = resultSet.findColumn("typeCodeDesc");
                int ordinalParentTypeCodeValue = resultSet.findColumn("parentTypeCodeValue");

                // Read the result set
                do {
                    ModelExportTypeCode result = new ModelExportTypeCode();
                    result.setTypeCodeCategory(resultSet.getString(ordinalTypeCodeCategory));
                    result.setTypeCodeValue(resultSet.getString(ordinalTypeCodeValue));
                    result.setTypeCodeDesc(resultSet.getString(ordinalTypeCodeDesc));
                    result.setParentTypeCodeValue(resultSet.getString(ordinalParentTypeCodeValue));

                    listResult.add(result);

                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            logger.severe("Error fetching dealer type codes: " + ex.getMessage());
        }

        return listResult;
    }

    public List<ModelExportDealer> callGetDealerInfosDBS(int processId) {
        List<ModelExportDealer> listResult = new ArrayList<>();

        //List<ModelExportDealer> modelExportDealers  = dealerService.getAllDealerInfos(processId);

        //String sql = "SELECT * FROM DEALERS WHERE PROCESS_ID = ?";

        try (Connection connection = DriverManager.getConnection(connectionString, user, userPass);
             CallableStatement callableStatement = connection.prepareCall("{call PKG_DBS.SP_GetAllDealerInfos(?, ?)}")) {

            // Set the input parameter
            callableStatement.setInt(1, processId);

            // Register the REF CURSOR as an output parameter
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

            // Execute the procedure
            callableStatement.execute();

            // Process the result set
            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(2)) {
                while (resultSet.next()) {

                    ModelExportDealer dealer = new ModelExportDealer();
                    dealer.setDealerId(resultSet.getString("dealerId"));
                    dealer.setDealerCountryCode(resultSet.getString("dealerCountryCode"));
                    dealer.setDealerDivCode(resultSet.getString("dealerDivCode"));

                    String dateStr = resultSet.getString("updateDate");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    // Parse string to LocalDateTime
                    LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);

                    // Convert LocalDateTime to java.util.Date
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

                    LocalDate localDate = localDateTime.toLocalDate();
                    dealer.setUpdateDate(localDate);

                    dealer.setDealerTypeCode(resultSet.getString("dealerTypeCode"));
                    dealer.setDealerLanguageCode(resultSet.getString("dealerLanguageCode"));
                    dealer.setAddressLine1(resultSet.getString("addressLine1"));
                    dealer.setAddressLine2(resultSet.getString("addressLine2"));
                    dealer.setCityName(resultSet.getString("cityName"));
                    dealer.setStateProvinceCode(resultSet.getString("stateProvinceCode"));
                    dealer.setCountryCode(resultSet.getString("countryCode"));
                    dealer.setZipPostalCode(resultSet.getString("zipPostalCode"));
                    dealer.setZipcodeSuffix(resultSet.getString("zipcodeSuffix"));
                    dealer.setLongitude(resultSet.getDouble("longitude"));
                    dealer.setLatitude(resultSet.getDouble("latitude"));
                    dealer.setDepartmentCode(resultSet.getString("departmentCode"));
                    dealer.setAddressTypeCode(resultSet.getString("addressTypeCode"));
                    dealer.setCategorizationCode(resultSet.getString("categorizationCode"));
                    dealer.setCategorizationTypeCode(resultSet.getString("categorizationTypeCode"));
                    dealer.setDealerDateData(resultSet.getString("dealerDateData"));
                    dealer.setDateTypeCode(resultSet.getString("dateTypeCode"));
                    dealer.setDealerDateDataFin(resultSet.getString("dealerDateDataFin"));
                    dealer.setDateTypeCodeFin(resultSet.getString("dateTypeCodeFin"));
                    dealer.setStatusCode(resultSet.getString("statusCode"));
                    dealer.setStatusTypeCode(resultSet.getString("statusTypeCode"));
                    dealer.setStatusCode2(resultSet.getString("statusCode2"));
                    dealer.setStatusTypeCode2(resultSet.getString("statusTypeCode2"));
                    dealer.setDealerNameData(resultSet.getString("dealerNameData"));
                    dealer.setNameTypeCode(resultSet.getString("nameTypeCode"));
                    dealer.setG_dealerNameData(resultSet.getString("G_dealerNameData"));
                    dealer.setG_nameTypeCode(resultSet.getString("G_nameTypeCode"));
                    dealer.setDealerPreferencetDetail(resultSet.getString("dealerPreferencetDetail"));
                    dealer.setDealerPreferencetDetail2(resultSet.getString("dealerPreferencetDetail2"));
                    dealer.setDealerPreferencetDetail3(resultSet.getString("dealerPreferencetDetail3"));
                    dealer.setDealerPreferencetDetail4(resultSet.getString("dealerPreferencetDetail4"));
                    dealer.setDealerPreferenceTypeCode(resultSet.getString("dealerPreferenceTypeCode"));
                    dealer.setUrl(resultSet.getString("url"));
                    dealer.setUrlFunctionCode(resultSet.getString("urlFunctionCode"));
                    dealer.setUrl2(resultSet.getString("url2"));
                    dealer.setUrlFunctionCode2(resultSet.getString("urlFunctionCode2"));
//                    dealer.setMentionLegale(resultSet.getString("mentionLegale"));

                    listResult.add(dealer);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error calling callGetDealerInfosDBS : " + e.getMessage());
        }

        return listResult;
    }

}
