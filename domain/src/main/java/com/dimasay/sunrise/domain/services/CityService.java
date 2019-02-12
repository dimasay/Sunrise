package com.dimasay.sunrise.domain.services;

import com.dimasay.sunrise.domain.entities.City;
import com.dimasay.sunrise.domain.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> getAllSupportedCities() {
        List<City> cities = new ArrayList<>();
        cityRepository.findAll().forEach(city -> cities.add(city));
        return cities;
    }

    public void addNewCity(String name, Float latitude, Float longitude) {
        if (name == null || latitude == null || longitude == null) {
            throw new InvalidParameterException("One of parameters is null");
        } else {
            City newCity = new City(name, latitude, longitude);
            cityRepository.save(newCity);
        }
    }
}
