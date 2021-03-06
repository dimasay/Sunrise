package com.dimasay.sunrise.domain.dto;

public class EventTimeResponseDTO {
    private String city;
    private String sunrise;
    private String sunset;

    public EventTimeResponseDTO() {
    }

    public EventTimeResponseDTO(String city, String sunrise, String sunset) {
        this.city = city;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getCity() {
        return city;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }
}
