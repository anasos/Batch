package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "")
public class MdmDealerDetailDealerDealerStatus implements Serializable {

    private String statusCode;
    private String statusTypeCode;

    // Getters and Setters

    @XmlElement
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @XmlAttribute
    public String getStatusTypeCode() {
        return statusTypeCode;
    }

    public void setStatusTypeCode(String statusTypeCode) {
        this.statusTypeCode = statusTypeCode;
    }
}
