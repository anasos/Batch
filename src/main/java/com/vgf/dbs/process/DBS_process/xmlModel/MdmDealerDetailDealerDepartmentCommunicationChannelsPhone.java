package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "", propOrder = {"phoneCountryCode", "areaCode", "phoneNumber", "fullPhoneNumber"})
public class MdmDealerDetailDealerDepartmentCommunicationChannelsPhone implements Serializable {

    private String phoneCountryCode;
    private String areaCode;
    private String phoneNumber;
    private String fullPhoneNumber;
    private String phoneTypeCode;

    // Getters and Setters

    @XmlElement
    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    @XmlElement
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @XmlElement
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @XmlElement
    public String getFullPhoneNumber() {
        return fullPhoneNumber;
    }

    public void setFullPhoneNumber(String fullPhoneNumber) {
        this.fullPhoneNumber = fullPhoneNumber;
    }

    @XmlAttribute
    public String getPhoneTypeCode() {
        return phoneTypeCode;
    }

    public void setPhoneTypeCode(String phoneTypeCode) {
        this.phoneTypeCode = phoneTypeCode;
    }
}
