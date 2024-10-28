package com.vgf.dbs.process.DBS_process.service;


import com.vgf.dbs.process.DBS_process.model.*;
import org.hibernate.dialect.OracleTypes;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProcessService {

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
    public int initializeNewProcess(String connectionString, String type) {
        int processId = -1;

        try (Connection connection = DriverManager.getConnection(connectionString);
             CallableStatement stmt = connection.prepareCall("{call PKG_DBS.SP_CREATE_PROCESS(?, ?)}")) {

            // Set input parameter
            stmt.setString(1, type);

            // Register output parameter
            stmt.registerOutParameter(2, Types.INTEGER);

            // Execute the stored procedure
            stmt.execute();

            // Retrieve the output parameter (processId)
            processId = stmt.getInt(2);

        } catch (SQLException ex) {
            logger.severe("Error initializing new process: " + ex.getMessage());
        }

        return processId;
    }

    public List<ModelExportDepartement> callGetDepartementInfosDBS(String connectionString, int processId, String dealrID) {
        List<ModelExportDepartement> listResult = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(connectionString);
             CallableStatement stmt = connection.prepareCall("{call PKG_DBS.SP_GetAllDepartementInfos(?, ?, ?)}")) {

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

    public List<ModelExportDealerService> callGetDealerServicesDBS(String connectionString, int processId) {
        List<ModelExportDealerService> listResult = new ArrayList<>();

        String sql = "SELECT * FROM DEALER_SERVICES WHERE PROCESS_ID = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, processId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ModelExportDealerService service = new ModelExportDealerService();
                service.setServiceTypeCode(rs.getString("SERVICE_TYPE_CODE"));
                // Map other fields if needed
                listResult.add(service);
            }

        } catch (SQLException ex) {
            logger.severe("Error fetching dealer services: " + ex.getMessage());
        }

        return listResult;
    }

    public List<ModelExportHoraire> callGetHoraireDBS(String connectionString, int processId) {
        List<ModelExportHoraire> listResult = new ArrayList<>();

        String sql = "SELECT * FROM DEALER_HORAIRES WHERE PROCESS_ID = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, processId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ModelExportHoraire horaire = new ModelExportHoraire();
                horaire.setDayOfWeekCode(rs.getString("DAY_OF_WEEK_CODE"));
                horaire.setClosedIndicator(rs.getString("CLOSED_INDICATOR"));
                // Map other fields
                listResult.add(horaire);
            }

        } catch (SQLException ex) {
            logger.severe("Error fetching dealer schedules: " + ex.getMessage());
        }

        return listResult;
    }

    public List<ModelExportTypeCode> callGetTypeCodesDBS(String connectionString, int processId) {
        List<ModelExportTypeCode> listResult = new ArrayList<>();

        String sql = "SELECT * FROM DEALER_TYPE_CODES WHERE PROCESS_ID = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, processId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ModelExportTypeCode typeCode = new ModelExportTypeCode();
                typeCode.setTypeCodeCategory(rs.getString("TYPE_CODE_CATEGORY"));
                typeCode.setTypeCodeValue(rs.getString("TYPE_CODE_VALUE"));
                typeCode.setTypeCodeDesc(rs.getString("TYPE_CODE_DESC"));
                // Map other fields
                listResult.add(typeCode);
            }

        } catch (SQLException ex) {
            logger.severe("Error fetching dealer type codes: " + ex.getMessage());
        }

        return listResult;
    }

    public List<ModelExportDealer> callGetDealerInfosDBS(String connectionString, int processId) {
        List<ModelExportDealer> listResult = new ArrayList<>();

        String sql = "SELECT * FROM DEALERS WHERE PROCESS_ID = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, processId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ModelExportDealer dealer = new ModelExportDealer();
                dealer.setDealerId(rs.getString("DEALER_ID"));
                dealer.setDealerCountryCode(rs.getString("DEALER_COUNTRY_CODE"));
                dealer.setDealerDivCode(rs.getString("DEALER_DIV_CODE"));
                //dealer.setUpdateDate(rs.getString("UPDATE_DATE"));
                dealer.setDealerTypeCode(rs.getString("DEALER_TYPE_CODE"));
                // Map other fields
                listResult.add(dealer);
            }

        } catch (SQLException ex) {
            logger.severe("Error fetching dealer info: " + ex.getMessage());
        }

        return listResult;
    }

}
