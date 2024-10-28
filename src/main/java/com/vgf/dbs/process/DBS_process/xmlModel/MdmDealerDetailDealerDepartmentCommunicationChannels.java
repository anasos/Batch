package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;

@XmlType(name = "", propOrder = {"phones", "email"})
public class MdmDealerDetailDealerDepartmentCommunicationChannels implements Serializable {

    private MdmDealerDetailDealerDepartmentCommunicationChannelsPhone[] phones;
    private String email;

    // Getters and Setters

    @XmlElementWrapper(name = "phones")
    @XmlElement(name = "phone")
    public MdmDealerDetailDealerDepartmentCommunicationChannelsPhone[] getPhones() {
        return phones;
    }

    public void setPhones(MdmDealerDetailDealerDepartmentCommunicationChannelsPhone[] phones) {
        this.phones = phones;
    }

    @XmlElement
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
