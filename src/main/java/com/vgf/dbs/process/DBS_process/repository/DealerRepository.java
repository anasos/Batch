package com.vgf.dbs.process.DBS_process.repository;

import com.vgf.dbs.process.DBS_process.model.ModelExportDealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealerRepository extends JpaRepository<ModelExportDealer, String> {

    @Procedure(procedureName = "PKG_DBS.SP_GetAllDealerInfos")
    List<ModelExportDealer> getAllDealerInfos(@Param("ProcessId") Number processId);

}