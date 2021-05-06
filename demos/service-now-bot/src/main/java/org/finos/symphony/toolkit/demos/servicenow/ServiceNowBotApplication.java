package org.finos.symphony.toolkit.demos.servicenow;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.finos.symphony.toolkit.demos.servicenow.service.ServiceNowService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
public class ServiceNowBotApplication implements CommandLineRunner {

    private final ServiceNowService serviceNowService;

	public static void main(String[] args) {
        SpringApplication.run(ServiceNowBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Inside run method of ServiceNowBotApplication");
        this.serviceNowService.getRITMDetailsByRITMNumber("xyz");

    }
}
