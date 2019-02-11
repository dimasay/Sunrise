package com.dimasay.sunrise.frontend_rest_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dimasay.sunrise")
public class Program {
    public static void main(String[] args) {
        SpringApplication.run(Program.class, args);
    }
}