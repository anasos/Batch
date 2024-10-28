package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "")
public class MdmDealerDetailDealerDealerPreferenceDealerPreferenceDetails implements Serializable {

    private String dealerPreferencetDetail;

    // Getter and Setter

    @XmlElement
    public String getDealerPreferencetDetail() {
        return dealerPreferencetDetail;
    }

    public void setDealerPreferencetDetail(String dealerPreferencetDetail) {
        this.dealerPreferencetDetail = dealerPreferencetDetail;
    }
}
