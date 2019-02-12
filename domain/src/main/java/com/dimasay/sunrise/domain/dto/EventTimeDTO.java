package com.dimasay.sunrise.domain.dto;

public class EventTimeDTO {
    private String sunrise;
    private String sunset;

    public EventTimeDTO() {
    }

    public EventTimeDTO(String sunrise, String sunset) {
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
