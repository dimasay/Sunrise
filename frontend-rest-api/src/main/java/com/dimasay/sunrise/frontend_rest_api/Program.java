package com.dimasay.sunrise.frontend_rest_api;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dimasay.sunrise")
public class Program {
    private static final Logger LOGGER = Logger.getLogger(Program.class);

    public static void main(String[] args) {
        SpringApplication.run(Program.class, args);
        LOGGER.info("Application was started!");
    }
}