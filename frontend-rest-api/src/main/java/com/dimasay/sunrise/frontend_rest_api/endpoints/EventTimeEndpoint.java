package com.dimasay.sunrise.frontend_rest_api.endpoints;

import com.dimasay.sunrise.domain.dto.EventTimeDTO;
import com.dimasay.sunrise.domain.services.EventTimeService;
import com.dimasay.sunrise.frontend_rest_api.dto.EventTimeResponseDTO;
import com.dimasay.sunrise.frontend_rest_api.dto.SunriseResponseDTO;
import com.dimasay.sunrise.frontend_rest_api.dto.SunsetResponseDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class EventTimeEndpoint {
    @Autowired
    private EventTimeService eventTimeService;
    private static final Logger LOGGER = Logger.getLogger(EventTimeEndpoint.class);


    @RequestMapping(path = "/event_time", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity getEventTime(@RequestParam(value = "action") String action, @RequestParam(value = "city") List<String> cityNames, @RequestParam(value = "date") String date) {
        switch (action) {
            case "sunrise":
                LOGGER.info(String.format("Search sunrise time for city/cities for date: &s started", date));
                Map<String, String> sunrise = eventTimeService.getSunriseTime(cityNames, date);
                return ResponseEntity.ok(convertMapToSunriseResponseList(sunrise));
            case "sunset":
                LOGGER.info(String.format("Search sunset time for city/cities for date: &s started", date));
                Map<String, String> sunset = eventTimeService.getSunsetTime(cityNames, date);
                return ResponseEntity.ok(convertMapToSunsetResponseList(sunset));
            case "all":
                LOGGER.info(String.format("Search sunrise and sunset times for city/cities for date: &s started", date));
                Map<String, EventTimeDTO> sunsetSunriseTimes = eventTimeService.getSunsetSunriseTimes(cityNames, date);
                return ResponseEntity.ok(convertMapToEventTimesResponseList(sunsetSunriseTimes));
            default:
                LOGGER.error(String.format("Action is wrong or null: &s", action));
                throw new InvalidParameterException("Invalid action value");
        }
    }

    private List<SunriseResponseDTO> convertMapToSunriseResponseList(Map<String, String> sunrise) {
        List<SunriseResponseDTO> sunriseResponseDTOS = new ArrayList<>();
        sunrise.forEach((key, value) -> sunriseResponseDTOS.add(new SunriseResponseDTO(key, value)));
        return sunriseResponseDTOS;
    }

    private List<SunsetResponseDTO> convertMapToSunsetResponseList(Map<String, String> sunrise) {
        List<SunsetResponseDTO> sunsetResponseDTOS = new ArrayList<>();
        sunrise.forEach((key, value) -> sunsetResponseDTOS.add(new SunsetResponseDTO(key, value)));
        return sunsetResponseDTOS;
    }

    private List<EventTimeResponseDTO> convertMapToEventTimesResponseList(Map<String, EventTimeDTO> sunsetSunriseTimes) {
        List<EventTimeResponseDTO> eventTimeResponseDTOS = new ArrayList<>();
        sunsetSunriseTimes.forEach((key, value) -> eventTimeResponseDTOS.add(new EventTimeResponseDTO(key, value.getSunrise(), value.getSunset())));
        return eventTimeResponseDTOS;
    }
}