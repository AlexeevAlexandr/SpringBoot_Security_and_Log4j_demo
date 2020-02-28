package com.demo.controller;

import com.demo.entity.Customer;
import com.demo.exception.CustomerException;
import com.demo.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/customer")
@AllArgsConstructor
public class Controller {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> findAll(){
        return customerService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Customer findById(@NotNull(message = "Id can not be empty") @PathVariable int id){
        if (!customerService.existsById(id)){
            throw new CustomerException(String.format("Id: %d not found", id));
        } else {
            return customerService.findById(id);
        }
    }

    @PostMapping
    public Customer create(@Valid @RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @PutMapping
    public Customer update(@Valid @RequestBody Customer customer) {
        if (!customerService.existsById(customer.getId())){
            throw new CustomerException(String.format("Could not update, id: %d not found", customer.getId()));
        } else {
            return customerService.update(customer);
        }
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@NotNull(message = "Id can not be empty") @PathVariable int id) {
        customerService.delete(id);
    }
}
