package com.dimasay.sunrise.domain.services;

import com.dimasay.sunrise.domain.entities.City;
import com.dimasay.sunrise.domain.repositories.CityRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {
    private static final Logger LOGGER = Logger.getLogger(CityService.class);
    private CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    public List<String> getAllSupportedCities() {
        List<String> supportedCities = new ArrayList<>();
        cityRepository.findAll().forEach(city -> supportedCities.add(city.getName()));
        LOGGER.info("List of supported cities received.");
        return supportedCities;
    }

    public City addNewCity(String name, Float latitude, Float longitude) {
        if (name == null || latitude == null || longitude == null) {
            LOGGER.error(String.format("One of required variables is null! cityName=%s, latitude=%s, longitude=%s.", name, latitude, longitude));
            throw new InvalidParameterException("One of parameters is null.");
        } else {
            City newCity = new City(name, latitude, longitude);
            cityRepository.save(newCity);
            LOGGER.info(String.format("New city &s created.", newCity.getName()));
            return newCity;
        }
    }
}
