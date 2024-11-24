package com.vgf.dbs.process.DBS_process.repository;


import com.vgf.dbs.process.DBS_process.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<ConfigEntity, Long> {

    @Procedure(procedureName = "PKG_DBS.SP_GET_CONFIG_VALUE")
    String getConfigValue(String key);
}

