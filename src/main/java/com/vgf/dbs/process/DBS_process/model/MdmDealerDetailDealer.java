package com.vgf.dbs.process.DBS_process.model;

public class MdmDealerDetailDealer {

    private String dealerId;
    private String dealerDivCode;
    private String dealerCountryCode;
    private String dealerTypeCode;
    private String updateDate;
    private String dealerLanguageCode;
    private String dealerMessage;
    private DealerMarketingAllowances dealerMarketingAllowances;

    // Getters and setters...

    public String getDealerId() { return dealerId; }
    public void setDealerId(String dealerId) { this.dealerId = dealerId; }

    public String getDealerDivCode() { return dealerDivCode; }
    public void setDealerDivCode(String dealerDivCode) { this.dealerDivCode = dealerDivCode; }

    public String getDealerCountryCode() { return dealerCountryCode; }
    public void setDealerCountryCode(String dealerCountryCode) { this.dealerCountryCode = dealerCountryCode; }

    public String getDealerTypeCode() { return dealerTypeCode; }
    public void setDealerTypeCode(String dealerTypeCode) { this.dealerTypeCode = dealerTypeCode; }

    public String getUpdateDate() { return updateDate; }
    public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }

    public String getDealerLanguageCode() { return dealerLanguageCode; }
    public void setDealerLanguageCode(String dealerLanguageCode) { this.dealerLanguageCode = dealerLanguageCode; }

    public String getDealerMessage() { return dealerMessage; }
    public void setDealerMessage(String dealerMessage) { this.dealerMessage = dealerMessage; }

    public DealerMarketingAllowances getDealerMarketingAllowances() { return dealerMarketingAllowances; }
    public void setDealerMarketingAllowances(DealerMarketingAllowances dealerMarketingAllowances) {
        this.dealerMarketingAllowances = dealerMarketingAllowances;
    }

}
