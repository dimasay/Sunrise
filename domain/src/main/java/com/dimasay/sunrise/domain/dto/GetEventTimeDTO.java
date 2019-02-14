package com.dimasay.sunrise.domain.dto;

import java.util.List;

public class GetEventTimeDTO {
    private List<String> cityNames;
    private String date;
    private int hoursToAdd;

    public GetEventTimeDTO(List<String> cityNames, String date, int hoursToAdd) {
        this.cityNames = cityNames;
        this.date = date;
        this.hoursToAdd = hoursToAdd;
    }

    public List<String> getCityNames() {
        return cityNames;
    }

    public void setCityNames(List<String> cityNames) {
        this.cityNames = cityNames;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHoursToAdd() {
        return hoursToAdd;
    }

    public void setHoursToAdd(int hoursToAdd) {
        this.hoursToAdd = hoursToAdd;
    }
}
