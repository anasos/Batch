package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "", propOrder = {"openTime", "closeTime"})
public class MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours implements Serializable {

    private String openTime;
    private String closeTime;

    // Getters and Setters

    @XmlElement
    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    @XmlElement
    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}
