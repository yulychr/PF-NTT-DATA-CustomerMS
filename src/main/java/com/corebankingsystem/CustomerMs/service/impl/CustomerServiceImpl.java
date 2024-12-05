package com.corebankingsystem.CustomerMs.service.impl;

import com.corebankingsystem.CustomerMs.model.Customer;
import com.corebankingsystem.CustomerMs.repository.CustomerRepository;
import com.corebankingsystem.CustomerMs.service.AccountService;
import com.corebankingsystem.CustomerMs.service.CustomerService;
import com.corebankingsystem.CustomerMs.service.CustomerValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerValidationService customerValidationService;
    @Autowired
    private AccountService accountService;

    @Override
    public ResponseEntity<Object> createCustomer(Customer customer) {
        Map<String, String> errors = customerValidationService.validateCustomerFields(customer);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Optional<Customer> dni_already_exists = customerRepository.findByDni(customer.getDni());
        if (dni_already_exists.isPresent()) {
            return ResponseEntity.status(409).body(Map.of("message", "El DNI ya existe"));
        }
        Customer customerCreated = customerRepository.save(customer);
        return ResponseEntity.status(201).body(customerCreated);
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> updateCustomer(Long id, Customer customer) {
        return getCustomerById(id)
                .map(existingCustomer -> {
                    Map<String, String> errors = customerValidationService.validateCustomerFields(customer);
                    if (!errors.isEmpty()) {
                        return ResponseEntity.badRequest().body(errors);
                    }
                    customer.setId(id);
                    Customer updatedCustomer = customerRepository.save(customer);
                    return ResponseEntity.status(200).body(updatedCustomer);
                })
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @Override
    public ResponseEntity<?> deleteCustomer(Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    if (accountService.hasActiveAccounts(id)) {
                        return ResponseEntity.status(409).body("This customer cannot be deleted, customer has active accounts.");
                    }
                    customerRepository.deleteById(id);
                    return ResponseEntity.status(200).body("Customer successfully deleted");
                })
                .orElseGet(() -> {
                    return ResponseEntity.status(404).body("Customer not found");
                });
    }
}
