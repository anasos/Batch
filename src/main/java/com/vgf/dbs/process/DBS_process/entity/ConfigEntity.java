package com.vgf.dbs.process.DBS_process.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONFIGURATIONS")  // Adjust to match the actual table name in your database
public class ConfigEntity {

    @Id
    @Column(name = "CONFIG_KEY")
    private String key;

    @Column(name = "CONFIG_VALUE")
    private String value;

    // Constructors
    public ConfigEntity() {}

    public ConfigEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Getters and Setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConfigEntity{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

