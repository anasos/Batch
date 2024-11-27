package com.vgf.dbs.process.DBS_process.xmlModel;

import com.vgf.dbs.process.DBS_process.adapter.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "", propOrder = {
        "departments", "addresses", "dealerMarketingAllowances", "dealerCategorizations",
        "dealerIndicators", "servicesOffered", "dealerDates", "dealerLanguageCode",
        "dealerStatuses", "dealerNames", "relatedDealers", "dealerPreferences",
        "dealerUrls", "dealerMessage","dealerId", "dealerCountryCode",
        "dealerDivCode", "updateDate", "dealerTypeCode"
})
public class MdmDealerDetailDealer implements Serializable {

    private List<MdmDealerDetailDealerDepartment> departments = new ArrayList<>();
    private List<MdmDealerDetailDealerAddress> addresses = new ArrayList<>();
    private MdmDealerDetailDealerDealerMarketingAllowances dealerMarketingAllowances;
    private List<MdmDealerDetailDealerDealerCategorization> dealerCategorizations = new ArrayList<>();
    private List<MdmDealerDetailDealerDealerIndicator> dealerIndicators = new ArrayList<>();
    private List<MdmDealerDetailDealerServiceOffered> servicesOffered = new ArrayList<>();
    private List<MdmDealerDetailDealerDealerDate> dealerDates = new ArrayList<>();
    private String dealerLanguageCode;
    private List<MdmDealerDetailDealerDealerStatus> dealerStatuses = new ArrayList<>();
    private List<MdmDealerDetailDealerDealerName> dealerNames;
    private List<MdmDealerDetailDealerRelatedDealer> relatedDealers;
    private List<MdmDealerDetailDealerDealerPreference> dealerPreferences;
    private List<MdmDealerDetailDealerDealerUrl> dealerUrls;
    private String dealerMessage;
    private String dealerId;
    private String dealerCountryCode;
    private String dealerDivCode;

    private LocalDate updateDate;

    private String dealerTypeCode;

    // Getters and Setters

    @XmlElementWrapper(name = "departments")
    @XmlElement(name = "department")
    public List<MdmDealerDetailDealerDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(List<MdmDealerDetailDealerDepartment> departments) {
        this.departments = departments;
    }

    @XmlElementWrapper(name = "addresses")
    @XmlElement(name = "address")
    public List<MdmDealerDetailDealerAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<MdmDealerDetailDealerAddress> addresses) {
        this.addresses = addresses;
    }

    @XmlElement(name = "dealerMarketingAllowances")
    public MdmDealerDetailDealerDealerMarketingAllowances getDealerMarketingAllowances() {
        return dealerMarketingAllowances;
    }

    public void setDealerMarketingAllowances(MdmDealerDetailDealerDealerMarketingAllowances dealerMarketingAllowances) {
        this.dealerMarketingAllowances = dealerMarketingAllowances;
    }

    @XmlElementWrapper(name = "dealerCategorizations")
    @XmlElement(name = "dealerCategorization")
    public List<MdmDealerDetailDealerDealerCategorization> getDealerCategorizations() {
        return dealerCategorizations;
    }

    public void setDealerCategorizations(List<MdmDealerDetailDealerDealerCategorization> dealerCategorizations) {
        this.dealerCategorizations = dealerCategorizations;
    }

    @XmlElementWrapper(name = "dealerIndicators")
    @XmlElement(name = "dealerIndicator")
    public List<MdmDealerDetailDealerDealerIndicator> getDealerIndicators() {
        return dealerIndicators;
    }

    public void setDealerIndicators(List<MdmDealerDetailDealerDealerIndicator> dealerIndicators) {
        this.dealerIndicators = dealerIndicators;
    }

    @XmlElementWrapper(name = "servicesOffered")
    @XmlElement(name = "serviceOffered")
    public List<MdmDealerDetailDealerServiceOffered> getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(List<MdmDealerDetailDealerServiceOffered> servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    @XmlElementWrapper(name = "dealerDates")
    @XmlElement(name = "dealerDate")
    public List<MdmDealerDetailDealerDealerDate> getDealerDates() {
        return dealerDates;
    }

    public void setDealerDates(List<MdmDealerDetailDealerDealerDate> dealerDates) {
        this.dealerDates = dealerDates;
    }

    @XmlElement
    public String getDealerLanguageCode() {
        return dealerLanguageCode;
    }

    public void setDealerLanguageCode(String dealerLanguageCode) {
        this.dealerLanguageCode = dealerLanguageCode;
    }

    @XmlElementWrapper(name = "dealerStatuses")
    @XmlElement(name = "dealerStatus")
    public List<MdmDealerDetailDealerDealerStatus> getDealerStatuses() {
        return dealerStatuses;
    }

    public void setDealerStatuses(List<MdmDealerDetailDealerDealerStatus> dealerStatuses) {
        this.dealerStatuses = dealerStatuses;
    }

    @XmlElementWrapper(name = "dealerNames")
    @XmlElement(name = "dealerName")
    public List<MdmDealerDetailDealerDealerName> getDealerNames() {
        return dealerNames;
    }

    public void setDealerNames(List<MdmDealerDetailDealerDealerName> dealerNames) {
        this.dealerNames = dealerNames;
    }

    @XmlElementWrapper(name = "relatedDealers")
    @XmlElement(name = "relatedDealer")
    public List<MdmDealerDetailDealerRelatedDealer> getRelatedDealers() {
        return relatedDealers;
    }

    public void setRelatedDealers(List<MdmDealerDetailDealerRelatedDealer> relatedDealers) {
        this.relatedDealers = relatedDealers;
    }

    @XmlElementWrapper(name = "dealerPreferences")
    @XmlElement(name = "dealerPreference")
    public List<MdmDealerDetailDealerDealerPreference> getDealerPreferences() {
        return dealerPreferences;
    }

    public void setDealerPreferences(List<MdmDealerDetailDealerDealerPreference> dealerPreferences) {
        this.dealerPreferences = dealerPreferences;
    }

    @XmlElementWrapper(name = "dealerUrls")
    @XmlElement(name = "dealerUrl")
    public List<MdmDealerDetailDealerDealerUrl> getDealerUrls() {
        return dealerUrls;
    }

    public void setDealerUrls(List<MdmDealerDetailDealerDealerUrl> dealerUrls) {
        this.dealerUrls = dealerUrls;
    }

    @XmlElement
    public String getDealerMessage() {
        return dealerMessage;
    }

    public void setDealerMessage(String dealerMessage) {
        this.dealerMessage = dealerMessage;
    }

    @XmlAttribute
    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    @XmlAttribute
    public String getDealerCountryCode() {
        return dealerCountryCode;
    }

    public void setDealerCountryCode(String dealerCountryCode) {
        this.dealerCountryCode = dealerCountryCode;
    }

    @XmlAttribute
    public String getDealerDivCode() {
        return dealerDivCode;
    }

    public void setDealerDivCode(String dealerDivCode) {
        this.dealerDivCode = dealerDivCode;
    }

    @XmlAttribute
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    @XmlAttribute
    public String getDealerTypeCode() {
        return dealerTypeCode;
    }

    public void setDealerTypeCode(String dealerTypeCode) {
        this.dealerTypeCode = dealerTypeCode;
    }
}
