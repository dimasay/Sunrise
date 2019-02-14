package com.dimasay.sunrise.domain.services;

import com.dimasay.sunrise.domain.dto.EventTimeDTO;
import com.dimasay.sunrise.domain.dto.ResultsDTO;
import com.dimasay.sunrise.domain.dto.SunriseSunsetResponse;
import com.dimasay.sunrise.domain.entities.City;
import com.dimasay.sunrise.domain.repositories.CityRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidParameterException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventTimeService {
    private static final Logger LOGGER = Logger.getLogger(EventTimeService.class);
    private CityRepository cityRepository;
    private RestTemplate restTemplate;

    public EventTimeService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
        restTemplate = new RestTemplate();
    }


    public Map<String, String> getSunriseTime(List<String> cityNames, String date, int hoursToAdd) {
        if (cityNames.isEmpty() || date.length() == 0) {
            String message = String.format("One of required parameters is missing: cityNames = %s, date = %s", cityNames.size(), date);
            LOGGER.error(message);
            throw new InvalidParameterException(message);
        } else {
            Map<String, String> sunriseTimes = new HashMap<>();
            cityNames.forEach(cityName -> {
                try {
                    EventTimeDTO eventTimeDTO = getEventTime(cityName, date, hoursToAdd);
                    if (eventTimeDTO != null) {
                        sunriseTimes.put(cityName, eventTimeDTO.getSunrise());
                    }
                } catch (ParseException e) {
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                }
            });

            LOGGER.info("Sunrise time was found for all requested supported cities");
            return sunriseTimes;
        }
    }

    public Map<String, String> getSunsetTime(List<String> cityNames, String date, int hoursToAdd) {
        if (cityNames.isEmpty() || date.length() == 0) {
            String message = String.format("One of required parameters is missing: cityNames = %s, date = %s", cityNames.size(), date);
            LOGGER.error(message);
            throw new InvalidParameterException(message);
        } else {
            Map<String, String> sunsetTimes = new HashMap<>();

            cityNames.forEach(cityName -> {
                try {
                    EventTimeDTO eventTimeDTO = getEventTime(cityName, date, hoursToAdd);
                    if (eventTimeDTO != null) {
                        sunsetTimes.put(cityName, eventTimeDTO.getSunset());
                    }
                } catch (ParseException e) {
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                }
            });

            LOGGER.info("Sunset time was found for all requested supported cities");
            return sunsetTimes;
        }
    }

    public Map<String, EventTimeDTO> getSunsetSunriseTimes(List<String> cityNames, String date, int hoursToAdd) {
        if (cityNames.isEmpty() || date.length() == 0) {
            LOGGER.error(String.format("One of required parameters is missing: cityNames = %s, date = %s", cityNames.size(), date));
            throw new InvalidParameterException("One of parameters is null");
        } else {
            Map<String, EventTimeDTO> sunsetSunriseTimes = new HashMap<>();

            cityNames.forEach(cityName -> {
                try {
                    EventTimeDTO eventTimeDTO = getEventTime(cityName, date, hoursToAdd);
                    if (eventTimeDTO != null) {
                        sunsetSunriseTimes.put(cityName, eventTimeDTO);
                    }
                } catch (ParseException e) {
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                }
            });

            LOGGER.info("Sunrise and sunset time were found for all requested supported cities");
            return sunsetSunriseTimes;
        }
    }

    private EventTimeDTO getEventTime(String cityName, String date, int hoursToAdd) throws ParseException {
        City city = cityRepository.findByName(cityName);
        if (city == null) {
            LOGGER.error(String.format("City: %s not supported", cityName));
            return null;
        } else {
            String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", city.getLatitude(), city.getLongitude(), date);
            ResultsDTO results = restTemplate.getForObject(url, SunriseSunsetResponse.class).getResults();

            SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm:ss a");
            LocalTime sunriseTime = new Time(formatDate.parse(results.getSunrise()).getTime()).toLocalTime();
            LocalTime sunsetTime = new Time(formatDate.parse(results.getSunset()).getTime()).toLocalTime();
            String sunriseTimeInZone = sunriseTime.plusHours(hoursToAdd).toString();
            String sunsetTimeInZone = sunsetTime.plusHours(hoursToAdd).toString();

            LOGGER.info(String.format("Sunrise and sunset times were found for city: %s", cityName));
            return new EventTimeDTO(sunriseTimeInZone, sunsetTimeInZone);
        }
    }
}