package com.vgf.dbs.process.DBS_process;

import com.vgf.dbs.process.DBS_process.command.BatchMethodRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DbsProcessApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbsProcessApplication.class, args);
	}

	// Make sure CommandLineRunner is called correctly with your logic
	@Bean
	public CommandLineRunner run(BatchMethodRunner runner) {
		return args -> runner.run(args); // This passes command-line arguments to your BatchMethodRunner
	}
}
