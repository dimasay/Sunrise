package com.dimasay.sunrise.frontend_rest_api.dto;

public class SunsetResponseDTO {
    private String city;
    private String sunset;

    public SunsetResponseDTO(String city, String sunset) {
        this.city = city;
        this.sunset = sunset;
    }

    public String getCity() {
        return city;
    }

    public String getSunset() {
        return sunset;
    }
}
