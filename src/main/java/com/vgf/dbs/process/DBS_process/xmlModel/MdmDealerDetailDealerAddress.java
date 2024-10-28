package com.vgf.dbs.process.DBS_process.xmlModel;


import javax.xml.bind.annotation.*;

@XmlRootElement
public class MdmDealerDetailDealerAddress {

    private String addressLine1;
    private String addressLine2;
    private String cityName;
    private String stateProvinceCode;
    private String countryCode;
    private String zipPostalCode;
    private String zipcodeSuffix;
    private double longitude;
    private boolean longitudeSpecified;
    private double latitude;
    private boolean latitudeSpecified;
    private String departmentCode;
    private String addressTypeCode;

    // Getters and Setters

    @XmlElement
    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @XmlElement(nillable = true)
    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    @XmlElement
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @XmlElement
    public String getStateProvinceCode() {
        return stateProvinceCode;
    }

    public void setStateProvinceCode(String stateProvinceCode) {
        this.stateProvinceCode = stateProvinceCode;
    }

    @XmlElement
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @XmlElement
    public String getZipPostalCode() {
        return zipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode) {
        this.zipPostalCode = zipPostalCode;
    }

    @XmlElement
    public String getZipcodeSuffix() {
        return zipcodeSuffix;
    }

    public void setZipcodeSuffix(String zipcodeSuffix) {
        this.zipcodeSuffix = zipcodeSuffix;
    }

    @XmlElement
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @XmlTransient
    public boolean isLongitudeSpecified() {
        return longitudeSpecified;
    }

    public void setLongitudeSpecified(boolean longitudeSpecified) {
        this.longitudeSpecified = longitudeSpecified;
    }

    @XmlElement
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @XmlTransient
    public boolean isLatitudeSpecified() {
        return latitudeSpecified;
    }

    public void setLatitudeSpecified(boolean latitudeSpecified) {
        this.latitudeSpecified = latitudeSpecified;
    }

    @XmlAttribute
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    @XmlAttribute
    public String getAddressTypeCode() {
        return addressTypeCode;
    }

    public void setAddressTypeCode(String addressTypeCode) {
        this.addressTypeCode = addressTypeCode;
    }
}
