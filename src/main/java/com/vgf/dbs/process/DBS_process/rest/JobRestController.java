package com.vgf.dbs.process.DBS_process.rest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobRestController {

    private final JobLauncher jobLauncher;
    private final Job exportJob;

    @Autowired
    public JobRestController(JobLauncher jobLauncher, Job exportJob) {
        this.jobLauncher = jobLauncher;
        this.exportJob = exportJob;
    }

    @GetMapping("/run-export-job")
    public ResponseEntity<String> runExportJob(@RequestParam(value = "runId", required = false) String runId) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("runId", runId != null ? runId : String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
            jobLauncher.run(exportJob, params);
            return ResponseEntity.ok("Export Job Started Successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to start Export Job: " + e.getMessage());
        }
    }
}

