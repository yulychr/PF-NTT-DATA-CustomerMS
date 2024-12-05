package com.corebankingsystem.CustomerMs;

import com.corebankingsystem.CustomerMs.model.Customer;
import com.corebankingsystem.CustomerMs.service.CustomerValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerValidationServiceTest {

    @InjectMocks
    private CustomerValidationService customerValidationService;

    @BeforeEach
    void setUp() {
        customerValidationService = new CustomerValidationService();
    }

    @Test
    void testValidateCustomerFields_DniEmpty_ShouldReturnError() {
        Customer customer = new Customer();
        customer.setDni("");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        Map<String, String> errors = customerValidationService.validateCustomerFields(customer);
        assertTrue(errors.containsKey("dni"));
        assertEquals("El DNI debe tener 8 dígitos", errors.get("dni"));
    }

    @Test
    void testValidateCustomerFields_InvalidEmail_ShouldReturnError() {
        Customer customer = new Customer();
        customer.setDni("12345678");
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setEmail("invalid-email");
        Map<String, String> errors = customerValidationService.validateCustomerFields(customer);
        assertTrue(errors.containsKey("email"));
        assertEquals("Formato de correo electrónico inválido", errors.get("email"));
    }

    @Test
    void testValidateCustomerFields_ValidCustomer_ShouldReturnNoErrors() {
        Customer customer = new Customer();
        customer.setDni("12345678");
        customer.setFirstName("Alice");
        customer.setLastName("Smith");
        customer.setEmail("alice.smith@example.com");
        Map<String, String> errors = customerValidationService.validateCustomerFields(customer);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testValidateCustomerFields_FirstNameEmpty_ShouldReturnError() {
        Customer customer = new Customer();
        customer.setDni("12345678");
        customer.setFirstName("");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        Map<String, String> errors = customerValidationService.validateCustomerFields(customer);
        assertTrue(errors.containsKey("firstName"));
        assertEquals("El nombre es obligatorio", errors.get("firstName"));
    }
}
