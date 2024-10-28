package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;

@XmlRootElement(name = "mdmDealerDetail")
@XmlType(propOrder = {"dealers", "meta"})
public class MdmDealerDetail implements Serializable {

    private MdmDealerDetailDealer[] dealers;
    private MdmDealerDetailMeta[] meta;

    // Getters and Setters

    @XmlElementWrapper(name = "dealers")
    @XmlElement(name = "dealer")
    public MdmDealerDetailDealer[] getDealers() {
        return dealers;
    }

    public void setDealers(MdmDealerDetailDealer[] dealers) {
        this.dealers = dealers;
    }

    @XmlElement(name = "meta")
    public MdmDealerDetailMeta[] getMeta() {
        return meta;
    }

    public void setMeta(MdmDealerDetailMeta[] meta) {
        this.meta = meta;
    }
}
