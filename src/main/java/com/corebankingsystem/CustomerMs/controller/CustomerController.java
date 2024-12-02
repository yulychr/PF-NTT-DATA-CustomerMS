package com.corebankingsystem.CustomerMs.controller;

import com.corebankingsystem.CustomerMs.model.Customer;
import com.corebankingsystem.CustomerMs.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customers/")
public class CustomerController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private CustomerService customerService;

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
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    //Delete - Delete a customer if they do not have an active account
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            try {
                String url = "http://localhost:8087/accounts/customer/" + id ;
                List<?> response = restTemplate.exchange(url, HttpMethod.GET, null, List.class).getBody();
                System.out.println("Response: " + response);
                if (response != null && !response.isEmpty()) {
                    return ResponseEntity.status(409).body("This customer cannot be deleted, customer has active accounts.");
                }
                customerService.deleteCustomer(id);
                String message = "Customer successfully deleted";
                return ResponseEntity.status(200).body(message);
            } catch (HttpClientErrorException e) {
                return ResponseEntity.status(500).body("Internal server error. ");
            }
        }
        return ResponseEntity.status(404).body("Customer not found");
    }

}
