package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "")
public class MdmDealerDetailDealerRelatedDealer implements Serializable {

    private String relatedDealerId;
    private String relationshipTypeCode;

    // Getters and Setters

    @XmlElement
    public String getRelatedDealerId() {
        return relatedDealerId;
    }

    public void setRelatedDealerId(String relatedDealerId) {
        this.relatedDealerId = relatedDealerId;
    }

    @XmlAttribute
    public String getRelationshipTypeCode() {
        return relationshipTypeCode;
    }

    public void setRelationshipTypeCode(String relationshipTypeCode) {
        this.relationshipTypeCode = relationshipTypeCode;
    }
}
