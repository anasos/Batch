package com.vgf.dbs.process.DBS_process.service;

import com.vgf.dbs.process.DBS_process.model.ModelExportDealer;
import com.vgf.dbs.process.DBS_process.repository.DealerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealerService {
    private final DealerRepository dealerRepository;

    public DealerService(DealerRepository dealerRepository) {
        this.dealerRepository = dealerRepository;
    }

    public List<ModelExportDealer> getAllDealerInfos(int processID) {
        return dealerRepository.getAllDealerInfos(processID);
    }
}
