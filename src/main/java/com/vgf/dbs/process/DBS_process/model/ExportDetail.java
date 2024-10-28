package com.vgf.dbs.process.DBS_process.model;

import com.vgf.dbs.process.DBS_process.xmlModel.MdmDealerDetailDealer;
import com.vgf.dbs.process.DBS_process.xmlModel.MdmDealerDetailMeta;

import java.util.List;

public class ExportDetail {

    private String brand;
    private List<MdmDealerDetailDealer> mdmDealerDetailDealers;
    private List<MdmDealerDetailMeta> mdmDealerDetailMetas;

    public ExportDetail(String brand, List<MdmDealerDetailDealer> details, List<MdmDealerDetailMeta> meta) {
        this.brand = brand;
        this.mdmDealerDetailDealers = details;
        this.mdmDealerDetailMetas = meta;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<MdmDealerDetailDealer> getMdmDealerDetailDealers() {
        return mdmDealerDetailDealers;
    }

    public void setMdmDealerDetailDealers(List<MdmDealerDetailDealer> mdmDealerDetailDealers) {
        this.mdmDealerDetailDealers = mdmDealerDetailDealers;
    }

    public List<MdmDealerDetailMeta> getMdmDealerDetailMetas() {
        return mdmDealerDetailMetas;
    }

    public void setMdmDealerDetailMetas(List<MdmDealerDetailMeta> mdmDealerDetailMetas) {
        this.mdmDealerDetailMetas = mdmDealerDetailMetas;
    }
}
