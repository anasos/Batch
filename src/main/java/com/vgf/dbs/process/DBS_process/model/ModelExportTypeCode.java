package com.vgf.dbs.process.DBS_process.model;
public class ModelExportTypeCode {

    // Fields
    private String typeCodeCategory;
    private String typeCodeValue;
    private String typeCodeDesc;
    private String parentTypeCodeValue;

    // Getters and Setters
    public String getTypeCodeCategory() {
        return typeCodeCategory;
    }

    public void setTypeCodeCategory(String typeCodeCategory) {
        this.typeCodeCategory = typeCodeCategory;
    }

    public String getTypeCodeValue() {
        return typeCodeValue;
    }

    public void setTypeCodeValue(String typeCodeValue) {
        this.typeCodeValue = typeCodeValue;
    }

    public String getTypeCodeDesc() {
        return typeCodeDesc;
    }

    public void setTypeCodeDesc(String typeCodeDesc) {
        this.typeCodeDesc = typeCodeDesc;
    }

    public String getParentTypeCodeValue() {
        return parentTypeCodeValue;
    }

    public void setParentTypeCodeValue(String parentTypeCodeValue) {
        this.parentTypeCodeValue = parentTypeCodeValue;
    }
}
