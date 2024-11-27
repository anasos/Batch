package com.vgf.dbs.process.DBS_process.service;

import com.vgf.dbs.process.DBS_process.config.DBSConfig;
import com.vgf.dbs.process.DBS_process.model.*;
import com.vgf.dbs.process.DBS_process.util.DBSConstUtil;
import com.vgf.dbs.process.DBS_process.util.FormatUtil;
import com.vgf.dbs.process.DBS_process.xmlModel.MdmDealerDetailDealer;
import com.vgf.dbs.process.DBS_process.xmlModel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ExportDBS {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    @Value("${spring.datasource.url}")
    private String connectionString;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String userPass;

    private List<MdmDealerDetailDealerDepartment> mdmDealerDetailDealerDepartmentList = new ArrayList<>();

    private List<MdmDealerDetailDealerAddress> mdmDealerDetailDealerAddressList = new ArrayList<>();

    private List<MdmDealerDetailDealerDealerCategorization> mdmDealerDetailDealerDealerCategorizationList = new ArrayList<>();

    private List<MdmDealerDetailDealerDealerIndicator> mdmDealerDetailDealerDealerIndicatorList = new ArrayList<>();

    private List<MdmDealerDetailDealerDealerPreference> mdmDealerDetailDealerDealerPreferenceList = new ArrayList<>();

    private List<MdmDealerDetailDealerDealerStatus> mdmDealerDetailDealerDealerStatusList = new ArrayList<>();

    private List<MdmDealerDetailDealerServiceOffered> mdmDealerDetailDealerServiceOfferedList = new ArrayList<>();

    private List<MdmDealerDetailDealerRelatedDealer> mdmDealerDetailDealerRelatedDealerList = new ArrayList<>();

    private List<MdmDealerDetailDealerDealerUrl> mdmDealerDetailDealerDealerUrlList = new ArrayList<>();

    private List<MdmDealerDetailDealerDealerDate> mdmDealerDetailDealerDealerDateList = new ArrayList<>();

    private List<MdmDealerDetailDealerDealerName> mdmDealerDetailDealerDealerNameList = new ArrayList<>();

    private static final Logger logger = Logger.getLogger(ExportDBS.class.getName());

    private String dealerCountryCodeDefault;

    private String codePaysDefault;

    private int processId;

    boolean isOnlyFRASVCDepartement = false;

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

    public String formateNomFichier(Connection connection, String pMarque, String pFileName) {

        // Replace placeholders in the filename
        pFileName = pFileName.replace("[FROMSTATION]", "GVF");
        pFileName = pFileName.replace("[TOSTATION]", "KTD");
        pFileName = pFileName.replace("[FORMAT]", "MDM");
        pFileName = pFileName.replace("[COUNTRYCODE]", codePaysDefault);

        // Replace brand placeholder using the configuration service
        String key = DBSConstUtil.NOMCOURTMARQUE.replace("[BRAND]", pMarque);
        String nomcourt = configurationService.getConfigValue(connection, key);

        if (nomcourt != null) {
            return pFileName.replace("[BRAND]", nomcourt);
        }

        return pFileName.replace("[BRAND]", pMarque);
    }

    public String[] getListeDepartment(Connection connection, String pBrand) {
        // Replace brand placeholder in the configuration key
        String key = DBSConstUtil.FILTREDEPARTEMENT.replace("[BRAND]", pBrand);
        String lDepartment = configurationService.getConfigValue(connection, key);

        if (lDepartment != null) {
            return lDepartment.split(DBSConstUtil.SEPARATORDEPARTEMENT);
        }
        return null;
    }

    // Generate XML method
    public boolean generateXML() {
        boolean res = false;

        if(!canExecute())
            return res;

        logger.info(String.format("Export DBS: Process created, ID = %d", processId));
        logger.info(String.format("Export DBS: Process (%d) - Start export to DBS", processId));

        // Fetch dealer information
        List<ModelExportDealer> dealerList = processService.callGetDealerInfosDBS(processId);

        String exportDBSPath = configurationService.fetchConfigValue(DBSConstUtil.KEY_EXPORT_DBS_OUT_FOLDER);
        dealerCountryCodeDefault = configurationService.fetchConfigValue(DBSConstUtil.KEY_DEALER_COUNTRY_CODE_DEFAULT);
        codePaysDefault = configurationService.fetchConfigValue(DBSConstUtil.KEY_CODE_PAYS_DEFAULT);

        if (codePaysDefault == null)
            codePaysDefault = "FRX";

        if (dealerCountryCodeDefault == null)
            dealerCountryCodeDefault = "FRA";

        if (dealerList == null) {
            result = ResultState.Failed;
            logger.info("Dealer list null ");
            return false;
        }

        logger.info("Dealer list size : [" + dealerList.size() + "]");

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

                retireveMetaInfo(exportDetails, connection);

            } catch (SQLException ex) {
                logger.severe("Initialising Connection To Database Failed : " + ex.getMessage());
            }

        }

        // XML Generation and ZIP Creation
        try(Connection connection = DriverManager.getConnection(connectionString, user, userPass)){

            logger.info("Start XML generation");

            for (ExportDetail exportDetail : exportDetails) {

                MdmDealerDetail mdmDealerDetail = new MdmDealerDetail();

                mdmDealerDetail.setDealers(exportDetail.getMdmDealerDetailDealers().toArray(new MdmDealerDetailDealer[0]));
                mdmDealerDetail.setMeta(exportDetail.getMdmDealerDetailMetas().toArray(new MdmDealerDetailMeta[0]));

                // Use JAXB for XML serialization
                JAXBContext jaxbContext = JAXBContext.newInstance(MdmDealerDetail.class);
                Marshaller marshaller = jaxbContext.createMarshaller();

                // Set xml file header
                marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-16");
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//
//                StringWriter writer = new StringWriter();
//                marshaller.marshal(mdmDealerDetail, writer);
//
//                // Post-processing the XML string to replace self-closing tags
//                String xmlContent = writer.toString();
//                xmlContent = xmlContent.replaceAll("<([a-zA-Z0-9]+) />", "<$1></$1>");

                // Marshal object to a DOM Document
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.newDocument();
                marshaller.marshal(mdmDealerDetail, document);

                // Transform the DOM Document to a String with 2-space indentation
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();

                // Set transformer properties
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); // Set to 2 spaces

                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(document), new StreamResult(writer));

                String xmlContent = writer.toString();
                xmlContent = xmlContent.replaceAll("<([a-zA-Z0-9]+) />", "<$1></$1>");

                // Write to ZIP file

                logger.info("Start ZIP creation");

                String zipFileName = formatFileName(connection, exportDetail.getBrand(), DBSConstUtil.DEFAULT_EXPORT_DBS_ARCHIVE_NAME);

                logger.info("ZIP filename : [" + zipFileName + "]");

                Path zipFilePath = Paths.get(exportDBSPath, zipFileName);

                try (FileOutputStream fileOut = new FileOutputStream(zipFilePath.toFile());
                     ZipOutputStream zipOut = new ZipOutputStream(fileOut)) {

                    ZipEntry zipEntry = new ZipEntry(formatFileName(connection, exportDetail.getBrand(), DBSConstUtil.DEFAULT_EXPORT_DBS_FILENAME));
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

    private void retireveMetaInfo(List<ExportDetail> exportDetails, Connection connection) {
        for (ExportDetail exportDetail : exportDetails) {
            String[] departmentList = null;

            // If not for all brands, filter the department (exclusion)
            if (!"ALL".equals(exportDetail.getBrand())) {
                // Retrieve the list of departments
                departmentList = getListeDepartment(connection, exportDetail.getBrand());
            }

            MdmDealerDetailMeta mdmDealerDetailMeta = new MdmDealerDetailMeta();
            List<MdmDealerDetailMetaTypeCode> typeCodeList = new ArrayList<>();

            List<ModelExportTypeCode> typeCodes = processService.callGetTypeCodesDBS(connection, processId);

            for (ModelExportTypeCode typeCode : typeCodes) {

                boolean isPresentInDepartmentList = false;

                if(departmentList != null){
                    isPresentInDepartmentList = Arrays.stream(departmentList).anyMatch(element -> element.equals(typeCode.getTypeCodeValue()));
                }

                if ("departmentCode".equals(typeCode.getTypeCodeCategory()) &&
                        (departmentList == null || !isPresentInDepartmentList)) {

                    MdmDealerDetailMetaTypeCode metaTypeCode = new MdmDealerDetailMetaTypeCode();
                    metaTypeCode.setTypeCodeCategory(typeCode.getTypeCodeCategory());
                    metaTypeCode.setTypeCodeValue(typeCode.getTypeCodeValue());
                    metaTypeCode.setTypeCodeDesc(typeCode.getTypeCodeDesc());

                    String parentTypeCodeValue = typeCode.getParentTypeCodeValue() != null ? typeCode.getParentTypeCodeValue() : "";
                    metaTypeCode.setParentTypeCodeValue(parentTypeCodeValue);

                    typeCodeList.add(metaTypeCode);
                }
            }

            mdmDealerDetailMeta.setTypeCodes(typeCodeList);

            // Set the number of dealers and timestamp
            mdmDealerDetailMeta.setNumberOfDealers(String.valueOf(exportDetail.getMdmDealerDetailDealers().size()));

            Date now = new Date();
            mdmDealerDetailMeta.setExportTimeStamp(now);

            exportDetail.getMdmDealerDetailMetas().add(mdmDealerDetailMeta);
        }
    }

    private void retrieveDealerInfo(Connection connection, ModelExportDealer dealer, List<ExportDetail> exportDetails) {
        String[] departments = null;
        List<String> lDepartments = new ArrayList<>();

        if (dealer.getDealerDivCode() != null) {
            // Retrieve department list for this dealer division code
            departments = getListeDepartment(connection, dealer.getDealerDivCode());

            if(departments != null)
                lDepartments = new ArrayList<>(Arrays.asList(departments));
        }

        MdmDealerDetailDealer dealerDetail = new MdmDealerDetailDealer();
        dealerDetail.setDealerTypeCode(dealer.getDealerTypeCode());

        mdmDealerDetailDealerAddressList = new ArrayList<>();
        mdmDealerDetailDealerDepartmentList = new ArrayList<>();

        mdmDealerDetailDealerDealerCategorizationList = new ArrayList<>();
        mdmDealerDetailDealerDealerIndicatorList = new ArrayList<>();
        mdmDealerDetailDealerDealerNameList = new ArrayList<>();
        mdmDealerDetailDealerDealerStatusList = new ArrayList<>();
        mdmDealerDetailDealerServiceOfferedList = new ArrayList<>();
        mdmDealerDetailDealerDealerDateList = new ArrayList<>();
        mdmDealerDetailDealerDealerPreferenceList = new ArrayList<>();
        mdmDealerDetailDealerRelatedDealerList = new ArrayList<>();
        mdmDealerDetailDealerDealerUrlList = new ArrayList<>();

        String mentionLegale = dealer.getMentionLegale() != null ? dealer.getMentionLegale() : "";

        // Set legal mentions or marketing allowances
        dealerDetail.setDealerMarketingAllowances(new MdmDealerDetailDealerDealerMarketingAllowances(
                mentionLegale, "", "", ""));

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
        dealerDetail.setDealerDivCode(configurationService.getConfigValue(connection, headerCode));

        dealerDetail.setUpdateDate(dealer.getUpdateDate());
        dealerDetail.setDealerLanguageCode(dealer.getDealerLanguageCode());
        dealerDetail.setDealerMessage(dealer.getDealerMessage());

        // Address details
        MdmDealerDetailDealerAddress address = getMdmDealerDetailDealerAddress(dealer);
        mdmDealerDetailDealerAddressList.add(address);

        // Retrieve departments for this dealer
        retrieveDepartments(connection, dealer, dealerDetail, lDepartments );

        // Categorization

        retrieveDealerCategorizations(dealer);

        MdmDealerDetailDealerDealerIndicator mdmDealerDetailDealerDealerIndicator = new MdmDealerDetailDealerDealerIndicator();
        // mdmDealerDetailDealerDealerIndicator.setIndicatorTypeCode(item.getIndicatorTypeCode());

        mdmDealerDetailDealerDealerIndicatorList.add(mdmDealerDetailDealerDealerIndicator);

        retrieveDealerServices(connection, dealer);

        retrieveDealerDate(dealer);

        retrieveDealerStatus(dealer);

        retrieveDealerNames(dealer);

        // Ajouter les données pour RelatedDealer
        MdmDealerDetailDealerRelatedDealer relatedDealer = new MdmDealerDetailDealerRelatedDealer();

//       Décommenter si nécessaire et définir les valeurs :
//       relatedDealer.setRelatedDealerId(dealer.getDealerId());
//       relatedDealer.setRelationshipTypeCode(dealer.getDepartmentCode());

        mdmDealerDetailDealerRelatedDealerList.add(relatedDealer);

        // Ajouter les données pour DealerPreference
        retrieveDealerPreferences(dealer);

        // URL of the SVC service website, defaulting to "www.volkswagen.fr" if not present
        retrieveDealerUrl(connection, dealer);

        dealerDetail.setServicesOffered(mdmDealerDetailDealerServiceOfferedList);
        dealerDetail.setDealerCategorizations(mdmDealerDetailDealerDealerCategorizationList);
        dealerDetail.setDealerStatuses(mdmDealerDetailDealerDealerStatusList);
        dealerDetail.setDealerDates(mdmDealerDetailDealerDealerDateList);
        dealerDetail.setDealerIndicators(mdmDealerDetailDealerDealerIndicatorList);
        dealerDetail.setDealerNames(mdmDealerDetailDealerDealerNameList);
        dealerDetail.setDealerUrls(mdmDealerDetailDealerDealerUrlList);
        dealerDetail.setRelatedDealers(mdmDealerDetailDealerRelatedDealerList);
        dealerDetail.setDealerPreferences(mdmDealerDetailDealerDealerPreferenceList);

        for (ExportDetail exportDetail : exportDetails) {

            if (("ALL".equals(exportDetail.getBrand()) && !"SK".equals(dealer.getDealerDivCode())) || exportDetail.getBrand().equals(dealer.getDealerDivCode())) {
                exportDetail.getMdmDealerDetailDealers().add(dealerDetail);
            }
        }
    }

    private void retrieveDealerUrl(Connection connection, ModelExportDealer dealer) {

        // URL of the SVC service website, defaulting to "www.volkswagen.fr" if not present
        MdmDealerDetailDealerDealerUrl mdmDealerDetailDealerDealerUrl = new MdmDealerDetailDealerDealerUrl();

        String siteWeb = DBSConstUtil.SITEWEBDEFAUTMARQUE;
        siteWeb = siteWeb.replace("[BRAND]", dealer.getDealerDivCode());

        String mdmUrl = configurationService.getConfigValue(connection, siteWeb);
        mdmUrl = mdmUrl != null ? mdmUrl:"";
        mdmDealerDetailDealerDealerUrl.setUrl(mdmUrl);

        mdmDealerDetailDealerDealerUrl.setUrlFunctionCode(dealer.getUrlFunctionCode2());
        mdmDealerDetailDealerDealerUrlList.add(mdmDealerDetailDealerDealerUrl);

        if (dealer.getUrl() != null && !dealer.getUrl().isEmpty()) {

            mdmDealerDetailDealerDealerUrl = new MdmDealerDetailDealerDealerUrl();

            String dealerUrl = dealer.getUrl() != null ? dealer.getUrl():"";
            mdmDealerDetailDealerDealerUrl.setUrl(dealerUrl);

            mdmDealerDetailDealerDealerUrl.setUrlFunctionCode(dealer.getUrlFunctionCode());
            mdmDealerDetailDealerDealerUrlList.add(mdmDealerDetailDealerDealerUrl);
        }

    }

    private void retrieveDealerPreferences(ModelExportDealer dealer) {

        MdmDealerDetailDealerDealerPreference dealerPreference = new MdmDealerDetailDealerDealerPreference();
        dealerPreference.setDealerPreferenceTypeCode(dealer.getDealerPreferenceTypeCode());

        List<MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails> listMdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails = new ArrayList<>();
        MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails = new MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails();

        if (!isOnlyFRASVCDepartement) {

            mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails = new MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails();
            mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails.setDealerPreferencetDetail(dealer.getDealerPreferencetDetail());
            listMdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails.add(mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails);

            mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails = new MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails();
            mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails.setDealerPreferencetDetail(dealer.getDealerPreferencetDetail2());
            listMdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails.add(mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails);

            mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails = new MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails();
            mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails.setDealerPreferencetDetail(dealer.getDealerPreferencetDetail3());
            listMdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails.add(mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails);

        }

        // Create a new instance of MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails
        mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails = new MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails();
        mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails.setDealerPreferencetDetail(dealer.getDealerPreferencetDetail4());
        listMdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails.add(mdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails);

        // Convert the list to an array and set it in the MdmDealerDetailDealerDealerPreference object
        dealerPreference.setDealerPreferenceDetails(listMdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails);

        mdmDealerDetailDealerDealerPreferenceList.add(dealerPreference);
    }

    private void retrieveDealerNames(ModelExportDealer dealer) {
        MdmDealerDetailDealerDealerName dealerName = new MdmDealerDetailDealerDealerName();
        dealerName.setDealerNameData(dealer.getDealerNameData());
        dealerName.setNameTypeCode(dealer.getNameTypeCode());
        mdmDealerDetailDealerDealerNameList.add(dealerName);

        dealerName = new MdmDealerDetailDealerDealerName();
        dealerName.setDealerNameData(dealer.getG_dealerNameData());
        dealerName.setNameTypeCode(dealer.getG_nameTypeCode());
        mdmDealerDetailDealerDealerNameList.add(dealerName);
    }

    private void retrieveDealerStatus(ModelExportDealer dealer) {

        MdmDealerDetailDealerDealerStatus mdmDealerDetailDealerDealerStatus = new MdmDealerDetailDealerDealerStatus();
        mdmDealerDetailDealerDealerStatus.setStatusCode(dealer.getStatusCode());
        mdmDealerDetailDealerDealerStatus.setStatusTypeCode(dealer.getStatusTypeCode());
        mdmDealerDetailDealerDealerStatusList.add(mdmDealerDetailDealerDealerStatus);

        mdmDealerDetailDealerDealerStatus = new MdmDealerDetailDealerDealerStatus();
        mdmDealerDetailDealerDealerStatus.setStatusCode(dealer.getStatusCode2());
        mdmDealerDetailDealerDealerStatus.setStatusTypeCode(dealer.getStatusTypeCode2());
        mdmDealerDetailDealerDealerStatusList.add(mdmDealerDetailDealerDealerStatus);
    }

    private void retrieveDealerDate(ModelExportDealer dealer) {

        MdmDealerDetailDealerDealerDate mdmDealerDetailDealerDealerDate = new MdmDealerDetailDealerDealerDate();
        mdmDealerDetailDealerDealerDate.setDateTypeCode(dealer.getDateTypeCode());

        if (dealer.getDealerDateData() != null && !dealer.getDealerDateData().isEmpty()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            LocalDateTime localDateTime = LocalDateTime.parse(dealer.getDealerDateData(), formatter);

            // Convert to Date
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            mdmDealerDetailDealerDealerDate.setDealerDateData(date);

        }

        mdmDealerDetailDealerDealerDateList.add(mdmDealerDetailDealerDealerDate);

        if (dealer.getDealerDateDataFin() != null && !dealer.getDealerDateDataFin().isEmpty()) {

            mdmDealerDetailDealerDealerDate = new MdmDealerDetailDealerDealerDate();
            mdmDealerDetailDealerDealerDate.setDateTypeCode(dealer.getDateTypeCodeFin());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            LocalDateTime localDateTime = LocalDateTime.parse(dealer.getDealerDateDataFin(), formatter);

            // Convert to Date
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            mdmDealerDetailDealerDealerDate.setDealerDateData(date);
            mdmDealerDetailDealerDealerDateList.add(mdmDealerDetailDealerDealerDate);
        }
    }

    private void retrieveDealerServices(Connection connection, ModelExportDealer dealer) {

        List<ModelExportDealerService> listResultDealerServices = processService.callGetDealerServicesDBS(connection, processId, dealer.getDealerId());

        for (ModelExportDealerService service : listResultDealerServices) {

            MdmDealerDetailDealerServiceOffered mdmDealerDetailDealerServiceOffered = new MdmDealerDetailDealerServiceOffered();
            mdmDealerDetailDealerServiceOffered.setServiceTypeCode(service.getServiceTypeCode());

            mdmDealerDetailDealerServiceOfferedList.add(mdmDealerDetailDealerServiceOffered);
        }
    }

    private void retrieveDealerCategorizations(ModelExportDealer dealer) {

        MdmDealerDetailDealerDealerCategorization mdmDealerDetailDealerDealerCategorization = new MdmDealerDetailDealerDealerCategorization();
        mdmDealerDetailDealerDealerCategorization.setCategorizationCode(dealer.getCategorizationCode());
        mdmDealerDetailDealerDealerCategorization.setCategorizationTypeCode(dealer.getCategorizationTypeCode());

        mdmDealerDetailDealerDealerCategorizationList.add(mdmDealerDetailDealerDealerCategorization);
    }

    private static MdmDealerDetailDealerAddress getMdmDealerDetailDealerAddress(ModelExportDealer dealer) {

        MdmDealerDetailDealerAddress address = new MdmDealerDetailDealerAddress();
        address.setAddressLine1(dealer.getAddressLine1());

        String addressLine2 = dealer.getAddressLine2() != null ? dealer.getAddressLine2() : "";
        address.setAddressLine2(addressLine2);

        address.setCityName(dealer.getCityName());
        address.setCountryCode(dealer.getCountryCode());
        address.setZipPostalCode(dealer.getZipPostalCode());
        address.setLatitude(dealer.getLatitude());
        address.setLongitude(dealer.getLongitude());
        address.setLongitudeSpecified(true);
        address.setStateProvinceCode("");

        String zipcodeSuffix = dealer.getZipcodeSuffix() != null ? dealer.getZipcodeSuffix() : "";
        address.setZipcodeSuffix(zipcodeSuffix);

        String zipPostalCode = dealer.getZipPostalCode() != null ? dealer.getZipPostalCode() : "";
        address.setZipPostalCode(zipPostalCode);

        return address;

    }

    private void retrieveDepartments(Connection connection, ModelExportDealer dealer, MdmDealerDetailDealer dealerDetail, List<String> lDepartments) {

        List<ModelExportDepartement> departmentList = processService.callGetDepartementInfosDBS(connection, processId, dealer.getDealerId());

        isOnlyFRASVCDepartement = false;

        for (ModelExportDepartement dept : departmentList) {

            boolean notExist = lDepartments.stream().noneMatch(d -> d.equals(dept.getDepartmentCode()));

            if( lDepartments.isEmpty() || notExist){

                if (departmentList.size() == 1 && "SERVICE".equals(dept.getDepartmentCode())) {
                    isOnlyFRASVCDepartement = true;
                }

                MdmDealerDetailDealerDepartment department = new MdmDealerDetailDealerDepartment();
                department.setDepartmentCode(dept.getDepartmentCode());
                department.setContactName(dept.getContactName());

                // Communication channels (email and phone)
                MdmDealerDetailDealerDepartmentCommunicationChannels communicationChannels = new MdmDealerDetailDealerDepartmentCommunicationChannels();
                String email = dept.getEmail() != null ? dept.getEmail() : "";
                communicationChannels.setEmail(email);

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

                extractDealerDetailAddress(mdmDealerDetailDealerAddressList, dept);

                dealerDetail.getDepartments().add(department);
                dealerDetail.setAddresses(mdmDealerDetailDealerAddressList);
            }

        }

    }

    private void extractDealerDetailPhoneCommunication(ModelExportDepartement dept, MdmDealerDetailDealerDepartment department, MdmDealerDetailDealerDepartmentCommunicationChannels communicationChannels) {

        MdmDealerDetailDealerDepartmentCommunicationChannelsPhone phone = new MdmDealerDetailDealerDepartmentCommunicationChannelsPhone();
        phone.setPhoneCountryCode(dept.getPhoneCountryCode());

        String phoneNumber = FormatUtil.formatPhoneNumber(dept.getPhoneNumber());
        phone.setPhoneNumber(phoneNumber);

        String areaCode = dept.getAreaCode() != null ? dept.getAreaCode():"";
        phone.setAreaCode(areaCode);

        phoneNumber = FormatUtil.formatPhoneNumber(dept.getFullPhoneNumber());
        phone.setFullPhoneNumber(phoneNumber);

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

        String addressLine2 = dept.getAddressLine2() != null ? dept.getAddressLine2() : "";
        mdmDealerDetailDealerAddress.setAddressLine2(addressLine2);

        mdmDealerDetailDealerAddress.setAddressTypeCode(dept.getAddressTypeCode());
        mdmDealerDetailDealerAddress.setCityName(dept.getCityName());
        mdmDealerDetailDealerAddress.setCountryCode(dept.getCountryCode());
        mdmDealerDetailDealerAddress.setDepartmentCode(dept.getDepartmentCode());
        mdmDealerDetailDealerAddress.setLatitude(Double.parseDouble(dept.getLatitude()));
        mdmDealerDetailDealerAddress.setLatitudeSpecified(true);
        mdmDealerDetailDealerAddress.setLongitude(Double.parseDouble(dept.getLongitude()));
        mdmDealerDetailDealerAddress.setLongitudeSpecified(true);
        mdmDealerDetailDealerAddress.setStateProvinceCode(""); // Setting empty string

        String zipcodeSuffix = dept.getZipcodeSuffix() != null ? dept.getZipcodeSuffix() : "";
        mdmDealerDetailDealerAddress.setZipcodeSuffix(zipcodeSuffix);

        String zipPostalCode = dept.getZipPostalCode() != null ? dept.getZipPostalCode() : "";
        mdmDealerDetailDealerAddress.setZipPostalCode(zipPostalCode);

        mdmDealerDetailDealerAddresses.add(mdmDealerDetailDealerAddress);
    }

    // Placeholder method for getting brand list
    private String[] getListeMarque() {

        String lMarque = configurationService.fetchConfigValue(DBSConstUtil.EXPORTMARQUE);
        String[] marques = lMarque.split(DBSConstUtil.SEPARATORMARQUE);

        logger.info("Liste des marques : " + Arrays.toString(marques));
        return marques;
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
                else
                {
                    List<MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours> hoursList = new ArrayList<>();
                    MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours hours = new MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours();
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
                String closedInd = element.getClosedIndicator() != null ? element.getClosedIndicator() : "";
                day.setClosedIndicator(closedInd);
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

    protected boolean canExecute() {

        boolean res = false;

        if (result != ResultState.Failed) {

            // Initialize the TS_PROCESS table
            try (Connection connection = DriverManager.getConnection(connectionString, user, userPass)) {
                processId = processService.initializeNewProcess(connection, ProcessService.EXPORT_FULL_DBS);

                if (processId > 0) {
                    res = true;
                    result = ResultState.Success;
                } else {
                    result = ResultState.Failed;
                }

            } catch (Exception e) {
                logger.severe("Failed to connect to Database canExecute method " + e.getMessage());
            }

        }

        return res;
    }

    public String formatFileName(Connection connection, String marque, String fileName) {

        fileName = fileName.replace("[FROMSTATION]", "GVF");
        fileName = fileName.replace("[TOSTATION]", "KTD");
        fileName = fileName.replace("[FORMAT]", "MDM");
        fileName = fileName.replace("[COUNTRYCODE]", codePaysDefault);

        String key = DBSConstUtil.NOMCOURTMARQUE.replace("[BRAND]", marque);
        String nomcourt = configurationService.getConfigValue(connection, key);

        if (nomcourt != null) {
            return fileName.replace("[BRAND]", nomcourt);
        }
        return fileName.replace("[BRAND]", marque);
    }

}

