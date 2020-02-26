package com.demo;

import com.demo.entity.Customer;
import com.demo.entity.Password;
import com.demo.repository.CustomerRepository;
import com.demo.servvice.CustomerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void findAll() {
        //given
        List<Customer> customer = Arrays.asList(
                new Customer("Name1", new Password("password1")),
                new Customer("Name2", new Password("password2"))
        );
        doReturn(customer).when(customerRepository).findAll();
        //when
        List<Customer> actualCustomer = customerServiceImpl.findAll();
        //then
        assertThat(actualCustomer).isEqualTo(customer);
    }

    @Test
    public void getOne() {
        //given
        Customer customer = new Customer("Name1", new Password("password1"));
        doReturn(customer).when(customerRepository).findById(1);
        //when
        Customer actualCustomer = customerServiceImpl.findById(1);
        //then
        assertThat(actualCustomer).isEqualTo(customer);
    }

    @Test
    public void create(){
        //given
        Customer customer = new Customer("Name", new Password("password"));
        doReturn(customer).when(customerRepository).save(customer);
        //when
        Customer actualCustomer = customerServiceImpl.create(customer);
        //then
        assertThat(actualCustomer).isEqualTo(customer);
    }

    @Test
    public void update() {
        //given
        Customer customer = new Customer("Name", new Password("password"));
        customer.setId(1);
        doReturn(customer).when(customerRepository).save(customer);
        doReturn(true).when(customerRepository).existsById(1);
        //when
        Customer actualCustomer = customerServiceImpl.update(customer);
        //then
        assertThat(actualCustomer).isEqualTo(customer);
    }

    @Test
    public void delete() {
        //given
        doReturn(true).when(customerRepository).existsById(1);
        //when
        assertTrue(customerServiceImpl.delete(1));
        //then
        verify(customerRepository, times(1)).deleteById(1);
    }
}
