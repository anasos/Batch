package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "")
public class MdmDealerDetailDealerDealerCategorization implements Serializable {

    private String categorizationCode;
    private String categorizationTypeCode;

    // Getters and Setters

    @XmlElement
    public String getCategorizationCode() {
        return categorizationCode;
    }

    public void setCategorizationCode(String categorizationCode) {
        this.categorizationCode = categorizationCode;
    }

    @XmlAttribute
    public String getCategorizationTypeCode() {
        return categorizationTypeCode;
    }

    public void setCategorizationTypeCode(String categorizationTypeCode) {
        this.categorizationTypeCode = categorizationTypeCode;
    }
}
