package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "")
public class MdmDealerDetailDealerDealerUrl implements Serializable {

    private String url;
    private String urlFunctionCode;

    // Getters and Setters

    @XmlElement
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlAttribute
    public String getUrlFunctionCode() {
        return urlFunctionCode;
    }

    public void setUrlFunctionCode(String urlFunctionCode) {
        this.urlFunctionCode = urlFunctionCode;
    }
}
