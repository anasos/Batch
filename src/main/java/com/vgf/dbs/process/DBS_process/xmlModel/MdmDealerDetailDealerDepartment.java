package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "", propOrder = {"communicationChannels", "hoursOfOperations"})
public class MdmDealerDetailDealerDepartment implements Serializable {

    private MdmDealerDetailDealerDepartmentCommunicationChannels communicationChannels;
    private MdmDealerDetailDealerDepartmentHoursOfOperations hoursOfOperations;
    private String departmentCode;
    private String contactName;

    // Getters and Setters

    @XmlElement
    public MdmDealerDetailDealerDepartmentCommunicationChannels getCommunicationChannels() {
        return communicationChannels;
    }

    public void setCommunicationChannels(MdmDealerDetailDealerDepartmentCommunicationChannels communicationChannels) {
        this.communicationChannels = communicationChannels;
    }

    @XmlElement
    public MdmDealerDetailDealerDepartmentHoursOfOperations getHoursOfOperations() {
        return hoursOfOperations;
    }

    public void setHoursOfOperations(MdmDealerDetailDealerDepartmentHoursOfOperations hoursOfOperations) {
        this.hoursOfOperations = hoursOfOperations;
    }

    @XmlAttribute
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    @XmlAttribute
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}

