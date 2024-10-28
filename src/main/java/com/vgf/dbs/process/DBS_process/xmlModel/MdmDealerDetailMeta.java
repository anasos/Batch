package com.vgf.dbs.process.DBS_process.xmlModel;

import com.vgf.dbs.process.DBS_process.util.DateAdapterUtil;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

@XmlType(name = "")
public class MdmDealerDetailMeta implements Serializable {

    private MdmDealerDetailMetaTypeCode[] typeCodes;
    private String numberOfDealers;
    private Date exportTimeStamp;

    // Getters and Setters

    @XmlElementWrapper(name = "typeCodes")
    @XmlElement(name = "typeCode")
    public MdmDealerDetailMetaTypeCode[] getTypeCodes() {
        return typeCodes;
    }

    public void setTypeCodes(MdmDealerDetailMetaTypeCode[] typeCodes) {
        this.typeCodes = typeCodes;
    }

    @XmlElement(name = "numberOfDealers")
    public String getNumberOfDealers() {
        return numberOfDealers;
    }

    public void setNumberOfDealers(String numberOfDealers) {
        this.numberOfDealers = numberOfDealers;
    }

    @XmlElement(name = "exportTimeStamp")
    @XmlJavaTypeAdapter(DateAdapterUtil.class) // Adapter to handle date formatting
    public Date getExportTimeStamp() {
        return exportTimeStamp;
    }

    public void setExportTimeStamp(Date exportTimeStamp) {
        this.exportTimeStamp = exportTimeStamp;
    }
}
