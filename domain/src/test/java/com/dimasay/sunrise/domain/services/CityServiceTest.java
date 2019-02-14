package com.dimasay.sunrise.domain.services;

import com.dimasay.sunrise.domain.entities.City;
import com.dimasay.sunrise.domain.repositories.CityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class CityServiceTest {
    private CityService cityService;
    private CityRepository cityRepository;

    @Before
    public void setUp() {
        cityRepository = Mockito.mock(CityRepository.class);
        cityService = new CityService(cityRepository);
    }

    @Test
    public void addNewCity() {
        when(cityRepository.findByName(eq("Odessa"))).thenReturn(null);
        doAnswer(returnsFirstArg()).when(cityRepository).save(any(City.class));

        City city = cityService.addNewCity("Lvov", 49.837043F, 24.023464F);
        assertEquals("Lvov", city.getName());
    }

    @Test
    public void getAllSupportedCities() {
        List<City> cities = cityService.getAllSupportedCities();
        assertNotNull(cities);
    }
}