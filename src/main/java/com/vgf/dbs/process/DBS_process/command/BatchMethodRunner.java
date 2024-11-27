package com.vgf.dbs.process.DBS_process.command;

import com.vgf.dbs.process.DBS_process.service.ExportDBS;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class BatchMethodRunner implements CommandLineRunner {

    private ExportDBS exportDBS;

    public ExportDBS getExportDBS() {
        return exportDBS;
    }

    public BatchMethodRunner(ExportDBS exportDBS) {
        this.exportDBS = exportDBS;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0) {
            String methodName = args[0];
            if (methodName.equals("exportDBS")) {
                exportDBS.generateXML();
            } else {
                System.out.println("Invalid method name: " + methodName);
            }
        } else {
            System.out.println("No method name provided.");
        }
    }
}
