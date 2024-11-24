package com.vgf.dbs.process.DBS_process.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
public class ModelExportDealer {

    @Id
    @Column(name = "dealerId")
    private String dealerId;

    private String dealerCountryCode;
    private String dealerDivCode;
    private Date updateDate;
    private String dealerTypeCode;
    private String dealerLanguageCode;
    private String dealerMessage;

    private String addressLine1;
    private String addressLine2;
    private String cityName;
    private String stateProvinceCode;
    private String countryCode;
    private String zipPostalCode;
    private String zipcodeSuffix;
    private double longitude; // decimal in C# is replaced with double in Java
    private double latitude;
    private String departmentCode;
    private String addressTypeCode;

    private String categorizationCode;
    private String categorizationTypeCode;

    private String dealerDateData;
    private String dateTypeCode;
    private String dealerDateDataFin;
    private String dateTypeCodeFin;

    private String statusCode;
    private String statusTypeCode;
    private String statusCode2;
    private String statusTypeCode2;

    private String dealerNameData;
    private String nameTypeCode;
    private String G_dealerNameData;
    private String G_nameTypeCode;
    private String relatedDealerId;
    private String relationshipTypeCode;
    private String dealerPreferencetDetail;
    private String dealerPreferencetDetail2;
    private String dealerPreferencetDetail3;
    private String dealerPreferencetDetail4;
    private String dealerPreferenceTypeCode;
    private String url;
    private String urlFunctionCode;
    private String url2;
    private String urlFunctionCode2;
    private String mentionLegale;

    // Getters and Setters for all fields
    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerCountryCode() {
        return dealerCountryCode;
    }

    public void setDealerCountryCode(String dealerCountryCode) {
        this.dealerCountryCode = dealerCountryCode;
    }

    public String getDealerDivCode() {
        return dealerDivCode;
    }

