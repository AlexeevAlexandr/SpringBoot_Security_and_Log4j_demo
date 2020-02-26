package com.demo.repository;

import com.demo.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    @Override
    List<Customer> findAll();

    Customer findById(int id);

    Customer findByName(String name);
}
