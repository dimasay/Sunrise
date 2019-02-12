package com.dimasay.sunrise.domain.services;

import com.dimasay.sunrise.domain.dto.EventTimeDTO;
import com.dimasay.sunrise.domain.entities.City;
import com.dimasay.sunrise.domain.repositories.CityRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
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
    @Autowired
    private CityRepository cityRepository;
    private static final Logger LOGGER = Logger.getLogger(EventTimeService.class);

    public Map<String, String> getSunriseTime(List<String> cityNames, String date) {
        if (cityNames.isEmpty() || date.length() == 0) {
            LOGGER.error(String.format("One of required parameters is missing: cityNames = %s, date = %s", cityNames.size(), date));
            throw new InvalidParameterException("One of parameters is null");
        } else {
            Map<String, String> sunriseTimes = new HashMap<>();
            cityNames.forEach(cityName -> {
                EventTimeDTO eventTimeDTO = getEventTime(cityName, date);
                if (eventTimeDTO != null) {
                    sunriseTimes.put(cityName, eventTimeDTO.getSunrise());
                }
            });
            LOGGER.info("Sunrise time was found for all requested supported cities");
            return sunriseTimes;
        }
    }

    public Map<String, String> getSunsetTime(List<String> cityNames, String date) {
        if (cityNames.isEmpty() || date.length() == 0) {
            LOGGER.error(String.format("One of required parameters is missing: cityNames = %s, date = %s", cityNames.size(), date));
            throw new InvalidParameterException("One of parameters is null");
        } else {
            Map<String, String> sunsetTimes = new HashMap<>();
            cityNames.forEach(cityName -> {
                EventTimeDTO eventTimeDTO = getEventTime(cityName, date);
                if (eventTimeDTO != null) {
                    sunsetTimes.put(cityName, eventTimeDTO.getSunset());
                }
            });
            LOGGER.info("Sunset time was found for all requested supported cities");
            return sunsetTimes;
        }
    }

    public Map<String, EventTimeDTO> getSunsetSunriseTimes(List<String> cityNames, String date) {
        if (cityNames.isEmpty() || date.length() == 0) {
            LOGGER.error(String.format("One of required parameters is missing: cityNames = %s, date = %s", cityNames.size(), date));
            throw new InvalidParameterException("One of parameters is null");
        } else {
            Map<String, EventTimeDTO> sunsetSunriseTimes = new HashMap<>();
            cityNames.forEach(cityName -> {
                EventTimeDTO eventTimeDTO = getEventTime(cityName, date);
                if (eventTimeDTO != null) {
                    sunsetSunriseTimes.put(cityName, eventTimeDTO);
                }
            });
            LOGGER.info("Sunrise and sunset time were found for all requested supported cities");
            return sunsetSunriseTimes;
        }
    }

    private EventTimeDTO getEventTime(String cityName, String date) {
        try {
            City city = cityRepository.findByName(cityName);
            if (city == null) {
                LOGGER.error(String.format("City: %s not supported", cityName));
                return null;
            } else {
                URIBuilder builder = new URIBuilder("https://api.sunrise-sunset.org/json");
                builder.setParameter("lat", Double.toString(city.getLatitude()));
                builder.setParameter("lng", Double.toString(city.getLongitude()));
                builder.setParameter("date", date);
                HttpGet getProjectRequest = new HttpGet(builder.build());

                HttpClient httpClient = HttpClientBuilder.create().build();
                CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(getProjectRequest);

                HttpEntity entity = response.getEntity();
                String json = EntityUtils.toString(entity);
                JsonParser jsonParser = new JsonParser();
                JsonObject responseJson = (JsonObject) jsonParser.parse(json);
                JsonObject result = responseJson.getAsJsonObject("results");
                String sunrise = result.get("sunrise").getAsString();
                String sunset = result.get("sunset").getAsString();

                SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm:ss a");
                LocalTime sunriseTime = new Time(formatDate.parse(sunrise).getTime()).toLocalTime();
                LocalTime sunsetTime = new Time(formatDate.parse(sunset).getTime()).toLocalTime();
            /*sunrise-sunset.org returns time in UTC +00:00 format. This application working only with Ukrainian cities.
            Ukraine time format is UTC+02:00, because I just add 2 hours to response time.
            */
                String sunriseTimeInZone = sunriseTime.plusHours(2).toString();
                String sunsetTimeInZone = sunsetTime.plusHours(2).toString();

                LOGGER.info(String.format("Sunrise/sunset times were found for city: %s", cityName));
                return new EventTimeDTO(sunriseTimeInZone, sunsetTimeInZone);
            }
        } catch (URISyntaxException | IOException | ParseException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}