    public void setDealerDivCode(String dealerDivCode) {
        this.dealerDivCode = dealerDivCode;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDealerTypeCode() {
        return dealerTypeCode;
    }

    public void setDealerTypeCode(String dealerTypeCode) {
        this.dealerTypeCode = dealerTypeCode;
    }

    public String getDealerLanguageCode() {
        return dealerLanguageCode;
    }

    public void setDealerLanguageCode(String dealerLanguageCode) {
        this.dealerLanguageCode = dealerLanguageCode;
    }

    public String getDealerMessage() {
        return dealerMessage;
    }

    public void setDealerMessage(String dealerMessage) {
        this.dealerMessage = dealerMessage;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateProvinceCode() {
        return stateProvinceCode;
    }

    public void setStateProvinceCode(String stateProvinceCode) {
        this.stateProvinceCode = stateProvinceCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getZipPostalCode() {
        return zipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode) {
        this.zipPostalCode = zipPostalCode;
    }

    public String getZipcodeSuffix() {
        return zipcodeSuffix;
    }

    public void setZipcodeSuffix(String zipcodeSuffix) {
        this.zipcodeSuffix = zipcodeSuffix;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getAddressTypeCode() {
        return addressTypeCode;
    }

    public void setAddressTypeCode(String addressTypeCode) {
        this.addressTypeCode = addressTypeCode;
    }

    public String getCategorizationCode() {
        return categorizationCode;
    }

    public void setCategorizationCode(String categorizationCode) {
        this.categorizationCode = categorizationCode;
    }

    public String getCategorizationTypeCode() {
        return categorizationTypeCode;
    }

    public void setCategorizationTypeCode(String categorizationTypeCode) {
        this.categorizationTypeCode = categorizationTypeCode;
    }

    public String getDealerDateData() {
        return dealerDateData;
    }

    public void setDealerDateData(String dealerDateData) {
        this.dealerDateData = dealerDateData;
    }

    public String getDateTypeCode() {
        return dateTypeCode;
    }

    public void setDateTypeCode(String dateTypeCode) {
        this.dateTypeCode = dateTypeCode;
    }

    public String getDealerDateDataFin() {
        return dealerDateDataFin;
    }

    public void setDealerDateDataFin(String dealerDateDataFin) {
        this.dealerDateDataFin = dealerDateDataFin;
    }

    public String getDateTypeCodeFin() {
        return dateTypeCodeFin;
    }

    public void setDateTypeCodeFin(String dateTypeCodeFin) {
        this.dateTypeCodeFin = dateTypeCodeFin;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusTypeCode() {
        return statusTypeCode;
    }

    public void setStatusTypeCode(String statusTypeCode) {
        this.statusTypeCode = statusTypeCode;
    }

    public String getStatusCode2() {
        return statusCode2;
    }

    public void setStatusCode2(String statusCode2) {
        this.statusCode2 = statusCode2;
    }

    public String getStatusTypeCode2() {
        return statusTypeCode2;
    }

    public void setStatusTypeCode2(String statusTypeCode2) {
        this.statusTypeCode2 = statusTypeCode2;
    }

    public String getDealerNameData() {
        return dealerNameData;
    }

    public void setDealerNameData(String dealerNameData) {
        this.dealerNameData = dealerNameData;
    }

    public String getNameTypeCode() {
        return nameTypeCode;
    }

    public void setNameTypeCode(String nameTypeCode) {
        this.nameTypeCode = nameTypeCode;
    }

    public String getG_dealerNameData() {
        return G_dealerNameData;
    }

    public void setG_dealerNameData(String g_dealerNameData) {
        G_dealerNameData = g_dealerNameData;
    }

    public String getG_nameTypeCode() {
        return G_nameTypeCode;
    }

    public void setG_nameTypeCode(String g_nameTypeCode) {
        G_nameTypeCode = g_nameTypeCode;
    }

    public String getRelatedDealerId() {
        return relatedDealerId;
    }

    public void setRelatedDealerId(String relatedDealerId) {
        this.relatedDealerId = relatedDealerId;
    }

    public String getRelationshipTypeCode() {
        return relationshipTypeCode;
    }

    public void setRelationshipTypeCode(String relationshipTypeCode) {
        this.relationshipTypeCode = relationshipTypeCode;
    }

    public String getDealerPreferencetDetail() {
        return dealerPreferencetDetail;
    }

    public void setDealerPreferencetDetail(String dealerPreferencetDetail) {
        this.dealerPreferencetDetail = dealerPreferencetDetail;
    }

    public String getDealerPreferencetDetail2() {
        return dealerPreferencetDetail2;
    }

    public void setDealerPreferencetDetail2(String dealerPreferencetDetail2) {
        this.dealerPreferencetDetail2 = dealerPreferencetDetail2;
    }

    public String getDealerPreferencetDetail3() {
        return dealerPreferencetDetail3;
    }

    public void setDealerPreferencetDetail3(String dealerPreferencetDetail3) {
        this.dealerPreferencetDetail3 = dealerPreferencetDetail3;
    }

    public String getDealerPreferencetDetail4() {
        return dealerPreferencetDetail4;
    }

    public void setDealerPreferencetDetail4(String dealerPreferencetDetail4) {
        this.dealerPreferencetDetail4 = dealerPreferencetDetail4;
    }

    public String getDealerPreferenceTypeCode() {
        return dealerPreferenceTypeCode;
    }

    public void setDealerPreferenceTypeCode(String dealerPreferenceTypeCode) {
        this.dealerPreferenceTypeCode = dealerPreferenceTypeCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlFunctionCode() {
        return urlFunctionCode;
    }

    public void setUrlFunctionCode(String urlFunctionCode) {
        this.urlFunctionCode = urlFunctionCode;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrlFunctionCode2() {
        return urlFunctionCode2;
    }

    public void setUrlFunctionCode2(String urlFunctionCode2) {
        this.urlFunctionCode2 = urlFunctionCode2;
    }

    public String getMentionLegale() {
        return mentionLegale;
    }

    public void setMentionLegale(String mentionLegale) {
        this.mentionLegale = mentionLegale;
    }

}
