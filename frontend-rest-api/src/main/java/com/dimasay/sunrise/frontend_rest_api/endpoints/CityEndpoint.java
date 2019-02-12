package com.dimasay.sunrise.frontend_rest_api.endpoints;

import com.dimasay.sunrise.domain.entities.City;
import com.dimasay.sunrise.domain.services.CityService;
import com.dimasay.sunrise.frontend_rest_api.dto.AddNewCityRequest;
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

    @RequestMapping(path = "/city", produces = "application/json", method = RequestMethod.GET)
    public List<City> getAllSupportedCities() {
        return cityService.getAllSupportedCities();
    }

    @RequestMapping(path = "/city", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity addNewCity(@RequestBody AddNewCityRequest addNewCityRequest) {
        cityService.addNewCity(addNewCityRequest.getName(), addNewCityRequest.getLatitude(), addNewCityRequest.getLongitude());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}