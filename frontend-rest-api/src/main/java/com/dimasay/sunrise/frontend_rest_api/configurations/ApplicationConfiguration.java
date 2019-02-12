package com.dimasay.sunrise.frontend_rest_api.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public ApplicationProperties applicationProperties(Environment env) {
        String jdbcUrl = env.getProperty("jdbcUrl", "jdbc:mysql://127.0.0.1:3306/sunrise?serverTimezone=UTC");
        String jdbcUser = env.getProperty("jdbcUser", "root");
        String jdbcPassword = env.getProperty("jdbcPassword", "111111");
        return new ApplicationProperties(jdbcUrl, jdbcUser, jdbcPassword);
    }
}