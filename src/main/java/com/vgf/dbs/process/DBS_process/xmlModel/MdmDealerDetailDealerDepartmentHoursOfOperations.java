package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlType(name = "", propOrder = {"days", "hoursNote"})
public class MdmDealerDetailDealerDepartmentHoursOfOperations implements Serializable {

    private List<MdmDealerDetailDealerDepartmentHoursOfOperationsDays> days;
    private String hoursNote;

    // Getters and Setters

    @XmlElement(name = "days")
    public List<MdmDealerDetailDealerDepartmentHoursOfOperationsDays> getDays() {
        return days;
    }

    public void setDays(List<MdmDealerDetailDealerDepartmentHoursOfOperationsDays> days) {
        this.days = days;
    }

    @XmlElement
    public String getHoursNote() {
        return hoursNote;
    }

    public void setHoursNote(String hoursNote) {
        this.hoursNote = hoursNote;
    }
}
