package com.demo.security;

import com.demo.entity.Customer;
import com.demo.entity.Password;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class AdminSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TestRestTemplate restTemplate;
    URI uri;
    @LocalServerPort int port;

    @Test
    public void adminAuthorizedGet() throws URISyntaxException {
        restTemplate = new TestRestTemplate("admin", "admin");
        uri = new URI("http://localhost:"+ port +"/customer");

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void adminUnauthorizedWrongPasswordGet() throws URISyntaxException {
        restTemplate = new TestRestTemplate("admin", "wrongPassword");
        uri = new URI("http://localhost:"+ port +"/customer");

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void adminUnauthorizedWrongNameGet() throws URISyntaxException {
        restTemplate = new TestRestTemplate("wrongName", "admin");
        uri = new URI("http://localhost:"+ port +"/customer");

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void adminAuthorizedPost() throws URISyntaxException {
        restTemplate = new TestRestTemplate("admin", "admin");
        uri = new URI("http://localhost:"+ port +"/customer");
        Customer customer = new Customer("name", new Password("password"));

        ResponseEntity<String> response = restTemplate.postForEntity(String.valueOf(uri), customer, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void adminUnauthorizedWrongNamePost() throws URISyntaxException {
        restTemplate = new TestRestTemplate("wrongName", "admin");
        uri = new URI("http://localhost:"+ port +"/customer");
        Customer customer = new Customer("name", new Password("password"));

        ResponseEntity<String> response = restTemplate.postForEntity(String.valueOf(uri), customer, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void adminUnauthorizedWrongPasswordPost() throws URISyntaxException {
        restTemplate = new TestRestTemplate("admin", "wrongPassword");
        uri = new URI("http://localhost:"+ port +"/customer");
        Customer customer = new Customer("name", new Password("password"));

        ResponseEntity<String> response = restTemplate.postForEntity(String.valueOf(uri), customer, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void adminOkDelete() throws Exception {
        mockMvc.perform(delete("/customer/1")).andExpect(status().isOk());
    }
}
