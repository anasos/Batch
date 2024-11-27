package com.vgf.dbs.process.DBS_process.rest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

public class JobRestController {

    private final JobLauncher jobLauncher;
    private final Job exportJob;

    @Autowired
    public JobRestController(JobLauncher jobLauncher, Job exportJob) {
        this.jobLauncher = jobLauncher;
        this.exportJob = exportJob;
    }

}

