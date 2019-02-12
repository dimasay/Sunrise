package com.dimasay.sunrise.frontend_rest_api.dto;

public class AddNewCityRequest {
    private String name;
    private Float latitude;
    private Float longitude;

    public String getName() {
        return name;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }
}
