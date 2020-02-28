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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class UserSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TestRestTemplate restTemplate;
    URI uri;
    @LocalServerPort int port;

    @Test
    public void userOkGet() throws URISyntaxException {
        restTemplate = new TestRestTemplate("user", "password");
        uri = new URI("http://localhost:"+ port +"/customer");

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void userUnauthorizedWrongPasswordGet() throws URISyntaxException {
        restTemplate = new TestRestTemplate("user", "wrongPassword");
        uri = new URI("http://localhost:"+ port +"/customer");

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void userUnauthorizedWrongNameGet() throws URISyntaxException {
        restTemplate = new TestRestTemplate("wrongUser", "password");
        uri = new URI("http://localhost:"+ port +"/customer");

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void userForbiddenPost() throws URISyntaxException {
        restTemplate = new TestRestTemplate("user", "password");
        uri = new URI("http://localhost:"+ port +"/customer");
        Customer customer = new Customer("name", new Password("password"));

        ResponseEntity<String> response = restTemplate.postForEntity(String.valueOf(uri), customer, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void userUnauthorizedPost() throws URISyntaxException {
        restTemplate = new TestRestTemplate("wrongUser", "password");
        uri = new URI("http://localhost:"+ port +"/customer");
        Customer customer = new Customer("name", new Password("password"));

        ResponseEntity<String> response = restTemplate.postForEntity(String.valueOf(uri), customer, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @WithMockUser(username = "user")
    @Test
    public void userForbiddenPut() throws Exception {
        mockMvc.perform(put("/customer")).andExpect(status().isForbidden());
    }

    @Test
    public void userUnauthorizedPut() throws Exception {
        mockMvc.perform(put("/customer")).andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "user")
    @Test
    public void userForbiddenDelete() throws Exception {
        mockMvc.perform(delete("/customer/1")).andExpect(status().isForbidden());
    }

    @Test
    public void userUnauthorizedDelete() throws Exception {
        mockMvc.perform(delete("/customer/1")).andExpect(status().isUnauthorized());
    }
}
