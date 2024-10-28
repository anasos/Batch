package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "")
public class MdmDealerDetailDealerDealerName implements Serializable {

    private String dealerNameData;
    private String nameTypeCode;

    // Getters and Setters

    @XmlElement
    public String getDealerNameData() {
        return dealerNameData;
    }

    public void setDealerNameData(String dealerNameData) {
        this.dealerNameData = dealerNameData;
    }

    @XmlAttribute
    public String getNameTypeCode() {
        return nameTypeCode;
    }

    public void setNameTypeCode(String nameTypeCode) {
        this.nameTypeCode = nameTypeCode;
    }
}
