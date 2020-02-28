package com.demo.service;

import com.demo.entity.Customer;
import com.demo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    final static Logger logger = Logger.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        try {
            logger.info("Attempt to get all customers");
            List<Customer> customers = customerRepository.findAll();
            logger.info("Attempt to get all customers - success");
            return customers;
        } catch (Exception e){
            logger.error("Attempt to get all customers - false\n" + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Customer findById(int id) {
        try {
            logger.info("Attempt to get customer by id");
            Customer customer = customerRepository.findById(id);
            if (customer != null){
                logger.info("Attempt to get customer by id - success");
                return customer;
            } else {
                logger.info(String.format("Customer by id: %d not found", id));
                return null;
            }
        } catch (Exception e){
            logger.error("Attempt to get customer by id - false\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public Customer create(Customer customer) {
        try {
            logger.info("Attempt to create a customer");
            customer = customerRepository.save(customer);
            logger.info(String.format("Customer created with ID: %d", customer.getId()));
            return customer;
        } catch (Exception e){
            logger.error("Attempt to create a customer - false\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public Customer update(Customer customer) {
        try {
            logger.info("Attempt to update a customer");
            int id = customer.getId();
            if (existsById(id)) {
                customer = customerRepository.save(customer);
                logger.info("Attempt to update a customer - success");
                return customer;
            } else {
                logger.info(String.format("Customer with id: %d not found", id));
                return null;
            }
        } catch (Exception e){
            logger.error("Attempt to update a customer - false\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            logger.info("Attempt to delete a customer");
            if (existsById(id)) {
                customerRepository.deleteById(id);
                logger.info("Attempt to delete a customer - success");
                return true;
            } else {
                logger.info(String.format("Customer with id: %d not found", id));
                return false;
            }
        } catch (Exception e){
            logger.error("Attempt to delete a customer - false\n" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean existsById(int id) {
        try {
            logger.info("Attempt to check customer by id");
            boolean flag = customerRepository.existsById(id);
            logger.info("Attempt to check customer by id - success");
            return flag;
        } catch (Exception e){
            logger.error("Attempt to check customer by id - false\n" + e.getMessage());
            return false;
        }
    }
}
