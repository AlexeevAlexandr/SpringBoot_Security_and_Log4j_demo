package com.demo.controller;

import com.demo.entity.Customer;
import com.demo.entity.Password;
import com.demo.helper.TestHelper;
import com.demo.service.CustomerService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static javax.servlet.http.HttpServletResponse.*;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ControllerTest {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private TestHelper testHelper;

    @Before
    public final void setup() {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void findAll() {
        when().get("/customer").
                then().statusCode(SC_OK);
    }

    @Test
    public void findById() {
        // create
        Customer customer = customerService.create(new Customer("user", new Password("password")));
        int id = customer.getId();
        try {
            // get by id
            when().get("/customer/" + id).
                    then().statusCode(SC_OK).body("id", equalTo(id));
            System.out.println(customerService.findById(id));
        } finally {
            // delete
            if (customerService.existsById(id)) {
                customerService.delete(id);
            }
        }
    }

    @Test
    public void findByIdNotFound() {
        when().get("/customer/0").
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    public void create() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        int id =
                given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toJSONString()).
                        when().post("/customer").
                        then().statusCode(SC_OK).extract().path("id");
        customerService.delete(id);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createWithEmptyName() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("name", "");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/customer").
                then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void update() {
        // create
        Customer customer = customerService.create(new Customer("user", new Password("password")));
        int id = customer.getId();

        try {
            // change
            customer.setName("another user");

            // update
            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(customer).
                    when().
                    put("/customer").
                    then().statusCode(SC_OK);

            // check
            Customer checkCustomer = customerService.findById(id);
            Assert.assertEquals(customer.toString(), checkCustomer.toString());
        } finally {
            // delete
            if (customerService.existsById(id)) {
                customerService.delete(id);
            }
        }
    }

    @Test
    public void updateWithWrongId() {
        // create
        Customer customer = new Customer("user", new Password("password"));
        customer.setId(0);

        // update
        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                body(customer).
                when().
                put("/customer").
                then().statusCode(SC_NOT_FOUND);

    }

    @Test
    public void updateWithEmptyId() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                body(jsonObject.toString()).
                when().
                put("/customer").
                then().
                statusCode(SC_NOT_FOUND);
    }

    @Test
    public void updateWithNullName() {
        // create
        Customer customer = customerService.create(new Customer("user", new Password("password")));
        int id = customer.getId();

        try {
            // change
            customer.setName(null);

            //update
            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(customer).
                    when().
                    put("/customer").
                    then().statusCode(SC_BAD_REQUEST);
        } finally {
            // delete
            if (customerService.existsById(id)) {
                customerService.delete(id);
            }
        }
    }

    @Test
    public void updateWithEmptyName() {
        // create
        Customer customer = customerService.create(new Customer("user", new Password("password")));
        int id = customer.getId();

        try {
            // change
            customer.setName("");

            //update
            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(customer).
                    when().
                    put("/customer").
                    then().statusCode(SC_BAD_REQUEST);
        } finally {
            // delete
            if (customerService.existsById(id)) {
                customerService.delete(id);
            }
        }
    }
}
