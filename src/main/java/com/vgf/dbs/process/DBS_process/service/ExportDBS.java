package com.vgf.dbs.process.DBS_process.service;

import com.vgf.dbs.process.DBS_process.config.DBSConfig;
import com.vgf.dbs.process.DBS_process.model.*;
import com.vgf.dbs.process.DBS_process.util.DBSConstUtil;
import com.vgf.dbs.process.DBS_process.xmlModel.*;
import com.vgf.dbs.process.DBS_process.xmlModel.MdmDealerDetailDealer;
import org.hibernate.boot.model.convert.internal.ConverterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ExportDBS {

    @Value("${spring.datasource.url}")
    private String connectionString;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String userPass;

    private static final Logger logger = Logger.getLogger(ExportDBS.class.getName());

    private String dealerCountryCodeDefault;
    private int processId;
    private ResultState result;  // Enum representing result states (e.g., SUCCESS, FAILED)

    @Autowired
    private ProcessService processService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private DBSConfig dbsConfig;

    // Placeholder enum for result states
    public enum ResultState {
        Success, Failed
    }

    // Placeholder method for calling the database or service to get dealer info
    private List<ModelExportDealer> callGetDealerInfosDBS(String connectionString, int processId) {
        List<ModelExportDealer> modelExportDealers = processService.callGetDealerInfosDBS(processId);
        logger.info("callGetDealerInfosDBS : {" + modelExportDealers.size() + "}");
        return modelExportDealers;
    }

    public String formateNomFichier(String pMarque, String pFileName) {

        // Replace placeholders in the filename
        pFileName = pFileName.replace("[FROMSTATION]", "GVF");
        pFileName = pFileName.replace("[TOSTATION]", "KTD");
        pFileName = pFileName.replace("[FORMAT]", "MDM");
        pFileName = pFileName.replace("[COUNTRYCODE]", dbsConfig.getCodePaysDefault());

        // Replace brand placeholder using the configuration service
        String key = DBSConstUtil.NOMCOURTMARQUE.replace("[BRAND]", pMarque);
        String nomcourt = configurationService.getConfigValue(connectionString, key);

        if (nomcourt != null) {
            return pFileName.replace("[BRAND]", nomcourt);
        }

        return pFileName.replace("[BRAND]", pMarque);
    }

    public String[] getListeDepartment(String pBrand) {
        // Replace brand placeholder in the configuration key
        String key = DBSConstUtil.FILTREDEPARTEMENT.replace("[BRAND]", pBrand);
        String lDepartment = configurationService.getConfigValue(connectionString, key);

        if (lDepartment != null) {
            return lDepartment.split(DBSConstUtil.SEPARATORDEPARTEMENT);
        }
        return null;
    }

    // Generate XML method
    public boolean generateXML() {
        boolean res = false;

        logger.info(String.format("Export DBS: Process created, ID = %d", processId));
        logger.info(String.format("Export DBS: Process (%d) - Start export to DBS", processId));

        // Fetch dealer information
        List<ModelExportDealer> dealerList = processService.callGetDealerInfosDBS(processId);
        String exportDBSPath = configurationService.fetchConfigValue(DBSConstUtil.KEY_EXPORT_DBS_OUT_FOLDER);

        if (dealerList == null) {
            result = ResultState.Failed;
            return false;
        }

        // Example brands (or get them from a method like getListeMarque())
        String[] brands = getListeMarque();
        List<ExportDetail> exportDetails = new ArrayList<>();

        for (String brand : brands) {
            exportDetails.add(new ExportDetail(brand, new ArrayList<>(), new ArrayList<>()));
        }


        if (!exportDetails.isEmpty()) {

            try(Connection connection = DriverManager.getConnection(connectionString, user, userPass)){
                for (ModelExportDealer dealer : dealerList) {
                    retrieveDealerInfo(connection, dealer, exportDetails);
                }
            } catch (SQLException ex) {
                logger.severe("Initialising Connection To Database Failed : " + ex.getMessage());
            }

        }

        // XML Generation and ZIP Creation
        try {
            for (ExportDetail exportDetail : exportDetails) {

                MdmDealerDetail mdmDealerDetail = new MdmDealerDetail();

                mdmDealerDetail.setDealers(exportDetail.getMdmDealerDetailDealers().toArray(new MdmDealerDetailDealer[0]));
                mdmDealerDetail.setMeta(exportDetail.getMdmDealerDetailMetas().toArray(new MdmDealerDetailMeta[0]));

                // Use JAXB for XML serialization
                JAXBContext jaxbContext = JAXBContext.newInstance(MdmDealerDetail.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                StringWriter writer = new StringWriter();
                marshaller.marshal(mdmDealerDetail, writer);

                // Post-processing the XML string to replace self-closing tags
                String xmlContent = writer.toString();
                xmlContent = xmlContent.replaceAll("<([a-zA-Z0-9]+) />", "<$1></$1>");

                // Write to ZIP file
                String zipFileName = formatFileName(exportDetail.getBrand(), DBSConstUtil.DEFAULT_EXPORT_DBS_ARCHIVE_NAME);
                Path zipFilePath = Paths.get(exportDBSPath, zipFileName);

                try (FileOutputStream fileOut = new FileOutputStream(zipFilePath.toFile());
                     ZipOutputStream zipOut = new ZipOutputStream(fileOut)) {

                    ZipEntry zipEntry = new ZipEntry(formatFileName(exportDetail.getBrand(), DBSConstUtil.DEFAULT_EXPORT_DBS_FILENAME));
                    zipOut.putNextEntry(zipEntry);
                    zipOut.write(xmlContent.getBytes(StandardCharsets.UTF_8));
                }

                res = true;
                result = ResultState.Success;
            }
        } catch (Exception e) {
            res = false;
            result = ResultState.Failed;
            logger.log(Level.SEVERE, "Error while generating XML or ZIP", e);
        }

        return res;
    }

    private void retrieveDealerInfo(Connection connection, ModelExportDealer dealer, List<ExportDetail> exportDetails) {
        String[] departments = null;
        if (dealer.getDealerDivCode() != null) {
            // Retrieve department list for this dealer division code
            departments = getListeDepartment(dealer.getDealerDivCode());
        }

        MdmDealerDetailDealer dealerDetail = new MdmDealerDetailDealer();
        dealerDetail.setDealerTypeCode(dealer.getDealerTypeCode());

        List<MdmDealerDetailDealerAddress> mdmDealerDetailDealerAddressList = new ArrayList<>();
        List<MdmDealerDetailDealerDepartment> mdmDealerDetailDealerDepartmentList = new ArrayList<>();

        List<MdmDealerDetailDealerDealerCategorization> mdmDealerDetailDealerDealerCategorizationList = new ArrayList<>();
        List<MdmDealerDetailDealerDealerIndicator> mdmDealerDetailDealerDealerIndicatorList = new ArrayList<>();
        List<MdmDealerDetailDealerDealerName> mdmDealerDetailDealerDealerNameList = new ArrayList<>();
        List<MdmDealerDetailDealerDealerStatus> mdmDealerDetailDealerDealerStatusList = new ArrayList<>();
        List<MdmDealerDetailDealerServiceOffered> mdmDealerDetailDealerServiceOfferedList = new ArrayList<>();
        List<MdmDealerDetailDealerDealerDate> mdmDealerDetailDealerDealerDateList = new ArrayList<>();


        // Set legal mentions or marketing allowances
        dealerDetail.setDealerMarketingAllowances(new MdmDealerDetailDealerDealerMarketingAllowances(
                dealer.getMentionLegale(), "", "", ""));

        if (dealer.getDealerId() != null && dealer.getDealerId().length() == 9) {
            dealerDetail.setDealerId(dealer.getDealerId().substring(4, 9));
        } else {
            dealerDetail.setDealerId(dealer.getDealerId());
        }

        if ("VW".equals(dealer.getDealerDivCode()) || "VU".equals(dealer.getDealerDivCode())) {
            dealerDetail.setDealerCountryCode(dealerCountryCodeDefault);
        } else {
            dealerDetail.setDealerCountryCode(dealer.getDealerCountryCode());
        }

        String headerCode = DBSConstUtil.CODEENTETEMARQUE.replace("[BRAND]", dealer.getDealerDivCode());
        dealerDetail.setDealerDivCode(configurationService.getConfigValue(connectionString, headerCode));

        dealerDetail.setUpdateDate(dealer.getUpdateDate());
        dealerDetail.setDealerLanguageCode(dealer.getDealerLanguageCode());
        dealerDetail.setDealerMessage(dealer.getDealerMessage());

        // Address details
        MdmDealerDetailDealerAddress address = getMdmDealerDetailDealerAddress(dealer);
        mdmDealerDetailDealerAddressList.add(address);

        // Retrieve departments for this dealer
        retrieveDepartments(connection, dealer, dealerDetail, mdmDealerDetailDealerDepartmentList, mdmDealerDetailDealerAddressList);

        // Categorization
        MdmDealerDetailDealerDealerCategorization mdmDealerDetailDealerDealerCategorization = new MdmDealerDetailDealerDealerCategorization();
        mdmDealerDetailDealerDealerCategorization.setCategorizationCode(dealer.getCategorizationCode());
        mdmDealerDetailDealerDealerCategorization.setCategorizationTypeCode(dealer.getCategorizationTypeCode());

        mdmDealerDetailDealerDealerCategorizationList.add(mdmDealerDetailDealerDealerCategorization);

        MdmDealerDetailDealerDealerIndicator mdmDealerDetailDealerDealerIndicator = new MdmDealerDetailDealerDealerIndicator();
        // mdmDealerDetailDealerDealerIndicator.setIndicatorTypeCode(item.getIndicatorTypeCode());

        mdmDealerDetailDealerDealerIndicatorList.add(mdmDealerDetailDealerDealerIndicator);

        List<ModelExportDealerService> listResultDealerServices = processService.callGetDealerServicesDBS(connection, processId, dealer.getDealerId());
        for (ModelExportDealerService service : listResultDealerServices) {
            MdmDealerDetailDealerServiceOffered mdmDealerDetailDealerServiceOffered = new MdmDealerDetailDealerServiceOffered();
            mdmDealerDetailDealerServiceOffered.setServiceTypeCode(service.getServiceTypeCode());

            mdmDealerDetailDealerServiceOfferedList.add(mdmDealerDetailDealerServiceOffered);
        }

        MdmDealerDetailDealerDealerDate mdmDealerDetailDealerDealerDate = new MdmDealerDetailDealerDealerDate();
        mdmDealerDetailDealerDealerDate.setDateTypeCode(dealer.getDateTypeCode());
        if (dealer.getDealerDateData() != null && !dealer.getDealerDateData().isEmpty()) {
            mdmDealerDetailDealerDealerDate.setDealerDateData(LocalDate.parse(dealer.getDealerDateData()));
        }
        mdmDealerDetailDealerDealerDateList.add(mdmDealerDetailDealerDealerDate);

        if (dealer.getDealerDateDataFin() != null && !dealer.getDealerDateDataFin().isEmpty()) {
            mdmDealerDetailDealerDealerDate = new MdmDealerDetailDealerDealerDate();
            mdmDealerDetailDealerDealerDate.setDateTypeCode(dealer.getDateTypeCodeFin());
            mdmDealerDetailDealerDealerDate.setDealerDateData(LocalDate.parse(dealer.getDealerDateDataFin()));
            mdmDealerDetailDealerDealerDateList.add(mdmDealerDetailDealerDealerDate);
        }

        MdmDealerDetailDealerDealerStatus mdmDealerDetailDealerDealerStatus = new MdmDealerDetailDealerDealerStatus();
        mdmDealerDetailDealerDealerStatus.setStatusCode(dealer.getStatusCode());
        mdmDealerDetailDealerDealerStatus.setStatusTypeCode(dealer.getStatusTypeCode());
        mdmDealerDetailDealerDealerStatusList.add(mdmDealerDetailDealerDealerStatus);

        mdmDealerDetailDealerDealerStatus = new MdmDealerDetailDealerDealerStatus();
        mdmDealerDetailDealerDealerStatus.setStatusCode(dealer.getStatusCode2());
        mdmDealerDetailDealerDealerStatus.setStatusTypeCode(dealer.getStatusTypeCode2());
        mdmDealerDetailDealerDealerStatusList.add(mdmDealerDetailDealerDealerStatus);

        dealerDetail.setServicesOffered(mdmDealerDetailDealerServiceOfferedList);
        dealerDetail.setDealerCategorizations(mdmDealerDetailDealerDealerCategorizationList);
        dealerDetail.setDealerStatuses(mdmDealerDetailDealerDealerStatusList);
        dealerDetail.setDealerDates(mdmDealerDetailDealerDealerDateList);
        dealerDetail.setDealerIndicators(mdmDealerDetailDealerDealerIndicatorList);

        for (ExportDetail exportDetail : exportDetails) {
            if ("ALL".equals(exportDetail.getBrand()) || exportDetail.getBrand().equals(dealer.getDealerDivCode())) {
                exportDetail.getMdmDealerDetailDealers().add(dealerDetail);
            }
        }
    }

    private static MdmDealerDetailDealerAddress getMdmDealerDetailDealerAddress(ModelExportDealer dealer) {
        MdmDealerDetailDealerAddress address = new MdmDealerDetailDealerAddress();
        address.setAddressLine1(dealer.getAddressLine1());
        address.setAddressLine2(dealer.getAddressLine2());
        address.setCityName(dealer.getCityName());
        address.setCountryCode(dealer.getCountryCode());
        address.setZipPostalCode(dealer.getZipPostalCode());
        address.setLatitude(dealer.getLatitude());
        address.setLongitude(dealer.getLongitude());
        address.setLongitudeSpecified(true);
        address.setStateProvinceCode("");
        address.setZipcodeSuffix(dealer.getZipcodeSuffix());
        address.setZipPostalCode(dealer.getZipPostalCode());
        return address;
    }

    private void retrieveDepartments(Connection connection, ModelExportDealer dealer, MdmDealerDetailDealer dealerDetail, List<MdmDealerDetailDealerDepartment> mdmDealerDetailDealerDepartmentList, List<MdmDealerDetailDealerAddress> mdmDealerDetailDealerAddresses) {

        List<ModelExportDepartement> departmentList = processService.callGetDepartementInfosDBS(connection, processId, dealer.getDealerId());

        boolean isOnlyFRASVCDepartement = false;

        for (ModelExportDepartement dept : departmentList) {

            if (departmentList.size() == 1 && "SERVICE".equals(dept.getDepartmentCode())) {
                isOnlyFRASVCDepartement = true;
            }

            MdmDealerDetailDealerDepartment department = new MdmDealerDetailDealerDepartment();
            department.setDepartmentCode(dept.getDepartmentCode());
            department.setContactName(dept.getContactName());

            // Communication channels (email and phone)
            MdmDealerDetailDealerDepartmentCommunicationChannels communicationChannels = new MdmDealerDetailDealerDepartmentCommunicationChannels();
            communicationChannels.setEmail(dept.getEmail());

            // Phones
            extractDealerDetailPhoneCommunication(dept, department, communicationChannels);

            /**********************/
            /****** HORAIRES ******/
            /**********************/

            MdmDealerDetailDealerDepartmentHoursOfOperations mdmDealerDetailDealerDepartmentHoursOfOperations = new MdmDealerDetailDealerDepartmentHoursOfOperations();
            // mdmDealerDetailDealerDepartmentHoursOfOperations.setHoursNote(""); // Uncomment if needed
            mdmDealerDetailDealerDepartmentHoursOfOperations.setDays(getHoraires(connection, dealer.getDealerId(), dept.getDepartmentCode()));
            department.setHoursOfOperations(mdmDealerDetailDealerDepartmentHoursOfOperations);
            mdmDealerDetailDealerDepartmentList.add(department);

            /********************/
            /****** Address ******/
            /********************/

            extractDealerDetailAddress(mdmDealerDetailDealerAddresses, dept);

            dealerDetail.getDepartments().add(department);
            dealerDetail.setAddresses(mdmDealerDetailDealerAddresses);
        }

    }

    private void extractDealerDetailPhoneCommunication(ModelExportDepartement dept, MdmDealerDetailDealerDepartment department, MdmDealerDetailDealerDepartmentCommunicationChannels communicationChannels) {
        MdmDealerDetailDealerDepartmentCommunicationChannelsPhone phone = new MdmDealerDetailDealerDepartmentCommunicationChannelsPhone();
        phone.setPhoneCountryCode(dept.getPhoneCountryCode());
        phone.setPhoneNumber(dept.getPhoneNumber());
        phone.setAreaCode(dept.getAreaCode());
        phone.setPhoneNumber(dept.getPhoneNumber());
        phone.setFullPhoneNumber(dept.getFullPhoneNumber());
        phone.setPhoneTypeCode(dept.getPhoneTypeCode());

        department.setCommunicationChannels(communicationChannels);

        List<MdmDealerDetailDealerDepartmentCommunicationChannelsPhone> communicationChannelsPhoneList = new ArrayList<>();
        communicationChannelsPhoneList.add(phone);

        communicationChannels.setPhones(communicationChannelsPhoneList);
        department.setCommunicationChannels(communicationChannels);
    }

    private void extractDealerDetailAddress(List<MdmDealerDetailDealerAddress> mdmDealerDetailDealerAddresses, ModelExportDepartement dept) {
        MdmDealerDetailDealerAddress mdmDealerDetailDealerAddress = new MdmDealerDetailDealerAddress();
        mdmDealerDetailDealerAddress.setAddressLine1(dept.getAddressLine1());
        mdmDealerDetailDealerAddress.setAddressLine2(dept.getAddressLine2());
        mdmDealerDetailDealerAddress.setAddressTypeCode(dept.getAddressTypeCode());
        mdmDealerDetailDealerAddress.setCityName(dept.getCityName());
        mdmDealerDetailDealerAddress.setCountryCode(dept.getCountryCode());
        mdmDealerDetailDealerAddress.setDepartmentCode(dept.getDepartmentCode());
        mdmDealerDetailDealerAddress.setLatitude(Double.parseDouble(dept.getLatitude()));
        mdmDealerDetailDealerAddress.setLatitudeSpecified(true);
        mdmDealerDetailDealerAddress.setLongitude(Double.parseDouble(dept.getLongitude()));
        mdmDealerDetailDealerAddress.setLongitudeSpecified(true);
        mdmDealerDetailDealerAddress.setStateProvinceCode(""); // Setting empty string
        mdmDealerDetailDealerAddress.setZipcodeSuffix(dept.getZipcodeSuffix());
        mdmDealerDetailDealerAddress.setZipPostalCode(dept.getZipPostalCode());

        mdmDealerDetailDealerAddresses.add(mdmDealerDetailDealerAddress);
    }

    // Placeholder method for getting brand list
    private String[] getListeMarque() {

        String lmarque = configurationService.fetchConfigValue(DBSConstUtil.EXPORTMARQUE);
        return lmarque.split(DBSConstUtil.SEPARATORMARQUE);
    }

    private List<MdmDealerDetailDealerDepartmentHoursOfOperationsDays> getHoraires(Connection connection, String dealerId, String departementCode) {
        List<MdmDealerDetailDealerDepartmentHoursOfOperationsDays> daysList = new ArrayList<>();

        List<ModelExportHoraire> horairesList = processService.callGetHoraireDBS(connection, processId, dealerId, departementCode);

        if (horairesList.isEmpty()) {
            String openTime = "08:30";
            String closeTime = "19:00";

            // Default days and their configurations
            String[] dayCodes = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
            String[] closedIndicators = {"N", "N", "N", "N", "N", "N", "Y"};

            for (int i = 0; i < dayCodes.length; i++) {
                MdmDealerDetailDealerDepartmentHoursOfOperationsDays day = new MdmDealerDetailDealerDepartmentHoursOfOperationsDays();
                day.setClosedIndicator(closedIndicators[i]);
                day.setDayOfWeekCode(dayCodes[i]);

                if (!"Y".equals(closedIndicators[i])) {
                    List<MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours> hoursList = new ArrayList<>();
                    MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours hours = new MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours();
                    hours.setOpenTime(openTime);
                    hours.setCloseTime(closeTime);
                    hoursList.add(hours);
                    day.setHours(hoursList.toArray(new MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours[0]));
                }

                daysList.add(day);
            }
        } else {
            // Dictionary to maintain order of days
            Map<String, Integer> dayOrder = Map.of(
                    "MO", 1, "TU", 2, "WE", 3, "TH", 4, "FR", 5, "SA", 6, "SU", 7
            );

            // Sort horairesList based on the day order
            horairesList.sort(Comparator.comparingInt(horaire -> dayOrder.get(horaire.getDayOfWeekCode())));

            for (ModelExportHoraire element : horairesList) {
                MdmDealerDetailDealerDepartmentHoursOfOperationsDays day = new MdmDealerDetailDealerDepartmentHoursOfOperationsDays();
                day.setClosedIndicator(element.getClosedIndicator());
                day.setDayOfWeekCode(element.getDayOfWeekCode());

                if ("N".equals(element.getClosedIndicator())) {
                    List<MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours> hoursList = new ArrayList<>();

                    if (element.getHR_DEBUT_2() == null || element.getHR_FIN_2() == null) {
                        MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours hours = new MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours();
                        hours.setOpenTime(element.getHR_DEBUT_1());
                        hours.setCloseTime(element.getHR_FIN_1());
                        hoursList.add(hours);
                    } else {
                        MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours hours1 = new MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours();
                        hours1.setOpenTime(element.getHR_DEBUT_1());
                        hours1.setCloseTime(element.getHR_FIN_1());
                        hoursList.add(hours1);

                        MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours hours2 = new MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours();
                        hours2.setOpenTime(element.getHR_DEBUT_2());
                        hours2.setCloseTime(element.getHR_FIN_2());
                        hoursList.add(hours2);
                    }

                    day.setHours(hoursList.toArray(new MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours[0]));
                }

                daysList.add(day);
            }
        }

        return daysList;
    }


    public boolean beginProcess() {
        boolean result = false;

        try {
            // Log the start of the process
            System.out.println("Starting the export process...");

            // Call some internal methods or steps (these could be methods like generateXML or database operations)
            result = generateXML();

            // Log the result of the process
            if (result) {
                System.out.println("Export process completed successfully.");
            } else {
                System.out.println("Export process failed.");
            }

        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            System.err.println("Error during export process: " + e.getMessage());

            logger.severe("Error during export process: " + e.getMessage());
        }

        return result;
    }

    public String formatFileName(String marque, String fileName) {

        fileName = fileName.replace("[FROMSTATION]", "GVF");
        fileName = fileName.replace("[TOSTATION]", "KTD");
        fileName = fileName.replace("[FORMAT]", "MDM");
        fileName = fileName.replace("[COUNTRYCODE]", dbsConfig.getCodePaysDefault());

        String key = DBSConstUtil.NOMCOURTMARQUE.replace("[BRAND]", marque);
        String nomcourt = configurationService.getConfigValue(connectionString, key);

        if (nomcourt != null) {
            return fileName.replace("[BRAND]", nomcourt);
        }
        return fileName.replace("[BRAND]", marque);
    }

}

