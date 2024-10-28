package com.vgf.dbs.process.DBS_process.service;

import com.vgf.dbs.process.DBS_process.config.DBSConfig;
import com.vgf.dbs.process.DBS_process.model.*;
import com.vgf.dbs.process.DBS_process.util.DBSConstUtil;
import com.vgf.dbs.process.DBS_process.xmlModel.*;
import com.vgf.dbs.process.DBS_process.xmlModel.MdmDealerDetailDealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExportDBS {

    private static final Logger logger = Logger.getLogger(ExportDBS.class.getName());
    private String connectionString;
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
        List<ModelExportDealer> modelExportDealers = processService.callGetDealerInfosDBS(connectionString, processId);
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
        List<ModelExportDealer> dealerList = processService.callGetDealerInfosDBS(connectionString, processId);
        String exportDBSPath = configurationService.fetchConfigValue(DBSConstUtil.KEY_EXPORT_DBS_OUT_FOLDER);

        if (dealerList == null) {
            result = ResultState.Failed;
            return false;
        }

        // Example brands (or get them from a method like getListeMarque())
        String[] brands = {"Brand1", "Brand2"};
        List<ExportDetail> exportDetails = new ArrayList<>();

        for (String brand : brands) {
            exportDetails.add(new ExportDetail(brand, new ArrayList<>(), new ArrayList<>()));
        }

        if (!exportDetails.isEmpty()) {
            for (ModelExportDealer dealer : dealerList) {
                String[] departments = null;
                if (dealer.getDealerDivCode() != null) {
                    // Retrieve department list for this dealer division code
                    departments = getListeDepartment(dealer.getDealerDivCode());
                }

                MdmDealerDetailDealer dealerDetail = new MdmDealerDetailDealer();
                dealerDetail.setDealerTypeCode(dealer.getDealerTypeCode());

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
                MdmDealerDetailDealerAddress address = new MdmDealerDetailDealerAddress();
                address.setAddressLine1(dealer.getAddressLine1());
                address.setAddressLine2(dealer.getAddressLine2());
                address.setCityName(dealer.getCityName());
                address.setCountryCode(dealer.getCountryCode());
                address.setZipPostalCode(dealer.getZipPostalCode());
                address.setLatitude(dealer.getLatitude());
                address.setLongitude(dealer.getLongitude());

                // Retrieve departments for this dealer
                List<ModelExportDepartement> departmentList = processService.callGetDepartementInfosDBS(connectionString, processId, dealer.getDealerId());
                for (ModelExportDepartement dept : departmentList) {
                    MdmDealerDetailDealerDepartment department = new MdmDealerDetailDealerDepartment();
                    department.setDepartmentCode(dept.getDepartmentCode());
                    department.setContactName(dept.getContactName());

                    // Communication channels (email and phone)
                    MdmDealerDetailDealerDepartmentCommunicationChannels communicationChannels = new MdmDealerDetailDealerDepartmentCommunicationChannels();
                    communicationChannels.setEmail(dept.getEmail());

                    // Phones
                    MdmDealerDetailDealerDepartmentCommunicationChannelsPhone phone = new MdmDealerDetailDealerDepartmentCommunicationChannelsPhone();
                    phone.setPhoneCountryCode(dept.getPhoneCountryCode());
                    phone.setPhoneNumber(dept.getPhoneNumber());
                    communicationChannels.setPhones(new MdmDealerDetailDealerDepartmentCommunicationChannelsPhone[]{phone});
                    department.setCommunicationChannels(communicationChannels);

                    dealerDetail.getDepartments().add(department);
                }

                for (ExportDetail exportDetail : exportDetails) {
                    if ("ALL".equals(exportDetail.getBrand()) || exportDetail.getBrand().equals(dealer.getDealerDivCode())) {
                        exportDetail.getMdmDealerDetailDealers().add(dealerDetail);
                    }
                }
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

    // Placeholder method for getting brand list
    private String[] getListeMarque() {
        // Simulate getting the brand list
        return new String[] {"Brand1", "Brand2"};  // Example
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

