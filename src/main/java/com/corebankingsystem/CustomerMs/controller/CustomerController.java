package com.corebankingsystem.CustomerMs.controller;

import com.corebankingsystem.CustomerMs.model.Customer;
import com.corebankingsystem.CustomerMs.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers/")
public class CustomerController {

    @Autowired
    public CustomerService customerService;

    //Post - Create a new customer
    @PostMapping
    public ResponseEntity<Object> createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    //Get - Retrieves a list of all customers in the system.
    @GetMapping
        public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getCustomers();
        return ResponseEntity.status(200).body(customers);
    }

    // Get {id]  - Retrieve a customer details by id
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
    }

    //Put {id} - Updates the details of an existing customer identified by the `id` provided in the path.
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    //Delete - Delete a customer if they do not have an active account
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }
}
