package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "")
public class MdmDealerDetailDealerDealerIndicator implements Serializable {

    private String indicatorTypeCode;

    // Getter and Setter

    @XmlAttribute
    public String getIndicatorTypeCode() {
        return indicatorTypeCode;
    }

    public void setIndicatorTypeCode(String indicatorTypeCode) {
        this.indicatorTypeCode = indicatorTypeCode;
    }
}
