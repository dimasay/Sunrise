package com.dimasay.sunrise.frontend_rest_api.endpoints;

import com.dimasay.sunrise.frontend_rest_api.dto.EventTimeResponseDTO;
import com.dimasay.sunrise.frontend_rest_api.dto.SunriseResponseDTO;
import com.dimasay.sunrise.frontend_rest_api.dto.SunsetResponseDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class EventTimeEndpointTest extends AbstractTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getEventTimeForSunrise() throws Exception {
        String action = "sunrise";
        String content = getEventTime(action);
        SunriseResponseDTO[] citiesList = super.mapFromJson(content, SunriseResponseDTO[].class);
        assertTrue(citiesList.length > 0);
    }

    @Test
    public void getEventTimeForSunset() throws Exception {
        String action = "sunset";
        String content = getEventTime(action);
        SunsetResponseDTO[] citiesList = super.mapFromJson(content, SunsetResponseDTO[].class);
        assertTrue(citiesList.length > 0);
    }

    @Test
    public void getEventTimeForSunriseAndSunset() throws Exception {
        String action = "all";
        String content = getEventTime(action);
        EventTimeResponseDTO[] citiesList = super.mapFromJson(content, EventTimeResponseDTO[].class);
        assertTrue(citiesList.length > 0);
    }

    private String getEventTime(String action) throws Exception {
        String uri = "/event_time";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("action", action)
                .param("city", "Odessa")
                .param("date", "2019-02-12")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        return mvcResult.getResponse().getContentAsString();
    }
}
