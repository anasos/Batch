package com.vgf.dbs.process.DBS_process.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBSConfig {

//    @Value("${dbs.connectionString}")
//    private String connectionString;
//
//    @Value("${dbs.exportFolder}")
//    private String exportFolder;

    @Value("${dbs.codePaysDefault}")
    private String codePaysDefault;

//    @Value("${dbs.dealerCountryCodeDefault}")
//    private String dealerCountryCodeDefault;


    public String getCodePaysDefault() {
        return codePaysDefault;
    }
}

