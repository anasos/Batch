package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlType(name = "", propOrder = {"dealerPreferenceDetails"})
public class MdmDealerDetailDealerDealerPreference implements Serializable {

    private List<MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails> dealerPreferenceDetails;
    private String dealerPreferenceTypeCode;

    // Getters and Setters

    @XmlElement(name = "dealerPreferenceDetails")
    public List<MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails> getDealerPreferenceDetails() {
        return dealerPreferenceDetails;
    }

    public void setDealerPreferenceDetails(List<MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails> dealerPreferenceDetails) {
        this.dealerPreferenceDetails = dealerPreferenceDetails;
    }

    @XmlAttribute
    public String getDealerPreferenceTypeCode() {
        return dealerPreferenceTypeCode;
    }

    public void setDealerPreferenceTypeCode(String dealerPreferenceTypeCode) {
        this.dealerPreferenceTypeCode = dealerPreferenceTypeCode;
    }
}
