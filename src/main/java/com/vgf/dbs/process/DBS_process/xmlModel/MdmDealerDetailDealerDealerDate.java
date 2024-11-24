package com.vgf.dbs.process.DBS_process.xmlModel;

import com.vgf.dbs.process.DBS_process.util.DateAdapterUtil;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@XmlType(name = "")
public class MdmDealerDetailDealerDealerDate implements Serializable {

    private LocalDate dealerDateData;
    private String dateTypeCode;

    // Getters and Setters

    @XmlElement(name = "dealerDateData")
    @XmlJavaTypeAdapter(DateAdapterUtil.class) // Adapter for date formatting
    public LocalDate getDealerDateData() {
        return dealerDateData;
    }

    public void setDealerDateData(LocalDate dealerDateData) {
        this.dealerDateData = dealerDateData;
    }

    @XmlAttribute
    public String getDateTypeCode() {
        return dateTypeCode;
    }

    public void setDateTypeCode(String dateTypeCode) {
        this.dateTypeCode = dateTypeCode;
    }
}
