package com.corebankingsystem.CustomerMs.service.impl;

import com.corebankingsystem.CustomerMs.model.Customer;
import com.corebankingsystem.CustomerMs.repository.CustomerRepository;
import com.corebankingsystem.CustomerMs.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseEntity<Object> createCustomer(Customer customer) {
        Map<String, String> errors = new HashMap<>();
        if (isNullOrEmpty(customer.getFirstName())){ errors.put("firstName", "El nombre es obligatorio");}
        if (isNullOrEmpty(customer.getLastName())) {errors.put("lastName", "El apellido es obligatorio");}
        if (isNullOrEmpty(customer.getDni())) {
            errors.put("dni", "El DNI es obligatorio");
        } else if (!customer.getDni().matches("\\d{8}")) {
            errors.put("dni", "El DNI debe tener 8 dígitos");
        }
        if (isNullOrEmpty(customer.getEmail())) {
            errors.put("email", "El email es obligatorio");
        } else if (!customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("email", "Formato de correo electrónico inválido");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Optional<Customer> dni_already_exists = customerRepository.findByDni(customer.getDni());
        if (dni_already_exists.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "El DNI ya existe");
            return ResponseEntity.status(409).body(response);
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
    public ResponseEntity<Customer> updateCustomer(Long id, Customer customer) {
        Optional<Customer> existingCustomer = getCustomerById(id);
        if (existingCustomer.isPresent()) {
            customer.setId(id);
            Customer updatedCustomer = customerRepository.save(customer);
            return ResponseEntity.status(200).body(updatedCustomer);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @Override
    public ResponseEntity<Object> deleteCustomer(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            // Si el cliente existe, lo eliminamos
            customerRepository.deleteById(id);
            return ResponseEntity.status(200).body("Customer successfully deleted");
        } else {
            // Si el cliente no existe, devolvemos una respuesta 404
            return ResponseEntity.status(404).body("Customer not found");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
