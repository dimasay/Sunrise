package com.dimasay.sunrise.frontend_rest_api.configurations;

public class ApplicationProperties {
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;

    public ApplicationProperties(String jdbcUrl, String jdbcUser, String jdbcPassword) {
        this.jdbcUrl = jdbcUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPassword = jdbcPassword;
    }

    String getJdbcUrl() {
        return jdbcUrl;
    }

    String getJdbcUser() {
        return jdbcUser;
    }

    String getJdbcPassword() {
        return jdbcPassword;
    }
}
