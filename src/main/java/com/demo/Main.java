package com.demo;

import com.demo.entity.Admin;
import com.demo.entity.Customer;
import com.demo.entity.Password;
import com.demo.service.AdminService;
import com.demo.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class Main {

    private final CustomerService customerService;
    private final AdminService adminService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    private void ifTableEmptySetDefaultUsernameAndPassword(){
        List<Customer> customers = customerService.findAll();
        if (customers.isEmpty()){
            customerService.create(new Customer("user", new Password("password")));
        }
        List<Admin> admins = adminService.findAll();
        if (admins.isEmpty()){
            adminService.create(new Admin("admin"));
        }
    }
}
