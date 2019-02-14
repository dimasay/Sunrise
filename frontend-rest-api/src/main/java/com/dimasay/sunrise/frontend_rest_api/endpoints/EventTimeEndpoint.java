package com.dimasay.sunrise.frontend_rest_api.endpoints;

import com.dimasay.sunrise.domain.dto.EventTimeResponseDTO;
import com.dimasay.sunrise.domain.dto.GetEventTimeDTO;
import com.dimasay.sunrise.domain.dto.SunriseResponseDTO;
import com.dimasay.sunrise.domain.dto.SunsetResponseDTO;
import com.dimasay.sunrise.domain.services.EventTimeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventTimeEndpoint {
    private static final Logger LOGGER = Logger.getLogger(EventTimeEndpoint.class);
    @Autowired
    private EventTimeService eventTimeService;

    @RequestMapping(path = "/event_time", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity getEventTime(@RequestParam(value = "action") String action, @RequestParam(value = "hours") int hours, @RequestParam(value = "city") List<String> cityNames, @RequestParam(value = "date") String date) {
        GetEventTimeDTO getEventTimeDTO = new GetEventTimeDTO(cityNames, date, hours);
        switch (action) {
            case "sunrise":
                return getSunrise(getEventTimeDTO, date);
            case "sunset":
                return getSunset(getEventTimeDTO, date);
            case "all":
                return getSunriseSunset(getEventTimeDTO, date);
            default:
                LOGGER.error(String.format("Action is wrong or null: %s", action));
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity getSunrise(GetEventTimeDTO getEventTimeDTO, String date) {
        LOGGER.info(String.format("Search sunrise time for city/cities for date: %s started", date));
        List<SunriseResponseDTO> sunriseResponseDTOS = eventTimeService.getSunriseTime(getEventTimeDTO);
        if (sunriseResponseDTOS.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(sunriseResponseDTOS);
        }
    }

    private ResponseEntity getSunset(GetEventTimeDTO getEventTimeDTO, String date) {
        LOGGER.info(String.format("Search sunset time for city/cities for date: %s started", date));
        List<SunsetResponseDTO> sunsetResponseDTOS = eventTimeService.getSunsetTime(getEventTimeDTO);
        if (sunsetResponseDTOS.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(sunsetResponseDTOS);
        }
    }

    private ResponseEntity getSunriseSunset(GetEventTimeDTO getEventTimeDTO, String date) {
        LOGGER.info(String.format("Search sunrise and sunset times for city/cities for date: %s started", date));
        List<EventTimeResponseDTO> eventTimeResponseDTOS = eventTimeService.getSunsetSunriseTimes(getEventTimeDTO);
        if (eventTimeResponseDTOS.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(eventTimeResponseDTOS);
        }
    }
}