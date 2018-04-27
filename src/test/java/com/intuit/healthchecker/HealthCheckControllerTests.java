package com.intuit.healthchecker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationLauncher.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class HealthCheckControllerTests {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

	@Test
	public void givenMultipleEndpoints_whenHealthRequested_thenReturnedEndpointsHealth() throws Exception {
	    String response1 = "{\"service1\":{\"healthy\":true,\"message\":\"OK\"}}";
	    String response2 = "{\"service2\":{\"healthy\":true,\"message\":\"OK\"}}";

        MockRestServiceServer mockApi = MockRestServiceServer.createServer(restTemplate);

        mockApi.expect(requestTo("http://service1/health"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response1, MediaType.APPLICATION_JSON));

        mockApi.expect(requestTo("http://service2/health"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response2, MediaType.APPLICATION_JSON));

		mockMvc.perform(get("/t360wsendpoints/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("service1.healthy").value(is(true)))
                .andExpect(jsonPath("service2.healthy").value(is(true)));
    }
}
