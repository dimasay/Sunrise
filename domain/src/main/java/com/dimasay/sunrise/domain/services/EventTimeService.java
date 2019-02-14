package com.dimasay.sunrise.domain.services;

import com.dimasay.sunrise.domain.dto.*;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class EventTimeService {
    private static final Logger LOGGER = Logger.getLogger(EventTimeService.class);
    private CityRepository cityRepository;
    private RestTemplate restTemplate;

    public EventTimeService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
        restTemplate = new RestTemplate();
    }


    public List<SunriseResponseDTO> getSunriseTime(GetEventTimeDTO getEventTimeDTO) {
        if (getEventTimeDTO.getCityNames().isEmpty() || getEventTimeDTO.getDate().length() == 0) {
            LOGGER.error(String.format("One of required parameters is missing: cityNames = %s, date = %s", getEventTimeDTO.getCityNames().size(), getEventTimeDTO.getDate()));
            return new ArrayList<>();
        } else {
            List<SunriseResponseDTO> sunriseResponseDTOS = new ArrayList<>();
            getEventTimeDTO.getCityNames().forEach(cityName -> {
                try {
                    String sunrise = getEventTime(cityName, getEventTimeDTO.getDate(), getEventTimeDTO.getHoursToAdd()).getSunrise();
                    if (sunrise != null) {
                        sunriseResponseDTOS.add(new SunriseResponseDTO(cityName, sunrise));
                    }
                } catch (ParseException e) {
                    LOGGER.error(e.getMessage());
                }
            });

            LOGGER.info("Sunrise time was found for all requested supported cities");
            return sunriseResponseDTOS;
        }
    }

    public List<SunsetResponseDTO> getSunsetTime(GetEventTimeDTO getEventTimeDTO) {
        if (getEventTimeDTO.getCityNames().isEmpty() || getEventTimeDTO.getDate().length() == 0) {
            String message = String.format("One of required parameters is missing: cityNames = %s, date = %s", getEventTimeDTO.getCityNames().size(), getEventTimeDTO.getDate());
            LOGGER.error(message);
            throw new InvalidParameterException(message);
        } else {
            List<SunsetResponseDTO> sunsetResponseDTOS = new ArrayList<>();
            getEventTimeDTO.getCityNames().forEach(cityName -> {
                try {
                    String sunset = getEventTime(cityName, getEventTimeDTO.getDate(), getEventTimeDTO.getHoursToAdd()).getSunset();
                    if (sunset != null) {
                        sunsetResponseDTOS.add(new SunsetResponseDTO(cityName, sunset));
                    }
                } catch (ParseException e) {
                    LOGGER.error(e.getMessage());
                }
            });

            LOGGER.info("Sunset time was found for all requested supported cities");
            return sunsetResponseDTOS;
        }
    }

    public List<EventTimeResponseDTO> getSunsetSunriseTimes(GetEventTimeDTO getEventTimeDTO) {
        if (getEventTimeDTO.getCityNames().isEmpty() || getEventTimeDTO.getDate().length() == 0) {
            LOGGER.error(String.format("One of required parameters is missing: cityNames = %s, date = %s", getEventTimeDTO.getCityNames().size(), getEventTimeDTO.getDate()));
            throw new InvalidParameterException("One of parameters is null");
        } else {
            List<EventTimeResponseDTO> eventTimeResponseDTOS = new ArrayList<>();
            getEventTimeDTO.getCityNames().forEach(cityName -> {
                try {
                    EventTimeDTO eventTimeDTO = getEventTime(cityName, getEventTimeDTO.getDate(), getEventTimeDTO.getHoursToAdd());
                    if (eventTimeDTO != null) {
                        eventTimeResponseDTOS.add(new EventTimeResponseDTO(cityName, eventTimeDTO.getSunrise(), eventTimeDTO.getSunset()));
                    }
                } catch (ParseException e) {
                    LOGGER.error(e.getMessage());
                }
            });

            LOGGER.info("Sunrise and sunset time were found for all requested supported cities");
            return eventTimeResponseDTOS;
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