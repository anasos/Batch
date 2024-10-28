package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlType(name = "", propOrder = {
        "departments", "addresses", "dealerMarketingAllowances", "dealerCategorizations",
        "dealerIndicators", "servicesOffered", "dealerDates", "dealerLanguageCode",
        "dealerStatuses", "dealerNames", "relatedDealers", "dealerPreferences",
        "dealerUrls", "dealerMessage"
})
public class MdmDealerDetailDealer implements Serializable {

    private List<MdmDealerDetailDealerDepartment> departments = new ArrayList<>();
    private MdmDealerDetailDealerAddress[] addresses;
    private MdmDealerDetailDealerDealerMarketingAllowances dealerMarketingAllowances;
    private MdmDealerDetailDealerDealerCategorization[] dealerCategorizations;
    private MdmDealerDetailDealerDealerIndicator[] dealerIndicators;
    private MdmDealerDetailDealerServiceOffered[] servicesOffered;
    private MdmDealerDetailDealerDealerDate[] dealerDates;
    private String dealerLanguageCode;
    private MdmDealerDetailDealerDealerStatus[] dealerStatuses;
    private MdmDealerDetailDealerDealerName[] dealerNames;
    private MdmDealerDetailDealerRelatedDealer[] relatedDealers;
    private MdmDealerDetailDealerDealerPreference[] dealerPreferences;
    private MdmDealerDetailDealerDealerUrl[] dealerUrls;
    private String dealerMessage;
    private String dealerId;
    private String dealerCountryCode;
    private String dealerDivCode;
    private Date updateDate;
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
    public MdmDealerDetailDealerAddress[] getAddresses() {
        return addresses;
    }

    public void setAddresses(MdmDealerDetailDealerAddress[] addresses) {
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
    public MdmDealerDetailDealerDealerCategorization[] getDealerCategorizations() {
        return dealerCategorizations;
    }

    public void setDealerCategorizations(MdmDealerDetailDealerDealerCategorization[] dealerCategorizations) {
        this.dealerCategorizations = dealerCategorizations;
    }

    @XmlElementWrapper(name = "dealerIndicators")
    @XmlElement(name = "dealerIndicator")
    public MdmDealerDetailDealerDealerIndicator[] getDealerIndicators() {
        return dealerIndicators;
    }

    public void setDealerIndicators(MdmDealerDetailDealerDealerIndicator[] dealerIndicators) {
        this.dealerIndicators = dealerIndicators;
    }

    @XmlElementWrapper(name = "servicesOffered")
    @XmlElement(name = "serviceOffered")
    public MdmDealerDetailDealerServiceOffered[] getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(MdmDealerDetailDealerServiceOffered[] servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    @XmlElementWrapper(name = "dealerDates")
    @XmlElement(name = "dealerDate")
    public MdmDealerDetailDealerDealerDate[] getDealerDates() {
        return dealerDates;
    }

    public void setDealerDates(MdmDealerDetailDealerDealerDate[] dealerDates) {
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
    public MdmDealerDetailDealerDealerStatus[] getDealerStatuses() {
        return dealerStatuses;
    }

    public void setDealerStatuses(MdmDealerDetailDealerDealerStatus[] dealerStatuses) {
        this.dealerStatuses = dealerStatuses;
    }

    @XmlElementWrapper(name = "dealerNames")
    @XmlElement(name = "dealerName")
    public MdmDealerDetailDealerDealerName[] getDealerNames() {
        return dealerNames;
    }

    public void setDealerNames(MdmDealerDetailDealerDealerName[] dealerNames) {
        this.dealerNames = dealerNames;
    }

    @XmlElementWrapper(name = "relatedDealers")
    @XmlElement(name = "relatedDealer")
    public MdmDealerDetailDealerRelatedDealer[] getRelatedDealers() {
        return relatedDealers;
    }

    public void setRelatedDealers(MdmDealerDetailDealerRelatedDealer[] relatedDealers) {
        this.relatedDealers = relatedDealers;
    }

    @XmlElementWrapper(name = "dealerPreferences")
    @XmlElement(name = "dealerPreference")
    public MdmDealerDetailDealerDealerPreference[] getDealerPreferences() {
        return dealerPreferences;
    }

    public void setDealerPreferences(MdmDealerDetailDealerDealerPreference[] dealerPreferences) {
        this.dealerPreferences = dealerPreferences;
    }

    @XmlElementWrapper(name = "dealerUrls")
    @XmlElement(name = "dealerUrl")
    public MdmDealerDetailDealerDealerUrl[] getDealerUrls() {
        return dealerUrls;
    }

    public void setDealerUrls(MdmDealerDetailDealerDealerUrl[] dealerUrls) {
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
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
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
