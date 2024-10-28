package com.vgf.dbs.process.DBS_process.xmlModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "", propOrder = {"imprint", "datasecurity", "datasecurityTBO", "termsOfUseTBO"})
public class MdmDealerDetailDealerDealerMarketingAllowances implements Serializable {

    private String imprint;
    private String datasecurity;
    private String datasecurityTBO;
    private String termsOfUseTBO;

    // Getters and Setters

    @XmlElement
    public String getImprint() {
        return imprint;
    }

    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    @XmlElement
    public String getDatasecurity() {
        return datasecurity;
    }

    public void setDatasecurity(String datasecurity) {
        this.datasecurity = datasecurity;
    }

    @XmlElement
    public String getDatasecurityTBO() {
        return datasecurityTBO;
    }

    public void setDatasecurityTBO(String datasecurityTBO) {
        this.datasecurityTBO = datasecurityTBO;
    }

    @XmlElement
    public String getTermsOfUseTBO() {
        return termsOfUseTBO;
    }

    public void setTermsOfUseTBO(String termsOfUseTBO) {
        this.termsOfUseTBO = termsOfUseTBO;
    }

    public MdmDealerDetailDealerDealerMarketingAllowances(String imprint, String datasecurity, String datasecurityTBO, String termsOfUseTBO) {
        this.imprint = imprint;
        this.datasecurity = datasecurity;
        this.datasecurityTBO = datasecurityTBO;
        this.termsOfUseTBO = termsOfUseTBO;
    }

    public MdmDealerDetailDealerDealerMarketingAllowances() {
    }
}
