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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventTimeService {
    @Autowired
    private CityRepository cityRepository;

    public Map<String, String> getSunriseTime(List<String> cityNames, String date) {
        if (cityNames.isEmpty() || date.length() == 0) {
            throw new InvalidParameterException("One of parameters is null");
        } else {
            Map<String, String> sunriseTimes = new HashMap<>();
            cityNames.forEach(cityName -> sunriseTimes.put(cityName, getEventTime(cityName, date).getSunrise()));
            return sunriseTimes;
        }
    }

    public Map<String, String> getSunsetTime(List<String> cityNames, String date) {
        if (cityNames.isEmpty() || date.length() == 0) {
            throw new InvalidParameterException("One of parameters is null");
        } else {
            Map<String, String> sunsetTimes = new HashMap<>();
            cityNames.forEach(cityName -> sunsetTimes.put(cityName, getEventTime(cityName, date).getSunset()));
            return sunsetTimes;
        }
    }

    public Map<String, EventTimeDTO> getSunsetSunriseTimes(List<String> cityNames, String date) {
        if (cityNames.isEmpty() || date.length() == 0) {
            throw new InvalidParameterException("One of parameters is null");
        } else {
            Map<String, EventTimeDTO> sunsetSunriseTimes = new HashMap<>();
            cityNames.forEach(cityName -> sunsetSunriseTimes.put(cityName, getEventTime(cityName, date)));
            return sunsetSunriseTimes;
        }
    }

    private EventTimeDTO getEventTime(String cityName, String date) {
        try {
            City city = cityRepository.findByName(cityName);
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
            return new EventTimeDTO(result.get("sunrise").getAsString(), result.get("sunset").getAsString());
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}