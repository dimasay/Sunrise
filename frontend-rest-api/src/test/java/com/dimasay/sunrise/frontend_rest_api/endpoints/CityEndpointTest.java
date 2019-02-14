package com.dimasay.sunrise.frontend_rest_api.endpoints;

import com.dimasay.sunrise.domain.entities.City;
import com.dimasay.sunrise.domain.repositories.CityRepository;
import com.dimasay.sunrise.frontend_rest_api.dto.AddNewCityRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class CityEndpointTest extends AbstractTest {
    @Autowired
    private CityRepository cityRepository;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void addNewCity() throws Exception {
        int status = addCity().getResponse().getStatus();
        assertEquals(200, status);

        cityRepository.delete(cityRepository.findByName("Kotovsk"));
    }

    @Test
    public void getAllSupportedCities() throws Exception {
        addCity();

        String uri = "/city";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        City[] citiesList = super.mapFromJson(content, City[].class);
        assertTrue(citiesList.length > 0);

        cityRepository.delete(cityRepository.findByName("Kotovsk"));
    }

    private MvcResult addCity() throws Exception {
        String uri = "/city";
        AddNewCityRequest addNewCityRequest = new AddNewCityRequest();
        addNewCityRequest.setName("Kotovsk");
        addNewCityRequest.setLatitude(47.737626F);
        addNewCityRequest.setLongitude(29.555124F);

        String inputJson = super.mapToJson(addNewCityRequest);
        return mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
    }
}