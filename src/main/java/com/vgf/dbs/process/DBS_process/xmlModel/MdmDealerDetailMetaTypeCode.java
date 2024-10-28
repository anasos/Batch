package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "")
public class MdmDealerDetailMetaTypeCode implements Serializable {

    private String typeCodeCategory;
    private String typeCodeValue;
    private String typeCodeDesc;
    private String parentTypeCodeValue;

    // Getters and Setters

    @XmlElement
    public String getTypeCodeCategory() {
        return typeCodeCategory;
    }

    public void setTypeCodeCategory(String typeCodeCategory) {
        this.typeCodeCategory = typeCodeCategory;
    }

    @XmlElement
    public String getTypeCodeValue() {
        return typeCodeValue;
    }

    public void setTypeCodeValue(String typeCodeValue) {
        this.typeCodeValue = typeCodeValue;
    }

    @XmlElement
    public String getTypeCodeDesc() {
        return typeCodeDesc;
    }

    public void setTypeCodeDesc(String typeCodeDesc) {
        this.typeCodeDesc = typeCodeDesc;
    }

    @XmlElement
    public String getParentTypeCodeValue() {
        return parentTypeCodeValue;
    }

    public void setParentTypeCodeValue(String parentTypeCodeValue) {
        this.parentTypeCodeValue = parentTypeCodeValue;
    }
}
