package com.dimasay.sunrise.frontend_rest_api.dto;

public class AddNewCityRequest {
    private String name;
    private Float latitude;
    private Float longitude;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLongitude() {
        return longitude;
    }
}
