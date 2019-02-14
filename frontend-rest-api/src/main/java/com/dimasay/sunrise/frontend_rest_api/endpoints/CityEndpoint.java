package com.dimasay.sunrise.frontend_rest_api.endpoints;

import com.dimasay.sunrise.domain.entities.City;
import com.dimasay.sunrise.domain.services.CityService;
import com.dimasay.sunrise.frontend_rest_api.dto.AddNewCityRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityEndpoint {
    @Autowired
    private CityService cityService;
    private static final Logger LOGGER = Logger.getLogger(CityEndpoint.class);

    @RequestMapping(path = "/city", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity getAllSupportedCities() {
        LOGGER.info("Getting all supported cities started.");
        List<City> cities = cityService.getAllSupportedCities();
        if (cities.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(cities);
        }
    }

    @RequestMapping(path = "/city", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity addNewCity(@RequestBody AddNewCityRequest addNewCityRequest) {
        LOGGER.info("Adding new city started.");
        City city = cityService.addNewCity(addNewCityRequest.getName(), addNewCityRequest.getLatitude(), addNewCityRequest.getLongitude());
        if (city != null) {
            LOGGER.info(String.format("City %s saved to database.", addNewCityRequest.getName()));
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}