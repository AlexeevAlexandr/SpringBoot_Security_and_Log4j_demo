package com.demo.servvice;

import com.demo.entity.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> findAll();

    Customer findById(int id);

    Customer create(Customer customer);

    Customer update(Customer customer);

    boolean delete(int id);

    boolean existsById(int id);
}
