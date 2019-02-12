package com.dimasay.sunrise.frontend_rest_api.dto;

public class SunriseResponseDTO {
    private String city;
    private String sunrise;

    public SunriseResponseDTO(String city, String sunrise) {
        this.city = city;
        this.sunrise = sunrise;
    }

    public String getCity() {
        return city;
    }

    public String getSunrise() {
        return sunrise;
    }
}
