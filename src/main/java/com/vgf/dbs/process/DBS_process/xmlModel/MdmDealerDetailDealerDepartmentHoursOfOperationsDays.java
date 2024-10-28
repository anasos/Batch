package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "", propOrder = {"closedIndicator", "hours"})
public class MdmDealerDetailDealerDepartmentHoursOfOperationsDays implements Serializable {

    private String closedIndicator;
    private MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours[] hours;
    private String dayOfWeekCode;

    // Getters and Setters

    @XmlElement
    public String getClosedIndicator() {
        return closedIndicator;
    }

    public void setClosedIndicator(String closedIndicator) {
        this.closedIndicator = closedIndicator;
    }

    @XmlElement(name = "hours")
    public MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours[] getHours() {
        return hours;
    }

    public void setHours(MdmDealerDetailDealerDepartmentHoursOfOperationsDaysHours[] hours) {
        this.hours = hours;
    }

    @XmlAttribute
    public String getDayOfWeekCode() {
        return dayOfWeekCode;
    }

    public void setDayOfWeekCode(String dayOfWeekCode) {
        this.dayOfWeekCode = dayOfWeekCode;
    }
}
