package com.demo;

import com.demo.entity.Customer;
import com.demo.entity.Password;
import com.demo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class Main {

    private final CustomerRepository customerRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    private void ifTableEmptySetDefaultUsernameAndPassword(){
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()){
            customerRepository.save(new Customer("user", new Password("password")));
        }
    }
}
