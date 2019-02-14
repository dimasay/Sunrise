package com.dimasay.sunrise.domain.dto;

public class SunsetResponseDTO {
    private String city;
    private String sunset;

    public SunsetResponseDTO() {
    }

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
