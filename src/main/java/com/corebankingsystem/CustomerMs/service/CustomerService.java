package com.corebankingsystem.CustomerMs.service;

import com.corebankingsystem.CustomerMs.model.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


public interface CustomerService {

    public ResponseEntity<Object> createCustomer(Customer customer);
    public List<Customer> getCustomers();
    public Optional<Customer> getCustomerById(Long id);
    public ResponseEntity<Customer> updateCustomer(Long id, Customer customer);
    public ResponseEntity<Object> deleteCustomer(Long id);

}
