package com.demo;

import com.demo.entity.Customer;
import com.demo.entity.Password;
import com.demo.repository.CustomerRepository;
import com.demo.servvice.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class Main {

    private final CustomerService customerService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    private void ifTableEmptySetDefaultUsernameAndPassword(){
        List<Customer> customers = customerService.findAll();
        if (customers.isEmpty()){
            customerService.create(new Customer("user", new Password("password")));
            customerService.create(new Customer("admin", new Password("admin")));
        }
    }
}
