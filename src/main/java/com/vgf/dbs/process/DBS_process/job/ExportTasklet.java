package com.vgf.dbs.process.DBS_process.job;

import com.vgf.dbs.process.DBS_process.service.ExportDBS;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ExportTasklet implements Tasklet {

    private final ExportDBS exportDBS;

    public ExportTasklet(ExportDBS exportDBS) {
        this.exportDBS = exportDBS;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        boolean success = exportDBS.beginProcess();

        if (success) {
            System.out.println("Export process completed successfully.");
        } else {
            System.out.println("Export process failed.");
            throw new RuntimeException("Export process failed.");
        }

        return RepeatStatus.FINISHED;
    }
}
